package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.dto.MatchWithCourt
import com.reis.vinicius.vempraquadra.model.firestore.ChatFirestore
import com.reis.vinicius.vempraquadra.model.firestore.MatchFirestore
import com.reis.vinicius.vempraquadra.model.entity.Match
import com.reis.vinicius.vempraquadra.model.firestore.CourtFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class MatchRepository(application: Application): FirestoreRepository<Match>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection(Repository.Collections.Match)
    private val courtCollection = db.collection(Repository.Collections.Court)

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

        collection.document(id).get(getSource()).await().toObject(MatchFirestore::class.java)?.let { match ->
            val court = courtCollection
                .whereEqualTo(CourtFirestore.Fields.id, match.courtId).get().await().firstOrNull()
                ?.toObject(CourtFirestore::class.java)?.toEntity()
                ?: throw Exception("Failed to find court with id ${match.courtId}")

            result = MatchWithCourt(match.toEntity(id), court)
        } ?: throw Exception("Failed to find match with id $id")

        return result
    }

    suspend fun changeAttendance(id: String, userId: String, join: Boolean) {
        collection.document(id).get(getSource()).await()?.let { documentSnapshot ->
            val match = documentSnapshot.toObject(MatchFirestore::class.java)
                ?: throw Exception("Failed to parse match with $id")
            var newUsersIds = match.usersIds.toMutableList()

            if (join)
                newUsersIds.add(userId)
            else
                newUsersIds.remove(userId)

            val updatedMatch = MatchFirestore(
                name = match.name ?: "",
                date = match.date ?: Date(),
                courtId = match.courtId,
                usersIds = newUsersIds.toList()
            )

            documentSnapshot.reference.set(updatedMatch)

        } ?: throw Exception("Failed to get match with id $id")
    }

    suspend fun getAllMatchWithCourt(): List<MatchWithCourt> {
        val matches = collection.get(getSource()).await()
            .map { it.toObject(MatchFirestore::class.java).toEntity(it.id)}
        if (matches.isNullOrEmpty()) return emptyList()
        val courts = courtCollection.whereIn(CourtFirestore.Fields.id, matches.map { it.courtId })
            .get(getSource()).await().toObjects(CourtFirestore::class.java).map { it.toEntity() }

        return matches.map { MatchWithCourt(it, courts.first { court -> it.courtId == court.id }) }
    }
}