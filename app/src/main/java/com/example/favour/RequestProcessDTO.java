package com.example.favour;


import android.content.Context;

public class RequestProcessDTO {
    private String requestID;
    private String favourerUID;
    private Boolean isAccepted;
    private Integer amount;
    private Boolean isAmountApproved;
    private Boolean isCompleted;
    private Boolean isCancelled;
    private Boolean isPurchased;
    private Boolean isDelivered;
    private String date;
    private Integer points;

    public RequestProcessDTO(String requestID, String favourerUID, Boolean isAccepted, Integer amount, Boolean isAmountApproved, Boolean isCompleted, Boolean isCancelled, Boolean isPurchased, Boolean isDelivered, String date, Integer points) {
        this.requestID = requestID;
        this.favourerUID = favourerUID;
        this.isAccepted = isAccepted;
        this.amount = amount;
        this.isAmountApproved = isAmountApproved;
        this.isCompleted = isCompleted;
        this.isCancelled = isCancelled;
        this.isPurchased = isPurchased;
        this.isDelivered = isDelivered;
        this.date = date;
        this.points = points;
    }


    public RequestProcessDTO() {
    }


    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setAmountApproved(Boolean amountApproved) {
        isAmountApproved = amountApproved;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getRequestID() {
        return requestID;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public Integer getAmount() {
        return amount;
    }

    public Boolean getAmountApproved() {
        return isAmountApproved;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }


    public Boolean getCancelled() {
        return isCancelled;
    }

    public String getFavourerUID() {
        return favourerUID;
    }

    public void setFavourerUID(String favourerUID) {
        this.favourerUID = favourerUID;
    }

    public Boolean getPurchased() {
        return isPurchased;
    }

    public void setPurchased(Boolean purchased) {
        isPurchased = purchased;
    }

    public Boolean getDelivered() {
        return isDelivered;
    }

    public void setDelivered(Boolean delivered) {
        isDelivered = delivered;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
