package com.example.mashop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mashop.Adapter.BrandAdapter
import com.example.mashop.Adapter.PopularAdapter
import com.example.mashop.Adapter.SliderAdapter
import com.example.mashop.Model.SliderModel
import com.example.mashop.ViewModel.MainViewModel
import com.example.mashop.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initBrand()
        initPopular()
        initButtonmenu()
        loadUserName()



    binding.profileBtn.setOnClickListener {
        startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
    }

    }


    private fun loadUserName() {
        // 1. تهيئة Firebase
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        // 2. الحصول على مُعرّف المستخدم الحالي
        val currentUser = auth.currentUser

        // التأكد من أن المستخدم مسجل الدخول
        if (currentUser != null) {
            val userId = currentUser.uid

            // 3. الوصول إلى مستند المستخدم في مجموعة "Users"
            db.collection("users") // افترض أن اسم المجموعة هو "Users"
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    // 4. فحص المستند واستخراج الاسم
                    if (document != null && document.exists()) {
                        // افترض أن الحقل في Firebase اسمه "name"
                        val userName = document.getString("username")

                        if (userName != null) {
                            // 5. وضع الاسم في الـ TextView (باستخدام View Binding)
                            // افترض أن اسم الـ TextView هو `userNameTextView`
                            binding.userName.text = userName
                        } else {
                            Log.e("Firebase", "حقل الاسم غير موجود في المستند")
                        }
                    } else {
                        Log.e("Firebase", "مستند المستخدم غير موجود")
                    }
                }
                .addOnFailureListener { exception ->
                    // التعامل مع أي أخطاء تحدث أثناء الاتصال بقاعدة البيانات
                    Log.e("Firebase", "فشل قراءة بيانات المستخدم: $exception")
                }
        } else {
            // إذا لم يكن هناك مستخدم مسجل الدخول، قم بتوجيهه إلى صفحة تسجيل الدخول
            // startActivity(Intent(this, LoginActivity::class.java))
            // finish()
        }
    }



    private fun initButtonmenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(
                Intent(
                this@MainActivity, CartActivity::class.java
                )
            )
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this,{ items->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE

        })
        viewModel.loadBanners()
    }
    private fun banners(images:List<SliderModel>){
        binding.viewpageSlider.adapter = SliderAdapter(images, binding.viewpageSlider)
        binding.viewpageSlider.clipToPadding = false
        binding.viewpageSlider.clipChildren = false
        binding.viewpageSlider.offscreenPageLimit = 3
        binding.viewpageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpageSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1){
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpageSlider)
        }
    }
    private fun initBrand() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer{
            binding.viewBrand.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter = BrandAdapter(it)
            binding.progressBarBrand.visibility = View.GONE

        })
        viewModel.loadBrands()
    }
    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.popular.observe(this, Observer{
            binding.viewPopular.layoutManager = GridLayoutManager(this@MainActivity,2)
            binding.viewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadPopular()
    }
}