package ui

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.xodus.templatefive.R
import com.xodus.templatefive.databinding.ActivityBaseBinding
import dagger.hilt.android.AndroidEntryPoint
import util.GoogleBillingV4Helper
import util.NativeBillingV4Helper

@AndroidEntryPoint
class BaseActivity : ThemeAwareActivity() {
    private var _binding: ActivityBaseBinding? = null
    private val binding: ActivityBaseBinding
        get() = _binding!!
    private var barHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.baseNvNavigation.post { barHeight = binding.baseNvNavigation.height }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.baseActivity_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(binding.baseNvNavigation, navHostFragment!!.navController)
        setLoading(false)
        appClass.nativeBillingHelper = NativeBillingV4Helper(this, appClass)
        appClass.googleBillingHelper = GoogleBillingV4Helper(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setLoading(visible: Boolean) {
        binding.baseFlLoading.isVisible = visible
    }

    fun hideNavigationBar(hide: Boolean, duration: Long = 500, onFinish: () -> (Unit) = {}) {
        //        if (barHeight == 0) {
        //            binding.baseNvNavigation.post {
        //                barHeight = binding.baseNvNavigation.height
        //                hideNavigationBar(hide, 0, onFinish)
        //            }
        //        } else {
        //            val animator: ValueAnimator = if (hide) {
        //                ValueAnimator.ofInt(binding.baseNvNavigation.height, 0)
        //            } else {
        //                ValueAnimator.ofInt(binding.baseNvNavigation.height, barHeight)
        //            }
        //            animator.duration = duration
        //            animator.addUpdateListener {
        //                val params = binding.baseNvNavigation.layoutParams as LinearLayout.LayoutParams
        //                params.height = it.animatedValue as Int
        //                binding.baseNvNavigation.layoutParams = params
        //            }
        //            animator.start()
        //            animator.doOnEnd {
        //                onFinish()
        //            }
        //        }
    }
}