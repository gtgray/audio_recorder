package tk.atna.audiorecorder.presentation.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import tk.atna.audiorecorder.R
import tk.atna.audiorecorder.databinding.MainFragmentBinding
import tk.atna.audiorecorder.presentation.main.adapter.RecordsAdapter
import tk.atna.audiorecorder.stuff.openApplicationSettings
import tk.atna.audiorecorder.stuff.playExternal

class MainFragment : Fragment() {

    private val binding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModel()

    private val adapter by lazy {
        RecordsAdapter(requireContext(), viewModel::handlePlaying, viewModel::forward)
    }

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecords.adapter = adapter
        binding.btnRecord.setOnClickListener { viewModel.handleRecording() }

        viewModel.records.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            applyState(it)
        }

        viewModel.playInternal.observe(viewLifecycleOwner) {
            playInternal(it)
        }

        viewModel.playExternal.observe(viewLifecycleOwner) {
            playExternal(it)
        }
    }

    override fun onStart() {
        super.onStart()
        checkPermissionsGranted()
    }

    override fun onPause() {
        super.onPause()
        viewModel.handleStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (REQUEST_RECORD_AUDIO_PERMISSIONS == requestCode) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                checkPermissionsGranted()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Snackbar.make(
                        requireView(),
                        R.string.base_record_permissions_needed,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.base_request) { requestPermissions() }
                        .show()
                } else {
                    Snackbar.make(
                        requireView(),
                        R.string.base_record_permissions_needed,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.base_settings) { requireContext().openApplicationSettings() }
                        .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FORWARD_AUDIO) {
            viewModel.forwardFinished()
        }
    }

    private fun applyState(newState: MainViewModel.State) {
        when (newState) {
            MainViewModel.State.DISABLED -> {
                binding.btnRecord.setText(R.string.base_disabled_record)
                binding.btnRecord.isEnabled = false
            }
            MainViewModel.State.IDLE -> {
                binding.btnRecord.setText(R.string.base_start_record)
                binding.btnRecord.isEnabled = true
                if (mediaPlayer?.isPlaying == true) mediaPlayer?.stop()
            }
            MainViewModel.State.RECORDING -> {
                binding.btnRecord.setText(R.string.base_stop_record)
                binding.btnRecord.isEnabled = true
            }
            MainViewModel.State.PLAYING -> {
                binding.btnRecord.isEnabled = false
            }
        }
    }

    private fun playInternal(uri: Uri) {
        releasePlayer()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(requireContext(), uri)
            setOnCompletionListener { viewModel.handlePlaying() }
            prepare()
            start()
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    private fun playExternal(uri: Uri) {
        playExternal(uri, REQUEST_FORWARD_AUDIO)
    }

    private fun checkPermissionsGranted() {
        val granted = isPermissionsGranted()
        viewModel.enableRecording(granted)
        if (!granted) requestPermissions()
    }

    private fun isPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSIONS)
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSIONS = 123
        private const val REQUEST_FORWARD_AUDIO = 321

        fun newInstance() = MainFragment()
    }

}