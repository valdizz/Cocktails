package com.valdizz.cocktails.ui.cocktaildetail

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.valdizz.cocktails.R
import com.valdizz.cocktails.common.ComponentUtils
import com.valdizz.cocktails.common.Constants
import com.valdizz.cocktails.common.Constants.INGREDIENTS_COUNT
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.CocktailsActivity
import kotlinx.android.synthetic.main.fragment_cocktail_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * [CocktailDetailFragment] displays detailed information about the cocktail.
 *
 * @author Vlad Kornev
 */
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
                CocktailsActivity.RANDOM_COCKTAIL_TAG
            ) {
                cocktailId = it
            }
            initCocktailObserver(cocktailId)
        }

        iv_cocktail_favorite.setOnClickListener {
            onFavoriteClick()
        }
    }

    private fun onFavoriteClick() {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COCKTAIL_ID, cocktailId)
    }

    private fun initCocktailObserver(id: Int) {
        cocktailDetailViewModel.loadCocktail(id)
        cocktailDetailViewModel.cocktail.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.LOADING) {
                progress_detail_cocktail.visibility = ProgressBar.VISIBLE
            } else {
                progress_detail_cocktail.visibility = ProgressBar.GONE
                it.data?.let { cocktail ->
                    showCocktail(cocktail)
                }
                if (it.status == Status.ERROR) {
                    Toast.makeText(context, getString(R.string.msg_network_request_failed), Toast.LENGTH_LONG).show()
                }
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
        tv_cocktail_type.text = getString(
            R.string.type,
            if (cocktail.type.isNullOrEmpty()) {
                getString(R.string.undefined)
            } else {
                cocktail.type
            }
        )
        tv_cocktail_category.text = getString(
            R.string.category,
            if (cocktail.category.isNullOrEmpty()) {
                getString(R.string.undefined)
            } else {
                cocktail.category
            }
        )
        tv_cocktail_glass.text = getString(
            R.string.glass,
            if (cocktail.glass.isNullOrEmpty()) {
                getString(R.string.undefined)
            } else {
                cocktail.glass
            }
        )
        tv_cocktail_recipe.text = if (cocktail.instructions.isNullOrEmpty()) {
            ""
        } else {
            cocktail.instructions
        }
        iv_cocktail_favorite.apply {
            if (cocktail.isFavorite) {
                tag = true
                setImageResource(R.drawable.ic_star_orange_24dp)
            } else {
                tag = false
                setImageResource(R.drawable.ic_star_border_orange_24dp)
            }
        }
        showIngredients(cocktail)
    }

    private fun showIngredients(cocktail: Cocktail) {
        for (i in 1..INGREDIENTS_COUNT) {
            val ingredientField = Cocktail::class.java.getDeclaredField("ingredient$i")
            ingredientField.isAccessible = true
            val ingredient = ingredientField.get(cocktail)
            val measureField = Cocktail::class.java.getDeclaredField("measure$i")
            measureField.isAccessible = true
            val measure = measureField.get(cocktail)
            if (ingredient != null && measure != null) {
                createIngredientLine(ll_main, ingredient as String, measure as String)
            }
        }
    }

    private fun createIngredientLine(view: LinearLayout, ingredient: String?, measure: String?) {
        if (view.findViewWithTag<LinearLayout>(ingredient) == null && !ingredient.isNullOrEmpty()) {
            val linearLayout = ComponentUtils.createLinearLayout(view, ingredient, 16, 50)
            val imageViewIngredient = ComponentUtils.createImageView(view, 40, 40)
            val textViewIngredient = ComponentUtils.createTextView(view, ingredient, 7f, Gravity.NO_GRAVITY, 16)
            val textViewMeasure = ComponentUtils.createTextView(view, measure, 1f, Gravity.END, 0)
            val divider = ComponentUtils.createDivider(view, 16)

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
            view.addView(linearLayout)
            view.addView(divider)
        }
    }
}