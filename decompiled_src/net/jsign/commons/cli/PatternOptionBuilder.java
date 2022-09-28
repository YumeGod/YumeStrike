package net.jsign.commons.cli;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;

public class PatternOptionBuilder {
   public static final Class STRING_VALUE = String.class;
   public static final Class OBJECT_VALUE = Object.class;
   public static final Class NUMBER_VALUE = Number.class;
   public static final Class DATE_VALUE = Date.class;
   public static final Class CLASS_VALUE = Class.class;
   public static final Class EXISTING_FILE_VALUE = FileInputStream.class;
   public static final Class FILE_VALUE = File.class;
   public static final Class FILES_VALUE = File[].class;
   public static final Class URL_VALUE = URL.class;

   public static Object getValueClass(char ch) {
      switch (ch) {
         case '#':
            return DATE_VALUE;
         case '$':
         case '&':
         case '\'':
         case '(':
         case ')':
         case ',':
         case '-':
         case '.':
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case ';':
         case '=':
         case '?':
         default:
            return null;
         case '%':
            return NUMBER_VALUE;
         case '*':
            return FILES_VALUE;
         case '+':
            return CLASS_VALUE;
         case '/':
            return URL_VALUE;
         case ':':
            return STRING_VALUE;
         case '<':
            return EXISTING_FILE_VALUE;
         case '>':
            return FILE_VALUE;
         case '@':
            return OBJECT_VALUE;
      }
   }

   public static boolean isValueCode(char ch) {
      return ch == '@' || ch == ':' || ch == '%' || ch == '+' || ch == '#' || ch == '<' || ch == '>' || ch == '*' || ch == '/' || ch == '!';
   }

   public static Options parsePattern(String pattern) {
      char opt = ' ';
      boolean required = false;
      Class type = null;
      Options options = new Options();

      for(int i = 0; i < pattern.length(); ++i) {
         char ch = pattern.charAt(i);
         if (!isValueCode(ch)) {
            if (opt != ' ') {
               Option option = Option.builder(String.valueOf(opt)).hasArg(type != null).required(required).type(type).build();
               options.addOption(option);
               required = false;
               type = null;
               char opt = true;
            }

            opt = ch;
         } else if (ch == '!') {
            required = true;
         } else {
            type = (Class)getValueClass(ch);
         }
      }

      if (opt != ' ') {
         Option option = Option.builder(String.valueOf(opt)).hasArg(type != null).required(required).type(type).build();
         options.addOption(option);
      }

      return options;
   }
}
