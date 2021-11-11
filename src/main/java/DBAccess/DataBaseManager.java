package DBAccess;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.qameta.allure.util.PropertiesUtils;

import java.sql.*;
import java.util.Properties;

public class DataBaseManager {

    Connection connection =null;
    static Session SSHSession = null;

    private static void makeSshTunnel(String stringSshuser, String stringSshPassword, String stringSshHost,
                                      int sshPort, String stringRemoteHost, int localPort, int remotePort) throws JSchException {
        final JSch jsch = new JSch();
        Session session = jsch.getSession(stringSshuser, stringSshHost, 22);
        session.setPassword(stringSshPassword);

        final Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        session.setConfig(configuration);
        session.connect();
        session.setPortForwardingL(localPort, stringRemoteHost, remotePort);
        SSHSession = session;
    }

    public Connection getConnectionStr(SetOfKeys keysSet){
        if(connection == null || SSHSession == null){
            try{
                String stringSshuser = keysSet.getSshUserName();
                String stringSshPassword = keysSet.getSshUserPassword();
                String stringSshHost = keysSet.getSshAddress();
                int sshPort = keysSet.getSshPort();
                String stringRemoteHost = keysSet.getdBIp();
                int localPort = 3366;
                int remotePort = keysSet.getPort();
                String stringDbUser = keysSet.getdBUserName();
                String stringDbName = keysSet.getDbName();
                String stringDbPassword = keysSet.getdBUserPassword();

                System.out.println("SSH loging username: " + stringSshuser);
                System.out.println("SSH login password: " + stringSshPassword);
                System.out.println("hostname or ip or SSH server: " + stringSshHost);
                System.out.println("remote SSH host port number: " + sshPort);
                System.out.println("hostname or ip of your DB: " + stringRemoteHost);
                System.out.println("local port number use to bind SSH tunnel: " + localPort);
                System.out.println("remote port number of your database: " + remotePort);
                System.out.println("database loging username: " + stringDbUser);
                System.out.println("database name: " + stringDbName);
                System.out.println("database login password: " + stringDbPassword);
                try{
                    makeSshTunnel(stringSshuser, stringSshPassword, stringSshHost, sshPort, stringRemoteHost, localPort, remotePort);
                } catch (JSchException e) {
                    e.printStackTrace();
                }
                System.out.println("SSH: " + SSHSession);
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:" + localPort + "/" +
                        stringDbName, stringDbUser, stringDbPassword);
                System.out.println("connection: " + connection);
                return connection;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        return connection;
    }

    public Connection getConnectionString(SetOfKeys keys) throws SQLException {
            Connection connect = null;
            connect = DriverManager.getConnection("jdbc:postgresql://" + keys.getterUrl()
                    .replace("http://", "").substring(0, keys.getterUrl()
                    .replace("http://", "").length()-5) + ":" + keys
                    .getPort() + "/axis", "axis", "axis");
            return connect;
    }

    public int getCountOfEventsPerUser(String firstName, String lastName, String email){
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionStr(key);
                Statement st = connection.createStatement();
                String sqlQueryForAllEvents = "Select Count(*) AS total from event " + "Where event.account_entity_id = (Select app_user.account_entity_id From app_user "
                        +  "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName + "' and app_user.last_name = '"
                        + lastName + "');";
                ResultSet resSet = st.executeQuery(sqlQueryForAllEvents);
                if(resSet.next()){
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfTemplatesPerUser(String firstName, String lastName, String email){
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL.getPropName());
            SetOfKeys k = SetOfKeys.findKeysByUrl(url);
            if( k != null){
                Connection connection = getConnectionString(k);
                Statement st = connection.createStatement();
                String sqlQueryForAllTemplates = "Select Count(*) AS total from template "
                        + "Where template.account_entity_id = (Select app_user.account_entity_id From app_user "
                        + "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName
                        + "' and app_user.last_name = '" + lastName + "');";
                ResultSet resSet = st.executeQuery(sqlQueryForAllTemplates);
                if(resSet.next()){
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfLabPlansPerUser(String firstName, String lastName, String email){
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key !=null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total from lab_plan "
                        + "Where lab_plan.account_entity_id = (Select app_user.account_entity_id From app_user "
                        + "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName
                        + "' and app_user.last_name = '" + lastName + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForAllPCs);
                if(resSet != null){
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfImagesPerUserAndOSType(String firstName, String lastName, String email, String osType){
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key !=null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total from image "
                        + "Where image.os_pl = '" + osType.toUpperCase() + "' and"
                        + "  image.account_entity_id = (Select app_user.account_entity_id From app_user "
                        + "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName
                        + "' and app_user.last_name = '" + lastName + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForAllPCs);
                if(resSet != null){
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfImagesPerUser(String firstName, String lastName, String email) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());

            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total from image "
                        + "Where image.account_entity_id = (Select app_user.account_entity_id From app_user "
                        + "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName
                        + "' and app_user.last_name = '" + lastName + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForAllPCs);
                if (resSet != null) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteEventFromDBByName(String eventName){
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String eventStatus = null;
                Statement statement = connection.createStatement();
                String eventStatusQuery = "Select event.status_pl as status From event "
                        + "Where event.name = '" + eventName + "';";
                ResultSet resSet = statement.executeQuery(eventStatusQuery);
                try {
                    if(resSet.next()){
                        eventStatus = resSet.getString("status");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if(eventStatus.equals("CANCELLED") || eventStatus.equals("FINISHED")){
                    String labQuery = "Delete FROM lab WHERE lab.event_id = (Select event.id AS event_id FROM event "
                            + "WHERE event.name = '" + eventName + "');";
                    PreparedStatement statement1 = connection.prepareStatement(labQuery);
                    statement1.executeUpdate();

                    String seatInstructorQuery = "Delete FROM event_seat_instructor WHERE event_seat_instructor.event_id = (Select event.id AS event_id FROM event "
                            + "WHERE event.name = '" + eventName + "');";
                    PreparedStatement statement2 = connection.prepareStatement(seatInstructorQuery);
                    statement2.executeUpdate();

                    String seatQuery = "Delete FROM seat WHERE seat.event_id = (Select event.id AS event_id FROM event "
                            + "WHERE event.name = '" + eventName + "');";
                    PreparedStatement statement3 = connection.prepareStatement(seatQuery);
                    statement3.executeUpdate();

                    String eventItemQuery = "Delete FROM event_item	WHERE event_item.event_id = (Select event.id AS event_id FROM event "
                            + "WHERE event.name = '" + eventName + "');";
                    PreparedStatement statement4 = connection.prepareStatement(eventItemQuery);
                    statement4.executeUpdate();

                    String accessCodeItemQuery = "Delete FROM access_code WHERE access_code.event_id = (Select event.id AS event_id FROM event "
						+ "WHERE event.name = '" + eventName + "');";
                    PreparedStatement statement5 = connection.prepareStatement(accessCodeItemQuery);
                    statement5.executeUpdate();

                    String auditLogQuery = "Delete FROM audit_log WHERE audit_log.event_id = (Select event.id AS event_id FROM event "
                            + "WHERE event.name = '" + eventName + "');";
                    PreparedStatement statement6 = connection.prepareStatement(auditLogQuery);
                    statement6.executeUpdate();

                    String eventQuery = "DELETE FROM event WHERE event.name = '"
                            + eventName + "';";
                    PreparedStatement statement7 = connection.prepareStatement(eventQuery);
                    statement7.executeUpdate();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteCourseTemplateFromDBByName(String courseTemplateName){
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionStr(key);
                String deleteCoursetemplateQuery = "DELETE FROM template WHERE template.name = ?";
                PreparedStatement statement2 = connection.prepareStatement(deleteCoursetemplateQuery);
                statement2.setString(1, courseTemplateName);
                statement2.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEntityFromDBByName(String entityName) {
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String deleteEntityPathItemQuery = "DELETE FROM account_entity_path WHERE account_entity_path.account_entity_id = (Select account_entity.id AS account_entity_id "
                        + "FROM account_entity WHERE account_entity.name =  ?);";
                PreparedStatement statement1 = connection.prepareStatement(deleteEntityPathItemQuery);
                statement1.setString(1, entityName);
                statement1.executeUpdate();
                String deleteAuditLogItemQuery = "DELETE FROM audit_log WHERE audit_log.account_entity_id = (Select account_entity.id AS account_entity_id "
                        + "FROM account_entity WHERE account_entity.name =  ?);";
                PreparedStatement statement3 = connection.prepareStatement(deleteAuditLogItemQuery);
                statement3.setString(1, entityName);
                statement3.executeUpdate();
                String deleteEntityQuery = "DELETE FROM account_entity WHERE account_entity.name = ?";
                PreparedStatement statement2 = connection.prepareStatement(deleteEntityQuery);
                statement2.setString(1, entityName);
                statement2.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLabPlanFromDBByName(String labPlanName){
        try{
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String deleteLabPlanItemQuery = "DELETE FROM lab_plan_item WHERE lab_plan_item.lab_plan_id= (Select lab_plan.id AS lab_id "
                        + "From lab_plan Where lab_plan.name = ?);";
                PreparedStatement statement1 = connection.prepareStatement(deleteLabPlanItemQuery);
                statement1.setString(1, labPlanName);
                statement1.executeUpdate();

                String deleteLabPlanQuery = "DELETE FROM lab_plan WHERE lab_plan.name = ?";
                PreparedStatement statement2 = connection.prepareStatement(deleteLabPlanQuery);
                statement2.setString(1, labPlanName);
                statement2.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLPDromDBByName(String labPlanName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String labPlanItemQuery = "DELETE FROM lab_plan_item WHERE lab_plan_item.lab_plan_id = "
                        + "(Select lab_plan.id AS lab_id From lab_plan Where lab_plan.name = ?)";
                PreparedStatement statement2 = connection.prepareStatement(labPlanItemQuery);
                statement2.setString(1, labPlanName);
                statement2.executeUpdate();
                String deleteLabPlanQuery = "DELETE FROM lab_plan WHERE lab_plan.name = ?";
                PreparedStatement statement = connection.prepareStatement(deleteLabPlanQuery);
                statement.setString(1, labPlanName);
                statement.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteImageDromDBByName(String imageName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            imageName = imageName.replaceAll("'", "''");
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String deleteImageVDIQuery = "Delete FROM cloud_provider_vdi "
                        + "WHERE cloud_provider_vdi.id = (Select image.master_vdi_id From image "
                        + "Where image.name='" + imageName + "');";
                PreparedStatement statement1 = connection.prepareStatement(deleteImageVDIQuery);
                statement1.executeUpdate();

                String deleteLabPlanItem = "Delete FROM lab_plan_item "
                        + "WHERE lab_plan_item.image_id = (Select image.id From image " + "Where image.name='"
                        + imageName + "');";
                        PreparedStatement statement2 = connection.prepareStatement(deleteLabPlanItem);
                        statement2.executeUpdate();

                String deleteImageQuery = "DELETE FROM image WHERE image.name = '"
                        + imageName
                        + "';";
                PreparedStatement statement = connection.prepareStatement(deleteImageQuery);
                statement.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateImageVerificationStatus(String imageName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String updateImageQuery = "UPDATE image SET verification_status = 'VERIFIED',  blocking_flags = 0 "
                        + "WHERE name = '" + imageName + "';";
                PreparedStatement statement1 = connection.prepareStatement(updateImageQuery);
                statement1.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateImageVerificationStatusBlock(String imageName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String updateImageQuery = "UPDATE image SET verification_status = 'VERIFIED',  blocking_flags = 0 "
                        + "WHERE name = '" + imageName + "';";
                PreparedStatement statement1 = connection.prepareStatement(updateImageQuery);
                statement1.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLabStatusByEventName(String eventName, String userEmail) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String event_id = "";
                Statement statement = connection.createStatement();
                Statement statement1 = connection.createStatement();
                String eventID = "Select event.id as event_id From event "
                        + "WHERE event.name = '" + eventName + "'"
                        + " and event.created_by__user_email = '" + userEmail + "'"
                        + " Order By event.id desc"
                        + " limit 1;";
                ResultSet resSet = statement1.executeQuery(event_id);
                if(resSet.next()){
                    event_id = resSet.getString("event_id");
                }
                String eventLabStatusQuery = "Select lab.status_pl as status From lab "
                        + "Where lab.event_id = '" + event_id + "' "
                        + " Order By lab.id desc"
                        + " limit 1;";
                ResultSet resSet2 = statement1.executeQuery(eventLabStatusQuery);
                if(resSet2.next()){
                    String status = resSet2.getString("status");
                    return status;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No status found";
    }

    public String getLabToolsStatusByEventName(String eventName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String eventLabStatusQuery = "Select lab.hyper_viewer_status as status From lab "
                        + "Where lab.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name = '" + eventName + "' "
                        + "AND event.created_by__user_email='" + email + "')"
                        + " Order By lab.id desc"
                        + " limit 1;";
                ResultSet resSet = statement.executeQuery(eventLabStatusQuery);
                if(resSet.next()){
                    String status = resSet.getString("status");
                    return status;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No status found";
    }

    public String getLabToolsStatusByEventName(String eventName, String email, int sequens) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String eventLabStatusQuery = "Select lab.hyper_viewer_status as status From lab "
                        + "Where lab.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name = '" + eventName + "' "
                        + "AND event.created_by__user_email='" + email + "')"
                        + "and lab.sequence='" + sequens + "' "
                        + " Order By lab.id desc"
                        + " limit 1;";
                ResultSet resSet = statement.executeQuery(eventLabStatusQuery);
                if(resSet.next()){
                    String status = resSet.getString("status");
                    return status;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No status found";
    }

    public String getLabPLStatusByEventName(String eventName, String email, int sequens) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String eventLabStatusQuery = "Select lab.status_pl as status From lab "
                        + "Where lab.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name = '" + eventName + "' "
                        + "AND event.created_by__user_email='" + email + "')"
                        + "and lab.sequence='" + sequens + "' "
                        + " Order By lab.id desc"
                        + " limit 1;";
                ResultSet resSet = statement.executeQuery(eventLabStatusQuery);
                if(resSet.next()){
                    String status = resSet.getString("status");
                    return status;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No Status found";
    }

    public String getEventStatusByName(String eventName, String userEmail) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String eventStatusQuery = "Select event.status_pl as status From event "
                        + "Where event.name = '" + eventName + "'"
                        + " and event.created_by__user_email = '" + userEmail + "';";
                ResultSet resSet = statement.executeQuery(eventStatusQuery);
                System.out.println(resSet);
                if(resSet.next()){
                    String event = resSet.getString("status");
                    return event;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No Status found";
    }
}
