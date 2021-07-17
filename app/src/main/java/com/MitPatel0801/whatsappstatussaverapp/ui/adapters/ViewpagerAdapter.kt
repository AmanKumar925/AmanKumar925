package com.MitPatel0801.whatsappstatussaverapp.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewpagerAdapter(
    fm: FragmentManager,
    private val list: List<Fragment>,
    private val context: Context
) : FragmentPagerAdapter(fm) {

    private val titles = listOf("STATUS", "SAVED","SETTING")

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Fragment = list[position]

    override fun getPageTitle(position: Int): CharSequence{
        return titles[position]
    }


}