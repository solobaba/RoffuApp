package com.example.roffuapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.example.roffuapp.sealed.Screen
import com.example.roffuapp.ui.theme.Dimension
import kotlin.math.roundToInt
import com.example.roffuapp.R

@Composable
fun AppBottomNav(
    activeRoute: String,
    bottomNavDestinations: List<Screen>,
    backgroundColor: Color,
    onCartOffsetMeasured: (offset: IntOffset) -> Unit,
    onActiveRouteChange: (route: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        val (cartOffsetY, setCartOffsetY) = remember {
            mutableStateOf(0)
        }
        Row(
            modifier = Modifier
                .onGloballyPositioned {
                    setCartOffsetY(it.size.height)
                }
                .fillMaxWidth()
                .padding(
                    horizontal = Dimension.pagePadding,
                    vertical = Dimension.pagePadding.div(2),
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            bottomNavDestinations.forEach {
                val isActive = activeRoute.equals(other = it.route, ignoreCase = true)
                AppBottomNavItem(
                    active = isActive,
                    title = stringResource(id = it.title ?: R.string.home),
                    icon = it.icon ?: R.drawable.ic_home_empty,
                    onRouteClicked = {
                        if (!isActive) {
                            onActiveRouteChange(it.route)
                        }
                    }
                )
                if (bottomNavDestinations.indexOf(it)
                        .plus(1) == bottomNavDestinations.size.div(2)
                ) {
                    Spacer(modifier = Modifier.width(Dimension.smIcon))
                }
            }
        }

        DrawableButton(
            modifier = Modifier
                .onGloballyPositioned {
                    it
                        .positionInWindow()
                        .let { offset ->
                            onCartOffsetMeasured(
                                IntOffset(
                                    x = offset.x
                                        .minus(Dimension.xlIcon.value)
                                        .roundToInt(),
                                    y = offset.y
                                        .plus(cartOffsetY.div(2))
                                        .roundToInt(),
                                )
                            )
                        }
                }
                .align(Alignment.TopCenter)
                .offset {
                    IntOffset(
                        y = -cartOffsetY.div(2),
                        x = 0,
                    )
                }
                .border(
                    width = Dimension.sm.div(2),
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape,
                ),
            painter = painterResource(id = R.drawable.ic_shopping_bag),
            backgroundColor = if (activeRoute == Screen.Cart.route) MaterialTheme.colors.primary
            else MaterialTheme.colors.background,
            iconSize = Dimension.mdIcon.times(0.8f),
            iconTint = if (activeRoute == Screen.Cart.route) MaterialTheme.colors.onPrimary
            else MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            onButtonClicked = { onActiveRouteChange(Screen.Cart.route) },
            shape = CircleShape,
            paddingValue = PaddingValues(Dimension.md),
        )
    }
}

@Composable
fun AppBottomNavItem(
    modifier: Modifier = Modifier,
    active: Boolean,
    title: String,
    icon: Int,
    onRouteClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .clickable {
                onRouteClicked()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .padding(bottom = Dimension.sm)
                .fillMaxWidth()
                .height(Dimension.xs)
                .clip(MaterialTheme.shapes.medium)
                .background(
                    if (active) MaterialTheme.colors.primary
                    else Color.Transparent,
                )
        )
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = if (active) MaterialTheme.colors.primary
            else MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            modifier = Modifier
                .size(Dimension.smIcon)
        )
    }
}

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    snackHost: SnackbarHostState,
    content: @Composable (data: SnackbarData) -> Unit = {
        Text(
            text = it.message,
            style = MaterialTheme.typography.body1,
        )
    },
    backgroundColorProvider: () -> Color = { Color.White },
    contentColor: Color = MaterialTheme.colors.onPrimary,
) {
    SnackbarHost(hostState = snackHost, modifier = modifier) { data ->
        Snackbar(
            modifier = Modifier
                .padding(Dimension.pagePadding)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            backgroundColor = backgroundColorProvider(),
            contentColor = contentColor,
            content = {
                content(data)
            }
        )
    }
}