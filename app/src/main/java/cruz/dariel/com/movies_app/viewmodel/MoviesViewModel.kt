package cruz.dariel.com.movies_app.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import cruz.dariel.com.movies_app.data.entities.Movie
import cruz.dariel.com.movies_app.data.repository.MovieRepository
import cruz.dariel.com.movies_app.util.NetworkResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    // Web Service Methods
    fun popularMovies(queryMap: Map<String, String>) = liveData(Dispatchers.IO) {
        emit(NetworkResource.loading(data = null))
        try {
            val movieResults = movieRepository.getPopularMovies(queryMap).results
            movieResults?.map {
                val dbMovie = getLocalMovieDetails(it.id)
                if(dbMovie != null){
                    it.isFavorite = true
                }
            }
            emit(NetworkResource.success(data = movieResults))
        }catch (exception: Exception){
            emit(NetworkResource.error(data = null, message = exception.message ?: "Error Ocurred"))
        }
    }

    fun getMovieDetails(id: Int) = liveData(Dispatchers.IO) {
        emit(NetworkResource.loading(data = null))
        try{
            val movie = movieRepository.getMovieDetails(id)
            emit(NetworkResource.success(data = movie))
        }catch (exception: Exception){
            emit(NetworkResource.error(data = null, message = exception.message ?: "Error Ocurred"))
        }
    }

    // DB Methods
    var favoriteMovies = movieRepository.getFavoriteMovies()

    private fun getLocalMovieDetails(id: Int) = movieRepository.getLocalMovieDetails(id)

    fun addOrRemoveFavorite(movie: Movie) = liveData(Dispatchers.IO) {
            val dbMovie = getLocalMovieDetails(movie.id)
            val result = if(dbMovie != null){
                deleteMovie(movie)
                -1
            }else{
                insertMovie(movie)
                1
            }
        emit(result)
    }

    private fun insertMovie(movie: Movie) = viewModelScope.launch {
        movieRepository.insertMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        movieRepository.deleteMovie(movie)
    }

}