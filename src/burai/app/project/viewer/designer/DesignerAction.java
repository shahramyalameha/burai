/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import burai.app.project.QEFXProjectController;
import burai.project.Project;

public class DesignerAction {

    private Project project;

    private QEFXProjectController controller;

    public DesignerAction(Project project, QEFXProjectController controller) {
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

    public void showDesigner() {
        // TODO
    }

}
