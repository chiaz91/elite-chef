package ntu.platform.cookery.data.firebase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import ntu.platform.cookery.util.getExtension
import java.util.*

// can be DAO, decide naming later
private const val TAG = "CY.FB.Storage"
object FBStorageRepository {
    private var storage: FirebaseStorage = Firebase.storage

    init {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Fb.Storage init")
//            Firebase.storage.useEmulator("10.0.2.2", 9199)
        }
    }



    private fun uploadImage(storageReference: StorageReference, uri: Uri): MutableLiveData<Uri>{
        val result = MutableLiveData<Uri>()
        Log.d(TAG, "attempt upload image to $storageReference")
        storageReference.putFile(uri)
            .addOnSuccessListener {  taskSnapshot ->
                Log.d(TAG, "Image upload task success")
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { downloadableUri ->
                        Log.d(TAG, "downloadable uri: ${downloadableUri}")
                        result.value = downloadableUri
                    }
            }
            .addOnFailureListener{ exception ->
                Log.d(TAG, "Image upload task was unsuccessful.", exception)

            }
        return result
    }

    fun uploadRecipeGraphic(userId: String, uri: Uri): MutableLiveData<Uri>{
        val ext  = uri.getExtension()
        val fileName = UUID.randomUUID().toString()+"."+ext
        val storageRef = storage.getReference(REF_USERS)
            .child(userId) // replace with user id later
            .child(REF_RECIPES)
            .child(fileName)
        return uploadImage(storageRef, uri)
    }
}