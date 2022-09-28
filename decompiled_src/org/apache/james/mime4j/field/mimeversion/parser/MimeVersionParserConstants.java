package org.apache.james.mime4j.field.mimeversion.parser;

public interface MimeVersionParserConstants {
   int EOF = 0;
   int WS = 3;
   int COMMENT = 5;
   int QUOTEDSTRING = 16;
   int DIGITS = 17;
   int DOT = 18;
   int QUOTEDPAIR = 19;
   int ANY = 20;
   int DEFAULT = 0;
   int INCOMMENT = 1;
   int NESTED_COMMENT = 2;
   int INQUOTEDSTRING = 3;
   String[] tokenImage = new String[]{"<EOF>", "\"\\r\"", "\"\\n\"", "<WS>", "\"(\"", "\")\"", "<token of kind 6>", "\"(\"", "<token of kind 8>", "<token of kind 9>", "\"(\"", "\")\"", "<token of kind 12>", "\"\\\"\"", "<token of kind 14>", "<token of kind 15>", "\"\\\"\"", "<DIGITS>", "\".\"", "<QUOTEDPAIR>", "<ANY>"};
}
