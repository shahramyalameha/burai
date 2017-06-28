/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.ssh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import burai.com.env.Environments;

import com.google.gson.Gson;

public class SSHServerList {

    private static SSHServerList instance = null;

    public static SSHServerList getInstance() {
        if (instance == null) {
            String path = Environments.getSSHDataPath();

            if (path != null && !path.isEmpty()) {
                try {
                    instance = readFile(path);

                } catch (IOException e) {
                    e.printStackTrace();
                    instance = null;
                }
            }
        }

        if (instance == null) {
            instance = new SSHServerList();
        }

        return instance;
    }

    private static SSHServerList readFile(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            return null;
        }

        Reader reader = null;
        SSHServerList sshServerList = null;

        try {
            File file = new File(path);
            if (!file.isFile()) {
                return null;
            }

            reader = new BufferedReader(new FileReader(file));

            Gson gson = new Gson();
            sshServerList = gson.fromJson(reader, SSHServerList.class);

        } catch (FileNotFoundException e1) {
            throw e1;

        } catch (Exception e2) {
            throw new IOException(e2);

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                    throw e3;
                }
            }
        }

        return sshServerList;
    }

    private static void writeFile(String path, SSHServerList sshServerList) throws IOException {
        if (path == null || path.isEmpty()) {
            return;
        }

        if (sshServerList == null) {
            return;
        }

        Writer writer = null;

        try {
            File file = new File(path);
            writer = new BufferedWriter(new FileWriter(file));

            Gson gson = new Gson();
            gson.toJson(sshServerList, writer);

        } catch (IOException e1) {
            throw e1;

        } catch (Exception e2) {
            throw new IOException(e2);

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e3) {
                    throw e3;
                }
            }
        }
    }

    private List<SSHServer> sshServers;

    private SSHServerList() {
        this.sshServers = new ArrayList<SSHServer>();
    }

    public SSHServer getSSHServer(String title) {
        if (title != null && !title.isEmpty()) {
            return this.getSSHServer(new SSHServer(title));
        }

        return null;
    }

    private SSHServer getSSHServer(SSHServer sshServer) {
        if (sshServer == null) {
            return null;
        }

        int index = this.sshServers.indexOf(sshServer);

        return index < 0 ? null : this.sshServers.get(index);
    }

    public SSHServer[] listSSHServers() {
        if (this.sshServers.isEmpty()) {
            return null;
        }

        SSHServer[] sshArray = new SSHServer[this.sshServers.size()];
        return this.sshServers.toArray(sshArray);
    }

    protected void addSSHServer(SSHServer sshServer) {
        if (sshServer == null) {
            return;
        }

        while (this.sshServers.contains(sshServer)) {
            this.sshServers.remove(sshServer);
        }

        this.sshServers.add(sshServer);
    }

    protected void removeSSHServer(SSHServer sshServer) {
        if (sshServer == null) {
            return;
        }

        while (this.sshServers.contains(sshServer)) {
            this.sshServers.remove(sshServer);
        }
    }

    protected void saveToFile() {
        String path = Environments.getSSHDataPath();

        if (path != null && !path.isEmpty()) {
            try {
                writeFile(path, this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
