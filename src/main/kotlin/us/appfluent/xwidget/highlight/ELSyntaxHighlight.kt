package us.appfluent.xwidget.highlight

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.tree.IElementType

import us.appfluent.xwidget.lexer.ELTokenTypes


enum class ELSyntaxHighlight(
    val tokenType: IElementType,
    val fallback: TextAttributesKey,
    val error: String? = null
) {
    EL_START(ELTokenTypes.EL_START, DefaultLanguageHighlighterColors.KEYWORD),
    EL_END(ELTokenTypes.EL_END, DefaultLanguageHighlighterColors.KEYWORD),

    LPAREN(ELTokenTypes.LPAREN, DefaultLanguageHighlighterColors.PARENTHESES),
    RPAREN (ELTokenTypes.RPAREN, DefaultLanguageHighlighterColors.PARENTHESES),
    UNMATCHED_LPAREN(ELTokenTypes.UNMATCHED_LPAREN, DefaultLanguageHighlighterColors.PARENTHESES, "Unmatched left parenthesis"),
    UNMATCHED_RPAREN(ELTokenTypes.UNMATCHED_RPAREN, DefaultLanguageHighlighterColors.PARENTHESES, "Unmatched right parenthesis"),
    LBRACKET(ELTokenTypes.LBRACKET, DefaultLanguageHighlighterColors.BRACKETS),
    RBRACKET(ELTokenTypes.RBRACKET, DefaultLanguageHighlighterColors.BRACKETS),
    UNMATCHED_LBRACKET(ELTokenTypes.UNMATCHED_LBRACKET, DefaultLanguageHighlighterColors.BRACKETS, "Unmatched left bracket"),
    UNMATCHED_RBRACKET(ELTokenTypes.UNMATCHED_RBRACKET, DefaultLanguageHighlighterColors.BRACKETS, "Unmatched right bracket"),
    COMMA(ELTokenTypes.COMMA, DefaultLanguageHighlighterColors.COMMA),
    DOT(ELTokenTypes.DOT, DefaultLanguageHighlighterColors.DOT),
    MINUS(ELTokenTypes.MINUS, DefaultLanguageHighlighterColors.KEYWORD),
    NOT(ELTokenTypes.NOT, DefaultLanguageHighlighterColors.KEYWORD),
    MULTIPLY(ELTokenTypes.MULTIPLY, DefaultLanguageHighlighterColors.KEYWORD),
    DIVIDE(ELTokenTypes.DIVIDE, DefaultLanguageHighlighterColors.KEYWORD),
    INTEGER_DIVIDE(ELTokenTypes.INTEGER_DIVIDE, DefaultLanguageHighlighterColors.KEYWORD),
    MODULO (ELTokenTypes.MODULO, DefaultLanguageHighlighterColors.KEYWORD),
    PLUS(ELTokenTypes.PLUS, DefaultLanguageHighlighterColors.KEYWORD),
    LT(ELTokenTypes.LT, DefaultLanguageHighlighterColors.KEYWORD),
    GT(ELTokenTypes.GT, DefaultLanguageHighlighterColors.KEYWORD),
    LTE(ELTokenTypes.LTE, DefaultLanguageHighlighterColors.KEYWORD),
    GTE(ELTokenTypes.GTE, DefaultLanguageHighlighterColors.KEYWORD),
    EQ(ELTokenTypes.EQ, DefaultLanguageHighlighterColors.KEYWORD),
    NEQ(ELTokenTypes.NEQ, DefaultLanguageHighlighterColors.KEYWORD),
    AND(ELTokenTypes.AND, DefaultLanguageHighlighterColors.KEYWORD),
    OR(ELTokenTypes.OR, DefaultLanguageHighlighterColors.KEYWORD),
    NULL_COALESCE(ELTokenTypes.NULL_COALESCE, DefaultLanguageHighlighterColors.KEYWORD),
    QUESTION(ELTokenTypes.QUESTION, DefaultLanguageHighlighterColors.KEYWORD),
    COLON(ELTokenTypes.COLON, DefaultLanguageHighlighterColors.KEYWORD),

    PROPERTY_NAME(ELTokenTypes.PROPERTY_NAME, DefaultLanguageHighlighterColors.IDENTIFIER),
    METHOD_NAME(ELTokenTypes.METHOD_NAME, DefaultLanguageHighlighterColors.INSTANCE_METHOD),
    FUNCTION_NAME(ELTokenTypes.FUNCTION_NAME, DefaultLanguageHighlighterColors.STATIC_METHOD),

    TRUE(ELTokenTypes.TRUE, DefaultLanguageHighlighterColors.STATIC_FIELD),
    FALSE(ELTokenTypes.FALSE, DefaultLanguageHighlighterColors.STATIC_FIELD),
    STRING(ELTokenTypes.STRING, DefaultLanguageHighlighterColors.STRING),
    NUMBER(ELTokenTypes.NUMBER, DefaultLanguageHighlighterColors.NUMBER);

    var textAttributesKey: TextAttributesKey = TextAttributesKey.createTextAttributesKey(name, fallback);

    companion object {
        private val attributes: MutableMap<IElementType, ELSyntaxHighlight> by lazy {
            mutableMapOf<IElementType, ELSyntaxHighlight>().apply {
                // Populate the map after the companion object is initialized
                for (highlight in values()) {
                    put(highlight.tokenType, highlight)
                }
            }
        }

        fun getTokenHighlight(tokenType: IElementType?): ELSyntaxHighlight? {
            return attributes[tokenType]
        }
    }
}