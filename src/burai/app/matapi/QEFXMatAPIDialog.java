/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.matapi;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import burai.app.QEFXMain;
import burai.matapi.MaterialsAPILoader;

public class QEFXMatAPIDialog extends Dialog<ButtonType> implements Initializable {

    @FXML
    private Label usageLabel;

    @FXML
    private TextField apiKeyField;

    public QEFXMatAPIDialog() {
        super();

        DialogPane dialogPane = this.getDialogPane();
        QEFXMain.initializeStyleSheets(dialogPane.getStylesheets());
        QEFXMain.initializeDialogOwner(this);

        String header = "";
        header = header + "The Materials API allows you to search crystal structures.";
        header = header + System.lineSeparator();
        header = header + "               <https://www.materialsproject.org/docs/api>";

        this.setResizable(false);
        this.setTitle("Materials API");
        dialogPane.setHeaderText(header);
        dialogPane.getButtonTypes().clear();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);

        Node node = null;
        try {
            node = this.createContent();
        } catch (Exception e) {
            node = new Label("ERROR: cannot show QEFXMatAPIDialog.");
            e.printStackTrace();
        }

        dialogPane.setContent(node);

        this.setResultConverter(buttonType -> {
            return buttonType;
        });
    }

    private Node createContent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("QEFXMatAPIDialog.fxml"));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupUsageLabel();
        this.setupApiKeyField();
    }

    private void setupUsageLabel() {
        if (this.usageLabel == null) {
            return;
        }

        String text = "";
        text = text + "Please input fllowing data (1 or 2), and press ENTER.";
        text = text + System.lineSeparator();
        text = text + System.lineSeparator();
        text = text + "  1. List of elements separated with \"-\".";
        text = text + System.lineSeparator();
        text = text + "       e.g.  Li-Fe-O";
        text = text + System.lineSeparator();
        text = text + System.lineSeparator();
        text = text + "  2. Chemical formula.";
        text = text + System.lineSeparator();
        text = text + "       e.g.  Fe2O3";
        text = text + System.lineSeparator();

        this.usageLabel.setText(text);
    }

    private void setupApiKeyField() {
        if (this.apiKeyField == null) {
            return;
        }

        String apiKey = MaterialsAPILoader.getApiKey();
        if (apiKey == null) {
            this.apiKeyField.setText("");
        } else {
            this.apiKeyField.setText(apiKey);
        }
    }

    public void showAndSetProperties() {
        Optional<ButtonType> optButtonType = this.showAndWait();
        if (optButtonType == null || !optButtonType.isPresent()) {
            return;
        }
        if (optButtonType.get() != ButtonType.OK) {
            return;
        }

        String apiKey = this.getApiKey();

        if (apiKey != null && (!apiKey.isEmpty())) {
            MaterialsAPILoader.setApiKey(apiKey);
        } else {
            MaterialsAPILoader.setApiKey(null);
        }
    }

    private String getApiKey() {
        if (this.apiKeyField == null) {
            return null;
        }

        String value = this.apiKeyField.getText();
        return value == null ? null : value.trim();
    }
}
