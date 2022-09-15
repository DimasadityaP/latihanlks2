package com.example.latihanlks2.network

import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import kotlin.jvm.Throws

class NetworkApi(
    requestURL: String?,
    private val charset : String? = "UTF-8",
    private val method : String? = "GET",
    private val contentType : String? = null
) {
    private val boundary: String = "===" + System.currentTimeMillis() + "==="
    private val httpConn : HttpURLConnection
    private var outputStream: OutputStream? = null
    private var writer: PrintWriter? = null

    companion object{
        private val LINE_FEED = "\r\n"
        val CONTENT_JSON = "application/json"
    }

    init {
        val url = URL(requestURL)
        httpConn = url.openConnection() as HttpURLConnection
        httpConn.useCaches = false
        httpConn.requestMethod = method

        if (method != "GET"){
            httpConn.doOutput = method == "POST"
            httpConn.doInput = method == "POST"
            if (contentType == CONTENT_JSON){
                httpConn.setRequestProperty("Content-Type", contentType)
            }else{
                httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=$boundary")
            }
            outputStream = httpConn.outputStream
            writer = PrintWriter(OutputStreamWriter(outputStream, charset), true)
        }
    }

    fun addFormField(name : String, value: String?): NetworkApi{
        writer?.append("--$boundary")?.append(LINE_FEED)
        writer?.append("Content-Disposition: form-data; name=\"$name\"")
            ?.append(LINE_FEED)
        writer?.append("Content-Type: Text/plain; charset=$charset")?.append(LINE_FEED)
        writer?.append(LINE_FEED)
        writer?.append(value)
        writer?.flush()
        writer?.append(LINE_FEED)?.flush()
        return this
    }

    @Throws(IOException::class)
    fun addFilePart(fieldName: String, uploadFile: File): NetworkApi{
        val fileName : String = uploadFile.name
        writer?.append("--$boundary")?.append(LINE_FEED)
        writer?.append("Content-Disposition: form-data; name=\"" +fieldName
                + "\"; filename=\"" +fileName + "\"")?.append(LINE_FEED)
        writer?.append(
            ("Content-Type: "
                    +URLConnection.guessContentTypeFromName(fileName))
        )?.append(LINE_FEED)
        writer?.append("Content-Transfer-Encoding: binary")?.append(LINE_FEED)
        writer?.append(LINE_FEED)
        writer?.flush()
        val inputStream = FileInputStream(uploadFile)
        val buffer = ByteArray(4096)
        var bytesRead = -1
        while ((inputStream.read(buffer).also { bytesRead = it }) != 1){
            outputStream?.write(buffer, 0, bytesRead)
        }
        outputStream?.flush()
        inputStream.close()
        writer?.append(LINE_FEED)
        writer?.flush()
        return this
    }

    @Throws(IOException::class)
    fun execute():String{
        try {
            val response = StringBuffer()
            if (contentType != CONTENT_JSON){
                writer?.append("--$boundary")?.append(LINE_FEED)
                writer?.close()
            }
            val  status: Int = httpConn.responseCode
            if (status in 200 until 300){
                val reader = BufferedReader(InputStreamReader(httpConn.inputStream))
                var line: String?
                while ((reader.readLine().also { line = it }) != null){
                    response.append(line)
                }
                reader.close()
            }else{
                val reader = BufferedReader(InputStreamReader(httpConn.errorStream))
                var line: String?
                while ((reader.readLine().also { line = it }) != null){
                    response.append(line)
                }
                reader.close()
                throw IOException(response.toString())
            }
            return response.toString()
        }catch (e : Exception){
            throw e
        }finally {
            httpConn.disconnect()
        }
    }
}