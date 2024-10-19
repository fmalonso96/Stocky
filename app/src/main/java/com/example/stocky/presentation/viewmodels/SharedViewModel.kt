package com.example.stocky.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocky.data.firestore.FirestoreRepository
import com.example.stocky.model.product.Product
import com.example.stocky.model.sale.Sale
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val firestoreRepository = FirestoreRepository()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _productMetrics = MutableLiveData<Map<String, List<Pair<Product, Int>>>>()
    val productMetrics: LiveData<Map<String, List<Pair<Product, Int>>>> = _productMetrics

    private val _salesOfTheDay = MutableLiveData<List<Sale>>()
    val salesOfTheDay: LiveData<List<Sale>> = _salesOfTheDay

    private val _sales = MutableLiveData<List<Sale>>()
    val sales: LiveData<List<Sale>> = _sales

    private val _selectedProductsQty = MutableLiveData<Map<String, Int>>()
    val selectedProductsQty: LiveData<Map<String, Int>> = _selectedProductsQty

    private val _selectedNoCost = MutableLiveData<List<String>>()
    val selectedNoCost: LiveData<List<String>> = _selectedNoCost

    private val _confirmedProducts = MutableLiveData<List<Product>>()
    val confirmedProducts: LiveData<List<Product>> = _confirmedProducts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    var currentProduct by mutableStateOf(Product())

    init {
        viewModelScope.launch {
            loadProducts()
            loadSalesOfTheDay()
        }
    }

    private fun loadProducts() {
        firestoreRepository.getProducts { _products.postValue(it) }
    }

    private fun loadSalesOfTheDay() {
        firestoreRepository.getSalesOfTheDay { _salesOfTheDay.postValue(it) }
    }

    fun addProduct(data: Product) {
        viewModelScope.launch {
            firestoreRepository.addProduct(data) { loadProducts() }
        }
    }

    fun updateProduct(data: Product) {
        viewModelScope.launch {
            firestoreRepository.updateProduct(data) { loadProducts() }
        }
    }

    fun setSelectedProductsQty(id: String, amount: Int) {
        val currentMap = _selectedProductsQty.value.orEmpty().toMutableMap()
        currentMap[id] = amount
        _selectedProductsQty.value = currentMap
    }

    fun resetSelectedProducts() {
        _selectedProductsQty.value = emptyMap()
        _selectedNoCost.value = emptyList()
    }

    fun setSelectedNoCost(id: String) {
        val currentList = _selectedNoCost.value.orEmpty().toMutableList()
        if (currentList.contains(id)) {
            currentList.remove(id)
        } else {
            currentList.add(id)
        }
        _selectedNoCost.value = currentList
    }

    fun setConfirmedProducts(list: List<Product>) {
        _confirmedProducts.value = list
    }

    fun resetConfirmedProducts() {
        _confirmedProducts.value = emptyList()
    }

    fun addSale(data: Sale) {
        viewModelScope.launch {
            firestoreRepository.addSale(data) { loadSalesOfTheDay() }
        }
    }

    fun loadSales() {
        _isLoading.value = true
        firestoreRepository.getSales {
            _sales.postValue(it)
            _isLoading.postValue(false)
        }
    }

    //cambiar la estructura de Map horrenda con una list de Pair.
    fun setSaleMetricsMap() {
        val totalSales = sales.value.orEmpty()

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val endDate = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, -8)
        val startDate = calendar.time

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val filteredSales = totalSales.filter { sale ->
            try {
                val saleDate = formatter.parse(sale.date)
                saleDate != null && saleDate.after(startDate) && saleDate.before(endDate)
            } catch (e: ParseException) {
                false
            }
        }

        val productCounts = mutableMapOf<String, Int>()

        filteredSales.forEach { sale ->
            sale.products.forEach { product ->
                productCounts[product.id] = productCounts.getOrDefault(product.id, 0) + 1
            }
        }

        val finalProductList = mutableListOf<Pair<Product, Int>>()

        filteredSales.flatMap { it.products }.distinctBy { it.id }.forEach { product ->
            finalProductList.add(Pair(product, productCounts[product.id] ?: 0))
        }

        val productsByCategory: Map<String, List<Pair<Product, Int>>> = finalProductList
            .groupBy({ it.first.category }, { it })

        _productMetrics.postValue(productsByCategory)
    }
}