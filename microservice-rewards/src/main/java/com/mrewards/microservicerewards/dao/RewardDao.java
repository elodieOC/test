package com.mrewards.microservicerewards.dao;


import com.mrewards.microservicerewards.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardDao extends JpaRepository<Reward, Integer> {

    Reward findRewardsById (Integer id);

    List<Reward> findRewardsByIdUser (Integer idUser);
    List<Reward> findRewardsByIdMerchant (Integer idMerchant);

    Optional<Reward> findRewardByIdMerchantAndIdUser (Integer idMerchant, Integer idUser);


}
