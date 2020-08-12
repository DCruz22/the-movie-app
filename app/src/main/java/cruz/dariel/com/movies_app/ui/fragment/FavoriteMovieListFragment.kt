package cruz.dariel.com.movies_app.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import cruz.dariel.com.movies_app.R
import cruz.dariel.com.movies_app.adapter.MovieAdapter
import cruz.dariel.com.movies_app.data.entities.Movie
import cruz.dariel.com.movies_app.databinding.FragmentFavoriteMovieListBinding
import cruz.dariel.com.movies_app.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieListFragment : Fragment(), MovieAdapter.MovieItemListener {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: FragmentFavoriteMovieListBinding
    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var dbMovies: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate<FragmentFavoriteMovieListBinding>(
            inflater,
            R.layout.fragment_favorite_movie_list,
            container,
            false
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = GridLayoutManager(activity, 3)
        movieAdapter = MovieAdapter(this)

        binding.rvFavMovieList.layoutManager = manager
        binding.rvFavMovieList.adapter = movieAdapter
    }

    override fun onResume() {
        super.onResume()

        moviesViewModel.favoriteMovies.observe(viewLifecycleOwner, Observer {
            setUpDbMovieList(it)
        })
    }

    private fun setUpDbMovieList(movies: List<Movie>){
        dbMovies = movies
        movieAdapter.setItems(dbMovies)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_by_popularity ->{
                setUpDbMovieList(dbMovies.sortedByDescending { it.average })
            }
            R.id.sort_by_name ->{
                setUpDbMovieList(dbMovies.sortedByDescending { it.title })
            }
            R.id.sort_by_date ->{
                setUpDbMovieList(dbMovies.sortedByDescending { it.releaseDate })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMovieClicked(movieId: Int, isFavorite: Boolean) {
        findNavController().navigate(R.id.action_mainTabFragment_to_movieDetailFragment,
            bundleOf("id" to movieId, "isFavorite" to isFavorite))
    }

    override fun onFavoriteButtonClicked(movie: Movie) {
        moviesViewModel.deleteMovie(movie)
        Toast.makeText(activity, getString(R.string.removed_favorites), Toast.LENGTH_SHORT).show()
    }

}
