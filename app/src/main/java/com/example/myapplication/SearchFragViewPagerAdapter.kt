package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.Main.Fragment.BoardFragment.BoardFragment

private const val NUM_PAGES = 2

class SearchFragViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BoardFragment()
            1 -> Tab1()
            else -> Tab2()



        }
    }
}