import Cruise.*;
import Person.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class PasswordResetTest {
    @BeforeEach
    public void backupDB()
    {
        try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase")) {
            CallableStatement cs = connection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
            cs.setString(1, "backup");
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    public void restoreDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:cruiseDatabase;shutdown=true");

        } catch (SQLException e) {
            //Shutting down db always produces SQLException
            //e.printStackTrace();
        }

        try {
            replaceDatabaseWithBackup();

            // Restart Derby
            DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void replaceDatabaseWithBackup() {
        try {
            File databaseDir = new File("cruiseDatabase");
            File backupDir = new File("backup/cruiseDatabase");

            FileUtils.copyDirectory(backupDir, databaseDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testResetFailure() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();

        Guest.resetRequest("test", "newPassword");

        Admin admin = new Admin("testAdmin", "test", "test", "test", "test");
        admin.createAccount();

        Collection<Guest> guests = admin.getResetRequests();

        for (Guest thing : guests)
        {
            if (thing.getUsername().equals("test"))
            {
                thing.resetPassword();
            }
        }

        Optional<Person> newGuest = Person.login("test", "test");

        assertFalse(newGuest.isPresent());
    }


    @Test
    void testResetSuccess() {
        Guest guest = new Guest("test", "test", "test", "test", "test");
        guest.createAccount();

        Guest.resetRequest("test", "newPassword");

        Admin admin = new Admin("testAdmin", "test", "test", "test", "test");
        admin.createAccount();

        Collection<Guest> guests = admin.getResetRequests();

        for (Guest thing : guests)
        {
            if (thing.getUsername().equals("test"))
            {
                thing.resetPassword();
            }
        }

        Optional<Person> newGuest = Person.login("test", "newPassword");

        assertTrue(newGuest.isPresent());


    }
}