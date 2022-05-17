package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.reis.vinicius.vempraquadra.model.dto.ChatWithMessages
import com.reis.vinicius.vempraquadra.model.dto.MessageWithUserData
import com.reis.vinicius.vempraquadra.model.entity.Chat
import com.reis.vinicius.vempraquadra.model.entity.Message
import com.reis.vinicius.vempraquadra.model.firestore.ChatFirestore
import com.reis.vinicius.vempraquadra.model.firestore.MatchFirestore
import com.reis.vinicius.vempraquadra.model.firestore.MessageFirestore
import com.reis.vinicius.vempraquadra.model.firestore.UserDataFirestore
import kotlinx.coroutines.tasks.await
import java.lang.reflect.Executable

class ChatRepository(application: Application): FirestoreRepository<Chat>(application) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth = Firebase.auth
    private val chatCollection = db.collection(Repository.Collections.Chat)
    private val messageCollection = db.collection(Repository.Collections.ChatMessage)
    private val userDataCollection = db.collection(Repository.Collections.UserData)
    private val matchRepository = RepositoryFactory(application)
        .create(Repository.Type.Match) as MatchRepository

    override suspend fun getAll(): List<Chat> {
        val matches = db.collection(Repository.Collections.Match)
            .get(getSource()).await()
            .map { it.toObject(MatchFirestore::class.java).toEntity(it.id) }

        return chatCollection.whereIn(ChatFirestore.Fields.matchId, matches.map { it.id })
            .get(getSource()).await()
            .map { query ->
                query.toObject(ChatFirestore::class.java).let { chat ->
                    chat.toEntity(
                        query.id,
                        matches.firstOrNull { it.id == chat.matchId },
                        getLastMessage(query.id)
                    )
                }
            }
    }

    override suspend fun getById(id: String): Chat =
        chatCollection.document(id).get(getSource()).await().let { documentSnapshot ->
            val chatFirestore = documentSnapshot.toObject(ChatFirestore::class.java)
                ?: throw Exception("Failed to parse chat with id $id")
            val match = matchRepository.getById(chatFirestore.matchId ?: "")

            chatFirestore.toEntity(documentSnapshot.id, match, getLastMessage(documentSnapshot.id))
        }

    override suspend fun insert(obj: Chat) {}

    override suspend fun update(obj: Chat) {}

    override suspend fun delete(obj: Chat): Boolean =
        chatCollection.document(obj.id).get(getSource()).await().reference.delete().isSuccessful

    suspend fun getAllByUser(userId: String): List<Chat> {
        val matches = db.collection(Repository.Collections.Match)
            .whereArrayContains(MatchFirestore.Fields.usersIds, userId).get(getSource()).await()
            .map { it.toObject(MatchFirestore::class.java).toEntity(it.id) }
        if (matches.isEmpty()) return emptyList()

        return chatCollection.whereIn(ChatFirestore.Fields.matchId, matches.map { it.id })
            .get(getSource()).await().map {
            it.toObject(ChatFirestore::class.java).let { chat ->
                chat.toEntity(
                    it.id,
                    matches.firstOrNull { match -> match.id == chat.matchId },
                    getLastMessage(it.id)
                )
            }
        }
    }

    suspend fun getWithMessagesById(id: String): ChatWithMessages =
        chatCollection.document(id).get(getSource()).await().let { documentSnapshot ->
            val chatFirestore = documentSnapshot.toObject(ChatFirestore::class.java)
                ?: throw Exception("Failed to parse chat with id $id")
            val match = matchRepository.getById(chatFirestore.matchId ?: "")
            val messages = getMessages(documentSnapshot.id)
            val chat = chatFirestore.toEntity(documentSnapshot.id, match, messages.lastOrNull()?.message)

            ChatWithMessages(chat, messages)
        }

    suspend fun saveMessage(message: Message): MessageWithUserData {
        messageCollection.add(MessageFirestore.fromEntity(message)).await()

        val userData = userDataCollection.document(message.sentById).get(getSource()).await().let {
            it.toObject(UserDataFirestore::class.java)?.toEntity(it.id)
                ?: throw Exception("Failed to get user date from id ${message.sentById}")
        }

        return MessageWithUserData(message, userData)
    }

    private suspend fun getMessages(chatId: String): List<MessageWithUserData> {
        val messages = messageCollection
            .whereEqualTo(MessageFirestore.Fields.chatId, chatId)
            .orderBy(MessageFirestore.Fields.sentIn, Query.Direction.ASCENDING)
            .get(getSource()).await()
            .map { it.toObject(MessageFirestore::class.java).toEntity(it.id) }

        val usersQuery = if (messages.isEmpty()) userDataCollection
            else userDataCollection.whereIn(FieldPath.documentId(), messages.map { it.sentById })

        val users = usersQuery.get(getSource()).await()
            .map { it.toObject(UserDataFirestore::class.java).toEntity(it.id) }

        return messages.map { MessageWithUserData(it, users.first { user -> user.id == it.sentById }) }
    }

    private suspend fun getLastMessage(chatId: String): Message? =
        messageCollection
            .whereEqualTo(MessageFirestore.Fields.chatId, chatId)
            .orderBy(MessageFirestore.Fields.sentIn, Query.Direction.DESCENDING)
            .limit(1).get(getSource()).await()
            .map { it.toObject(MessageFirestore::class.java).toEntity(it.id) }.firstOrNull()
}