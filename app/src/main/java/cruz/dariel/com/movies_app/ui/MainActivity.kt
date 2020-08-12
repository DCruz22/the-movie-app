package cruz.dariel.com.movies_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import cruz.dariel.com.movies_app.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val myNavHostFragment by lazy {
        this.findNavController(R.id.myNavHostFragment)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appBarConfiguration = AppBarConfiguration(myNavHostFragment.graph)
        NavigationUI.setupActionBarWithNavController(this, myNavHostFragment)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(myNavHostFragment, appBarConfiguration)
    }

}
