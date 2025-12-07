package com.example.canvassampleapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.util.fastForEach

@Composable
fun DrawingCanvas(
    paths: List<PathData>,
    currentPath: PathData?,
    onAction: (DrawingAction) -> Unit,
    modifier: Modifier
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onAction(DrawingAction.onNewPathStart) },
                    onDragEnd = { onAction(DrawingAction.onPathEnd) },
                    onDragCancel = { onAction(DrawingAction.onPathEnd) },
                    onDrag = { change, _ ->
                        onAction(DrawingAction.onDraw(change.position))
                    }
                )
            }
    ) {

        fun buildPath(points: List<Offset>): Path {
            return Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach {
                        lineTo(it.x, it.y)
                    }
                }
            }
        }

        paths.fastForEach {
            drawPath(
                path = buildPath(it.path),
                color = it.color,
                style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        currentPath?.let {
            drawPath(
                path = buildPath(it.path),
                color = it.color,
                style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

