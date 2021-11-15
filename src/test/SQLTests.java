import DBAccess.DataBaseManager;
import org.testng.annotations.Test;


import java.sql.SQLException;

public class SQLTests {

    @Test
    public void selectDataFromDatabase(){

    }

    @Test
    public void checkIfUserInDataBase() throws SQLException {
        DataBaseManager data = new DataBaseManager();
        data.getAccountRoleById("Employee");

    }

    @Test
    public void checkUserImgs() throws SQLException, ClassNotFoundException {
    DataBaseManager data = new DataBaseManager();
    data.getCountOfImagesPerUser("Alex", "Smith", "alex@gmail.com");
    }

    @Test
    public void checkIfUserExists() throws SQLException {
        DataBaseManager data = new DataBaseManager();
        data.checkIfUserExists("testuser1@gmail.com", "Adam");
    }


}
