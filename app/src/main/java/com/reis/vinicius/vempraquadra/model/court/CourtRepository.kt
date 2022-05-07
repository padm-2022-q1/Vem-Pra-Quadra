package com.reis.vinicius.vempraquadra.model.court

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.reis.vcom.reis.vinicius.vempraquadra.model.courtinicius.vempraquadra.model.FirebaseRepository
import kotlinx.coroutines.tasks.await

class CourtRepository(application: Application): FirebaseRepository<Court>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(CourtFirestore.CollectionName)

    override suspend fun getAll(): List<Court> =
        collection.get(getSource()).await()
            .toObjects(CourtFirestore::class.java).map { it.toEntity() }

    override suspend fun getById(id: String): Court =
        collection.whereEqualTo(CourtFirestore.Fields.id, id).get(getSource()).await()
            .toObjects(CourtFirestore::class.java).first().toEntity()

    override suspend fun insert(obj: Court): String = CourtFirestore(
        id = 0,
        name = obj.name,
        address = obj.address
    ).let {
        collection.add(it)
        it.id?.toString() ?: throw Exception("Failed to add court")
    }

    override suspend fun update(obj: Court) {
        collection.whereEqualTo(CourtFirestore.Fields.id, obj.id)
            .get(getSource()).await().let{ querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.set(CourtFirestore.fromEntity(obj))
            }
    }

    override suspend fun delete(obj: Court) {
        collection.whereEqualTo(CourtFirestore.Fields.id, obj.id)
            .get(getSource()).await().let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to remove element: Id ${obj.id} not found")

                querySnapshot.first().reference.delete()
            }
    }

    override suspend fun deleteMany(objects: List<Court>?) {
        val results = if (objects == null) collection
        else collection.whereIn(CourtFirestore.Fields.id, objects.map { it.id })

        results.get(getSource()).await().forEach { queryDocumentSnapshot ->
            queryDocumentSnapshot.reference.delete()
        }
    }
}