package com.journiapp.stampapplication.presentation.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.imageLoader
import com.journiapp.stampapplication.R
import com.journiapp.stampapplication.model.Stamp
import com.journiapp.stampapplication.presentation.viewmodel.StampViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StampListView(
    stampViewModel: StampViewModel = viewModel(),
    openSearchScreen: () -> Unit,
    onRemoveStamp: (Stamp) -> Unit
) {

    val stampScreenState by stampViewModel.stampScreenState.collectAsStateWithLifecycle()
    val stamps = stampScreenState.stamps
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stamps") },
                actions = {
                    Button(
                        onClick = { openSearchScreen() },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorPrimary)),
                    ) {
                        Text("ADD STAMP")
                    }
                }
            )
        }
    ) { innerPadding ->
        val minSize = 150.dp

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            items(stamps.size) { index ->
                StampItem(stamps[index], onRemoveStamp = {
                    onRemoveStamp(stamps[index])
                }, onStampLongPress = {
                    showDialog = true
                })
            }
        }
    }
}

@Composable
fun StampItem(stamp: Stamp, onStampLongPress: () -> Unit, onRemoveStamp: (Stamp) -> Unit) {

    val imageLoader = LocalContext.current.imageLoader
    val BASE_API_URL = "https://www.journiapp.com/picture/"

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ConfirmRemovalDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                onRemoveStamp(stamp)
                showDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .aspectRatio(1f)
            .rotate(
                Random
                    .nextInt(-60, 60)
                    .toFloat()
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onStampLongPress()
                        showDialog = true
                    }
                )
            },
    ) {
        Image(
            painter = rememberImagePainter(
                data = BASE_API_URL + stamp.pictureGuid + "_stamp.png",
                imageLoader = imageLoader,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = "Stamp Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ConfirmRemovalDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Remove Stamp") },
        text = { Text(text = "Are you sure you want to remove this stamp?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Remove")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}