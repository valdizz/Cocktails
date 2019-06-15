package com.valdizz.cocktails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.valdizz.cocktails.model.entity.Ingredient
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.ingredients.IngredientsViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.Assert.assertTrue
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class IngredientsViewModelTest : Spek({

    emulateInstantTaskExecutorRule()

    val repositoryMock: ICocktailsRepository = mockk()
    val testViewModel = IngredientsViewModel(repositoryMock)

    val ingredientsObserver: Observer<Resource<List<Ingredient>>> = mockk(relaxed = true)

    val ingredient: Ingredient = mockk(relaxed = true)
    val ingredients = listOf(ingredient)
    val errorMessage = "My test error case"
    val responseOk = Resource(Status.SUCCESS, ingredients, null)
    val responseError = Resource(Status.ERROR, null, errorMessage)
    val responseLoading = Resource(Status.LOADING, null, null)

    given("ingredients data loading") {
        beforeEachTest {
            clearMocks(repositoryMock)
            testViewModel.ingredients.observeForever(ingredientsObserver)
        }

        afterEachTest {
            testViewModel.ingredients.removeObserver(ingredientsObserver)
        }

        describe("data loading: success scenario") {
            beforeEachTest {
                coEvery {
                    repositoryMock.getIngredients()
                } returns MutableLiveData<Resource<List<Ingredient>>>().also {
                    it.value = responseOk
                }
                testViewModel.searchIngredient(null)
            }

            it("loaded data should be posted") {
                verify { ingredientsObserver.onChanged(responseOk) }
            }

            it("should have a size > 0") {
                assertTrue(responseOk.data?.size!! > 0)
            }
        }

        describe("data loading: error scenario") {
            beforeEachTest {
                coEvery {
                    repositoryMock.getIngredients()
                } returns MutableLiveData<Resource<List<Ingredient>>>().also {
                    it.value = responseError
                }
                testViewModel.searchIngredient(null)
            }

            it("error response should be posted") {
                verify { ingredientsObserver.onChanged(responseError) }
            }
        }

        describe("data loading: show progress") {
            beforeEachTest {
                coEvery {
                    repositoryMock.getIngredients()
                } returns MutableLiveData<Resource<List<Ingredient>>>().also {
                    it.value = responseLoading
                }
                testViewModel.searchIngredient(null)
            }

            it("show loading progress") {
                verify { ingredientsObserver.onChanged(responseLoading) }
            }
        }
    }
})