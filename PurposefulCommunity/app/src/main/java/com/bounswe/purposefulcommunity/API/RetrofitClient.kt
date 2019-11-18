package com.bounswe.mercatus.API

import com.bounswe.purposefulcommunity.Models.SignInBody
import com.bounswe.purposefulcommunity.Models.SignInRes
import com.bounswe.purposefulcommunity.Models.SignUpBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    // Sign in request
    @Headers("Content-Type:application/json")
    @POST("user/login")
    fun signin(
        @Body info: SignInBody
    ): retrofit2.Call<SignInRes>

    // Sign up request
    @Headers("Content-Type:application/json")
    @POST("user/register")
    fun registerUser(
        @Body info: SignUpBody
    ): retrofit2.Call<SignUpBody>
}
class RetrofitInstance {
    companion object {
        val BASE_URL: String = "http://18.184.25.234:8080"


        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        /**
         * Companion object to create the Mercatus
         */
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}