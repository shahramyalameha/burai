/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import java.io.IOException;

import burai.app.QEFXAppComponent;
import burai.app.project.QEFXProjectController;
import burai.atoms.viewer.AtomsViewer;

public class QEFXDesignerWindow extends QEFXAppComponent<QEFXDesignerWindowController> {

    public QEFXDesignerWindow(QEFXProjectController projectController, AtomsViewer atomsViewer) throws IOException {
        super("QEFXDesignerWindow.fxml", new QEFXDesignerWindowController(projectController, atomsViewer));
    }

    public void setWidth(double width) {
        if (this.controller != null) {
            this.controller.setWidth(width);
        }
    }

    public void setHeight(double height) {
        if (this.controller != null) {
            this.controller.setHeight(height);
        }
    }

    public void setOnWindowMaximized(WindowMaximized onWindowMaximized) {
        if (this.controller != null) {
            this.controller.setOnWindowMaximized(onWindowMaximized);
        }
    }
}
