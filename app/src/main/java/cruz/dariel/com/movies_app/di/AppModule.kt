package cruz.dariel.com.movies_app.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cruz.dariel.com.movies_app.data.MovieRoomDatabase
import cruz.dariel.com.movies_app.data.dao.MovieDao
import cruz.dariel.com.movies_app.data.remote.MovieAPIService
import cruz.dariel.com.movies_app.data.repository.MovieRepository
import cruz.dariel.com.movies_app.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(Constants.API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson() : Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieAPIService = retrofit.create(MovieAPIService::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = MovieRoomDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideMovieDao(db: MovieRoomDatabase) = db.movieDao

    @Singleton
    @Provides
    fun provideRepository(movieService: MovieAPIService, movieDao: MovieDao) = MovieRepository(movieService, movieDao)
}