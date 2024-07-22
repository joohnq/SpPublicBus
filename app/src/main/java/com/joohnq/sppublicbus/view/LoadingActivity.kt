package com.joohnq.sppublicbus.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.joohnq.sppublicbus.databinding.ActivityLoadingBinding
import com.joohnq.sppublicbus.view.components.setOnApplyWindowInsetsListener
import com.joohnq.sppublicbus.viewmodel.AuthenticationViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingActivity : AppCompatActivity() {
    private val authenticationViewmodel: AuthenticationViewmodel by viewModels()
    private val binding: ActivityLoadingBinding by lazy {
        ActivityLoadingBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.setOnApplyWindowInsetsListener()
        binding.observers()
        authenticationViewmodel.authentication()
    }

    private fun ActivityLoadingBinding.toggleLoadingPage(state: Boolean) {
        includeCustomLoadingPage.root.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun ActivityLoadingBinding.toggleErrorPage(
        state: Boolean,
        text: String? = null
    ) {
        includeCustomErrorPage.root.visibility = if (state) View.VISIBLE else View.GONE
        includeCustomErrorPage.customErrorText.text = text
    }

    private fun ActivityLoadingBinding.observers() {
        authenticationViewmodel.authentication.observe(this@LoadingActivity) { state ->
            state.fold(
                onLoading = {
                    toggleErrorPage(false)
                },
                onError = { error ->
                    toggleLoadingPage(false)
                    toggleErrorPage(true, error)
                },
                onSuccess = {
                    Intent(this@LoadingActivity, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                },
            )
        }
    }
}