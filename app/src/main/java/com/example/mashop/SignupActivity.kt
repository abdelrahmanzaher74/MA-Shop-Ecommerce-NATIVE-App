package com.example.mashop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mashop.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Firebase instances
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // تخزين البيانات لما ندوس علي زرار التسجيل
        binding.nextBtn.setOnClickListener {
            val name = binding.nameEt.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val confirmPass = binding.confirmPassEt.text.toString().trim()

            //بنتاكد ان مفيش اي خانه فاضيه والباسورد شبه التاكيد بتاعه
            when {
                name.isEmpty() -> binding.nameLy.error = "Enter your name"
                email.isEmpty() -> binding.emailLy.error = "Enter your email"
                pass.isEmpty() -> binding.passLy.error = "Enter your password"
                pass != confirmPass -> binding.passLy.error = "Passwords should match"
                else -> registerUser(name, email, pass)
            }
        }
    }
   // فانكشن بتاخد البيانات بتاعت المستخدم عشان تخزنها في فاير بيز
    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser == null) {
                        Toast.makeText(this, "User creation failed.", Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }

                    val uid = firebaseUser.uid
                    val userMap = hashMapOf(
                        "username" to name,
                        "email" to email,
                        "role" to "user"
                    )

                    // حفظ البيانات في Firestore
                    db.collection("users").document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            // ايميل التاكيد
                            firebaseUser.sendEmailVerification().addOnCompleteListener { verifyTask ->
                                if (verifyTask.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Verification email sent to $email",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Failed to send verification email: ${verifyTask.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Failed to save user data: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                } else {
                    Toast.makeText(
                        this,
                        "Signup failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
