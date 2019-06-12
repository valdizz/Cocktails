package com.valdizz.cocktails.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valdizz.cocktails.model.entity.*

@Dao
abstract class CocktailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCocktails(cocktails: List<Cocktail>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCocktailsIfNotExist(cocktails: List<Cocktail>)

    @Query("UPDATE Cocktail SET ingredient1 = :ingredient1 WHERE id = :cocktailId")
    abstract fun updateCocktailIngredient(cocktailId: Int, ingredient1: String)

    @Query("UPDATE Cocktail SET type = :type WHERE id = :cocktailId")
    abstract fun updateCocktailType(cocktailId: Int, type: String)

    @Query("SELECT * FROM Cocktail")
    abstract fun loadCocktails(): LiveData<List<Cocktail>>

    @Query("SELECT * FROM Cocktail WHERE isFavorite = 1")
    abstract fun loadFavoriteCocktails(): LiveData<List<Cocktail>>

    @Query("UPDATE Cocktail SET isFavorite = NOT isFavorite WHERE id = :cocktailId")
    abstract fun setFavoriteCocktail(cocktailId: Int)

    @Query("SELECT * FROM Cocktail WHERE drink LIKE :name")
    abstract fun searchCocktailByName(name: String): LiveData<List<Cocktail>>

    @Query("SELECT * FROM Cocktail WHERE id = :id")
    abstract fun searchCocktailById(id: Int): LiveData<Cocktail>

    @Query("""SELECT * FROM Cocktail WHERE (ingredient1 LIKE :ingredient OR ingredient2 LIKE :ingredient OR ingredient3 LIKE :ingredient
            OR ingredient4 LIKE :ingredient OR ingredient5 LIKE :ingredient OR ingredient6 LIKE :ingredient OR ingredient7 LIKE :ingredient
            OR ingredient8 LIKE :ingredient OR ingredient9 LIKE :ingredient OR ingredient10 LIKE :ingredient OR ingredient11 LIKE :ingredient
            OR ingredient12 LIKE :ingredient OR ingredient13 LIKE :ingredient OR ingredient14 LIKE :ingredient OR ingredient15 LIKE :ingredient)""")
    abstract fun searchCocktailByIngredient(ingredient: String): LiveData<List<Cocktail>>

    @Query("SELECT * FROM Cocktail WHERE type = :type")
    abstract fun filterCocktailByType(type: String): LiveData<List<Cocktail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertIngredients(ingredients: List<Ingredient>)

    @Query("SELECT * FROM Ingredient WHERE ingredient LIKE :name")
    abstract fun searchIngredientByName(name: String): LiveData<List<Ingredient>>

    @Query("SELECT * FROM Ingredient")
    abstract fun loadIngredients(): LiveData<List<Ingredient>>

    @Query("SELECT * FROM Cocktail ORDER BY RANDOM() LIMIT 1")
    abstract fun loadRandomCocktail(): LiveData<Cocktail>
}