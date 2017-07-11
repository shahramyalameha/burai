/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result.graph.tools;

import java.io.IOException;

import burai.app.QEFXAppComponent;
import burai.app.project.QEFXProjectController;
import javafx.scene.Node;

public class QEFXGraphNote extends QEFXAppComponent<QEFXGraphNoteController> {

    public QEFXGraphNote(QEFXProjectController projectController, Node content, boolean initMaximized) throws IOException {
        super("QEFXGraphNote.fxml", new QEFXGraphNoteController(projectController, content, initMaximized));
    }

    public void setOnNoteMaximized(NoteMaximized onNoteMaximized) {
        if (this.controller != null) {
            this.controller.setOnNoteMaximized(onNoteMaximized);
        }
    }

    public void minimize() {
        if (this.controller != null) {
            this.controller.minimize();
        }
    }

    public void maximize() {
        if (this.controller != null) {
            this.controller.maximize();
        }
    }

}
