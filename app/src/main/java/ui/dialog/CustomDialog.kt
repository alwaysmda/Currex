package ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.FEATURE_NO_TITLE
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.xodus.templatefive.R
import com.xodus.templatefive.databinding.DialogCustomBinding
import main.ApplicationClass
import util.extension.getScreenWidth


class CustomDialog(private val app: ApplicationClass, private val customView: Int = R.layout.dialog_custom) : DialogFragment() {

    //Element
    private var title: String = ""
    private var content: String = ""
    private var positiveText: String = ""
    private var negativeText: String = ""
    private var neutralText: String = ""
    private var onPositive: (CustomDialog) -> Unit = {}
    private var onNegative: (CustomDialog) -> Unit = {}
    private var onNeutral: (CustomDialog) -> Unit = {}
    private var showProgress: Boolean = false
    private var showButtons: Boolean = false
    private var isTitleCenter: Boolean = false
    private var isContentCenter: Boolean = false
    private var titleColor: Int = 0
    private var contentColor: Int = 0
    private var positiveColor: Int = 0
    private var negativeColor: Int = 0
    private var neutralColor: Int = 0
    private var onViewCreated: (CustomDialog, View) -> (Unit) = { _, _ -> }
    private var onViewCreatedSet = false
    private var dismissOnButtonClick = true

    fun onViewCreated(onViewCreated: (CustomDialog, View) -> Unit): CustomDialog {
        this.onViewCreated = onViewCreated
        onViewCreatedSet = true
        return this
    }

    fun showLoading(show: Boolean): CustomDialog {
        showProgress = show
        //        bind()
        init()
        return this
    }

    fun setTitle(text: Int): CustomDialog {
        title = app.resources.getString(text)
        //        bind()
        init()
        return this
    }

    fun setTitle(text: String): CustomDialog {
        title = text
        //        bind()
        init()
        return this
    }

    fun setTitleColor(color: Int): CustomDialog {
        titleColor = color
        //        bind()
        init()
        return this
    }

    fun setTitleCenter(): CustomDialog {
        isTitleCenter = true
        //        bind()
        init()
        return this
    }

    fun setContent(text: Int): CustomDialog {
        content = app.resources.getString(text)
        //        bind()
        init()
        return this
    }

    fun setContent(text: String): CustomDialog {
        content = text
        //        bind()
        init()
        return this
    }

    fun setContentColor(color: Int): CustomDialog {
        contentColor = color
        //        bind()
        init()
        return this
    }


    fun setContentCenter(): CustomDialog {
        isContentCenter = true
        //        bind()
        init()
        return this
    }

    fun setPositiveText(text: Int): CustomDialog {
        showButtons = true
        positiveText = app.resources.getString(text)
        //        bind()
        init()
        return this
    }

    fun setPositiveText(text: String): CustomDialog {
        showButtons = true
        positiveText = text
        //        bind()
        init()
        return this
    }

    fun setPositiveColor(color: Int): CustomDialog {
        positiveColor = color
        //        bind()
        init()
        return this
    }

    fun onPositive(action: (CustomDialog) -> Unit): CustomDialog {
        showButtons = true
        onPositive = action
        //        bind()
        init()
        return this
    }

    fun setNegativeText(text: String): CustomDialog {
        showButtons = true
        negativeText = text
        //        bind()
        init()
        return this
    }

    fun setNegativeText(text: Int): CustomDialog {
        showButtons = true
        negativeText = app.resources.getString(text)
        //        bind()
        init()
        return this
    }

    fun setNegativeColor(color: Int): CustomDialog {
        negativeColor = color
        //        bind()
        init()
        return this
    }

    fun onNegative(action: (CustomDialog) -> Unit): CustomDialog {
        showButtons = true
        onNegative = action
        //        bind()
        init()
        return this
    }

    fun setNeutralText(text: String): CustomDialog {
        showButtons = true
        neutralText = text
        //        bind()
        init()
        return this
    }

    fun setNeutralText(text: Int): CustomDialog {
        showButtons = true
        neutralText = app.resources.getString(text)
        //        bind()
        init()
        return this
    }

    fun setNeutralColor(color: Int): CustomDialog {
        neutralColor = color
        //        bind()
        init()
        return this
    }

    fun onNeutral(action: (CustomDialog) -> Unit): CustomDialog {
        showButtons = true
        onNeutral = action
        //        bind()
        init()
        return this
    }

    fun showButtons(show: Boolean): CustomDialog {
        this.showButtons = show
        //        bind()
        init()
        return this
    }

    fun setCancelabel(cancelable: Boolean): CustomDialog {
        isCancelable = cancelable
        return this
    }

    fun setDismissOnButtonClick(dismiss: Boolean): CustomDialog {
        dismissOnButtonClick = dismiss
        return this
    }

    fun show(fragmentManager: FragmentManager?, onViewCreated: (CustomDialog, View) -> (Unit) = { _, _ -> }): CustomDialog {
        if (onViewCreatedSet.not())
            this.onViewCreated = onViewCreated
        fragmentManager?.let {
            show(it, this.javaClass.simpleName)
        }
        return this
    }

    private var _binding: DialogCustomBinding? = null
    private val binding: DialogCustomBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //        val view = inflater.inflate(customView, container, false)
        if (customView == R.layout.dialog_custom) {
            _binding = DialogCustomBinding.inflate(inflater).apply {
                lifecycleOwner = viewLifecycleOwner
                app = this@CustomDialog.app
            }
            init()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(this, view)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(requireActivity())
        root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        // creating the fullscreen dialog
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout((getScreenWidth() / 100) * 90, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun init() {
        if (_binding == null)
            return
        with(binding) {
            dialogCustomTvContent.movementMethod = ScrollingMovementMethod()
            with(dialogCustomTvTitle) {
                text = title
                if (titleColor == 0) {
                    setTextColor(ContextCompat.getColor(this@CustomDialog.app, R.color.md_black_1000))
                } else {
                    setTextColor(titleColor)
                }
                if (isTitleCenter) {
                    gravity = Gravity.CENTER
                }
            }
            with(dialogCustomTvContent) {
                text = content
                if (contentColor == 0) {
                    setTextColor(ContextCompat.getColor(this@CustomDialog.app, R.color.md_black_1000))
                } else {
                    setTextColor(contentColor)
                }
                if (isContentCenter) {
                    gravity = Gravity.CENTER
                }
            }
            dialogCustomPbProgress.isVisible = showProgress
            dialogCustomLlButton.isVisible = showButtons
            with(dialogCustomBtnPositive) {
                visibility = if (positiveText.isEmpty()) View.INVISIBLE else View.VISIBLE
                text = positiveText
                setOnClickListener {
                    onPositive(this@CustomDialog)
                    if (dismissOnButtonClick) {
                        dismiss()
                    }
                }
            }
            with(dialogCustomBtnNegative) {
                visibility = if (negativeText.isEmpty()) View.INVISIBLE else View.VISIBLE
                text = negativeText
                setOnClickListener {
                    onNegative(this@CustomDialog)
                    if (dismissOnButtonClick) {
                        dismiss()
                    }
                }
            }
            with(dialogCustomBtnNeutral) {
                visibility = if (neutralText.isEmpty()) View.INVISIBLE else View.VISIBLE
                text = neutralText
                setOnClickListener {
                    onNeutral(this@CustomDialog)
                    if (dismissOnButtonClick) {
                        dismiss()
                    }
                }
            }

        }
    }

}