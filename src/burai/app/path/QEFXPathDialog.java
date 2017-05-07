/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.path;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import burai.app.QEFXMain;
import burai.app.QEFXMainController;
import burai.com.env.Environments;

public class QEFXPathDialog extends Dialog<ButtonType> implements Initializable {

    private static final String DEFAULT_MESSAGE = "Select Directory";

    private QEFXMainController controller;

    @FXML
    private Label qeLabel;

    @FXML
    private Button qeButton;

    @FXML
    private Label mpiLabel;

    @FXML
    private Button mpiButton;

    public QEFXPathDialog(QEFXMainController controller) {
        super();

        if (controller == null) {
            throw new IllegalArgumentException("controller is null.");
        }

        this.controller = controller;

        DialogPane dialogPane = this.getDialogPane();
        QEFXMain.initializeStyleSheets(dialogPane.getStylesheets());
        QEFXMain.initializeDialogOwner(this);

        this.setResizable(false);
        this.setTitle("Path");
        dialogPane.setHeaderText("Set path.");
        dialogPane.getButtonTypes().clear();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Node node = null;
        try {
            node = this.createContent();
        } catch (Exception e) {
            node = new Label("ERROR: cannot show QEFXPathDialog.");
            e.printStackTrace();
        }

        dialogPane.setContent(node);

        this.setResultConverter(buttonType -> {
            return buttonType;
        });
    }

    private Node createContent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("QEFXPathDialog.fxml"));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupQeLabel();
        this.setupButton(this.qeButton, false);
        this.setupMpiLabel();
        this.setupButton(this.mpiButton, true);
    }

    private void setupQeLabel() {
        if (this.qeLabel == null) {
            return;
        }

        String text = this.qeLabel.getText();
        if (text != null) {
            if (Environments.isWindows()) {
                text = text.replaceAll("pw.x", "pw.exe");
                this.qeLabel.setText(text);
            }
        }
    }

    private void setupMpiLabel() {
        if (this.mpiLabel == null) {
            return;
        }

        String text = this.mpiLabel.getText();
        if (text != null) {
            if (Environments.isWindows()) {
                text = text.replaceAll("mpirun", "mpiexec.exe");
                this.mpiLabel.setText(text);
            }
        }
    }

    private void setupButton(Button button, boolean isMPI) {
        if (button == null) {
            return;
        }

        String path = null;
        if (isMPI) {
            path = QEPath.getMPIPath();
        } else {
            path = QEPath.getPath();
        }

        if (path != null) {
            button.setText(path);
            button.setTooltip(new Tooltip(path));
        } else {
            button.setText(DEFAULT_MESSAGE);
        }

        button.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            if (isMPI) {
                chooser.setTitle("Path of MPI");
            } else {
                chooser.setTitle("Path of Quantum ESPRESSO");
            }

            String initPath = this.getButtonText(button);
            File initDir = initPath == null ? null : new File(initPath);
            if (initDir != null) {
                try {
                    if (initDir.isDirectory()) {
                        chooser.setInitialDirectory(initDir);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            File directory = null;
            Stage stage = this.controller.getStage();
            if (stage != null) {
                directory = chooser.showDialog(stage);
            }

            String dirPath = directory == null ? null : directory.getPath();
            if (dirPath != null) {
                dirPath = dirPath.trim();
            }

            if (dirPath != null && !(dirPath.isEmpty())) {
                button.setText(dirPath);
                button.setTooltip(new Tooltip(dirPath));
            }
        });
    }

    public void showAndSetProperties() {
        Optional<ButtonType> optButtonType = this.showAndWait();
        if (optButtonType == null || !optButtonType.isPresent()) {
            return;
        }
        if (optButtonType.get() != ButtonType.OK) {
            return;
        }

        String qePath = this.getButtonText(this.qeButton);
        String mpiPath = this.getButtonText(this.mpiButton);

        if (qePath == null || qePath.isEmpty()) {
            QEPath.setPath(null);
        } else {
            QEPath.setPath(qePath);
        }

        if (mpiPath == null || mpiPath.isEmpty()) {
            QEPath.setMPIPath(null);
        } else {
            QEPath.setMPIPath(mpiPath);
        }
    }

    private String getButtonText(Button button) {
        if (button == null) {
            return null;
        }

        String value = button.getText();
        if (DEFAULT_MESSAGE.equals(value)) {
            value = null;
        }

        return value == null ? null : value.trim();
    }
}
