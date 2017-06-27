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

    public SSHServerList getInstance() {
        if (instance == null) {
            instance = new SSHServerList();
        }

        return instance;
    }

    private List<SSHServer> sshServers;

    private SSHServerList() {
        this.sshServers = new ArrayList<SSHServer>();
    }

    protected void addSSHServer(SSHServer sshServer) {
        if (sshServer == null) {
            return;
        }

        if (this.sshServers.contains(sshServer)) {
            this.sshServers.remove(sshServer);
        }

        this.sshServers.add(sshServer);
    }
}
