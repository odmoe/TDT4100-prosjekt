package tdt4100_gg_prosjekt;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tdt4100_gg_prosjekt.FileHandler;

public class FileHandlerTest {


    private FileHandler fileHandler = FileHandler.getInstance();

    private static boolean setUpIsDone = false;
    //modifisert setup-metode så den kun kjører én gang
    @BeforeEach
    public void setup() throws FileNotFoundException {
        if (setUpIsDone) {
            return;
        }
        fileHandler.newUserToFile("TestUser1", "TestPassword1");
        fileHandler.newUserToFile("TestUser2", "TestPassword2");
        setUpIsDone = true;
    }

    
    @Test
    public void testGetUsersFromFile() throws FileNotFoundException{
        assertDoesNotThrow(() -> fileHandler.getUsersFromFile("Password").get("TestUser1"));
        assertEquals("TestPassword1", fileHandler.getUsersFromFile("Password").get("TestUser1"));
        assertEquals("TestPassword1", fileHandler.getUsersFromFile("Password").get("TestUser1"));
        assertEquals("TestPassword2", fileHandler.getUsersFromFile("Password").get("TestUser2"));
        assertEquals("100", fileHandler.getUsersFromFile("Balance").get("TestUser1"));
        assertEquals("100", fileHandler.getUsersFromFile("Balance").get("TestUser2"));
    }


    @Test
    public void testDoesUserNameAlreadyExist() throws FileNotFoundException{
        assertEquals(true, fileHandler.doesUserNameAlreadyExist("TestUser1"));
        assertEquals(false, fileHandler.doesUserNameAlreadyExist("TestUser"));
    }

    @Test
    public void testCheckUser(){
        assertEquals(true, fileHandler.checkUser("TestUser1", "TestPassword1"));
        assertEquals(true, fileHandler.checkUser("TestUser2", "TestPassword2"));
        assertEquals(false, fileHandler.checkUser("TestUser3", "TestPassword3"));
        assertEquals(false, fileHandler.checkUser("TestUser1", "TestPassword2"));
        assertEquals(false, fileHandler.checkUser("TestUser2", "TestPassword1"));
    }

    @Test
    public void testNewBalance() throws FileNotFoundException{
        fileHandler.newBalance("TestUser1", "500");
        assertEquals("500", fileHandler.getUsersFromFile("Balance").get("TestUser1"));
        fileHandler.newBalance("TestUser2", "700");
        assertEquals("700", fileHandler.getUsersFromFile("Balance").get("TestUser2"));
    }

    // @AfterEach
    // public void clearTest() throws FileNotFoundException{
    //     fileHandler.writeToFile("src/main/resources/oskar_og_tallak_prosjekt/Users.txt", "", false);
    //     fileHandler.newUserToFile("Sensor","prosjekt");
    // }
}