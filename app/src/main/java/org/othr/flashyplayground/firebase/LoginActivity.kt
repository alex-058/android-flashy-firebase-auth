package org.othr.flashyplayground.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityLoginBinding
import org.othr.flashyplayground.activities.SplashScreenActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            when {
                // cut out empty spaces and check on input
                TextUtils.isEmpty(binding.emailFieldText.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.message_enterEmail,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(binding.passwordFieldText.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.message_enterPassword,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // if user has entered something in e-mail and password
                else -> {
                    // get rid of accidentally having empty space in input
                    val email: String = binding.emailFieldText.text.toString().trim { it <= ' '}
                    val password: String = binding.passwordFieldText.text.toString().trim { it <= ' '}

                    // Create firebase instance and login user
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // If login was successful
                            if (task.isSuccessful) {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "You are logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                /**
                                 * Event handling for "logging in" user in Flashy App
                                 * Send back to splash screen and show / update log status
                                 */

                                val intent = Intent(this@LoginActivity, SplashScreenActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            }

                            else {
                                // If login was not successful
                                Toast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

            }
        }
    }
}