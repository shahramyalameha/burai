/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.design;

import javafx.scene.paint.Color;

public interface AtomDesignListener {

    public abstract void onAtomicRadiusChanged(AtomDesign atomDesign, double radius);

    public abstract void onAtomicColorChanged(AtomDesign atomDesign, Color color);

    public abstract void onAtomsStyleChanged(AtomDesign atomDesign, AtomsStyle atomsStyle);

    public abstract void onBondWidthChanged(AtomDesign atomDesign, double bondWidth);

}
