/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 2025/4/5
 */

package cn.rtast.qwertylearneridea

import cn.rtast.rutil.http.Http
import cn.rtast.rutil.string.toJson
import com.google.gson.annotations.SerializedName

data class TranslateRequest(
    val text: String,
    @SerializedName("source_lang")
    val sourceLang: String,
    @SerializedName("target_lang")
    val targetLang: String
)

data class TranslateResponse(
    val alternatives: List<String>
)

fun getTranslate(request: TranslateRequest): String {
    return Http.post<TranslateResponse>(TRANSLATE_URL, request.toJson().apply {
        println(this)
    }).apply {
        println(this)
    }.alternatives.joinToString(", ")
}