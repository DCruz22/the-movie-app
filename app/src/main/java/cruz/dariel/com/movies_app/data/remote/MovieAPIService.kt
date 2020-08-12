package cruz.dariel.com.movies_app.data.remote

import android.provider.SyncStateContract
import cruz.dariel.com.movies_app.data.entities.Movie
import cruz.dariel.com.movies_app.data.entities.SearchResponse
import cruz.dariel.com.movies_app.util.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MovieAPIService {
    @GET("discover/movie?api_key=${Constants.API_KEY}&include_adult=false&")
    suspend fun getPopularMovies(@QueryMap queryMap: Map<String, String>): SearchResponse

    @GET("movie/{movie_id}?api_key=${Constants.API_KEY}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Movie
}