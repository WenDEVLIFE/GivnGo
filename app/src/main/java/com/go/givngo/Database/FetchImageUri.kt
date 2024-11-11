package com.go.givngo.Database

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException

// This fetch function now handles a single URI for the thumbnail
fun fetchImageUri(
    storage: FirebaseStorage,
    imagePath: String,
    context: Context,
    onUriFetched: (Uri?) -> Unit
) {
    val imageRef = storage.reference.child(imagePath)
    imageRef.downloadUrl
        .addOnSuccessListener { uri ->
            onUriFetched(uri)
        }
        .addOnFailureListener { exception ->
            if ((exception as? StorageException)?.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                Log.d("Storage", "Image at path $imagePath not found.")
                onUriFetched(null)
            } else {
                Log.e("Storage", "Error retrieving image URI", exception)
                onUriFetched(null)
            }
        }
}
