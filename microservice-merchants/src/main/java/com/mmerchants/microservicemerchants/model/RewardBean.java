package com.mmerchants.microservicemerchants.model;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class RewardBean {

    private Integer id;
    private Integer points;
    private Integer rewardsNbr;
    private Integer idUser;
    private Integer idMerchant;
    private Integer maxPoints;
    private byte[] qrCode;
    private String base64;

    public RewardBean() {
    }

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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRewardsNbr() {
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

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }
}
