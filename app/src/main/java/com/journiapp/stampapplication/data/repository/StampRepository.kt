package com.journiapp.stampapplication.data.repository

import android.util.Log
import com.journiapp.stampapplication.data.datasource.remote.StampRemoteDataSource
import com.journiapp.stampapplication.model.LoginRequest
import com.journiapp.stampapplication.model.ProfileResponse
import com.journiapp.stampapplication.model.Stamp
import com.journiapp.stampapplication.model.StampRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StampRepository @Inject constructor(private val remoteDataSource: StampRemoteDataSource) {
    suspend fun login(request: LoginRequest): Flow<List<Stamp>> = flow {
        try {
            val response = remoteDataSource.loginAndFetchStamps(request.email, request.password)
            emit(response.getOrThrow().stamps)
        } catch (e: Exception) {
            //add later
        }
    }

    suspend fun getProfile(): Flow<Result<ProfileResponse>> = flow {
        emit(remoteDataSource.getProfile())
    }

    suspend fun addRemoveStamp(request: StampRequest): Flow<Boolean> = flow {
        val stampResult = remoteDataSource.postStamp(request)
        if (stampResult.isSuccess) {
            stampResult.getOrThrow().pictureGuid?.let {
                emit(true)
            } ?: emit(false)
        } else {
            emit(false)
        }
    }
}