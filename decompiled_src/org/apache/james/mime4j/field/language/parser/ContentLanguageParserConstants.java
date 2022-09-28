package org.apache.james.mime4j.field.language.parser;

public interface ContentLanguageParserConstants {
   int EOF = 0;
   int WS = 3;
   int COMMENT = 5;
   int QUOTEDSTRING = 16;
   int DIGITS = 17;
   int ALPHA = 18;
   int ALPHANUM = 19;
   int DOT = 20;
   int QUOTEDPAIR = 21;
   int ANY = 22;
   int DEFAULT = 0;
   int INCOMMENT = 1;
   int NESTED_COMMENT = 2;
   int INQUOTEDSTRING = 3;
   String[] tokenImage = new String[]{"<EOF>", "\",\"", "\"-\"", "<WS>", "\"(\"", "\")\"", "<token of kind 6>", "\"(\"", "<token of kind 8>", "<token of kind 9>", "\"(\"", "\")\"", "<token of kind 12>", "\"\\\"\"", "<token of kind 14>", "<token of kind 15>", "\"\\\"\"", "<DIGITS>", "<ALPHA>", "<ALPHANUM>", "\".\"", "<QUOTEDPAIR>", "<ANY>"};
}
