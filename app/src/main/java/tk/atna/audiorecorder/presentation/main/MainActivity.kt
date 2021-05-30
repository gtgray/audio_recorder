package tk.atna.audiorecorder.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tk.atna.audiorecorder.R
import tk.atna.audiorecorder.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}