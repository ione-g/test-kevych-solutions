package eone.grim.moviebrowser.domain.entity

data class MovieDetails(
    val id: Long,
    val title: String,
    val originalTitle: String?,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val originalLanguage: String,
    val adult: Boolean,
    val genres: List<Genre>,
    val budget: Long,
    val revenue: Long,
    val runtime: Long,
    val voteAverage: Double,
    val voteCount: Int
)