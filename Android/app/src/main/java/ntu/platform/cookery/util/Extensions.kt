package ntu.platform.cookery.util


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ntu.platform.cookery.R
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*


fun Fragment.setToolBar(toolbar: Toolbar){
    if (activity is AppCompatActivity){
        (activity as AppCompatActivity).apply{
            setSupportActionBar(toolbar)
//            NavigationUI.setupActionBarWithNavController(this, findNavController())
            val appBarConfiguration = AppBarConfiguration(setOf(R.id.newsfeed_list,R.id.recipe_list, R.id.profile))

            NavigationUI.setupActionBarWithNavController(this, findNavController(), appBarConfiguration)
        }
    }
}

fun Fragment.setTitle(title: String) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}

fun Fragment.setTitle(@StringRes stringId: Int) {
    setTitle(requireContext().getString(stringId))
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


fun  <T : Any> List<T>.log(tag:String? = "Cy.Debug"){
    Log.d(tag, "List.size = $size")
    forEach{ingredient ->
        Log.d(tag, ingredient.toString())
    }
}


fun String.hashMD5(): String{
    try {
        val md = MessageDigest.getInstance("MD5")
        md.update( this.encodeToByteArray())
        val messageDigest = md.digest()
        val intBytes = BigInteger(1, messageDigest)
        return intBytes.toString(16)

    } catch (e: Exception ){
        e.printStackTrace()
    }
    return ""
}

fun isSameDate(date1:Long, date2: Long): Boolean{
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date(date1)) == sdf.format(Date(date2))
}