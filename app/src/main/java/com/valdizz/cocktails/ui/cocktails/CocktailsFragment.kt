package com.valdizz.cocktails.ui.cocktails

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.valdizz.cocktails.R
import com.valdizz.cocktails.adapter.CocktailsRecyclerViewAdapter
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.COCKTAILS_BY_INGREDIENTS_TAG
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.COCKTAILS_TAG
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.FAVORITES_TAG
import com.valdizz.cocktails.ui.cocktaildetail.CocktailDetailFragment
import kotlinx.android.synthetic.main.fragment_cocktails.*
import org.koin.android.viewmodel.ext.android.viewModel

class CocktailsFragment : Fragment() {

    companion object {
        private const val INGREDIENT_NAME_ARGS = "ingredient_name_args"

        fun newInstance(ingredientName: String?) = CocktailsFragment().apply {
            arguments = bundleOf(INGREDIENT_NAME_ARGS to ingredientName)
        }
    }

    val cocktailsViewModel: CocktailsViewModel by viewModel()
    private lateinit var searchView: SearchView
    private var queryString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cocktails, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.cocktails_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        menuItem.setOnActionExpandListener(onActionExpandListener)
        searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(onQueryTextListener)

        if (queryString.isNotEmpty()) {
            menuItem.expandActionView()
            searchView.setQuery(queryString, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CocktailsRecyclerViewAdapter(cocktailClickListener)
        initRecyclerView(adapter)
        initConnectionObserver()

        when (activity?.supportFragmentManager?.findFragmentById(R.id.fragment_container)?.tag) {
            COCKTAILS_TAG -> {
                setHasOptionsMenu(true)
                initCocktailsObserver(adapter)
            }
            FAVORITES_TAG -> {
                setHasOptionsMenu(false)
                initFavoriteObserver(adapter)
            }
            COCKTAILS_BY_INGREDIENTS_TAG -> {
                setHasOptionsMenu(false)
                arguments?.getString(INGREDIENT_NAME_ARGS)?.let {
                    initCocktailsByIngredientsObserver(adapter, it)
                }
            }
        }
    }

    private fun initRecyclerView(adapter: CocktailsRecyclerViewAdapter) {
        rv_cocktails.layoutManager = LinearLayoutManager(activity)
        rv_cocktails.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_cocktails.adapter = adapter
    }

    private fun initConnectionObserver() {
        cocktailsViewModel.isConnected.observe(viewLifecycleOwner, Observer { isConnected ->
            if (!isConnected) {
                Toast.makeText(context, getString(R.string.msg_connection_unavailable), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initCocktailsObserver(adapter: CocktailsRecyclerViewAdapter) {
        cocktailsViewModel.searchCocktails("%a%")
        cocktailsViewModel.cocktails.observe(viewLifecycleOwner, Observer { cocktails ->
            adapter.cocktails = cocktails.data ?: emptyList()
            progress_cocktails.isVisible = cocktails.status == Status.LOADING
        })
    }

    private fun initFavoriteObserver(adapter: CocktailsRecyclerViewAdapter) {
        cocktailsViewModel.favoriteCocktails.observe(viewLifecycleOwner, Observer { cocktails ->
            adapter.cocktails = cocktails.data ?: emptyList()
            progress_cocktails.isVisible = cocktails.status == Status.LOADING
        })
    }

    private fun initCocktailsByIngredientsObserver(adapter: CocktailsRecyclerViewAdapter, ingredientName: String) {
        cocktailsViewModel.searchCocktailsByIngredient(ingredientName)
        cocktailsViewModel.cocktailsByIngredient.observe(viewLifecycleOwner, Observer { cocktails ->
            adapter.cocktails = cocktails.data ?: emptyList()
            progress_cocktails.isVisible = cocktails.status == Status.LOADING
        })
    }

    private val cocktailClickListener: CocktailClickListener = object :
        CocktailClickListener {
        override fun onClick(cocktailId: Int) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, CocktailDetailFragment.newInstance(cocktailId))
                ?.addToBackStack(null)?.commit()
        }
    }

    private val onQueryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                cocktailsViewModel.searchCocktails("%$it%")
                queryString = query
                searchView.clearFocus()
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }

    private val onActionExpandListener: MenuItem.OnActionExpandListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            cocktailsViewModel.searchCocktails("%a%")
            queryString = ""
            return true
        }
    }
}