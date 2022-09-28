package com.xmlmind.fo.properties.expression;

public interface ParserConstants {
   int EOF = 0;
   int LENGTH = 10;
   int PERCENTAGE = 11;
   int NUMBER = 12;
   int NAME = 13;
   int COLOR = 14;
   int UNIT = 15;
   int DIGIT = 16;
   int HEXDIGIT = 17;
   int ERROR = 18;
   int DEFAULT = 0;
   String[] tokenImage = new String[]{"<EOF>", "\"+\"", "\"-\"", "\"*\"", "\"div\"", "\"mod\"", "\"(\"", "\")\"", "\",\"", "\" \"", "<LENGTH>", "<PERCENTAGE>", "<NUMBER>", "<NAME>", "<COLOR>", "<UNIT>", "<DIGIT>", "<HEXDIGIT>", "<ERROR>"};
}
