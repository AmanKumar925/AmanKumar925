package com.MitPatel0801.whatsappstatussaverapp

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.MitPatel0801.whatsappstatussaverapp.databinding.ActivityMainBinding
import com.MitPatel0801.whatsappstatussaverapp.ui.fragments.RecentFragment
import com.MitPatel0801.whatsappstatussaverapp.ui.fragments.SavedFragment
import com.MitPatel0801.whatsappstatussaverapp.ui.fragments.SettingFragment
import com.MitPatel0801.whatsappstatussaverapp.ui.adapters.ViewpagerAdapter
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {


    lateinit var recentFragment: RecentFragment

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                requestPermission()
            }
        }

    private lateinit var binding: ActivityMainBinding
    private lateinit var savedFragment: SavedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showTutorial()
        setSupportActionBar(binding.toolBar)

        if (!isPermissionGranted()) {
            val permissions = arrayOf(WRITE_EXTERNAL_STORAGE)
            for (i in permissions.indices) {
                requestPermissionandroid(permissions[i], i)
            }
        }

        savedFragment = SavedFragment()
        recentFragment = RecentFragment()
        val fragments = listOf(recentFragment, savedFragment, SettingFragment())
        binding.viewPager.adapter = ViewpagerAdapter(supportFragmentManager, fragments, this)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }
    private fun isPermissionGranted(): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        return permissionCheck == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionandroid(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
    private fun showTutorial() {
        val myValue = getSharedPreferences("status", MODE_PRIVATE).getBoolean("isFirstTime", false)
        if (!myValue) {
            firstDialogue()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission()
            }
        }

        val editor = getSharedPreferences("status", MODE_PRIVATE).edit()
        with(editor)
        {
            putBoolean("isFirstTime", true)
            apply()
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        when {
            (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) -> {

            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                Snackbar
                    .make(
                        this,
                        binding.viewPager,
                        "You have to allow this permission for saving Status.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    .setAction("OK") {
                        requestPermissionLauncher.launch(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val isDeleteFromSavedFragment = data!!.getBooleanExtra("IS_DELETED", false)
                Log.d("AAA", "onActivityResult:$isDeleteFromSavedFragment ")
                if (isDeleteFromSavedFragment) {
                    Log.d("AAA", "onActivityResult:${data.getBooleanExtra("ITEM", false)} ")
                    savedFragment.resultCall(data.getBooleanExtra("ITEM", true))
                }
            }
        }
    }


    private fun firstDialogue() {

        val view = layoutInflater.inflate(R.layout.first_dialog_box, null)
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .setView(view)
            .setCancelable(false)
            .setPositiveButton("Next") { _, _ ->
                secondDialogue()
            }
            .setNegativeButton("Skip") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission()
                }
                return@setNegativeButton
            }
            .create()
            .show()
    }


    private fun secondDialogue() {
        val view = layoutInflater.inflate(R.layout.second_dialog_box, null)
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .setView(view)
            .setCancelable(false)
            .setPositiveButton("Next") { _, _ ->
                thirdDialogue()
            }
            .setNegativeButton("Skip") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission()
                }
                return@setNegativeButton
            }.setNeutralButton("Back") { _, _ ->
                firstDialogue()
            }
            .create()
            .show()
    }

    @SuppressLint("ResourceAsColor")
    private fun thirdDialogue() {
        val view = layoutInflater.inflate(R.layout.third_dialog_box, null)
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .setView(view)
            .setCancelable(false)
            .setPositiveButton("Finish") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission()
                }
            }
            .setNeutralButton("Back") { _, _ ->
                secondDialogue()
            }
            .create()
            .show()
    }
}
