package com.robbyari.acaraku.presentation.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.repository.EventRepository
import com.robbyari.acaraku.utils.generateOrderId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repo: EventRepository
): ViewModel() {

    private val _idOrder = MutableStateFlow("")
    val idOrder: StateFlow<String> = _idOrder

    private val _detail = MutableStateFlow<Response<Events>>(Response.Loading)
    val detail: StateFlow<Response<Events>> = _detail

    fun getDetail(id: String?) {
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

    suspend fun addTransaction(
        event: Events,
        idOrder: String,
        email: String,
        urlSnap: String,
        price: Int,
        status: String,
        transactionTime: String,
        nameEvent: String,
        idEvent: String
    ) {
        repo.addTransaction(event = event,
            idOrder = idOrder,
            email = email,
            urlSnap = urlSnap,
            price = price,
            status = status,
            transactionTime = transactionTime,
            nameEvent = nameEvent,
            idEvent = idEvent
        )
    }

    fun getIdOrder() {
        _idOrder.value = generateOrderId(10)
    }
}