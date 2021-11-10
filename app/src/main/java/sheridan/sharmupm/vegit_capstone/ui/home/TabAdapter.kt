package sheridan.sharmupm.vegit_capstone.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sheridan.sharmupm.vegit_capstone.ui.home.advertisement.AdvertisementFragment
import sheridan.sharmupm.vegit_capstone.ui.home.statistics.StatisticsFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private lateinit var fragment: Fragment

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        if (position == 0) {
            fragment = AdvertisementFragment()
        } else {
            fragment = StatisticsFragment()
        }
        return fragment
    }
}