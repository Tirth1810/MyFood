package com.example.myapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapp.Fragments.First
import com.example.myapp.Fragments.Second
import com.example.myapp.Fragments.Third

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                First()
            }
            1 -> {
                Second()
            }
            2 -> {
                Third()
            }
            else -> {
                Fragment()
            }
        }
    }
}