package com.musers.microserviceusers.controller;

import com.musers.microserviceusers.dao.RoleDao;
import com.musers.microserviceusers.dao.UserDao;
import com.musers.microserviceusers.model.Role;
import com.musers.microserviceusers.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

//    @Autowired
    private UserController userController = new UserController();
    @Mock
    private UserDao mockUserDao;
    @Mock
    private RoleDao mockRoleDao;


    /** Jeu de données */
    private User testUser;
    private List<Role> listeRoleTest;
    private Role roleTestAdmin;
    private Role roleTestUser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        listeRoleTest = new ArrayList<>();
        testUser = new User();
        roleTestAdmin = new Role();
        roleTestUser = new Role();

        // Set du jeu de données
        /*ROLE*/
        roleTestAdmin.setId(1);
        roleTestAdmin.setRoleName("ADMIN_TEST");
        roleTestUser.setId(2);
        roleTestUser.setRoleName("USER_TEST");
        listeRoleTest.add(roleTestAdmin);
        listeRoleTest.add(roleTestUser);

        /*USER*/
        testUser.setId(1);
        testUser.setEmail("test@test.com");
        testUser.setFirstName("Newt");
        testUser.setLastName("Scamender");
        testUser.setPassword("fantasticBeasts");
    }

    @Test
    public void listUsers() {
    }

    @Test
    public void addUser() {
    }

    @Test
    public void showUser() {
    }

    @Test
    public void logUser() {
    }

    @Test
    public void findUserForPassword() {
    }

    @Test
    public void findUserByToken() {
    }

    @Test
    public void findUserByTokenAndSetsNewPassword() {
    }
}