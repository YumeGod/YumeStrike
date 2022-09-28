package com.xmlmind.fo.converter;

public interface ErrorHandler {
   void error(Exception var1) throws Exception;

   void warning(Exception var1) throws Exception;
}
