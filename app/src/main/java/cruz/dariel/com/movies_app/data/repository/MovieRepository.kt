package cruz.dariel.com.movies_app.data.repository

import cruz.dariel.com.movies_app.data.dao.MovieDao
import cruz.dariel.com.movies_app.data.entities.Movie
import cruz.dariel.com.movies_app.data.remote.MovieAPIService
import retrofit2.http.QueryMap
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieAPIService,
    private val movieDao: MovieDao
) {

    // Web Service Methods
    suspend fun getPopularMovies(queryMap: Map<String, String>) = movieService.getPopularMovies(queryMap)

    suspend fun getMovieDetails(id: Int) = movieService.getMovieDetails(movieId = id)

    // DB methods
    fun getFavoriteMovies() = movieDao.getAllMovies()

    fun getLocalMovieDetails(id: Int) = movieDao.getMovieDetails(id)
    suspend fun insertMovie(movie: Movie) = movieDao.insertMovie(movie = movie)
    suspend fun deleteMovie(movie: Movie) = movieDao.deleteMovie(movie = movie)

}