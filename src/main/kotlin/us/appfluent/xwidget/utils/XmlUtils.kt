package us.appfluent.xwidget.utils

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag

class XmlUtils {
    companion object {
        fun findXmlTag(psiFile: PsiFile?, editor: Editor?): XmlTag? {
            val offset = editor?.caretModel?.offset
            val element = psiFile?.findElementAt(offset!!)
            return when(element?.parent) {
                is XmlAttributeValue -> element.parent.parent.parent as XmlTag
                is XmlAttribute -> element.parent.parent as XmlTag
                is XmlTag -> element.parent as XmlTag
                else -> null
            }
        }
    }
}