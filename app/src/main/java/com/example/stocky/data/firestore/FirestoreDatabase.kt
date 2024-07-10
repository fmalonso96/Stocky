package com.example.stocky.data.firestore

import android.annotation.SuppressLint
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object FirestoreDatabase {

    @SuppressLint("StaticFieldLeak")
    private var db = Firebase.firestore

    fun getInstance(): FirebaseFirestore {
        return db
    }
}