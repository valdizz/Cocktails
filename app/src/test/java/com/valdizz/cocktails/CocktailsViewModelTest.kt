package com.valdizz.cocktails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.CocktailsActivity
import com.valdizz.cocktails.ui.cocktails.CocktailsViewModel
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
class CocktailsViewModelTest : Spek({

    emulateInstantTaskExecutorRule()

    val repositoryMock: ICocktailsRepository = mockk()
    val testViewModel = CocktailsViewModel(repositoryMock)

    val cocktailsObserver: Observer<Resource<List<Cocktail>>> = mockk(relaxed = true)

    val cocktail: Cocktail = mockk(relaxed = true)
    val cocktails = listOf(cocktail)
    val errorMessage = "My test error case"
    val responseOk = Resource(Status.SUCCESS, cocktails, null)
    val responseError = Resource(Status.ERROR, null, errorMessage)
    val responseLoading = Resource(Status.LOADING, null, null)

    given("cocktails data loading") {
        beforeEachTest {
            clearMocks(repositoryMock)
            testViewModel.cocktails.observeForever(cocktailsObserver)
        }

        afterEachTest {
            testViewModel.cocktails.removeObserver(cocktailsObserver)
        }

        describe("data loading: success scenario") {
            beforeEachTest {
                coEvery {
                    repositoryMock.filterCocktailByType("Alcoholic")
                } returns MutableLiveData<Resource<List<Cocktail>>>().also {
                    it.value = responseOk
                }
                testViewModel.loadCocktails(Pair(CocktailsActivity.FILTER_COCKTAILS_TAG, "Alcoholic"))
            }

            it("loaded data should be posted") {
                verify { cocktailsObserver.onChanged(responseOk) }
            }

            it("should have a size > 0") {
                assertTrue(responseOk.data?.size!! > 0)
            }
        }

        describe("data loading: error scenario") {
            beforeEachTest {
                coEvery {
                    repositoryMock.filterCocktailByType("Alcoholic")
                } returns MutableLiveData<Resource<List<Cocktail>>>().also {
                    it.value = responseError
                }
                testViewModel.loadCocktails(Pair(CocktailsActivity.FILTER_COCKTAILS_TAG, "Alcoholic"))
            }

            it("error response should be posted") {
                verify { cocktailsObserver.onChanged(responseError) }
            }
        }

        describe("data loading: show progress") {
            beforeEachTest {
                coEvery {
                    repositoryMock.filterCocktailByType("Alcoholic")
                } returns MutableLiveData<Resource<List<Cocktail>>>().also {
                    it.value = responseLoading
                }
                testViewModel.loadCocktails(Pair(CocktailsActivity.FILTER_COCKTAILS_TAG, "Alcoholic"))
            }

            it("show loading progress") {
                verify { cocktailsObserver.onChanged(responseLoading) }
            }
        }
    }
})