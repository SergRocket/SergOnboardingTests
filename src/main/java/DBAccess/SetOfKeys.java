package DBAccess;

public class SetOfKeys {
    /*TESTING_ENV("http://172.16.10.62:8085", 5433, "172.16.242.2", "axis", "axis", "axis", "172.16.10.62", 22, "vkholintest", "ReadyTech2016"),
    AWS_QA("https://axis-qa01.readytech.com", 5432,"axis-qa-master.ch2n3vepnvhb.eu-central-1.rds.amazonaws.com", "axis",
                   "ReadyTechAxis", "axisqa01", "18.184.30.64", 22, "qa", "w9BySKVvP!87bWd");*/

    private String url;
    private int port;
    private String dBIp;
    private String dBUserName;
    private String dBUserPassword;
    private String dBName;
    private String sshAddress;
    private int sshPort;
    private String sshUserName;
    private String sshUserPassword;

    private SetOfKeys(String url, int port, String dBIp, String dBUserName, String dBUserPassword, String dBName, String sshAddress, int sshPort,
                      String sshUserName, String sshUserPassword) {
        this.url = url;
        this.port = port;
        this.dBIp = dBIp;
        this.dBUserName = dBUserName;
        this.dBUserPassword = dBUserPassword;
        this.dBName = dBName;
        this.sshAddress = sshAddress;
        this.sshPort = sshPort;
        this.sshUserName = sshUserName;
        this.sshUserPassword = sshUserPassword;
    }

    public String getterUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getdBIp() {
        return dBIp;
    }

    public String getdBUserName() {
        return dBUserName;
    }

    public String getdBUserPassword() {
        return dBUserPassword;
    }

    public String getDbName() {
        return dBName;
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
        return sshUserPassword;
    }

    public static SetOfKeys findKeysByUrl(String url) {
       /* for (SetOfKeys keys :
                values())
        {
            if(keys.getterUrl().equals(url)){
                return keys;
            }
        }*/
        return null;
    }

}
