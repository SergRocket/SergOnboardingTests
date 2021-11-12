package DBAccess;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.qameta.allure.util.PropertiesUtils;
import oracle.ucp.proxy.annotation.Pre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static oracle.security.pki.resources.OraclePKICmd.c;

public class DataBaseManager {

    Connection connection = null;
    static Session SSHSession = null;

    public DataBaseManager() throws SQLException {
    }

    private static void makeSshTunnel(String stringSshuser, String stringSshPassword, String stringSshHost,
                                      int sshPort, String stringRemoteHost, int localPort, int remotePort) throws JSchException {
        final JSch jsch = new JSch();
        Session session = jsch.getSession(stringSshuser, stringSshHost, 22);
        session.setPassword(stringSshPassword);

        final Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        //session.setConfig(configuration);
        session.connect();
        session.setPortForwardingL(localPort, stringRemoteHost, remotePort);
        SSHSession = session;
    }

    public Connection getConnectionStr(SetOfKeys keysSet) {
        if (connection == null || SSHSession == null) {
            try {
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
                try {
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
                        .replace("http://", "").length() - 5) + ":" + keys
                .getPort() + "/axis", "axis", "axis");
        return connect;
    }

    public int getCountOfEventsPerUser(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionStr(key);
                Statement st = connection.createStatement();
                String sqlQueryForAllEvents = "Select Count(*) AS total from event " + "Where event.account_entity_id = (Select app_user.account_entity_id From app_user "
                        + "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName + "' and app_user.last_name = '"
                        + lastName + "');";
                ResultSet resSet = st.executeQuery(sqlQueryForAllEvents);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfTemplatesPerUser(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys k = SetOfKeys.findKeysByUrl(url);
            if (k != null) {
                Connection connection = getConnectionString(k);
                Statement st = connection.createStatement();
                String sqlQueryForAllTemplates = "Select Count(*) AS total from template "
                        + "Where template.account_entity_id = (Select app_user.account_entity_id From app_user "
                        + "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName
                        + "' and app_user.last_name = '" + lastName + "');";
                ResultSet resSet = st.executeQuery(sqlQueryForAllTemplates);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfLabPlansPerUser(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total from lab_plan "
                        + "Where lab_plan.account_entity_id = (Select app_user.account_entity_id From app_user "
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

    public int getCountOfImagesPerUserAndOSType(String firstName, String lastName, String email, String osType) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total from image "
                        + "Where image.os_pl = '" + osType.toUpperCase() + "' and"
                        + "  image.account_entity_id = (Select app_user.account_entity_id From app_user "
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

    public int getCountOfImagesPerUser(String firstName, String lastName, String email) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/

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

    public void deleteEventFromDBByName(String eventName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                String eventStatus = null;
                Statement statement = connection.createStatement();
                String eventStatusQuery = "Select event.status_pl as status From event "
                        + "Where event.name = '" + eventName + "';";
                ResultSet resSet = statement.executeQuery(eventStatusQuery);
                try {
                    if (resSet.next()) {
                        eventStatus = resSet.getString("status");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (eventStatus.equals("CANCELLED") || eventStatus.equals("FINISHED")) {
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

    public void deleteCourseTemplateFromDBByName(String courseTemplateName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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

    public void deleteLabPlanFromDBByName(String labPlanName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            imageName = imageName.replaceAll("'", "''");
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
                if (resSet.next()) {
                    event_id = resSet.getString("event_id");
                }
                String eventLabStatusQuery = "Select lab.status_pl as status From lab "
                        + "Where lab.event_id = '" + event_id + "' "
                        + " Order By lab.id desc"
                        + " limit 1;";
                ResultSet resSet2 = statement1.executeQuery(eventLabStatusQuery);
                if (resSet2.next()) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String eventLabStatusQuery = "Select lab.hyper_viewer_status as status From lab "
                        + "Where lab.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name = '" + eventName + "' "
                        + "AND event.created_by__user_email='" + email + "')"
                        + " Order By lab.id desc"
                        + " limit 1;";
                ResultSet resSet = statement.executeQuery(eventLabStatusQuery);
                if (resSet.next()) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
                if (resSet.next()) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
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
                if (resSet.next()) {
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
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String eventStatusQuery = "Select event.status_pl as status From event "
                        + "Where event.name = '" + eventName + "'"
                        + " and event.created_by__user_email = '" + userEmail + "';";
                ResultSet resSet = statement.executeQuery(eventStatusQuery);
                System.out.println(resSet);
                if (resSet.next()) {
                    String event = resSet.getString("status");
                    return event;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No Status found";
    }

    public String getResourceCreationBlockingFlagStatusByPCName(String pcName, String userEmail) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String imageVerificationStatusQuery = "Select provider_config.blocking_flags as bf From provider_config "
                        + "Where provider_config.name = '" + pcName + "'" + " and provider_config.created_by__user_email = '" + userEmail + "';";
                ResultSet resSet = statement.executeQuery(imageVerificationStatusQuery);
                if (resSet.next()) {
                    String bfStatus = resSet.getString("bf");
                    return bfStatus;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No Status found";
    }

    public String getImageVerificationStatusByName(String imageName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String imageVerificationStatusQuery = "Select image.verification_status as status From image "
                        + "Where image.name = '" + imageName + "' "
                        + "AND image.created_by__user_email = '" + email + "';";
                ResultSet resSet = statement.executeQuery(imageVerificationStatusQuery);
                if (resSet.next()) {
                    String verification_status = resSet.getString("status");
                    return verification_status;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "no Status found";
    }

    public int getCountOfLabsPerEvent(String eventName, String userEmail) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForSeatsCount = "Select Count(*) as total From lab "
                        + "WHERE lab.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name = '" + eventName + "'"
                        + " and event.created_by__user_email = '" + userEmail + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForSeatsCount);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfSeatsPerEvent(String eventName, String userEmail) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForSeatsCount = "Select Count(*) as total From seat "
                        + "WHERE seat.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name = '" + eventName + "'"
                        + " and event.created_by__user_email = '" + userEmail + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForSeatsCount);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfActionsByEventName(String eventName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForLogActionsPerEventName = "Select Count(*) as total From audit_log "
                        + "Where audit_log.model_type = 'class com.readytech.axis.domain.Event' AND "
                        + "audit_log.model_name = '" + eventName + "' "
                        + "AND audit_log.user_email = '" + email + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForLogActionsPerEventName);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfActionsByCourseTemplateName(String courseTemplateName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForLogActionsPerCT = "Select Count(*) as total From audit_log "
                        + "Where audit_log.model_type = 'class com.readytech.axis.domain.Template' AND "
                        + "audit_log.model_name = '" + courseTemplateName + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForLogActionsPerCT);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfActionsByLabPlanName(String labPlanName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForLogActionsPerPC = "Select Count(*) as total From audit_log "
                        + "Where audit_log.model_type = 'class com.readytech.axis.domain.LabPlan' AND "
                        + "audit_log.model_name = '" + labPlanName + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForLogActionsPerPC);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfActionsByImageName(String imageName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForLogActionsPerPC = "Select Count(*) as total From audit_log "
                        + "Where audit_log.model_type = 'class com.readytech.axis.domain.images.Image' AND "
                        + "audit_log.model_name ='" + imageName + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForLogActionsPerPC);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfActionsByAccountName(String accountLastName, String userEmail) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForLogActionsPerAcciunt = "Select Count(*) as total From audit_log "
                        + "Where audit_log.account_Entity_id = "
                        + "(SELECT app_user.account_entity_id FROM app_user where app_user.last_name = '" + accountLastName + "'"
                        + " and app_user.email = '" + userEmail + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForLogActionsPerAcciunt);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfActionsByPCName(String pcName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForLogActionsPerPC = "Select Count(*) as total From audit_log "
                        + "Where audit_log.model_type = 'class com.readytech.axis.domain.ProviderConfig' AND "
                        + "audit_log.model_name = '" + pcName + "' "
                        + "AND audit_log.user_email = '" + email + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForLogActionsPerPC);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfRegionsByPCName(String pcName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForRegionsPerPC = "Select Count(*) AS total From provider_config_region_settings "
                        + "Where provider_config_region_settings.provider_config_id = (Select provider_config.id From provider_config "
                        + "Where provider_config.name = '" + pcName + "' "
                        + "and status_pl = 'ENABLED'"
                        + "and provider_config.created_by__user_email = '" + email + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForRegionsPerPC);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountOfPCsByUser(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total From provider_config "
                        + "Where provider_config.account_entity_id = (Select app_user.account_entity_id From app_user "
                        + "Where app_user.email ='" + email + "' and app_user.first_name = '" + firstName
                        + "' and app_user.last_name = '" + lastName + "');";
                ResultSet resSet = statement.executeQuery(sqlQueryForAllPCs);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    return counter;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteUserFromDBByName(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                String sqlQueryForAccountEntityUserRole = "Delete FROM account_entity_user_role "
                        + "WHERE user_id = (Select app_user.id From app_user " + "Where app_user.email='"
                        + email + "' and app_user.first_name = '" + firstName + "' and app_user.last_name = '"
                        + lastName + "');";
                PreparedStatement statement = connection.prepareStatement(sqlQueryForAccountEntityUserRole);
                statement.executeUpdate();

                String sqlQueryForInvitationKey = "Delete FROM invitation_data "
                        + "Where invitation_data.user_id= (Select app_user.id From app_user " + "Where app_user.email='"
                        + email + "' and app_user.first_name = '" + firstName + "' and app_user.last_name = '"
                        + lastName + "');";
                PreparedStatement statement3 = connection.prepareStatement(sqlQueryForInvitationKey);
                statement3.executeUpdate();

                String sqlQueryForUser = "Delete FROM app_user " + "Where app_user.email='" + email + "' and app_user.first_name = '"
                        + firstName + "' and app_user.last_name = '" + lastName + "';";
                PreparedStatement statement2 = connection.prepareStatement(sqlQueryForUser);
                statement2.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfUserDoesNotExistInDB(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForUserCheck = "Select Count(*) as Total From app_user " + "Where app_user.first_name = '"
                        + firstName + "' and app_user.last_name = '" + lastName + "' and app_user.email = '" + email + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForUserCheck);
                if (resSet.next()) {
                    int counter = resSet.getInt("total");
                    if (counter > 0) {
                        return false;
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void cleanUpDataBaseByProviderConnectionName(String providerConnectionName) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            providerConnectionName = providerConnectionName.replaceAll("'", "''");
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                String labQuery = "Delete FROM lab WHERE lab.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '"
                        + providerConnectionName + "'));";
                PreparedStatement statement1 = connection.prepareStatement(labQuery);
                statement1.executeUpdate();

                String seatQuery = "Delete FROM seat WHERE seat.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.provider_config_id = (Select provider_config.id AS provider_id	From provider_config "
                        + "Where provider_config.name = '"
                        + providerConnectionName + "'));";
                PreparedStatement statement2 = connection.prepareStatement(seatQuery);
                statement2.executeUpdate();

                String eventItemQuery = "Delete FROM event_item	WHERE event_item.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.provider_config_id = (Select provider_config.id AS provider_id	From provider_config "
                        + "Where provider_config.name = '"
                        + providerConnectionName + "'));";
                PreparedStatement statement3 = connection.prepareStatement(eventItemQuery);
                statement3.executeUpdate();

                String eventQuery = "DELETE FROM event WHERE event.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '" + providerConnectionName + "');";
                PreparedStatement statement4 = connection.prepareStatement(eventQuery);
                statement4.executeUpdate();

                String templateQuery = "DELETE FROM template WHERE template.lab_plan_id = (Select lab_plan.id AS lab_id	From lab_plan "
                        + "Where lab_plan.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '" + providerConnectionName + "'));";
                PreparedStatement statement5 = connection.prepareStatement(templateQuery);
                statement5.executeUpdate();

                String labPlanItemQuery = "DELETE FROM lab_plan_item WHERE lab_plan_item.id = (Select lab_plan_item.id AS lab_item_id FROM lab_plan_item "
                        + "WHERE lab_plan_item.lab_plan_id= (Select lab_plan.id AS lab_id	From lab_plan "
                        + "Where lab_plan.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '" + providerConnectionName + "')));";
                PreparedStatement statement6 = connection.prepareStatement(labPlanItemQuery);
                statement6.executeUpdate();

                String labPlanQuery = "DELETE FROM lab_plan WHERE lab_plan.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '"
                        + providerConnectionName + "');";
                PreparedStatement statement7 = connection.prepareStatement(labPlanQuery);
                statement7.executeUpdate();

                String imageQuery = "DELETE FROM image WHERE image.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '"
                        + providerConnectionName + "');";
                PreparedStatement statement8 = connection.prepareStatement(imageQuery);
                statement8.executeUpdate();

                String uuid = "";
                try {
                    Statement statement = connection.createStatement();
                    String sqlQueryForCloud_Provider_VDI_UUID = "Select cloud_provider_vdi.uuid as uuid From cloud_provider_vdi "
                            + "WHERE cloud_provider_vdi.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                            + "Where provider_config.name = '"
                            + providerConnectionName + "');";
                    ResultSet resSet = statement.executeQuery(sqlQueryForCloud_Provider_VDI_UUID);
                    if (resSet.next()) {
                        uuid = resSet.getString("uuid");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

                String cloudProviderVdiQuery = "DELETE FROM cloud_provider_vdi WHERE cloud_provider_vdi.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '" + providerConnectionName + "');";
                PreparedStatement statement9 = connection.prepareStatement(cloudProviderVdiQuery);
                statement9.executeUpdate();


                String command_bus_command_Query = "DELETE FROM command_bus_command WHERE command_bus_command.command LIKE '"
                        + "%" + uuid + "%' ;";
                PreparedStatement statement10 = connection.prepareStatement(command_bus_command_Query);
                statement10.executeUpdate();

                String command_bus_process_Query = "DELETE FROM command_bus_process WHERE command_bus_process.process_starter_command LIKE '"
                        + "%" + uuid
                        + "%' ;";
                PreparedStatement statement11 = connection.prepareStatement(command_bus_process_Query);
                statement11.executeUpdate();

                String providerConfigQuery = "DELETE FROM provider_config WHERE provider_config.name = '" + providerConnectionName
                        + "';";
                PreparedStatement statement12 = connection.prepareStatement(providerConfigQuery);
                statement12.executeUpdate();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cleanUpImageAndPCAfterVerification(String pcName, String imageName) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            String verifEventName = "VERIFICATION_" + imageName;
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                String labPlanItemQuery = "DELETE FROM lab_plan_item WHERE lab_plan_item.image_id = (Select image.id AS image_id FROM image "
                        + "Where image.name = '" + imageName + "');";
                PreparedStatement statement1 = connection.prepareStatement(labPlanItemQuery);
                statement1.executeUpdate();

                String imageVerifQuery = "DELETE FROM image_verification WHERE image_verification.provider_config_name = '"
                        + pcName + "';";
                PreparedStatement statement2 = connection.prepareStatement(imageVerifQuery);
                statement2.executeUpdate();

                String imageQuery = "DELETE FROM image WHERE image.name = '" + imageName + "';";
                PreparedStatement statement3 = connection.prepareStatement(imageQuery);
                statement3.executeUpdate();

                String labQuery = "Delete FROM lab WHERE lab.image__name = '" + imageName + "';";
                PreparedStatement statement4 = connection.prepareStatement(labQuery);
                statement4.executeUpdate();

                String eventItemQuery = "Delete FROM event_item	WHERE event_item.image__name = '" + imageName + "';";
                PreparedStatement statement5 = connection.prepareStatement(eventItemQuery);
                statement5.executeUpdate();

                String uuid = "";
                try {
                    Statement statement = connection.createStatement();
                    String sqlQueryForCloud_Provider_VDI_UUID = "Select cloud_provider_vdi.uuid as uuid From cloud_provider_vdi "
                            + "WHERE cloud_provider_vdi.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                            + "Where provider_config.name = '"
                            + pcName + "');";
                    ResultSet resSet = statement.executeQuery(sqlQueryForCloud_Provider_VDI_UUID);
                    if (resSet.next()) {
                        uuid = resSet.getString("uuid");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                String cloudProviderVDIQuery = "Delete FROM cloud_provider_vdi "
                        + "WHERE cloud_provider_vdi.provider_config_id = (Select provider_config.id AS provider_id From provider_config "
                        + "Where provider_config.name = '" + pcName + "');";
                PreparedStatement statement6 = connection.prepareStatement(cloudProviderVDIQuery);
                statement6.executeUpdate();

                String command_bus_command_Query = "DELETE FROM command_bus_command WHERE command_bus_command.command LIKE '"
                        + "%" + uuid + "%' ;";
                PreparedStatement statement7 = connection.prepareStatement(command_bus_command_Query);
                statement7.executeUpdate();

                String command_bus_process_Query = "DELETE FROM command_bus_process WHERE command_bus_process.process_starter_command LIKE '"
                        + "%" + uuid + "%' ;";
                PreparedStatement statement8 = connection.prepareStatement(command_bus_process_Query);
                statement8.executeUpdate();

                String auditLogQuery = "Delete FROM audit_log WHERE audit_log.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name LIKE '" + verifEventName + "%' LIMIT 1);";
                PreparedStatement statement9 = connection.prepareStatement(auditLogQuery);
                statement9.executeUpdate();

                String labQuery1 = "Delete FROM lab WHERE lab.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name LIKE '" + verifEventName + "%' LIMIT 1);";
                PreparedStatement statement10 = connection.prepareStatement(labQuery1);
                statement10.executeUpdate();

                String seatInstructorQuery = "Delete FROM event_seat_instructor WHERE event_seat_instructor.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name LIKE '" + verifEventName + "%' LIMIT 1);";
                PreparedStatement statement11 = connection.prepareStatement(seatInstructorQuery);
                statement11.executeUpdate();

                String seatQuery = "Delete FROM seat WHERE seat.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name LIKE '" + verifEventName + "%' LIMIT 1);";
                PreparedStatement statement12 = connection.prepareStatement(seatQuery);
                statement12.executeUpdate();

                String eventItemQuery1 = "Delete FROM event_item	WHERE event_item.event_id = (Select event.id AS event_id FROM event "
                        + "WHERE event.name LIKE '" + verifEventName + "%' LIMIT 1);";
                PreparedStatement statement13 = connection.prepareStatement(eventItemQuery1);
                statement13.executeUpdate();

                String eventQuery = "DELETE FROM event Where event.name IN " + "(SELECT event.name FROM event " + "WHERE event.name LIKE '"
                        + verifEventName + "%' LIMIT 1)";
                PreparedStatement statement14 = connection.prepareStatement(eventQuery);
                statement14.executeUpdate();

                String ctQuery = "DELETE FROM template WHERE template.name LIKE '" + verifEventName + "%';";
                PreparedStatement statement15 = connection.prepareStatement(ctQuery);
                statement15.executeUpdate();

                String labPlanQuery = "DELETE FROM lab_plan WHERE lab_plan.name LIKE '" + verifEventName + "%';";
                PreparedStatement statement16 = connection.prepareStatement(labPlanQuery);
                statement16.executeUpdate();

                String providerConfigQuery = "DELETE FROM provider_config WHERE provider_config.name = '" + pcName + "';";
                PreparedStatement statement17 = connection.prepareStatement(providerConfigQuery);
                statement17.executeUpdate();

            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public String getResetPassKeyByAccountCreds(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForResetPassKey = "Select password_reset_data.reset_key as key " + "password_reset_data "
                        + "Where password_reset_data.user_id = (Select app_user.id AS user_id FROM app_user "
                        + "Where app_user.first_name = '" + firstName + "' and app_user.user_last_name = '"
                        + lastName + "' and app_user.email = '" + email + "')"
                        + " order by password_reset_data.user_id desc limit 1";
                ResultSet resSet= statement.executeQuery(sqlQueryForResetPassKey);
                if(resSet.next()){
                    String keys = resSet.getString("key");
                    return keys;
                }

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "Bad query";
    }

    public String getCloudProviderVD(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForResetPassKey = "Select password_reset_data.reset_key as key "
                        + "password_reset_data " + "Where password_reset_data.user_id = (Select app_user.id AS user_id FROM app_user "
                        + "Where app_user.first_name = '" + firstName + "' and app_user.user_last_name = '"
                        + lastName + "' and app_user.email = '" + email + "')"
                        + " order by password_reset_data.user_id desc limit 1";
                ResultSet resSet = statement.executeQuery(sqlQueryForResetPassKey);
                if(resSet.next()){
                    String keys = resSet.getString("key");
                    return keys;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "Bad qurey";
    }

    public String getActivationKeyByAccountCreds(String accountName, String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForActivationKey = "Select pending_registration_data.activation_key as key "
                        + "From pending_registration_data "
                        + "Where pending_registration_data.account_name = '" + accountName
                        + "' and pending_registration_data.user_first_name= '" + firstName
                        + "' and pending_registration_data.user_last_name = '"  + lastName
                        + "' and pending_registration_data.user_email = '" + email
                        + "' order by pending_registration_data.id desc limit 1";
                ResultSet resSet = statement.executeQuery(sqlQueryForActivationKey);
                if(resSet.next()){
                    String keys = resSet.getString("key");
                    return keys;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "Bad query";
    }

    public String getAccountIdByAccountCreds(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAccountId = "Select account.code as key " + "From account "
                        + "Where account.owner_id = " + "(Select app_user.id From app_user "
                        + "Where app_user.first_name= '" + firstName + "' and app_user.last_name = '"
                        + lastName + "' and app_user.email = '" + email + "' order by app_user.id desc limit 1)";
                ResultSet resSet = statement.executeQuery(sqlQueryForAccountId);
                if(resSet.next()){
                    String keys = resSet.getString("key");
                    return keys;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "Bad query";
    }

    public String getInvitationKeyByAccountCreds(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForInvitationKey = "Select invitation_data.invitation_key as key "
                        + "From invitation_data " + "Where invitation_data.user_id = " + "(Select app_user.id From app_user "
                        + "Where app_user.first_name= '" + firstName + "' and app_user.last_name = '" + lastName
                        + "' and app_user.email = '" + email
                        + "' order by app_user.id desc limit 1) order by invitation_data.id desc limit 1";
                ResultSet resSet = statement.executeQuery(sqlQueryForInvitationKey);
                if(resSet.next()){
                    String keys = resSet.getString("key");
                    return keys;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return "Bad query";
    }

    public int getCountOfUsersByPartialUserName(String partialFirstName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total From app_user "
                        + "Where first_name LIKE '" + partialFirstName + '%' + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForAllPCs);
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

    public void updateUserRecord(String firstName, String lastName, String email) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String updateUserQuery = "UPDATE app_user " + "SET first_name='1', last_name='1', email='1' "
                        + "WHERE first_name='" + firstName + "' and last_name='" + lastName + "' and email='"
                        + email + "';";
                statement.executeQuery(updateUserQuery);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCountOfUsersByPartialUserNameAndStatus(String partialFirstName, String status) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String sqlQueryForAllPCs = "Select Count(*) AS total From app_user "
                        + "Where first_name LIKE '" + partialFirstName + '%' + "' and status_pl= '" + status + "';";
                ResultSet resSet = statement.executeQuery(sqlQueryForAllPCs);
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

    public void deleteUserFromDBByName(String userName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String labPlanItemQuery = "DELETE FROM account_entity_user_role WHERE account_entity_user_role.user_id = "
                        + "(Select app_user.id AS user_id From app_user Where app_user.first_name = ?)";
                PreparedStatement statement2 = connection.prepareStatement(labPlanItemQuery);
                statement2.setString(1, userName);
                statement2.executeUpdate();
                String deleteLabPlanQuery = "DELETE FROM app_user WHERE app_user.first_name = ?";
                PreparedStatement statement = connection.prepareStatement(deleteLabPlanQuery);
                statement.setString(1, userName);
                statement.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserFromDBByNameContains(String userName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if(key != null){
                Connection connection = getConnectionString(key);
                String deleteLabPlanQuery = "DELETE FROM app_user WHERE app_user.first_name LIKE " + "'%" + userName+ "%'";
                PreparedStatement statement = connection.prepareStatement(deleteLabPlanQuery);
                statement.setString(1, userName);
                statement.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAccountCodeByName(String accountName) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "http://172.16.10.62:8081";
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String accountCodeQuery = "Select account.code as acc_code From account "
                        + "Where account.name = '" + accountName + "';";
                ResultSet resSet = statement.executeQuery(accountCodeQuery);
                if(resSet.next()){
                    String event = resSet.getString("acc_code");
                    return event;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No status found";
    }

    public void deleteAllElementsOnAccountByAccountName(String name) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                String accountEntityId = "";
                List<String> providedConnectionIdList = new ArrayList<String>();
                List<String> imageIdList = new ArrayList<String>();
                List<String> labPlanIdList = new ArrayList<String>();
                List<String> eventIdList = new ArrayList<String>();
                Statement statement = connection.createStatement();
                String queryAccountEntityID = "select account.owner_id as ownerID From account Where account.name='"
                        + name +"';";
                String queryForProviderConnectionIdsList = "";
                String queryForImageIdsList = "";
                String queryForLabPlanIdsList = "";
                String queryForEventIdsList = "";
                String queryForLabDelete = "";
                String queryForSeatDelete = "";
                String queryForImageVerificationDelete = "";
                String queryForLabPlanItemDelete = "";
                ResultSet accountEntIdResult = statement.executeQuery(queryAccountEntityID);
                if(accountEntIdResult.next()){
                    accountEntityId = accountEntIdResult.getString("ownerID");
                    queryForProviderConnectionIdsList =
                            "Select provider_config.id from provider_config where provider_config.account_entity_id = "
                                    + accountEntityId;
                    ResultSet providerConnectidResult = statement.executeQuery(queryForProviderConnectionIdsList);
                    while(providerConnectidResult.next()){
                        providedConnectionIdList.add(providerConnectidResult.getString("id"));
                    }
                    queryForImageIdsList =
                            "select image.id From image where image.account_entity_id = " + accountEntityId;
                    ResultSet imageidResult = statement.executeQuery(queryForImageIdsList);
                    while(imageidResult.next()){
                        imageIdList.add(imageidResult.getString("id"));
                    }

                    queryForLabPlanIdsList = "select lab_plan.id From lab_plan where lab_plan.account_entity_id = "
                            + accountEntityId;
                    ResultSet labplanIdResult = statement.executeQuery(queryForLabPlanIdsList);
                    while(labplanIdResult.next()){
                        labPlanIdList.add(labplanIdResult.getString("id"));
                    }

                    queryForEventIdsList = "select event.id From event where event.account_entity_id = "
                            + accountEntityId;
                    ResultSet eventIdResult = statement.executeQuery(queryForEventIdsList);
                    while(eventIdResult.next()){
                        eventIdList.add(eventIdResult.getString("id"));
                    }

                    for(String id : providedConnectionIdList){
                        queryForLabDelete = "delete from lab where lab.provider_config_id = " + id;
                        PreparedStatement statement1 = connection.prepareStatement(queryForLabDelete);
                        statement1.executeUpdate();
                    }

                    for(String id : eventIdList){
                        queryForSeatDelete = "delete from seat where seat.event_id = " + id;
                        PreparedStatement statement1 = connection.prepareStatement(queryForSeatDelete);
                        statement1.executeUpdate();
                    }

                    for(String id : imageIdList){
                        queryForImageVerificationDelete = "delete from image_verification where image_verification.image_id = " + id;
                        PreparedStatement statement2 = connection.prepareStatement(queryForImageVerificationDelete);
                        statement2.executeUpdate();
                    }

                    String queryForEventDelete = "delete from event where event.account_entity_id = " + accountEntityId;
                    PreparedStatement statement2 = connection.prepareStatement(queryForEventDelete);
                    statement2.executeUpdate();

                    String queryForTemplateDelete = "delete from template where template.account_entity_id = " + accountEntityId;
                    PreparedStatement statement3 = connection.prepareStatement(queryForTemplateDelete);
                    statement3.executeUpdate();

                    for(String id : labPlanIdList){
                        queryForLabPlanItemDelete = "delete from lab_plan_item where lab_plan_item.lab_plan_id = " + id;
                        PreparedStatement statement1 = connection.prepareStatement(queryForLabPlanItemDelete);
                        statement1.executeUpdate();
                    }

                    String queryForLabPlanDelete = "delete from lab_plan where lab_plan.account_entity_id = " + accountEntityId;
                    PreparedStatement statement4 = connection.prepareStatement(queryForLabPlanDelete);
                    statement4.executeUpdate();

                    String queryForImageDelete = "delete from image where image.account_entity_id = " + accountEntityId;
                    PreparedStatement statement5 = connection.prepareStatement(queryForImageDelete);
                    statement5.executeUpdate();

                    String queryForProviderConnectionDelete = "delete from provider_config where provider_config.account_entity_id = "
                            + accountEntityId;
                    PreparedStatement statement6 = connection.prepareStatement(queryForProviderConnectionDelete);
                    statement6.executeUpdate();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStatusPlLab(){
        try{
            Class.forName("org.postgresql.Driver");
            String url = null;
                   /* PropertiesUtils.getProp(PropKeys.PROD_URL
                    .getPropName());*/
            SetOfKeys key = SetOfKeys.findKeysByUrl(url);
            if (key != null) {
                Connection connection = getConnectionString(key);
                Statement statement = connection.createStatement();
                String eventStatusQuery = "SELECT status_pl FROM lab where id IN (SELECT max(id)-2 FROM lab)";
                ResultSet resSet = statement.executeQuery(eventStatusQuery);
                System.out.println(resSet);
                if(resSet.next()){
                    String event = resSet.getString("status_pl");
                    return event;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "No status found";
    }
}
