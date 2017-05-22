/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler.slabmodel;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import burai.app.project.QEFXProjectController;
import burai.app.project.editor.modeler.slabmodel.QEFXSlabEditor;
import burai.app.project.viewer.atoms.AtomsAction;
import burai.app.project.viewer.modeler.ModelerIcon;
import burai.atoms.model.Cell;
import burai.atoms.viewer.AtomsViewer;
import burai.atoms.viewer.AtomsViewerInterface;
import burai.project.Project;

public class SlabAction {

    private Cell cell;

    private QEFXProjectController controller;

    private SlabModeler slabModeler;

    private AtomsViewerInterface atomsViewer;

    public SlabAction(Project project, QEFXProjectController controller) {
        this(project == null ? null : project.getCell(), controller);
    }

    public SlabAction(Cell cell, QEFXProjectController controller) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        if (controller == null) {
            throw new IllegalArgumentException("controller is null.");
        }

        this.cell = cell;
        this.controller = controller;

        this.slabModeler = null;
        this.atomsViewer = null;
    }

    public void showSlabModeler(SlabModel[] slabModels) {
        if (this.slabModeler == null || this.atomsViewer == null) {
            this.initializeSlabModeler();
            return;
        }

        this.slabModeler.initialize();
        this.controller.setModelerSlabMode();
    }

    private void initializeSlabModeler() {
        if (this.slabModeler == null) {
            this.slabModeler = new SlabModeler(this.cell);
        }

        if (this.atomsViewer == null) {
            this.atomsViewer = this.createAtomsViewer();
        }

        QEFXSlabEditor slabEditor = null;
        try {
            slabEditor = new QEFXSlabEditor(this.controller, this.slabModeler);
        } catch (IOException e) {
            slabEditor = null;
            e.printStackTrace();
        }

        if (this.atomsViewer != null && slabEditor != null) {
            this.controller.setModelerSlabMode();

            this.controller.setOnModeBacked(controller2 -> {
                if (this.slabModeler != null && this.slabModeler.isToReflect()) {
                    if (this.slabModeler != null) {
                        this.slabModeler.reflect();
                    }

                    Platform.runLater(() -> {
                        AtomsViewerInterface atomsViewer = this.controller.getAtomsViewer();
                        if (atomsViewer != null && atomsViewer instanceof AtomsViewer) {
                            ((AtomsViewer) atomsViewer).setCellToCenter();
                        }
                    });
                }

                return true;
            });

            this.controller.clearStackedsOnViewerPane();

            if (this.atomsViewer != null) {
                this.controller.setViewerPane(this.atomsViewer);
            }

            this.controller.stackOnViewerPane(new ModelerIcon("Slab" + System.lineSeparator() + "Model"));

            Node editorNode = slabEditor.getNode();
            if (editorNode != null) {
                this.controller.setEditorPane(editorNode);
            }
        }
    }

    private AtomsViewer createAtomsViewer() {
        Cell cell = this.slabModeler == null ? null : this.slabModeler.getCell();
        if (cell == null) {
            return null;
        }

        AtomsViewer atomsViewer = new AtomsViewer(cell, AtomsAction.getAtomsViewerSize(), true);
        this.slabModeler.setAtomsViewer(atomsViewer);

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
}
