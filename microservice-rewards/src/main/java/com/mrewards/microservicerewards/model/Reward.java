package com.mrewards.microservicerewards.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "rewards")
public class Reward {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType. IDENTITY )
    private Integer id;

    @NotEmpty
    @Column(name="max_points")
    private Integer maxPoints;
    
    @NotEmpty
    @Column(name="points")
    private Integer points;

    @NotEmpty
    @Column(name="rewards")
    private Integer rewardsNbr;

    //TODO add rewardsToCollect
    
    @NotEmpty
    @Column(name="id_user")
    private Integer idUser;
    
    @NotEmpty
    @Column(name="id_merchant")
    private Integer idMerchant;
    
   

    public Reward() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxPoints() {return maxPoints;}

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Integer getPoints() {
        if(points == null) {
            points = 0;
        }
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRewardsNbr() {
        if(rewardsNbr == null){
            rewardsNbr = 0;
        }
        return rewardsNbr;
    }

    public void setRewardsNbr(Integer rewardsNbr) {
        this.rewardsNbr = rewardsNbr;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdMerchant() {
        return idMerchant;
    }

    public void setIdMerchant(Integer idMerchant) {
        this.idMerchant = idMerchant;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Reward{" +
                "id=" + id +
                ", maxPoints=" + maxPoints +
                ", points=" + points +
                ", rewardsNbr=" + rewardsNbr +
                '}';
    }
}
