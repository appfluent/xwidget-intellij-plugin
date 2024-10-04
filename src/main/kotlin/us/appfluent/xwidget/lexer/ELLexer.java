// Generated by JFlex 1.9.1 http://jflex.de/  (tweaked for IntelliJ platform)
// source: ELLexer.flex

package us.appfluent.xwidget.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;


public class ELLexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int AFTER_DOT = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = {
     0,  0,  1, 1
  };

  /**
   * Top-level table for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_TOP = zzUnpackcmap_top();

  private static final String ZZ_CMAP_TOP_PACKED_0 =
    "\1\0\25\u0100\1\u0200\11\u0100\1\u0300\17\u0100\1\u0400\247\u0100"+
    "\10\u0500\u1020\u0100";

  private static int [] zzUnpackcmap_top() {
    int [] result = new int[4352];
    int offset = 0;
    offset = zzUnpackcmap_top(ZZ_CMAP_TOP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_top(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Second-level tables for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_BLOCKS = zzUnpackcmap_blocks();

  private static final String ZZ_CMAP_BLOCKS_PACKED_0 =
    "\11\0\1\1\4\2\22\0\1\1\1\3\2\0\1\4"+
    "\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
    "\1\15\1\16\1\17\12\20\1\21\1\22\1\23\1\24"+
    "\1\25\1\26\1\0\32\27\1\30\1\31\1\32\1\0"+
    "\1\27\1\0\1\33\3\27\1\34\1\35\5\27\1\36"+
    "\1\37\2\27\1\40\1\27\1\41\1\42\1\43\1\44"+
    "\5\27\1\45\1\46\1\47\1\50\6\0\1\2\32\0"+
    "\1\1\u01df\0\1\1\177\0\13\1\35\0\2\2\5\0"+
    "\1\1\57\0\1\1\240\0\1\1\377\0\u0100\51";

  private static int [] zzUnpackcmap_blocks() {
    int [] result = new int[1536];
    int offset = 0;
    offset = zzUnpackcmap_blocks(ZZ_CMAP_BLOCKS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_blocks(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\1\3\1\1\1\4\2\1\1\5"+
    "\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\1\1\20\1\21\1\22\1\23\1\24"+
    "\2\22\1\1\1\25\1\1\1\22\1\26\1\27\1\30"+
    "\2\0\1\31\1\0\1\15\1\32\1\33\1\34\1\35"+
    "\1\36\2\22\1\37\1\40\1\41\1\0\2\22\1\0"+
    "\1\22\1\42\1\0\1\43\4\0";

  private static int [] zzUnpackAction() {
    int [] result = new int[62];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\52\0\124\0\176\0\250\0\322\0\124\0\374"+
    "\0\u0126\0\124\0\124\0\124\0\124\0\124\0\124\0\124"+
    "\0\124\0\u0150\0\124\0\u017a\0\u01a4\0\u01ce\0\u01f8\0\u0222"+
    "\0\124\0\124\0\u024c\0\u0276\0\u02a0\0\124\0\u02ca\0\u02f4"+
    "\0\124\0\124\0\124\0\u031e\0\u0126\0\124\0\u0348\0\u0372"+
    "\0\124\0\124\0\124\0\124\0\124\0\u039c\0\u03c6\0\124"+
    "\0\124\0\124\0\u03f0\0\u041a\0\u0444\0\u046e\0\u0498\0\u0222"+
    "\0\u04c2\0\u0222\0\u04ec\0\u0516\0\u0540\0\u056a";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[62];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length() - 1;
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpacktrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\2\4\1\5\1\6\1\7\1\10\1\11\1\12"+
    "\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22"+
    "\1\23\1\3\1\24\1\25\1\26\1\27\1\30\1\31"+
    "\1\3\1\32\2\30\1\33\5\30\1\34\1\30\1\3"+
    "\1\35\1\36\1\37\1\3\27\0\1\40\3\0\12\40"+
    "\60\0\2\4\73\0\1\41\72\0\1\42\12\0\1\43"+
    "\24\0\1\44\16\0\7\45\1\46\21\45\1\47\20\45"+
    "\16\0\1\50\1\0\1\22\55\0\1\51\51\0\1\52"+
    "\51\0\1\53\53\0\1\54\33\0\1\55\7\0\1\30"+
    "\6\0\1\30\3\0\12\30\15\0\1\55\7\0\1\30"+
    "\6\0\1\30\3\0\1\56\11\30\15\0\1\55\7\0"+
    "\1\30\6\0\1\30\3\0\6\30\1\57\3\30\53\0"+
    "\1\60\22\0\1\61\42\0\1\62\7\0\1\40\6\0"+
    "\1\40\3\0\12\40\44\0\1\63\12\0\2\45\1\0"+
    "\46\45\21\0\1\50\41\0\1\55\7\0\1\30\6\0"+
    "\1\30\3\0\3\30\1\64\6\30\15\0\1\55\7\0"+
    "\1\30\6\0\1\30\3\0\11\30\1\65\45\0\1\66"+
    "\21\0\1\55\7\0\1\30\6\0\1\30\3\0\7\30"+
    "\1\67\2\30\15\0\1\55\7\0\1\30\6\0\1\30"+
    "\3\0\1\30\1\70\10\30\27\0\1\71\37\0\1\55"+
    "\7\0\1\30\6\0\1\30\3\0\1\30\1\72\10\30"+
    "\13\0\1\73\76\0\1\74\55\0\1\75\52\0\1\76"+
    "\33\0\1\43\27\0";

  private static int [] zzUnpacktrans() {
    int [] result = new int[1428];
    int offset = 0;
    offset = zzUnpacktrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpacktrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state {@code aState}
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\3\1\1\11\2\1\10\11\1\1\1\11"+
    "\5\1\2\11\3\1\1\11\2\1\3\11\2\0\1\11"+
    "\1\0\1\1\5\11\2\1\3\11\1\0\2\1\1\0"+
    "\2\1\1\0\1\1\4\0";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[62];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** Number of newlines encountered up to the start of the matched text. */
  @SuppressWarnings("unused")
  private int yyline;

  /** Number of characters from the last newline up to the start of the matched text. */
  @SuppressWarnings("unused")
  protected int yycolumn;

  /** Number of characters up to the start of the matched text. */
  @SuppressWarnings("unused")
  private long yychar;

  /** Whether the scanner is currently at the beginning of a line. */
  @SuppressWarnings("unused")
  private boolean zzAtBOL = true;

  /** Whether the user-EOF-code has already been executed. */
  private boolean zzEOFDone;

  /* user code: */
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


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public ELLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** Returns the maximum size of the scanner buffer, which limits the size of tokens. */
  private int zzMaxBufferLen() {
    return Integer.MAX_VALUE;
  }

  /**  Whether the scanner buffer can grow to accommodate a larger token. */
  private boolean zzCanGrow() {
    return true;
  }

  /**
   * Translates raw input code points to DFA table row
   */
  private static int zzCMap(int input) {
    int offset = input & 255;
    return offset == input ? ZZ_CMAP_BLOCKS[offset] : ZZ_CMAP_BLOCKS[ZZ_CMAP_TOP[input >> 8] | offset];
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      {@code false}, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position {@code pos} from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException
  {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMap(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
              {
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
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1:
            { return TokenType.BAD_CHARACTER;
            }
          // fall through
          case 36: break;
          case 2:
            { return TokenType.WHITE_SPACE;
            }
          // fall through
          case 37: break;
          case 3:
            { return ELTokenTypes.NOT;
            }
          // fall through
          case 38: break;
          case 4:
            { return ELTokenTypes.MODULO;
            }
          // fall through
          case 39: break;
          case 5:
            { return handleParenthesis(true);
            }
          // fall through
          case 40: break;
          case 6:
            { return handleParenthesis(false);
            }
          // fall through
          case 41: break;
          case 7:
            { return ELTokenTypes.MULTIPLY;
            }
          // fall through
          case 42: break;
          case 8:
            { return ELTokenTypes.PLUS;
            }
          // fall through
          case 43: break;
          case 9:
            { return ELTokenTypes.COMMA;
            }
          // fall through
          case 44: break;
          case 10:
            { return ELTokenTypes.MINUS;
            }
          // fall through
          case 45: break;
          case 11:
            { yybegin(AFTER_DOT); return ELTokenTypes.DOT;
            }
          // fall through
          case 46: break;
          case 12:
            { return ELTokenTypes.DIVIDE;
            }
          // fall through
          case 47: break;
          case 13:
            { return ELTokenTypes.NUMBER;
            }
          // fall through
          case 48: break;
          case 14:
            { return ELTokenTypes.COLON;
            }
          // fall through
          case 49: break;
          case 15:
            { return ELTokenTypes.LT;
            }
          // fall through
          case 50: break;
          case 16:
            { return ELTokenTypes.GT;
            }
          // fall through
          case 51: break;
          case 17:
            { return ELTokenTypes.QUESTION;
            }
          // fall through
          case 52: break;
          case 18:
            { yybegin(YYINITIAL); return ELTokenTypes.PROPERTY_NAME;
            }
          // fall through
          case 53: break;
          case 19:
            { return handleBracket(true);
            }
          // fall through
          case 54: break;
          case 20:
            { return handleBracket(false);
            }
          // fall through
          case 55: break;
          case 21:
            { return ELTokenTypes.EL_END;
            }
          // fall through
          case 56: break;
          case 22:
            { return ELTokenTypes.NEQ;
            }
          // fall through
          case 57: break;
          case 23:
            { return ELTokenTypes.EL_START;
            }
          // fall through
          case 58: break;
          case 24:
            { return ELTokenTypes.AND;
            }
          // fall through
          case 59: break;
          case 25:
            { return ELTokenTypes.STRING;
            }
          // fall through
          case 60: break;
          case 26:
            { return ELTokenTypes.LTE;
            }
          // fall through
          case 61: break;
          case 27:
            { return ELTokenTypes.EQ;
            }
          // fall through
          case 62: break;
          case 28:
            { return ELTokenTypes.GTE;
            }
          // fall through
          case 63: break;
          case 29:
            { return ELTokenTypes.NULL_COALESCE;
            }
          // fall through
          case 64: break;
          case 30:
            { yypushback(1); return ELTokenTypes.FUNCTION_NAME;
            }
          // fall through
          case 65: break;
          case 31:
            { return ELTokenTypes.OR;
            }
          // fall through
          case 66: break;
          case 32:
            { return ELTokenTypes.INTEGER_DIVIDE;
            }
          // fall through
          case 67: break;
          case 33:
            { yypushback(1); yybegin(YYINITIAL); return ELTokenTypes.METHOD_NAME;
            }
          // fall through
          case 68: break;
          case 34:
            { return ELTokenTypes.TRUE;
            }
          // fall through
          case 69: break;
          case 35:
            { return ELTokenTypes.FALSE;
            }
          // fall through
          case 70: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}