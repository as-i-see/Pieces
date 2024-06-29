package com.asisee.streetpieces.module

//
//@Module
//@TestInstallIn(components = [SingletonComponent::class], replaces = [FirebaseModule::class])
//object TestFirebaseModule {
//  private const val HOST = "10.0.2.2"
//  private const val AUTH_PORT = 9099
//  private const val FIRESTORE_PORT = 8080
//  @Provides fun auth(): FirebaseAuth = Firebase.auth.also { it.useEmulator(HOST, AUTH_PORT) }
//
//  @Provides
//  fun firestore(): FirebaseFirestore =
//    Firebase.firestore.also { it.useEmulator(HOST, FIRESTORE_PORT) }
//}
