/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import burai.app.QEFXAppComponent;
import burai.com.keys.PriorKeyEvent;

public abstract class QEFXResultEditor<E extends QEFXResultEditorController<?>> extends QEFXAppComponent<E> {

    public QEFXResultEditor(String fileFXML, E controller) throws IOException {
        super(fileFXML, controller);

        if (this.node != null) {
            this.setupMouse(this.node);
            this.setupKeys(this.node);
        }
    }

    private void setupMouse(Node node) {
        if (node == null) {
            return;
        }

        node.setOnMouseReleased(event -> {
            this.node.requestFocus();
        });
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
