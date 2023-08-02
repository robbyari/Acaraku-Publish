package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class TicketShape(
    private val circleRadius: Dp,
    private val cornerSize: CornerSize
) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(path = getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val roundedRect = RoundRect(size.toRect(), CornerRadius(cornerSize.toPx(size, density)))
        val roundedRectPath = Path().apply { addRoundRect(roundedRect) }
        return Path.combine(operation = PathOperation.Intersect, path1 = roundedRectPath, path2 = getTicketPath(size, density))
    }

    private fun getTicketPath(size: Size, density: Density): Path {
        val middleY = size.height.div(other = 2) + 200
        val circleRadiusInPx = with(density) { circleRadius.toPx() }
        return Path().apply {
            reset()
            lineTo(x = 0F, y = 0F)
            lineTo(x = size.width, y = 0F)
            arcTo(
                rect = Rect(
                    left = size.width - circleRadiusInPx,
                    top = middleY.minus(circleRadiusInPx),
                    right = size.width + circleRadiusInPx,
                    bottom = middleY.plus(circleRadiusInPx)
                ),
                startAngleDegrees = 270F,
                sweepAngleDegrees = -180F,
                forceMoveTo = false
            )
            lineTo(x = size.width, y = size.height)
            lineTo(x = 0F, y = size.height)
            arcTo(
                rect = Rect(
                    left = 0F.minus(circleRadiusInPx),
                    top = middleY.minus(circleRadiusInPx),
                    right = 0F.plus(circleRadiusInPx),
                    bottom = middleY.plus(circleRadiusInPx)
                ),
                startAngleDegrees = 90F,
                sweepAngleDegrees = -180F,
                forceMoveTo = false
            )
            lineTo(x = 0F, y = 0F)
        }
    }
}