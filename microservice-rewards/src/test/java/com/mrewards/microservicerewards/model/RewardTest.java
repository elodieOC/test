package com.mrewards.microservicerewards.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RewardTest {

    private Reward createReward(){
        Reward reward = new Reward();
        reward.setId(1);
        reward.setPoints(null);
        reward.setMaxPoints(10);
        reward.setRewardsNbr(null);
        reward.setIdUser(1);
        reward.setIdMerchant(1);
        return reward;
    }

    @Test
    public void getPoints() {
        Reward reward = createReward();
        int points = reward.getPoints();
        Assert.assertEquals(points, 0);
        Assert.assertNotEquals(reward.getPoints(), null);
    }

    @Test
    public void getRewardsNbr() {
        Reward reward = createReward();
        int rewardsNbr = reward.getRewardsNbr();
        Assert.assertEquals(rewardsNbr, 0);
        Assert.assertNotEquals(reward.getRewardsNbr(), null);
    }

    @Test
    public void toString1() {
        Reward reward = createReward();
        Assert.assertNotEquals(reward.toString(),"Reward{id=1, maxPoints=10, points=0, rewardsNbr=0}");
        Assert.assertEquals(reward.toString(),"Reward{id=1, maxPoints=10, points=null, rewardsNbr=null}");

    }
}