/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.movie;

import java.io.File;

public class MovieProgress {

    private File file;

    private QEFXMovieProgressDialog dialog;

    protected MovieProgress(File file) {
        this.file = file;
        this.dialog = null;
    }

    protected synchronized void setProgress(double value) {
        if (this.dialog != null) {
            this.dialog.setProgress(value);
        }
    }

    protected synchronized void showProgress() {
        if (this.dialog != null) {
            return;
        }

        if (this.file != null) {
            this.dialog = new QEFXMovieProgressDialog(this.file);
            this.dialog.showProgress();
        }
    }

    protected synchronized void hideProgress() {
        if (this.dialog == null) {
            return;
        }

        this.dialog.hideProgress();
        this.dialog = null;
    }
}
