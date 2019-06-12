package com.valdizz.cocktails.ui.ingredientdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.valdizz.cocktails.R
import com.valdizz.cocktails.common.Constants
import com.valdizz.cocktails.model.entity.Ingredient
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.CocktailsActivity.Companion.COCKTAILS_BY_INGREDIENTS_TAG
import com.valdizz.cocktails.ui.cocktails.CocktailsFragment
import kotlinx.android.synthetic.main.fragment_ingredient_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * [IngredientDetailFragment] displays detailed information about the ingredient.
 *
 * @author Vlad Kornev
 */
class IngredientDetailFragment : Fragment() {

    companion object {
        private const val INGREDIENT_ARGS = "ingredient_details"

        fun newInstance(ingredientName: String) = IngredientDetailFragment().apply {
            arguments = bundleOf(INGREDIENT_ARGS to ingredientName)
        }
    }

    private val ingredientDetailViewModel: IngredientDetailViewModel by viewModel()
    private val drawableCrossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ingredient_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(INGREDIENT_ARGS)?.let {
            initIngredientObserver(it)
        }
    }

    private fun initIngredientObserver(name: String) {
        ingredientDetailViewModel.loadIngredient(name)
        ingredientDetailViewModel.ingredient.observe(viewLifecycleOwner, Observer { ingredient ->
            progress_detail_ingredient.isVisible = ingredient.status == Status.LOADING
            ingredient.data?.let {
                val selectedIngredient = it[0]
                showIngredient(selectedIngredient)
                btn_show_cocktails.setOnClickListener { loadCocktailsByIngredient(selectedIngredient.ingredient) }
            }
        })
    }

    private fun showIngredient(ingredient: Ingredient) {
        Glide
            .with(view?.context!!)
            .load(Constants.INGREDIENT_IMAGE_URL + ingredient.ingredient + Constants.INGREDIENT_IMAGE_EXT)
            .placeholder(R.drawable.ic_ingredient)
            .transition(withCrossFade(drawableCrossFadeFactory))
            .into(iv_ingredient_detail)
        tv_ingredient_name.text = ingredient.ingredient
        tv_ingredient_type.text = getString(R.string.type,
            if (ingredient.type.isNullOrEmpty()) { getString(R.string.undefined) } else { ingredient.type })
        tv_ingredient_description.text = if (ingredient.description.isNullOrEmpty()) {
            getString(R.string.no_description)
        } else {
            ingredient.description
        }
        btn_show_cocktails.text = getString(R.string.cocktails_with, ingredient.ingredient)
    }

    private fun loadCocktailsByIngredient(ingredientName: String) {
        val navView: BottomNavigationView? = activity?.findViewById(R.id.nav_view)
        navView?.selectedItemId = R.id.navigation_cocktails

        activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.animator.scalexy_enter, R.animator.scalexy_exit)
            ?.replace(R.id.fragment_container, CocktailsFragment.newInstance(ingredientName),
                COCKTAILS_BY_INGREDIENTS_TAG)?.commit()
    }
}