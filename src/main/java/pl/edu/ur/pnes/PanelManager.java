package pl.edu.ur.pnes;

import pl.edu.ur.pnes.panels.CustomPanel;

import java.io.IOException;

public class PanelManager {

    public static void addCenterPanel(CustomPanel panelClass) {
        addCenterPanel(panelClass, panelClass.getClass().getSimpleName());
    }

    public static void addCenterPanel(CustomPanel panelClass, String tabName) {
        try {
            MainApp.mainController.centerTabPane.addTab(tabName, panelClass.loadPanel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLeftPanel(CustomPanel panelClass) {
        addLeftPanel(panelClass, panelClass.getClass().getSimpleName());
    }

    public static void addLeftPanel(CustomPanel panelClass, String tabName) {
        try {
            MainApp.mainController.leftTabPane.addTab(tabName, panelClass.loadPanel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRightPanel(CustomPanel panelClass) {
        addRightPanel(panelClass, panelClass.getClass().getSimpleName());
    }

    public static void addRightPanel(CustomPanel panelClass, String tabName) {
        try {
            MainApp.mainController.rightTabPane.addTab(tabName, panelClass.loadPanel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
