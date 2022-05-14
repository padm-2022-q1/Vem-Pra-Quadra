package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.firestore.UserDataFirestore
import com.reis.vinicius.vempraquadra.model.entity.UserData
import kotlinx.coroutines.tasks.await

class UserDataRepository(application: Application): FirestoreRepository<UserData>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(UserDataFirestore.CollectionName)

    override suspend fun getAll(): List<UserData> =
        collection.get(getSource()).await()
            .toObjects(UserDataFirestore::class.java).map { it.toEntity() }

    override suspend fun getById(id: String): UserData =
        collection.whereEqualTo(UserDataFirestore.Fields.id, id).get(getSource()).await()
            .toObjects(UserDataFirestore::class.java).first().toEntity()

    override suspend fun insert(obj: UserData){
        collection.add(UserDataFirestore.fromEntity(obj))
    }

    override suspend fun update(obj: UserData) {
        collection.whereEqualTo(UserDataFirestore.Fields.id, obj.id)
            .get(getSource()).await().let{ querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.set(UserDataFirestore.fromEntity(obj))
            }
    }

    override suspend fun delete(obj: UserData) {
        collection.whereEqualTo(UserDataFirestore.Fields.id, obj.id)
            .get(getSource()).await().let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove Match: Id ${obj.id} not found")

                querySnapshot.first().reference.delete()
            }
    }
}