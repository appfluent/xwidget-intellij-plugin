package us.appfluent.xwidget.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.Key
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import us.appfluent.xwidget.utils.findXmlTag
import us.appfluent.xwidget.utils.isXWidgetFragment


abstract class XmlNavigationAction(
    private val tagName: String,
    private val attributeName: String? = null
) : AnAction() {

    companion object {
        val XML_TAG = Key.create<Any>("XML_TAG")
        val XML_ATTRIBUTE = Key.create<Any>("XML_ATTRIBUTE")
        val XML_ATTRIBUTE_VALUE = Key.create<Any>("XML_ATTRIBUTE_VALUE")
    }

    abstract fun navigate(e: AnActionEvent)

    fun getXmlTag(e: AnActionEvent): XmlTag?  {
        return e.presentation.getClientProperty(XML_TAG) as XmlTag?
    }

    fun getXmlAttribute(e: AnActionEvent): XmlAttribute?  {
        return e.presentation.getClientProperty(XML_ATTRIBUTE) as XmlAttribute?
    }

    fun getXmlAttributeValue(e: AnActionEvent): String?  {
        return e.presentation.getClientProperty(XML_ATTRIBUTE_VALUE) as String?
    }

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) return
        navigate(e)
    }

    override fun update(e: AnActionEvent) {
        var isEnabled = false
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val isFragment = isXWidgetFragment(psiFile)
        if (isFragment) {
            val editor = e.getData(CommonDataKeys.EDITOR)
            val xmlTag = findXmlTag(psiFile, editor)
            if (xmlTag != null && xmlTag.name == tagName) {
                isEnabled = true
                e.presentation.putClientProperty(XML_TAG, xmlTag)
                if (attributeName != null) {
                    val xmlAttribute = xmlTag.getAttribute(attributeName)
                    if (xmlAttribute != null) {
                        e.presentation.putClientProperty(XML_ATTRIBUTE, xmlAttribute)
                        val attributeValue = xmlAttribute.value
                        if (attributeValue != null) {
                            e.presentation.putClientProperty(XML_ATTRIBUTE_VALUE, attributeValue)
                        }
                    }
                }
            }
        }
        e.presentation.isEnabledAndVisible = isEnabled
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        // Choose EDT if interacting with the UI, or BGT for background work
        return ActionUpdateThread.BGT // or ActionUpdateThread.EDT
    }
}

