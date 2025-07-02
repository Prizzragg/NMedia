package ru.netology.nmedia.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.scale
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {


    private val action = "action"
    private val content = "content"
    private val gson = Gson()
    private val channelIdLike = "remote"
    private val channelIdNewPost = "create"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameLike = getString(R.string.channel_remote_name)
            val descriptionTextLike = getString(R.string.channel_remote_description)
            val importanceLike = NotificationManager.IMPORTANCE_DEFAULT
            val channelLike = NotificationChannel(channelIdLike, nameLike, importanceLike).apply {
                description = descriptionTextLike
            }
            val nameNewPost = getString(R.string.channel_create_name)
            val descriptionTextNewPost = getString(R.string.channel_create_description)
            val importanceNewPost = NotificationManager.IMPORTANCE_DEFAULT
            val channelNewPost =
                NotificationChannel(channelIdNewPost, nameNewPost, importanceNewPost).apply {
                    description = descriptionTextNewPost
                }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channelLike)
            manager.createNotificationChannel(channelNewPost)
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data[action]?.let {
            try {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(
                        gson.fromJson(message.data[content], ActionLike::class.java)
                    )

                    Action.NEW_POST -> handleCreateNewPost(
                        gson.fromJson(message.data[content], ActionNewPost::class.java)
                    )
                }
            } catch (exception: IllegalArgumentException) {
                return@let
            }
        }
    }

    private fun handleLike(like: ActionLike) {
        val notification = NotificationCompat.Builder(this, channelIdLike)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    like.userName,
                    like.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleCreateNewPost(newPost: ActionNewPost) {
        val notification = NotificationCompat.Builder(this, channelIdNewPost)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_create_new_post,
                    newPost.postAuthor,
                )
            )
            .setLargeIcon(
                BitmapFactory.decodeResource(resources, R.drawable.user_avatar)
                    .scale(128, 128, false)
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(newPost.textPost)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    enum class Action {
        LIKE,
        NEW_POST
    }

    data class ActionLike(
        val userId: Int,
        val userName: String,
        val postId: Int,
        val postAuthor: String
    )

    data class ActionNewPost(
        val textPost: String,
        val userId: Int,
        val postAuthor: String,
        val postId: Int
    )

}