package org.apache.james.mime4j.field.contentdisposition.parser;

public interface ContentDispositionParserConstants {
   int EOF = 0;
   int WS = 5;
   int COMMENT = 7;
   int QUOTEDSTRING = 18;
   int DIGITS = 19;
   int ATOKEN = 20;
   int QUOTEDPAIR = 21;
   int ANY = 22;
   int DEFAULT = 0;
   int INCOMMENT = 1;
   int NESTED_COMMENT = 2;
   int INQUOTEDSTRING = 3;
   String[] tokenImage = new String[]{"<EOF>", "\"\\r\"", "\"\\n\"", "\";\"", "\"=\"", "<WS>", "\"(\"", "\")\"", "<token of kind 8>", "\"(\"", "<token of kind 10>", "<token of kind 11>", "\"(\"", "\")\"", "<token of kind 14>", "\"\\\"\"", "<token of kind 16>", "<token of kind 17>", "\"\\\"\"", "<DIGITS>", "<ATOKEN>", "<QUOTEDPAIR>", "<ANY>"};
}
