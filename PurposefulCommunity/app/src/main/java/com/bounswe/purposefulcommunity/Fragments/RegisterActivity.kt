package com.bounswe.purposefulcommunity.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bounswe.mercatus.API.ApiInterface
import com.bounswe.mercatus.API.RetrofitInstance
import com.bounswe.purposefulcommunity.Fragments.Community.CommunityFeedActivity
import com.bounswe.purposefulcommunity.Models.SignInBody
import com.bounswe.purposefulcommunity.Models.SignInRes
import com.bounswe.purposefulcommunity.Models.SignUpBody
import com.bounswe.purposefulcommunity.R
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.title_activity_sign_up)

        btn_signup.setOnClickListener {
            val name = editName.text.toString()
            val surname = editSurname.text.toString()
            val email = editMail.text.toString()
            val password = editPassword.text.toString()

            if (isValidForm(email, name, surname, password)) {
                signUp(email, name, surname, password)
            }
        }
        link_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
    }
    private fun signUp(email: String, name: String, surname: String, password: String){
        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val registerInfo = SignUpBody(email,name, password,surname)

        purApp.registerUser(registerInfo).enqueue(object : Callback<SignUpBody> {
            override fun onFailure(call: Call<SignUpBody>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@RegisterActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<SignUpBody>, response: Response<SignUpBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@RegisterActivity, "Registration success!", Toast.LENGTH_SHORT)
                        .show()
                    val sharedPreferences = getSharedPreferences("TOKEN_INFO", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    //Since we need token, we need to do signin
                    signIn(email, password, editor)
                }
                else  {
                    Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
    private fun signIn(email: String, password: String, editor: SharedPreferences.Editor){

        val purApp = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val signInInfo = SignInBody(email, password)

        purApp.signin(signInInfo).enqueue(object : Callback<SignInRes> {
            override fun onFailure(call: Call<SignInRes>, t: Throwable) {
                if(t.cause is ConnectException){
                    Toast.makeText(
                        this@RegisterActivity,
                        "Check your connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Something bad happened!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: Call<SignInRes>, response: Response<SignInRes>) {
                if (response.code() == 200) {
                    editor.putString("token", response.body()?.token)
                    editor.commit()

                    val intent = Intent(this@RegisterActivity, CommunityFeedActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )

                } else {
                    Toast.makeText(this@RegisterActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun isValidForm(email: String, name: String, surname: String, password: String):Boolean{

        var isValid = true

        if (name.isEmpty()){
            layName.isErrorEnabled = true
            layName.error = "Name cannot be empty!"
            isValid = false
        }else{
            layName.isErrorEnabled = false
        }
        if (surname.isEmpty()){
            laySurname.isErrorEnabled = true
            laySurname.error = "Surname cannot be empty!"
            isValid = false
        }else{
            laySurname.isErrorEnabled = false
        }
        if (!email.isValidEmail()){
            layMail.isErrorEnabled = true
            layMail.error = "Email address is wrong!"
            isValid = false
        }else{
            layMail.isErrorEnabled = false
        }
        if (password.isEmpty()){
            layPassword.isErrorEnabled = true
            layPassword.error = "Password cannot be empty!"
            isValid = false
        }
        else if(password.length < 8){
            layPassword.isErrorEnabled = true
            layPassword.error = "Password length must be longer than 8."
            isValid = false
        }
        else{
            layPassword.isErrorEnabled = false
        }
        return isValid
    }
    private fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}
