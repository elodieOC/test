package com.mrewards.microservicerewards.manager;

import com.mrewards.microservicerewards.dao.RewardDao;
import com.mrewards.microservicerewards.model.Reward;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RewardsManagerImplTest {

    private RewardsManagerImpl rewardsManager = new RewardsManagerImpl();

    private Reward createReward(){
        Reward reward = new Reward();
        reward.setId(1);
        reward.setPoints(2);
        reward.setMaxPoints(10);
        reward.setRewardsNbr(0);
        reward.setIdUser(1);
        reward.setIdMerchant(1);
        return reward;
    }
    @Test
    public void addPointManager() {
        Reward reward = createReward();
        Reward actualReward = rewardsManager.addPointManager(reward);
        Reward expectedReward = createReward();
        expectedReward.setPoints(3);
        Assert.assertEquals(actualReward.toString(), expectedReward.toString());

        actualReward.setPoints(10);
        actualReward = rewardsManager.addPointManager(actualReward);
        expectedReward.setPoints(0);
        expectedReward.setRewardsNbr(1);
        Assert.assertEquals(actualReward.toString(), expectedReward.toString());

        actualReward.setPoints(9);
        actualReward = rewardsManager.addPointManager(actualReward);
        expectedReward.setPoints(0);
        expectedReward.setRewardsNbr(2);
        Assert.assertEquals(actualReward.toString(), expectedReward.toString());

        actualReward.setPoints(10);
        actualReward = rewardsManager.addPointManager(actualReward);
        Assert.assertNotEquals(actualReward.toString(), expectedReward.toString());

    }

}