package com.example.notesapptubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        btnReg.setOnClickListener {
            performRegister()
        }

        loginTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        showPassBtn.setOnClickListener {
            if(showPassBtn.text.toString().equals("Show")){
                passRegEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPassBtn.text = "Hide"
            }else{
                passRegEt.transformationMethod = PasswordTransformationMethod.getInstance()
                showPassBtn.text = "Show"
            }
        }

    }

    private fun performRegister(){
        val email = regEmailEt.text.toString()
        val pass = passRegEt.text.toString()

        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please input email or password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    Log.d("Register","Succesfully created user with uid : ${it.result?.user?.uid}")
                    Toast.makeText(this,"Register Succesful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }else{
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Register Failed : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}