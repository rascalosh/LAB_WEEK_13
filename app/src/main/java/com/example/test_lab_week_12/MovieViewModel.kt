package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    // define the StateFlow in replace of the LiveData
    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    // fetch movies from the API
    private fun fetchPopularMovies() {
        // launch a coroutine in viewModelScope
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .catch { e ->
                    // catches exceptions from the Flow
                    _error.value = "An exception occurred: ${e.message}"
                }
                .collect { movieList ->
                    // collect emits values from the Flow to StateFlow
                    _popularMovies.value = movieList
                }
        }
    }
}