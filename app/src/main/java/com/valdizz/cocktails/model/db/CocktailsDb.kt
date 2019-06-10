package com.valdizz.cocktails.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valdizz.cocktails.model.entity.*

@Database(
    entities = [Cocktail::class, Ingredient::class, Category::class, Type::class, Glass::class],
    version = 1,
    exportSchema = false)
abstract class CocktailsDb : RoomDatabase() {

    abstract fun cocktailsDao(): CocktailsDao
}