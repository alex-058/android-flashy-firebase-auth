package org.othr.flashyplayground.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import org.othr.flashyplayground.R
import org.othr.flashyplayground.databinding.ActivityRegisterBinding
// Firebase imports
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.othr.flashyplayground.activities.FlashcardTopicListActivity


class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            // check if user has entered something
            when {
                // cut out empty spaces and check on input
                TextUtils.isEmpty(binding.emailFieldText.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        R.string.message_enterEmail,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(binding.passwordFieldText.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        R.string.message_enterPassword,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // if user has entered something in e-mail and password
                else -> {
                    // get rid of accidentally having empty space in input
                    val email: String = binding.emailFieldText.text.toString().trim { it <= ' '}
                    val password: String = binding.passwordFieldText.text.toString().trim { it <= ' '}

                    // Create firebase instance and register a user with e-mail and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                // If registration was successful
                                if (task.isSuccessful) {

                                    // Firebase registered user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You were registered successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    /**
                                     * Event handling for "logging in" user in Flashy App
                                     * Send back to splash screen and show / update log status
                                     * More interesting for login activity I guess
                                     */

                                    val intent = Intent(this, FlashcardTopicListActivity::class.java)
                                    // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                }

                                else {
                                    // If registering was not successful
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }

            }
        }

        binding.textViewLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}