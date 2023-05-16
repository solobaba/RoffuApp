package com.example.roffuapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import com.example.roffuapp.ui.theme.Dimension

@Composable
fun DrawableButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.primary,
    iconTint: Color = Color.Unspecified,
    onButtonClicked: () -> Unit,
    painter: Painter,
    shape: Shape = MaterialTheme.shapes.medium,
    iconSize: Dp = Dimension.mdIcon,
    elevation: Dp = Dimension.zero,
    paddingValue: PaddingValues = PaddingValues(Dimension.xs),
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape)
            .clip(shape)
            .background(backgroundColor)
            .clickable(
                onClick = {
                    if (enabled) onButtonClicked()
                }
            )
            .padding(paddingValues = paddingValue)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .size(iconSize),
            painter = painter,
            contentDescription = "icon next",
            tint = iconTint,
        )
    }
}