package org.apache.batik.util;

public interface ParsedURLProtocolHandler {
   String getProtocolHandled();

   ParsedURLData parseURL(String var1);

   ParsedURLData parseURL(ParsedURL var1, String var2);
}
