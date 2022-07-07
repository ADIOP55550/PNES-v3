package pl.edu.ur.pnes.parser;

import javafx.geometry.Point3D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import pl.edu.ur.pnes.editor.Session;
import pl.edu.ur.pnes.parser.utils.ParserUtils;
import pl.edu.ur.pnes.petriNet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.ur.pnes.petriNet.netTypes.NetType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static pl.edu.ur.pnes.MainApp.mainController;

public class PnmlParser {
    //temp only for visual effect ;))))
    private final static Logger logger = LogManager.getLogger();
    public static void parsePnmlFile(File pnmlFile) {
        DocumentBuilder builder = null;
        Document document = null;
        Element rootElement;

        if(!pnmlFile.getName().contains(".pnml")){//check file can be pnml file
            ParserUtils.parserAlert(Alert.AlertType.ERROR, "Not supported file type", null, ButtonType.CLOSE);
            return;
        }

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(pnmlFile);
        } catch (SAXParseException spe) {
            ParserUtils.parserAlert(Alert.AlertType.ERROR, spe.getMessage(), "Parsing error" + ", line " + spe.getLineNumber ()
                    + ", column: " + spe.getColumnNumber()
                    + ", uri " + spe.getSystemId (), ButtonType.CLOSE);
            return;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            ParserUtils.parserAlert(Alert.AlertType.ERROR, "File can't be read", "PNML file parser error", ButtonType.CLOSE);
            return;
        }
        document.getDocumentElement().normalize();
        rootElement = document.getDocumentElement();

        if(!rootElement.getNodeName().equals(ParserSupportedElements.ROOT_PNML.getValue())) { //if root is not pnml tag
            ParserUtils.parserAlert(Alert.AlertType.WARNING, "Root element error", null, ButtonType.CLOSE);
            return;
        }

        Node net = rootElement.getElementsByTagName(ParserSupportedElements.NET.getValue()).item(0);
        if (!net.hasChildNodes()) return;//check pnml contains net
        Node page = rootElement.getElementsByTagName(ParserSupportedElements.PAGE.getValue()).item(0);
        if (!page.hasChildNodes()) return;

        PetriNet petriNet = new PetriNet();
        petriNet.setNetType(NetType.PN);//TODO other types
        NodeList pageElements = page.getChildNodes();
        for (int i = 0; i < pageElements.getLength(); i++) {
            Node pageItem = pageElements.item(i);
            if(pageItem.getNodeName().equals(ParserSupportedElements.PLACE.getValue())) createPlace(pageItem, petriNet);
            else if(pageItem.getNodeName().equals(ParserSupportedElements.TRANSITION.getValue()))createTransition(pageItem, petriNet);
            else if(pageItem.getNodeName().equals(ParserSupportedElements.ARC.getValue())) createArc(pageItem, petriNet);//ARCS OUT OF THIS LOOP
            //TODO other tags in page
        }

        try {
            Session netSession = new Session(petriNet);
            netSession.fileProperty().set(pnmlFile);
            mainController.open(netSession);
            mainController.setFocusedSession(netSession);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void createPlace(Node place, PetriNet net){
        Place netPlace = new Place(net);
        netPlace.setName(place.getAttributes().getNamedItem("id").getNodeValue());
        NodeList placeItems = place.getChildNodes();
        for (int i = 0; i < placeItems.getLength(); i++) {
            Node item = placeItems.item(i);
            if (item.getNodeName().equals(ParserSupportedElements.GRAPHICS.getValue())) netPlace.setPosition(getPositionPNML(item));
            if (item.getNodeName().equals(ParserSupportedElements.INITIAL_MARKING.getValue())
//                    && item.getFirstChild().getNodeValue() != null
//                    && !item.getFirstChild().getNodeValue().isBlank()
//                    && !item.getFirstChild().getNodeValue().isEmpty()
                ){
                logger.debug(item.getNodeValue());
                logger.debug(item.getTextContent());
                netPlace.setTokensAs(Integer.class, Integer.valueOf(item.getTextContent().trim()));
                //TODO tokens for other net types
            }
            //TODO: for graphics/initialMarking and toolspecific
        }
        net.addElement(netPlace);
    }
    private static void createTransition(Node transition, PetriNet net){
        Transition netTransition = new Transition(net);
        netTransition.setName(transition.getAttributes().getNamedItem("id").getNodeValue());
        NodeList placeItems = transition.getChildNodes();
        for (int i = 0; i < placeItems.getLength(); i++) {
            Node item = placeItems.item(i);
            if (item.getNodeName().equals(ParserSupportedElements.GRAPHICS.getValue())) netTransition.setPosition(getPositionPNML(item));
            //TODO: for tool-specific
        }
        net.addElement(netTransition);
    }
    private static void createArc(Node arc, PetriNet net){
        NetElement source = net.allElementsStream().filter(netElement -> arc.getAttributes().getNamedItem(ParserSupportedElements.SOURCE.getValue())
                        .getNodeValue().equals(netElement.getName())).findFirst().orElse(null);
        NetElement target = net.allElementsStream().filter(netElement -> arc.getAttributes().getNamedItem(ParserSupportedElements.TARGET.getValue())
                        .getNodeValue().equals(netElement.getName())).findFirst().orElse(null);
        if (source != null && target != null) {
            Arc netArc = new Arc(net, (pl.edu.ur.pnes.petriNet.Node) source, (pl.edu.ur.pnes.petriNet.Node) target);
            netArc.setName(arc.getAttributes().getNamedItem("id").getNodeValue());
            net.addElement(netArc);
        }
    }
    private static Point3D getPositionPNML(Node graphics){
        NodeList graphicsItems = graphics.getChildNodes();
        for (int i = 0; i < graphicsItems.getLength(); i++) { //for position and dimension
            Node item = graphicsItems.item(i);
            if (item.getNodeName().equals(ParserSupportedElements.POSITION.getValue())){
                double x = 0;
                double y = 0;
                if(item.getAttributes().item(0).getNodeName().equals(ParserSupportedElements.X.getValue())){
                    x = Double.parseDouble(item.getAttributes().item(0).getNodeValue());
                    y = Double.parseDouble(item.getAttributes().item(1).getNodeValue());
                    return new Point3D(x, y, 0);
                } //if y is first arg TODO
            }
        }
        return Point3D.ZERO;
    }
}
