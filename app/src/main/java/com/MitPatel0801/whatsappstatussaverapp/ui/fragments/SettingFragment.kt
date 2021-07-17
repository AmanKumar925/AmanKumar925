package com.MitPatel0801.whatsappstatussaverapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.MitPatel0801.whatsappstatussaverapp.R
import com.MitPatel0801.whatsappstatussaverapp.databinding.FragmentSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.Error


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myValue =
            requireContext().getSharedPreferences("status", MODE_PRIVATE)
                .getString("WhatsApp", "WhatsApp")

        if (myValue == null || myValue == "WhatsApp") {
            binding.toggleGroup.check(binding.whatsApp.id)
        } else {
            binding.toggleGroup.check(binding.whatsappBusiness.id)
        }


        binding.about.setOnClickListener {
            bottomSheetCreate(getString(R.string.about), "Disclaimer")
        }

        binding.privacyPolicy.setOnClickListener {
            bottomSheetCreate(getString(R.string.policy), "Privacy Policies")
        }

        binding.contactUs.setOnClickListener {
            val mIntent = Intent(Intent.ACTION_SENDTO)
            mIntent.data = Uri.parse("mailto:contact.us.statussaver@gmail.com")
            mIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Status Saver App")
            startActivity(mIntent)
        }

        binding.rateApp.setOnClickListener {
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            try {
                startActivity(intent)
            }catch (e:Error){
                Toast.makeText(requireContext(),"Sorry Something went wrong. Can you try again please",Toast.LENGTH_SHORT).show()
        }
        }

        binding.whatsApp.setOnClickListener {
            val editor = requireContext().getSharedPreferences("status", MODE_PRIVATE).edit()
            with(editor)
            {
                putString("WhatsApp", "WhatsApp")
                apply()
            }
            Toast.makeText(requireContext(), "Changed to WhatsApp", Toast.LENGTH_SHORT).show()
        }


        binding.whatsappBusiness.setOnClickListener {
            Toast.makeText(requireContext(), "Changed to WhatsApp Business", Toast.LENGTH_SHORT)
                .show()
            val editor = requireContext().getSharedPreferences("status", MODE_PRIVATE).edit()
            with(editor)
            {
                putString("WhatsApp", "WhatsApp Business")
                apply()
            }
        }

    }


    private fun bottomSheetCreate(text: String, title: String) {

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(R.layout.bottomsheet_layout)
        bottomSheetDialog.findViewById<TextView>(R.id.paragraph)?.text = text
        bottomSheetDialog.findViewById<TextView>(R.id.title)?.text = title
        bottomSheetDialog.show()

    }


}


