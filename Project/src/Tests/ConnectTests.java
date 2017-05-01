package Tests;

import Controller.Connect;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by dajun on 4/30/17.
 */
public class ConnectTests {

    @Test
    public void databaseConnectionTest() throws SQLException, ClassNotFoundException {

        Connect connection = new Connect();
        String realUsername = "dajun";
        String correctPassword = "1234";
        String fakeUsername = "dd";
        String wrongPassword = "0000";

        assertEquals(connection.verify(realUsername,correctPassword), true);
        assertEquals(connection.verify(realUsername,wrongPassword), false);
        assertEquals(connection.verify(fakeUsername,correctPassword), true);

        connection.disconnect();
    }

}
