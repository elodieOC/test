package com.mrewards.microservicerewards.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "rewards")
public class Reward {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType. IDENTITY )
    private Integer id;

    @NotNull
    @Column(name="max_points")
    private Integer maxPoints;
    

    @Column(name="points")
    private Integer points;


    @Column(name="rewards")
    private Integer rewardsNbr;
    
    @NotNull
    @Column(name="id_user")
    private Integer idUser;
    
    @NotNull
    @Column(name="id_merchant")
    private Integer idMerchant;

    @Lob
    @Transient
    private byte[] qrCode;

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }


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
