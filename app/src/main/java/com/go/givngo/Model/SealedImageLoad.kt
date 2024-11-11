package com.go.givngo.Model


sealed class ImageLoadState {
    object Success : ImageLoadState()
    object Error : ImageLoadState()
    object Loading : ImageLoadState()
}
