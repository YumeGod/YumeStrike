package org.apache.james.mime4j.field.structured.parser;

public interface StructuredFieldParserConstants {
   int EOF = 0;
   int STRING_CONTENT = 11;
   int FOLD = 12;
   int QUOTEDSTRING = 13;
   int WS = 14;
   int CONTENT = 15;
   int QUOTEDPAIR = 16;
   int ANY = 17;
   int DEFAULT = 0;
   int INCOMMENT = 1;
   int NESTED_COMMENT = 2;
   int INQUOTEDSTRING = 3;
   String[] tokenImage = new String[]{"<EOF>", "\"(\"", "\")\"", "\"(\"", "<token of kind 4>", "\"(\"", "\")\"", "<token of kind 7>", "<token of kind 8>", "\"\\\"\"", "<token of kind 10>", "<STRING_CONTENT>", "<FOLD>", "\"\\\"\"", "<WS>", "<CONTENT>", "<QUOTEDPAIR>", "<ANY>"};
}
