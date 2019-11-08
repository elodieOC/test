package com.mmerchants.microservicemerchants.model;

import com.mmerchants.microservicemerchants.utils.Encryption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;

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
        testMerchant.setAddress("15 rue du chocolat");
        testMerchant.setUserId(2);
    }

    
    @Test
    public void getSetId() {
        testMerchant.setId(55);
        Assert.assertTrue(testMerchant.getId() == 55);
        Assert.assertFalse(testMerchant.getId() == 1);
    }

    @Test
    public void getSetUserId() {
        Assert.assertTrue(testMerchant.getUserId() == 2);
        testMerchant.setUserId(55);
        Assert.assertTrue(testMerchant.getUserId() == 55);
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
    public void toStringTest() {
        Assert.assertEquals("Merchant{id=1, merchantName='Le Fournil', category='boulangerie', address='15 rue du chocolat'}", testMerchant.toString());
        Assert.assertNotEquals("Merchant{id=55, merchantName='Le Four', category='pizzeria', address='15 rue de la chocolatine'}", testMerchant.toString());
    }
}