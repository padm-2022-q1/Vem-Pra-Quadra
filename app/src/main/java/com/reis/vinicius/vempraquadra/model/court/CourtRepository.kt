package com.reis.vinicius.vempraquadra.model.court

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.reis.vcom.reis.vinicius.vempraquadra.model.courtinicius.vempraquadra.model.FirebaseRepository
import com.reis.vinicius.vempraquadra.model.RepositoryFactory
import kotlinx.coroutines.tasks.await
import kotlin.concurrent.timerTask

class CourtRepository(application: Application): FirebaseRepository<Court>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(RepositoryFactory.Collections.Court)

    companion object {
        private const val nextIdDoc = "courtId"
    }

    override suspend fun getAll(): List<Court> =
        collection.whereGreaterThan(CourtFirestore.Fields.id, 0).get(getSource()).await()
            .toObjects(CourtFirestore::class.java).map { it.toEntity() }

    override suspend fun getById(id: String): Court =
        collection.whereEqualTo(CourtFirestore.Fields.id, id).get(getSource()).await()
            .toObjects(CourtFirestore::class.java).first().toEntity()

    override suspend fun insert(obj: Court) {
        collection.add(CourtFirestore.fromEntity(obj))
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

    private suspend fun nextId(): Long =
        collection.document(nextIdDoc).get(getSource()).await().let { documentSnapshot ->  
            if (documentSnapshot.exists()) {
                val oldValue = documentSnapshot.toObject(LongId::class.java)?.value
                    ?: throw Exception("Failed to retrieve last id value")
                LongId(oldValue + 1)
            } else {
                LongId(1)
            }.let { newTaskId ->
                documentSnapshot.reference.set(newTaskId)
                newTaskId.value ?: throw Exception("Nww id should not be null")
            }
        }
}