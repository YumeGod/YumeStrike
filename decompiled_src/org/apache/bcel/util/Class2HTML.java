package org.apache.bcel.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;

public class Class2HTML implements Constants {
   private JavaClass java_class;
   private String dir;
   private static String class_package;
   private static String class_name;
   private static ConstantPool constant_pool;

   public Class2HTML(JavaClass java_class, String dir) throws IOException {
      Method[] methods = java_class.getMethods();
      this.java_class = java_class;
      this.dir = dir;
      class_name = java_class.getClassName();
      constant_pool = java_class.getConstantPool();
      int index = class_name.lastIndexOf(46);
      if (index > -1) {
         class_package = class_name.substring(0, index);
      } else {
         class_package = "";
      }

      ConstantHTML constant_html = new ConstantHTML(dir, class_name, class_package, methods, constant_pool);
      AttributeHTML attribute_html = new AttributeHTML(dir, class_name, constant_pool, constant_html);
      new MethodHTML(dir, class_name, methods, java_class.getFields(), constant_html, attribute_html);
      this.writeMainHTML(attribute_html);
      new CodeHTML(dir, class_name, methods, constant_pool, constant_html);
      attribute_html.close();
   }

   public static void main(String[] argv) {
      String[] file_name = new String[argv.length];
      int files = 0;
      ClassParser parser = null;
      JavaClass java_class = null;
      String zip_file = null;
      char sep = System.getProperty("file.separator").toCharArray()[0];
      String dir = "." + sep;

      try {
         for(int i = 0; i < argv.length; ++i) {
            if (argv[i].charAt(0) == '-') {
               if (argv[i].equals("-d")) {
                  ++i;
                  dir = argv[i];
                  if (!dir.endsWith("" + sep)) {
                     dir = dir + sep;
                  }

                  (new File(dir)).mkdirs();
               } else if (argv[i].equals("-zip")) {
                  ++i;
                  zip_file = argv[i];
               } else {
                  System.out.println("Unknown option " + argv[i]);
               }
            } else {
               file_name[files++] = argv[i];
            }
         }

         if (files == 0) {
            System.err.println("Class2HTML: No input files specified.");
         } else {
            for(int i = 0; i < files; ++i) {
               System.out.print("Processing " + file_name[i] + "...");
               if (zip_file == null) {
                  parser = new ClassParser(file_name[i]);
               } else {
                  parser = new ClassParser(zip_file, file_name[i]);
               }

               java_class = parser.parse();
               new Class2HTML(java_class, dir);
               System.out.println("Done.");
            }
         }
      } catch (Exception var10) {
         System.out.println(var10);
         var10.printStackTrace(System.out);
      }

   }

   static String referenceClass(int index) {
      String str = constant_pool.getConstantString(index, (byte)7);
      str = Utility.compactClassName(str);
      str = Utility.compactClassName(str, class_package + ".", true);
      return "<A HREF=\"" + class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + str + "</A>";
   }

   static final String referenceType(String type) {
      String short_type = Utility.compactClassName(type);
      short_type = Utility.compactClassName(short_type, class_package + ".", true);
      int index = type.indexOf(91);
      if (index > -1) {
         type = type.substring(0, index);
      }

      return !type.equals("int") && !type.equals("short") && !type.equals("boolean") && !type.equals("void") && !type.equals("char") && !type.equals("byte") && !type.equals("long") && !type.equals("double") && !type.equals("float") ? "<A HREF=\"" + type + ".html\" TARGET=_top>" + short_type + "</A>" : "<FONT COLOR=\"#00FF00\">" + type + "</FONT>";
   }

   static String toHTML(String str) {
      StringBuffer buf = new StringBuffer();

      try {
         for(int i = 0; i < str.length(); ++i) {
            char ch;
            switch (ch = str.charAt(i)) {
               case '\n':
                  buf.append("\\n");
                  break;
               case '\r':
                  buf.append("\\r");
                  break;
               case '<':
                  buf.append("&lt;");
                  break;
               case '>':
                  buf.append("&gt;");
                  break;
               default:
                  buf.append(ch);
            }
         }
      } catch (StringIndexOutOfBoundsException var4) {
      }

      return buf.toString();
   }

   private void writeMainHTML(AttributeHTML attribute_html) throws IOException {
      PrintWriter file = new PrintWriter(new FileOutputStream(this.dir + class_name + ".html"));
      Attribute[] attributes = this.java_class.getAttributes();
      file.println("<HTML>\n<HEAD><TITLE>Documentation for " + class_name + "</TITLE>" + "</HEAD>\n" + "<FRAMESET BORDER=1 cols=\"30%,*\">\n" + "<FRAMESET BORDER=1 rows=\"80%,*\">\n" + "<FRAME NAME=\"ConstantPool\" SRC=\"" + class_name + "_cp.html" + "\"\n MARGINWIDTH=\"0\" " + "MARGINHEIGHT=\"0\" FRAMEBORDER=\"1\" SCROLLING=\"AUTO\">\n" + "<FRAME NAME=\"Attributes\" SRC=\"" + class_name + "_attributes.html" + "\"\n MARGINWIDTH=\"0\" " + "MARGINHEIGHT=\"0\" FRAMEBORDER=\"1\" SCROLLING=\"AUTO\">\n" + "</FRAMESET>\n" + "<FRAMESET BORDER=1 rows=\"80%,*\">\n" + "<FRAME NAME=\"Code\" SRC=\"" + class_name + "_code.html\"\n MARGINWIDTH=0 " + "MARGINHEIGHT=0 FRAMEBORDER=1 SCROLLING=\"AUTO\">\n" + "<FRAME NAME=\"Methods\" SRC=\"" + class_name + "_methods.html\"\n MARGINWIDTH=0 " + "MARGINHEIGHT=0 FRAMEBORDER=1 SCROLLING=\"AUTO\">\n" + "</FRAMESET></FRAMESET></HTML>");
      file.close();

      for(int i = 0; i < attributes.length; ++i) {
         attribute_html.writeAttribute(attributes[i], "class" + i);
      }

   }
}
