package pl.jermey.clean_boilerplate.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import android.security.keystore.UserNotAuthenticatedException
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.databinding.DataBindingUtil
import pl.jermey.clean_boilerplate.R
import pl.jermey.clean_boilerplate.databinding.FingerprintViewBinding
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.cert.CertificateException
import java.util.concurrent.Executors
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@RequiresApi(Build.VERSION_CODES.M)
class FingerprintView : AppCompatActivity() {

    private val KEY_NAME = "example_key"
    private val ANDROID_KEYSTORE_PROVIDER = "AndroidKeyStore"

    lateinit var binding: FingerprintViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fingerprint_view)
        binding.auth.setOnClickListener { auth() }
        binding.setup.setOnClickListener { setup() }
        binding.test.setOnClickListener {
            try {
                test()
            } catch (e: UserNotAuthenticatedException) {
                Toast.makeText(this@FingerprintView, "Fail: UserNotAuthenticatedException", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun test() {
        val cipher = Cipher.getInstance("$KEY_ALGORITHM_AES/$BLOCK_MODE_CBC/$ENCRYPTION_PADDING_PKCS7")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val bytes = cipher.doFinal("Hello cipher!".toByteArray())

        Handler(mainLooper).post {
            Toast.makeText(
                this@FingerprintView,
                "Success:\nbytes:${bytes?.contentToString()}\niv:${cipher.iv?.contentToString()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun auth() {
        val executor = Executors.newSingleThreadExecutor()
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Handler(mainLooper).post {
                    Toast.makeText(this@FingerprintView, "Error $errorCode: $errString", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                test()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Handler(mainLooper).post {
                    Toast.makeText(this@FingerprintView, "Fail", Toast.LENGTH_SHORT).show()
                }
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Set the title to display.")
            .setSubtitle("Set the subtitle to display.")
            .setDescription("Set the description to display")
            .setNegativeButtonText("Negative Button")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun setup() {
        generateKey()
    }

    private fun getSecretKey(): SecretKey {

        return try {
            KeyStore.getInstance(ANDROID_KEYSTORE_PROVIDER).apply { load(null) }.getKey(KEY_NAME, null) as SecretKey
        } catch (e: Exception) {
            when (e) {
                is NoSuchAlgorithmException,
                is NoSuchProviderException -> throw RuntimeException(e)
                else -> throw e
            }
        }
    }

    private fun generateKey() {
        val keyGenerator: KeyGenerator?
        val keyStore: KeyStore?
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEYSTORE_PROVIDER)
        } catch (e: Exception) {
            when (e) {
                is NoSuchAlgorithmException,
                is NoSuchProviderException -> throw RuntimeException(e)
                else -> throw e
            }
        }

        try {
            keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM_AES, ANDROID_KEYSTORE_PROVIDER)
        } catch (e: Exception) {
            when (e) {
                is NoSuchAlgorithmException,
                is NoSuchProviderException -> throw RuntimeException(e)
                else -> throw e
            }
        }
        try {
            keyStore?.load(null)
            keyGenerator?.init(KeyGenParameterSpec.Builder(KEY_NAME, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                .apply {
                    setKeySize(256)
                    setBlockModes(BLOCK_MODE_CBC)
                    setUserAuthenticationRequired(true)
                    setUserAuthenticationValidityDurationSeconds(2)
                    setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        setInvalidatedByBiometricEnrollment(true)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        setIsStrongBoxBacked(true)
                    }
                }
                .build())
            keyGenerator?.generateKey()
        } catch (e: Exception) {
            when (e) {
                is NoSuchAlgorithmException,
                is InvalidAlgorithmParameterException,
                is CertificateException,
                is IOException -> throw RuntimeException(e)
                else -> throw e
            }
        }

    }


}