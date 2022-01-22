package com.bcebhagalpur.pdfconverter

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menuIcon: ImageView
    private lateinit var contentView: LinearLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var homeFragment: HomeFragment
    private lateinit var filesFragment: FilesFragment
    private lateinit var starredFragment: StarredFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)
//        changeColor()


        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        menuIcon = findViewById(R.id.menu_icon)
        contentView = findViewById(R.id.content)
        navigationDrawer()
        drawerHeaderItemHandle()

        bottomNavigationView=findViewById(R.id.bottomNavigationView)
//        homeFragment()
        bottom()
//        homeFragment()

    }

    @Suppress("DEPRECATION")
    private fun bottom(){
        bottomNavigationView.setOnNavigationItemSelectedListener {

//            if (previousMenuItem != null){
//                previousMenuItem?.isChecked = false
//            }
//
//            it.isCheckable = true
//            it.isChecked = true
//            previousMenuItem = it

            when(it.itemId){
                R.id.home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        homeFragment
                    )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    supportActionBar?.hide()
                }
                R.id.files -> {
                    filesFragment = FilesFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        filesFragment

                    )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    supportActionBar?.hide()
                }

                R.id.starred -> {
                    starredFragment = StarredFragment()
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        starredFragment
                    )
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    supportActionBar?.hide()

                }
            }
            true
        }
    }
//    @SuppressLint("ResourceAsColor")
//    private fun homeFragment(){
//        homeFragment= HomeFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayout, homeFragment)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
//        supportActionBar?.show()
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//
//        if (id!= android.R.id.home){
//            homeFragment()
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    @SuppressLint("RtlHardcoded")
    private fun navigationDrawer() {
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        menuIcon.setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) drawerLayout.closeDrawer(
                GravityCompat.START
            ) else drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    @SuppressLint("RtlHardcoded")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)

        }else {
                    val a = Intent(Intent.ACTION_MAIN)
                    a.addCategory(Intent.CATEGORY_HOME)
                    a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(a)
                }
            }


    private fun drawerHeaderItemHandle(){
//        val headerView=navigationView.getHeaderView(0)
//        val rl= headerView.findViewById<RelativeLayout>(R.id.rl_)

    }


//    private fun changeColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.black)
//        }
//
//    }
}
