package com.journiapp.stampapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journiapp.stampapplication.common.DataSingleton
import com.journiapp.stampapplication.data.repository.StampRepository
import com.journiapp.stampapplication.domain.usecase.AddRemoveStampUseCase
import com.journiapp.stampapplication.domain.usecase.LoginUseCase
import com.journiapp.stampapplication.model.LoginRequest
import com.journiapp.stampapplication.model.Stamp
import com.journiapp.stampapplication.model.StampRequest
import com.journiapp.stampapplication.presentation.view.state.StampScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StampViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val addRemoveStampUseCase: AddRemoveStampUseCase,
    private val repository: StampRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _stampScreenState = MutableStateFlow(StampScreenUIState())
    val stampScreenState: StateFlow<StampScreenUIState> = _stampScreenState.asStateFlow()

    init {
        loadStamps()
    }

    private fun loadStamps() {
        viewModelScope.launch(ioDispatcher) {
            try {
                val loginRequest = LoginRequest(
                    email = "challenge@journiapp.com",
                    password = "FreshAccount5"
                )

                loginUseCase(loginRequest).collect { stamps ->
                    _stampScreenState.value =
                        _stampScreenState.value.copy(stamps = stamps)
                }
            } catch (e: Exception) {
                //Handle Error
            }
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch(ioDispatcher) {
            repository.getProfile().collect { result ->
                _stampScreenState.value =
                    _stampScreenState.value.copy(stamps = result.getOrThrow().stamps)
            }
        }
    }

    fun removeStamp(stamp: Stamp) {
        if (DataSingleton.countryCode == null) return

        val request =
            StampRequest(
                countryCode = DataSingleton.countryCode!!,
                create = System.currentTimeMillis(),
                delete = true
            )

        viewModelScope.launch(ioDispatcher) {
            addRemoveStampUseCase(request).collect { result ->
                if (result) {
                    fetchProfile()
                } else {
                    ///Do nothing, probably show a toast later
                }
            }
        }
    }
}