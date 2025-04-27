package com.example.zequiz.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.zequiz.R
import com.example.zequiz.fragments.HomeGuruFragment
import com.example.zequiz.fragments.BankSoalFragment
import com.example.zequiz.fragments.BuatKuisFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainGuruActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_guru)

        bottomNavigationView = findViewById(R.id.bottom_navigation_guru)

        loadFragment(HomeGuruFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home_guru -> {
                    loadFragment(HomeGuruFragment())
                    true
                }
                R.id.menu_buat_kuis -> {
                    loadFragment(BuatKuisFragment())
                    true
                }
                R.id.menu_bank_soal -> {
                    loadFragment(BankSoalFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_guru, fragment)
            .commit()
    }
}
