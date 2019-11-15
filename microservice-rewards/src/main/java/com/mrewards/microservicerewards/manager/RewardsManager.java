package com.mrewards.microservicerewards.manager;


import com.mrewards.microservicerewards.model.Reward;

/**
 * Interface du manager du service rewards.
 */
public interface RewardsManager {
    Reward addPointManager(Reward rewardAccount);
    Reward redeemRewardManager(Reward rewardAccount);
}
