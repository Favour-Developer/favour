package com.example.favour

class UserDTO() {
    lateinit var username: String
    lateinit var email: String
    lateinit var gender: String
    lateinit var address: String
    lateinit var photoURL: String

    constructor(username: String, email: String, gender: String, address: String, photoURL: String): this(){
        this.username = username
        this.email = email
        this.address = address
        this.gender = gender
        this.photoURL = photoURL
    }
}