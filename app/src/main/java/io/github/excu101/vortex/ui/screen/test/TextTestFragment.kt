package io.github.excu101.vortex.ui.screen.test

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import io.github.excu101.vortex.ui.component.bar

class TextTestFragment : Fragment() {

    companion object {
        fun newInstance(text: String): TextTestFragment {
            val args = Bundle()
            args.putString("textState", text)
            val fragment = TextTestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var root: CoordinatorLayout? = null

    private var text: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        root = CoordinatorLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        text = TextView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                gravity = CENTER
            }
        }

        root?.addView(text)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text?.text = requireArguments().getString("textState")
    }

}