package us.appfluent.xwidget.services

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class OpenProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        project.getService(ConfigurationService::class.java)
    }
}