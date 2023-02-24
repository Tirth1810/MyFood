package com.example.myapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapp.Fragments.IntroductionFirstFragment
import com.example.myapp.Fragments.IntroductionSecondFragment
import com.example.myapp.Fragments.IntroductionThirdFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                IntroductionFirstFragment()
            }
            1 -> {
                IntroductionSecondFragment()
            }
            2 -> {
                IntroductionThirdFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}