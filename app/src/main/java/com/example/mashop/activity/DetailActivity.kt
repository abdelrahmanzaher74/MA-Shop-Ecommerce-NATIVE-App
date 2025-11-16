package com.example.mashop.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mashop.Adapter.ColorAdapter
import com.example.mashop.Adapter.SizeAdapter
import com.example.mashop.Adapter.SliderAdapter
import com.example.mashop.Helper.ManagmentCart
import com.example.mashop.Model.ItemsModel
import com.example.mashop.Model.SliderModel
import com.example.mashop.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOder = 1
    private lateinit var managementCart: ManagmentCart

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        managementCart = ManagmentCart(this)

        getBundle()
        initLists()
        banners()
    }
    private fun initLists() {
        val sizeList = ArrayList<String>()
        for (size in item.size){
            sizeList.add(size.toString())
        }
        binding.sizeList.adapter = SizeAdapter(sizeList)
        binding.sizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl){
            colorList.add(imageUrl)
        }
        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun banners() {
        val sliderItems = ArrayList<SliderModel>()
        for (imageUrl in item.picUrl) {
            Log.d("BANNERS", "Image URL: $imageUrl")
            sliderItems.add(SliderModel(imageUrl))
        }
        binding.Slider.adapter = SliderAdapter(sliderItems, binding.Slider)
        binding.Slider.clipToPadding = true
        binding.Slider.clipChildren = true
        binding.Slider.offscreenPageLimit = 1

        if (sliderItems.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.Slider)
        }else {
            binding.dotIndicator.visibility = View.GONE
        }
    }
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private fun getBundle() {
                item = intent.getParcelableExtra("object",ItemsModel::class.java)?: return
//              item = intent.getParcelableExtra("object") ?: return


            binding.titleTxt.text = item.title
            binding.descriptionTxt.text = item.description
            binding.priceTxt.text = "$" + item.price
            binding.ratingTxt.text = "${item.rating} Rating"
            binding.addToCartBtn.setOnClickListener {
                item.numberInCart = numberOder
                managementCart.insertFood(item)
            }
            binding.backBtn.setOnClickListener { finish() }
            binding.cartBtn1.setOnClickListener {
                startActivity(Intent(this@DetailActivity, CartActivity::class.java))
            }
        }
}
