package com.journiapp.stampapplication.model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.ArrayList

interface API {

    /** login with demo user and receive list of stamps */
    @POST("/mobile/user/login")
    suspend fun login(@Body body: LoginRequest): Response<ProfileResponse>

    /** fetch list of stamps */
    @GET("/api/v8.0/user/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    /** add or remove a stamp */
    @POST("/mobile/stamp")
    suspend fun postStamp(@Body stampRequest: StampRequest): Response<StampResponse>

    /** get autocomplete suggestions for places search. returns Google Reference and Description for each suggestion. */
    @GET("/mobile/country/search/{query}")
    fun getCountries(@Path("query") query: String): Call<ArrayList<SearchSuggestion>>

    /** fetch country code for a given autocomplete suggestion (Google Reference) */
    @POST("/mobile/country/code")
    fun getCountryCode(@Body request: CountryCodeRequest): Call<CountryCodeResponse>

}