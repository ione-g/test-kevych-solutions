package eone.grim.moviebrowser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import eone.grim.moviebrowser.presentation.movies.details.MovieDetailsContract
import eone.grim.moviebrowser.presentation.movies.details.MovieDetailsViewModel
import org.koin.compose.koinInject

@Composable
fun MovieDetailsScreen(id: Long) {
    val vm: MovieDetailsViewModel = koinInject()
    val state by vm.state.collectAsState()

    LaunchedEffect(id) { vm.onIntent(MovieDetailsContract.Intent.Load(id)) }

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        state.error != null -> ErrorBox(
            error = state.error ?: "Unknown error",
            onRetry = { vm.onIntent(MovieDetailsContract.Intent.Load(id)) }
        )

        state.movie != null -> {
            val m = state.movie!!

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {
                        if (!m.backdropUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = m.backdropUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.65f)
                                            )
                                        )
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = m.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            m.originalTitle?.takeIf { it.isNotBlank() }?.let {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 12.dp)
                    ) {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (!m.posterUrl.isNullOrBlank()) {
                                    AsyncImage(
                                        model = m.posterUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(120.dp)
                                            .height(180.dp)
                                            .clip(MaterialTheme.shapes.large)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .width(120.dp)
                                            .height(180.dp)
                                            .clip(MaterialTheme.shapes.large)
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Filled.Movie, contentDescription = null)
                                    }
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        m.releaseYear?.let { year ->
                                            item { MetaChip(icon = Icons.Filled.CalendarMonth, text = year) }
                                        }
                                        if (m.language.isNotBlank()) {
                                            item { MetaChip(icon = Icons.Filled.Language, text = m.language) }
                                        }
                                        if (m.isAdult) {
                                            item { MetaChip(text = "18+") }
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Filled.Star, contentDescription = null)
                                        Text(
                                            text = m.ratingText,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    m.runtimeText.takeIf { it.isNotBlank() }?.let { rt ->
                                        Text("$rt min", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }

                if (m.genreNames.isNotEmpty()) {
                    item {
                        SectionCard(
                            title = "Genres",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 12.dp)
                        ) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(m.genreNames) { name ->
                                    AssistChip(onClick = {}, label = {
                                        if (name != null) {
                                            Text(name)
                                        }
                                    })
                                }
                            }
                        }
                    }
                }

                item {
                    SectionCard(
                        title = "Financials",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 12.dp)
                    ) {
                        KeyValueRow(label = "Budget", value = m.budgetText)
                        Spacer(Modifier.height(8.dp))
                        KeyValueRow(label = "Revenue", value = m.revenueText)
                    }
                }

                if (m.overview.isNotBlank()) {
                    item {
                        SectionCard(
                            title = "Overview",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 12.dp)
                                .padding(bottom = 24.dp)
                        ) {
                            Text(text = m.overview, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else {
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }

        else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No data")
        }
    }
}

@Composable
private fun MetaChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    AssistChip(
        onClick = {},
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                }
                Text(text)
            }
        }
    )
}

@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun KeyValueRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ErrorBox(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error: $error")
        Spacer(Modifier.height(12.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}
