package com.haramblur.app.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.haramblur.app.R
import com.haramblur.app.domain.repository.SettingsRepository
import com.haramblur.app.domain.usecase.detection.ProcessScreenContentUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Foreground service for continuous content monitoring
 */
@AndroidEntryPoint
class ContentMonitoringService : Service() {
    
    @Inject
    lateinit var settingsRepository: SettingsRepository
    
    @Inject
    lateinit var processScreenContentUseCase: ProcessScreenContentUseCase
    
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null
    private var serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private var screenWidth = 720
    private var screenHeight = 1280
    private var screenDensity = DisplayMetrics.DENSITY_DEFAULT
    
    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "content_monitoring_channel"
        const val ACTION_START_MONITORING = "start_monitoring"
        const val ACTION_STOP_MONITORING = "stop_monitoring"
        const val EXTRA_RESULT_CODE = "result_code"
        const val EXTRA_RESULT_DATA = "result_data"
        
        private const val TAG = "ContentMonitoringService"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        setupScreenMetrics()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_MONITORING -> {
                val resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, -1)
                val resultData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(EXTRA_RESULT_DATA, Intent::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_RESULT_DATA)
                }
                
                if (resultCode != -1 && resultData != null) {
                    startMonitoring(resultCode, resultData)
                }
            }
            ACTION_STOP_MONITORING -> {
                stopMonitoring()
            }
        }
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Content Monitoring",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors screen content for inappropriate material"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun setupScreenMetrics() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = windowManager.currentWindowMetrics.bounds
            screenWidth = bounds.width()
            screenHeight = bounds.height()
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels
            screenHeight = displayMetrics.heightPixels
        }
        
        screenDensity = displayMetrics.densityDpi
        
        // Scale down for performance
        screenWidth = (screenWidth * 0.5).toInt()
        screenHeight = (screenHeight * 0.5).toInt()
    }
    
    private fun startMonitoring(resultCode: Int, resultData: Intent) {
        try {
            val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData)
            
            setupImageReader()
            createVirtualDisplay()
            startForegroundService()
            
            Log.d(TAG, "Content monitoring started")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start monitoring", e)
            stopSelf()
        }
    }
    
    private fun setupImageReader() {
        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 2)
        imageReader?.setOnImageAvailableListener({ reader ->
            serviceScope.launch {
                processScreenCapture(reader)
            }
        }, null)
    }
    
    private fun createVirtualDisplay() {
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "HaramBlur-ScreenCapture",
            screenWidth,
            screenHeight,
            screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader?.surface,
            null,
            null
        )
    }
    
    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("HaramBlur Protection Active")
            .setContentText("Monitoring screen content for inappropriate material")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private suspend fun processScreenCapture(reader: ImageReader) {
        try {
            val image = reader.acquireLatestImage() ?: return
            
            // Convert Image to Bitmap
            val bitmap = imageToBitmap(image)
            image.close()
            
            if (bitmap != null) {
                // Process the bitmap for inappropriate content
                processScreenContentUseCase.execute(bitmap)
                bitmap.recycle()
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error processing screen capture", e)
        }
    }
    
    private fun imageToBitmap(image: Image): Bitmap? {
        return try {
            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * screenWidth
            
            val bitmap = Bitmap.createBitmap(
                screenWidth + rowPadding / pixelStride,
                screenHeight,
                Bitmap.Config.ARGB_8888
            )
            bitmap.copyPixelsFromBuffer(buffer)
            
            // Crop to actual screen size if there's padding
            if (rowPadding == 0) {
                bitmap
            } else {
                val croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, screenWidth, screenHeight)
                bitmap.recycle()
                croppedBitmap
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error converting image to bitmap", e)
            null
        }
    }
    
    private fun stopMonitoring() {
        virtualDisplay?.release()
        mediaProjection?.stop()
        imageReader?.close()
        
        virtualDisplay = null
        mediaProjection = null
        imageReader = null
        
        stopForeground(true)
        stopSelf()
        
        Log.d(TAG, "Content monitoring stopped")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
        serviceScope.cancel()
    }
}