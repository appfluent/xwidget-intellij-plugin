package us.appfluent.xwidget.lexer;

import com.intellij.psi.tree.IElementType;

public interface ELTokenTypes {

    IElementType EL_START = new IElementType("EL_START", null);
    IElementType EL_END = new IElementType("EL_END", null);

    IElementType LPAREN = new IElementType("LPAREN", null);
    IElementType RPAREN = new IElementType("RPAREN", null);
    IElementType UNMATCHED_LPAREN = new IElementType("UNMATCHED_LPAREN", null);
    IElementType UNMATCHED_RPAREN = new IElementType("UNMATCHED_RPAREN", null);
    IElementType LBRACKET = new IElementType("LBRACKET", null);
    IElementType RBRACKET= new IElementType("RBRACKET", null);
    IElementType UNMATCHED_LBRACKET = new IElementType("UNMATCHED_LBRACKET", null);
    IElementType UNMATCHED_RBRACKET= new IElementType("UNMATCHED_RBRACKET", null);
    IElementType COMMA = new IElementType("COMMA", null);
    IElementType DOT = new IElementType("DOT", null);
    IElementType MINUS = new IElementType("MINUS", null);
    IElementType NOT = new IElementType("NOT", null);
    IElementType MULTIPLY = new IElementType("MULTIPLY", null);
    IElementType DIVIDE = new IElementType("DIVIDE", null);
    IElementType INTEGER_DIVIDE = new IElementType("INTEGER_DIVIDE", null);
    IElementType MODULO = new IElementType("MODULO", null);
    IElementType PLUS = new IElementType("PLUS", null);
    IElementType LT = new IElementType("LT", null);
    IElementType GT = new IElementType("GT", null);
    IElementType LTE = new IElementType("LTE", null);
    IElementType GTE = new IElementType("GTE", null);
    IElementType EQ = new IElementType("EQ", null);
    IElementType NEQ = new IElementType("NEQ", null);
    IElementType AND = new IElementType("AND", null);
    IElementType OR = new IElementType("OR", null);
    IElementType NULL_COALESCE = new IElementType("NULL_COALESCE", null);
    IElementType QUESTION = new IElementType("QUESTION", null);
    IElementType COLON = new IElementType("COLON", null);

    IElementType PROPERTY_NAME = new IElementType("PROPERTY_NAME", null);
    IElementType METHOD_NAME = new IElementType("METHOD_NAME", null);
    IElementType FUNCTION_NAME = new IElementType("FUNCTION_NAME", null);

    IElementType TRUE = new IElementType("TRUE", null);
    IElementType FALSE = new IElementType("FALSE", null);
    IElementType STRING = new IElementType("STRING", null);
    IElementType NUMBER = new IElementType("NUMBER", null);
}
