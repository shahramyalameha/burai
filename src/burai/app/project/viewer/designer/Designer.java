/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.designer;

import burai.atoms.viewer.AtomsViewer;

public abstract class Designer {

    protected AtomsViewer atomsViewer;

    protected Designer() {

        this.atomsViewer = null;

    }

}
