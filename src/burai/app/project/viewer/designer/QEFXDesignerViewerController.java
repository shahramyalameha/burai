/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.atoms.AtomsAction;
import burai.atoms.design.Design;
import burai.atoms.model.Cell;
import burai.atoms.viewer.AtomsViewer;
import burai.atoms.viewer.NodeWrapper;
import burai.com.fx.FXBufferedThread;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class QEFXDesignerViewerController extends QEFXAppController {

    private static final double WIN_SCALE_WIDTH = 0.32;
    private static final double WIN_SCALE_HEIGHT = 0.32;

    private QEFXProjectController projectController;

    private boolean dualMode;

    private AtomsViewer atomsViewerPrim;

    private AtomsViewer atomsViewerDual;

    @FXML
    private StackPane basePane;

    @FXML
    private Pane primPane;

    @FXML
    private Pane dualPane;

    private QEFXDesignerWindow dualWindow;

    private FXBufferedThread dualWindowThread;

    public QEFXDesignerViewerController(QEFXProjectController projectController, Cell cell) {
        super(projectController == null ? null : projectController.getMainController());

        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.projectController = projectController;

        this.dualMode = false;

        this.atomsViewerPrim = new AtomsViewer(cell, AtomsAction.getAtomsViewerSize(), true);
        this.atomsViewerDual = new AtomsViewer(cell, AtomsAction.getAtomsViewerSize(), true);
        this.atomsViewerPrim.linkAtomsViewer(this.atomsViewerDual);

        try {
            this.dualWindow = new QEFXDesignerWindow(this.projectController, this.atomsViewerDual);
        } catch (IOException e) {
            this.dualWindow = null;
            e.printStackTrace();
        }

        this.dualWindowThread = null;
        if (this.dualWindow != null) {
            this.dualWindowThread = new FXBufferedThread(true);
        }
    }

    public void addExclusiveNode(Node node) {
        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.addExclusiveNode(node);
        }
    }

    public void addExclusiveNode(NodeWrapper nodeWrapper) {
        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.addExclusiveNode(nodeWrapper);
        }
    }

    public Design getDesign() {
        return this.atomsViewerPrim == null ? null : this.atomsViewerPrim.getDesign();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBasePane();
        this.setupPrimPane();
        this.setupDualPane();
        this.setupDualWindow();
        this.resizePanes();
    }

    private void setupBasePane() {
        if (this.basePane == null) {
            return;
        }

        this.basePane.widthProperty().addListener(o -> this.resizePanes());
        this.basePane.heightProperty().addListener(o -> this.resizePanes());
    }

    private void resizePanes() {
        if (this.basePane == null) {
            return;
        }

        double width = this.basePane.getWidth();
        double height = this.basePane.getHeight();

        if (this.primPane != null) {
            this.primPane.setPrefWidth(this.dualMode ? (0.5 * width) : width);
            this.primPane.setPrefHeight(this.dualMode ? (0.5 * height) : height);
        }

        if (this.dualPane != null) {
            this.dualPane.setPrefWidth(this.dualMode ? (0.5 * width) : 0.0);
            this.dualPane.setPrefHeight(this.dualMode ? (0.5 * height) : 0.0);
        }

        if (this.dualWindow != null && this.dualWindow != null) {
            this.dualWindowThread.runLater(() -> {
                this.dualWindow.setWidth(this.dualMode ? (0.5 * width) : (WIN_SCALE_WIDTH * width));
                this.dualWindow.setHeight(this.dualMode ? (0.5 * height) : (WIN_SCALE_HEIGHT * height));
            });
        }
    }

    private void setupPrimPane() {
        if (this.primPane == null) {
            return;
        }

        this.primPane.getChildren().clear();

        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.bindSceneTo(this.primPane);
            this.primPane.getChildren().add(this.atomsViewerPrim);
        }
    }

    private void setupDualPane() {
        if (this.dualPane == null) {
            return;
        }

        this.dualPane.getChildren().clear();
    }

    private void setupDualWindow() {
        if (this.dualWindow == null) {
            return;
        }

        Node dualNode = this.dualWindow.getNode();
        if (this.basePane != null) {
            this.basePane.getChildren().add(dualNode);
        }

        this.dualWindow.setOnWindowMaximized(maximized -> {
            if (maximized) {
                this.toBeDualMode();
            } else {
                this.toBeSingleMode();
            }
        });
    }

    private void toBeDualMode() {
        this.dualMode = true;

        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.startExclusiveMode();
        }

        // TODO
        this.resizePanes();

        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.stopExclusiveMode();
        }
    }

    private void toBeSingleMode() {
        this.dualMode = false;

        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.startExclusiveMode();
        }

        // TODO
        this.resizePanes();

        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.stopExclusiveMode();
        }
    }
}
