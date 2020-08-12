package cruz.dariel.com.movies_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide

import cruz.dariel.com.movies_app.R
import cruz.dariel.com.movies_app.data.entities.Movie
import cruz.dariel.com.movies_app.databinding.FragmentMovieDetailBinding
import cruz.dariel.com.movies_app.util.Constants
import cruz.dariel.com.movies_app.util.Status
import cruz.dariel.com.movies_app.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private val moviesViewModel: MoviesViewModel by viewModels()
    private var movieId = 0
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_movie_detail,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId = arguments?.getInt("id") ?: 0
        isFavorite = arguments?.getBoolean("isFavorite") ?: false

        moviesViewModel.getMovieDetails(movieId ?: 0).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource.status){
                    Status.LOADING ->{
                        binding.progressBar.visibility = View.VISIBLE
                        binding.detailsLayout.visibility = View.GONE
                    }
                    Status.SUCCESS ->{
                        bindDetailsView(resource.data!!)
                        binding.progressBar.visibility = View.GONE
                        binding.detailsLayout.visibility = View.VISIBLE
                    }
                    Status.ERROR ->{
                        binding.progressBar.visibility = View.GONE
                        binding.detailsLayout.visibility = View.GONE
                        Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun bindDetailsView(movie: Movie) {
        binding.title.text = movie.title
        binding.average.text = "Average: ${movie.average}"
        binding.releaseDate.text = "Released On: ${movie.releaseDate}"
        binding.overview.text = movie.overview
        Glide.with(binding.root)
            .load(Constants.LARGE_IMAGE_URL + movie.posterPath)
            .into(binding.poster)

        setFavoriteIcon(isFavorite)

        binding.detailsFavoriteFab.setOnClickListener {
            movie.isFavorite = !movie.isFavorite
            isFavorite = !isFavorite
            moviesViewModel.addOrRemoveFavorite(movie).observe(viewLifecycleOwner, Observer {
                if (it == -1) {
                    Toast.makeText(
                        activity,
                        getString(R.string.removed_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.added_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            setFavoriteIcon(isFavorite)
        }
    }

    private fun setFavoriteIcon(isFavorite: Boolean){
        if(isFavorite){
            binding.detailsFavoriteFab.setImageResource(R.drawable.ic_favorite_green_24dp)
        }else{
            binding.detailsFavoriteFab.setImageResource(R.drawable.ic_favorite_white_24dp)
        }
    }
}
