/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.movie;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import burai.app.project.QEFXProjectController;
import burai.app.project.editor.result.QEFXResultEditorController;
import burai.app.project.viewer.result.movie.QEFXMovieViewerController;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;
import burai.project.property.ProjectGeometry;

public class QEFXMovieEditorController extends QEFXResultEditorController<QEFXMovieViewerController> {

    private static final double GRAPHIC_SIZE = 20.0;
    private static final String GRAPHIC_CLASS = "piclight-button";

    @FXML
    private Button movieButton;

    @FXML
    private TextField numberField;

    @FXML
    private Label totalLabel;

    @FXML
    private Button exportButton;

    @FXML
    private TextArea atomArea;

    public QEFXMovieEditorController(QEFXProjectController projectController, QEFXMovieViewerController viewerController) {
        super(projectController, viewerController);
    }

    @Override
    protected void setupFXComponents() {
        this.setupMovieButton();
        this.setupNumberField();
        this.setupExportButton();
        this.setupAtomArea();
    }

    private void setupMovieButton() {
        if (this.movieButton == null) {
            return;
        }

        this.movieButton.setText("");
        this.movieButton.setGraphic(SVGLibrary.getGraphic(SVGData.MOVIE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.movieButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupNumberField() {
        if (this.numberField == null) {
            return;
        }

        // TODO
    }

    private void setupExportButton() {
        if (this.exportButton == null) {
            return;
        }

        this.exportButton.getStyleClass().add(GRAPHIC_CLASS);
        this.exportButton.setGraphic(SVGLibrary.getGraphic(SVGData.EXPORT, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.exportButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupAtomArea() {
        if (this.atomArea == null) {
            return;
        }

        if (this.viewerController == null) {
            return;
        }

        this.viewerController.setOnGeometryShown((i, n, geometry) -> {
            if (n > 0) {
                if (this.totalLabel != null) {
                    this.totalLabel.setText(Integer.toString(n));
                }
            } else {
                if (this.totalLabel != null) {
                    this.totalLabel.setText("#");
                }
                return;
            }

            if (0 <= i && i < n) {
                if (this.numberField != null) {
                    this.numberField.setText(Integer.toString(i));
                }
            } else {
                if (this.numberField != null) {
                    this.numberField.setText("#");
                }
                return;
            }

            String strGeometry = this.geometryToString(geometry);
            if (strGeometry != null && !(strGeometry.isEmpty())) {
                if (this.atomArea != null) {
                    this.atomArea.setText(strGeometry);
                }
            } else {
                if (this.atomArea != null) {
                    this.atomArea.setText("");
                }
                return;
            }
        });
    }

    private String geometryToString(ProjectGeometry geometry) {
        if (geometry == null) {
            return null;
        }

        // TODO
        return null;
    }
}
