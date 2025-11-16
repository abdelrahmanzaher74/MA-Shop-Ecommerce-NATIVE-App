package com.example.mashop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mashop.Adapter.CartAdapter
import com.example.mashop.Helper.ChangeNumberItemsListener
import com.example.mashop.Helper.ManagmentCart
import com.example.mashop.databinding.ActivityCartBinding
class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        setVariable()
        initCartList()
        calculateCart()

    }

    // جزء من CartActivity.kt

    private fun handleCheckout() {
        // 1. جلب الإجمالي النهائي الذي تم حسابه في calculateCart()
        // نستخدم الـ totalTxt للقراءة منه، ونزيل علامة الدولار ($)
        val totalText = binding.totalTxt.text.toString().replace("$", "")
        val totalAmount = totalText.toDoubleOrNull()

        val cartItems = managmentCart.getListCart()

        // 2. التحقق من السلة والإجمالي
        if (cartItems.isEmpty() || totalAmount == null || totalAmount <= 0) {
            Toast.makeText(this, "Your cart is empty or total is 0.", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. الانتقال إلى CheckoutActivity وتمرير الإجمالي
        val intent = Intent(this, CheckoutActivity::class.java).apply {
            putExtra("TOTAL_AMOUNT", totalAmount) // تمرير الإجمالي كـ Double
        }
        startActivity(intent)
    }



    private fun initCartList() {
        binding.viewCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.viewCart.adapter =
            CartAdapter(managmentCart.getListCart(), this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                }
            })
        with(binding) {
            emptyTxt.visibility =
                if (managmentCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility =
                if (managmentCart.getListCart().isEmpty()) View.GONE else View.VISIBLE
        }
    }
    private fun calculateCart(){
        val percentTax = 0.02
        val delivery = 10.0
        tax = Math.round((managmentCart.getTotalFee() * percentTax) * 100) /100.0
        val total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) /100
        val itemTotal = Math.round(managmentCart.getTotalFee() * 100) /100

        with(binding){
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliferyTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }
    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
        binding.checkOutBtn.setOnClickListener {
            handleCheckout()
        }
    }
}