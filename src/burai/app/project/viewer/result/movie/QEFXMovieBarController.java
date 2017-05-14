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
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class QEFXMovieBarController extends QEFXAppController {

    private static final double INSETS_SIZE = 4.0;

    private static final double GRAPHIC_SIZE_LARGE = 20.0;
    private static final double GRAPHIC_SIZE_SMALL = 20.0;
    private static final String GRAPHIC_CLASS = "piclight-button";
    private static final String GRAPHIC_CLASS_PLAY = "picplay-button";
    private static final String GRAPHIC_CLASS_PAUSE = "picpause-button";

    private QEFXMovieViewerController viewerController;

    @FXML
    private BorderPane basePane;

    @FXML
    private Button playButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private Button firstButton;

    @FXML
    private Button lastButton;

    @FXML
    private Slider movieSlider;

    public QEFXMovieBarController(QEFXProjectController projectController, QEFXMovieViewerController viewerController) {
        super(projectController == null ? null : projectController.getMainController());

        if (viewerController == null) {
            throw new IllegalArgumentException("viewerController is null.");
        }

        this.viewerController = viewerController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBasePane();
        this.setupPlayButton();
        this.setupNextButton();
        this.setupPrevButton();
        this.setupFirstButton();
        this.setupLastButton();
        this.setupMovieSlider();
    }

    private void setupBasePane() {
        if (this.basePane == null) {
            return;
        }

        StackPane.setMargin(this.basePane, new Insets(INSETS_SIZE));
        StackPane.setAlignment(this.basePane, Pos.BOTTOM_CENTER);
    }

    private void setupPlayButton() {
        if (this.playButton == null) {
            return;
        }

        this.playButton.setText("");
        this.playButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.MOVIE_PLAY, GRAPHIC_SIZE_LARGE, null, GRAPHIC_CLASS_PLAY));

        this.playButton.setTooltip(new Tooltip("play"));

        this.playButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupNextButton() {
        if (this.nextButton == null) {
            return;
        }

        this.nextButton.setText("");
        this.nextButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.MOVIE_NEXT, GRAPHIC_SIZE_LARGE, null, GRAPHIC_CLASS));

        this.nextButton.setTooltip(new Tooltip("next"));
        this.nextButton.setOnAction(event -> this.viewerController.showNextGeometry());
    }

    private void setupPrevButton() {
        if (this.prevButton == null) {
            return;
        }

        this.prevButton.setText("");
        this.prevButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.MOVIE_PREVIOUS, GRAPHIC_SIZE_LARGE, null, GRAPHIC_CLASS));

        this.prevButton.setTooltip(new Tooltip("previous"));
        this.prevButton.setOnAction(event -> this.viewerController.showPreviousGeometry());
    }

    private void setupFirstButton() {
        if (this.firstButton == null) {
            return;
        }

        this.firstButton.setText("");
        this.firstButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.MOVIE_FIRST, GRAPHIC_SIZE_SMALL, null, GRAPHIC_CLASS));

        this.firstButton.setTooltip(new Tooltip("first"));
        this.firstButton.setOnAction(event -> this.viewerController.showFirstGeometry());
    }

    private void setupLastButton() {
        if (this.lastButton == null) {
            return;
        }

        this.lastButton.setText("");
        this.lastButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.MOVIE_LAST, GRAPHIC_SIZE_SMALL, null, GRAPHIC_CLASS));

        this.lastButton.setTooltip(new Tooltip("last"));
        this.lastButton.setOnAction(event -> this.viewerController.showLastGeometry());
    }

    private void setupMovieSlider() {
        if (this.movieSlider == null) {
            return;
        }

        this.movieSlider.valueProperty().addListener(o -> {
            double value = this.movieSlider.getValue();
            this.viewerController.showGeometry(value);
        });
    }

    protected void disableNextButtons(boolean disable) {
        if (this.nextButton != null) {
            this.nextButton.setDisable(disable);
        }

        if (this.lastButton != null) {
            this.lastButton.setDisable(disable);
        }
    }

    protected void disablePreviousButtons(boolean disable) {
        if (this.prevButton != null) {
            this.prevButton.setDisable(disable);
        }

        if (this.firstButton != null) {
            this.firstButton.setDisable(disable);
        }
    }

    protected void setSliderValue(double value) {
        if (this.movieSlider != null) {
            double value_ = Math.min(Math.max(0.0, value), 1.0);
            this.movieSlider.setValue(value_);
        }
    }
}
