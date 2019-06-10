package com.valdizz.cocktails.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.valdizz.cocktails.common.AppExecutors
import com.valdizz.cocktails.common.RateLimiter
import com.valdizz.cocktails.model.api.ApiResponse
import com.valdizz.cocktails.model.api.CocktailsApiService
import com.valdizz.cocktails.model.db.CocktailsDao
import com.valdizz.cocktails.model.entity.*

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
                val drinks = mutableListOf<Cocktail>()
                for (drink in item.drinks) {
                    drinks.add(drink.copy(ingredient1 = ingredient))
                }
                cocktailsDao.insertCocktails(drinks)
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
                cocktailsDao.insertCocktails(item.drinks)
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(type)
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> = cocktailsDao.filterCocktailByType(type)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> = cocktailsApiService.filterCocktailByType(type)
        }.asLiveData()
    }

    override fun filterCocktailByCategory(category: String): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBoundResource<List<Cocktail>, Cocktails>(appExecutors) {
            override fun saveCallResult(item: Cocktails) {
                cocktailsDao.insertCocktails(item.drinks)
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(category)
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> = cocktailsDao.filterCocktailByCategory(category)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> =
                cocktailsApiService.filterCocktailByCategory(category)
        }.asLiveData()
    }

    override fun filterCocktailByGlass(glass: String): LiveData<Resource<List<Cocktail>>> {
        return object : NetworkBoundResource<List<Cocktail>, Cocktails>(appExecutors) {
            override fun saveCallResult(item: Cocktails) {
                cocktailsDao.insertCocktails(item.drinks)
            }

            override fun shouldFetch(data: List<Cocktail>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(glass)
            }

            override fun loadFromDb(): LiveData<List<Cocktail>> = cocktailsDao.filterCocktailByGlass(glass)

            override fun createCall(): LiveData<ApiResponse<Cocktails>> =
                cocktailsApiService.filterCocktailByGlass(glass)
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

    override fun getCategories(): LiveData<Resource<List<Category>>> {
        return object : NetworkBoundResource<List<Category>, Categories>(appExecutors) {
            override fun saveCallResult(item: Categories) {
                cocktailsDao.insertCategories(item.categories)
            }

            override fun shouldFetch(data: List<Category>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("categories")
            }

            override fun loadFromDb(): LiveData<List<Category>> = cocktailsDao.loadCategories()

            override fun createCall(): LiveData<ApiResponse<Categories>> = cocktailsApiService.getCategories()
        }.asLiveData()
    }

    override fun getGlasses(): LiveData<Resource<List<Glass>>> {
        return object : NetworkBoundResource<List<Glass>, Glasses>(appExecutors) {
            override fun saveCallResult(item: Glasses) {
                cocktailsDao.insertGlasses(item.glasses)
            }

            override fun shouldFetch(data: List<Glass>?): Boolean {
                return data == null || data.isEmpty()|| repoListRateLimit.shouldFetch("glasses")
            }

            override fun loadFromDb(): LiveData<List<Glass>> = cocktailsDao.loadGlasses()

            override fun createCall(): LiveData<ApiResponse<Glasses>> = cocktailsApiService.getGlasses()
        }.asLiveData()
    }

    override fun getAlcoholicFilters(): LiveData<Resource<List<Type>>> {
        return object : NetworkBoundResource<List<Type>, Types>(appExecutors) {
            override fun saveCallResult(item: Types) {
                cocktailsDao.insertTypes(item.types)
            }

            override fun shouldFetch(data: List<Type>?): Boolean {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("type")
            }

            override fun loadFromDb(): LiveData<List<Type>> = cocktailsDao.loadTypes()

            override fun createCall(): LiveData<ApiResponse<Types>> = cocktailsApiService.getTypes()
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