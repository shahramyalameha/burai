/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.jmol;

import org.jmol.api.JmolViewer;

public class JmolAtomicAction implements JmolAction {

    public JmolAtomicAction() {
        /*
         * TOOD
         */
    }

    @Override
    public boolean actionOnJmol(JmolViewer viewer) {
        if (viewer == null) {
            return false;
        }

        /*
         * TOOD
         */

        return true;
    }

}
