package com.valdizz.cocktails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource
import com.valdizz.cocktails.model.repository.Status
import com.valdizz.cocktails.ui.cocktaildetail.CocktailDetailViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.Assert.assertNotNull
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class CocktailDetailViewModelTest : Spek({

    emulateInstantTaskExecutorRule()

    val repositoryMock: ICocktailsRepository = mockk()
    val testViewModel = CocktailDetailViewModel(repositoryMock)

    val cocktailObserver: Observer<Resource<Cocktail>> = mockk(relaxed = true)

    val cocktail: Cocktail = mockk(relaxed = true)
    val errorMessage = "My test error case"
    val responseOk = Resource(Status.SUCCESS, cocktail, null)
    val responseError = Resource(Status.ERROR, null, errorMessage)
    val responseLoading = Resource(Status.LOADING, null, null)

    given("cocktail detail data loading") {
        beforeEachTest {
            clearMocks(repositoryMock)
            testViewModel.cocktail.observeForever(cocktailObserver)
        }

        afterEachTest {
            testViewModel.cocktail.removeObserver(cocktailObserver)
        }

        describe("data loading: success scenario") {
            beforeEachTest {
                coEvery {
                    repositoryMock.searchCocktailById(123)
                } returns MutableLiveData<Resource<Cocktail>>().also {
                    it.value = responseOk
                }
                testViewModel.loadCocktail(123)
            }

            it("loaded data should be posted") {
                verify { cocktailObserver.onChanged(responseOk) }
            }

            it("should have id") {
                assertNotNull(responseOk.data?.id)
            }

            it("should have name") {
                assertNotNull(responseOk.data?.drink)
            }
        }

        describe("data loading: error scenario") {
            beforeEachTest {
                coEvery {
                    repositoryMock.searchCocktailById(123)
                } returns MutableLiveData<Resource<Cocktail>>().also {
                    it.value = responseError
                }
                testViewModel.loadCocktail(123)
            }

            it("error response should be posted") {
                verify { cocktailObserver.onChanged(responseError) }
            }
        }

        describe("data loading: show progress") {
            beforeEachTest {
                coEvery {
                    repositoryMock.searchCocktailById(123)
                } returns MutableLiveData<Resource<Cocktail>>().also {
                    it.value = responseLoading
                }
                testViewModel.loadCocktail(123)
            }

            it("show loading progress") {
                verify { cocktailObserver.onChanged(responseLoading) }
            }
        }
    }
})