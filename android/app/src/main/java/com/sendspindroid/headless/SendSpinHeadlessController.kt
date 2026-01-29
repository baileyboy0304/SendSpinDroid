package com.sendspindroid.headless

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.sendspindroid.playback.PlaybackService
import com.sendspindroid.sendspin.protocol.SendSpinProtocol

/**
 * Lightweight wrapper for controlling PlaybackService without UI dependencies.
 *
 * Initialize once (typically from your Application or Activity) and call connect/disconnect
 * to manage SendSpin playback in a headless way.
 */
class SendSpinHeadlessController(private val context: Context) {

    companion object {
        private const val TAG = "SendSpinHeadlessController"
    }

    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    /**
     * Initialize the MediaController connection to PlaybackService.
     */
    fun initialize(onReady: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        if (mediaController != null) {
            onReady?.invoke()
            return
        }

        val sessionToken = SessionToken(
            context,
            ComponentName(context, PlaybackService::class.java)
        )

        mediaControllerFuture = MediaController.Builder(context, sessionToken)
            .buildAsync()

        mediaControllerFuture?.addListener(
            {
                try {
                    mediaController = mediaControllerFuture?.get()
                    Log.d(TAG, "MediaController connected to PlaybackService")
                    onReady?.invoke()
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to connect MediaController", e)
                    onError?.invoke(e)
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    /**
     * Connect to a SendSpin server on the local network.
     */
    fun connectLocal(address: String, path: String = SendSpinProtocol.ENDPOINT_PATH) {
        val controller = mediaController
        if (controller == null) {
            Log.w(TAG, "MediaController not ready; call initialize() first.")
            return
        }

        val args = Bundle().apply {
            putString(PlaybackService.ARG_SERVER_ADDRESS, address)
            putString(PlaybackService.ARG_SERVER_PATH, path)
        }
        val command = SessionCommand(PlaybackService.COMMAND_CONNECT, Bundle.EMPTY)
        controller.sendCustomCommand(command, args)
        Log.d(TAG, "Sent connect command to $address path=$path")
    }

    /**
     * Connect to a SendSpin server using Music Assistant Remote Access.
     */
    fun connectRemote(remoteId: String) {
        val controller = mediaController
        if (controller == null) {
            Log.w(TAG, "MediaController not ready; call initialize() first.")
            return
        }

        val args = Bundle().apply {
            putString(PlaybackService.ARG_REMOTE_ID, remoteId)
        }
        val command = SessionCommand(PlaybackService.COMMAND_CONNECT_REMOTE, Bundle.EMPTY)
        controller.sendCustomCommand(command, args)
        Log.d(TAG, "Sent remote connect command for remoteId=$remoteId")
    }

    /**
     * Connect to a SendSpin server using an authenticated proxy.
     */
    fun connectProxy(url: String, authToken: String) {
        val controller = mediaController
        if (controller == null) {
            Log.w(TAG, "MediaController not ready; call initialize() first.")
            return
        }

        val args = Bundle().apply {
            putString(PlaybackService.ARG_PROXY_URL, url)
            putString(PlaybackService.ARG_AUTH_TOKEN, authToken)
        }
        val command = SessionCommand(PlaybackService.COMMAND_CONNECT_PROXY, Bundle.EMPTY)
        controller.sendCustomCommand(command, args)
        Log.d(TAG, "Sent proxy connect command for url=$url")
    }

    /**
     * Disconnect from the current SendSpin session.
     */
    fun disconnect() {
        val controller = mediaController
        if (controller == null) {
            Log.w(TAG, "MediaController not ready; call initialize() first.")
            return
        }

        val command = SessionCommand(PlaybackService.COMMAND_DISCONNECT, Bundle.EMPTY)
        controller.sendCustomCommand(command, Bundle.EMPTY)
        Log.d(TAG, "Sent disconnect command")
    }

    /**
     * Release resources and disconnect the MediaController.
     */
    fun release() {
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
        mediaControllerFuture = null
        mediaController = null
    }
}
