package com.robbyari.acaraku.presentation.screen.ticketdetail

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
class TicketDetailViewModel @Inject constructor(
    private val repo: EventRepository
): ViewModel() {

    private val _detail = MutableStateFlow<Response<Events>>(Response.Loading)
    val detail: StateFlow<Response<Events>> = _detail

    fun getDetailMyTicket(id: String?) {
        viewModelScope.launch {
            if (id != null) {
                try {
                    repo.getDetail(id)
                        .collect { response ->
                            _detail.value = response
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}