/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler.slabmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlabGenom {

    private static final double COORD_THR = 0.5; // angstrom

    private List<String> genom;

    public SlabGenom(String[] names, double[] coords) {
        if (names == null || names.length < 1) {
            throw new IllegalArgumentException("names is empty.");
        }

        if (coords == null || coords.length < 1) {
            throw new IllegalArgumentException("coords is empty.");
        }

        if (names.length != coords.length) {
            throw new IllegalArgumentException("names.length != coords.length.");
        }

        this.setupGeom(names, coords);
    }

    private void setupGeom(String[] names, double[] coords) {
        this.genom = new ArrayList<String>();

        int istart = 0;
        int iend = 0;

        while (true) {
            iend = this.nextLayer(istart, coords);
            if (istart >= iend) {
                break;
            }

            String code = this.getLayerCode(istart, iend, names);
            if (code != null && !(code.isEmpty())) {
                this.genom.add(code);
            }
        }
    }

    private int nextLayer(int istart, double[] coords) {
        if (istart >= coords.length) {
            return coords.length;
        }

        int iend = coords.length;
        double coord0 = coords[istart];

        for (int i = istart + 1; i < coords.length; i++) {
            double coord = coords[i];
            if (Math.abs(coord - coord0) > COORD_THR) {
                iend = i;
                break;
            }
        }

        return iend;
    }

    private String getLayerCode(int istart, int iend, String[] names) {
        if ((iend - istart) == 1) {
            return names[istart];
        }

        String[] names2 = new String[iend - istart];
        for (int i = istart; i < iend; i++) {
            names2[i - istart] = names[i];
        }

        Arrays.sort(names2);

        int mult = 0;
        String name = null;
        StringBuilder code = new StringBuilder();

        for (int i = 0; i <= names2.length; i++) {
            if (i < names2.length && name != null && name.equals(names2[i])) {
                mult++;
                continue;
            }

            if (name != null && !(name.isEmpty())) {
                if (code.length() > 0) {
                    code.append(' ');
                }

                code.append(name);
                if (mult > 1) {
                    code.append('*');
                    code.append(mult);
                }
            }

            if (i < names2.length) {
                mult = 1;
                name = names2[i];
            }
        }

        return code.toString();
    }

    @Override
    public String toString() {
        StringBuilder allCode = new StringBuilder();

        if (this.genom != null) {
            for (String code : this.genom) {
                if (code != null) {
                    allCode.append('{');
                    allCode.append(code);
                    allCode.append('}');
                }
            }
        }

        return allCode.length() > 0 ? allCode.toString() : "{}";
    }

    @Override
    public int hashCode() {
        return this.genom == null ? 0 : this.genom.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        SlabGenom other = (SlabGenom) obj;
        if (this.genom == null) {
            return this.genom == other.genom;
        } else {
            return this.genom.equals(other.genom);
        }
    }
}
