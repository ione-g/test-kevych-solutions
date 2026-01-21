package eone.grim.moviebrowser.presentation.movies.details

import eone.grim.moviebrowser.data.images.ImageConfigStore
import eone.grim.moviebrowser.domain.usecase.GetMovieDetails
import eone.grim.moviebrowser.presentation.model.toUI
import eone.grim.moviebrowser.presentation.mvi.MviViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getDetails: GetMovieDetails,
    private val images: ImageConfigStore,
    private val scope: CoroutineScope
) : MviViewModel<MovieDetailsContract.Intent, MovieDetailsContract.State, MovieDetailsContract.Effect>(
    MovieDetailsContract.State()
) {
    private var job: Job? = null
    private var lastId: Long? = null

    override fun onIntent(intent: MovieDetailsContract.Intent) {
        when (intent) {
            is MovieDetailsContract.Intent.Load -> {
                lastId = intent.id
                load(intent.id)
            }
        }
    }

    private fun load(id: Long) {
        job?.cancel()
        job = scope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val details = getDetails(id)
                _state.value = _state.value.copy(
                    isLoading = false,
                    movie = details.toUI(images),
                    error = null
                )
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = t.message ?: "Unknown error"
                )
            }
        }
    }
}
