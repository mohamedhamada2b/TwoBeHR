package com.example.data.constants

import android.R
import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.*
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class Utilities {
    var img: Uri? = null
    var menulayoutmanager: RecyclerView.LayoutManager? = null
    companion object{
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected)) {
                Toast.makeText(
                    context,
                    "تأكد من الإتصال بالإنترنت",
                    Toast.LENGTH_LONG
                ).show()
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        fun showMessage(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getTypeFace(context: Context): Typeface? {
            return Typeface.createFromAsset(context.getAssets(), "font/twolight.otf")
        }

        fun getLang(context: Context?): String? {
            return "ar"
        }

        fun getRequestBodyText(data: String?): RequestBody? {
            return RequestBody.create(MediaType.parse("text/plain"), data)
        }
    }
}