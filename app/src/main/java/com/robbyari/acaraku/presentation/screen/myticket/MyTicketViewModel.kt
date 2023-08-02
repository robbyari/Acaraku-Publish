package com.robbyari.acaraku.presentation.screen.myticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.model.Transaction
import com.robbyari.acaraku.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTicketViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {

    private val _myTicket = MutableStateFlow<Response<List<Transaction>>>(Response.Loading)
    val myTicket: StateFlow<Response<List<Transaction>>> = _myTicket

    init {
        getMyTicket()
    }

    private fun getMyTicket() {
        viewModelScope.launch {
            repo.getSettlementTicket()
                .collect { response ->
                    _myTicket.value = response
                }
        }
    }

}