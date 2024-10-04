package us.appfluent.xwidget.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;

%%

%public
%class ELLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
  private int parenDepth = 0;
  private int bracketDepth = 0;

  private IElementType handleParenthesis(boolean isOpening) {
    if (isOpening) {
      parenDepth++;
      return ELTokenTypes.LPAREN;
    } else {
      if (parenDepth > 0) {
        parenDepth--;
        return ELTokenTypes.RPAREN;
      } else {
        return ELTokenTypes.UNMATCHED_RPAREN;
      }
    }
  }

  private IElementType handleBracket(boolean isOpening) {
    if (isOpening) {
      bracketDepth++;
      return ELTokenTypes.LBRACKET;
    } else {
      if (bracketDepth > 0) {
        bracketDepth--;
        return ELTokenTypes.RBRACKET;
      } else {
        return ELTokenTypes.UNMATCHED_RBRACKET;
      }
    }
  }
%}

WHITE_SPACE=\s+
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
NUMBER_LITERAL=[0-9]+(\.[0-9]*)?
STRING_LITERAL=\'([^\\\']|\\.)*\'
TRUE=true
FALSE=false

// Declare AFTER_DOT as an exclusive state
%x AFTER_DOT

%%

<YYINITIAL> {
  "${"                   { return ELTokenTypes.EL_START; }
  "}"                    { return ELTokenTypes.EL_END; }
  "("                    { return handleParenthesis(true); }
  ")"                    { return handleParenthesis(false); }
  "["                    { return handleBracket(true); }
  "]"                    { return handleBracket(false); }
  "."                    { yybegin(AFTER_DOT); return ELTokenTypes.DOT; } // Transition to AFTER_DOT

  "-"                    { return ELTokenTypes.MINUS; }
  "!"                    { return ELTokenTypes.NOT; }

  "*"                    { return ELTokenTypes.MULTIPLY; }
  "/"                    { return ELTokenTypes.DIVIDE; }
  "~/"                   { return ELTokenTypes.INTEGER_DIVIDE; }
  "%"                    { return ELTokenTypes.MODULO; }

  "+"                    { return ELTokenTypes.PLUS; }

  "<"                    { return ELTokenTypes.LT; }
  ">"                    { return ELTokenTypes.GT; }
  "<="                   { return ELTokenTypes.LTE; }
  ">="                   { return ELTokenTypes.GTE; }

  "=="                   { return ELTokenTypes.EQ; }
  "!="                   { return ELTokenTypes.NEQ; }


  "&&"                   { return ELTokenTypes.AND; }
  "&amp;&amp;"           { return ELTokenTypes.AND; }

  "||"                   { return ELTokenTypes.OR; }

  "??"                   { return ELTokenTypes.NULL_COALESCE; }

  "?"                    { return ELTokenTypes.QUESTION; }
  ":"                    { return ELTokenTypes.COLON; }

  ","                    { return ELTokenTypes.COMMA; }

  {TRUE}                 { return ELTokenTypes.TRUE; }
  {FALSE}                { return ELTokenTypes.FALSE; }
  {STRING_LITERAL}       { return ELTokenTypes.STRING; }
  {NUMBER_LITERAL}       { return ELTokenTypes.NUMBER; }
  {WHITE_SPACE}          { return TokenType.WHITE_SPACE; }

  {IDENTIFIER}"("        { yypushback(1); return ELTokenTypes.FUNCTION_NAME; } // function call
  {IDENTIFIER}           { yybegin(YYINITIAL); return ELTokenTypes.PROPERTY_NAME; } // Property access
}

<AFTER_DOT> {
  {IDENTIFIER}"("        { yypushback(1); yybegin(YYINITIAL); return ELTokenTypes.METHOD_NAME; } // method call
  {IDENTIFIER}           { yybegin(YYINITIAL); return ELTokenTypes.PROPERTY_NAME; } // Property access
}

[^] { return TokenType.BAD_CHARACTER; }

<<EOF>> {
  if (parenDepth > 0) {
      parenDepth = 0;
      return ELTokenTypes.UNMATCHED_LPAREN;
  }
  if (bracketDepth > 0) {
      bracketDepth = 0;
      return ELTokenTypes.UNMATCHED_LBRACKET;
  }
  return null;
}