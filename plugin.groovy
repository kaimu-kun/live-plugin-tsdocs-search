import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import groovy.json.JsonSlurper

import static liveplugin.PluginUtil.*

registerAction("tsdocsSearchAction", "ctrl shift S") { AnActionEvent event ->
    Closure itemProvider = { String pattern, ProgressIndicator indicator ->
        if(pattern.length() < 2) return []

        googleSuggestionsFor(pattern)
    }
    showPopupSearch("tsdocs quick search", event.project, itemProvider) { String item ->
        BrowserUtil.open("https://tsdocs.dev/docs/${item}")
    }a
}


static List<String> googleSuggestionsFor(String text) {
    text = URLEncoder.encode(text, "UTF-8")
    def json = "https://www.npmjs.com/search/suggestions?q=$text".toURL().text
    new JsonSlurper().parseText(json).collect {
        item -> return item.name
    }
}

if (!isIdeStartup) show("Loaded tsdocsSearchAction<br/>Use ctrl+shift+S to run it")
