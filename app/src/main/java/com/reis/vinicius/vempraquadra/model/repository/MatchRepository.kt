package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.adapter.CourtDropdownListAdapter
import com.reis.vinicius.vempraquadra.model.dto.MatchWithCourt
import com.reis.vinicius.vempraquadra.model.firestore.ChatFirestore
import com.reis.vinicius.vempraquadra.model.firestore.MatchFirestore
import com.reis.vinicius.vempraquadra.model.entity.Match
import com.reis.vinicius.vempraquadra.model.firestore.CourtFirestore
import kotlinx.coroutines.tasks.await

class MatchRepository(application: Application): FirestoreRepository<Match>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(Repository.Collections.Match)

    override suspend fun getAll(): List<Match> =
        collection.get(getSource()).await()
            .map { it.toObject<MatchFirestore>().toEntity(it.id) }

    override suspend fun getById(id: String): Match =
        collection.document(id).get(getSource()).await().let { documentSnapshot ->
            documentSnapshot.toObject<MatchFirestore>()?.toEntity(documentSnapshot.id)
                ?: throw Exception("Failed to get match with id $id")
        }

    override suspend fun insert(obj: Match) {
        db.runTransaction { transaction ->
            val newMatch = collection.document()
            val newChat = db.collection(Repository.Collections.Chat).document()

            transaction
                .set(newMatch, MatchFirestore.fromEntity(obj))
                .set(newChat, ChatFirestore(newMatch.id))
        }.await()
    }

    override suspend fun update(obj: Match) {
        collection.document(obj.id).get(getSource()).await()
            .reference.set(MatchFirestore.fromEntity(obj))
    }

    override suspend fun delete(obj: Match): Boolean =
        collection.document(obj.id).get(getSource()).await().reference.delete().isSuccessful

    suspend fun editAttendance(userId: String, attend: Boolean){

    }

    suspend fun getFullMatchById(id: String): MatchWithCourt {
        lateinit var result: MatchWithCourt

        db.runTransaction { transaction ->
            val matchDoc = collection.document(id)
            val match = transaction.get(matchDoc).toObject(MatchFirestore::class.java)

            match?.let {
                val courtDoc = db.collection(Repository.Collections.Court)
                    .whereEqualTo(CourtFirestore.Fields.id, it.courtId).get().result.first().reference

                val court = transaction.get(courtDoc).toObject(CourtFirestore::class.java)
                    ?:

                result = MatchWithCourt(match.toEntity(matchDoc.id), court.toEntity())
            }
        }.await()


    }
}