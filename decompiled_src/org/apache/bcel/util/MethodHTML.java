package org.apache.bcel.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;

final class MethodHTML implements Constants {
   private String class_name;
   private PrintWriter file;
   private ConstantHTML constant_html;
   private AttributeHTML attribute_html;

   MethodHTML(String dir, String class_name, Method[] methods, Field[] fields, ConstantHTML constant_html, AttributeHTML attribute_html) throws IOException {
      this.class_name = class_name;
      this.attribute_html = attribute_html;
      this.constant_html = constant_html;
      this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_methods.html"));
      this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
      this.file.println("<TR><TH ALIGN=LEFT>Access&nbsp;flags</TH><TH ALIGN=LEFT>Type</TH><TH ALIGN=LEFT>Field&nbsp;name</TH></TR>");

      for(int i = 0; i < fields.length; ++i) {
         this.writeField(fields[i]);
      }

      this.file.println("</TABLE>");
      this.file.println("<TABLE BORDER=0><TR><TH ALIGN=LEFT>Access&nbsp;flags</TH><TH ALIGN=LEFT>Return&nbsp;type</TH><TH ALIGN=LEFT>Method&nbsp;name</TH><TH ALIGN=LEFT>Arguments</TH></TR>");

      for(int i = 0; i < methods.length; ++i) {
         this.writeMethod(methods[i], i);
      }

      this.file.println("</TABLE></BODY></HTML>");
      this.file.close();
   }

   private void writeField(Field field) throws IOException {
      String type = Utility.signatureToString(field.getSignature());
      String name = field.getName();
      String access = Utility.accessToString(field.getAccessFlags());
      access = Utility.replace(access, " ", "&nbsp;");
      this.file.print("<TR><TD><FONT COLOR=\"#FF0000\">" + access + "</FONT></TD>\n<TD>" + Class2HTML.referenceType(type) + "</TD><TD><A NAME=\"field" + name + "\">" + name + "</A></TD>");
      Attribute[] attributes = field.getAttributes();

      for(int i = 0; i < attributes.length; ++i) {
         this.attribute_html.writeAttribute(attributes[i], name + "@" + i);
      }

      for(int i = 0; i < attributes.length; ++i) {
         if (attributes[i].getTag() == 1) {
            String str = ((ConstantValue)attributes[i]).toString();
            this.file.print("<TD>= <A HREF=\"" + this.class_name + "_attributes.html#" + name + "@" + i + "\" TARGET=\"Attributes\">" + str + "</TD>\n");
            break;
         }
      }

      this.file.println("</TR>");
   }

   private final void writeMethod(Method method, int method_number) throws IOException {
      String signature = method.getSignature();
      String[] args = Utility.methodSignatureArgumentTypes(signature, false);
      String type = Utility.methodSignatureReturnType(signature, false);
      String name = method.getName();
      String access = Utility.accessToString(method.getAccessFlags());
      Attribute[] attributes = method.getAttributes();
      access = Utility.replace(access, " ", "&nbsp;");
      String html_name = Class2HTML.toHTML(name);
      this.file.print("<TR VALIGN=TOP><TD><FONT COLOR=\"#FF0000\"><A NAME=method" + method_number + ">" + access + "</A></FONT></TD>");
      this.file.print("<TD>" + Class2HTML.referenceType(type) + "</TD><TD>" + "<A HREF=" + this.class_name + "_code.html#method" + method_number + " TARGET=Code>" + html_name + "</A></TD>\n<TD>(");

      for(int i = 0; i < args.length; ++i) {
         this.file.print(Class2HTML.referenceType(args[i]));
         if (i < args.length - 1) {
            this.file.print(", ");
         }
      }

      this.file.print(")</TD></TR>");

      for(int i = 0; i < attributes.length; ++i) {
         this.attribute_html.writeAttribute(attributes[i], "method" + method_number + "@" + i, method_number);
         byte tag = attributes[i].getTag();
         int j;
         if (tag != 3) {
            if (tag == 2) {
               Attribute[] c_a = ((Code)attributes[i]).getAttributes();

               for(j = 0; j < c_a.length; ++j) {
                  this.attribute_html.writeAttribute(c_a[j], "method" + method_number + "@" + i + "@" + j, method_number);
               }
            }
         } else {
            this.file.print("<TR VALIGN=TOP><TD COLSPAN=2></TD><TH ALIGN=LEFT>throws</TH><TD>");
            int[] exceptions = ((ExceptionTable)attributes[i]).getExceptionIndexTable();

            for(j = 0; j < exceptions.length; ++j) {
               this.file.print(this.constant_html.referenceConstant(exceptions[j]));
               if (j < exceptions.length - 1) {
                  this.file.print(", ");
               }
            }

            this.file.println("</TD></TR>");
         }
      }

   }
}
