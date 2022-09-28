package org.apache.james.mime4j.field.address;

public interface AddressListParserConstants {
   int EOF = 0;
   int WS = 10;
   int ALPHA = 11;
   int DIGIT = 12;
   int ATEXT = 13;
   int DOTATOM = 14;
   int DOMAINLITERAL = 18;
   int COMMENT = 20;
   int QUOTEDSTRING = 31;
   int QUOTEDPAIR = 32;
   int ANY = 33;
   int DEFAULT = 0;
   int INDOMAINLITERAL = 1;
   int INCOMMENT = 2;
   int NESTED_COMMENT = 3;
   int INQUOTEDSTRING = 4;
   String[] tokenImage = new String[]{"<EOF>", "\"\\r\"", "\"\\n\"", "\",\"", "\":\"", "\";\"", "\"<\"", "\">\"", "\"@\"", "\".\"", "<WS>", "<ALPHA>", "<DIGIT>", "<ATEXT>", "<DOTATOM>", "\"[\"", "<token of kind 16>", "<token of kind 17>", "\"]\"", "\"(\"", "\")\"", "<token of kind 21>", "\"(\"", "<token of kind 23>", "<token of kind 24>", "\"(\"", "\")\"", "<token of kind 27>", "\"\\\"\"", "<token of kind 29>", "<token of kind 30>", "\"\\\"\"", "<QUOTEDPAIR>", "<ANY>"};
}
