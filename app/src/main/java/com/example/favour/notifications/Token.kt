package com.example.favour.notifications

class Token {
    private var token = ""

    constructor() {}

    constructor(token: String) {
        this.token = token
    }

    public fun getToken(): String {
        return token
    }

    public fun setToken() {
        this.token = token
    }


}