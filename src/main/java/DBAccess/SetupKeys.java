package DBAccess;

import static javax.management.Query.value;

public class SetupKeys {

    //TESTING_ENV("http://172.16.30.101:9009", "5433", "labtracker_web");
    private String url;
    private String port;
    private String dbName;

    private SetupKeys(String url, String port, String dbName){
        this.url = url;
        this.port = port;
        this.dbName = dbName;
    }

    public String getterUrl(){
        return url;
    }

    public String getPort(){
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public static SetupKeys findDBNameWithUrl(String url){
        /*for(SetupKeys keys : values()){
          if(keys.getterUrl().equals(url)){
              return keys;
          }
        }*/
        return null;
    }
}
