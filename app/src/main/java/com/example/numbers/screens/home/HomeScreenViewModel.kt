package com.example.numbers.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numbers.models.NumberType
import com.example.numbers.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _screenEvent = MutableSharedFlow<ScreenEvent>()
    val screenEvent = _screenEvent.asSharedFlow()


    fun searchChanged(search: String) {
        _state.value = _state.value.copy(search = search)
    }

    fun startSearch(isRandom: Boolean = false) {
        viewModelScope.launch {
          _state.value = _state.value.copy(isLoading = true)
          delay(1.seconds)
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun typeChanged(type: NumberType){
        _state.value = _state.value.copy(type = type)
    }

    sealed interface ScreenEvent {
        class Snackbar(val message: String) : ScreenEvent
        class Navigate(val destination: String) : ScreenEvent
    }
}