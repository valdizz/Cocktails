package com.valdizz.cocktails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.valdizz.cocktails.R
import com.valdizz.cocktails.common.Constants.INGREDIENT_IMAGE_EXT
import com.valdizz.cocktails.common.Constants.INGREDIENT_IMAGE_URL
import com.valdizz.cocktails.model.entity.Ingredient
import com.valdizz.cocktails.ui.ingredients.IngredientClickListener
import kotlinx.android.synthetic.main.item_ingredient.view.*

class IngredientsRecyclerViewAdapter(private val ingredientClickListener: IngredientClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ingredients: List<Ingredient> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(ingredients[position])
        holder.itemView.setOnClickListener {
            ingredientClickListener.onClick(ingredients[position].ingredient)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val drawableCrossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

        fun bindData(item: Ingredient) {
            itemView.tv_ingredient.text = item.ingredient
            Glide
                .with(itemView.context)
                .load(INGREDIENT_IMAGE_URL + item.ingredient + INGREDIENT_IMAGE_EXT)
                .transition(withCrossFade(drawableCrossFadeFactory))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_ingredient)
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.iv_ingredient)
        }
    }
}