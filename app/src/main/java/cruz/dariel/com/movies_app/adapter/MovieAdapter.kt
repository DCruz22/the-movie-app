package cruz.dariel.com.movies_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import cruz.dariel.com.movies_app.R
import cruz.dariel.com.movies_app.data.entities.Movie
import cruz.dariel.com.movies_app.databinding.MovieItemBinding
import cruz.dariel.com.movies_app.util.Constants

class MovieAdapter(private val listener: MovieItemListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    interface MovieItemListener {
        fun onMovieClicked(movieId: Int, isFavorite: Boolean)
        fun onFavoriteButtonClicked(movie: Movie)
    }

    private var movies = listOf<Movie>()

    fun setItems(items: List<Movie>) {
        this.movies = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val current = this.movies[position]
        holder.bindItem(current)
    }

    class MovieViewHolder(
        private val itemMovieItemBinding: MovieItemBinding,
        private val listener: MovieItemListener
    ) : RecyclerView.ViewHolder(itemMovieItemBinding.root), View.OnClickListener {

        private lateinit var movie: Movie

        init {
            itemMovieItemBinding.moviePoster.setOnClickListener(this)
            itemMovieItemBinding.addToFavorites.setOnClickListener(this)
        }

        fun bindItem(movie: Movie) {
            this.movie = movie
            setFavoriteDrawable()
            Glide.with(itemMovieItemBinding.root)
                .load(Constants.SMALL_IMAGE_URL + movie.posterPath)
                .into(itemMovieItemBinding.moviePoster)
        }

        override fun onClick(p0: View?) {
            when (p0?.id) {
                itemMovieItemBinding.addToFavorites.id -> {
                    listener.onFavoriteButtonClicked(movie)
                    setFavoriteDrawable()
                }
                itemMovieItemBinding.moviePoster.id -> listener.onMovieClicked(
                    movie.id,
                    movie.isFavorite
                )
            }
        }

        private fun setFavoriteDrawable() {
            itemMovieItemBinding.addToFavorites.setImageResource(
                when (movie.isFavorite) {
                    true -> R.drawable.ic_favorite_green_24dp
                    else -> R.drawable.ic_favorite_gray_24dp
                }
            )
        }

    }
}