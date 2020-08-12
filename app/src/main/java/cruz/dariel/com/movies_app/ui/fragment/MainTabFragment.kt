package cruz.dariel.com.movies_app.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import cruz.dariel.com.movies_app.R
import cruz.dariel.com.movies_app.adapter.TabAdapter
import cruz.dariel.com.movies_app.databinding.FragmentMainTabBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainTabFragment : Fragment() {

    private lateinit var tabAdapter: TabAdapter
    private val icons = arrayOf(R.drawable.ic_local_movies_white_24dp, R.drawable.ic_favorite_white_24dp)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentMainTabBinding>(inflater, R.layout.fragment_main_tab, container, false)

        tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter.addFragment(MovieListFragment(), "Movies")
        tabAdapter.addFragment(FavoriteMovieListFragment(), "Favorites")

        binding.pager.adapter = tabAdapter
        binding.tabLayout.setupWithViewPager(binding.pager)
        for(i in 0 until binding.tabLayout.tabCount){
            binding.tabLayout.getTabAt(i)!!.icon = requireActivity().getDrawable(icons[i])
        }

        return binding.root
    }
}
