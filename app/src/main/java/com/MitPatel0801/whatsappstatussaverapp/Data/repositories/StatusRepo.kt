package com.MitPatel0801.whatsappstatussaverapp.Data.repositories

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.MitPatel0801.whatsappstatussaverapp.Data.db.StatusValue
import java.io.File

class StatusRepo(private var mcontext:Context) {


    private var WHATSAPP_LOCATION = "/WhatsApp/Media/.Statuses"
    private var WHATSAPP_LOCATION_UPDATE = "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"

    var parentDirPath = ""

    fun getMediaFiles(string: String?): List<StatusValue> {

        if (string == null || string == "WhatsApp") {
            WHATSAPP_LOCATION = "/WhatsApp/Media/.Statuses"
            WHATSAPP_LOCATION_UPDATE = "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
        } else {
            WHATSAPP_LOCATION = "/WhatsApp Business/Media/.Statuses"
            WHATSAPP_LOCATION_UPDATE = "/WhatsApp Business/Media/.Statuses"
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            parentDirPath = (Environment.getExternalStorageDirectory().toString() + WHATSAPP_LOCATION_UPDATE)
        }
        else{
            parentDirPath =  (Environment.getExternalStorageDirectory().toString() + WHATSAPP_LOCATION)

        }


        val inFiles = mutableSetOf<StatusValue>()
        val files = File(parentDirPath).listFiles()

        if (files != null) {
            for (file in files) {
                if (file.name.endsWith(".jpg") ||
                    file.name.endsWith(".gif")
                ) {
                    inFiles.add(StatusValue(Uri.fromFile(file), file.name))
                } else if (file.name.endsWith(".mp4")) {
                    inFiles.add(StatusValue(Uri.fromFile(file), file.name, true))
                }
            }
        }
        return inFiles.toList()
    }

}