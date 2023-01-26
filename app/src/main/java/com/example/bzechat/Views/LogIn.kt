package com.example.bzechat.Views

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bzechat.R
import com.example.bzechat.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {

    //Initializations
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var mauth: FirebaseAuth = Firebase.auth
    lateinit var logInBinding:ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInBinding=ActivityLogInBinding.inflate(layoutInflater)
        setContentView(logInBinding.root)

        //ProgressDialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCancelable(false)

        //Create new Account
        logInBinding.tvCreateNewAccount.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }
        //Login To App
        if (mauth.currentUser!=null){
            startActivity(Intent(this,HomePage::class.java))
            finish()
        }
        logInBinding.btnLogin.setOnClickListener {
            progressDialog.show()
            val email = logInBinding.edtEmail.text.toString()
            val password = logInBinding.edtPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(this,HomePage::class.java))
                        progressDialog.dismiss()
                        Toast.makeText(this,"Logged in Successfully",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        progressDialog.dismiss()
                        Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                progressDialog.dismiss()
                Toast.makeText(this,"Please Enter all fields",Toast.LENGTH_SHORT).show()
            }

        }

    }
}