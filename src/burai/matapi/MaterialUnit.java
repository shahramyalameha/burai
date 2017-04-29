/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.matapi;

public class MaterialUnit {

    private String cif;

    private String material_id;

    private MaterialUnit() {
        this.cif = null;
        this.material_id = null;
    }

    public String getCif() {
        return this.cif;
    }

    public String getMaterialId() {
        return this.material_id;
    }

}
