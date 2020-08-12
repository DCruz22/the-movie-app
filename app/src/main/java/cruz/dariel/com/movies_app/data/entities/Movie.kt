package cruz.dariel.com.movies_app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var overview: String = "",
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("vote_average")
    var average: Double = 0.0,
    @SerializedName("release_date")
    var releaseDate: String = "",
    var isFavorite: Boolean = false
)