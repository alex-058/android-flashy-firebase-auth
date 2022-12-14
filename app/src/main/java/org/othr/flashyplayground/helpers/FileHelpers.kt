package org.othr.flashyplayground.helpers

import android.content.Context
import timber.log.Timber
import java.io.*
import java.lang.Exception

/**
 * Helper file to deal with writing to a file, reading from a file and checking if a file exists
 */

fun write (context: Context, fileName: String, data: String) {
    try {
        val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    } catch (ex: Exception) {
        Timber.e("Cannot read file: %s ", ex.toString())
    }
}

fun read (context: Context, fileName: String): String {
    var str = "" // will be the returned string
    try {
        val inputStream = context.openFileInput(fileName)
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val partialStr = StringBuilder()
            var done = false
            // read file content line-by-line
            while (!done) {
                val line = bufferedReader.readLine()
                done = (line == null)
                if (line != null) partialStr.append(line)
            }
            inputStream.close()
            str = partialStr.toString()
        }
    } catch (e: FileNotFoundException) {
        Timber.e("file not found: %s", e.toString())
    } catch (e: IOException) {
        Timber.e("cannot read file: %s", e.toString())
    }
    return str
}

fun exists(context: Context, filename: String): Boolean {
    val file = context.getFileStreamPath(filename)
    return file.exists()
}