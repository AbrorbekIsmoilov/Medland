package uz.crud.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import uz.crud.data.model.ChatData

@Dao
interface ChatDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAllChat(chats: List<ChatData>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(chat: ChatData)

    @Query("UPDATE chat SET downloadedUri = :downloadedUri WHERE id = :id")
    suspend fun update(downloadedUri: String, id: String)

    @Query("SELECT * FROM chat")
    suspend fun getAllChats(): List<ChatData>

    @Query("DELETE  FROM chat")
    suspend fun deleteAllChats()

    @Query(value = "SELECT downloadedUri FROM chat WHERE id = :id")
    suspend fun findDownloadUriById(id: String?): String?
}