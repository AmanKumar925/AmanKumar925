package com.MitPatel0801.whatsappstatussaverapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.MitPatel0801.whatsappstatussaverapp.Data.db.StatusValue
import com.MitPatel0801.whatsappstatussaverapp.Data.repositories.SavedRepo

class SavedViewModel:ViewModel() {

    private val savedRepo = SavedRepo()

    val myData: MutableLiveData<List<StatusValue>> = MutableLiveData()

    fun getMediaFiles() {
        myData.value = savedRepo.getAllStatus()
    }
}