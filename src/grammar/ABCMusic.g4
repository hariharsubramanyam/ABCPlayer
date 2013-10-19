/**
 * This file is the grammar file used by ANTLR.
 *
 * In order to compile this file, navigate to this directory
 * (<src/grammar>) and run the following command:
 *
 * java _jar ../../antlr.jar ABCMusic.g4
 */

grammar ABCMusic;

/*
 * This puts 'package grammar;' at the top of the output Java files.
 * Do not change these lines unless you know what you're doing.
 */
@header {
package grammar;
}

/*
 * This adds code to the generated lexer and parser. This makes the lexer and
 * parser throw errors if they encounter invalid input. Do not change these
 * lines unless you know what you're doing.
 */
@members {
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
}

/*
 * These are the lexical rules. They define the tokens used by the lexer.
 */
EOL : [ \t]*[\n]+[\r]*;
LINEFEED : [\t\r\n]+;
TEXT : .+?;
DIGIT : '0'..'9';
BASENOTE : 'C' | 'D' | 'E' | 'F' | 'G' | 'A' | 'B' | 'c' | 'd' | 'e' | 'f' | 'g' | 'a' | 'b';
KEYACCIDENTAL : '#' | 'b';
MODEMINOR : 'm';

/*
 * These are the parser rules. They define the structures used by the parser.
 *
 * You should make sure you have one rule that describes the entire input.
 * This is the 'start rule'. The start rule should end with the special
 * predefined token EOF so that it describes the entire input. Below, we've made
 * 'line' the start rule.
 *
 * For more information, see
 * http://www.antlr.org/wiki/display/ANTLR4/Parser+Rules#ParserRules-StartRulesandEOF
 */
abc_tune : abc_header EOF;
abc_header : field_number comment* field_title other_fields* field_key;

field_number : 'X:' DIGIT+ EOL;
field_title : 'T:' TEXT EOL;
other_fields : field_composer | field_default_length | field_meter | field_tempo | field_voice | comment;
field_composer : 'C:' TEXT EOL;
field_default_length : 'L:' note_length_strict EOL;
field_meter : 'M:' meter EOL;
field_tempo : 'Q:' tempo EOL;
field_voice : 'V:' TEXT EOL;
field_key : 'K:' key EOL;

comment : '%' TEXT LINEFEED;
note_length_strict : DIGIT+ '/' DIGIT+;
meter : 'C' | 'C|' | meter_fraction;
meter_fraction : DIGIT+ '/' DIGIT+;
tempo : meter_fraction '=' DIGIT+;

key : BASENOTE KEYACCIDENTAL? MODEMINOR?;