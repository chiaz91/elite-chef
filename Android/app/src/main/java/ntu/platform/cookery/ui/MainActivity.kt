package ntu.platform.cookery.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ntu.platform.cookery.R
import ntu.platform.cookery.data.firebase.FBAuthRepository
import ntu.platform.cookery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity)
        binding.navView.setupWithNavController(navController)

        FBAuthRepository.userLiveData.observe(this, {
            if (it == null){
                toLauncherActivity()
            }
        })

    }

    private fun toLauncherActivity(){
        val intent = Intent(this, LauncherActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity)
                navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}