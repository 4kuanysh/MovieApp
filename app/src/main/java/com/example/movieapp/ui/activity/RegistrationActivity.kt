package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.movieapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, RegistrationActivity::class.java))


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initUI()
    }

    private fun initUI() {
        singup_btn.setOnClickListener {
            val email = email_input.text.toString()
            val password = password_input.text.toString()
            val password_confirm = password_confirm_input.text.toString()

            if (password_confirm.equals(password))
                signUp(email, password)
            else
                Toast.makeText(this, R.string.passwords_do_not_match, Toast.LENGTH_LONG).show()
        }
    }

    private fun signUp(email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
                run {
                    if (task.isSuccessful) {
                        Toast.makeText(this, R.string.success_message, Toast.LENGTH_LONG).show()
                        LoginActivity.start(this);
                        finish()
                    } else {
                        val error = task.exception
                        Toast.makeText(this, error?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }


    }
}
