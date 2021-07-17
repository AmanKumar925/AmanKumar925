package com.MitPatel0801.whatsappstatussaverapp.ui.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.MitPatel0801.whatsappstatussaverapp.Data.repositories.StatusRepo
import com.MitPatel0801.whatsappstatussaverapp.Data.db.StatusValue


class StatusViewModel(application: Application) : AndroidViewModel(application) {

    var isWhatsApp:Boolean = true
    private val statusRepo by lazy { StatusRepo(application) }

    val myData: MutableLiveData<List<StatusValue>> = MutableLiveData()

    fun getMediaFiles() {
        val myValue =
            getApplication<Application>().getSharedPreferences("status", MODE_PRIVATE).getString("WhatsApp", "WhatsApp")
        isWhatsApp = myValue == null || myValue == "WhatsApp"
        myData.value = statusRepo.getMediaFiles(myValue)
    }

}