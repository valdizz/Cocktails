package com.valdizz.cocktails.ui.cocktaildetail

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.valdizz.cocktails.R
import com.valdizz.cocktails.common.Constants
import com.valdizz.cocktails.common.toPx
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.CocktailsActivity
import kotlinx.android.synthetic.main.fragment_cocktail_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class CocktailDetailFragment : Fragment() {

    companion object {
        private const val COCKTAIL_ARGS = "cocktail_details"
        private const val COCKTAIL_ID = "cocktail_id"

        fun newInstance(cocktailId: Int) = CocktailDetailFragment().apply {
            arguments = bundleOf(COCKTAIL_ARGS to cocktailId)
        }
    }

    private val cocktailDetailViewModel: CocktailDetailViewModel by viewModel()
    private var cocktailId: Int = 0
    private val drawableCrossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            cocktailId = savedInstanceState.getInt(COCKTAIL_ID, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cocktail_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getInt(COCKTAIL_ARGS)?.let {
            if (activity?.supportFragmentManager?.findFragmentById(R.id.fragment_container)?.tag !=
                CocktailsActivity.RANDOM_COCKTAIL_TAG) {
                cocktailId = it
            }
            initCocktailObserver(cocktailId)
        }

        iv_cocktail_favorite.setOnClickListener {
            cocktailDetailViewModel.setFavoriteCocktail(cocktailId)
            Toast.makeText(
                context,
                if (iv_cocktail_favorite.tag == true) {
                    getString(R.string.msg_removed_to_favorites)
                } else {
                    getString(R.string.msg_added_to_favorites)
                }, Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COCKTAIL_ID, cocktailId)
    }

    private fun initCocktailObserver(id: Int) {
        cocktailDetailViewModel.loadCocktail(id)
        cocktailDetailViewModel.cocktail.observe(viewLifecycleOwner, Observer { cocktail ->
            progress_detail_cocktail.isVisible = cocktail.status == Status.LOADING
            cocktail.data?.let {
                showCocktail(it)
            }
        })
    }

    private fun showCocktail(cocktail: Cocktail) {
        cocktailId = cocktail.id
        Glide
            .with(this)
            .load(cocktail.image)
            .placeholder(R.drawable.ic_cocktail)
            .transition(withCrossFade(drawableCrossFadeFactory))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
            .into(iv_cocktail_detail)
        tv_cocktail_name.text = cocktail.drink
        tv_cocktail_type.text = getString(R.string.type, cocktail.type)
        tv_cocktail_category.text = getString(R.string.category, cocktail.category)
        tv_cocktail_glass.text = getString(R.string.glass, cocktail.glass)
        tv_cocktail_recipe.text = cocktail.instructions
        iv_cocktail_favorite.apply {
            if (cocktail.isFavorite) {
                tag = true
                setImageResource(R.drawable.ic_star_orange_24dp)
            } else {
                tag = false
                setImageResource(R.drawable.ic_star_border_orange_24dp)
            }
        }
        if (!cocktail.ingredient1.isNullOrEmpty() && !cocktail.measure1.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient1, cocktail.measure1)
        }
        if (!cocktail.ingredient2.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient2, cocktail.measure2)
        }
        if (!cocktail.ingredient3.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient3, cocktail.measure3)
        }
        if (!cocktail.ingredient4.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient4, cocktail.measure4)
        }
        if (!cocktail.ingredient5.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient5, cocktail.measure5)
        }
        if (!cocktail.ingredient6.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient6, cocktail.measure6)
        }
        if (!cocktail.ingredient7.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient7, cocktail.measure7)
        }
        if (!cocktail.ingredient8.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient8, cocktail.measure8)
        }
        if (!cocktail.ingredient9.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient9, cocktail.measure9)
        }
        if (!cocktail.ingredient10.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient10, cocktail.measure10)
        }
        if (!cocktail.ingredient11.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient11, cocktail.measure11)
        }
        if (!cocktail.ingredient12.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient12, cocktail.measure12)
        }
        if (!cocktail.ingredient13.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient13, cocktail.measure13)
        }
        if (!cocktail.ingredient14.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient14, cocktail.measure14)
        }
        if (!cocktail.ingredient15.isNullOrEmpty()) {
            createIngredientLine(ll_main, cocktail.ingredient15, cocktail.measure15)
        }
    }

    private fun createIngredientLine(view: LinearLayout, ingredient: String?, measure: String?) {
        val linearLayout = LinearLayout(view.context).apply {
            tag = ingredient
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50.toPx()
            ).apply {
                setMargins(16.toPx(), 0, 16.toPx(), 0)
            }
        }

        val imageViewIngredient = ImageView(view.context).apply {
            layoutParams = LinearLayout.LayoutParams(40.toPx(), 40.toPx()).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
        }

        val textViewIngredient = TextView(view.context).apply {
            text = ingredient
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                7f
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
            setPadding(16.toPx(), 0, 16.toPx(), 0)
        }

        val textViewMeasure = TextView(view.context).apply {
            text = measure
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
            gravity = Gravity.END
        }

        val divider = View(view.context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1.toPx()).apply {
                setMargins(16.toPx(), 0, 16.toPx(), 0)
            }
            background = resources.getDrawable(android.R.color.darker_gray, view.context.theme)
        }

        Glide
            .with(view.context)
            .load(Constants.INGREDIENT_IMAGE_URL + ingredient + Constants.INGREDIENT_IMAGE_EXT)
            .placeholder(R.drawable.ic_ingredient)
            .transition(withCrossFade(drawableCrossFadeFactory))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions.circleCropTransform())
            .into(imageViewIngredient)

        linearLayout.addView(imageViewIngredient)
        linearLayout.addView(textViewIngredient)
        linearLayout.addView(textViewMeasure)
        if (view.findViewWithTag<LinearLayout>(ingredient) == null) {
            view.addView(linearLayout)
            view.addView(divider)
        }
    }
}