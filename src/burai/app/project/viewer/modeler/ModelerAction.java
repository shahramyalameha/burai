/*
 * Copyright (C) 2016 Satomichi Nishihara
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
import burai.project.Project;

public class ModelerAction {

    private Project project;

    private QEFXProjectController controller;

    public ModelerAction(Project project, QEFXProjectController controller) {
        if (project == null) {
            throw new IllegalArgumentException("project is null.");
        }

        if (controller == null) {
            throw new IllegalArgumentException("controller is null.");
        }

        this.project = project;
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

        this.controller.setEditorPane(modelerEditor.getNode());
    }
}
