package com.udeldev.storyapp.view.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.udeldev.storyapp.R
import com.udeldev.storyapp.databinding.ActivityRegisterBinding
import com.udeldev.storyapp.helper.factory.ViewModelFactory
import com.udeldev.storyapp.helper.utils.Result
import com.udeldev.storyapp.view.login.LoginActivity
import com.udeldev.storyapp.view.welcome.WelcomeActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniComponent()
        setContentView(activityRegisterBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityRegisterBinding.editRegisterEmail.message.observe(this) {
            activityRegisterBinding.editRegisterEmailLayout.error = it
        }

        activityRegisterBinding.editRegisterPassword.message.observe(this) {
            activityRegisterBinding.editRegisterPasswordLayout.error = it
        }

        activityRegisterBinding.editRegisterPasswordConfirm.message.observe(this) {
            activityRegisterBinding.editRegisterPasswordConfirmLayout.error = it
        }

        registerViewModel.response.observe(this){result ->
            when(result){
                is Result.Failure -> showDialog(resources.getString(R.string.error), result.throwable)
                is Result.Loading -> showLoading(result.state)
                is Result.Success -> showDialog(resources.getString(R.string.success), result.data.message ?: resources.getString(R.string.success_create_user))
            }
        }


        activityRegisterBinding.buttonRegister.setOnClickListener {
            if (activityRegisterBinding.editRegisterEmail.text.isNullOrEmpty()) {
                activityRegisterBinding.editRegisterEmailLayout.error = resources.getString(R.string.empty_text)
            }
            if (activityRegisterBinding.editRegisterPassword.text.isNullOrEmpty()) {
                activityRegisterBinding.editRegisterPasswordLayout.error = resources.getString(R.string.empty_text)
            }
            if (activityRegisterBinding.editRegisterPasswordConfirm.text.isNullOrEmpty()) {
                activityRegisterBinding.editRegisterPasswordConfirmLayout.error =
                    resources.getString(R.string.empty_text)
            }
            if (activityRegisterBinding.editRegisterPassword.text.toString() != activityRegisterBinding.editRegisterPasswordConfirm.text.toString()){
                activityRegisterBinding.editRegisterPasswordConfirmLayout.error = resources.getString(R.string.password_not_matched)
            }

            if (activityRegisterBinding.editRegisterEmailLayout.error != null || activityRegisterBinding.editRegisterPasswordLayout.error != null || activityRegisterBinding.editRegisterPasswordConfirmLayout.error != null) {
                Toast.makeText(this, resources.getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerViewModel.registerUser(
                activityRegisterBinding.editRegisterName.text.toString(),
                activityRegisterBinding.editRegisterEmail.text.toString(),
                activityRegisterBinding.editRegisterPassword.text.toString(),
            )
        }

        activityRegisterBinding.textButtonRegisterLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(title : String, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
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

    private fun showLoading (isLoading: Boolean){
        activityRegisterBinding.registerLayoutComponent.visibility = if (isLoading) View.GONE else View.VISIBLE
        activityRegisterBinding.progressRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun iniComponent() {
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        registerViewModel = obtainViewModel(this)
    }

    private fun obtainViewModel(context: Context): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(context)
        return ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

}