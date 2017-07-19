/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import java.io.IOException;

import burai.app.project.QEFXProjectController;
import burai.app.project.editor.designer.QEFXDesignerEditor;
import burai.project.Project;
import javafx.scene.Node;

public class DesignerAction {

    private Project project;

    private QEFXProjectController controller;

    private Designer designer;

    private QEFXDesignerViewer designerViewer;

    public DesignerAction(Project project, QEFXProjectController controller) {
        if (project == null) {
            throw new IllegalArgumentException("project is null.");
        }

        if (controller == null) {
            throw new IllegalArgumentException("controller is null.");
        }

        this.project = project;
        this.controller = controller;

        this.designer = null;
        this.designerViewer = null;
    }

    public QEFXProjectController getController() {
        return this.controller;
    }

    public void showDesigner() {
        if (this.designer == null || this.designerViewer == null) {
            this.initializeDesigner();
            return;
        }

        this.controller.setDesignerMode();
    }

    private void initializeDesigner() {
        if (this.designer == null) {
            this.designer = new Designer();
        }

        QEFXDesignerEditor designerEditor = null;
        if (this.designer != null) {
            try {
                designerEditor = new QEFXDesignerEditor(this.controller);
            } catch (IOException e) {
                designerEditor = null;
                e.printStackTrace();
            }
        }

        if (this.designerViewer == null) {
            try {
                this.designerViewer = new QEFXDesignerViewer(this.controller);
            } catch (IOException e) {
                this.designerViewer = null;
                e.printStackTrace();
            }
        }

        if (designerEditor != null && this.designerViewer != null) {
            this.controller.setDesignerMode();
            this.controller.clearStackedsOnViewerPane();

            Node viewerNode = this.designerViewer.getNode();
            if (viewerNode != null) {
                this.controller.setViewerPane(viewerNode);
            }

            Node editorNode = designerEditor.getNode();
            if (editorNode != null) {
                this.controller.setEditorPane(editorNode);
            }
        }
    }
}
