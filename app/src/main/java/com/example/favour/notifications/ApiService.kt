package com.example.favour.notifications

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAA--6PY3c:APA91bEei1vpEIq81qShcjbeW0xH9YP4IUxtQ2LBQu8KgVqm0t9IajyG2Z1KbxC_yOSrwCjZRcKH0nXkLR_qT9M3Ne6ifcTIlZhuV2IwISqSg_Uxf1caNZLuaJEURl3nWj0dF0BbNbV1"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender): Call<MyResponse>
}
