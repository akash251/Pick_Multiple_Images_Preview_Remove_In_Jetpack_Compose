package com.kamatiakash.pick_multiple_images_preview_remove_in_jetpack_compose

import android.net.Uri

data class MainScreenState(
    val listOfSelectedImages:List<Uri> = emptyList()
)
