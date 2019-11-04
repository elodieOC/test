package com.musers.microserviceusers.model;

import com.musers.microserviceusers.utils.Encryption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;


public class UserTest {
       /** Jeu de données */
    private User testUser;
    private Role testRole;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testUser = new User();
        testRole = new Role();
        /*ROLE*/
        testRole.setId(1);
        testRole.setRoleName("USER_TEST");
        /*USER*/
        testUser.setId(1);
        testUser.setEmail("NS.auror@mom.com");
        testUser.setFirstName("Newt");
        testUser.setLastName("Scamender");
        testUser.setPassword("niffler");
    }

    @Test(expected = Test.None.class)
    public void setGetResetToken() {
        testUser.setResetToken("resetTestToken");
        Assert.assertTrue(testUser.getResetToken().equals("resetTestToken"));
        Assert.assertFalse(testUser.getResetToken()== null);
    }

    @Test(expected = Test.None.class)
    public void getSetTokenDate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        testUser.setTokenDate(now);
        Assert.assertTrue(testUser.getTokenDate().equals(now));
    }

    @Test(expected = Test.None.class)
    public void getSetId() {
        testUser.setId(55);
        Assert.assertTrue(testUser.getId() == 55);
        Assert.assertFalse(testUser.getId() == 1);
    }

    @Test(expected = Test.None.class)
    public void getSetFirstName() {
        Assert.assertTrue(testUser.getFirstName().equals("Newt"));
        testUser.setFirstName("Loki");
        Assert.assertTrue(testUser.getFirstName().equals("Loki"));
    }

    @Test(expected = Test.None.class)
    public void getSetLastName() {
        Assert.assertTrue(testUser.getLastName().equals("Scamender"));
        testUser.setLastName("Lauffeyson");
        Assert.assertTrue(testUser.getLastName().equals("Lauffeyson"));
    }

    @Test(expected = Test.None.class)
    public void getEmail() {
        Assert.assertTrue(testUser.getEmail().equals("NS.auror@mom.com"));
        testUser.setEmail("LL.mischief@asgard.com");
        Assert.assertTrue(testUser.getEmail().equals("LL.mischief@asgard.com"));
    }

    @Test(expected = Test.None.class)
    public void getSetPassword() {
        Assert.assertTrue(testUser.getPassword().equals("niffler"));
        String encrypted = Encryption.encrypt("niffler");
        Assert.assertTrue(Encryption.encrypt(testUser.getPassword()).equals(encrypted));
        testUser.setPassword("FantasticBeasts");
        String encrypted2 = Encryption.encrypt("FantasticBeasts");
        Assert.assertTrue(Encryption.encrypt(testUser.getPassword()).equals(encrypted2));
    }

    @Test(expected = Test.None.class)
    public void getSetUserRole() {
        testUser.setUserRole(testRole);
        Assert.assertEquals("USER_TEST",testUser.getUserRole().getRoleName());
        Role newTestRole = new Role();
        newTestRole.setRoleName("ADMIN_TEST");
        newTestRole.setId(2);
        testUser.setUserRole(newTestRole);
        Assert.assertTrue(testUser.getUserRole().getRoleName().equals("ADMIN_TEST"));
    }

    @Test(expected = Test.None.class)
    public void toStringTest() {
        Assert.assertEquals("User{id=1, firstName='Newt', lastName='Scamender', email='NS.auror@mom.com'}", testUser.toString());
        Assert.assertNotEquals("User{id=55, firstName='Loki', lastName='Lauffeyson', email='LL.mischief@asgard.com'}", testUser.toString());
    }
}