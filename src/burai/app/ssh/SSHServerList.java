/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.ssh;

import java.util.ArrayList;
import java.util.List;

public class SSHServerList {

    private static SSHServerList instance = null;

    public static SSHServerList getInstance() {
        if (instance == null) {
            instance = new SSHServerList();
        }

        return instance;
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
}
