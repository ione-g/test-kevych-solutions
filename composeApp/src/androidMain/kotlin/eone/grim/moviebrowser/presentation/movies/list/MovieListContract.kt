package eone.grim.moviebrowser.presentation.movies.list

import eone.grim.moviebrowser.presentation.model.MovieUI
import eone.grim.moviebrowser.presentation.mvi.MviEffect
import eone.grim.moviebrowser.presentation.mvi.MviIntent
import eone.grim.moviebrowser.presentation.mvi.MviState

object MovieListContract {

    sealed interface Intent : MviIntent {
        data object OnAppear : Intent
        data class OnMovieClick(val id: Long) : Intent
        data object OnRetry : Intent
        data object OnLoadNextPage : Intent

    }

    data class State(
        val items: List<MovieUI> = emptyList(),
        val page: Int = 1,
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val endReached: Boolean = false,
        val error: String? = null,
        val errorMore: String? = null
    ) : MviState

    sealed interface Effect : MviEffect {
        data class NavigateToDetails(val id: Long) : Effect
        data class ShowMessage(val text: String) : Effect
    }
}