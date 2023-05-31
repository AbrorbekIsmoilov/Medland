package uz.crud.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val _id: Int? = null,
    val id: String? = null,
    val userDoctor: Int,
    val userId: String? = null,
    val clientId: String? = null,
    var name: String? = null,
    val type: Int,
    val url: String? = null,
    var downloadedUri: String? = null,
    var isDownloading: Boolean = false,
    val date: String? = null
)
