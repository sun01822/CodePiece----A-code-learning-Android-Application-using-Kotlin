package com.example.codepiece

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.codepiece.adapter.CustomSpinnerAdapter
import com.example.codepiece.helper.ApiHelper
import com.google.android.material.navigation.NavigationView
import com.example.codepiece.models.CodeCompilerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var codeEditText: EditText
    private lateinit var inputEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var codePieceTextView: TextView
    private lateinit var spinner: Spinner
    private lateinit var selectedLanguage: String
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        codeEditText = findViewById(R.id.codeEditText)
        inputEditText = findViewById(R.id.inputEditText)
        submitButton = findViewById(R.id.submitButton)
        codePieceTextView = findViewById(R.id.codePieceTextView)
        spinner = findViewById(R.id.languageSpinner) // Get reference to the Spinner
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val languages = arrayOf("c", "cpp", "python3", "java")

        // Initialize Spinner adapter
        val customAdapter = CustomSpinnerAdapter(this, languages)
        customAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = customAdapter

        selectedLanguage = "c" // Set the default value for selectedLanguage

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLanguage = languages[position] // Update selectedLanguage when an item is selected
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected (optional)
                selectedLanguage = "c" // Set selectedLanguage to default "C"
            }
        }

        submitButton.setOnClickListener {
            val rawCode = codeEditText.text.toString()
            val input = inputEditText.text.toString()
            val originalCode = rawCode.trimIndent()
            ApiHelper.compileCode(selectedLanguage, originalCode, input, object : Callback<CodeCompilerResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<CodeCompilerResponse>, response: Response<CodeCompilerResponse>) {
                    if (response.isSuccessful) {
                        val compilerResponse = response.body()
                        compilerResponse?.let {
                            codePieceTextView.text = "Output: ${it.output}\nCPU Time: ${it.cpuTime}\nMemory: ${it.memory}\n${it.language}\n"
                        }
                    } else {
                        codePieceTextView.text = "Error: ${response.message()}"
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<CodeCompilerResponse>, t: Throwable) {
                    codePieceTextView.text = "Error: ${t.message}"
                }
            })
        }


        // Set up the ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        // Set the navigation drawer item click listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation drawer item clicks here
            when (menuItem.itemId) {
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
}
