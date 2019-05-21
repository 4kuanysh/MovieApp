package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.movieapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
    }

    private fun initUI() {
        login_btn.setOnClickListener(this)
        singup_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val email = email_input.text.toString()
        val password = password_input.text.toString()

        when (v?.id) {
            R.id.login_btn -> {
                logIn(email, password)
            }
            R.id.singup_btn -> {
                RegistrationActivity.start(this)
            }
        }
    }

    private fun logIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
                    run {
                        if (task.isSuccessful) {
                            Toast.makeText(this, getString(R.string.success_message), Toast.LENGTH_LONG)
                            MainActivity.start(this)
                            finish()
                        } else {
                            val error = task.exception

                            Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }

}
