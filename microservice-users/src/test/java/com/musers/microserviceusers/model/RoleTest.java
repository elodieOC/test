package com.musers.microserviceusers.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class RoleTest {
    /** Jeu de données */
    private User userNewt;
    private User userLoki;
    private Role testRole;
    private Role testRoleAdmin;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testRole = new Role();
        testRoleAdmin = new Role();

        userNewt = new User();
        userLoki = new User();

        /*ROLES*/
        //role 1
        testRole.setId(1);
        testRole.setRoleName("USER_TEST");
        //role 2
        testRoleAdmin.setId(2);
        testRoleAdmin.setRoleName("ADMIN_TEST");
        /*USERS*/
        //user 1
        userNewt.setId(1);
        userNewt.setEmail("NS.auror@mom.com");
        userNewt.setFirstName("Newt");
        userNewt.setLastName("Scamender");
        //user 2
        userLoki.setId(2);
        userLoki.setEmail("LL.mischief@asgard.com");
        userLoki.setFirstName("Loki");
        userLoki.setLastName("Lauffeyson");
    }
    @Test(expected = Test.None.class)
    public void getSetUsers() {
        List<User> userListRoleUser = new ArrayList<>();
        userListRoleUser.add(userLoki);
        userListRoleUser.add(userNewt);
        testRole.setUsers(userListRoleUser);
        Assert.assertEquals(2, testRole.getUsers().size());
        Assert.assertNotEquals(1, testRole.getUsers().size());
    }

    @Test(expected = Test.None.class)
    public void getSetId() {
        Assert.assertTrue(testRole.getId() == 1);
        Assert.assertTrue(testRoleAdmin.getId() == 2);
        testRole.setId(55);
        Assert.assertTrue(testRole.getId() != 1);
    }

    @Test(expected = Test.None.class)
    public void getSetRoleName() {
        Assert.assertEquals("USER_TEST", testRole.getRoleName());
        testRole.setRoleName("NEW_ROLE_NAME");
        Assert.assertNotEquals("USER_TEST", testRole.getRoleName());
        Assert.assertEquals("NEW_ROLE_NAME", testRole.getRoleName());
    }

    @Test(expected = Test.None.class)
    public void toStringTest(){
        Assert.assertEquals("Role{roleName='USER_TEST'}", testRole.toString());
        Assert.assertNotEquals("Role{roleName='USER_TEST'}", testRoleAdmin.toString());
    }

}