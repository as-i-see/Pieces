package com.asisee.streetpieces.model.service.module

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.asisee.streetpieces")
class AppModule {
    @Single
    fun firebaseAuth() = Firebase.auth
    @Single
    fun firebaseStorage() = Firebase.storage
    @Single
    fun firebaseFirestore() = Firebase.firestore
}