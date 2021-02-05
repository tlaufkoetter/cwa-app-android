package de.rki.coronawarnapp.datadonation.survey

import de.rki.coronawarnapp.appconfig.AppConfigProvider
import de.rki.coronawarnapp.datadonation.storage.OTPRepository
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurveyHighRiskUrlProvider @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val otpRepo: OTPRepository
) {

    /**
     * Provides the Url for the Data Donation Survey for high risk cards.
     *
     * @throws IllegalArgumentException If the AppConfig provides a malformed URL for the survey
     * @throws IllegalStateException If no one time password (otp) can be loaded from the OTPRepository
     */
    suspend fun provideUrl(): String {
        val url = appConfigProvider.getAppConfig().identifier // Change to appConfig.getAppConfig().surveyOnHighRiskUrl
        val queryParameterNameOtp = "queryParamNameOtp" // Change to appConfig.getAppConfig().otpQueryParameterName
        val otp = otpRepo.lastOTP?.uuid ?: throw IllegalStateException("Could not OTP uuid from OTPRepository")

        val httpUrl = try {
            url.toHttpUrl()
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException("$url is not a well-formed URL")
        }

        return httpUrl.newBuilder()
            .addQueryParameter(queryParameterNameOtp, otp.toString())
            .build()
            .toUrl()
            .toString()
    }
}
