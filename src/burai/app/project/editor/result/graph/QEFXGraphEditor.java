/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.graph;

import java.io.IOException;

import burai.app.project.QEFXProjectController;
import burai.app.project.editor.result.QEFXResultEditor;
import burai.app.project.viewer.result.graph.QEFXGraphViewer;

public class QEFXGraphEditor extends QEFXResultEditor<QEFXGraphEditorController> {

    public QEFXGraphEditor(QEFXProjectController projectController, QEFXGraphViewer<?> viewer) throws IOException {
        super("QEFXGraphEditor.fxml",
                new QEFXGraphEditorController(projectController, viewer == null ? null : viewer.getController()));
    }

}
