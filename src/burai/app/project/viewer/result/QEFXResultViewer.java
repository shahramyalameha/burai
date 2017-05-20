/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import burai.app.QEFXAppComponent;
import burai.com.keys.PriorKeyEvent;

public abstract class QEFXResultViewer<V extends QEFXResultViewerController> extends QEFXAppComponent<V> {

    public QEFXResultViewer(Node node, V controller) {
        super(node, controller);

        if (this.node != null) {
            this.setupKeys(this.node);
        }
    }

    public QEFXResultViewer(String fileFXML, V controller) throws IOException {
        super(fileFXML, controller);

        if (this.node != null) {
            this.setupKeys(this.node);
        }
    }

    public void reload() {
        if (this.controller != null) {
            this.controller.reload();
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
                this.controller.reloadSafely();
            }
        });
    }
}
