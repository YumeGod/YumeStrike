package org.apache.bcel.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;

final class CodeHTML implements Constants {
   private String class_name;
   private Method[] methods;
   private PrintWriter file;
   private BitSet goto_set;
   private ConstantPool constant_pool;
   private ConstantHTML constant_html;
   private static boolean wide = false;

   CodeHTML(String dir, String class_name, Method[] methods, ConstantPool constant_pool, ConstantHTML constant_html) throws IOException {
      this.class_name = class_name;
      this.methods = methods;
      this.constant_pool = constant_pool;
      this.constant_html = constant_html;
      this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_code.html"));
      this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\">");

      for(int i = 0; i < methods.length; ++i) {
         this.writeMethod(methods[i], i);
      }

      this.file.println("</BODY></HTML>");
      this.file.close();
   }

   private final String codeToHTML(ByteSequence bytes, int method_number) throws IOException {
      short opcode = (short)bytes.readUnsignedByte();
      int default_offset = 0;
      int no_pad_bytes = 0;
      StringBuffer buf = new StringBuffer("<TT>" + Constants.OPCODE_NAMES[opcode] + "</TT></TD><TD>");
      int i;
      int i;
      if (opcode == 170 || opcode == 171) {
         i = bytes.getIndex() % 4;
         no_pad_bytes = i == 0 ? 0 : 4 - i;

         for(i = 0; i < no_pad_bytes; ++i) {
            bytes.readByte();
         }

         default_offset = bytes.readInt();
      }

      String name;
      int index;
      int class_index;
      int vindex;
      int[] jump_table;
      int offset;
      short index;
      switch (opcode) {
         case 18:
            index = bytes.readUnsignedByte();
            buf.append("<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(this.constant_pool.constantToString(index, this.constant_pool.getConstant(index).getTag())) + "</a>");
            break;
         case 19:
         case 20:
            index = bytes.readShort();
            buf.append("<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(this.constant_pool.constantToString(index, this.constant_pool.getConstant(index).getTag())) + "</a>");
            break;
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 169:
            if (wide) {
               vindex = bytes.readShort();
               wide = false;
            } else {
               vindex = bytes.readUnsignedByte();
            }

            buf.append("%" + vindex);
            break;
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 119:
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 186:
         case 190:
         case 191:
         case 194:
         case 195:
         default:
            if (Constants.NO_OF_OPERANDS[opcode] > 0) {
               for(int i = 0; i < Constants.TYPE_OF_OPERANDS[opcode].length; ++i) {
                  switch (Constants.TYPE_OF_OPERANDS[opcode][i]) {
                     case 8:
                        buf.append(bytes.readUnsignedByte());
                        break;
                     case 9:
                        buf.append(bytes.readShort());
                        break;
                     case 10:
                        buf.append(bytes.readInt());
                        break;
                     default:
                        System.err.println("Unreachable default case reached!");
                        System.exit(-1);
                  }

                  buf.append("&nbsp;");
               }
            }
            break;
         case 132:
            short constant;
            if (wide) {
               vindex = bytes.readShort();
               constant = bytes.readShort();
               wide = false;
            } else {
               vindex = bytes.readUnsignedByte();
               constant = bytes.readByte();
            }

            buf.append("%" + vindex + " " + constant);
            break;
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 198:
         case 199:
            index = bytes.getIndex() + bytes.readShort() - 1;
            buf.append("<A HREF=\"#code" + method_number + "@" + index + "\">" + index + "</A>");
            break;
         case 170:
            int low = bytes.readInt();
            int high = bytes.readInt();
            offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
            default_offset += offset;
            buf.append("<TABLE BORDER=1><TR>");
            jump_table = new int[high - low + 1];

            for(i = 0; i < jump_table.length; ++i) {
               jump_table[i] = offset + bytes.readInt();
               buf.append("<TH>" + (low + i) + "</TH>");
            }

            buf.append("<TH>default</TH></TR>\n<TR>");

            for(i = 0; i < jump_table.length; ++i) {
               buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table[i] + "\">" + jump_table[i] + "</A></TD>");
            }

            buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset + "\">" + default_offset + "</A></TD></TR>\n</TABLE>\n");
            break;
         case 171:
            int npairs = bytes.readInt();
            offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
            jump_table = new int[npairs];
            default_offset += offset;
            buf.append("<TABLE BORDER=1><TR>");

            int i;
            for(int i = 0; i < npairs; ++i) {
               i = bytes.readInt();
               jump_table[i] = offset + bytes.readInt();
               buf.append("<TH>" + i + "</TH>");
            }

            buf.append("<TH>default</TH></TR>\n<TR>");

            for(i = 0; i < npairs; ++i) {
               buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table[i] + "\">" + jump_table[i] + "</A></TD>");
            }

            buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset + "\">" + default_offset + "</A></TD></TR>\n</TABLE>\n");
            break;
         case 178:
         case 179:
         case 180:
         case 181:
            index = bytes.readShort();
            ConstantFieldref c1 = (ConstantFieldref)this.constant_pool.getConstant(index, (byte)9);
            class_index = c1.getClassIndex();
            name = this.constant_pool.getConstantString(class_index, (byte)7);
            name = Utility.compactClassName(name, false);
            index = c1.getNameAndTypeIndex();
            String field_name = this.constant_pool.constantToString(index, (byte)12);
            if (name.equals(this.class_name)) {
               buf.append("<A HREF=\"" + this.class_name + "_methods.html#field" + field_name + "\" TARGET=Methods>" + field_name + "</A>\n");
            } else {
               buf.append(this.constant_html.referenceConstant(class_index) + "." + field_name);
            }
            break;
         case 182:
         case 183:
         case 184:
         case 185:
            int m_index = bytes.readShort();
            if (opcode == 185) {
               int nargs = bytes.readUnsignedByte();
               int reserved = bytes.readUnsignedByte();
               ConstantInterfaceMethodref c = (ConstantInterfaceMethodref)this.constant_pool.getConstant(m_index, (byte)11);
               class_index = c.getClassIndex();
               this.constant_pool.constantToString(c);
               index = c.getNameAndTypeIndex();
            } else {
               ConstantMethodref c = (ConstantMethodref)this.constant_pool.getConstant(m_index, (byte)10);
               class_index = c.getClassIndex();
               this.constant_pool.constantToString(c);
               index = c.getNameAndTypeIndex();
            }

            name = Class2HTML.referenceClass(class_index);
            String str = Class2HTML.toHTML(this.constant_pool.constantToString(this.constant_pool.getConstant(index, (byte)12)));
            ConstantNameAndType c2 = (ConstantNameAndType)this.constant_pool.getConstant(index, (byte)12);
            String signature = this.constant_pool.constantToString(c2.getSignatureIndex(), (byte)1);
            String[] args = Utility.methodSignatureArgumentTypes(signature, false);
            String type = Utility.methodSignatureReturnType(signature, false);
            buf.append(name + ".<A HREF=\"" + this.class_name + "_cp.html#cp" + m_index + "\" TARGET=ConstantPool>" + str + "</A>" + "(");

            for(int i = 0; i < args.length; ++i) {
               buf.append(Class2HTML.referenceType(args[i]));
               if (i < args.length - 1) {
                  buf.append(", ");
               }
            }

            buf.append("):" + Class2HTML.referenceType(type));
            break;
         case 187:
         case 192:
         case 193:
            index = bytes.readShort();
            buf.append(this.constant_html.referenceConstant(index));
            break;
         case 188:
            buf.append("<FONT COLOR=\"#00FF00\">" + Constants.TYPE_NAMES[bytes.readByte()] + "</FONT>");
            break;
         case 189:
            index = bytes.readShort();
            buf.append(this.constant_html.referenceConstant(index));
            break;
         case 196:
            wide = true;
            buf.append("(wide)");
            break;
         case 197:
            index = bytes.readShort();
            int dimensions = bytes.readByte();
            buf.append(this.constant_html.referenceConstant(index) + ":" + dimensions + "-dimensional");
            break;
         case 200:
         case 201:
            int windex = bytes.getIndex() + bytes.readInt() - 1;
            buf.append("<A HREF=\"#code" + method_number + "@" + windex + "\">" + windex + "</A>");
      }

      buf.append("</TD>");
      return buf.toString();
   }

   private final void findGotos(ByteSequence bytes, Method method, Code code) throws IOException {
      this.goto_set = new BitSet(bytes.available());
      int remainder;
      int no_pad_bytes;
      int offset;
      int npairs;
      int j;
      int match;
      if (code != null) {
         CodeException[] ce = code.getExceptionTable();
         remainder = ce.length;

         for(no_pad_bytes = 0; no_pad_bytes < remainder; ++no_pad_bytes) {
            this.goto_set.set(ce[no_pad_bytes].getStartPC());
            this.goto_set.set(ce[no_pad_bytes].getEndPC());
            this.goto_set.set(ce[no_pad_bytes].getHandlerPC());
         }

         Attribute[] attributes = code.getAttributes();

         label74:
         for(offset = 0; offset < attributes.length; ++offset) {
            if (attributes[offset].getTag() == 5) {
               LocalVariable[] vars = ((LocalVariableTable)attributes[offset]).getLocalVariableTable();
               npairs = 0;

               while(true) {
                  if (npairs >= vars.length) {
                     break label74;
                  }

                  j = vars[npairs].getStartPC();
                  match = j + vars[npairs].getLength();
                  this.goto_set.set(j);
                  this.goto_set.set(match);
                  ++npairs;
               }
            }
         }
      }

      for(int i = 0; bytes.available() > 0; ++i) {
         int opcode = bytes.readUnsignedByte();
         int index;
         switch (opcode) {
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 198:
            case 199:
               index = bytes.getIndex() + bytes.readShort() - 1;
               this.goto_set.set(index);
               break;
            case 169:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            default:
               bytes.unreadByte();
               this.codeToHTML(bytes, 0);
               break;
            case 170:
            case 171:
               remainder = bytes.getIndex() % 4;
               no_pad_bytes = remainder == 0 ? 0 : 4 - remainder;

               for(int j = 0; j < no_pad_bytes; ++j) {
                  bytes.readByte();
               }

               int default_offset = bytes.readInt();
               if (opcode == 170) {
                  npairs = bytes.readInt();
                  j = bytes.readInt();
                  offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
                  default_offset += offset;
                  this.goto_set.set(default_offset);

                  for(match = 0; match < j - npairs + 1; ++match) {
                     index = offset + bytes.readInt();
                     this.goto_set.set(index);
                  }
               } else {
                  npairs = bytes.readInt();
                  offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
                  default_offset += offset;
                  this.goto_set.set(default_offset);

                  for(j = 0; j < npairs; ++j) {
                     match = bytes.readInt();
                     index = offset + bytes.readInt();
                     this.goto_set.set(index);
                  }
               }
               break;
            case 200:
            case 201:
               index = bytes.getIndex() + bytes.readInt() - 1;
               this.goto_set.set(index);
         }
      }

   }

   private void writeMethod(Method method, int method_number) throws IOException {
      String signature = method.getSignature();
      String[] args = Utility.methodSignatureArgumentTypes(signature, false);
      String type = Utility.methodSignatureReturnType(signature, false);
      String name = method.getName();
      String html_name = Class2HTML.toHTML(name);
      String access = Utility.accessToString(method.getAccessFlags());
      access = Utility.replace(access, " ", "&nbsp;");
      Attribute[] attributes = method.getAttributes();
      this.file.print("<P><B><FONT COLOR=\"#FF0000\">" + access + "</FONT>&nbsp;" + "<A NAME=method" + method_number + ">" + Class2HTML.referenceType(type) + "</A>&nbsp<A HREF=\"" + this.class_name + "_methods.html#method" + method_number + "\" TARGET=Methods>" + html_name + "</A>(");

      for(int i = 0; i < args.length; ++i) {
         this.file.print(Class2HTML.referenceType(args[i]));
         if (i < args.length - 1) {
            this.file.print(",&nbsp;");
         }
      }

      this.file.println(")</B></P>");
      Code c = null;
      byte[] code = null;
      int tag;
      if (attributes.length > 0) {
         this.file.print("<H4>Attributes</H4><UL>\n");

         for(int i = 0; i < attributes.length; ++i) {
            tag = attributes[i].getTag();
            if (tag != -1) {
               this.file.print("<LI><A HREF=\"" + this.class_name + "_attributes.html#method" + method_number + "@" + i + "\" TARGET=Attributes>" + Constants.ATTRIBUTE_NAMES[tag] + "</A></LI>\n");
            } else {
               this.file.print("<LI>" + attributes[i] + "</LI>");
            }

            if (tag == 2) {
               c = (Code)attributes[i];
               Attribute[] attributes2 = c.getAttributes();
               code = c.getCode();
               this.file.print("<UL>");

               for(int j = 0; j < attributes2.length; ++j) {
                  tag = attributes2[j].getTag();
                  this.file.print("<LI><A HREF=\"" + this.class_name + "_attributes.html#" + "method" + method_number + "@" + i + "@" + j + "\" TARGET=Attributes>" + Constants.ATTRIBUTE_NAMES[tag] + "</A></LI>\n");
               }

               this.file.print("</UL>");
            }
         }

         this.file.println("</UL>");
      }

      if (code != null) {
         ByteSequence stream = new ByteSequence(code);
         stream.mark(stream.available());
         this.findGotos(stream, method, c);
         stream.reset();
         this.file.println("<TABLE BORDER=0><TR><TH ALIGN=LEFT>Byte<BR>offset</TH><TH ALIGN=LEFT>Instruction</TH><TH ALIGN=LEFT>Argument</TH>");

         for(tag = 0; stream.available() > 0; ++tag) {
            int offset = stream.getIndex();
            String str = this.codeToHTML(stream, method_number);
            String anchor = "";
            if (this.goto_set.get(offset)) {
               anchor = "<A NAME=code" + method_number + "@" + offset + "></A>";
            }

            String anchor2;
            if (stream.getIndex() == code.length) {
               anchor2 = "<A NAME=code" + method_number + "@" + code.length + ">" + offset + "</A>";
            } else {
               anchor2 = "" + offset;
            }

            this.file.println("<TR VALIGN=TOP><TD>" + anchor2 + "</TD><TD>" + anchor + str + "</TR>");
         }

         this.file.println("<TR><TD> </A></TD></TR>");
         this.file.println("</TABLE>");
      }

   }
}
