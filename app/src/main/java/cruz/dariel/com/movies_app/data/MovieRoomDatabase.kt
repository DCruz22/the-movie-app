package cruz.dariel.com.movies_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cruz.dariel.com.movies_app.data.dao.MovieDao
import cruz.dariel.com.movies_app.data.entities.Movie

@Database(entities = [Movie::class], version = 2, exportSchema = false)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object{
        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getInstance(context: Context): MovieRoomDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieRoomDatabase::class.java,
                        "movie_database"
                    ).
                        fallbackToDestructiveMigration().
                        build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}