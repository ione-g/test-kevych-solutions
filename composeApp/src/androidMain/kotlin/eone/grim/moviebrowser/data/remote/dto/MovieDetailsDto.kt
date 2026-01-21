package eone.grim.moviebrowser.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto (
    val id: Long,
    val adult: Boolean = false,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("genres") val genres: List<GenreDto> = emptyList(),
    @SerialName("original_language") val originalLanguage: String = "",
    @SerialName("original_title") val originalTitle: String = "",
    val overview: String = "",
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    val title: String = "",
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val budget: Long = 0,
    val revenue: Long = 0,
    val runtime: Long = 0
)