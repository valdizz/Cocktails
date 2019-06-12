package com.valdizz.cocktails.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.valdizz.cocktails.common.AppExecutors
import com.valdizz.cocktails.common.RateLimiter
import com.valdizz.cocktails.model.api.ApiResponse
import com.valdizz.cocktails.model.api.CocktailsApiService
import com.valdizz.cocktails.model.db.CocktailsDao
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.entity.Cocktails
import com.valdizz.cocktails.model.entity.Ingredient
import com.valdizz.cocktails.model.entity.Ingredients

class CocktailsRepository(
    val appExecutors: AppExecutors,
    val cocktailsDao: CocktailsDao,
    val cocktailsApiService: CocktailsApiService,
    val repoListRateLimit: RateLimiter<String>
) : ICocktailsRepository {

    override fun searchCocktailByName(name: String): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBoundResource<List<Cocktail>, Cocktails>(appExecutors) {
            override fun saveCallResult(item: Cocktails) {
                if (!item.drinks.isNullOrEmpty()) {
                    cocktailsDao.insertCocktails(item.drinks)
                }
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(name)
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> = cocktailsDao.searchCocktailByName(name)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> = cocktailsApiService.searchCocktailByName(name)
        }.asLiveData()
    }

    override fun searchCocktailById(id: Int): LiveData<Resource<Cocktail>> {
        return object : NetworkBoundResource<Cocktail, Cocktails>(appExecutors) {
            override fun saveCallResult(item: Cocktails) {
                cocktailsDao.insertCocktails(item.drinks)
            }

            override fun shouldFetch(data: Cocktail?): Boolean = data?.measure1 == null

            override fun loadFromDb(): LiveData<Cocktail> = cocktailsDao.searchCocktailById(id)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> = cocktailsApiService.searchCocktailById(id)
        }.asLiveData()
    }

    override fun searchCocktailByIngredient(ingredient: String): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBoundResource<List<Cocktail>, Cocktails>(appExecutors) {
            override fun saveCallResult(item: Cocktails) {
                cocktailsDao.insertCocktailsIfNotExist(item.drinks)
                for (drink in item.drinks) {
                    cocktailsDao.updateCocktailIngredient(drink.id, ingredient)
                }
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> = cocktailsDao.searchCocktailByIngredient(ingredient)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> =
                cocktailsApiService.searchCocktailByIngredient(ingredient)
        }.asLiveData()
    }

    override fun filterCocktailByType(type: String): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBoundResource<List<Cocktail>, Cocktails>(appExecutors) {
            override fun saveCallResult(item: Cocktails) {
                cocktailsDao.insertCocktailsIfNotExist(item.drinks)
                for (drink in item.drinks) {
                    cocktailsDao.updateCocktailType(drink.id, type)
                }
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(type)
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> = cocktailsDao.filterCocktailByType(type)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> = cocktailsApiService.filterCocktailByType(type)
        }.asLiveData()
    }

    override fun searchIngredientByName(name: String): LiveData<Resource<List<Ingredient>>> {
        return object : NetworkBoundResource<List<Ingredient>, Ingredients>(appExecutors) {
            override fun saveCallResult(item: Ingredients) {
                cocktailsDao.insertIngredients(item.ingredients)
            }

            override fun shouldFetch(data: List<Ingredient>?): Boolean {
                var isNotFullData = false
                data?.map {
                    if (it.id == null || it.description == null || it.type == null) {
                        isNotFullData = true
                    }
                }
                return data == null || data.isEmpty() || isNotFullData
            }

            override fun loadFromDb(): LiveData<List<Ingredient>> = cocktailsDao.searchIngredientByName(name)

            override fun createCall(): LiveData<ApiResponse<Ingredients>> =
                cocktailsApiService.searchIngredientByName(name)
        }.asLiveData()
    }

    override fun getIngredients(): LiveData<Resource<List<Ingredient>>> {
        return object : NetworkBoundResource<List<Ingredient>, Ingredients>(appExecutors) {
            override fun saveCallResult(item: Ingredients) {
                cocktailsDao.insertIngredients(item.ingredients)
            }

            override fun shouldFetch(data: List<Ingredient>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("ingredients")
            }

            override fun loadFromDb(): LiveData<List<Ingredient>> = cocktailsDao.loadIngredients()

            override fun createCall(): LiveData<ApiResponse<Ingredients>> = cocktailsApiService.getIngredients()
        }.asLiveData()
    }

    override fun randomCocktail(): LiveData<Resource<Cocktail>> {
        return object : NetworkBoundResource<Cocktail, Cocktails>(appExecutors) {
            private var id = 0

            override fun saveCallResult(item: Cocktails) {
                id = item.drinks[0].id
                cocktailsDao.insertCocktails(item.drinks)
            }

            override fun shouldFetch(data: Cocktail?): Boolean = data == null

            override fun loadFromDb(): LiveData<Cocktail> = cocktailsDao.searchCocktailById(id)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> = cocktailsApiService.getRandomCocktail()
        }.asLiveData()
    }

    override fun getFavoriteCocktails(): LiveData<Resource<List<Cocktail>>> {
        val result = MediatorLiveData<Resource<List<Cocktail>>>()
        result.value = Resource.loading(null)
        val dbSource = cocktailsDao.loadFavoriteCocktails()
        result.addSource(dbSource) { data ->
            result.value = Resource.success(data)
        }
        return result
    }

    override fun setFavoriteCocktail(cocktailId: Int) {
        appExecutors.diskIO().execute {
            cocktailsDao.setFavoriteCocktail(cocktailId)
        }
    }
}