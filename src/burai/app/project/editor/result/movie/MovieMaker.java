/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.movie;

import burai.app.QEFXMainController;
import burai.project.Project;

public class MovieMaker {

    private QEFXMainController mainController;

    private Project project;

    protected MovieMaker(QEFXMainController mainController, Project project) {
        if (mainController == null) {
            throw new IllegalArgumentException("mainController is null.");
        }

        if (project == null) {
            throw new IllegalArgumentException("project is null.");
        }

        this.mainController = mainController;
        this.project = project;
    }

}
