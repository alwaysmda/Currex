package ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.android.currex.R
import com.android.currex.databinding.ActivityBaseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {
    private var _binding: ActivityBaseBinding? = null
    val binding: ActivityBaseBinding
        get() = _binding!!

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.remove("android:support:fragments")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //        binding.baseNvNavigation.post { barHeight = binding.baseNvNavigation.height }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.baseActivity_fragment) as NavHostFragment
        //        NavigationUI.setupWithNavController(binding.baseNvNavigation, navHostFragment.navController)
        //        setNavigationTabs()
        //        app.nativeBillingHelper = NativeBillingV4Helper(this, app)
        //        app.googleBillingHelper = GoogleBillingV4Helper(this)
        //        binding.baseNvNavigation.setOnItemSelectedListener { item ->
        // In order to get the expected behavior, you have to call default Navigation method manually
        //            NavigationUI.onNavDestinationSelected(item, navHostFragment.navController)
        //            return@setOnItemSelectedListener true
        //        }
    }
}