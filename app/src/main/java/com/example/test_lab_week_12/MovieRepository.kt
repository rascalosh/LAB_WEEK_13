package com.example.test_lab_week_12

import com.example.test_lab_week_12.api.MovieService
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "e98ba98a83509f8bb5ed17cb0b5c0f43"

    // fetch movies from the API
    // this function returns a Flow of Movie objects
    // a Flow is a type of coroutine that can emit multiple values
    // for more info, see: https://kotlinlang.org/docs/flow.html#flows
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val movies = movieService.getPopularMovies(apiKey).results


            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()

            val filtered = movies
                .filter { it.releaseDate?.startsWith(currentYear) == true }
                .sortedByDescending { it.popularity }

            emit(filtered)
        }.flowOn(Dispatchers.IO)
    }

}