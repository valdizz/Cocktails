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
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.ui.cocktails.CocktailClickListener
import kotlinx.android.synthetic.main.item_cocktail.view.*

class CocktailsRecyclerViewAdapter(private val cocktailClickListener: CocktailClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cocktails: List<Cocktail> = ArrayList()
        set(value) {
            field = value.sortedBy { it.drink }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cocktail, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(cocktails[position])
        holder.itemView.setOnClickListener {
            cocktailClickListener.onClick(cocktails[position].id)
        }
    }

    override fun getItemCount(): Int {
        return cocktails.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val drawableCrossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

        fun bindData(item: Cocktail) {
            itemView.tv_drink.text = item.drink
            Glide
                .with(itemView.context)
                .load(item.image)
                .transition(withCrossFade(drawableCrossFadeFactory))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_cocktail)
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.iv_cocktail)
        }
    }
}