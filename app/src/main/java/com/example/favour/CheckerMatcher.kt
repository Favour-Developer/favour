package com.example.favour

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText

class CheckerMatcher() {


    fun checkEmptyEmailPass(
        et: EditText,
        pt: EditText
    ): Boolean {
        if (!isEmail(et.text.toString())) {
            et.error = "Enter valid email."
            return false
        }
        if (!isPassword(pt.text.toString())) {
            pt.error = "Enter valid password."
            return false
        }
        return true
    }
    fun checkEmptyPhonePass(
        pht: EditText,
        pwt: EditText
    ): Boolean {
        if (!isPhone(pht.text.toString())) {
            pht.error = "Enter valid mobile number. Make Sure of exactly 10 characters."
            return false
        }
        if (!isPassword(pwt.text.toString())) {
            pwt.error = "Enter valid password."
            return false
        }
        return true
    }

    fun checkEmptyNamePhone(
        nt: EditText,
        pt: EditText
    ): Boolean {
        if (TextUtils.isEmpty(nt.toString())) {
            nt.error = "Name can't be blank."
            return false
        }
        if (!isPhone(pt.text.toString())) {
            pt.error = "Enter valid mobile number. Make Sure of exactly 10 characters."
            return false
        }
        return true
    }

    fun checkPassPass(
        pt1: EditText,
        pt2: EditText
    ): Boolean {
        if (!isPassword(pt1.text.toString())) {
            pt1.error = "Enter valid password. Make Sure of exact 10 characters."
            return false
        }
        if (!isPassword(pt2.text.toString())) {
            pt2.error = "Enter valid password. Make Sure of exact 10 characters."
            return false
        }
        if(pt1.text.toString() != pt2.text.toString()){
            val err = "Both the fields should match."
            pt1.error = err
            pt2.error = err
            return false
        }
        return true
    }

    //to check whether the string entered in the email field is in the format of email address
    private fun isEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    //to check whether the length of the password is atleast of 8 characters
    private fun isPassword(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= 8
    }

    private fun isPhone(phone: String): Boolean {
        return (phone.length == 10) && (Patterns.PHONE.matcher(phone).matches())
    }

}