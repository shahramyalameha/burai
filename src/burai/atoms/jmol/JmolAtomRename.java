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

import burai.atoms.element.ElementUtil;
import burai.atoms.model.event.AtomEvent;

public class JmolAtomRename implements JmolAction {

    private int index;

    private String name;

    private boolean avail;

    public JmolAtomRename(int index, AtomEvent event) {
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
            this.avail = !(name1.equals(name2));
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

        if (this.name == null || this.name.isEmpty()) {
            return false;
        }

        Viewer viewer_ = (Viewer) viewer;

        String baseName = null;
        try {
            baseName = viewer_.ms.at[this.index].getAtomName();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String atomName = this.getAtomName(this.name, baseName);
        if (atomName == null || atomName.isEmpty()) {
            return false;
        }

        String strData1 = "1;;" + Integer.toString(this.index + 1) + " ";
        strData1 = strData1 + this.name + " ";

        String strData2 = "1;;" + Integer.toString(this.index + 1) + " ";
        strData2 = strData2 + ElementUtil.getAtomicNumber(this.name) + " ";

        try {
            viewer_.setAtomData(AtomCollection.TAINT_ATOMNAME, "", strData1, true);
            viewer_.setAtomData(AtomCollection.TAINT_ELEMENT, "", strData2, true);
            viewer_.refresh(3, "atom is renamed #" + this.index);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String getAtomName(String newElem, String baseName) {
        if (baseName == null || baseName.isEmpty()) {
            return null;
        }

        String str = baseName;
        str = str.replace('(', ' ');
        str = str.replace(')', ' ');
        str = str.trim();

        String[] subStr = str.split("[\\s,]+");
        if (subStr == null || subStr.length < 2) {
            return null;
        }

        String strIndex = subStr[subStr.length - 1];
        String newName = newElem + "(" + strIndex + ")";
        return newName;
    }
}
