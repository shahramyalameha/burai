/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result.movie;

import java.io.IOException;

import burai.app.QEFXAppComponent;
import burai.app.project.QEFXProjectController;

public class QEFXMovieBar extends QEFXAppComponent<QEFXMovieBarController> {

    public QEFXMovieBar(QEFXProjectController projectController) throws IOException {
        super("QEFXMovieBar.fxml", new QEFXMovieBarController(projectController));
    }

}
