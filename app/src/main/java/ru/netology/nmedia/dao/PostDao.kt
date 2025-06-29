package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    fun save(post: PostEntity) {
        if (post.id == 0L) {
            insert(post)
        } else {
            updateById(post.id, post.content)
        }
    }

    @Query("UPDATE postEntity SET content=:content WHERE id=:id")
    fun updateById(id: Long, content: String)

    @Insert
    fun insert(post: PostEntity)

    @Query(
        """
           UPDATE postEntity SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id =:id;
        """
    )
    fun likeById(id: Long)

    @Query("DELETE FROM postEntity WHERE id=:id")
    fun removeById(id: Long)

    @Query(
        """
           UPDATE postEntity SET
               reposts = reposts + 1
           WHERE id =:id;
        """
    )
    fun repostById(id: Long)
}