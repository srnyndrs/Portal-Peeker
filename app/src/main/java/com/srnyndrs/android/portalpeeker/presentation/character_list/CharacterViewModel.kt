package com.srnyndrs.android.portalpeeker.presentation.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.srnyndrs.android.portalpeeker.domain.model.Character
import com.srnyndrs.android.portalpeeker.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CharacterRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<PagingData<Character>>(PagingData.empty())
    val uiState: Flow<PagingData<Character>> = _uiState.asStateFlow().cachedIn(viewModelScope)

    private var searchJob: Job? = null

    init {
        getCharacters()
    }

    fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1000L)
            getCharacters(query)
        }
    }

    private fun getCharacters(query: String = "") {
        viewModelScope.launch {
            try {
                repository
                    .getCharacters(query)
                    .collect { pagingData ->
                        _uiState.value = pagingData
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}