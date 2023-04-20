package com.example.numbers.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.numbers.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

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

    }

    sealed interface ScreenEvent {
        class Snackbar(val message: String) : ScreenEvent
        class Navigate(val destination: String) : ScreenEvent
    }
}