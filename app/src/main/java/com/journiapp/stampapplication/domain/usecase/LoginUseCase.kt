package com.journiapp.stampapplication.domain.usecase

import com.journiapp.stampapplication.data.repository.StampRepository
import com.journiapp.stampapplication.model.LoginRequest
import com.journiapp.stampapplication.model.Stamp
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: StampRepository) {
    suspend operator fun invoke(request: LoginRequest): Flow<List<Stamp>> =
        repository.login(request)
}