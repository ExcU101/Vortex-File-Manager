package io.github.excu101.vortex.ui.screen.storage.page.create

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import io.github.excu101.filesystem.fs.utils.toPath
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.navigation.dsl.FragmentFactory
import io.github.excu101.vortex.navigation.utils.NavigationController
import io.github.excu101.vortex.ui.component.parcelable
import io.github.excu101.vortex.ui.component.storage.create.StorageItemCreateBinding
import io.github.excu101.vortex.ui.navigation.AppNavigation.Args.Storage

class StorageItemCreatePage : Fragment() {

    companion object : FragmentFactory<StorageItemCreatePage> {
        override fun createFragment(): StorageItemCreatePage {
            return StorageItemCreatePage()
        }
    }

    private var binding: StorageItemCreateBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = StorageItemCreateBinding(requireContext())
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            arguments?.parcelable<PathItem>(Storage.CreatePage.ParentDirectoryKey)?.path?.let {
                path.setText(it)
            }

            name.requestFocus()

            name.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    nameLayout.error = if (name.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            path.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    pathLayout.error = if (path.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            type.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    typeLayout.error = if (type.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            mode.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    modeLayout.error = if (mode.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            confirm.setOnClickListener { view ->
                if (name.text?.isEmpty() != false) {
                    nameLayout.error = "To create item needs to enter item name"
                    return@setOnClickListener
                }
                if (path.text?.isEmpty() != false) {
                    pathLayout.error = "To create item needs to enter item path"
                    return@setOnClickListener
                }
                if (type.text?.isEmpty() != false) {
                    typeLayout.error = "To create item needs to enter item type"
                    return@setOnClickListener
                }
                if (mode.text?.isEmpty() != false) {
                    modeLayout.error = "To create item needs to enter item type"
                    return@setOnClickListener
                }
                val name = this.name.text.toString().toPath()
                val src = this.path.text.toString().toPath()
                val path = src.resolve(name)
                val mode = this.mode.text.toString().toInt()

                setFragmentResult("operation",
                    bundleOf(
                        "path" to path.toString(),
                        "mode" to mode,
                        "isDir" to (type.text.toString() == "Directory")
                    )
                )
                NavigationController().navigateUp()
            }
        }
    }

}