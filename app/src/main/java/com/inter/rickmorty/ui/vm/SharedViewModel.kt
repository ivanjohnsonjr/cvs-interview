package com.inter.rickmorty.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.inter.rickmorty.core.repo.RickMortyRepository
import com.inter.rickmorty.core.repo.SearchResult
import com.inter.rickmorty.ui.UIConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: RickMortyRepository
) : ViewModel() {

    data class UiStateData(
        val query: String = "",
        val isLoading: Boolean = false,
        val selected: SearchResult? = null
    )

    private val _uiState = MutableStateFlow(UiStateData())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResultDataFlow: Flow<PagingData<SearchResult>> = _uiState
        .map { it.query }
        .drop(1) //Drop the first one because its just and empty string
        .distinctUntilChanged()
        .debounce(UIConstants.DEBOUNCE)
        .flatMapLatest { repo.getSearchPaginationData(it) }
        .cachedIn(viewModelScope)
        .onEach {
            //Cancel loading
            _uiState.update {
                it.copy(isLoading = false)
            }
        }

    fun search(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                isLoading = true
            )
        }
    }

    fun select(selected: SearchResult) {
        _uiState.update {
            it.copy(selected = selected)
        }
    }

}