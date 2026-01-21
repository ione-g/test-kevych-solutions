package eone.grim.moviebrowser.presentation.model

data class MovieDetailsUI(
    val id: Long,
    val title: String,
    val originalTitle: String?,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseYear: String?,
    val language: String,
    val budgetText: String,
    val runtimeText: String,
    val revenueText: String,
    val ratingText: String,
    val isAdult: Boolean,
    val genreNames: List<String?>
)

