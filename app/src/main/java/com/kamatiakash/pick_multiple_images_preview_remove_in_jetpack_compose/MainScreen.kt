package com.kamatiakash.pick_multiple_images_preview_remove_in_jetpack_compose

import android.Manifest
import android.net.Uri
import android.view.WindowInsets
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {

    val state = viewModel.state
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) {
            viewModel.updateSelectedImageList(listOfImages = it)
        }

    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)

    SideEffect {
        permissionState.launchPermissionRequest()
    }

    Column(modifier = Modifier.padding(20.dp).fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.5f)
        ) {
            if (state.listOfSelectedImages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Please choose images")

                }
            }

            if (state.listOfSelectedImages.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    itemsIndexed(state.listOfSelectedImages) { index, uri ->
                        ImagePreviewItem(uri = uri,
                            height = screenHeight * 0.5f,
                            width = screenWidth * 0.6f,
                            onClick = { viewModel.onItemRemove(index) }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }
        }

        Button(onClick = {
            if (permissionState.status.isGranted){
                galleryLauncher.launch("image/*")
            }
            else
                permissionState.launchPermissionRequest()
        }) {
            Text(text = "Choose images")
        }
    }
}


