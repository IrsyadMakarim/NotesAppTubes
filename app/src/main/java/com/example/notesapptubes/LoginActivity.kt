package com.example.notesapptubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        btnLogin.setOnClickListener {
            performLogin()
        }

        signUpTv.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        showPassLoginBtn.setOnClickListener {
            if(showPassLoginBtn.text.toString().equals("Show")){
                passLoginEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPassLoginBtn.text = "Hide"
            }else{
                passLoginEt.transformationMethod = PasswordTransformationMethod.getInstance()
                showPassLoginBtn.text = "Show"
            }
        }
    }

    private fun performLogin(){
        val email = loginEmailEt.text.toString()
        val pass = passLoginEt.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Log.d("Login", "Login Succesful")
                    Toast.makeText(this,"Login Success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }else{
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Login Failed : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}