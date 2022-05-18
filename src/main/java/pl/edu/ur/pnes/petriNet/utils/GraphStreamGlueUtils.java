package pl.edu.ur.pnes.petriNet.utils;

import javafx.geometry.Point3D;
import org.graphstream.ui.geom.Point3;

public class GraphStreamGlueUtils {
    public static Point3D position(Point3 value) {
        return new Point3D(value.x, value.y, value.z);
    }

    public static Point3D position(double[] value) {
        return new Point3D(value[0], value[1], value[2]);
    }
}
