package com.robbyari.acaraku.presentation.screen.transaction

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
class TransactionViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {

    private val _transactionList = MutableStateFlow<Response<List<Transaction>>>(Response.Loading)
    val transactionList: StateFlow<Response<List<Transaction>>> = _transactionList

    init {
        getAllTransactions()
        updateTransactionInFirebase()
    }

    private fun getAllTransactions() {
        viewModelScope.launch {
            repo.getAllTransaction()
                .collect { response ->
                    _transactionList.value = response
                }
        }
    }

    private fun updateTransactionInFirebase() {
        viewModelScope.launch {
            repo.updateTransactionInFirebase()
        }
    }

}