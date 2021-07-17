package com.MitPatel0801.whatsappstatussaverapp.ui.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.MitPatel0801.whatsappstatussaverapp.Data.db.StatusValue
import com.MitPatel0801.whatsappstatussaverapp.databinding.StatusItemBinding
import com.MitPatel0801.whatsappstatussaverapp.ui.activities.StatusActivity


class StatusAdapter(private val list: MutableList<StatusValue>, val context: Activity,val isRecentFragment: Boolean) :
    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val statusItemBinding =
            StatusItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(statusItemBinding)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val binding: StatusItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                creatingIntent(adapterPosition)
                it.isClickable = false
            }
        }

        fun bind(statusValue: StatusValue) {
            binding.root.isClickable= true
            if (statusValue.isVideo) {
                binding.videImage.visibility = View.VISIBLE
            }else{
                binding.videImage.visibility = View.GONE
            }
            Glide.with(context)
                .load(statusValue.fileUri)
                .override(900, 700)
                .into(binding.image)

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    private fun creatingIntent(position: Int) {
        val intent = Intent(context, StatusActivity::class.java)
        intent.putExtra(StatusActivity.MEDIA_POSITION, position)
        intent.putExtra(StatusActivity.IS_RECENT_FRAGMENT, isRecentFragment)
       context.startActivityForResult(intent,1)
    }

    fun addList(newList: List<StatusValue>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}
