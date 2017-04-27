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
import java.util.UUID;

import org.jmol.api.JmolViewer;

import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.com.env.Environments;
import burai.com.file.FileTools;

public class JmolCIFAction implements JmolAction {

    private static final long PRE_DELETE_TIME = 4000L;

    private static final String KEY_JMOL_INDEX = "jmolIndex";

    private Cell cell;

    public JmolCIFAction(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.cell = cell;
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

        double a = this.cell.getA();
        double b = this.cell.getB();
        double c = this.cell.getC();
        double alpha = this.cell.getAlpha();
        double beta = this.cell.getBeta();
        double gamma = this.cell.getGamma();

        if (a <= 0.0) {
            throw new IOException("lattice parameter: a is not positive.");
        }
        if (b <= 0.0) {
            throw new IOException("lattice parameter: b is not positive.");
        }
        if (c <= 0.0) {
            throw new IOException("lattice parameter: c is not positive.");
        }
        if (alpha <= 0.0) {
            throw new IOException("lattice parameter: alpha is not positive.");
        }
        if (beta <= 0.0) {
            throw new IOException("lattice parameter: beta is not positive.");
        }
        if (gamma <= 0.0) {
            throw new IOException("lattice parameter: gamma is not positive.");
        }

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            writer.println("data_" + name);
            writer.println("_cell_length_a " + a);
            writer.println("_cell_length_b " + b);
            writer.println("_cell_length_c " + c);
            writer.println("_cell_angle_alpha " + alpha);
            writer.println("_cell_angle_beta  " + beta);
            writer.println("_cell_angle_gamma " + gamma);

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

            Atom[] atoms = this.cell.listAtoms();
            if (atoms != null) {
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

                    int myIndex = atom.intProperty(KEY_JMOL_INDEX);
                    String label2 = label1 + myIndex;

                    double x = atom.getX();
                    double y = atom.getY();
                    double z = atom.getZ();
                    double[] position = this.cell.convertToLatticePosition(x, y, z);
                    if (position == null || position.length < 3) {
                        continue;
                    }

                    String line = "  ";
                    line = line + label1 + " ";
                    line = line + label2 + " ";
                    line = line + position[0] + " ";
                    line = line + position[1] + " ";
                    line = line + position[2] + " 1";
                    writer.println(line);
                }
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
