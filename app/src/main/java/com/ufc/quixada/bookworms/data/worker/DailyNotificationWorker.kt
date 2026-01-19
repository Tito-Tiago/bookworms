package com.ufc.quixada.bookworms.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ufc.quixada.bookworms.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.random.Random

@HiltWorker
class DailyNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, workerParams) {

    private val messages = listOf(
        "Já compartilhou o que achou da sua leitura atual? Alguém pode estar só esperando essa indicação para começar um livro novo!",
        "Desafio do dia: Leia 5 páginas agora e conte para um amigo o que aconteceu. Ler acompanhado é muito melhor!",
        "Sabe aquele livro que mudou sua forma de pensar? Que tal postar um trecho dele hoje e inspirar a galera?",
        "Intervalo é hora de relaxar! Que tal viajar para outro mundo sem sair do pátio? Abra seu livro e aproveite.",
        "Que tal nesse intervalo fazer uma roda de leitura com seus colegas?"
    )

    override suspend fun doWork(): Result {
        val message = messages.random()
        showNotification(message)
        return Result.success()
    }

    private fun showNotification(message: String) {
        val channelId = "daily_motivation_channel"
        val channelName = "Motivação Diária"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações diárias de incentivo à leitura"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle("Bookworms")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}
