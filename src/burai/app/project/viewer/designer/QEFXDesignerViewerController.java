/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import java.net.URL;
import java.util.ResourceBundle;

import burai.app.QEFXAppController;
import burai.app.project.QEFXProjectController;

public class QEFXDesignerViewerController extends QEFXAppController {

    private QEFXProjectController projectController;

    public QEFXDesignerViewerController(QEFXProjectController projectController) {
        super(projectController == null ? null : projectController.getMainController());

        // TODO
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }

}
