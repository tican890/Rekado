package com.pavelrekun.rekado.services.payloads

import android.os.Environment
import com.pavelrekun.rekado.data.Payload
import com.pavelrekun.rekado.services.eventbus.Events
import io.paperdb.Paper
import org.greenrobot.eventbus.EventBus
import java.io.File


object PayloadHelper {

    val FOLDER_PATH = "${Environment.getExternalStorageDirectory()}/Rekado/"

    private const val CHOSEN_PAYLOAD = "CHOSEN_PAYLOAD"

    fun init() {
        val folderFile = File(FOLDER_PATH)
        if (!folderFile.exists()) folderFile.mkdir()
    }

    fun getPayloads(): MutableList<Payload> {
        val payloads: MutableList<Payload> = ArrayList()

        File(FOLDER_PATH).listFiles().forEach {
            if (it.extension == "bin") {
                payloads.add(Payload(getName(it.path), it.path))
            }
        }

        return payloads
    }

    fun getPayloadTitles(): MutableList<String> {
        val payloads: MutableList<String> = ArrayList()

        for (payload in getPayloads()) {
            payloads.add(payload.name)
        }

        return payloads
    }

    fun getName(path: String): String {
        return File(path).name
    }

    fun getPath(name: String): String {
        return "$FOLDER_PATH/$name"
    }

    fun findPayload(name: String): Payload? {
        for (payload in getPayloads()) {
            if (payload.name == name) {
                return payload
            }
        }

        return null
    }

    fun putChosenPayload(payload: Payload) {
        Paper.book().write(CHOSEN_PAYLOAD, payload)
        EventBus.getDefault().postSticky(Events.PayloadSelected())
    }

    fun getChosenPaylaod(): Payload {
        return Paper.book().read(CHOSEN_PAYLOAD)
    }

    fun removeChosenPayload() {
        Paper.book().delete(CHOSEN_PAYLOAD)
    }
}