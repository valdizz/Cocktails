package com.valdizz.cocktails.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valdizz.cocktails.model.entity.*

/**
 * Room database class.
 *
 * @author Vlad Kornev
 */
@Database(
    entities = [Cocktail::class, Ingredient::class],
    version = 1,
    exportSchema = false)
abstract class CocktailsDb : RoomDatabase() {

    abstract fun cocktailsDao(): CocktailsDao
}