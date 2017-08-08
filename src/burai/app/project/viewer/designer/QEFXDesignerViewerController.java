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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class QEFXDesignerViewerController extends QEFXAppController {

    private static final long ANIMATION_TIME1 = 550L;
    private static final long ANIMATION_TIME2 = 650L;

    private static final double WIN_SCALE_WIDTH = 0.32;
    private static final double WIN_SCALE_HEIGHT = 0.32;

    private QEFXProjectController projectController;

    private boolean dualMode;
    private boolean modeChanging;

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
        this.modeChanging = false;

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
        this.resizePanes(this.dualMode ? 1.0 : 0.0, true);
    }

    private void setupBasePane() {
        if (this.basePane == null) {
            return;
        }

        this.basePane.widthProperty().addListener(o -> {
            if (!this.modeChanging) {
                this.resizePanes(this.dualMode ? 1.0 : 0.0, true);
            }
        });

        this.basePane.heightProperty().addListener(o -> {
            if (!this.modeChanging) {
                this.resizePanes(this.dualMode ? 1.0 : 0.0, true);
            }
        });
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

        this.addExclusiveNode(dualNode);

        this.dualWindow.setOnWindowMaximized(maximized -> {
            if (maximized) {
                this.changeDualMode(true);
            } else {
                this.changeDualMode(false);
            }
        });
    }

    private void changeDualMode(boolean toDual) {
        this.dualMode = toDual;

        this.modeChanging = true;

        if (this.atomsViewerPrim != null) {
            this.atomsViewerPrim.startExclusiveMode();
        }

        DoubleProperty rateProperty = new SimpleDoubleProperty(0.0);
        rateProperty.addListener(o -> {
            double rate = rateProperty.get();
            rate = Math.sin(0.5 * Math.PI * rate);

            if (toDual) {
                this.resizePanes(rate, false);
            } else {
                this.resizePanes(1.0 - rate, false);
            }
        });

        KeyValue keyValue = new KeyValue(rateProperty, 1.0);
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(toDual ? ANIMATION_TIME1 : ANIMATION_TIME2), keyValue);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event -> {
            if (this.atomsViewerPrim != null) {
                this.atomsViewerPrim.stopExclusiveMode();
            }

            this.modeChanging = false;
        });

        timeline.playFromStart();
    }

    private void resizePanes(double rate, boolean async) {
        if (this.basePane == null) {
            return;
        }

        double width = this.basePane.getWidth();
        double height = this.basePane.getHeight();

        if (this.primPane != null) {
            double scale = 1.0 - 0.5 * rate;
            this.primPane.setPrefWidth(scale * width);
            this.primPane.setPrefHeight(scale * height);
        }

        if (this.dualPane != null) {
            double scale = 0.5 * rate;
            this.dualPane.setPrefWidth(scale * width);
            this.dualPane.setPrefHeight(scale * height);
        }

        if (this.dualWindow != null) {
            double scale1 = (0.5 - WIN_SCALE_WIDTH) * rate + WIN_SCALE_WIDTH;
            double scale2 = (0.5 - WIN_SCALE_HEIGHT) * rate + WIN_SCALE_HEIGHT;

            if (!async) {
                this.dualWindow.setWidth(scale1 * width);
                this.dualWindow.setHeight(scale2 * height);

            } else {
                if (this.dualWindowThread != null) {
                    this.dualWindowThread.runLater(() -> {
                        this.dualWindow.setWidth(scale1 * width);
                        this.dualWindow.setHeight(scale2 * height);
                    });
                }
            }
        }
    }
}
