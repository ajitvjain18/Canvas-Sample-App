package com.example.canvassampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.canvassampleapp.ui.theme.CanvasSampleAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CanvasSampleAppTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        DrawingCanvas(
                            paths = state.paths,
                            currentPath = state.currentPathData,
                            onAction = viewModel::actions,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp,Alignment.CenterHorizontally)
                        ) {
                            allColors.fastForEach { c ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(c)
                                        .border(
                                            width = 3.dp,
                                            color = if (state.selectedColor == c) Color.White else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            viewModel.actions(DrawingAction.onSelectedColor(c))
                                        }
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.actions(DrawingAction.onClearCanvas) }
                        ) {
                            Text(text = "Clear Canvas")
                        }
                    }
                }
            }
        }
    }
}
