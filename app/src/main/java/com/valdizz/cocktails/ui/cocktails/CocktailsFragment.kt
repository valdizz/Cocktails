package com.valdizz.cocktails.ui.cocktails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.valdizz.cocktails.R
import com.valdizz.cocktails.adapter.CocktailsRecyclerViewAdapter
import com.valdizz.cocktails.common.Constants
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.COCKTAILS_BY_INGREDIENTS_TAG
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.FAVORITES_TAG
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.FILTER_COCKTAILS_TAG
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.SEARCH_COCKTAILS_TAG
import com.valdizz.cocktails.ui.cocktaildetail.CocktailDetailFragment
import kotlinx.android.synthetic.main.fragment_cocktails.*
import org.koin.android.viewmodel.ext.android.viewModel

class CocktailsFragment : Fragment() {

    companion object {
        private const val INGREDIENT_NAME_ARGS = "ingredient_name_args"
        private const val FILTER_DIALOG_REQUEST_CODE = 1

        fun newInstance(ingredientName: String?) = CocktailsFragment().apply {
            arguments = bundleOf(INGREDIENT_NAME_ARGS to ingredientName)
        }
    }

    val cocktailsViewModel: CocktailsViewModel by viewModel()
    private lateinit var searchView: SearchView
    private var queryString = ""
    private var filterType = 0
    private val types = arrayOf(Constants.TYPE_ALCOHOLIC, Constants.TYPE_NON_ALCOHOLIC, Constants.TYPE_OPTIONAL_ALCOHOL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cocktails, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.cocktails_menu, menu)
        val menuItemSearch = menu.findItem(R.id.action_search)
        val menuItemFilter = menu.findItem(R.id.action_filter)
        menuItemSearch.setOnActionExpandListener(onSearchActionExpandListener)
        menuItemFilter.setOnMenuItemClickListener(onFilterClickListener)
        searchView = menuItemSearch.actionView as SearchView
        searchView.setOnQueryTextListener(onSearchQueryTextListener)

        if (queryString.isNotEmpty()) {
            menuItemSearch.expandActionView()
            searchView.setQuery(queryString, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CocktailsRecyclerViewAdapter(cocktailClickListener)
        initRecyclerView(adapter)
        initCocktailsObserver(adapter)
        initConnectionObserver()

        when (activity?.supportFragmentManager?.findFragmentById(R.id.fragment_container)?.tag) {
            FILTER_COCKTAILS_TAG -> {
                setHasOptionsMenu(true)
                cocktailsViewModel.loadCocktails(Pair(FILTER_COCKTAILS_TAG, types[filterType]))
            }
            FAVORITES_TAG -> {
                setHasOptionsMenu(false)
                cocktailsViewModel.loadCocktails(Pair(FAVORITES_TAG, ""))
            }
            COCKTAILS_BY_INGREDIENTS_TAG -> {
                setHasOptionsMenu(false)
                arguments?.getString(INGREDIENT_NAME_ARGS)?.let {
                    cocktailsViewModel.loadCocktails(Pair(COCKTAILS_BY_INGREDIENTS_TAG, it))
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
        cocktailsViewModel.cocktails.observe(viewLifecycleOwner, Observer { cocktails ->
            adapter.cocktails = cocktails.data ?: emptyList()
            progress_cocktails.isVisible = cocktails.status == Status.LOADING
        })
    }

    private val cocktailClickListener: CocktailClickListener = object :
        CocktailClickListener {
        override fun onClick(cocktailId: Int) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.animator.scalexy_enter, R.animator.scalexy_exit)
                ?.replace(R.id.fragment_container, CocktailDetailFragment.newInstance(cocktailId))
                ?.addToBackStack(null)?.commit()
        }
    }

    private val onSearchQueryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                cocktailsViewModel.loadCocktails(Pair(SEARCH_COCKTAILS_TAG, "%$it%"))
                queryString = query
                searchView.clearFocus()
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }

    private val onSearchActionExpandListener: MenuItem.OnActionExpandListener =
        object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                cocktailsViewModel.loadCocktails(Pair(FILTER_COCKTAILS_TAG, types[filterType]))
                queryString = ""
                return true
            }
        }

    private val onFilterClickListener: MenuItem.OnMenuItemClickListener = MenuItem.OnMenuItemClickListener {
        val dialog = TypeDialogFragment.newInstance(filterType)
        dialog.setTargetFragment(this@CocktailsFragment, 1)
        dialog.show(fragmentManager, this@CocktailsFragment.javaClass.name)
        true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FILTER_DIALOG_REQUEST_CODE) {
            data?.let {
                filterType = data.getIntExtra(TypeDialogFragment.SELECTED_TYPE_TAG, 0)
                cocktailsViewModel.loadCocktails(Pair(FILTER_COCKTAILS_TAG, types[filterType]))
            }
        }
    }
}