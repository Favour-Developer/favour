package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


open class NavigationDrawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var t: ActionBarDrawerToggle
    private lateinit var frameLayout: FrameLayout
    private lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_navigation_drawer)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        frameLayout = findViewById(R.id.framelayout)
        val toolbar: Toolbar = findViewById(R.id.toolBar_main)
        session = Session(applicationContext)
        val navigation = findViewById<NavigationView>(R.id.nav_view)
        val navHeader: View = navigation.getHeaderView(0)
        navHeader.findViewById<TextView>(R.id.userName).text = session.getUsername()
        navHeader.findViewById<TextView>(R.id.userNumber).text = session.getMobile()


        t = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

//        t.setHomeAsUpIndicator(R.drawable.key)
        drawerLayout.addDrawerListener(t)
        t.syncState()
        navView.setNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_rankings -> Toast.makeText(
                this,
                "Rankings part in progress",
                Toast.LENGTH_SHORT
            ).show()
            R.id.nav_fCompleted -> Toast.makeText(this, "In progress", Toast.LENGTH_SHORT).show()
            R.id.nav_fPoints -> Toast.makeText(this, "In progress", Toast.LENGTH_SHORT).show()
            R.id.nav_logout -> logoutdialog()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun openCloseNavigationDrawer(view: View) {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun openAccount(view: View) {
        drawerLayout.closeDrawer(GravityCompat.START)
        frameLayout.removeAllViews()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, AccountFragment())
            .addToBackStack("FragAccount").commit()
    }

    fun contactUs(view: View) {
        frameLayout.removeAllViews()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, HelpFragment())
            .addToBackStack("fragHelp").commit()
    }


    private fun logoutdialog() {
        val alert: Any = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
               session.setLoginState(false)
//                Log.i("Session",Gson().toJson(session.toString()).toString())
                startActivity(Intent(this, Login::class.java))
            }
            .setNegativeButton(
                "No"
            ) { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }

    override fun setContentView(layoutResID: Int) {
        if (frameLayout != null) {
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val stubView: View = inflater.inflate(layoutResID, frameLayout, false)
            frameLayout.addView(stubView, lp)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if(supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name == "FragEditProfile"){
            super.onBackPressed()
        }
        else if (supportFragmentManager.backStackEntryCount > 0) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
    }


    override fun setContentView(view: View?) {
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.addView(view, lp)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        frameLayout.addView(view, params)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        t.syncState()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (t.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

}

