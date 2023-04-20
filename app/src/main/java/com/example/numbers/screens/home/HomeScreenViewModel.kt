package com.example.numbers.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numbers.models.NumberType
import com.example.numbers.navigation.Routes
import com.example.numbers.network.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: NumberRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _screenEvent = MutableSharedFlow<ScreenEvent>()
    val screenEvent = _screenEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(list = repository.getNumbersFromDB())
        }
    }

    fun searchChanged(search: String) {
        _state.value = _state.value.copy(search = search.filter { it.isDigit() })
    }

    fun startSearch(isRandom: Boolean = false) {
        viewModelScope.launch {
            if (!isRandom && _state.value.search.isEmpty()) {
                showError("Enter your number first!")
                return@launch
            }
            _state.value = _state.value.copy(isLoading = true)
            val result = repository.searchNumber(state.value.search, state.value.type, isRandom)
            println(result)
            when (result) {
                is Response.Error -> {
                    showError(result.message)
                }
                is Response.Success -> {
                    _screenEvent.emit(ScreenEvent.Navigate(Routes.DETAILS + result.data.toJson))
                    _state.value = _state.value.copy(list = repository.getNumbersFromDB(), search = "")
                }
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun showError(message: String) {
        viewModelScope.launch {
            _screenEvent.emit(ScreenEvent.Snackbar(message))
        }
    }

    fun typeChanged(type: NumberType) {
        _state.value = _state.value.copy(type = type)
    }

    sealed interface ScreenEvent {
        class Snackbar(val message: String) : ScreenEvent
        class Navigate(val destination: String) : ScreenEvent
    }
}