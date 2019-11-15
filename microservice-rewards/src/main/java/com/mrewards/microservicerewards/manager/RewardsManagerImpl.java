package com.mrewards.microservicerewards.manager;

import com.mrewards.microservicerewards.model.Reward;


/**
 * Interface du manager du service rewards.
 */
public class RewardsManagerImpl implements RewardsManager {

    public RewardsManagerImpl(){

    }
    /**
     * <p>adds a point if less than maxpoints</p>
     * <p>If the maxpoints is reached, a discount is given, rewardsnbr+=1, points = 0</p>
     * @param rewardAccount
     * @return rewardAccount
     */
    @Override
    public Reward addPointManager(Reward rewardAccount) {
        int actualPoints = rewardAccount.getPoints();
        int maxPoints = rewardAccount.getMaxPoints();
        int rewardsNbr = rewardAccount.getRewardsNbr();

        if (actualPoints < maxPoints - 1){
            rewardAccount.setPoints(actualPoints + 1);
        }
        else{
            rewardAccount.setPoints(0);
            rewardAccount.setRewardsNbr(rewardsNbr + 1);
        }
        return rewardAccount;
    }

    /**
     * <p>client redeems a reward from merchant</p>
     * <p>rewardNbr -1</p>
     * @param rewardAccount
     * @return
     */
    @Override
    public Reward redeemRewardManager(Reward rewardAccount) {
        int rewardsNbr = rewardAccount.getRewardsNbr();
        rewardAccount.setRewardsNbr(rewardsNbr-1);
        return rewardAccount;
    }
}
