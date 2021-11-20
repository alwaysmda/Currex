package util.extension

import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import com.xodus.templatefive.BuildConfig
import main.ApplicationClass
import java.io.*
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun moveFile(inputPath: String, inputFile: String, outputPath: String): Boolean {
    val inputStream: InputStream?
    val outputStream: OutputStream?
    try {
        //create output directory if it doesn't exist
        val dir = File(outputPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        inputStream = FileInputStream(inputPath + inputFile)
        outputStream = FileOutputStream(outputPath + inputFile)
        val buffer = ByteArray(1024)
        var read: Int
        read = inputStream.read(buffer)
        while (read != -1) {
            outputStream.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
        inputStream.close()
        // write the output file
        outputStream.flush()
        outputStream.close()
        // delete the original file
        File(inputPath + inputFile).delete()
    } catch (e: Exception) {
        log("tag", e.message)
        return false
    }

    return true
}

fun deleteFile(inputPath: String, inputFile: String): Boolean {
    return try {
        // delete the original file
        File(inputPath + inputFile).delete()
    } catch (e: Exception) {
        log("tag", e.message)
        false
    }

}

fun deleteFolder(path: String) {
    val file = File(path)
    if (file.isDirectory)
        for (child in file.listFiles()!!)
            deleteFolder(child.path)
    file.delete()
}

fun copyFile(inputPath: String, inputFile: String, outputPath: String): Boolean {
    var inputStream: InputStream?
    var outputStream: OutputStream?
    try {
        //create output directory if it doesn't exist
        val dir = File(outputPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        inputStream = FileInputStream(inputPath + inputFile)
        outputStream = FileOutputStream(outputPath + inputFile)
        val buffer = ByteArray(1024)
        var read: Int
        read = inputStream.read(buffer)
        while (read != -1) {
            outputStream.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
        inputStream.close()
        // write the output file (You have now copied the file)
        outputStream.flush()
        outputStream.close()
    } catch (e: FileNotFoundException) {
        log("tag", e.message)
        return false
    } catch (e: Exception) {
        log("tag", e.message)
        return false
    }

    return true
}

fun copyFile(inputPath: String, outputPath: String): Boolean {
    var inputStream: InputStream?
    var outputStream: OutputStream?
    try {
        //create output directory if it doesn't exist
        val dir = File(outputPath.substring(0, outputPath.lastIndexOf("/")))
        if (!dir.exists()) {
            dir.mkdirs()
        }
        inputStream = FileInputStream(inputPath)
        outputStream = FileOutputStream(outputPath)
        val buffer = ByteArray(1024)
        var read: Int
        read = inputStream.read(buffer)
        while (read != -1) {
            outputStream.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
        inputStream.close()
        // write the output file (You have now copied the file)
        outputStream.flush()
        outputStream.close()
    } catch (fnfe1: FileNotFoundException) {
        log("tag", fnfe1.message)
        return false
    } catch (e: Exception) {
        log("tag", e.message)
        return false
    }

    return true
}

fun renameFile(path: String, fileName: String, newName: String): Boolean {
    val oldPath = "$path/$fileName"
    val newPath = "$path/$newName"
    val file = File(oldPath)
    val newFile = File(newPath)
    return file.renameTo(newFile)
}

fun createFileFromString(data: String?, inputPath: String, fileName: String): Boolean {
    data?.let {
        // Get the directory for the user's public pictures directory.
        val path = File(inputPath)
        // Make sure the path directory exists.
        if (!path.exists()) {
            // Make it, if it doesn't exit
            path.mkdirs()
        }
        val file = File(path, fileName)
        // Save your stream, don't forget to flush() it before closing it.
        try {
            file.createNewFile()
            val fOut = FileOutputStream(file)
            val myOutWriter = OutputStreamWriter(fOut)
            myOutWriter.append(it)
            myOutWriter.close()
            fOut.flush()
            fOut.close()
        } catch (e: IOException) {
            log("Exception", "File write failed: $e")
            return false
        }

        log("History File Created")
        return true
    } ?: run {
        log("No Data")
        return false
    }
}

fun unzip(inputPath: String, fileName: String, outputPath: String): Boolean {
    val `is`: InputStream
    val zis: ZipInputStream
    try {
        val directory = File(outputPath)
        if (directory.exists().not()) {
            directory.mkdirs()
        }

        var filename: String
        `is` = FileInputStream(inputPath + fileName)
        zis = ZipInputStream(BufferedInputStream(`is`))
        var ze: ZipEntry?
        val buffer = ByteArray(1024)
        var count: Int
        while (zis.nextEntry.also { ze = it } != null) {
            filename = ze!!.name

            if (filename.contains("MACOSX").not()) {
                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze!!.isDirectory) {
                    val fmd = File(outputPath.toString() + filename)
                    fmd.mkdirs()
                    continue
                }


                val fout = FileOutputStream(outputPath.toString() + filename)
                while (zis.read(buffer).also { count = it } != -1) {
                    fout.write(buffer, 0, count)
                }
                fout.close()
                zis.closeEntry()
            }
        }
        zis.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }

    return true
}

fun convertStreamToString(inputStream: InputStream): String {
    var sb = StringBuilder()
    try {
        val reader = BufferedReader(InputStreamReader(inputStream))
        sb = StringBuilder()
        var line: String
        line = reader.readLine()
        while (line != null) {
            sb.append(line)
            line = reader.readLine()
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return sb.toString()
}

fun getStringFromFile(file: File): String? {
    val ret: String
    try {
        if (file.exists()) {
            val fin = FileInputStream(file)
            ret = convertStreamToString(fin)
            //Make sure you close all streams.
            fin.close()
        } else {
            return null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

    return ret
}

fun getStringFromFile(filePath: String): String? {
    return getStringFromFile(File(filePath))
}


fun getPrivateKeyFromRSA(filepath: String): PrivateKey? {
    var key: PrivateKey? = null
    try {
        val file = File(filepath)
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        val spec = PKCS8EncodedKeySpec(bytes)
        val kf = KeyFactory.getInstance("RSA")
        key = kf.generatePrivate(spec)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return key
}

fun getInternalDirectory(): File {
    return Environment.getExternalStorageDirectory()
}

fun getDataDirectory(): File {
    return File(Environment.getExternalStorageDirectory().path + "/Android/data/" + BuildConfig.APPLICATION_ID)
}

fun scanMedia(path: String) {
    log("SCANNING=" + Uri.fromFile(File(path)))
    MediaScannerConnection.scanFile(
        ApplicationClass.getInstance(),
        arrayOf(path),
        null
    ) { p, uri -> log("SCAN COMPLETE|PATH=$p|URI=$uri") }
}

fun getExternalSDCardDirectory(): File? {
    val path = System.getenv("SECONDARY_STORAGE")
    path?.let { p ->
        if (!TextUtils.isEmpty(p)) {
            if (p.contains(":")) {
                for (_path in p.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    val file = File(_path)
                    if (file.exists()) {
                        return file
                    }
                }
            } else {
                val file = File(p)
                if (file.exists()) {
                    return file
                }
            }
        }
    }
    return null
}