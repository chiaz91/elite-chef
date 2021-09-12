package ntu.platform.cookery.data.firebase

import android.util.Log
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import androidx.lifecycle.LiveData
import com.google.firebase.database.*


// Ref https://firebase.googleblog.com/2017/12/using-android-architecture-components.html
private const val TAG = "FirebaseQueryLiveData"
class FBQueryLiveData(private val query: Query) : LiveData<DataSnapshot?>() {
    private val listener: MyValueEventListener = MyValueEventListener()

    override fun onActive() {
        Log.d(TAG, "onActive")
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        Log.d(TAG, "onInactive")
        query.removeEventListener(listener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(TAG, "Can't listen to query $query", databaseError.toException())
        }
    }

}