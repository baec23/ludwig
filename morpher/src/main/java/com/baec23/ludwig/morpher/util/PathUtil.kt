package com.baec23.ludwig.morpher.util

import android.graphics.PointF
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.vector.PathNode
import androidx.core.graphics.plus
import androidx.core.graphics.times
import com.baec23.ludwig.morpher.model.path.LudwigSubpath
import com.baec23.ludwig.morpher.model.path.PathSegment
import kotlin.math.abs

internal fun List<androidx.graphics.path.PathSegment>.toSubpaths(
    bounds: RectF,
): List<LudwigSubpath> {
    return this.toSubpaths(bounds, bounds.width(), bounds.height())
}

internal fun List<androidx.graphics.path.PathSegment>.toSubpaths(
    bounds: RectF,
    targetWidth: Float,
    targetHeight: Float,
): List<LudwigSubpath> {

    val scaleFactorX = targetWidth / bounds.width()
    val scaleFactorY = targetHeight / bounds.height()
    val scaleFactor = minOf(scaleFactorX, scaleFactorY)
    val scaledWidth = scaleFactor * bounds.width()
    val scaledHeight = scaleFactor * bounds.height()
    val offsetX = ((targetWidth - scaledWidth) / 2) - bounds.left * scaleFactor
    val offsetY = ((targetHeight - scaledHeight) / 2) - bounds.top * scaleFactor
    val offset = PointF(offsetX, offsetY)

    val toReturn = mutableListOf<LudwigSubpath>()
    val currSubpath = mutableListOf<PathSegment>()

    var currPosition = PointF(0f, 0f)
    var area = 0f

    this.forEach { androidSegment ->
        when (androidSegment.type) {
            androidx.graphics.path.PathSegment.Type.Move -> {
                val end = androidSegment.points[0] * scaleFactor + offset
                if (currSubpath.isNotEmpty()) {
                    if (currSubpath.first() != currSubpath.last() &&
                        currSubpath.first().startPosition.x.approxEquals(currSubpath.last().endPosition.x) &&
                        currSubpath.first().startPosition.y.approxEquals(currSubpath.last().endPosition.y)
                    ) {
                        val trapezoidArea = (end.x - currPosition.x) * (currPosition.y + end.y) / 2
                        area += trapezoidArea
                        toReturn.add(
                            LudwigSubpath(
                                pathSegments = currSubpath.toList(),
                                bounds = Rect(0f, 0f, targetWidth, targetHeight),
                                isClosed = true,
                                area = area
                            )
                        )
                    } else {
                        toReturn.add(
                            LudwigSubpath(
                                pathSegments = currSubpath.toList(),
                                bounds = Rect(0f, 0f, targetWidth, targetHeight),
                                isClosed = false
                            )
                        )
                    }
                    currSubpath.clear()
                    area = 0f
                }
                currPosition = end
            }

            androidx.graphics.path.PathSegment.Type.Line -> {
                val start = androidSegment.points[0] * scaleFactor + offset
                val end = androidSegment.points[1] * scaleFactor + offset
                val cp1 = lerp(start, end, 1 / 3f)
                val cp2 = lerp(start, end, 2 / 3f)

                currSubpath.add(
                    PathSegment(
                        startPosition = Offset(start.x, start.y),
                        endPosition = Offset(end.x, end.y),
                        pathNode = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, end.x, end.y)
                    )
                )
                val trapezoidArea = (end.x - start.x) * (start.y + end.y) / 2
                area += trapezoidArea
                currPosition = end
            }

            androidx.graphics.path.PathSegment.Type.Quadratic -> {
                val start = androidSegment.points[0] * scaleFactor + offset
                val inCp = androidSegment.points[1] * scaleFactor + offset
                val end = androidSegment.points[2] * scaleFactor + offset

                val cp1 = Offset(
                    start.x + 2.0f / 3.0f * (inCp.x - start.x),
                    start.y + 2.0f / 3.0f * (inCp.y - start.y)
                )
                val cp2 = Offset(
                    end.x + 2.0f / 3.0f * (inCp.x - end.x), end.y + 2.0f / 3.0f * (inCp.y - end.y)
                )

                currSubpath.add(
                    PathSegment(
                        startPosition = Offset(start.x, start.y),
                        endPosition = Offset(end.x, end.y),
                        pathNode = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, end.x, end.y)
                    )
                )
                val trapezoidArea = (end.x - start.x) * (start.y + end.y) / 2
                area += trapezoidArea
                currPosition = end
            }

            androidx.graphics.path.PathSegment.Type.Conic -> {/* no-op */
            }

            androidx.graphics.path.PathSegment.Type.Cubic -> {
                val start = androidSegment.points[0] * scaleFactor + offset
                val cp1 = androidSegment.points[1] * scaleFactor + offset
                val cp2 = androidSegment.points[2] * scaleFactor + offset
                val end = androidSegment.points[3] * scaleFactor + offset

                currSubpath.add(
                    PathSegment(
                        startPosition = Offset(start.x, start.y),
                        endPosition = Offset(end.x, end.y),
                        pathNode = PathNode.CurveTo(cp1.x, cp1.y, cp2.x, cp2.y, end.x, end.y)
                    )
                )
                val trapezoidArea = (end.x - start.x) * (start.y + end.y) / 2
                area += trapezoidArea
                currPosition = end
            }

            androidx.graphics.path.PathSegment.Type.Close -> {
                if (currSubpath.isNotEmpty()) {
                    val end = PointF(
                        currSubpath.first().startPosition.x, currSubpath.first().startPosition.y
                    )
                    val trapezoidArea = (end.x - currPosition.x) * (currPosition.y + end.y) / 2
                    area += trapezoidArea
                    toReturn.add(
                        LudwigSubpath(
                            pathSegments = currSubpath.toList(),
                            bounds = Rect(0f, 0f, targetWidth, targetHeight),
                            isClosed = true,
                            area = area
                        )
                    )
                    currSubpath.clear()
                    area = 0f
                    currPosition = end
                }
            }

            androidx.graphics.path.PathSegment.Type.Done -> {
                return@forEach
            }
        }
    }
    if (currSubpath.isNotEmpty()) {
        val end = currSubpath.first().startPosition
        if (currPosition.x.approxEquals(end.x) &&
            currPosition.y.approxEquals(end.y)) {
            val trapezoidArea = (end.x - currPosition.x) * (currPosition.y + end.y) / 2
            area += trapezoidArea
            toReturn.add(
                LudwigSubpath(
                    pathSegments = currSubpath.toList(),
                    bounds = Rect(0f, 0f, targetWidth, targetHeight),
                    isClosed = true,
                    area = area
                )
            )
        } else {
            toReturn.add(
                LudwigSubpath(
                    pathSegments = currSubpath.toList(),
                    bounds = Rect(0f, 0f, targetWidth, targetHeight),
                    isClosed = false
                )
            )
        }
    }
    return toReturn.toList()
}

private fun Float.approxEquals(other: Float, threshold: Float = 0.0001f): Boolean {
    return abs(this - other) <= threshold
}