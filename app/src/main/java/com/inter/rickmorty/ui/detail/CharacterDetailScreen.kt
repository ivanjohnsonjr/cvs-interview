package com.inter.rickmorty.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.inter.rickmorty.R
import com.inter.rickmorty.core.repo.SearchResult
import com.inter.rickmorty.ui.UIConstants
import com.inter.rickmorty.ui.home.ErrorView
import com.inter.rickmorty.ui.theme.Dimen
import com.inter.rickmorty.ui.vm.SharedViewModel


private val SearchResult.formatedDate: String
    get() {
        return UIConstants.dateTimeFormatter.format(created)
    }

private val SearchResult.hasType: Boolean
    get() = !type.isNullOrEmpty()

@Composable
fun CharacterDetailScreen(
    modifier: Modifier = Modifier,
    vm: SharedViewModel
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    if (state.selected != null) {
        DetailInfo(
            modifier = modifier,
            selected = state.selected!!
        )
    } else {
        //Display Error Screen
        ErrorView(
            text = stringResource(R.string.detail_fail_error_text),
            modifier = modifier
        )
    }
}

@Composable
private fun InfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(start = Dimen.unit),
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DetailInfo(
    modifier: Modifier,
    selected: SearchResult
) {
    Column(
        modifier = modifier.padding(bottom = Dimen.unit),
        verticalArrangement = spacedBy(Dimen.unit)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selected.image)
                    .crossfade(true)
                    .build(),
                placeholder = rememberVectorPainter(Icons.Filled.Person),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
        }

        InfoRow(
            label = stringResource(R.string.name_label),
            value = selected.name
        )

        if (selected.hasType) {
            InfoRow(
                label = stringResource(R.string.type_label),
                value = selected.type!!
            )
        }

        InfoRow(
            label = stringResource(R.string.status_label),
            value = selected.status
        )

        InfoRow(
            label = stringResource(R.string.species_label),
            value = selected.species
        )

        InfoRow(
            label = stringResource(R.string.origin_label),
            value = selected.origin.name
        )

        InfoRow(
            label = stringResource(R.string.created_label),
            value = selected.formatedDate
        )
    }
}


