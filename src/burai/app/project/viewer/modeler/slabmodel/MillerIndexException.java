/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler.slabmodel;

public class MillerIndexException extends Exception {

    public MillerIndexException() {
        super("incorrect miller index.");
    }

    public MillerIndexException(String message) {
        super(message);
    }

    public MillerIndexException(Exception e) {
        super(e);
    }

}
