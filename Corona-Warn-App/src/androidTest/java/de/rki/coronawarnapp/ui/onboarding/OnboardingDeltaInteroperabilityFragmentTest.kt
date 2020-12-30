package de.rki.coronawarnapp.ui.onboarding

import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.rki.coronawarnapp.R
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import testhelpers.BaseUITest
import testhelpers.Screenshot
import tools.fastlane.screengrab.Screengrab

@RunWith(AndroidJUnit4::class)
class OnboardingDeltaInteroperabilityFragmentTest : BaseUITest() {

    @MockK lateinit var viewModel: OnboardingDeltaInteroperabilityFragmentViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        setupMockViewModel(object : OnboardingDeltaInteroperabilityFragmentViewModel.Factory {
            override fun create(): OnboardingDeltaInteroperabilityFragmentViewModel = viewModel
        })
    }

    @After
    fun teardown() {
        clearAllViewModels()
    }

    @Test
    fun launch_fragment() {
        launchFragment<OnboardingDeltaInteroperabilityFragment>()
    }

    @Screenshot
    @Test
    fun capture_screenshot() {
        launchFragmentInContainer<OnboardingDeltaInteroperabilityFragment>(themeResId = R.style.AppTheme)
        // Check any view to make sure screenshot is not blank
        onView(withId(R.id.onboarding_button_next)).check(matches(isDisplayed()))
        Screengrab.screenshot(OnboardingDeltaInteroperabilityFragment::class.simpleName)
    }
}

@Module
abstract class OnboardingDeltaInteroperabilityFragmentTestModule {
    @ContributesAndroidInjector
    abstract fun onboardingDeltaInteroperabilityFragment(): OnboardingDeltaInteroperabilityFragment
}
