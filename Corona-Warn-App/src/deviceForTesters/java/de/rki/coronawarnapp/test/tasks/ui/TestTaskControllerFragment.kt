package de.rki.coronawarnapp.test.tasks.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.common.io.BaseEncoding
import de.rki.coronawarnapp.R
import de.rki.coronawarnapp.databinding.FragmentTestTaskControllerBinding
import de.rki.coronawarnapp.test.menu.ui.TestMenuItem
import de.rki.coronawarnapp.util.di.AutoInject
import de.rki.coronawarnapp.util.ui.observe2
import de.rki.coronawarnapp.util.ui.viewBindingLazy
import de.rki.coronawarnapp.util.viewmodel.CWAViewModelFactoryProvider
import de.rki.coronawarnapp.util.viewmodel.cwaViewModels
import timber.log.Timber
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature
import javax.inject.Inject
import kotlin.math.sign

@SuppressLint("SetTextI18n")
class TestTaskControllerFragment : Fragment(R.layout.fragment_test_task_controller), AutoInject {

    @Inject lateinit var viewModelFactory: CWAViewModelFactoryProvider.Factory
    private val vm: TestTaskControllerFragmentViewModel by cwaViewModels { viewModelFactory }

    private val binding: FragmentTestTaskControllerBinding by viewBindingLazy()

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.factoryState.observe2(this) { state ->
            binding.taskfactoriesValues.text = state.infos.joinToString("\n")
        }

        vm.controllerState.observe2(this) {
            binding.runningTasksValues.text = it.stateDescriptions.joinToString("\n")
        }

        vm.lastActivityState.observe2(this) { state ->
            val lastResults = state.lastActivity.joinToString("\n")
            binding.tasksLastResults.text = lastResults
        }

        vm.latestTestTaskProgress.observe2(this) {
            if (it == null) return@observe2
            Snackbar.make(
                requireView(),
                "Latest TestTask progress: ${it.primaryMessage.get(requireContext())}",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        val tmp = activity

        binding.testTaskLaunch.setOnClickListener {
            vm.launchTestTask()

            val base64 = BaseEncoding.base64()
            var attestation = "my-challenge2".toByteArray()
            var payload = "hello world".toByteArray()
            var keyAlias = "somealias"

            val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null, null)

            val kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")
            val certBuilder = KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_SIGN)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setAttestationChallenge(attestation)
            kpg.initialize(certBuilder.build())
            val keyPair = kpg.generateKeyPair()
            Timber.tag("KeyAttestation").d("KeyPair: %s", keyPair)
            Timber.tag("KeyAttestation").d("Private: %s", keyPair.private)
            Timber.tag("KeyAttestation").d("Pulblic: %s", keyPair.public)

            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initSign(keyPair.private)
            signature.update(payload)
            val encodedSignature = base64.encode(signature.sign())
            Timber.tag("KeyAttestation").d("encodedSignature: %s", encodedSignature)



            val certificateChain = keyStore.getCertificateChain(keyAlias)
            Timber.tag("KeyAttestation").d("certificateChain: %s", certificateChain)
            certificateChain.forEach {
                Timber.tag("KeyAttestation").d("certificate type: %s", it.type)
                Timber.tag("KeyAttestation").d("certificate: %s", it.encoded)
                Timber.tag("KeyAttestation").d("certificate base64: %s", base64.encode(it.encoded))
            }

//            // SafetyNet PoC
//            var nonce = ByteArray(16)
//            var apiKey = "<ENTER>"
//            Timber.tag("SafetyNet").d("Requesting client...")
//            val client = SafetyNet.getClient(requireActivity())
//            Timber.tag("SafetyNet").d("Requesting attestation...")
//            client
//                .attest(nonce, apiKey)
//                .addOnSuccessListener {
//                    Timber.tag("SafetyNet").d("Attestation: %s", it.jwsResult)
//                }
//                .addOnFailureListener {
//                    Timber.tag("SafetyNet").d(it)
//                }

        }
    }

    companion object {
        val MENU_ITEM = TestMenuItem(
            title = "TaskController",
            description = "Observe and influence the CWA task controller.",
            targetId = R.id.test_taskcontroller_fragment
        )
    }
}
