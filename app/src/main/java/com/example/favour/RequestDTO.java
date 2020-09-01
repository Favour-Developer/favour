package com.example.favour;


public class RequestDTO {


    private String person_name;
    private String requestID;
    private String userUid;
    private String categories;
    private Integer timer;
    private String doc;
    private Boolean urgent;
    private Integer shop_bor;
    private String items;
    private Integer photoOrtext;
    private Boolean isCompleted;
    private Boolean isProgress;
    private Boolean expired;


    public RequestDTO() {
    }

    public RequestDTO(String person_name, String requestID, String userUid, String categories, String items, String doc, Integer timer, Boolean urgent, Integer shop_bor, Integer photoOrText, Boolean isProgress, Boolean isCompleted, Boolean expired) {
        this.person_name = person_name;
        this.requestID = requestID;
        this.userUid = userUid;
        this.categories = categories;
        this.timer = timer;
        this.urgent = urgent;
        this.shop_bor = shop_bor;
        this.items = items;
        this.photoOrtext = photoOrText;
        this.isProgress = isProgress;
        this.isCompleted = isCompleted;
        this.doc = doc;
        this.expired = expired;
    }

    public String getItems() {
        return items;
    }

    public String getPerson_name() {
        return person_name;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getCategories() {
        return categories;
    }

    public Integer getTimer() {
        return timer;
    }

    public Boolean getUrgent() {
        return urgent;
    }

    public Integer getShop_bor() {
        return shop_bor;
    }

    public Integer getPhotoOrtext() {
        return photoOrtext;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public Boolean getIsProgress() {
        return isProgress;
    }

    public String getDoc() {
        return doc;
    }

    public Boolean getExpired() {
        if (expired != null)
            return expired;
        else return false;
    }
}
