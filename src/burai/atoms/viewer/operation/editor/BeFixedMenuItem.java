/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.viewer.operation.editor;

import java.util.List;

import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.atoms.model.property.AtomProperty;
import burai.atoms.viewer.AtomsViewer;
import burai.atoms.viewer.operation.ViewerEventManager;
import burai.atoms.visible.VisibleAtom;

public class BeFixedMenuItem extends EditorMenuItem {

    private static final String ITEM_LABEL = "Let selected atoms be FIXED";

    public BeFixedMenuItem(ViewerEventManager manager) {
        super(ITEM_LABEL, manager);
    }

    public BeFixedMenuItem(EditorMenu editorMenu) {
        super(ITEM_LABEL, editorMenu);
    }

    @Override
    protected void editAtoms() {
        if (this.manager == null) {
            return;
        }

        AtomsViewer atomsViewer = this.manager.getAtomsViewer();
        if (atomsViewer == null) {
            return;
        }

        List<VisibleAtom> visibleAtoms = atomsViewer.getVisibleAtoms();

        int numSelected = 0;
        for (VisibleAtom visibleAtom : visibleAtoms) {
            if (visibleAtom != null && visibleAtom.isSelected()) {
                numSelected++;
            }
        }
        if (numSelected < 1) {
            return;
        }

        Cell cell = atomsViewer.getCell();
        if (cell == null) {
            return;
        }

        atomsViewer.storeCell();

        for (VisibleAtom visibleAtom : visibleAtoms) {
            if (visibleAtom != null && visibleAtom.isSelected()) {
                Atom atom = visibleAtom.getModel();
                if (atom != null) {
                    atom = atom.getMasterAtom();
                }
                if (atom != null) {
                    atom.setProperty(AtomProperty.FIXED_X, true);
                    atom.setProperty(AtomProperty.FIXED_Y, true);
                    atom.setProperty(AtomProperty.FIXED_Z, true);
                }
            }
        }
    }
}
