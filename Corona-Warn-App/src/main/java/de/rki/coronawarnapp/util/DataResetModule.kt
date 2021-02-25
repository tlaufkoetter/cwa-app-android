package de.rki.coronawarnapp.util

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import de.rki.coronawarnapp.contactdiary.storage.ContactDiaryPreferences
import de.rki.coronawarnapp.datadonation.analytics.storage.AnalyticsSettings
import de.rki.coronawarnapp.datadonation.survey.SurveySettings
import de.rki.coronawarnapp.diagnosiskeys.download.DownloadDiagnosisKeysSettings
import de.rki.coronawarnapp.main.CWASettings
import de.rki.coronawarnapp.nearby.modules.detectiontracker.ExposureDetectionTracker
import de.rki.coronawarnapp.statistics.source.StatisticsProvider
import de.rki.coronawarnapp.storage.ClearableSettings
import de.rki.coronawarnapp.storage.OnboardingData

@Module
class DataResetModule {
    @IntoSet
    @Provides
    fun exposureDetectionTracker(module: ExposureDetectionTracker): ClearableSettings = module

    @IntoSet
    @Provides
    fun downloadDiagnosisKeysSettings(module: DownloadDiagnosisKeysSettings): ClearableSettings = module

    @IntoSet
    @Provides
    fun contactDiaryPreferences(module: ContactDiaryPreferences): ClearableSettings = module

    @IntoSet
    @Provides
    fun cwaSettings(module: CWASettings): ClearableSettings = module

    @IntoSet
    @Provides
    fun statisticsProvider(module: StatisticsProvider): ClearableSettings = module

    @IntoSet
    @Provides
    fun surveySettings(module: SurveySettings): ClearableSettings = module

    @IntoSet
    @Provides
    fun analyticsSettings(module: AnalyticsSettings): ClearableSettings = module

    @IntoSet
    @Provides
    fun onboardingData(module: OnboardingData): ClearableSettings = module
}
