package com.mrewards.microservicerewards.model;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

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

    //TODO add rewardsToCollect
    
    @NotNull
    @Column(name="id_user")
    private Integer idUser;
    
    @NotNull
    @Column(name="id_merchant")
    private Integer idMerchant;

    @Lob
    @Column(name = "qr_code")
    private byte[] qrCode;
    @Transient
    private MultipartFile qrCodeFile;

    @Transient
    private String base64;

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public String getBase64() {
        return this.base64 = Base64.encode(this.qrCode);
    }

    public void setBase64(String base64) {
        this.base64 = base64;
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
