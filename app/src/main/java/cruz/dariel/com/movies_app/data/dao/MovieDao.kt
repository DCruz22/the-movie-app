package cruz.dariel.com.movies_app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import cruz.dariel.com.movies_app.data.entities.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: Movie?)

    @Delete
    suspend fun deleteMovie(movie: Movie?)

    @Query("Select * from movie_table Order By average DESC")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("Select * from movie_table Where id = :id")
    fun getMovieDetails(id: Int): Movie

}