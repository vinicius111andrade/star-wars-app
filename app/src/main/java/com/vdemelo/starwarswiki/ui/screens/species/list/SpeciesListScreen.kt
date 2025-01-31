package com.vdemelo.starwarswiki.ui.screens.species.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vdemelo.starwarswiki.R
import com.vdemelo.starwarswiki.domain.entity.model.Species
import com.vdemelo.starwarswiki.domain.entity.model.TextField
import com.vdemelo.starwarswiki.ui.components.ImageLoader
import com.vdemelo.starwarswiki.ui.components.LabelAndTextData
import com.vdemelo.starwarswiki.ui.components.RetrySection
import com.vdemelo.starwarswiki.ui.components.SearchBar
import com.vdemelo.starwarswiki.ui.nav.buildSpeciesDetailsRoute
import org.koin.androidx.compose.getViewModel

@Composable
fun SpeciesListScreen(
    navController: NavController,
    viewModel: SpeciesListViewModel = getViewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star_wars_logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .heightIn(min = 0.dp, max = 140.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.species_list_screen_title),
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            SearchBar(
                hint = stringResource(id = R.string.list_screen_search_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearch = { viewModel.loadSpeciesPaginated(search = it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SpeciesList(navController = navController)
        }
    }
}

@Composable
fun SpeciesList(
    navController: NavController,
    viewModel: SpeciesListViewModel = getViewModel()
) {
    val list by remember { viewModel.speciesList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (list.isNotEmpty()) {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                val itemCount = list.size

                items(itemCount) { i ->
                    val hasScrolledDown = i >= itemCount - 2
                    if (hasScrolledDown && !endReached && !isLoading) {
                        viewModel.loadSpeciesPaginated()
                    }
                    SpeciesItem(
                        species = list[i],
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    if (isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else if (loadError.isNotEmpty()) {
                        RetrySection(
                            error = loadError,
                            onRetry = { viewModel.loadSpeciesPaginated() }
                        )
                    }
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else if (loadError.isNotEmpty()) {
                    RetrySection(
                        error = loadError,
                        onRetry = { viewModel.loadSpeciesPaginated() }
                    )
                }
            }
        }
    }
}

@Composable
fun SpeciesItem(
    species: Species,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SpeciesListViewModel = getViewModel()
) {
    val id = species.url?.let { viewModel.getSpeciesNumber(it) }
    val imageUrl = id?.let { viewModel.getSpeciesImageUrl(it) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                id?.run {
                    navController.navigate(
                        route = buildSpeciesDetailsRoute(id = id.toString())
                    )
                }
            }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ImageLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .size(120.dp),
                url = imageUrl,
                contentDescription = species.name
                    ?: stringResource(id = R.string.details_screen_image_content_description)
            )
            Spacer(modifier = Modifier.height(8.dp))
            detailsData(species).forEach {
                LabelAndTextData(label = it.label, text = it.text)
            }
        }
    }
}

@Composable
fun detailsData(species: Species): List<TextField> {
    return listOf(
        TextField(
            label = stringResource(id = R.string.list_screen_name_label),
            text = species.name
        ),
        TextField(
            label = stringResource(id = R.string.list_screen_language_label),
            text = species.language
        ),
        TextField(
            label = stringResource(id = R.string.list_screen_classification_label),
            text = species.classification
        )
    )
}
