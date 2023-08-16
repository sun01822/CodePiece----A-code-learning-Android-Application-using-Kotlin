package com.example.codepiece

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.codepiece.adapter.ModuleAdapter
import com.example.codepiece.data.ModuleItem
import com.example.codepiece.databinding.ActivityMainBinding
import com.example.codepiece.fragments.CompilerFragment
import com.example.codepiece.fragments.CoursesFragment
import com.example.codepiece.fragments.HomeFragment
import com.example.codepiece.fragments.ProblemsFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var shareContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // sharing link
        shareContent = "https://play.google.com/store/apps/details?id=com.example.codepiece"

        // Initialize UI components
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView

        // Set up the ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        // Set the navigation drawer item click listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation drawer item clicks here
            when (menuItem.itemId) {
                R.id.home -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, homeFragment)
                        .commit()
                }
                R.id.compiler -> {
                    val compilerFragment = CompilerFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, compilerFragment)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.moreApps -> {
                    // Handle item 1 click
                    // Example: startActivity(Intent(this, Item1Activity::class.java))
                }
                R.id.rateUs -> {
                    // Handle item 2 click
                    // Example: startActivity(Intent(this, Item2Activity::class.java))

                }
                R.id.share ->{
                    shareLinkToSocialMedia(shareContent)
                }
                // Add more cases for other menu items
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Find the current fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 0) {
                // If there are fragments in the back stack, pop the back stack
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareLinkToSocialMedia(shareContent: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareContent)

        // Create a chooser to allow the user to pick from available apps
        val chooserIntent = Intent.createChooser(intent, "Share Link via")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        } else {
            // No apps on the device can handle the sharing intent.
        }
    }
}
