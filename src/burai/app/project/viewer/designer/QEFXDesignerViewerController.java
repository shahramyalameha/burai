/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;

public class QEFXDesignerViewerController extends QEFXAppController {

    private QEFXProjectController projectController;

    @FXML
    private Pane primPane;

    @FXML
    private Pane dualPane;

    @FXML
    private StackPane basePane;

    public QEFXDesignerViewerController(QEFXProjectController projectController) {
        super(projectController == null ? null : projectController.getMainController());

        this.projectController = projectController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupPrimPane();
        this.setupDualPane();
        this.setupBasePane();
    }

    private void setupPrimPane() {
        if (this.primPane == null) {
            return;
        }

        // TODO
    }

    private void setupDualPane() {
        if (this.dualPane == null) {
            return;
        }

        // TODO
    }

    private void setupBasePane() {
        if (this.basePane == null) {
            return;
        }

        // TODO
    }
}
