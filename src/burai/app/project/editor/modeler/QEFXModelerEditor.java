/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.modeler;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import burai.app.project.QEFXProjectController;
import burai.app.project.editor.result.QEFXResultEditor;
import burai.app.project.viewer.result.graph.QEFXGraphViewer;
import burai.com.keys.PriorKeyEvent;

public class QEFXModelerEditor extends QEFXResultEditor<QEFXModelerEditorController> {

    public QEFXModelerEditor(QEFXProjectController projectController, QEFXGraphViewer<?> viewer) throws IOException {
        super("QEFXGraphEditor.fxml",
                new QEFXModelerEditorController(projectController, viewer == null ? null : viewer.getController()));

        if (this.node != null) {
            this.setupKeys(this.node);
        }
    }

    private void setupKeys(Node node) {
        if (node == null) {
            return;
        }

        node.setOnKeyPressed(event -> {
            if (event == null) {
                return;
            }

            if (PriorKeyEvent.isPriorKeyEvent(event)) {
                return;
            }

            if (KeyCode.F5.equals(event.getCode())) {
                // F5
                this.controller.reload();
            }
        });
    }
}
