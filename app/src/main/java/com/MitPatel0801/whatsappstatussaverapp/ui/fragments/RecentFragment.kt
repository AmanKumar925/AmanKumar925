package com.MitPatel0801.whatsappstatussaverapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.MitPatel0801.whatsappstatussaverapp.databinding.FragmentRecentBinding
import com.MitPatel0801.whatsappstatussaverapp.ui.adapters.StatusAdapter
import com.MitPatel0801.whatsappstatussaverapp.ui.viewmodels.StatusViewModel


class RecentFragment : Fragment() {


    private lateinit var binding: FragmentRecentBinding
    private lateinit var viewModel: StatusViewModel
    private lateinit var adapter: StatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[StatusViewModel::class.java]
        binding = FragmentRecentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StatusAdapter(mutableListOf(), requireActivity(), true)
        binding.recycleView.adapter = adapter
        binding.openWhatsapp.setOnClickListener {

            if(viewModel.isWhatsApp)
            {
                startNewActivity(requireContext(),"com.whatsapp")
            }else{
                startNewActivity(requireContext(),"com.whatsapp.w4b")
            }


        }

        viewModel.myData.observe(viewLifecycleOwner) {
            adapter.addList(it)
            if (it.isEmpty()) {
                binding.openWhatsapp.visibility = View.VISIBLE
                binding.emptyMessage.visibility = View.VISIBLE
            } else {
                binding.openWhatsapp.visibility = View.GONE
                binding.emptyMessage.visibility = View.GONE
            }
        }

        // SetOnRefreshListener on SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            rearrangeItems()
        }
    }

    private fun rearrangeItems() {
        viewModel.getMediaFiles()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMediaFiles()
    }

   private fun startNewActivity(context: Context, packageName: String) {
        var intent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=$packageName")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}