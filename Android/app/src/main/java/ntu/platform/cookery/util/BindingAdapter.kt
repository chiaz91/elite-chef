package ntu.platform.cookery.util

import android.net.Uri
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ntu.platform.cookery.R
import ntu.platform.cookery.base.BaseRecyclerViewAdapter
import ntu.platform.cookery.base.MutableListLiveData
import java.text.SimpleDateFormat

private const val TAG = "cz.BindAdapter"

object BindingAdapter{

//    @JvmStatic
//    @BindingAdapter("linearAdapter")
//    fun <T : Any> setAdapter(recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter<T>) {
//        Log.d(TAG, "linearAdapter")
//        val layoutManager = LinearLayoutManager(recyclerView.context)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = layoutManager
//    }

    @JvmStatic
    @BindingAdapter( "listLiveData")
    fun <T : Any> setItemList(recyclerView: RecyclerView, items: MutableListLiveData<T>?) {
        Log.d(TAG, "liveData")
        items?.value?.let { itemList ->
            (recyclerView.adapter as? BaseRecyclerViewAdapter<T>)?.apply {
                this.items = itemList
                this.notifyDataSetChanged()
            }
        }
    }

    @JvmStatic
    @BindingAdapter( "loadWithGlide")
    fun <T : Any> loadImage(imageView: ImageView, uri: String?) {
        Log.d(TAG, "loadWithGlide")
        uri?.let {
            imageView.loadWithGlide(uri)
        }
    }

//    @JvmStatic
//    @BindingAdapter( "autoCompleteEntries")
//    fun setAutoCompleteAdapter(autoCompleteTextView: AutoCompleteTextView, @ArrayRes arrStringRes: Int) {
//        val context = autoCompleteTextView.context
//        val arr = context.resources.getStringArray(arrStringRes)
//        val arrAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, arr)
//        autoCompleteTextView.setAdapter(arrAdapter)
//    }

    @JvmStatic
    @BindingAdapter("dropDownItems", "dropDownItemLayout", "dropDownItemsIncludeEmpty", requireAll = false)
    fun AutoCompleteTextView.setItems(items: Array<String>?, @LayoutRes layout: Int?, includeEmpty: Boolean?) =
        setAdapter(
            ArrayAdapter(
                context,
                layout ?: android.R.layout.simple_dropdown_item_1line,
                (if (includeEmpty == true) arrayOf("") else emptyArray()) + (items ?: emptyArray())
            )
        )

    @JvmStatic
    @BindingAdapter( "timeAgo")
    fun TextView.relativeTimestamp( time: Long){
        text = DateUtils.getRelativeTimeSpanString(time)
    }


    @JvmStatic
    @BindingAdapter("timestamp", "dateFormat", requireAll = false)
    fun TextView.formatDatetime(timestamp:Long, dateFormat: String?) {
        val sdf = SimpleDateFormat(dateFormat ?: "yyyy-MM-dd HH:mm:ss.SSS")
        this.text = sdf.format(timestamp)
    }


}
