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
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.toolbar_layout.*


open class NavigationDrawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var t: ActionBarDrawerToggle
    private lateinit var frameLayout: FrameLayout
    private lateinit var session: Session
    private lateinit var read: DatabaseReference
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
        if (session.getPhotoUrl() != "") {
            Picasso.with(this)
                .load(session.getPhotoUrl())
                .into(navHeader.findViewById<ImageView>(R.id.userImage))
        }


        t = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        setSupportActionBar(toolbar)


//        t.setHomeAsUpIndicator(R.drawable.key)
        drawerLayout.addDrawerListener(t)
        actionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBar?.setHomeButtonEnabled(true)
        t.syncState()
        navView.setNavigationItemSelectedListener(this)
        checkNotification()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_rankings -> Toast.makeText(
                this,
                "Rankings part in progress",
                Toast.LENGTH_SHORT
            ).show()
            R.id.nav_fCompleted -> openCompleted()
            R.id.nav_fPoints -> Toast.makeText(this, "In progress", Toast.LENGTH_SHORT).show()
            R.id.nav_logout -> logoutdialog()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openCompleted() {
        drawerLayout.closeDrawer(GravityCompat.START)
        frameLayout.removeAllViews()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, CompletedFavoursFragment())
            .addToBackStack("FragCompleted").commit()
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

    fun openNotification(view: View) {
        val m = HashMap<String, Boolean>()
        m["read"] = true
        read.ref.updateChildren(m as Map<String, Any>)
        frameLayout.removeAllViews()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, FragmentNotification())
            .addToBackStack("fragNotification").commit()
    }

    fun gotoHome(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun logoutdialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout ?")
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                session.setLoginState(false)
                session.logout()
            }
            .setNegativeButton(
                "No", null
            )
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
        } else if (supportFragmentManager.backStackEntryCount > 0 && supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name == "FragEditProfile"
        ) {
            super.onBackPressed()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
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

    private fun checkNotification() {
        read = FirebaseDatabase.getInstance().reference.child(session.NOTIFICATIONS)
            .child(FirebaseAuth.getInstance().uid.toString())
        read.child("read").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val b = snapshot.getValue(Boolean::class.java)
                if (b != null) {
                    if (!b) red_dot_notification.visibility = View.VISIBLE
                    else red_dot_notification.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}

