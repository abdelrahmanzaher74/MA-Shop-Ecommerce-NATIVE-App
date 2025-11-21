package com.example.mashop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mashop.LoginActivity
import com.example.mashop.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ... (باقي الاستيرادات)
private lateinit var binding: ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    // ... (باقي الكود)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadUserProfileData()

        // 🟢 التصحيح هنا: استبدال CartActivity بـ MyOrdersActivity
        binding.myOrder.setOnClickListener {
            startActivity(Intent(this, MyOrdersActivity::class.java))
        }

        binding.myAdress.setOnClickListener { /* go to AddressActivity */ }
        binding.Settings.setOnClickListener { /* go to SettingsActivity */ }
        binding.backBtn.setOnClickListener { finish() }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    // ... (باقي الدوال)
}

// جزء من ProfileActivity.kt

private fun loadUserProfileData() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val userId = currentUser.uid

        db.collection("users") // اسم المجموعة: "users"
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {

                    val userName = document.getString("username") // الحقل "username"
                    val userEmail = document.getString("email")   // الحقل "email"

                    if (userName != null) {
                        binding.userName.text = userName
                    }

                    if (userEmail != null) {
                        // تحديث الـ userEmail في شاشة البروفايل
                        binding.userEmail.text = userEmail
                        Log.d("ProfileData", "User Email Loaded: $userEmail")
                    } else {
                        Log.e("ProfileData", "Email field not found in document.")
                    }
                } else {
                    Log.e("ProfileData", "User document not found.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileData", "Failed to load user data: $exception")
            }
    }
}