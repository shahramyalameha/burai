/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import java.net.URL;
import java.util.ResourceBundle;

import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.atoms.viewer.AtomsViewer;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class QEFXDesignerWindowController extends QEFXAppController {

    private static final double INSETS_SIZE = 4.0;

    private static final double GRAPHIC_SIZE = 22.0;
    private static final String GRAPHIC_CLASS = "designer-button";

    private AtomsViewer atomsViewer;

    private boolean maximized;
    private WindowMaximized onWindowMaximized;

    @FXML
    private Group baseGroup;

    @FXML
    private Pane mainPane;

    @FXML
    private Button scaleButton;

    public QEFXDesignerWindowController(QEFXProjectController projectController, AtomsViewer atomsViewer) {
        super(projectController == null ? null : projectController.getMainController());

        if (atomsViewer == null) {
            throw new IllegalArgumentException("atomsViewer is null.");
        }

        this.atomsViewer = atomsViewer;

        this.maximized = false;
        this.onWindowMaximized = null;
    }

    public void setWidth(double width) {
        if (width <= 0.0) {
            return;
        }

        if (this.mainPane != null) {
            this.mainPane.setPrefWidth(width);
        }
    }

    public void setHeight(double height) {
        if (height <= 0.0) {
            return;
        }

        if (this.mainPane != null) {
            this.mainPane.setPrefHeight(height);
        }
    }

    public void setOnWindowMaximized(WindowMaximized onWindowMaximized) {
        this.onWindowMaximized = onWindowMaximized;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBaseGroup();
        this.setupMainPane();
        this.setupScaleButton();
    }

    private void setupBaseGroup() {
        if (this.baseGroup == null) {
            return;
        }

        StackPane.setMargin(this.baseGroup, new Insets(INSETS_SIZE));
        StackPane.setAlignment(this.baseGroup, Pos.BOTTOM_LEFT);
    }

    private void setupMainPane() {
        if (this.mainPane == null) {
            return;
        }

        this.mainPane.setPrefWidth(0.0);
        this.mainPane.setPrefHeight(0.0);
        this.mainPane.getChildren().clear();

        if (this.atomsViewer != null) {
            this.atomsViewer.bindSceneTo(this.mainPane);
            this.mainPane.getChildren().add(this.atomsViewer);
        }
    }

    private void setupScaleButton() {
        if (this.scaleButton == null) {
            return;
        }

        this.scaleButton.setText("");
        this.scaleButton.getStyleClass().add(GRAPHIC_CLASS);
        this.updateScaleButton(!this.maximized);

        this.scaleButton.setOnAction(event -> {
            this.maximized = !this.maximized;

            if (this.onWindowMaximized != null) {
                this.onWindowMaximized.onWindowMaximized(this.maximized);
            }

            this.updateScaleButton(!this.maximized);
        });
    }

    private void updateScaleButton(boolean toMaximize) {
        if (this.scaleButton == null) {
            return;
        }

        if (toMaximize) {
            this.scaleButton.setTooltip(new Tooltip("maximize"));
            this.scaleButton.setGraphic(
                    SVGLibrary.getGraphic(SVGData.MAXIMIZE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        } else {
            this.scaleButton.setTooltip(new Tooltip("minimize"));
            this.scaleButton.setGraphic(
                    SVGLibrary.getGraphic(SVGData.MINIMIZE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));
        }
    }
}
