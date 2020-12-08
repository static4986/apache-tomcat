package server.support.map;

public class Host {

    private String hostName;

    private String basePath;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Host(String hostName, String basePath) {
        this.hostName = hostName;
        this.basePath = basePath;
    }
}
