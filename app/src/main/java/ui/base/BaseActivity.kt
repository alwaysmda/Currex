package ui.base

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.Router.PopRootControllerMode
import com.bluelinelabs.conductor.RouterTransaction
import com.example.currex.R
import dagger.hilt.android.AndroidEntryPoint
import ui.convert.ConvertController
import util.extension.getColorFromAttributes
import util.extension.setStatusbarColor


@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {

    private lateinit var router: Router

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
        setStatusbarColor(this, getColorFromAttributes(this, R.attr.colorBackground0))
        setContentView(R.layout.activity_base)
        val container = findViewById<ViewGroup>(R.id.base_container)

        router = attachRouter(this, container, savedInstanceState).setPopRootControllerMode(PopRootControllerMode.NEVER)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(ConvertController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}