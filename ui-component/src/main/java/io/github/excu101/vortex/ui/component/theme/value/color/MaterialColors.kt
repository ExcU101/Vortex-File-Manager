package io.github.excu101.vortex.ui.component.theme.value.color

import io.github.excu101.manager.model.Color

object MaterialColors {
    object Red : MaterialColor, AdditionalMaterialColor {
        override val color50: Color = Color("#FFEBEE")
        override val color100: Color = Color("#FFCDD2")
        override val color200: Color = Color("#EF9A9A")
        override val color300: Color = Color("#E57373")
        override val color400: Color = Color("#EF5350")
        override val color500: Color = Color("#F44336")
        override val color600: Color = Color("#E53935")
        override val color700: Color = Color("#D32F2F")
        override val color800: Color = Color("#C62828")
        override val color900: Color = Color("#B71C1C")
        override val colorA100: Color = Color("#FF8A80")
        override val colorA200: Color = Color("#FF5252")
        override val colorA400: Color = Color("#FF1744")
        override val colorA700: Color = Color("#D50000")
    }

    object Pink : MaterialColor, AdditionalMaterialColor {
        override val color50: Color = Color("#FCE4EC")
        override val color100: Color = Color("#F8BBD0")
        override val color200: Color = Color("#F48FB1")
        override val color300: Color = Color("#F06292")
        override val color400: Color = Color("#EC407A")
        override val color500: Color = Color("#E91E63")
        override val color600: Color = Color("#D81B60")
        override val color700: Color = Color("#C2185B")
        override val color800: Color = Color("#AD1457")
        override val color900: Color = Color("#880E4F")
        override val colorA100: Color = Color("#FF80AB")
        override val colorA200: Color = Color("#FF4081")
        override val colorA400: Color = Color("#F50057")
        override val colorA700: Color = Color("#C51162")
    }

    object Purple : MaterialColor, AdditionalMaterialColor {
        override val color50: Color = Color("#F3E5F5")
        override val color100: Color = Color("#E1BEE7")
        override val color200: Color = Color("#CE93D8")
        override val color300: Color = Color("#BA68C8")
        override val color400: Color = Color("#AB47BC")
        override val color500: Color = Color("#9C27B0")
        override val color600: Color = Color("#8E24AA")
        override val color700: Color = Color("#7B1FA2")
        override val color800: Color = Color("#6A1B9A")
        override val color900: Color = Color("#4A148C")
        override val colorA100: Color = Color("#EA80FC")
        override val colorA200: Color = Color("#E040FB")
        override val colorA400: Color = Color("#D500F9")
        override val colorA700: Color = Color("#AA00FF")
    }

    object DeepPurple : MaterialColor, AdditionalMaterialColor {
        override val color50: Color = Color("#EDE7F6")
        override val color100: Color = Color("#D1C4E9")
        override val color200: Color = Color("#B39DDB")
        override val color300: Color = Color("#9575CD")
        override val color400: Color = Color("#7E57C2")
        override val color500: Color = Color("#673AB7")
        override val color600: Color = Color("#5E35B1")
        override val color700: Color = Color("#512DA8")
        override val color800: Color = Color("#4527A0")
        override val color900: Color = Color("#311B92")
        override val colorA100: Color = Color("#B388FF")
        override val colorA200: Color = Color("#7C4DFF")
        override val colorA400: Color = Color("#651FFF")
        override val colorA700: Color = Color("#6200EA")
    }

    object Indigo : MaterialColor, AdditionalMaterialColor {
        override val color50: Color = Color("#E8EAF6")
        override val color100: Color = Color("#C5CAE9")
        override val color200: Color = Color("#9FA8DA")
        override val color300: Color = Color("#7986CB")
        override val color400: Color = Color("#5C6BC0")
        override val color500: Color = Color("#3F51B5")
        override val color600: Color = Color("#3949AB")
        override val color700: Color = Color("#303F9F")
        override val color800: Color = Color("#283593")
        override val color900: Color = Color("#1A237E")
        override val colorA100: Color = Color("#8C9EFF")
        override val colorA200: Color = Color("#536DFE")
        override val colorA400: Color = Color("#3D5AFE")
        override val colorA700: Color = Color("#304FFE")
    }
}