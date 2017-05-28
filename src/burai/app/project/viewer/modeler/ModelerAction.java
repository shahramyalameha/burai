/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import burai.app.QEFXMain;
import burai.app.project.QEFXProjectController;
import burai.app.project.editor.modeler.QEFXModelerEditor;
import burai.app.project.viewer.atoms.AtomsAction;
import burai.atoms.model.Cell;
import burai.atoms.viewer.AtomsViewer;
import burai.atoms.viewer.AtomsViewerInterface;
import burai.project.Project;

public class ModelerAction {

    private Cell cell;

    private QEFXProjectController controller;

    private Modeler modeler;

    private AtomsViewer atomsViewer;

    public ModelerAction(Project project, QEFXProjectController controller) {
        this(project == null ? null : project.getCell(), controller);
    }

    public ModelerAction(Cell cell, QEFXProjectController controller) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        if (controller == null) {
            throw new IllegalArgumentException("controller is null.");
        }

        this.cell = cell;
        this.controller = controller;

        this.modeler = null;
        this.atomsViewer = null;
    }

    public QEFXProjectController getController() {
        return this.controller;
    }

    public void showModeler() {
        if (this.modeler == null || this.atomsViewer == null) {
            this.initializeModeler();
            return;
        }

        this.controller.setModelerMode();
    }

    private void initializeModeler() {
        if (this.modeler == null) {
            this.modeler = new Modeler(this.cell);
        }

        if (this.atomsViewer == null) {
            this.atomsViewer = this.createAtomsViewer();
        }

        QEFXModelerEditor modelerEditor = null;
        try {
            modelerEditor = new QEFXModelerEditor(this.controller, this.modeler);
        } catch (IOException e) {
            modelerEditor = null;
            e.printStackTrace();
        }

        if (this.atomsViewer != null && modelerEditor != null) {
            this.controller.setModelerMode();

            this.controller.setOnModeBacked(controller2 -> {
                if (this.modeler != null && this.modeler.isToReflect()) {
                    this.showReflectDialog();
                }
                return true;
            });

            this.controller.clearStackedsOnViewerPane();

            if (this.atomsViewer != null) {
                this.controller.setViewerPane(this.atomsViewer);
            }

            this.controller.stackOnViewerPane(new ModelerIcon("Modeler"));

            Node editorNode = modelerEditor.getNode();
            if (editorNode != null) {
                this.controller.setEditorPane(editorNode);
            }
        }
    }

    private AtomsViewer createAtomsViewer() {
        Cell cell = this.modeler == null ? null : this.modeler.getCell();
        if (cell == null) {
            return null;
        }

        AtomsViewer atomsViewer = new AtomsViewer(cell, AtomsAction.getAtomsViewerSize());
        this.modeler.setAtomsViewer(atomsViewer);

        final BorderPane projectPane;
        if (this.controller != null) {
            projectPane = this.controller.getProjectPane();
        } else {
            projectPane = null;
        }

        if (projectPane != null) {
            atomsViewer.addExclusiveNode(() -> {
                return projectPane.getRight();
            });
            atomsViewer.addExclusiveNode(() -> {
                return projectPane.getBottom();
            });
        }

        return atomsViewer;
    }

    private void showReflectDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        QEFXMain.initializeDialogOwner(alert);
        alert.setHeaderText("Reflect this model upon the input-file ?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> optButtonType = alert.showAndWait();
        if (optButtonType == null || (!optButtonType.isPresent())) {
            return;
        }
        if (!ButtonType.YES.equals(optButtonType.get())) {
            return;
        }

        if (this.modeler != null) {
            this.modeler.reflect();
        }

        Platform.runLater(() -> {
            AtomsViewerInterface atomsViewer = this.controller.getAtomsViewer();
            if (atomsViewer != null && atomsViewer instanceof AtomsViewer) {
                ((AtomsViewer) atomsViewer).setCellToCenter();
            }
        });
    }
}
