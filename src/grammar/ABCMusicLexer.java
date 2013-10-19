// Generated from ABCMusic.g4 by ANTLR 4.0

package grammar;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ABCMusicLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__25=1, T__24=2, T__23=3, T__22=4, T__21=5, T__20=6, T__19=7, T__18=8, 
		T__17=9, T__16=10, T__15=11, T__14=12, T__13=13, T__12=14, T__11=15, T__10=16, 
		T__9=17, T__8=18, T__7=19, T__6=20, T__5=21, T__4=22, T__3=23, T__2=24, 
		T__1=25, T__0=26, LINEFEED=27, TEXT=28, DIGIT=29, BASENOTE=30, KEYACCIDENTAL=31, 
		MODEMINOR=32, SPACE=33, REST=34, OCTAVE=35, ACCIDENTAL=36, BARLINE=37;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"']'", "'C'", "'C:'", "'*'", "'-'", "'['", "'('", "'X:'", "'T:'", "'Q:'", 
		"'w:'", "' '", "'-'", "'M:'", "'[2'", "'%'", "'L:'", "'_'", "'='", "'V:'", 
		"'K:'", "'[1'", "'/'", "'~'", "'C|'", "'|'", "LINEFEED", "TEXT", "DIGIT", 
		"BASENOTE", "KEYACCIDENTAL", "'m'", "SPACE", "'z'", "OCTAVE", "ACCIDENTAL", 
		"BARLINE"
	};
	public static final String[] ruleNames = {
		"T__25", "T__24", "T__23", "T__22", "T__21", "T__20", "T__19", "T__18", 
		"T__17", "T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", 
		"T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", 
		"T__0", "LINEFEED", "TEXT", "DIGIT", "BASENOTE", "KEYACCIDENTAL", "MODEMINOR", 
		"SPACE", "REST", "OCTAVE", "ACCIDENTAL", "BARLINE"
	};


	    // This method makes the lexer or parser stop running if it encounters
	    // invalid input and throw a RuntimeException.
	    public void reportErrorsAsExceptions() {
	        removeErrorListeners();
	        addErrorListener(new ExceptionThrowingErrorListener());
	    }

	    private static class ExceptionThrowingErrorListener extends BaseErrorListener {
	        @Override
	        public void syntaxError(Recognizer<?, ?> recognizer,
	                Object offendingSymbol, int line, int charPositionInLine,
	                String msg, RecognitionException e) {
	            throw new RuntimeException(msg);
	        }
	    }


	public ABCMusicLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ABCMusic.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\2\4\'\u00c6\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t"+
		"\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"+
		"\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"+
		"\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36"+
		"\t\36\4\37\t\37\4 \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\3\2\3"+
		"\3\3\3\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32"+
		"\3\33\3\33\3\34\6\34\u0090\n\34\r\34\16\34\u0091\3\35\6\35\u0095\n\35"+
		"\r\35\16\35\u0096\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\6$"+
		"\u00a6\n$\r$\16$\u00a7\3$\6$\u00ab\n$\r$\16$\u00ac\5$\u00af\n$\3%\3%\3"+
		"%\3%\3%\3%\3%\5%\u00b8\n%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\5&\u00c5\n"+
		"&\3\u0096\'\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1"+
		"\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25"+
		"\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1\67\35\19\36\1;\37\1"+
		"= \1?!\1A\"\1C#\1E$\1G%\1I&\1K\'\1\3\2\6\4\13\f\17\17\4CIci\4%%dd\3\""+
		"\"\u00d3\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\3M\3\2\2\2\5O\3\2\2\2\7Q\3\2\2\2\tT"+
		"\3\2\2\2\13V\3\2\2\2\rX\3\2\2\2\17Z\3\2\2\2\21\\\3\2\2\2\23_\3\2\2\2\25"+
		"b\3\2\2\2\27e\3\2\2\2\31h\3\2\2\2\33j\3\2\2\2\35m\3\2\2\2\37p\3\2\2\2"+
		"!s\3\2\2\2#u\3\2\2\2%x\3\2\2\2\'z\3\2\2\2)|\3\2\2\2+\177\3\2\2\2-\u0082"+
		"\3\2\2\2/\u0085\3\2\2\2\61\u0087\3\2\2\2\63\u0089\3\2\2\2\65\u008c\3\2"+
		"\2\2\67\u008f\3\2\2\29\u0094\3\2\2\2;\u0098\3\2\2\2=\u009a\3\2\2\2?\u009c"+
		"\3\2\2\2A\u009e\3\2\2\2C\u00a0\3\2\2\2E\u00a2\3\2\2\2G\u00ae\3\2\2\2I"+
		"\u00b7\3\2\2\2K\u00c4\3\2\2\2MN\7_\2\2N\4\3\2\2\2OP\7E\2\2P\6\3\2\2\2"+
		"QR\7E\2\2RS\7<\2\2S\b\3\2\2\2TU\7,\2\2U\n\3\2\2\2VW\7/\2\2W\f\3\2\2\2"+
		"XY\7]\2\2Y\16\3\2\2\2Z[\7*\2\2[\20\3\2\2\2\\]\7Z\2\2]^\7<\2\2^\22\3\2"+
		"\2\2_`\7V\2\2`a\7<\2\2a\24\3\2\2\2bc\7S\2\2cd\7<\2\2d\26\3\2\2\2ef\7y"+
		"\2\2fg\7<\2\2g\30\3\2\2\2hi\7\"\2\2i\32\3\2\2\2jk\7^\2\2kl\7/\2\2l\34"+
		"\3\2\2\2mn\7O\2\2no\7<\2\2o\36\3\2\2\2pq\7]\2\2qr\7\64\2\2r \3\2\2\2s"+
		"t\7\'\2\2t\"\3\2\2\2uv\7N\2\2vw\7<\2\2w$\3\2\2\2xy\7a\2\2y&\3\2\2\2z{"+
		"\7?\2\2{(\3\2\2\2|}\7X\2\2}~\7<\2\2~*\3\2\2\2\177\u0080\7M\2\2\u0080\u0081"+
		"\7<\2\2\u0081,\3\2\2\2\u0082\u0083\7]\2\2\u0083\u0084\7\63\2\2\u0084."+
		"\3\2\2\2\u0085\u0086\7\61\2\2\u0086\60\3\2\2\2\u0087\u0088\7\u0080\2\2"+
		"\u0088\62\3\2\2\2\u0089\u008a\7E\2\2\u008a\u008b\7~\2\2\u008b\64\3\2\2"+
		"\2\u008c\u008d\7~\2\2\u008d\66\3\2\2\2\u008e\u0090\t\2\2\2\u008f\u008e"+
		"\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092"+
		"8\3\2\2\2\u0093\u0095\13\2\2\2\u0094\u0093\3\2\2\2\u0095\u0096\3\2\2\2"+
		"\u0096\u0097\3\2\2\2\u0096\u0094\3\2\2\2\u0097:\3\2\2\2\u0098\u0099\4"+
		"\62;\2\u0099<\3\2\2\2\u009a\u009b\t\3\2\2\u009b>\3\2\2\2\u009c\u009d\t"+
		"\4\2\2\u009d@\3\2\2\2\u009e\u009f\7o\2\2\u009fB\3\2\2\2\u00a0\u00a1\t"+
		"\5\2\2\u00a1D\3\2\2\2\u00a2\u00a3\7|\2\2\u00a3F\3\2\2\2\u00a4\u00a6\7"+
		")\2\2\u00a5\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7"+
		"\u00a8\3\2\2\2\u00a8\u00af\3\2\2\2\u00a9\u00ab\7.\2\2\u00aa\u00a9\3\2"+
		"\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"+
		"\u00af\3\2\2\2\u00ae\u00a5\3\2\2\2\u00ae\u00aa\3\2\2\2\u00afH\3\2\2\2"+
		"\u00b0\u00b8\7`\2\2\u00b1\u00b2\7`\2\2\u00b2\u00b8\7`\2\2\u00b3\u00b8"+
		"\7a\2\2\u00b4\u00b5\7a\2\2\u00b5\u00b8\7a\2\2\u00b6\u00b8\7?\2\2\u00b7"+
		"\u00b0\3\2\2\2\u00b7\u00b1\3\2\2\2\u00b7\u00b3\3\2\2\2\u00b7\u00b4\3\2"+
		"\2\2\u00b7\u00b6\3\2\2\2\u00b8J\3\2\2\2\u00b9\u00c5\7~\2\2\u00ba\u00bb"+
		"\7~\2\2\u00bb\u00c5\7~\2\2\u00bc\u00bd\7]\2\2\u00bd\u00c5\7~\2\2\u00be"+
		"\u00bf\7~\2\2\u00bf\u00c5\7_\2\2\u00c0\u00c1\7<\2\2\u00c1\u00c5\7~\2\2"+
		"\u00c2\u00c3\7~\2\2\u00c3\u00c5\7<\2\2\u00c4\u00b9\3\2\2\2\u00c4\u00ba"+
		"\3\2\2\2\u00c4\u00bc\3\2\2\2\u00c4\u00be\3\2\2\2\u00c4\u00c0\3\2\2\2\u00c4"+
		"\u00c2\3\2\2\2\u00c5L\3\2\2\2\n\2\u0091\u0096\u00a7\u00ac\u00ae\u00b7"+
		"\u00c4";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}