package io.github.excu101.pluginsystem.model

import android.graphics.drawable.Drawable
import android.view.View

interface Screen {

    val route: String

    val icon: Drawable?

    val content: View

}