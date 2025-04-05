/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 2025/4/5
 */

package cn.rtast.qwertylearneridea

data class TranslateResult(
    val result: Result,
    val data: Data
) {
    data class Result(
        val code: Int
    )

    data class Data(
        val entries: List<Entry>
    )

    data class Entry(
        val explain: String,
        val entry: String
    )
}