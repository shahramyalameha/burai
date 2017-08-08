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

import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.atoms.AtomsAction;
import burai.atoms.design.Design;
import burai.atoms.model.Cell;
import burai.atoms.viewer.AtomsViewer;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class QEFXDesignerViewerController extends QEFXAppController {

    private QEFXProjectController projectController;

    private AtomsViewer atomsViewerPrim;

    private AtomsViewer atomsViewerDual;

    @FXML
    private Pane primPane;

    @FXML
    private Pane dualPane;

    @FXML
    private StackPane basePane;

    public QEFXDesignerViewerController(QEFXProjectController projectController, Cell cell) {
        super(projectController == null ? null : projectController.getMainController());

        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.projectController = projectController;

        this.atomsViewerPrim = new AtomsViewer(cell, AtomsAction.getAtomsViewerSize());
        this.atomsViewerDual = new AtomsViewer(cell, AtomsAction.getAtomsViewerSize());
    }

    public Design getDesign() {
        return this.atomsViewerPrim == null ? null : this.atomsViewerPrim.getDesign();
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

        if (this.atomsViewerDual != null) {
            this.atomsViewerDual.bindSceneTo(this.dualPane);
            this.dualPane.getChildren().add(this.atomsViewerDual);
        }
    }

    private void setupBasePane() {
        if (this.basePane == null) {
            return;
        }

        // TODO
    }
}
