package com.robbyari.acaraku.presentation.screen.detail

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
class DetailViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {
    private val _detail = MutableStateFlow<Response<Events>>(Response.Loading)
    val detail: StateFlow<Response<Events>> = _detail

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun getDetail(id: String?) {
        viewModelScope.launch {
            if (id != null) {
                try {
                    _isFavorite.value = repo.checkFavorite(id)
                    _isLoggedIn.value = repo.isLoggedIn()
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

    suspend fun addFavorite(events: Events) {
        repo.addFavorite(events)
        _isFavorite.value = true
    }

    suspend fun removeFavorite(events: Events) {
        repo.removeFavorite(events)
        _isFavorite.value = false
    }
}