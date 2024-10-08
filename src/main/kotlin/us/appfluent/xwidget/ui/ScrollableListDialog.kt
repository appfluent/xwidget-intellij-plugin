package us.appfluent.xwidget.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

fun showScrollableListDialog(
    project: Project,
    title: String,
    items: List<String>,
    visibleRowCount: Int = 10,
    preferredSize: Dimension = Dimension(400, 200)
): Int? {
    val dialog = ScrollableListDialog(project, title, items, visibleRowCount, preferredSize)
    if (dialog.showAndGet()) {
        return dialog.getSelectedIndex()
    }
    return null
}

class ScrollableListDialog(
    project: Project,
    title: String,
    items: List<String>,
    visibleRowCount: Int,
    private val preferredSize: Dimension
) : DialogWrapper(project) {

    private val listModel = DefaultListModel<String>().apply {
        items.forEach { addElement(it) }
    }

    private val list = JBList(listModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        this.visibleRowCount = visibleRowCount // Set the number of visible rows

        // Add mouse listener for double-click
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(event: MouseEvent) {
                if (event.clickCount == 2) {
                    // Close the dialog when an item is double-clicked
                    if (selectedValue != null) {
                        close(OK_EXIT_CODE) // Close with OK exit code
                    }
                }
            }
        })
    }

    init {
        this.title = title
        init() // Initialize the dialog
    }

    override fun createCenterPanel(): JComponent {
        val scrollPane = JBScrollPane(list)
        scrollPane.preferredSize = preferredSize // Set preferred size for the scroll pane
        return scrollPane
    }

    fun getSelectedItem(): String? {
        return list.selectedValue
    }

    fun getSelectedIndex(): Int {
        return if (list.selectedValue != null) list.selectedIndex else -1
    }
}