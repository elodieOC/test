package com.mmerchants.microservicemerchants.batch;

import com.mmerchants.microservicemerchants.dao.MerchantDao;
import com.mmerchants.microservicemerchants.model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Class that checks age of token if any and erases them or not accordingly</h1>
 */
@Component
public class ScheduledTasks {
    @Autowired
    private MerchantDao merchantDao;

    @Scheduled(cron="0 0 23 * * MON-SUN") //Fire at 11pm every day
    public void checkTokens(){
        List<Merchant> allMerchants = merchantDao.findAll();
        List<Merchant> usersWithToken = new ArrayList<>();

        for (Merchant u: allMerchants){
            if (u.getResetToken() != null){
                usersWithToken.add(u);
            }
        }

        for (Merchant u : usersWithToken){
            Timestamp tokenDate = u.getTokenDate();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            if(tokenDate.after(now)){
                u.setResetToken(null);
                u.setTokenDate(null);
                merchantDao.save(u);
            }
        }
    }
}
