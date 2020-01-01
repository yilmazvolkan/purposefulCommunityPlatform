package com.bounswe.mercatus.API

import com.bounswe.purposefulcommunity.Models.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

    // Get community list request
    @Headers("Content-Type:application/json")
    @GET("/community/get/all")
    fun getComList(
        @Header("token") token: String
    ): retrofit2.Call<List<CommunityBody>>

    // Get one community  request
    @Headers("Content-Type:application/json")
    @GET("/community/get/{id}")
    fun getOneComm(
        @Path("id") id: String,
        @Header("token") token: String
    ): retrofit2.Call<GetOneCommBody>

    // Create Community request
    @Headers("Content-Type:application/json")
    @POST("/community/create")
    fun createComm(
        @Body info: CreateCommBody,
        @Header("token") token: String
    ): retrofit2.Call<CommunityBody>

    // Get my following request
    @Headers("Content-Type:application/json")
    @GET("/community/get-self-followings")
    fun getMyFollowing(
        @Header("token") token: String
    ): retrofit2.Call<List<GetOneCommBody>>

    // Follow a community request
    @Headers("Content-Type:application/json")
    @GET("/community/follow-community")
    fun followCommunity(
        @Query("communityId") communityId: String,
        @Header("token") token: String
    ): retrofit2.Call<ResponseBody>

    // Upload image to Community
    @Headers("Content-Type:application/json")
    @Multipart
    @POST("/image/upload-community-image")
    fun uploadImage(
        @Query("communityId") communityId: String,
        @Part file: MultipartBody.Part,
        @Header("token") token: String
    ): retrofit2.Call<ResponseBody>

    ////////// Template

    // Create Community Template
    @Headers("Content-Type:application/json")
    @POST("/data-template/create-csd")
    fun createTemp(
        @Body info: CreateTemplateBody,
        @Header("token") token: String
    ): retrofit2.Call<ResponseBody>

    // Get Community Template  List request
    @Headers("Content-Type:application/json")
    @GET("/data-template/get-community-templates/{community-id}")
    fun getTemplates(
        @Path("community-id") id: String,
        @Header("token") token: String
    ): retrofit2.Call<List<GetTempBody>>

    // Get a Template fields request
    @Headers("Content-Type:application/json")
    @GET("/data-template/fields/{id}")
    fun getFields(
        @Path("id") id: String,
        @Header("token") token: String
    ): retrofit2.Call<List<GetFieldsBody>>

    // Get one Template request
    @Headers("Content-Type:application/json")
    @GET("/data-template/{id}")
    fun getOneTempl(
        @Path("id") id: String,
        @Header("token") token: String
    ): retrofit2.Call<GetTempBody>

    ////////// Instance

    // Create Community Instance
    @Headers("Content-Type:application/json")
    @POST("/data-instance/create/instance")
    fun createInstance(
        @Body info: CreateInstanceBody,
        @Header("token") token: String
    ): retrofit2.Call<ResponseBody>

    // Get Community Instance List request
    @Headers("Content-Type:application/json")
    @GET("/data-instance/get/community-instances/{community-id}/jsonld")
    fun getAllInstances(
        @Path("community-id") id: String,
        @Header("token") token: String
    ): retrofit2.Call<List<GetInstanceLDBody>>

    ////////// Search

    // Create Community Instance
    @Headers("Content-Type:application/json")
    @POST("/search/search/in-templates")
    fun searchInstance(
        @Query("or") or: Boolean,
        @Query("templateId") templateId: String,
        @Header("token") token: String,
        @Body searchingValues: JsonObject
    ): retrofit2.Call<List<GetInstanceLDBody>>

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