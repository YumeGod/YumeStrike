package net.jsign.commons.cli;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandler {
   public static Object createValue(String str, Object obj) throws ParseException {
      return createValue(str, (Class)obj);
   }

   public static Object createValue(String str, Class clazz) throws ParseException {
      if (PatternOptionBuilder.STRING_VALUE == clazz) {
         return str;
      } else if (PatternOptionBuilder.OBJECT_VALUE == clazz) {
         return createObject(str);
      } else if (PatternOptionBuilder.NUMBER_VALUE == clazz) {
         return createNumber(str);
      } else if (PatternOptionBuilder.DATE_VALUE == clazz) {
         return createDate(str);
      } else if (PatternOptionBuilder.CLASS_VALUE == clazz) {
         return createClass(str);
      } else if (PatternOptionBuilder.FILE_VALUE == clazz) {
         return createFile(str);
      } else if (PatternOptionBuilder.EXISTING_FILE_VALUE == clazz) {
         return createFile(str);
      } else if (PatternOptionBuilder.FILES_VALUE == clazz) {
         return createFiles(str);
      } else {
         return PatternOptionBuilder.URL_VALUE == clazz ? createURL(str) : null;
      }
   }

   public static Object createObject(String classname) throws ParseException {
      Class cl;
      try {
         cl = Class.forName(classname);
      } catch (ClassNotFoundException var4) {
         throw new ParseException("Unable to find the class: " + classname);
      }

      try {
         return cl.newInstance();
      } catch (Exception var3) {
         throw new ParseException(var3.getClass().getName() + "; Unable to create an instance of: " + classname);
      }
   }

   public static Number createNumber(String str) throws ParseException {
      try {
         return (Number)(str.indexOf(46) != -1 ? Double.valueOf(str) : Long.valueOf(str));
      } catch (NumberFormatException var2) {
         throw new ParseException(var2.getMessage());
      }
   }

   public static Class createClass(String classname) throws ParseException {
      try {
         return Class.forName(classname);
      } catch (ClassNotFoundException var2) {
         throw new ParseException("Unable to find the class: " + classname);
      }
   }

   public static Date createDate(String str) {
      throw new UnsupportedOperationException("Not yet implemented");
   }

   public static URL createURL(String str) throws ParseException {
      try {
         return new URL(str);
      } catch (MalformedURLException var2) {
         throw new ParseException("Unable to parse the URL: " + str);
      }
   }

   public static File createFile(String str) {
      return new File(str);
   }

   public static File[] createFiles(String str) {
      throw new UnsupportedOperationException("Not yet implemented");
   }
}
