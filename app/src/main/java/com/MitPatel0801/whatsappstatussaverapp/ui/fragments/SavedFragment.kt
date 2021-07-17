package com.MitPatel0801.whatsappstatussaverapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import com.MitPatel0801.whatsappstatussaverapp.databinding.FragmentSavedBinding
import com.MitPatel0801.whatsappstatussaverapp.ui.adapters.StatusAdapter
import com.MitPatel0801.whatsappstatussaverapp.ui.viewmodels.SavedViewModel
import com.google.android.material.snackbar.Snackbar


class SavedFragment : Fragment() {


    private lateinit var binding: FragmentSavedBinding
    private lateinit var viewModel: SavedViewModel
    private lateinit var adapter: StatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SavedViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StatusAdapter(mutableListOf(), requireActivity(), false)
        binding.recycleView.adapter = adapter

        viewModel.myData.observe(viewLifecycleOwner) {
            if(it.isEmpty())
            {
                binding.emptyMessage.visibility = View.VISIBLE
            }else{
                binding.emptyMessage.visibility = View.GONE
            }
            adapter.addList(it)
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


    @SuppressLint("ShowToast")
    fun resultCall(isVideo: Boolean) {
        val item = if (isVideo) {
            "Video"
        } else {
            "Photo"
        }
        Snackbar.make(binding.root, "$item has been deleted", Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.guidLine)
            .show()
    }

}