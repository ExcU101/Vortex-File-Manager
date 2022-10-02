package io.github.excu101.vortex.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.excu101.vortex.VortexServiceApi
import io.github.excu101.vortex.ui.screen.main.MainActivity

open class VortexServiceFragment : Fragment {

    private var service: VortexServiceApi? = null

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    fun requireService(): VortexServiceApi {
        check(service != null) { "Service not found" }

        return service!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        service = (requireActivity() as MainActivity).service
        return null
    }

}