package com.example.repetitiontest.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.repetitiontest.fragments.intro.IntroPageFragment

class IntroPageAdapter(
    private val fragment: Fragment,
    private val pageList: ArrayList<String>
)
    :
    FragmentStateAdapter(
        fragment
    ) {
    override fun getItemCount(): Int = pageList.size

    override fun createFragment(position: Int): Fragment {
        return IntroPageFragment.newInstance(pageList[position])
    }

}