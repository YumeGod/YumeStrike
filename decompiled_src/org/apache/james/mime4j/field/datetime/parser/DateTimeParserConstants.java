package org.apache.james.mime4j.field.datetime.parser;

public interface DateTimeParserConstants {
   int EOF = 0;
   int OFFSETDIR = 24;
   int MILITARY_ZONE = 35;
   int WS = 36;
   int COMMENT = 38;
   int DIGITS = 46;
   int QUOTEDPAIR = 47;
   int ANY = 48;
   int DEFAULT = 0;
   int INCOMMENT = 1;
   int NESTED_COMMENT = 2;
   String[] tokenImage = new String[]{"<EOF>", "\"\\r\"", "\"\\n\"", "\",\"", "\"Mon\"", "\"Tue\"", "\"Wed\"", "\"Thu\"", "\"Fri\"", "\"Sat\"", "\"Sun\"", "\"Jan\"", "\"Feb\"", "\"Mar\"", "\"Apr\"", "\"May\"", "\"Jun\"", "\"Jul\"", "\"Aug\"", "\"Sep\"", "\"Oct\"", "\"Nov\"", "\"Dec\"", "\":\"", "<OFFSETDIR>", "\"UT\"", "\"GMT\"", "\"EST\"", "\"EDT\"", "\"CST\"", "\"CDT\"", "\"MST\"", "\"MDT\"", "\"PST\"", "\"PDT\"", "<MILITARY_ZONE>", "<WS>", "\"(\"", "\")\"", "<token of kind 39>", "\"(\"", "<token of kind 41>", "<token of kind 42>", "\"(\"", "\")\"", "<token of kind 45>", "<DIGITS>", "<QUOTEDPAIR>", "<ANY>"};
}
