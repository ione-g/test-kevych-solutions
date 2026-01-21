package eone.grim.moviebrowser.data.maper

import eone.grim.moviebrowser.data.remote.dto.MovieDetailsDto
import eone.grim.moviebrowser.domain.entity.MovieDetails

fun MovieDetailsDto.toDomain(): MovieDetails = MovieDetails(
    id = id,
    title = title,
    originalTitle = originalTitle.takeIf { it.isNotBlank() && it != title },
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    originalLanguage = originalLanguage,
    voteAverage = voteAverage,
    voteCount = voteCount,
    budget = budget,
    revenue = revenue,
    runtime = runtime,
    adult = adult,
    genres = genres.map{it.toDomain()}
)

