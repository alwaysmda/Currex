package ui.base

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.currex.R
import com.example.currex.databinding.ActivityBaseBinding
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Dark_Blue)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = ContextCompat.getColor(baseContext, R.color.md_black_1000)
            window.navigationBarColor = ContextCompat.getColor(baseContext, R.color.md_black_1000)
        }
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}