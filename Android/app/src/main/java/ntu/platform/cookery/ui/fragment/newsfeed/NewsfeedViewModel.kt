package ntu.platform.cookery.ui.fragment.newsfeed

import android.util.Log
import androidx.lifecycle.ViewModel
import ntu.platform.cookery.base.BaseClickedListener
import ntu.platform.cookery.base.SingleLiveEvent
import ntu.platform.cookery.data.firebase.FBDatabaseRepository
import ntu.platform.cookery.ui.adapter.FBPostsAdapter

private const val TAG = "Cy.news.vm"
class NewsfeedViewModel : ViewModel() {
    val options = FBDatabaseRepository.getAllPostOptions()
    val itemClick = SingleLiveEvent<String>()
    val adapter = FBPostsAdapter(options).also {
        it.clickedListener = BaseClickedListener{action, viewHolder ->
            Log.d(TAG, "$action, click on item ${viewHolder.bindingAdapterPosition}")
            val pos = viewHolder.bindingAdapterPosition

            Log.d(TAG, "item id: ${it.getItemId(pos)}")
            Log.d(TAG, "item key: ${it.getRef(pos).key}")

            itemClick.value = it.getRef(pos).key
        }
    }
}