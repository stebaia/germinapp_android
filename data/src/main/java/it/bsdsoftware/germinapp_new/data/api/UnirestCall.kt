package it.bsdsoftware.germinapp_new.data.api

import android.util.Log
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.async.Callback
import com.mashape.unirest.http.exceptions.UnirestException
import com.mashape.unirest.request.body.MultipartBody
import java.io.File
import java.util.concurrent.Future


fun sendFileUnirest(
    url: String,
    token: String,
    idRilevazione: Long,
    files: List<File>
): Future<HttpResponse<String>> {
    Unirest.setTimeouts(0, 0)
    return Unirest.post(url)
        .header(
            "Authentication",
            token
        )
        .field("idRilevazione", idRilevazione)
        .fields("file", files)
        .asStringAsync(object : Callback<String> {
            override fun completed(response: HttpResponse<String>?) {
                response?.let { Log.v("rretwtsr", it.body) }
            }

            override fun failed(e: UnirestException?) {
                e?.message?.let { Log.v("rr", it) }
            }

            override fun cancelled() {
                TODO("Not yet implemented")
            }

        })

}

fun MultipartBody.fields(name: String, files: List<File>): MultipartBody {
    for (file in files) {
        this.field(name, file)
    }
    return this
}