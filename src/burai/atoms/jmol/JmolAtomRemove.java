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
import org.jmol.viewer.Viewer;

import burai.atoms.model.event.AtomEvent;

public class JmolAtomRemove implements JmolAction {

    private int index;

    private String name;

    private boolean avail;

    public JmolAtomRemove(int index, AtomEvent event) {
        if (index < 0) {
            throw new IllegalArgumentException("index is negative.");
        }

        if (event == null) {
            throw new IllegalArgumentException("event is null.");
        }

        this.index = index;

        String name1 = event.getName();
        String name2 = event.getOldName();

        this.name = name1;

        this.avail = false;
        if (name1 != null && name2 != null) {
            this.avail = name1.equals(name2);
        }
    }

    @Override
    public boolean isAvailable() {
        return this.avail;
    }

    @Override
    public boolean actionOnJmol(JmolViewer viewer) {
        if (viewer == null || !(viewer instanceof Viewer)) {
            return false;
        }

        Viewer viewer_ = (Viewer) viewer;

        if (viewer_.ms == null) {
            return false;
        }

        /*
         * TODO
         */

        return true;
    }
}
