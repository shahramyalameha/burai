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
import org.jmol.modelset.AtomCollection;
import org.jmol.viewer.Viewer;

import burai.atoms.model.event.AtomEvent;

public class JmolAtomMove implements JmolAction {

    private static final double RMIN = 1.0e-4;
    private static final double RRMIN = RMIN * RMIN;

    private int index;

    private float x;
    private float y;
    private float z;

    private boolean avail;

    public JmolAtomMove(int index, AtomEvent event) {
        if (index < 0) {
            throw new IllegalArgumentException("index is negative.");
        }

        if (event == null) {
            throw new IllegalArgumentException("event is null.");
        }

        this.index = index;

        this.x = (float) event.getX();
        this.y = (float) event.getY();
        this.z = (float) event.getZ();

        double dx = event.getDeltaX();
        double dy = event.getDeltaY();
        double dz = event.getDeltaZ();
        double rr = dx * dx + dy * dy + dz * dz;
        this.avail = (rr > RRMIN);
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

        String strData = "1;;" + Integer.toString(this.index + 1) + " ";
        strData = strData + "A A ";
        strData = strData + this.x + " ";
        strData = strData + this.y + " ";
        strData = strData + this.z + " ";

        try {
            viewer_.setAtomData(AtomCollection.TAINT_COORD, "", strData, true);
            viewer_.refresh(3, "atom is moved #" + this.index);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
