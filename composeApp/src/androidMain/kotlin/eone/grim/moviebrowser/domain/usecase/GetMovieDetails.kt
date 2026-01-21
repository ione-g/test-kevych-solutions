package eone.grim.moviebrowser.domain.usecase

import eone.grim.moviebrowser.domain.entity.MovieDetails
import eone.grim.moviebrowser.domain.repository.MovieDetailsRepository

class GetMovieDetails(private val repo: MovieDetailsRepository) {
    suspend operator fun invoke(id: Long): MovieDetails = repo.getDetails(id)
}