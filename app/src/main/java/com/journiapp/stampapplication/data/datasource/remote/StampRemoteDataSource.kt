package com.journiapp.stampapplication.data.datasource.remote

import android.util.Log
import com.journiapp.stampapplication.common.SharedPrefHelper
import com.journiapp.stampapplication.model.API
import com.journiapp.stampapplication.model.LoginRequest
import com.journiapp.stampapplication.model.ProfileResponse
import com.journiapp.stampapplication.model.StampRequest
import com.journiapp.stampapplication.model.StampResponse
import retrofit2.awaitResponse
import javax.inject.Inject

class StampRemoteDataSource @Inject constructor(
    private val api: API,
    private val sharedPrefHelper: SharedPrefHelper
) {

    suspend fun loginAndFetchStamps(email: String, password: String): Result<ProfileResponse> =
        try {
            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val profileResponse = response.body()!!
                /*val user = UserInformation()
                NetworkUtil.getCookieString(response.awaitResponse().headers())
                    ?.let { user?.sessionCookie = it }

                sharedPrefHelper.saveUserInfo(user)*/
                Result.success(profileResponse)
            } else {
                Result.failure(Exception("Login failed"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getProfile(): Result<ProfileResponse> = try {
        val response = api.getProfile()
        if (response.isSuccessful) {
            val profileResponse = response.body()!!
            Result.success(profileResponse)
        } else {
            Result.failure(Exception("Failed to get profile"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun postStamp(request: StampRequest): Result<StampResponse> = try {
        val response = api.postStamp(request)
        if (response.isSuccessful) {
            val stampResponse = response.body()!!
            Result.success(stampResponse)
        } else {
            Result.failure(Exception("Failed to post stamp"))
        }
    } catch (e: Throwable) {
        Result.failure(e)
    }

}