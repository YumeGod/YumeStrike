package org.apache.bcel.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.Utility;

final class AttributeHTML implements Constants {
   private String class_name;
   private PrintWriter file;
   private int attr_count = 0;
   private ConstantHTML constant_html;
   private ConstantPool constant_pool;

   AttributeHTML(String dir, String class_name, ConstantPool constant_pool, ConstantHTML constant_html) throws IOException {
      this.class_name = class_name;
      this.constant_pool = constant_pool;
      this.constant_html = constant_html;
      this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_attributes.html"));
      this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");
   }

   private final String codeLink(int link, int method_number) {
      return "<A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + link + "\" TARGET=Code>" + link + "</A>";
   }

   final void close() {
      this.file.println("</TABLE></BODY></HTML>");
      this.file.close();
   }

   final void writeAttribute(Attribute attribute, String anchor) throws IOException {
      this.writeAttribute(attribute, anchor, 0);
   }

   final void writeAttribute(Attribute attribute, String anchor, int method_number) throws IOException {
      byte tag = attribute.getTag();
      if (tag != -1) {
         ++this.attr_count;
         if (this.attr_count % 2 == 0) {
            this.file.print("<TR BGCOLOR=\"#C0C0C0\"><TD>");
         } else {
            this.file.print("<TR BGCOLOR=\"#A0A0A0\"><TD>");
         }

         this.file.println("<H4><A NAME=\"" + anchor + "\">" + this.attr_count + " " + Constants.ATTRIBUTE_NAMES[tag] + "</A></H4>");
         int index;
         int catch_type;
         int i;
         label80:
         switch (tag) {
            case 0:
               index = ((SourceFile)attribute).getSourceFileIndex();
               this.file.print("<UL><LI><A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">Source file index(" + index + ")</A></UL>\n");
               break;
            case 1:
               index = ((ConstantValue)attribute).getConstantValueIndex();
               this.file.print("<UL><LI><A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">Constant value index(" + index + ")</A></UL>\n");
               break;
            case 2:
               Code c = (Code)attribute;
               Attribute[] attributes = c.getAttributes();
               this.file.print("<UL><LI>Maximum stack size = " + c.getMaxStack() + "</LI>\n<LI>Number of local variables = " + c.getMaxLocals() + "</LI>\n<LI><A HREF=\"" + this.class_name + "_code.html#method" + method_number + "\" TARGET=Code>Byte code</A></LI></UL>\n");
               CodeException[] ce = c.getExceptionTable();
               int len = ce.length;
               if (len > 0) {
                  this.file.print("<P><B>Exceptions handled</B><UL>");

                  for(int i = 0; i < len; ++i) {
                     catch_type = ce[i].getCatchType();
                     this.file.print("<LI>");
                     if (catch_type != 0) {
                        this.file.print(this.constant_html.referenceConstant(catch_type));
                     } else {
                        this.file.print("Any Exception");
                     }

                     this.file.print("<BR>(Ranging from lines " + this.codeLink(ce[i].getStartPC(), method_number) + " to " + this.codeLink(ce[i].getEndPC(), method_number) + ", handled at line " + this.codeLink(ce[i].getHandlerPC(), method_number) + ")</LI>");
                  }

                  this.file.print("</UL>");
               }
               break;
            case 3:
               int[] indices = ((ExceptionTable)attribute).getExceptionIndexTable();
               this.file.print("<UL>");

               for(catch_type = 0; catch_type < indices.length; ++catch_type) {
                  this.file.print("<LI><A HREF=\"" + this.class_name + "_cp.html#cp" + indices[catch_type] + "\" TARGET=\"ConstantPool\">Exception class index(" + indices[catch_type] + ")</A>\n");
               }

               this.file.print("</UL>\n");
               break;
            case 4:
               LineNumber[] line_numbers = ((LineNumberTable)attribute).getLineNumberTable();
               this.file.print("<P>");
               int i = 0;

               while(true) {
                  if (i >= line_numbers.length) {
                     break label80;
                  }

                  this.file.print("(" + line_numbers[i].getStartPC() + ",&nbsp;" + line_numbers[i].getLineNumber() + ")");
                  if (i < line_numbers.length - 1) {
                     this.file.print(", ");
                  }

                  ++i;
               }
            case 5:
               LocalVariable[] vars = ((LocalVariableTable)attribute).getLocalVariableTable();
               this.file.print("<UL>");

               for(int i = 0; i < vars.length; ++i) {
                  index = vars[i].getSignatureIndex();
                  String signature = ((ConstantUtf8)this.constant_pool.getConstant(index, (byte)1)).getBytes();
                  signature = Utility.signatureToString(signature, false);
                  i = vars[i].getStartPC();
                  int end = i + vars[i].getLength();
                  this.file.println("<LI>" + Class2HTML.referenceType(signature) + "&nbsp;<B>" + vars[i].getName() + "</B> in slot %" + vars[i].getIndex() + "<BR>Valid from lines " + "<A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + i + "\" TARGET=Code>" + i + "</A> to " + "<A HREF=\"" + this.class_name + "_code.html#code" + method_number + "@" + end + "\" TARGET=Code>" + end + "</A></LI>");
               }

               this.file.print("</UL>\n");
               break;
            case 6:
               InnerClass[] classes = ((InnerClasses)attribute).getInnerClasses();
               this.file.print("<UL>");

               for(i = 0; i < classes.length; ++i) {
                  index = classes[i].getInnerNameIndex();
                  String name;
                  if (index > 0) {
                     name = ((ConstantUtf8)this.constant_pool.getConstant(index, (byte)1)).getBytes();
                  } else {
                     name = "&lt;anonymous&gt;";
                  }

                  String access = Utility.accessToString(classes[i].getInnerAccessFlags());
                  this.file.print("<LI><FONT COLOR=\"#FF0000\">" + access + "</FONT> " + this.constant_html.referenceConstant(classes[i].getInnerClassIndex()) + " in&nbsp;class " + this.constant_html.referenceConstant(classes[i].getOuterClassIndex()) + " named " + name + "</LI>\n");
               }

               this.file.print("</UL>\n");
               break;
            default:
               this.file.print("<P>" + attribute.toString());
         }

         this.file.println("</TD></TR>");
         this.file.flush();
      }
   }
}
