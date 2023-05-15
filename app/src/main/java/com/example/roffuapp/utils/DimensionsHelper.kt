package com.example.roffuapp.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import timber.log.Timber

sealed class DimensionsHelper {
}

@Composable
fun Context.getScreenSize() : Size {
    val currentScreenWidth = this.resources.displayMetrics.widthPixels.getDp()
    val currentScreenHeight = this .resources.displayMetrics.heightPixels.getDp()
    return Size(width = currentScreenWidth, height = currentScreenHeight)
}

data class Size(
    val width: Dp,
    val height: Dp,
)

/** An extension function that is used to convert the px as Int values to a valid Dp */
@Composable
fun Int.getDp() : Dp{
    val px = this
    with(LocalDensity.current){
        Timber.d("density is ${this.density}")
        return px.toDp()
    }
}

/** An extension function that is used to convert the px as float values to a valid Dp */
@Composable
fun Float.getDp() : Dp{
    val px = this
    with(LocalDensity.current){
        Timber.d("density is ${this.density}")
        return px.toDp()
    }
}

val LocalScreenSize = compositionLocalOf<Size>{ error("Screen size is not specified") }