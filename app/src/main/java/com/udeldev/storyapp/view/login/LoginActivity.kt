package com.udeldev.storyapp.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityLoginBinding
import com.udeldev.storyapp.helper.factory.ViewModelFactory
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.view.main.MainActivity
import com.udeldev.storyapp.view.main.MainViewModel
import com.udeldev.storyapp.view.register.RegisterActivity
import com.udeldev.storyapp.view.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setContentView(activityLoginBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityLoginBinding.editLoginEmail.message.observe(this) {
            activityLoginBinding.editLoginEmailLayout.error = it
        }

        activityLoginBinding.editLoginPassword.message.observe(this) {
            activityLoginBinding.editLoginPasswordLayout.error = it
        }

        activityLoginBinding.textButtonLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        loginViewModel.user.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(result.state)
                is Result.Failure -> showErrorDialog(result.throwable)
                is Result.Success -> {
                    loginViewModel.saveSession(result.data.loginResult?.token.toString())
                }
            }

        }

        loginViewModel.token.observe(this) {
            if (!it.isNullOrEmpty()) {
                Log.i("LoginActivity", it)
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        activityLoginBinding.buttonLogin.setOnClickListener {
            if (activityLoginBinding.editLoginEmail.text.isNullOrEmpty()){
                activityLoginBinding.editLoginEmailLayout.error = resources.getString(R.string.empty_text)
            }
            if (activityLoginBinding.editLoginPassword.text.isNullOrEmpty()){
                activityLoginBinding.editLoginPasswordLayout.error = resources.getString(R.string.empty_text)
            }

            if (activityLoginBinding.editLoginEmailLayout.error != null || activityLoginBinding.editLoginPasswordLayout.error != null) {
                Toast.makeText(this, resources.getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginViewModel.loginUser(
                activityLoginBinding.editLoginEmail.text.toString(),
                activityLoginBinding.editLoginPassword.text.toString()
            )
        }

    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(message)
            setPositiveButton("Oke") { _, _ ->
                val intent = Intent(context, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activityLoginBinding.progressLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
        activityLoginBinding.loginLayoutComponent.visibility =  if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(context: Context): LoginViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun initComponent() {
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel = obtainViewModel(this)
    }


}