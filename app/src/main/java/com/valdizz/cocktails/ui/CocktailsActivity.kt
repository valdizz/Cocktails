package com.valdizz.cocktails.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.valdizz.cocktails.R
import com.valdizz.cocktails.ui.cocktaildetail.CocktailDetailFragment
import com.valdizz.cocktails.ui.cocktails.CocktailsFragment
import com.valdizz.cocktails.ui.ingredients.IngredientsFragment
import com.valdizz.cocktails.ui.network.NetworkConnectionViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * [CocktailsActivity] has a fragment container and [BottomNavigationView].
 *
 * @author Vlad Kornev
 */
class CocktailsActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_COCKTAILS_TAG = "search_cocktails"
        const val FILTER_COCKTAILS_TAG = "filter_cocktails"
        const val INGREDIENTS_TAG = "ingredients"
        const val FAVORITES_TAG = "favorites"
        const val RANDOM_COCKTAIL_TAG = "random_cocktail"
        const val COCKTAILS_BY_INGREDIENTS_TAG = "cocktails_by_ingredients"
    }

    private val networkConnectionViewModel: NetworkConnectionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktails)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val connectionMessage =
            Snackbar.make(navView, getString(R.string.msg_connection_unavailable), Snackbar.LENGTH_LONG)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            navView.selectedItemId = R.id.navigation_cocktails
        }
        initConnectionObserver(connectionMessage)
    }

    private fun initConnectionObserver(connectionMessage: Snackbar) {
        networkConnectionViewModel.getConnection().observe(this, Observer {
            if (it) {
                connectionMessage.dismiss()
            } else {
                connectionMessage.show()
            }
        })
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_cocktails -> {
                loadFragment(CocktailsFragment.newInstance(null), FILTER_COCKTAILS_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ingredients -> {
                loadFragment(IngredientsFragment.newInstance(), INGREDIENTS_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_random -> {
                loadFragment(CocktailDetailFragment.newInstance(0), RANDOM_COCKTAIL_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                loadFragment(CocktailsFragment.newInstance(null), FAVORITES_TAG)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.animator.scalexy_enter, R.animator.scalexy_exit)
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }
}
