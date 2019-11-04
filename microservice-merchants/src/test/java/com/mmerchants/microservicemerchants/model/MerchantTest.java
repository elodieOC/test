package com.mmerchants.microservicemerchants.model;

import com.mmerchants.microservicemerchants.utils.Encryption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class MerchantTest {

    /** Jeu de données */
    private Merchant testMerchant;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testMerchant = new Merchant();
        /*Merchant*/
        testMerchant.setId(1);
        testMerchant.setEmail("boulangerie@aufournil.fr");
        testMerchant.setMerchantName("Le Fournil");
        testMerchant.setCategory("boulangerie");
        testMerchant.setPassword("croissant"); 
        testMerchant.setAddress("15 rue du chocolat");
    }

    @Test(expected = Test.None.class)
    public void setGetResetToken() {
        testMerchant.setResetToken("resetTestToken");
        Assert.assertTrue(testMerchant.getResetToken().equals("resetTestToken"));
        Assert.assertFalse(testMerchant.getResetToken()== null);
    }

    @Test(expected = Test.None.class)
    public void getSetTokenDate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        testMerchant.setTokenDate(now);
        Assert.assertTrue(testMerchant.getTokenDate().equals(now));
    }
    
    @Test
    public void getSetId() {
        testMerchant.setId(55);
        Assert.assertTrue(testMerchant.getId() == 55);
        Assert.assertFalse(testMerchant.getId() == 1);
    }

    @Test
    public void getSetMerchantName() {
        Assert.assertTrue(testMerchant.getMerchantName().equals("Le Fournil"));
        testMerchant.setMerchantName("L'atelier du pain");
        Assert.assertTrue(testMerchant.getMerchantName().equals("L'atelier du pain"));
    }

    @Test
    public void getSetEmail() {
        Assert.assertTrue(testMerchant.getEmail().equals("boulangerie@aufournil.fr"));
        testMerchant.setEmail("atelier@dupain.fr");
        Assert.assertTrue(testMerchant.getEmail().equals("atelier@dupain.fr"));
    }

    @Test
    public void getSetCategory() {
        Assert.assertEquals("boulangerie", testMerchant.getCategory());
        testMerchant.setCategory("pizzeria");
        Assert.assertEquals("pizzeria", testMerchant.getCategory());
        Assert.assertNotEquals("boulangerie", testMerchant.getCategory());
    }

    @Test
    public void getSetAddress() {
        Assert.assertEquals("15 rue du chocolat", testMerchant.getAddress());
        testMerchant.setAddress("17 avenue de la tarte");
        Assert.assertEquals("17 avenue de la tarte", testMerchant.getAddress());
        Assert.assertNotEquals("15 rue du chocolat", testMerchant.getAddress());
    }

    @Test
    public void getSetPassword() {  
        Assert.assertTrue(testMerchant.getPassword().equals("croissant"));
        String encrypted = Encryption.encrypt("croissant");
        Assert.assertTrue(Encryption.encrypt(testMerchant.getPassword()).equals(encrypted));
        testMerchant.setPassword("paschocolatine");
        String encrypted2 = Encryption.encrypt("paschocolatine");
        Assert.assertTrue(Encryption.encrypt(testMerchant.getPassword()).equals(encrypted2));
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("Merchant{id=1, merchantName='Le Fournil', category='boulangerie', address='15 rue du chocolat'}", testMerchant.toString());
        Assert.assertNotEquals("Merchant{id=55, merchantName='Le Four', category='pizzeria', address='15 rue de la chocolatine'}", testMerchant.toString());
    }
}