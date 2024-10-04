package us.appfluent.xwidget.highlight

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.lexer.FlexAdapter
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.xml.XmlAttributeValue

import us.appfluent.xwidget.lexer.ELLexer

class ELAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is XmlAttributeValue) {
            val regex = "\\$\\{(.*?)}".toRegex()
            val matches = regex.findAll(element.value)
            matches.forEach { matchResult ->
                val elExpression = matchResult.value // This is the full EL expression
                val startOffset = matchResult.range.first
                val endOffset = matchResult.range.last + 1  // +1 because range is exclusive at the end
                val lexer = FlexAdapter(ELLexer(null))

                lexer.start(elExpression)
                while (lexer.tokenType != null) {
                    val start = startOffset + element.valueTextRange.startOffset + lexer.tokenStart
                    val end = startOffset + element.valueTextRange.startOffset + lexer.tokenEnd
                    println(lexer.tokenType)
                    highlight(holder, start, end, lexer.tokenType)
                    lexer.advance()
                }
            }
        }
    }

    private fun highlight(holder: AnnotationHolder, start: Int, end: Int, tokenType: IElementType?) {
        if (tokenType == TokenType.BAD_CHARACTER) {
            holder.newSilentAnnotation(HighlightSeverity.ERROR)
                .range(TextRange(start, end))
                .textAttributes(CodeInsightColors.ERRORS_ATTRIBUTES)
                .tooltip("Bad character at col:$start")
                .create()
        } else {
            val highlight = ELSyntaxHighlight.getTokenHighlight(tokenType)
            if (highlight != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(TextRange(start, end))
                    .textAttributes(highlight.textAttributesKey)
                    .create()
                if (highlight.error != null) {
                    holder.newSilentAnnotation(HighlightSeverity.ERROR)
                        .range(TextRange(start, end))
                        .textAttributes(CodeInsightColors.ERRORS_ATTRIBUTES)
                        .tooltip(highlight.error)
                        .create()
                }
            }
        }

    }
}