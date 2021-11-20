package ru.krackdigger.retrofitapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    @GET
    fun getStringResponse(@Url url: String?): Call<String?>?
}