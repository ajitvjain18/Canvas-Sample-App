package com.example.canvassampleapp

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DrawingState(
    val selectedColor : Color = Color.Black,
    val currentPathData: PathData? = null,
    val paths: List<PathData> = emptyList()
)

val allColors = listOf(
    Color.Black,
    Color.Blue,
    Color.Red,
    Color.Green
)

data class PathData(
    val id : String,
    val color: Color,
    val path : List<Offset>
)

sealed interface DrawingAction{
    data object onNewPathStart : DrawingAction
    data class onDraw(val offset: Offset) : DrawingAction
    data object onPathEnd : DrawingAction
    data class onSelectedColor(val color: Color) : DrawingAction
    data object onClearCanvas : DrawingAction
}

class DrawingViewModel : ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()


    fun actions(action: DrawingAction){
        when(action){
            DrawingAction.onClearCanvas -> clearCanvas()
            is DrawingAction.onDraw -> onDrawCanvas(action.offset)
            DrawingAction.onNewPathStart -> onNewPathStarted()
            DrawingAction.onPathEnd -> onPathEnded()
            is DrawingAction.onSelectedColor -> onSelectColor(action.color)
        }
    }

    private fun clearCanvas() {
        _state.update { it.copy(
            currentPathData = null,
            paths = emptyList()
        ) }
    }

    private fun onDrawCanvas(offset: Offset) {
        val currentPath = state.value.currentPathData ?: return
        _state.update { it.copy(
            currentPathData = currentPath.copy(
                path = currentPath.path + offset,
            )
        ) }
    }

    private fun onNewPathStarted() {
        _state.update { it.copy(
            currentPathData = PathData(
                id = System.currentTimeMillis().toString(),
                color = it.selectedColor,
                path = emptyList()
            )
        ) }
    }

    private fun onPathEnded() {
        val currentPath = state.value.currentPathData ?: return
        _state.update { it.copy(
            currentPathData = null,
            paths = it.paths + currentPath
            )
        }
    }

    private fun onSelectColor(color: Color) {
       _state.update { it.copy(selectedColor = color)
       }
    }

}