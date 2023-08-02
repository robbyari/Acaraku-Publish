package com.robbyari.acaraku.presentation.screen.favorite

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
class FavoriteViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {
    private val _favoriteList = MutableStateFlow<Response<List<Events>>>(Response.Loading)
    val favoriteList: StateFlow<Response<List<Events>>> = _favoriteList

    init {
        getAllFavorite()
    }

    fun getAllFavorite() {
        viewModelScope.launch {
            repo.getFavorite()
                .collect { response ->
                    _favoriteList.value = response
                }
        }
    }
}