package com.MitPatel0801.whatsappstatussaverapp.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.MitPatel0801.whatsappstatussaverapp.Data.db.StatusValue
import com.MitPatel0801.whatsappstatussaverapp.Data.repositories.SavedRepo
import com.MitPatel0801.whatsappstatussaverapp.Data.repositories.StatusRepo
import com.google.android.exoplayer2.SimpleExoPlayer


class StatusActivityViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    var currentWindow: Int = 0
    var playbackPosition: Long = 0
    var player: SimpleExoPlayer? = null

    val statusValue: MutableLiveData<StatusValue> = MutableLiveData()

    private val statusRepo by lazy { StatusRepo(application) }
    private val savedRepo by lazy { SavedRepo() }
    var isSaved = false

    fun getRecentFragment(position: Int) {

        val myValue =
            context.getSharedPreferences("status", Context.MODE_PRIVATE)
                .getString("WhatsApp", "WhatsApp")

        val status = statusRepo.getMediaFiles(myValue)[position]
        statusValue.value = status

        savedRepo.getAllStatus().forEach {
            if (it.fileName == status.fileName) {
                isSaved = true
            }
        }

    }

    fun getSavedFragment(position: Int) {
        statusValue.value = savedRepo.getAllStatus()[position]
        isSaved = true
    }

    fun deleteStatus() {
        isSaved = !isSaved
        savedRepo.delete(statusValue.value!!)
    }

    fun saveStatus() {
        isSaved = !isSaved
        savedRepo.save(statusValue.value!!)
    }


}