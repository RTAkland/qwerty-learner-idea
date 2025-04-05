/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 2025/4/5
 */

@file:Suppress("UnstableApiUsage")

package cn.rtast.qwertylearneridea

import cn.rtast.rutil.string.fromJson
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.SelectionEvent
import com.intellij.openapi.editor.event.SelectionListener
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.WindowManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Component
import java.util.regex.Pattern

private const val url = "https://dict.youdao.com/suggest?num=1&ver=3.0&doctype=json&cache=false&le=zh&q="

class QwertyLearnerBarWidget(project: Project) : StatusBarWidget, StatusBarWidget.TextPresentation {
    private val selectionListener: SelectionListener
    private var translatedText: String = "QwertyLearner"
    private var client = OkHttpClient()

    init {
        selectionListener = object : SelectionListener {
            override fun selectionChanged(e: SelectionEvent) {
                val range: TextRange = e.newRange
                val editor = e.editor
                val document = editor.document
                val selectedText: String = document.getText(range)
                if (!selectedText.isEmpty() && (isEnglishStr(selectedText) || isChineseStr(selectedText))) {
                    object : Task.Backgroundable(project, "Get translate", true) {
                        override fun run(indicator: ProgressIndicator) {
                            val req = Request.Builder().get().url("$url$selectedText").build()
                            val response = client.newCall(req).execute().body.string()
                            val resText = response.fromJson<TranslateResult>()
                            val result = resText.data.entries.first().entry + ": " + resText.data.entries.first().explain
                            translatedText = result
                            val statusBar = WindowManager.getInstance().getStatusBar(project)
                            statusBar.updateWidget(ID())
                        }
                    }.queue()
                } else translatedText = ""
                val statusBar = WindowManager.getInstance().getStatusBar(project)
                statusBar.updateWidget(ID())
            }
        }
    }

    override fun ID(): String {
        return "QwertyLearnerBarWidget"
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation? {
        return this
    }

    override fun install(statusBar: StatusBar) {
        EditorFactory.getInstance().eventMulticaster.addSelectionListener(selectionListener, this)
    }

    override fun dispose() {
        EditorFactory.getInstance().eventMulticaster.removeSelectionListener(selectionListener)
    }

    override fun getText(): @NlsContexts.Label String {
        return translatedText
    }

    override fun getAlignment(): Float {
        return Component.CENTER_ALIGNMENT
    }

    override fun getTooltipText(): @NlsContexts.Tooltip String? {
        return "Qwerty-learner-idea"
    }

    companion object {
        fun isEnglishStr(charaString: String): Boolean {
            return charaString.matches("^[a-zA-Z]*".toRegex())
        }

        fun isChineseStr(str: String): Boolean {
            val regEx = "[\\u4e00-\\u9fa5]+"
            val p = Pattern.compile(regEx)
            val m = p.matcher(str)
            return m.find()
        }
    }
}
