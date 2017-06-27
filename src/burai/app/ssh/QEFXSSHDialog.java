/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.ssh;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import burai.app.QEFXMain;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class QEFXSSHDialog extends Dialog<ButtonType> implements Initializable {

    private static final double GRAPHIC_SIZE = 20.0;
    private static final String GRAPHIC_CLASS = "piclight-button";

    @FXML
    private ComboBox<SSHServer> selectCombo;

    @FXML
    private Button addButton;

    @FXML
    private Button delButton;

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    public QEFXSSHDialog() {
        super();

        DialogPane dialogPane = this.getDialogPane();
        QEFXMain.initializeStyleSheets(dialogPane.getStylesheets());
        QEFXMain.initializeDialogOwner(this);

        this.setResizable(false);
        this.setTitle("Remote configuration");
        dialogPane.setHeaderText("Set remote configurations.");
        dialogPane.getButtonTypes().clear();
        dialogPane.getButtonTypes().addAll(ButtonType.CLOSE);

        Node node = null;
        try {
            node = this.createContent();
        } catch (Exception e) {
            node = new Label("ERROR: cannot show QEFXSSHDialog.");
            e.printStackTrace();
        }

        dialogPane.setContent(node);

        this.setResultConverter(buttonType -> {
            return buttonType;
        });
    }

    private Node createContent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("QEFXSSHDialog.fxml"));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupSelectCombo();
        this.setupAddButton();
        this.setupDelButton();
        this.setupHostField();
        this.setupPortField();
        this.setupUserField();
        this.setupPassField();
    }

    private void setupSelectCombo() {
        if (this.selectCombo == null) {
            return;
        }

        // TODO
    }

    private void setupAddButton() {
        if (this.addButton == null) {
            return;
        }

        this.addButton.setText("");
        this.addButton.getStyleClass().add(GRAPHIC_CLASS);
        this.addButton.setGraphic(SVGLibrary.getGraphic(SVGData.PLUS, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        // TODO
    }

    private void setupDelButton() {
        if (this.delButton == null) {
            return;
        }

        this.delButton.setText("");
        this.delButton.getStyleClass().add(GRAPHIC_CLASS);
        this.delButton.setGraphic(SVGLibrary.getGraphic(SVGData.MINUS, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        // TODO

        SSHServer sshServer = this.getSSHServer();
        if (sshServer != null) {
            SSHServerList.getInstance().removeSSHServer(sshServer);
        }
    }

    private void setupHostField() {
        if (this.hostField == null) {
            return;
        }

        // TODO
    }

    private void setupPortField() {
        if (this.portField == null) {
            return;
        }

        // TODO
    }

    private void setupUserField() {
        if (this.userField == null) {
            return;
        }

        // TODO
    }

    private void setupPassField() {
        if (this.passField == null) {
            return;
        }

        // TODO
    }

    private SSHServer getSSHServer() {
        if (this.selectCombo == null) {
            return null;
        }

        return this.selectCombo.getValue();
    }
}
