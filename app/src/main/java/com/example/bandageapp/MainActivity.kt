package com.example.bandageapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.bandageapp.databinding.ActivityMainBinding
import com.example.bandageapp.fragment.AboutFragment
import com.example.bandageapp.fragment.BlogFragment
import com.example.bandageapp.fragment.ContactFragment
import com.example.bandageapp.fragment.DetailProductFragment
import com.example.bandageapp.fragment.HomeFragment
import com.example.bandageapp.fragment.LoginRegisterFragment
import com.example.bandageapp.fragment.PagesFragment
import com.example.bandageapp.fragment.ShopFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupDrawerLayout()
        setupNavigationView()

        // Change toolbar title to bold
        binding.toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleTextStyle)

        // Load default fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            binding.navView.setCheckedItem(R.id.nav_home)
        }

    }

    // Setup DrawerLayout
    private fun setupDrawerLayout() {
        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    // Setup NavigationView
    private fun setupNavigationView() {
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment = when (item.itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_shop -> ShopFragment()
            R.id.nav_about -> AboutFragment()
            R.id.nav_blog -> BlogFragment()
            R.id.nav_contact -> ContactFragment()
            R.id.nav_pages -> PagesFragment()
            R.id.nav_login_register -> LoginRegisterFragment()
            else -> HomeFragment()
        }

        replaceFragment(fragment)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search..."

        // Add listener to SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Check current fragment
                val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
                if (currentFragment is HomeFragment) {
                    // Call filterProducts function in HomeFragment
                    currentFragment.filterProducts(newText.orEmpty())
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_shop -> {
                Toast.makeText(this, "Shop clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_favorite -> {
                Toast.makeText(this, "Favorite clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
