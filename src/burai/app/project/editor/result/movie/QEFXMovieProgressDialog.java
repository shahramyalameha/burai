/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.movie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import burai.app.QEFXMain;

public class QEFXMovieProgressDialog extends Dialog<File> implements Initializable {

    private File movieFile;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    public QEFXMovieProgressDialog(File movieFile) {
        super();

        if (movieFile == null) {
            throw new IllegalArgumentException("movieFile is null.");
        }

        this.movieFile = movieFile;

        DialogPane dialogPane = this.getDialogPane();
        QEFXMain.initializeStyleSheets(dialogPane.getStylesheets());
        QEFXMain.initializeDialogOwner(this);

        this.setResizable(false);
        this.setTitle("Movie: " + this.movieFile.getName());
        dialogPane.getButtonTypes().clear();

        Node node = null;
        try {
            node = this.createContent();
        } catch (Exception e) {
            node = new Label("ERROR: cannot show QEFXMovieProgressDialog.");
            e.printStackTrace();
        }

        dialogPane.setContent(node);

        this.setResultConverter(buttonType -> {
            return this.movieFile;
        });
    }

    private Node createContent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("QEFXMovieProgressDialog.fxml"));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupProgressLabel();
        this.setupProgressIndicator();
    }

    private void setupProgressLabel() {
        if (this.progressLabel == null) {
            return;
        }

        String text = this.progressLabel.getText();
        text = text == null ? "" : text;
        text = text + System.lineSeparator() + this.movieFile.getPath();
        this.progressLabel.setText(text);
    }

    private void setupProgressIndicator() {
        if (this.progressIndicator == null) {
            return;
        }

        this.progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    public void setProgress(double value) {
        if (this.progressIndicator != null) {
            //this.progressIndicator.setProgress(value);
        }
    }

    public void showProgress() {
        this.setProgress(0.0);
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().clear();
        this.show();
    }

    public void hideProgress() {
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().clear();
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        this.hide();
    }
}
