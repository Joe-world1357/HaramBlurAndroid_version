package com.haramblur.app.ui.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

/**
 * Duration for animations
 */
object AnimationDuration {
    const val SHORT = 150
    const val MEDIUM = 300
    const val LONG = 500
}

/**
 * Composable with animated transitions for navigation
 */
fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(
                    durationMillis = AnimationDuration.MEDIUM,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(AnimationDuration.MEDIUM))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(
                    durationMillis = AnimationDuration.MEDIUM,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(AnimationDuration.MEDIUM))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = AnimationDuration.MEDIUM,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(AnimationDuration.MEDIUM))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(
                    durationMillis = AnimationDuration.MEDIUM,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(AnimationDuration.MEDIUM))
        },
        content = content
    )
}

/**
 * Navigate with animation
 */
fun NavHostController.navigateWithAnimation(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Enter transition for elements
 */
fun enterTransition(delay: Int = 0): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = AnimationDuration.MEDIUM,
            delayMillis = delay
        )
    ) + scaleIn(
        animationSpec = tween(
            durationMillis = AnimationDuration.MEDIUM,
            delayMillis = delay
        ),
        initialScale = 0.95f
    )
}

/**
 * Exit transition for elements
 */
fun exitTransition(delay: Int = 0): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = AnimationDuration.SHORT,
            delayMillis = delay
        )
    ) + scaleOut(
        animationSpec = tween(
            durationMillis = AnimationDuration.SHORT,
            delayMillis = delay
        ),
        targetScale = 0.95f
    )
}

/**
 * Spring-based slide in animation
 */
fun springSlideIn(initialOffsetX: Int): EnterTransition {
    return slideInHorizontally(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = IntOffset.VisibilityThreshold
        ),
        initialOffsetX = { initialOffsetX }
    ) + fadeIn(
        animationSpec = tween(AnimationDuration.MEDIUM)
    )
}

/**
 * Spring-based slide out animation
 */
fun springSlideOut(targetOffsetX: Int): ExitTransition {
    return slideOutHorizontally(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
            visibilityThreshold = IntOffset.VisibilityThreshold
        ),
        targetOffsetX = { targetOffsetX }
    ) + fadeOut(
        animationSpec = tween(AnimationDuration.SHORT)
    )
}
