/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.ssh;

public class SSHServer {

    private String title;

    private String host;

    private String port;

    private String user;

    private String password;

    private String keyPath;

    protected SSHServer(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title is empty.");
        }

        this.title = title;
        this.host = null;
        this.port = null;
        this.user = null;
        this.password = null;
        this.keyPath = null;
    }

    public String getTitle() {
        return this.title;
    }

    public String getHost() {
        return this.host;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return this.port;
    }

    protected void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return this.user;
    }

    protected void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public String getKeyPath() {
        return this.keyPath;
    }

    protected void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public int hashCode() {
        return this.title.hashCode();
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

        SSHServer other = (SSHServer) obj;

        return this.title.equals(other.title);
    }
}
