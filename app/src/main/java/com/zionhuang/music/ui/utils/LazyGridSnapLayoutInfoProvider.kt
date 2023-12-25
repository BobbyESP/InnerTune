@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.zionhuang.music.ui.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastSumBy
import kotlin.math.abs
import kotlin.math.sign

@ExperimentalFoundationApi
fun SnapLayoutInfoProvider(
    lazyGridState: LazyGridState,
    positionInLayout: Density.(layoutSize: Float, itemSize: Float) -> Float =
        { layoutSize, itemSize -> (layoutSize / 2f - itemSize / 2f) },
): SnapLayoutInfoProvider = object : SnapLayoutInfoProvider {

    private val layoutInfo: LazyGridLayoutInfo
        get() = lazyGridState.layoutInfo

    // Single page snapping is the default
    override fun Density.calculateApproachOffset(initialVelocity: Float): Float = 0f

    override fun Density.calculateSnappingOffset(currentVelocity: Float): Float {
        var closestItemOffset = Float.MAX_VALUE

        layoutInfo.visibleItemsInfo.fastForEach { item ->
            val offset = calculateDistanceToDesiredSnapPosition(layoutInfo, item, positionInLayout)

            // Check if the item is closer to the center than the current closest item
            if (abs(offset) < abs(closestItemOffset)) {
                closestItemOffset = offset
            }
        }

        // Adjust the offset based on the velocity
        return closestItemOffset + currentVelocity
    }


    override fun Density.calculateSnapStepSize(): Float = with(layoutInfo) {
        if (visibleItemsInfo.isNotEmpty()) {
            visibleItemsInfo.fastSumBy { it.size.width } / visibleItemsInfo.size.toFloat()
        } else {
            0f
        }
    }
}

@ExperimentalFoundationApi
fun SnapLayoutInfoProvider(
    layoutInfo: LazyListLayoutInfo,
    positionInLayout: Density.(layoutSize: Float, itemSize: Float) -> Float =
        { layoutSize, itemSize -> (layoutSize / 2f - itemSize / 2f) },
): SnapLayoutInfoProvider = object : SnapLayoutInfoProvider {

    // Single page snapping is the default
    override fun Density.calculateApproachOffset(initialVelocity: Float): Float = 0f

    override fun Density.calculateSnappingOffset(currentVelocity: Float): Float {
        var closestItemOffset = Float.MAX_VALUE

        layoutInfo.visibleItemsInfo.fastForEach { item ->
            val offset = calculateDistanceToDesiredSnapPosition(layoutInfo, item, positionInLayout)

            // Check if the item is closer to the center than the current closest item
            if (abs(offset) < abs(closestItemOffset)) {
                closestItemOffset = offset
            }
        }

        // Adjust the offset based on the velocity
        return closestItemOffset + currentVelocity
    }

    override fun Density.calculateSnapStepSize(): Float = with(layoutInfo) {
        if (visibleItemsInfo.isNotEmpty()) {
            visibleItemsInfo.fastSumBy { it.size } / visibleItemsInfo.size.toFloat()
        } else {
            0f
        }
    }
}

fun Density.calculateDistanceToDesiredSnapPosition(
    layoutInfo: LazyListLayoutInfo,
    item: LazyListItemInfo,
    positionInLayout: Density.(layoutSize: Float, itemSize: Float) -> Float,
): Float {
    val containerSize =
        with(layoutInfo) { singleAxisViewportSize - beforeContentPadding - afterContentPadding }

    val desiredDistance =
        positionInLayout(containerSize.toFloat(), item.size.toFloat())

    val itemCurrentPosition = item.offset
    return itemCurrentPosition - desiredDistance
}

private val LazyListLayoutInfo.singleAxisViewportSize: Int
    get() = if (orientation == Orientation.Vertical) viewportSize.height else viewportSize.width

fun Density.calculateDistanceToDesiredSnapPosition(
    layoutInfo: LazyGridLayoutInfo,
    item: LazyGridItemInfo,
    positionInLayout: Density.(layoutSize: Float, itemSize: Float) -> Float,
): Float {
    val containerSize =
        with(layoutInfo) { singleAxisViewportSize - beforeContentPadding - afterContentPadding }

    val desiredDistance = positionInLayout(containerSize.toFloat(), item.size.width.toFloat())
    val itemCurrentPosition = item.offset.x.toFloat()

    return itemCurrentPosition - desiredDistance
}

private val LazyGridLayoutInfo.singleAxisViewportSize: Int
    get() = if (orientation == Orientation.Vertical) viewportSize.height else viewportSize.width
