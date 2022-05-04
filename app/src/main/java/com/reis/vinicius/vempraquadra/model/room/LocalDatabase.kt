package com.reis.vinicius.vempraquadra.model.room

import android.location.Address
import androidx.room.Database
import androidx.room.RoomDatabase
import com.reis.vinicius.vempraquadra.model.data.dao.*
import com.reis.vinicius.vempraquadra.model.data.entity.*
import com.reis.vinicius.vempraquadra.model.data.junctions.UsersMatches

@Database(
    entities = [
        Address::class,
        Court::class,
        Chat::class,
        ChatMessage::class,
        Match::class,
        Post::class,
        User::class,
        UsersMatches::class,     
    ],
    version = 1,
    exportSchema = false,
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun addressDao(): AddressDao
    abstract fun courtDao(): CourtDao
    abstract fun chatDao(): ChatDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun matchDao(): MatchDao
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun usersMatchesDao(): UsersMatchesDao
}