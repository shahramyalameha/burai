/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.matapi;

public abstract class MaterialData {

    public static MaterialData getInstance(String matID) {
        return MaterialData.getInstance(matID, null);
    }

    public static MaterialData getInstance(String matID, String apiKey) {

        MaterialData matData = null;
        if (apiKey != null && (!apiKey.trim().isEmpty())) {
            matData = MaterialAllData.getInstance(matID, apiKey);
        }

        if (matData == null) {
            matData = MaterialCIF.getInstance(matID);
        }

        return matData;
    }

    protected MaterialData() {
        // NOP
    }

    public abstract String getCIF();

}
