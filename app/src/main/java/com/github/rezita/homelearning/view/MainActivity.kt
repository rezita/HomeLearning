package com.github.rezita.homelearning.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.databinding.ActivityMainBinding
import com.github.rezita.homelearning.view.fragments.AdminTabFragment
import com.github.rezita.homelearning.view.fragments.ErikTabFragment
import com.github.rezita.homelearning.view.fragments.MarkTabFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var userName: String? = null


    var adapter: MainTabAdapter? = null

    val labels = arrayOf(
        R.string.main_activity_tab1,
        R.string.main_activity_tab2,
        R.string.main_activity_tab3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewPager = binding.pager
        val tabLayout = binding.tabLayout

        val adapter = MainTabAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(labels[position])
        }.attach()
        //removeUserName()
        setUserName()
    }

    /*
        private fun removeUserName(){
            val prefs = this.getSharedPreferences("", Context.MODE_PRIVATE)
            prefs.edit().remove(R.string.preferences_user_name.toString()).apply()
            prefs.edit().remove("SecretCode").apply()
        }
    */
    private fun setUserName() {
        userName = readSavedUser()
        if (userName == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readSavedUser(): String? {
        val prefs = this.getSharedPreferences("", Context.MODE_PRIVATE)
        return prefs.getString(R.string.preferences_user_name.toString(), null)
    }

    inner class MainTabAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragment, lifecycle) {

        override fun getItemCount(): Int = labels.size

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ErikTabFragment()
                1 -> MarkTabFragment()
                else -> AdminTabFragment()
            }
        }
    }
}