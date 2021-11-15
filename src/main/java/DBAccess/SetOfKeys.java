package DBAccess;

public enum SetOfKeys {
    TEST_ENV("http://172.16.10.62:8085", 5433, "172.16.242.2", "axis", "axis", "axis", "172.16.10.62", 22, "vkholintest", "ReadyTech2016"),
    MASTER_ENV("http://172.16.10.62:8081", 5432, "172.16.239.2", "axis", "axis", "axis", "172.16.10.62", 22, "vkholintest", "ReadyTech2016"),
    DEV_ENV("http://172.16.10.62:8082", 5432, "172.16.238.2", "axis", "axis", "axis", "172.16.10.62", 22, "vkholintest", "ReadyTech2016"),
    FEATURE_PRAGMASOFT_ENV("http://172.16.10.62:8083", 5432, "172.16.240.2", "axis", "axis", "axis", "172.16.10.62", 22, "vkholintest", "ReadyTech2016"),
    FEATURE_INFOSTROY_ENV("http://172.16.10.62:8084", 5432, "172.16.241.2", "axis", "axis", "axis", "172.16.10.62", 22, "vkholintest", "ReadyTech2016"),
    AWS_ACCEPTANCE("https://axis-acceptance01.readytech.com", 5432,"axis-acceptance-master.ch2n3vepnvhb.eu-central-1.rds.amazonaws.com", "reCDoLoRyL",
            "dHkh3p5ECf7ES66Yscyt", "axis", "18.184.30.64", 22, "qa", "w9BySKVvP!87bWd"),
    AWS_QA("https://axis-qa01.readytech.com", 5432,"axis-qa-master.ch2n3vepnvhb.eu-central-1.rds.amazonaws.com", "axis",
            "ReadyTechAxis", "axisqa01", "18.184.30.64", 22, "qa", "w9BySKVvP!87bWd"),
    AWS_QA2("https://axis-qa02.readytech.com", 5432,"axis-qa-master.ch2n3vepnvhb.eu-central-1.rds.amazonaws.com", "axis",
            "ReadyTechAxis", "axisqa02", "18.184.30.64", 22, "qa", "w9BySKVvP!87bWd"),
    AWS_DEV5("https://axis-dev05.readytech.com", 5432,"axis-dev.ch2n3vepnvhb.eu-central-1.rds.amazonaws.com", "axis",
            "ReadyTechAxis", "axisdev05", "172.95.11.194", 22, "qa", "w9BySKVvP!87bWd");

    private String url;
    private int port;
    private String dbIP;
    private String dbUserName;
    private String dbUserPassword;
    private String dbName;
    private String sshAddress;
    private int sshPort;
    private String sshUserName;
    private String sshUserPass;

    private SetOfKeys(String url, int port, String dbIP, String dbUserName, String dbUserPassword, String dbName, String sshAddress, int sshPort,
                     String sshUserName, String sshUserPass) {

        this.url = url;
        this.port = port;
        this.dbIP = dbIP;
        this.dbUserName = dbUserName;
        this.dbUserPassword = dbUserPassword;
        this.dbName = dbName;
        this.sshAddress = sshAddress;
        this.sshPort = sshPort;
        this.sshUserName = sshUserName;
        this.sshUserPass = sshUserPass;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getDbIP() {
        return dbIP;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getdDbUserPassword() {
        return dbUserPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public String getSshAddress() {
        return sshAddress;
    }

    public int getSshPort() {
        return sshPort;
    }

    public String getSshUserName() {
        return sshUserName;
    }

    public String getSshUserPassword() {
        return sshUserPass;
    }

    public static SetOfKeys findKeysByUrl(String url) {
        for(SetOfKeys v : values()){
            if (v.getUrl().equals(url)) {
                return v;
            }
        }
        return null;
    }

}
