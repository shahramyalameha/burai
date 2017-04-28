/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.jmol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jmol.api.JmolViewer;

import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.com.env.Environments;
import burai.com.file.FileTools;

public class JmolCIFRead implements JmolAction {

    private static final double MIN_VOLUME = 1.0e-6;
    private static final double MAX_DENSITY = 0.1;

    private static final long PRE_DELETE_TIME = 4000L;

    private static final String KEY_JMOL_INDEX = "jmolIndex";

    private static final String REAL_FORMAT = "%24.16e";

    private double a;
    private double b;
    private double c;
    private double alpha;
    private double beta;
    private double gamma;

    private int numAtoms;
    private String[][] atomLabel;
    private double[][] atomCoord;

    private double density;

    public JmolCIFRead(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.setupDensity(cell);
        this.setupLattice(cell);
        this.setupAtoms(cell);
    }

    private void setupDensity(Cell cell) {
        double mass = (double) cell.numAtoms(true);
        double volume = cell.getVolume();

        this.density = 2.0 * MAX_DENSITY;
        if (volume > MIN_VOLUME) {
            this.density = mass / volume;
        }
    }

    private void setupLattice(Cell cell) {
        this.a = cell.getA();
        this.b = cell.getB();
        this.c = cell.getC();
        this.alpha = cell.getAlpha();
        this.beta = cell.getBeta();
        this.gamma = cell.getGamma();
    }

    private void setupAtoms(Cell cell) {
        Atom[] atoms = cell.listAtoms();
        if (atoms == null || atoms.length < 1) {
            return;
        }

        List<String[]> atomsLabelList = new ArrayList<String[]>();
        List<double[]> atomsCoordList = new ArrayList<double[]>();

        int jmolIndex = 0;

        for (Atom atom : atoms) {
            if (atom == null) {
                continue;
            }

            String label1 = atom.getElementName();
            if (label1 == null) {
                continue;
            }

            label1 = label1.trim();
            if (label1.isEmpty()) {
                continue;
            }

            if (!atom.isSlaveAtom()) {
                atom.setProperty(KEY_JMOL_INDEX, jmolIndex);
                jmolIndex++;
            }

            int jmolIndex2 = atom.intProperty(KEY_JMOL_INDEX);
            String label2 = label1 + jmolIndex2;

            double x = atom.getX();
            double y = atom.getY();
            double z = atom.getZ();
            double[] position = cell.convertToLatticePosition(x, y, z);
            if (position == null || position.length < 3) {
                continue;
            }

            atomsLabelList.add(new String[] { label1, label2 });
            atomsCoordList.add(position);
        }

        this.numAtoms = atomsLabelList.size();
        if (this.numAtoms < 1) {
            return;
        }

        this.atomLabel = new String[this.numAtoms][];
        this.atomLabel = atomsLabelList.toArray(this.atomLabel);

        this.atomCoord = new double[this.numAtoms][];
        this.atomCoord = atomsCoordList.toArray(this.atomCoord);
    }

    @Override
    public boolean isAvailable() {
        if (this.density > MAX_DENSITY) {
            return false;
        }

        if (this.a <= 0.0) {
            return false;
        }

        if (this.b <= 0.0) {
            return false;
        }

        if (this.c <= 0.0) {
            return false;
        }

        if (this.alpha <= 0.0) {
            return false;
        }

        if (this.beta <= 0.0) {
            return false;
        }

        if (this.gamma <= 0.0) {
            return false;
        }

        if (this.numAtoms < 1) {
            return false;
        }

        if (this.atomLabel == null || this.atomLabel.length < this.numAtoms) {
            return false;
        }

        if (this.atomCoord == null || this.atomCoord.length < this.numAtoms) {
            return false;
        }

        for (int i = 0; i < this.numAtoms; i++) {
            if (this.atomLabel[i] == null || this.atomLabel[i].length < 2) {
                return false;
            }

            if (this.atomCoord[i] == null || this.atomCoord[i].length < 3) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean actionOnJmol(JmolViewer viewer) {
        if (viewer == null) {
            return false;
        }

        File cifFile = this.getCIFFile();
        if (cifFile == null) {
            return false;
        }

        try {
            this.writeCIF(cifFile);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        viewer.script("load '" + cifFile.getPath() + "'");

        this.deleteCIF(cifFile);

        return true;
    }

    private File getCIFFile() {
        UUID objUuid = UUID.randomUUID();
        String strUuid = objUuid == null ? null : objUuid.toString();
        String cifName = strUuid == null ? null : (strUuid + ".cif");
        String cifDir = Environments.getJmolDirPath();
        File cifFile = cifName == null ? null : new File(cifDir, cifName);
        return cifFile;
    }

    private void deleteCIF(File file) {
        Thread thread = new Thread(() -> {
            synchronized (file) {
                try {
                    file.wait(PRE_DELETE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            FileTools.deleteAllFiles(file, false);
        });

        thread.start();
    }

    private void writeCIF(File file) throws IOException {
        String name = file.getName();
        if (name == null || name.isEmpty()) {
            name = "xxxx";
        }

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            writer.println("data_" + name);
            writer.println("_cell_length_a " + String.format(REAL_FORMAT, this.a));
            writer.println("_cell_length_b " + String.format(REAL_FORMAT, this.b));
            writer.println("_cell_length_c " + String.format(REAL_FORMAT, this.c));
            writer.println("_cell_angle_alpha " + String.format(REAL_FORMAT, this.alpha));
            writer.println("_cell_angle_beta  " + String.format(REAL_FORMAT, this.beta));
            writer.println("_cell_angle_gamma " + String.format(REAL_FORMAT, this.gamma));

            writer.println("loop_");
            writer.println(" _symmetry_equiv_pos_site_id");
            writer.println(" _symmetry_equiv_pos_as_xyz");
            writer.println("  1  'x,y,z'");

            writer.println("loop_");
            writer.println(" _atom_site_type_symbol");
            writer.println(" _atom_site_label");
            writer.println(" _atom_site_fract_x");
            writer.println(" _atom_site_fract_y");
            writer.println(" _atom_site_fract_z");
            writer.println(" _atom_site_occupancy");

            for (int i = 0; i < this.numAtoms; i++) {
                String label1 = this.atomLabel[i][0];
                String label2 = this.atomLabel[i][1];
                double x = this.atomCoord[i][0];
                double y = this.atomCoord[i][1];
                double z = this.atomCoord[i][2];

                String line = "  ";
                line = line + label1 + " ";
                line = line + label2 + " ";
                line = line + String.format(REAL_FORMAT, x) + " ";
                line = line + String.format(REAL_FORMAT, y) + " ";
                line = line + String.format(REAL_FORMAT, z) + " 1";
                writer.println(line);
            }

        } catch (IOException e) {
            throw e;

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
