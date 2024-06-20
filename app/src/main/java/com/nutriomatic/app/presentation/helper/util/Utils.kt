package com.nutriomatic.app.presentation.helper.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.DateFormat
import android.icu.text.DecimalFormatSymbols
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Currency
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nutriomatic.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.ParseException
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timestamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
private const val MAXIMAL_SIZE = 1000_000

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timestamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/NutriOMatic/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/NutriOMatic/$timestamp.jpg")
    if (imageFile.parentFile?.exists() == false) {
        imageFile.parentFile?.mkdir()
    }

    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timestamp, ".jpg", filesDir)
}

fun uriToFile(context: Context, imageUri: Uri): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val fileOutputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) fileOutputStream.write(
        buffer,
        0,
        length
    )
    fileOutputStream.close()
    inputStream.close()
    return myFile
}

fun File.reduceFileSize(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)

    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}


fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

suspend fun getCachedImageUri(context: Context, imageUrl: String): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val futureTarget = Glide.with(context)
                .asFile()
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .submit()

            val file = futureTarget.get()
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}

fun convertStringToMillis(dateString: String): Long {
    val dateFormats = arrayOf("MMM d, yyyy", "d MMMM yyyy", "yyyy-MM-dd")
    val dateFormat =
        dateFormats.map { SimpleDateFormat(it, Locale.getDefault()) }.firstOrNull { formatter ->
            formatter.isLenient = false
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            try {
                formatter.parse(dateString)?.time
                true
            } catch (e: ParseException) {
                false
            }
        }
    return dateFormat?.parse(dateString)?.time ?: 0L
}

fun millisToISOFormat(millis: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(Date(millis))
}

fun createRequestBodyText(value: String): RequestBody {
    return value.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun createRequestBodyInt(value: Int): RequestBody {
    return value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
}

fun convertToLocalDateTimeString(
    utcDateTimeString: String,
    locale: Locale = Locale.getDefault(),
): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date: Date = inputFormat.parse(utcDateTimeString)!!

    val outputFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale)
    outputFormat.timeZone = TimeZone.getDefault()
    return outputFormat.format(date)
}

fun convertToLocalDateString(
    utcDateTimeString: String,
    locale: Locale = Locale.getDefault(),
): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date: Date = inputFormat.parse(utcDateTimeString)!!

    val outputFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale)
    outputFormat.timeZone = TimeZone.getDefault()
    return outputFormat.format(date)
}

fun convertToLocalTimeString(
    utcDateTimeString: String,
    locale: Locale = Locale.getDefault(),
): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", locale)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date: Date = inputFormat.parse(utcDateTimeString)!!

    val outputFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale)
    outputFormat.timeZone = TimeZone.getDefault()
    return outputFormat.format(date)
}

fun formatCurrency(value: Double): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return currencyFormat.format(value)
}