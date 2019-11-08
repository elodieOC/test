package com.mmerchants.microservicemerchants.dao;


import com.mmerchants.microservicemerchants.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantDao extends JpaRepository<Merchant, Integer> {
    Merchant findByEmail(String email);
    Merchant findByMerchantName(String merchantName);

    Optional<Merchant> findFirstByEmail(String email);
    Optional<Merchant> findFirstByMerchantName(String merchantName);


}
