/*
 * Copyright (C) 2017 Satomichi Nishihara
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
import burai.app.QEFXAppComponent;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.modeler.Modeler;
import burai.com.keys.PriorKeyEvent;

public class QEFXModelerEditor extends QEFXAppComponent<QEFXModelerEditorController> {

    public QEFXModelerEditor(QEFXProjectController projectController, Modeler modeler) throws IOException {
        super("QEFXModelerEditor.fxml", new QEFXModelerEditorController(projectController, modeler));

        if (this.node != null) {
            this.node.setOnMouseReleased(event -> this.node.requestFocus());
        }

        if (this.node != null) {
            this.setupKeys(this.node, modeler);
        }
    }

    private void setupKeys(Node node, Modeler modeler) {
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

            if (event.isShortcutDown() && KeyCode.Z.equals(event.getCode())) {
                if (!event.isShiftDown()) {
                    // Shortcut + Z
                    if (modeler != null) {
                        modeler.undo();
                    }

                } else {
                    // Shortcut + Shift + Z
                    if (modeler != null) {
                        modeler.redo();
                    }
                }

            } else if (event.isShortcutDown() && KeyCode.C.equals(event.getCode())) {
                // Shortcut + C
                if (modeler != null) {
                    modeler.center();
                }
            }
        });
    }
}
