package com.example.codepiece

import android.os.Bundle
import android.view.MenuItem
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
import com.example.codepiece.fragments.ProblemsFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView

        val moduleItems = listOf(
            ModuleItem("Problems", R.drawable.problems),
            ModuleItem("Courses", R.drawable.courses),
            ModuleItem("Books", R.drawable.books),
            ModuleItem("Compile Code", R.drawable.compiler),
            ModuleItem("Join Lectures", R.drawable.lectures),
            ModuleItem("Join Contest", R.drawable.contest),
            ModuleItem("Test Yourself", R.drawable.test),
            ModuleItem("Blogs", R.drawable.blog),
            ModuleItem("DSA", R.drawable.dsa),
            // Add more items as needed
        )

        val moduleAdapter = ModuleAdapter(this, moduleItems)
        moduleAdapter.setOnclickListener(object : ModuleAdapter.OnClickListener {
            override fun onClick(position: Int, moduleItem: ModuleItem) {
                // Handle item click
                val fragment: Fragment = when (moduleItem.name) {
                    "Problems" -> ProblemsFragment()
                    "Courses" -> CoursesFragment()
                    else -> {
                        // Handle other cases or set to a default fragment if needed
                        ProblemsFragment() // Replace with a default fragment or handle other cases
                    }
                }
                navigateToFragment(fragment)
            }
        })

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = moduleAdapter

        // Set up the ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        val fragment = ProblemsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        // Set the navigation drawer item click listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation drawer item clicks here
            when (menuItem.itemId) {
                R.id.compiler -> {
                    val compilerFragment = CompilerFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, compilerFragment)
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null) // Add to back stack for back navigation
            .commit()
    }
}
