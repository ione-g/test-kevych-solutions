package eone.grim.moviebrowser.presentation.model

import eone.grim.moviebrowser.data.images.ImageConfigStore
import eone.grim.moviebrowser.domain.entity.MovieDetails

suspend fun MovieDetails.toUI(
    images: ImageConfigStore
): MovieDetailsUI = MovieDetailsUI(
    id = id,
    title = title,
    originalTitle = originalTitle.takeIf { it?.isNotBlank() == true && it != title },
    overview = overview,
    posterUrl = images.posterUrl(posterPath),
    backdropUrl = images.backdropUrl(backdropPath),
    releaseYear = releaseDate?.take(4),
    language = originalLanguage.uppercase(),
    ratingText = "${voteAverage.format1()} (${voteCount.formatWithSpaces()})",
    budgetText = "$budget",
    runtimeText ="$runtime",
    revenueText = "$revenue",
    isAdult = adult,
    genreNames = genres.map{it.name}
)

private fun Double.format1(): String = "%,.1f".format(this).replace(',', ' ')
private fun Int.formatWithSpaces(): String = "%,d".format(this).replace(',', ' ')
