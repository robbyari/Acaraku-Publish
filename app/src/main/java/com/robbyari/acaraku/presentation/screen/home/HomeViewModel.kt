package com.robbyari.acaraku.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {
    private val _eventList = MutableStateFlow<Response<List<Events>>>(Response.Loading)
    val eventList: StateFlow<Response<List<Events>>> = _eventList

    init {
        getAllEvents()
    }

    private fun getAllEvents() {
        viewModelScope.launch {
            repo.getAllEvent()
                .collect { response ->
                    _eventList.value = response
                }
        }
    }
}