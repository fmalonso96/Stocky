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
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val firestoreRepository = FirestoreRepository()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _sales = MutableLiveData<List<Sale>>()
    val sales: LiveData<List<Sale>> = _sales

    private val _selectedProductsQty = MutableLiveData<Map<String, Int>>()
    val selectedProductsQty: LiveData<Map<String, Int>> = _selectedProductsQty

    private val _selectedNoCost = MutableLiveData<List<String>>()
    val selectedNoCost: LiveData<List<String>> = _selectedNoCost

    private val _confirmedProducts = MutableLiveData<List<Product>>()
    val confirmedProducts: LiveData<List<Product>> = _confirmedProducts

    var currentProduct by mutableStateOf(Product())

    init {
        viewModelScope.launch {
            loadProducts()
            loadSales()
        }
    }

    private fun loadProducts() {
        firestoreRepository.getProducts { _products.postValue(it) }
    }

    private fun loadSales() {
        firestoreRepository.getSales { _sales.postValue(it) }
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
            firestoreRepository.addSale(data) { loadSales() }
        }
    }
}