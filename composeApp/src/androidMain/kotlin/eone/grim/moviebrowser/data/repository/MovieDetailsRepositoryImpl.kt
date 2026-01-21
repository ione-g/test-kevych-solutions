package eone.grim.moviebrowser.data.repository

import android.util.Log
import eone.grim.moviebrowser.data.maper.toDomain
import eone.grim.moviebrowser.data.remote.TmdbApi
import eone.grim.moviebrowser.data.remote.dto.MovieDetailsDto
import eone.grim.moviebrowser.domain.entity.MovieDetails
import eone.grim.moviebrowser.domain.repository.MovieDetailsRepository

class MovieDetailsRepositoryImpl(
    private val api: TmdbApi
) : MovieDetailsRepository {

    override suspend fun getDetails(
        id: Long,
        language: String?
    ): MovieDetails {
        val dto: MovieDetailsDto = api.getMovieDetails(
            movieId = id,
            language = language
        )
        Log.d("Checking full loading",dto.toString())
        return dto.toDomain()
    }
}