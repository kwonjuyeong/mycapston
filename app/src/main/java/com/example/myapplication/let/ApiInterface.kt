package com.example.myapplication.let

import com.example.myapplication.let.LoginRes
import com.example.myapplication.let.SignUpRes
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiInterface {

    @FormUrlEncoded
    @POST("accounts/signup-req")
    fun insertUserInfo(
        @Field("business_name") businessName: String,
        @Field("business_number")
        businessNumber: String,
        @Field("officer_email")
        officerEmail: String,
        @Field("officer_name")
        officerName: String,
        @Field("officer_phone")
        officerPhone: String,
        @Field("officer_position")
        officerPosition: String,
        @Field("password")
        password: String,
        @Field("industry")
        industry: String,
        @Field("location_name")
        locationName: String
    ): Single<SignUpRes>

    @FormUrlEncoded
    @POST("accounts/signup-accept")
    fun requestSignUpAccept(
        @Field("officer_email") officeEmail: String
    ): Single<SignUpRes>

    @FormUrlEncoded
    @POST("accounts/login/")
    fun requestLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Single<LoginRes>


    companion object {
        private const val BASE_URL =
            "http://ff4839aab5bb.ngrok.io"

        fun create(): ApiInterface {
            val logger = HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BASIC
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}
