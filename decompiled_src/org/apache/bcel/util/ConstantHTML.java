package org.apache.bcel.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;

final class ConstantHTML implements Constants {
   private String class_name;
   private String class_package;
   private ConstantPool constant_pool;
   private PrintWriter file;
   private String[] constant_ref;
   private Constant[] constants;
   private Method[] methods;

   ConstantHTML(String dir, String class_name, String class_package, Method[] methods, ConstantPool constant_pool) throws IOException {
      this.class_name = class_name;
      this.class_package = class_package;
      this.constant_pool = constant_pool;
      this.methods = methods;
      this.constants = constant_pool.getConstantPool();
      this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_cp.html"));
      this.constant_ref = new String[this.constants.length];
      this.constant_ref[0] = "&lt;unknown&gt;";
      this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\"><TABLE BORDER=0>");

      for(int i = 1; i < this.constants.length; ++i) {
         if (i % 2 == 0) {
            this.file.print("<TR BGCOLOR=\"#C0C0C0\"><TD>");
         } else {
            this.file.print("<TR BGCOLOR=\"#A0A0A0\"><TD>");
         }

         if (this.constants[i] != null) {
            this.writeConstant(i);
         }

         this.file.print("</TD></TR>\n");
      }

      this.file.println("</TABLE></BODY></HTML>");
      this.file.close();
   }

   String referenceConstant(int index) {
      return this.constant_ref[index];
   }

   private void writeConstant(int index) {
      byte tag = this.constants[index].getTag();
      this.file.println("<H4> <A NAME=cp" + index + ">" + index + "</A> " + Constants.CONSTANT_NAMES[tag] + "</H4>");
      int class_index;
      int name_index;
      String ref;
      switch (tag) {
         case 7:
            ConstantClass c4 = (ConstantClass)this.constant_pool.getConstant(index, (byte)7);
            name_index = c4.getNameIndex();
            String class_name2 = this.constant_pool.constantToString(index, tag);
            String short_class_name = Utility.compactClassName(class_name2);
            short_class_name = Utility.compactClassName(short_class_name, this.class_package + ".", true);
            ref = "<A HREF=\"" + class_name2 + ".html\" TARGET=_top>" + short_class_name + "</A>";
            this.constant_ref[index] = "<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + short_class_name + "</A>";
            this.file.println("<P><TT>" + ref + "</TT><UL>" + "<LI><A HREF=\"#cp" + name_index + "\">Name index(" + name_index + ")</A></UL>\n");
            break;
         case 8:
            ConstantString c5 = (ConstantString)this.constant_pool.getConstant(index, (byte)8);
            name_index = c5.getStringIndex();
            String str = Class2HTML.toHTML(this.constant_pool.constantToString(index, tag));
            this.file.println("<P><TT>" + str + "</TT><UL>" + "<LI><A HREF=\"#cp" + name_index + "\">Name index(" + name_index + ")</A></UL>\n");
            break;
         case 9:
            ConstantFieldref c3 = (ConstantFieldref)this.constant_pool.getConstant(index, (byte)9);
            class_index = c3.getClassIndex();
            name_index = c3.getNameAndTypeIndex();
            String field_class = this.constant_pool.constantToString(class_index, (byte)7);
            String short_field_class = Utility.compactClassName(field_class);
            short_field_class = Utility.compactClassName(short_field_class, this.class_package + ".", true);
            String field_name = this.constant_pool.constantToString(name_index, (byte)12);
            if (field_class.equals(this.class_name)) {
               ref = "<A HREF=\"" + field_class + "_methods.html#field" + field_name + "\" TARGET=Methods>" + field_name + "</A>";
            } else {
               ref = "<A HREF=\"" + field_class + ".html\" TARGET=_top>" + short_field_class + "</A>." + field_name + "\n";
            }

            this.constant_ref[index] = "<A HREF=\"" + this.class_name + "_cp.html#cp" + class_index + "\" TARGET=Constants>" + short_field_class + "</A>.<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + field_name + "</A>";
            this.file.println("<P><TT>" + ref + "</TT><BR>\n" + "<UL>" + "<LI><A HREF=\"#cp" + class_index + "\">Class(" + class_index + ")</A><BR>\n" + "<LI><A HREF=\"#cp" + name_index + "\">NameAndType(" + name_index + ")</A></UL>");
            break;
         case 10:
         case 11:
            if (tag == 10) {
               ConstantMethodref c = (ConstantMethodref)this.constant_pool.getConstant(index, (byte)10);
               class_index = c.getClassIndex();
               name_index = c.getNameAndTypeIndex();
            } else {
               ConstantInterfaceMethodref c1 = (ConstantInterfaceMethodref)this.constant_pool.getConstant(index, (byte)11);
               class_index = c1.getClassIndex();
               name_index = c1.getNameAndTypeIndex();
            }

            String method_name = this.constant_pool.constantToString(name_index, (byte)12);
            String html_method_name = Class2HTML.toHTML(method_name);
            String method_class = this.constant_pool.constantToString(class_index, (byte)7);
            String short_method_class = Utility.compactClassName(method_class);
            short_method_class = Utility.compactClassName(method_class);
            short_method_class = Utility.compactClassName(short_method_class, this.class_package + ".", true);
            ConstantNameAndType c2 = (ConstantNameAndType)this.constant_pool.getConstant(name_index, (byte)12);
            String signature = this.constant_pool.constantToString(c2.getSignatureIndex(), (byte)1);
            String[] args = Utility.methodSignatureArgumentTypes(signature, false);
            String type = Utility.methodSignatureReturnType(signature, false);
            String ret_type = Class2HTML.referenceType(type);
            StringBuffer buf = new StringBuffer("(");

            for(int i = 0; i < args.length; ++i) {
               buf.append(Class2HTML.referenceType(args[i]));
               if (i < args.length - 1) {
                  buf.append(",&nbsp;");
               }
            }

            buf.append(")");
            String arg_types = buf.toString();
            if (method_class.equals(this.class_name)) {
               ref = "<A HREF=\"" + this.class_name + "_code.html#method" + this.getMethodNumber(method_name + signature) + "\" TARGET=Code>" + html_method_name + "</A>";
            } else {
               ref = "<A HREF=\"" + method_class + ".html" + "\" TARGET=_top>" + short_method_class + "</A>." + html_method_name;
            }

            this.constant_ref[index] = ret_type + "&nbsp;<A HREF=\"" + this.class_name + "_cp.html#cp" + class_index + "\" TARGET=Constants>" + short_method_class + "</A>.<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=ConstantPool>" + html_method_name + "</A>&nbsp;" + arg_types;
            this.file.println("<P><TT>" + ret_type + "&nbsp;" + ref + arg_types + "&nbsp;</TT>\n<UL>" + "<LI><A HREF=\"#cp" + class_index + "\">Class index(" + class_index + ")</A>\n" + "<LI><A HREF=\"#cp" + name_index + "\">NameAndType index(" + name_index + ")</A></UL>");
            break;
         case 12:
            ConstantNameAndType c6 = (ConstantNameAndType)this.constant_pool.getConstant(index, (byte)12);
            name_index = c6.getNameIndex();
            int signature_index = c6.getSignatureIndex();
            this.file.println("<P><TT>" + Class2HTML.toHTML(this.constant_pool.constantToString(index, tag)) + "</TT><UL>" + "<LI><A HREF=\"#cp" + name_index + "\">Name index(" + name_index + ")</A>\n" + "<LI><A HREF=\"#cp" + signature_index + "\">Signature index(" + signature_index + ")</A></UL>\n");
            break;
         default:
            this.file.println("<P><TT>" + Class2HTML.toHTML(this.constant_pool.constantToString(index, tag)) + "</TT>\n");
      }

   }

   private final int getMethodNumber(String str) {
      for(int i = 0; i < this.methods.length; ++i) {
         String cmp = this.methods[i].getName() + this.methods[i].getSignature();
         if (cmp.equals(str)) {
            return i;
         }
      }

      return -1;
   }
}
