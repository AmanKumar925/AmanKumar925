package com.MitPatel0801.whatsappstatussaverapp.Data.repositories

import android.net.Uri
import android.os.Environment
import com.MitPatel0801.whatsappstatussaverapp.Data.db.StatusValue
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class SavedRepo {

    private val root = Environment.getExternalStorageDirectory().toString()
    private val myDir = File("$root/status_saver/${Environment.DIRECTORY_MOVIES}")

    //Save to the Dir
    fun save(statusValue: StatusValue) {

        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        val file = File(myDir, statusValue.fileName)
        copyFile(File(statusValue.fileUri.path), file)
    }


    fun getAllStatus(): List<StatusValue> {

        val inFiles = mutableSetOf<StatusValue>()

        val files = myDir.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.name.endsWith(".jpg") ||
                    file.name.endsWith(".gif")
                ) {
                    inFiles.add(StatusValue(Uri.fromFile(file), file.name))

                } else if (file.name.endsWith(".mp4")) {
                    inFiles.add(StatusValue(Uri.fromFile(file), file.name,true))
                }
            }
        }
        return inFiles.toList();
    }

    //Delete asked files
    fun delete(statusValue: StatusValue) {
        getAllStatus().forEach {
            if (it.fileName == statusValue.fileName) {
                File(it.fileUri.path).delete()
            }
        }
    }

    //util function
    private fun copyFile(src: File, dst: File) {
        val inStream = FileInputStream(src)
        val outStream = FileOutputStream(dst)
        val inChannel = inStream.channel
        val outChannel = outStream.channel

        inChannel.transferTo(0, inChannel.size(), outChannel)

        inStream.close()
        outStream.close()
    }

}

