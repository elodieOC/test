package com.mrewards.microservicerewards.manager;

import com.mrewards.microservicerewards.model.Reward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface du manager du service rewards.
 */
public class RewardsManagerImpl implements RewardsManager {
    Logger log = LoggerFactory.getLogger(this.getClass());
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        int actualPoints = rewardAccount.getPoints();
        int maxPoints = rewardAccount.getMaxPoints();
        int rewardsNbr = rewardAccount.getRewardsNbr();

        if (actualPoints < maxPoints - 1){
            log.info("actual points of fidelity card are inferior to max points-1");
            rewardAccount.setPoints(actualPoints + 1);
            log.info("increments points +1");
        }
        else{
            log.info("actual points of fidelity card are superior to max points-1");
            rewardAccount.setPoints(0);
            log.info("set points to 0");
            rewardAccount.setRewardsNbr(rewardsNbr + 1);
            log.info("increments rewardsNbr +1");
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
        log.info(new Object(){}.getClass().getEnclosingMethod().getName());
        int rewardsNbr = rewardAccount.getRewardsNbr();
        rewardAccount.setRewardsNbr(rewardsNbr-1);
        log.info("decrements rewardsNbr -1");
        return rewardAccount;
    }
}
