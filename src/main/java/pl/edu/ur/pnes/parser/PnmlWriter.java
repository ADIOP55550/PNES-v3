package pl.edu.ur.pnes.parser;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.edu.ur.pnes.MainController;
import pl.edu.ur.pnes.parser.utils.ParserUtils;
import pl.edu.ur.pnes.petriNet.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PnmlWriter {
    private final static Logger logger = LogManager.getLogger();
    private DocumentBuilder builder = null;
    private Document document = null;
    public void generatePnmlFromNet(PetriNet petriNet) {
        if(petriNet.getAllNodesStream().findAny().isEmpty()) {
            ParserUtils.parserAlert(Alert.AlertType.INFORMATION, "Your net is empty", null, ButtonType.CLOSE);
            return;
        }

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.newDocument();
        }  catch (ParserConfigurationException e) {
            ParserUtils.parserAlert(Alert.AlertType.ERROR, "Parser error", null, ButtonType.CLOSE);
            return;
        }
        Element rootElement = document.createElement("pnml");
        rootElement.setAttribute("xmlns", "https://www.pnml.org/version-2009/grammar/pnmlcoremodel.rng");
        document.appendChild(rootElement);

        Element netElement = document.createElement("net");
        rootElement.appendChild(netElement);

        Element pageElement = document.createElement("page");
        netElement.appendChild(pageElement);


        petriNet.getPlaces().forEach(place -> {
            pageElement.appendChild(generatePlaceElement(place));
        });
        petriNet.getTransitions().forEach(transition -> {
            pageElement.appendChild(generateTransitionElement(transition));
        });
        petriNet.getArcs().forEach(arc -> {
            pageElement.appendChild(generateArcElement(arc));
        });

        printPnmlDebug(document); //temp----------------------------------------------------------------------
    }
    private Element generatePlaceElement(Place place){
        Element placeElement = document.createElement("place");
        placeElement.setAttribute("id", place.getName());
        placeElement.appendChild(generateGraphicsElement(place));
        return placeElement;
    }
    private Element generateTransitionElement(Transition transition){
        Element transitionElement = document.createElement("transition");
        transitionElement.setAttribute("id", transition.getName());
        transitionElement.appendChild(generateGraphicsElement(transition));
        return transitionElement;
    }
    private Element generateArcElement(Arc arc){
        Element arcElement = document.createElement("arc");
        arcElement.setAttribute("id", arc.getName());
        arcElement.setAttribute("source", arc.input.getName());
        arcElement.setAttribute("target", arc.output.getName());
        return arcElement;
    }
    private Element generateGraphicsElement(Node petriNetNode){
        Element graphicsElement = document.createElement("graphics");
        Element positionElement = document.createElement("position");
        positionElement.setAttribute(ParserSupportedElements.X.getValue(), String.valueOf(petriNetNode.getPosition().getX()));
        positionElement.setAttribute(ParserSupportedElements.Y.getValue(), String.valueOf(petriNetNode.getPosition().getY()));
//        Element dimensionElement
        graphicsElement.appendChild(positionElement);
        return graphicsElement;
    }
    public File saveToNewFile(Window parentScene) { //also temp :)))
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PetriNet to PNML file");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNML", "*.pnml"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showSaveDialog(parentScene);
        DOMSource dom = new DOMSource(document);
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult streamResult = new StreamResult(new FileOutputStream(file));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(dom, new StreamResult(System.out));
            transformer.transform(dom, streamResult);
        } catch (TransformerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
    public void saveExistedFile(File file) { //only for presentation 06.06 TODO
        DOMSource dom = new DOMSource(document);
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult streamResult = new StreamResult(new FileOutputStream(file));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(dom, new StreamResult(System.out));
            transformer.transform(dom, streamResult);
        } catch (TransformerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void printPnmlDebug(Document document){ //temp for debug
        DOMSource dom = new DOMSource(document);
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult streamResult = new StreamResult(new FileOutputStream("/home/dawid/Pulpit/x.pnml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(dom, new StreamResult(System.out));
            transformer.transform(dom, streamResult);
        } catch (TransformerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
