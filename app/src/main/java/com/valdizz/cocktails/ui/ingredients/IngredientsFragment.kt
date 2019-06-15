package com.valdizz.cocktails.ui.ingredients

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.valdizz.cocktails.R
import com.valdizz.cocktails.adapter.IngredientsRecyclerViewAdapter
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.ingredientdetail.IngredientDetailFragment
import kotlinx.android.synthetic.main.fragment_ingredients.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * [IngredientsFragment] displays a list of ingredients.
 *
 * @author Vlad Kornev
 */
class IngredientsFragment : Fragment() {

    companion object {
        fun newInstance() = IngredientsFragment()
    }

    val ingredientsViewModel: IngredientsViewModel by viewModel()
    private lateinit var searchView: SearchView
    private var queryString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.ingredients_menu, menu)
        val menuItem = menu.findItem(R.id.action_search_ingredient)
        menuItem.setOnActionExpandListener(onActionExpandListener)
        searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(onQueryTextListener)

        if (queryString.isNotEmpty()) {
            menuItem.expandActionView()
            searchView.setQuery(queryString, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = IngredientsRecyclerViewAdapter(ingredientClickListener)
        initRecyclerView(adapter)
        initObserver(adapter)
    }

    private fun initRecyclerView(adapter: IngredientsRecyclerViewAdapter) {
        rv_ingredients.layoutManager = LinearLayoutManager(activity)
        rv_ingredients.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rv_ingredients.adapter = adapter
    }

    private fun initObserver(adapter: IngredientsRecyclerViewAdapter) {
        ingredientsViewModel.searchIngredient(null)
        ingredientsViewModel.ingredients.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.LOADING) {
                progress_ingredients.visibility = ProgressBar.VISIBLE
            } else {
                progress_ingredients.visibility = ProgressBar.GONE
                adapter.ingredients = it.data ?: emptyList()
                if (it.status == Status.ERROR) {
                    Toast.makeText(context, getString(R.string.msg_network_request_failed), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private val ingredientClickListener: IngredientClickListener = object :
        IngredientClickListener {
        override fun onClick(ingredientName: String) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.animator.scalexy_enter, R.animator.scalexy_exit)
                ?.replace(R.id.fragment_container, IngredientDetailFragment.newInstance(ingredientName))
                ?.addToBackStack(null)?.commit()
        }
    }

    private val onQueryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                ingredientsViewModel.searchIngredient("%$it%")
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
            ingredientsViewModel.searchIngredient(null)
            queryString = ""
            return true
        }
    }
}