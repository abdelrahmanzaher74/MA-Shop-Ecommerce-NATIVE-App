package  com.example.mashop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mashop.Model.BrandModel
import com.example.mashop.Model.ItemsModel
import com.example.mashop.Model.SliderModel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class MainViewModel():ViewModel() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _brand = MutableLiveData<MutableList<BrandModel>>()
    private val _popular = MutableLiveData<MutableList<ItemsModel>>()
    val brands: LiveData<MutableList<BrandModel>> = _brand
    val popular: LiveData<MutableList<ItemsModel>> = _popular
    val banners: LiveData<List<SliderModel>> = _banner
    private var bannersListener: ValueEventListener? = null
    private var brandsListener: ValueEventListener? = null
    private var popularListener: ValueEventListener? = null
    fun loadBanners(){
        val Ref = firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null){
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun loadBrands(){
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<BrandModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(BrandModel::class.java)
                    if (list != null){
                        lists.add(list)
                    }
                }
                _brand.value = lists
            }
            override fun onCancelled(error: DatabaseError) {
             }

        })
    }
    fun loadPopular(){
        val Ref = firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null){
                        lists.add(list)
                    }
                }
                _popular.value = lists
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    // في نهاية كلاس MainViewModel.kt

    override fun onCleared() {
        super.onCleared()

        // 1. إزالة المستمعين من Firebase Realtime Database
        firebaseDatabase.getReference("Banner").removeEventListener(bannersListener!!)
        firebaseDatabase.getReference("Category").removeEventListener(brandsListener!!)
        firebaseDatabase.getReference("Items").removeEventListener(popularListener!!)

        // 2. تعيين المتغيرات إلى null (اختياري لكن مفضل)
        bannersListener = null
        brandsListener = null
        popularListener = null
    }

}

