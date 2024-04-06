package com.journiapp.stampapplication.domain.usecase

import com.journiapp.stampapplication.data.repository.StampRepository
import com.journiapp.stampapplication.model.StampRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddRemoveStampUseCase @Inject constructor(private val repository: StampRepository) {
    suspend operator fun invoke(request: StampRequest): Flow<Boolean> =
        repository.addRemoveStamp(request)
}