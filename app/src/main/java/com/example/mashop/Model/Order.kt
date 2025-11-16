package com.example.mashop.Model

data class Order(
    val id: String,
    val userId: String, // **الجديد:** لربط الطلب بالمستخدم
    val date: String,
    val total: Double,
    val status: String,
    val items: List< ItemsModel>, // قائمة المنتجات في الطلب
    val shippingAddress: String, // عنوان الشحن
    val paymentMethod: String // طريقة الد
)
