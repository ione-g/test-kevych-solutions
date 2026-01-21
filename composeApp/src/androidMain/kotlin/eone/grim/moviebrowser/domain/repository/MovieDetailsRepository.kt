package eone.grim.moviebrowser.domain.repository

import eone.grim.moviebrowser.domain.entity.MovieDetails

interface MovieDetailsRepository {
    suspend fun getDetails(id: Long, language: String? = null): MovieDetails

}