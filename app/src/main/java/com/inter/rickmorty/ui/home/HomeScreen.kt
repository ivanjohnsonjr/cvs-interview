package com.inter.rickmorty.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.inter.rickmorty.R
import com.inter.rickmorty.core.repo.SearchResult
import com.inter.rickmorty.ui.theme.Dimen
import com.inter.rickmorty.ui.theme.RickMortyTheme
import com.inter.rickmorty.ui.vm.SharedViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: SharedViewModel,
    onShowDetail: () -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val searchResults: LazyPagingItems<SearchResult> = viewModel.searchResultDataFlow.collectAsLazyPagingItems()

    InternalHomeScreen(
        modifier = modifier,
        query = state.query,
        showEmpty = state.showEmpty,
        isLoading = state.isLoading,
        searchResults = searchResults,
        onSearch = { query ->
            viewModel.search(query)
        },
        onSelect = {
            viewModel.select(it)
            onShowDetail()
        }
    )
}

@Composable
private fun InternalHomeScreen(
    modifier: Modifier,
    query: String,
    showEmpty: Boolean,
    isLoading: Boolean,
    searchResults: LazyPagingItems<SearchResult>,
    onSearch: (String) -> Unit,
    onSelect: (SearchResult) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(Dimen.unit),
        horizontalArrangement = Arrangement.spacedBy(Dimen.unit),
        modifier = modifier
    ) {
        item(
            span = { GridItemSpan(maxCurrentLineSpan) }
        ) {
            LocalSearchBar(
                modifier = Modifier.padding(bottom = Dimen.unit),
                query = query,
                isLoading = isLoading
            ) {
                onSearch(it)
            }
        }

        if (showEmpty) {
            item(
                span = { GridItemSpan(maxCurrentLineSpan) }
            ) {
                EmptyView()
            }
        }

        items(count = searchResults.itemCount) { index ->

            val searchResult = searchResults[index]!!
            CharacterView(
                searchResult = searchResult
            ) {
                onSelect(searchResult)
            }
        }

        if (searchResults.loadState.hasError) {
            item(
                span = { GridItemSpan(maxCurrentLineSpan) }
            ) {
                ErrorView(
                    modifier = Modifier.padding(top = Dimen.unitx2),
                    text = stringResource(R.string.fail_load_results)
                )
            }
        }

        if (searchResults.loadState.append is LoadState.Loading) {
            item(
                span = { GridItemSpan(maxCurrentLineSpan) }
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        modifier = modifier.padding(start = Dimen.unit),
                        text = stringResource(R.string.loading_text),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }

}

@Composable
private fun EmptyView(
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_state_text),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    isLoading: Boolean,
    onSearch: (String) -> Unit
) {
    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = {
                    onSearch(it)
                },
                onSearch = { },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(Dimen.loadingIndicatorSize),
                            strokeWidth = Dimen.loadingIndicatorStrokeWidth,
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                trailingIcon = {},
            )
        },
        expanded = false,
        onExpandedChange = { },
        content = {}
    )
}

@Composable
private fun CharacterView(
    searchResult: SearchResult,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        AsyncImage(
            modifier = Modifier
                .defaultMinSize(minHeight = Dimen.imageMinDimension, minWidth = Dimen.imageMinDimension)
                .fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(searchResult.image)
                .crossfade(true)
                .build(),
            placeholder = rememberVectorPainter(Icons.Filled.Person),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ErrorView(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            modifier = modifier.size(32.dp),
            imageVector = Icons.Filled.Warning,
            colorFilter = ColorFilter.tint(Color.Red),
            contentDescription = stringResource(R.string.error_desc)
        )

        Text(
            modifier = Modifier.padding(start = Dimen.unit),
            color = Color.Red,
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RickMortyTheme {
        ErrorView(
            "fail to load "
        )
    }
}