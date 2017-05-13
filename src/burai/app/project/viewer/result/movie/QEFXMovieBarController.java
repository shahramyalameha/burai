/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result.movie;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class QEFXMovieBarController extends QEFXAppController {

    private static final double INSETS_SIZE = 4.0;

    private static final double GRAPHIC_SIZE = 20.0;
    private static final String GRAPHIC_CLASS = "piclight-button";

    private QEFXProjectController projectController;

    @FXML
    private BorderPane basePane;

    @FXML
    private Button startButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private Button firstButton;

    @FXML
    private Button lastButton;

    public QEFXMovieBarController(QEFXProjectController projectController) {
        super(projectController == null ? null : projectController.getMainController());

        if (projectController == null) {
            throw new IllegalArgumentException("projectController is null.");
        }

        this.projectController = projectController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBasePane();
        this.setupStartButton();
        this.setupNextButton();
        this.setupPrevButton();
        this.setupFirstButton();
        this.setupLastButton();
    }

    private void setupBasePane() {
        if (this.basePane == null) {
            return;
        }

        StackPane.setMargin(this.basePane, new Insets(INSETS_SIZE));
        StackPane.setAlignment(this.basePane, Pos.BOTTOM_CENTER);
    }

    private void setupStartButton() {
        if (this.startButton == null) {
            return;
        }

        this.startButton.setText("");
        this.startButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CLOSE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.startButton.setTooltip(new Tooltip("start"));

        this.startButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupNextButton() {
        if (this.nextButton == null) {
            return;
        }

        this.nextButton.setText("");
        this.nextButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CLOSE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.nextButton.setTooltip(new Tooltip("next"));

        this.nextButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupPrevButton() {
        if (this.prevButton == null) {
            return;
        }

        this.prevButton.setText("");
        this.prevButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CLOSE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.prevButton.setTooltip(new Tooltip("previous"));

        this.prevButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupFirstButton() {
        if (this.firstButton == null) {
            return;
        }

        this.firstButton.setText("");
        this.firstButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CLOSE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.firstButton.setTooltip(new Tooltip("first"));

        this.firstButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupLastButton() {
        if (this.lastButton == null) {
            return;
        }

        this.lastButton.setText("");
        this.lastButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CLOSE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.lastButton.setTooltip(new Tooltip("last"));

        this.lastButton.setOnAction(event -> {
            // TODO
            });
    }

}
