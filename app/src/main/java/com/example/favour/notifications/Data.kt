package com.example.favour.notifications

class Data {
    private var user: String = ""
    private var icon = 0
    private var body: String = ""
    private var title: String = ""
    private var sented: String = ""

    constructor() {}
    constructor(user: String, icon: Int, body: String, title: String, sented: String) {
        this.user = user
        this.icon = icon
        this.body = body
        this.title = title
        this.sented = sented
    }


    fun getUser(): String? {
        return user
    }

    fun setUser() {
        this.user = user
    }

    fun getIcon(): Int {
        return icon
    }

    fun setIcon() {
        this.icon = icon
    }


    fun getBody(): String? {
        return body
    }

    fun setBody() {
        this.user = user
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle() {
        this.title = title
    }

    fun getSented(): String? {
        return sented
    }

    fun setSented() {
        this.sented = sented
    }


}