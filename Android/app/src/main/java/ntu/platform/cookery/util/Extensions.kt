package ntu.platform.cookery.util


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ntu.platform.cookery.R
import java.io.File


fun Fragment.setToolBar(toolbar: Toolbar){
    if (activity is AppCompatActivity){
        (activity as AppCompatActivity).apply{
            setSupportActionBar(toolbar)
            NavigationUI.setupActionBarWithNavController(this, findNavController())
        }
    }
}

fun Fragment.setTitle(title: String) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}

fun Fragment.setDisplayHomeAsUp(enabled: Boolean) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    }
}

fun ImageView.loadWithGlide(uri: Uri){
    Glide.with(context)
        .load(uri)
        .apply(RequestOptions()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image))
        .into(this)
}
fun ImageView.loadWithGlide(uri: String){
    Glide.with(context)
        .load(uri)
        .apply(RequestOptions()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image))
        .into(this)
}



//fun <T> MutableLiveData<T>.notifyObserver() {
//    this.value = this.value
//}


fun Uri.getExtension(context: Context? = null): String {
    val name = lastPathSegment!!

    //Check uri format to avoid null
    return if (context != null && scheme.equals(ContentResolver.SCHEME_CONTENT) ){
        val mime = MimeTypeMap.getSingleton()
        val type = context.contentResolver.getType(this)
        mime.getExtensionFromMimeType(type).toString()
    }
    else if (scheme.equals(ContentResolver.SCHEME_FILE)){
        val file = Uri.fromFile( File(this.path))
        MimeTypeMap.getFileExtensionFromUrl(file.toString())
    } else {
        name.substring(name.lastIndexOf(".")+1)
    }
}