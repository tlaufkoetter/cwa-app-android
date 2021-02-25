package de.rki.coronawarnapp.nearby.modules.detectiontracker

import de.rki.coronawarnapp.storage.ClearableSettings
import kotlinx.coroutines.flow.Flow

interface ExposureDetectionTracker : ClearableSettings {
    val calculations: Flow<Map<String, TrackedExposureDetection>>

    fun trackNewExposureDetection(identifier: String)

    fun finishExposureDetection(identifier: String? = null, result: TrackedExposureDetection.Result)

    override fun clear()
}
