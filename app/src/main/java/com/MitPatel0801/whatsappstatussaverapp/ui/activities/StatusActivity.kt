package com.MitPatel0801.whatsappstatussaverapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.ViewModelProvider
import com.MitPatel0801.whatsappstatussaverapp.BuildConfig
import com.MitPatel0801.whatsappstatussaverapp.R
import com.MitPatel0801.whatsappstatussaverapp.databinding.ActivityStatusBinding
import com.MitPatel0801.whatsappstatussaverapp.ui.viewmodels.StatusActivityViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import java.io.File


class StatusActivity : AppCompatActivity() {
    internal var myExternalFile: File?=null
    companion object {
        const val IS_RECENT_FRAGMENT = "recentFragment"
        const val MEDIA_POSITION = "POSITION"
    }

    private lateinit var binding: ActivityStatusBinding
    private lateinit var viewModel: StatusActivityViewModel
    private var isRecentFragment = false


    @SuppressLint("UseCompatLoadingForDrawables", "ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //View Model
        viewModel = ViewModelProvider(this)[StatusActivityViewModel::class.java]

        //Intent values
        val position = intent.getIntExtra(MEDIA_POSITION, -1)
        isRecentFragment = intent.getBooleanExtra(IS_RECENT_FRAGMENT, false)

        //First time Fetch Data
        fetchData(position)

        viewModel.statusValue.observe(this) {
            setMediaPlayer()
        }

        //Icon Set
        setIcon()

        //back button
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Save Button Click listener
        binding.saveButton.setOnClickListener {

            if (viewModel.isSaved) {
                if (!isRecentFragment) {
                    intent.putExtra("ITEM", viewModel.statusValue.value!!.isVideo)
                    intent.putExtra("IS_DELETED", true)

                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    setSaveIcon()
                    val item = if (viewModel.statusValue.value!!.isVideo) {
                        "Video"
                    } else {
                        "Photo"
                    }
                    Snackbar.make(it, "$item has been deleted", Snackbar.LENGTH_SHORT)
                        .setAnchorView(it)
                        .show()
                }
                viewModel.deleteStatus()
            } else {
                val item = if (viewModel.statusValue.value!!.isVideo) {
                    "Video"
                } else {
                    "Photo"
                }
                Snackbar.make(it, "$item has been saved", Snackbar.LENGTH_SHORT)
                    .setAnchorView(it)
                    .show()

                sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(getExternalFilesDir("/status_saver/Movies/"))
                    )
                )
                viewModel.saveStatus()
                setDeleteIcon()
            }
        }

        binding.share.setOnClickListener {
            onShare()
        }
    }


    private fun setIcon() {
        if (isRecentFragment) {
            if (viewModel.isSaved) {
                setDeleteIcon()
            } else {
                setSaveIcon()
            }
        } else {
            setDeleteIcon()
        }
    }

    private fun fetchData(position: Int) {
        if (isRecentFragment) {
            viewModel.getRecentFragment(position)
        } else {
            viewModel.getSavedFragment(position)
        }
    }

    private fun setMediaPlayer() {

        if (viewModel.statusValue.value?.isVideo == true) {
            binding.videoView.visibility = View.VISIBLE
            binding.photoContainer.visibility = View.GONE
            initializePlayer()
        } else {

            binding.videoView.visibility = View.GONE
            binding.photoContainer.visibility = View.VISIBLE
            binding.photoContainer.setImageURI(viewModel.statusValue.value?.fileUri)
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDeleteIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.saveButton.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_baseline_delete_outline_24,
                    this.theme
                )
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.saveButton.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_delete_outline_24,this.theme))
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setSaveIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.saveButton.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_baseline_content_copy_24,
                    this.theme
                )
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.saveButton.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_content_copy_24,theme))
            }
        }
    }

    private fun initializePlayer() {
        binding.videoView.setShowNextButton(false)
        binding.videoView.setShowPreviousButton(false)

        if (viewModel.player == null) {
            viewModel.player = SimpleExoPlayer.Builder(this).build()
        }
        binding.videoView.player = viewModel.player

        val mediaItem = viewModel.statusValue.value?.let { MediaItem.fromUri(it.fileUri) }

        if (mediaItem != null) {
            viewModel.player?.setMediaItem(mediaItem)
        }
        viewModel.player!!.playWhenReady = true
        viewModel.player!!.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
        viewModel.player!!.prepare()
    }

    private fun releasePlayer() {
        viewModel.player?.run {

            viewModel.playbackPosition = currentPosition
            viewModel.currentWindow = currentWindowIndex
            this.playWhenReady = false
            stop()
            release()
            viewModel.player = null
        }
    }

    override fun onStart() {
        super.onStart()
        super.onStart()
        if (viewModel.statusValue.value?.isVideo == true) {
            if (Util.SDK_INT >= 24) {
                initializePlayer()
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        if (viewModel.statusValue.value?.isVideo == true) {
            if (Util.SDK_INT < 24 || viewModel.player == null) {
                initializePlayer()
            }
        }


    }

    override fun onPause() {
        super.onPause()
        if (viewModel.statusValue.value?.isVideo == true) {
            if (Util.SDK_INT < 24) {
                releasePlayer()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.statusValue.value?.isVideo == true) {
            if (Util.SDK_INT >= 24) {
                releasePlayer()

            }
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun onShare() {

        var title = ""
        val newFile = File(viewModel.statusValue.value!!.fileUri.path)

        val contentUri = getUriForFile(this, BuildConfig.APPLICATION_ID , newFile)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            if (viewModel.statusValue.value!!.isVideo) {
                type = "video/mp4"
                title = "Share video"
            } else {
                type = "image/*"
                title = "Share image"
            }

        }

        startActivity(Intent.createChooser(shareIntent, title))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }


}