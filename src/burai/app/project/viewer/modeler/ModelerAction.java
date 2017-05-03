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

import burai.app.project.QEFXProjectController;
import burai.app.project.editor.modeler.QEFXModelerEditor;
import burai.atoms.model.Cell;
import burai.project.Project;

public class ModelerAction {

    private Modeler modeler;

    private QEFXProjectController controller;

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

        this.modeler = new Modeler(cell);
        this.controller = controller;
    }

    public QEFXProjectController getController() {
        return this.controller;
    }

    public void showModeler() {
        this.initializeModeler();
        //this.controller.setResultExplorerMode();
    }

    private void initializeModeler() {
        //this.controller.setResultExplorerMode(controller2 -> {
        //    this.explorer.reload();
        //    this.fileTree.reload();
        //});

        this.controller.clearStackedsOnViewerPane();

        //Node explorerNode = this.explorer.getNode();
        //if (explorerNode != null) {
        //    this.controller.stackOnViewerPane(explorerNode);
        //}

        QEFXModelerEditor modelerEditor = null;
        try {
            modelerEditor = new QEFXModelerEditor(this.controller);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        this.controller.setViewerPane(null);
        this.controller.setEditorPane(modelerEditor.getNode());
    }
}
