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
import cruz.dariel.com.movies_app.databinding.FragmentMovieListBinding
import cruz.dariel.com.movies_app.util.NetworkResource
import cruz.dariel.com.movies_app.util.Status
import cruz.dariel.com.movies_app.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment(), MovieAdapter.MovieItemListener {

    private lateinit var binding: FragmentMovieListBinding
    private lateinit var movieAdapter: MovieAdapter
    private val moviesViewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_movie_list,
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

        binding.rvMovieList.layoutManager = manager
        binding.rvMovieList.adapter = movieAdapter
    }

    override fun onResume() {
        super.onResume()
        setUpObservers()
    }
    private fun setUpObservers(mapQuery: Map<String, String> = mapOf("sort_by" to "vote_average.desc")){
        moviesViewModel.popularMovies(mapQuery).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.rvMovieList.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { movies ->
                            movieAdapter.setItems(movies)
                        }
                    }
                    Status.ERROR -> {
                        binding.rvMovieList.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.rvMovieList.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_by_popularity ->{
                setUpObservers()
            }
            R.id.sort_by_name ->{
                setUpObservers(mapOf("sort_by" to "original_title.desc"))
            }
            R.id.sort_by_date ->{
                setUpObservers(mapOf("sort_by" to "release_date.desc"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMovieClicked(movieId: Int, isFavorite: Boolean) {
        findNavController().navigate(
            R.id.action_mainTabFragment_to_movieDetailFragment,
            bundleOf("id" to movieId, "isFavorite" to isFavorite)
        )
    }

    override fun onFavoriteButtonClicked(movie: Movie) {
        movie.isFavorite = !movie.isFavorite
        moviesViewModel.addOrRemoveFavorite(movie).observe(viewLifecycleOwner, Observer {
            if (it == -1) {
                Toast.makeText(activity, getString(R.string.removed_favorites), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, getString(R.string.added_favorites), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
