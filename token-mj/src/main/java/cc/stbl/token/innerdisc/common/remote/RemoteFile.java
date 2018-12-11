package cc.stbl.token.innerdisc.common.remote;

import java.util.Date;

public class RemoteFile {
    private String name;
    private String uri;
    private String url;
    private long length;
    private boolean isDirectory;
    private boolean isFile;

    private Date lastModified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }


    @Override
    public int hashCode() {
        return (this.getClass()+"."+this.url).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof RemoteFile && obj != null && this.getUrl() != null) {
            return this.url.equals(((RemoteFile) obj).getUrl());
        }
        return false;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getLastModified() {
        return lastModified;
    }
}
