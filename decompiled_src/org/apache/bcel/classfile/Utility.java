package org.apache.bcel.classfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.FilterReader;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.bcel.Constants;
import org.apache.bcel.util.ByteSequence;

public abstract class Utility {
   private static int consumed_chars;
   private static boolean wide = false;
   private static final int FREE_CHARS = 48;
   private static int[] CHAR_MAP = new int[48];
   private static int[] MAP_CHAR = new int[256];
   private static final char ESCAPE_CHAR = '$';

   public static final String accessToString(int access_flags) {
      return accessToString(access_flags, false);
   }

   public static final String accessToString(int access_flags, boolean for_class) {
      StringBuffer buf = new StringBuffer();
      int p = 0;

      for(int i = 0; p < 1024; ++i) {
         p = pow2(i);
         if ((access_flags & p) != 0 && (!for_class || p != 32 && p != 512)) {
            buf.append(Constants.ACCESS_NAMES[i] + " ");
         }
      }

      return buf.toString().trim();
   }

   public static final String classOrInterface(int access_flags) {
      return (access_flags & 512) != 0 ? "interface" : "class";
   }

   public static final String codeToString(byte[] code, ConstantPool constant_pool, int index, int length, boolean verbose) {
      StringBuffer buf = new StringBuffer(code.length * 20);
      ByteSequence stream = new ByteSequence(code);

      try {
         for(int i = 0; i < index; ++i) {
            codeToString(stream, constant_pool, verbose);
         }

         for(int i = 0; stream.available() > 0; ++i) {
            if (length < 0 || i < length) {
               String indices = fillup(stream.getIndex() + ":", 6, true, ' ');
               buf.append(indices + codeToString(stream, constant_pool, verbose) + '\n');
            }
         }

         return buf.toString();
      } catch (IOException var10) {
         System.out.println(buf.toString());
         var10.printStackTrace();
         throw new ClassFormatError("Byte code error: " + var10);
      }
   }

   public static final String codeToString(byte[] code, ConstantPool constant_pool, int index, int length) {
      return codeToString(code, constant_pool, index, length, true);
   }

   public static final String codeToString(ByteSequence bytes, ConstantPool constant_pool, boolean verbose) throws IOException {
      short opcode = (short)bytes.readUnsignedByte();
      int default_offset = 0;
      int no_pad_bytes = 0;
      StringBuffer buf = new StringBuffer(Constants.OPCODE_NAMES[opcode]);
      int i;
      int i;
      int i;
      if (opcode == 170 || opcode == 171) {
         i = bytes.getIndex() % 4;
         no_pad_bytes = i == 0 ? 0 : 4 - i;

         for(i = 0; i < no_pad_bytes; ++i) {
            if ((i = bytes.readByte()) != 0) {
               System.err.println("Warning: Padding byte != 0 in " + Constants.OPCODE_NAMES[opcode] + ":" + i);
            }
         }

         default_offset = bytes.readInt();
      }

      int index;
      int vindex;
      int[] jump_table;
      int offset;
      switch (opcode) {
         case 18:
            index = bytes.readUnsignedByte();
            buf.append("\t\t" + constant_pool.constantToString(index, constant_pool.getConstant(index).getTag()) + (verbose ? " (" + index + ")" : ""));
            break;
         case 19:
         case 20:
            index = bytes.readUnsignedShort();
            buf.append("\t\t" + constant_pool.constantToString(index, constant_pool.getConstant(index).getTag()) + (verbose ? " (" + index + ")" : ""));
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
               vindex = bytes.readUnsignedShort();
               wide = false;
            } else {
               vindex = bytes.readUnsignedByte();
            }

            buf.append("\t\t%" + vindex);
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
               for(i = 0; i < Constants.TYPE_OF_OPERANDS[opcode].length; ++i) {
                  buf.append("\t\t");
                  switch (Constants.TYPE_OF_OPERANDS[opcode][i]) {
                     case 8:
                        buf.append(bytes.readByte());
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
               }
            }
            break;
         case 132:
            short constant;
            if (wide) {
               vindex = bytes.readUnsignedShort();
               constant = bytes.readShort();
               wide = false;
            } else {
               vindex = bytes.readUnsignedByte();
               constant = bytes.readByte();
            }

            buf.append("\t\t%" + vindex + "\t" + constant);
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
            buf.append("\t\t#" + (bytes.getIndex() - 1 + bytes.readShort()));
            break;
         case 170:
            int low = bytes.readInt();
            int high = bytes.readInt();
            offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
            default_offset += offset;
            buf.append("\tdefault = " + default_offset + ", low = " + low + ", high = " + high + "(");
            jump_table = new int[high - low + 1];

            for(i = 0; i < jump_table.length; ++i) {
               jump_table[i] = offset + bytes.readInt();
               buf.append(jump_table[i]);
               if (i < jump_table.length - 1) {
                  buf.append(", ");
               }
            }

            buf.append(")");
            break;
         case 171:
            int npairs = bytes.readInt();
            offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
            int[] match = new int[npairs];
            jump_table = new int[npairs];
            default_offset += offset;
            buf.append("\tdefault = " + default_offset + ", npairs = " + npairs + " (");

            for(i = 0; i < npairs; ++i) {
               match[i] = bytes.readInt();
               jump_table[i] = offset + bytes.readInt();
               buf.append("(" + match[i] + ", " + jump_table[i] + ")");
               if (i < npairs - 1) {
                  buf.append(", ");
               }
            }

            buf.append(")");
            break;
         case 178:
         case 179:
         case 180:
         case 181:
            index = bytes.readUnsignedShort();
            buf.append("\t\t" + constant_pool.constantToString(index, (byte)9) + (verbose ? " (" + index + ")" : ""));
            break;
         case 182:
         case 183:
         case 184:
            index = bytes.readUnsignedShort();
            buf.append("\t" + constant_pool.constantToString(index, (byte)10) + (verbose ? " (" + index + ")" : ""));
            break;
         case 185:
            index = bytes.readUnsignedShort();
            i = bytes.readUnsignedByte();
            buf.append("\t" + constant_pool.constantToString(index, (byte)11) + (verbose ? " (" + index + ")\t" : "") + i + "\t" + bytes.readUnsignedByte());
            break;
         case 187:
         case 192:
            buf.append("\t");
         case 193:
            index = bytes.readUnsignedShort();
            buf.append("\t<" + constant_pool.constantToString(index, (byte)7) + ">" + (verbose ? " (" + index + ")" : ""));
            break;
         case 188:
            buf.append("\t\t<" + Constants.TYPE_NAMES[bytes.readByte()] + ">");
            break;
         case 189:
            index = bytes.readUnsignedShort();
            buf.append("\t\t<" + compactClassName(constant_pool.getConstantString(index, (byte)7), false) + ">" + (verbose ? " (" + index + ")" : ""));
            break;
         case 196:
            wide = true;
            buf.append("\t(wide)");
            break;
         case 197:
            index = bytes.readUnsignedShort();
            i = bytes.readUnsignedByte();
            buf.append("\t<" + compactClassName(constant_pool.getConstantString(index, (byte)7), false) + ">\t" + i + (verbose ? " (" + index + ")" : ""));
            break;
         case 200:
         case 201:
            buf.append("\t\t#" + (bytes.getIndex() - 1 + bytes.readInt()));
      }

      return buf.toString();
   }

   public static final String codeToString(ByteSequence bytes, ConstantPool constant_pool) throws IOException {
      return codeToString(bytes, constant_pool, true);
   }

   public static final String compactClassName(String str) {
      return compactClassName(str, true);
   }

   public static final String compactClassName(String str, String prefix, boolean chopit) {
      int len = prefix.length();
      str = str.replace('/', '.');
      if (chopit && str.startsWith(prefix) && str.substring(len).indexOf(46) == -1) {
         str = str.substring(len);
      }

      return str;
   }

   public static final String compactClassName(String str, boolean chopit) {
      return compactClassName(str, "java.lang.", chopit);
   }

   private static final boolean is_digit(char ch) {
      return ch >= '0' && ch <= '9';
   }

   private static final boolean is_space(char ch) {
      return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
   }

   public static final int setBit(int flag, int i) {
      return flag | pow2(i);
   }

   public static final int clearBit(int flag, int i) {
      int bit = pow2(i);
      return (flag & bit) == 0 ? flag : flag ^ bit;
   }

   public static final boolean isSet(int flag, int i) {
      return (flag & pow2(i)) != 0;
   }

   public static final String methodTypeToSignature(String ret, String[] argv) throws ClassFormatError {
      StringBuffer buf = new StringBuffer("(");
      String str;
      if (argv != null) {
         for(int i = 0; i < argv.length; ++i) {
            str = getSignature(argv[i]);
            if (str.endsWith("V")) {
               throw new ClassFormatError("Invalid type: " + argv[i]);
            }

            buf.append(str);
         }
      }

      str = getSignature(ret);
      buf.append(")" + str);
      return buf.toString();
   }

   public static final String[] methodSignatureArgumentTypes(String signature) throws ClassFormatError {
      return methodSignatureArgumentTypes(signature, true);
   }

   public static final String[] methodSignatureArgumentTypes(String signature, boolean chopit) throws ClassFormatError {
      ArrayList vec = new ArrayList();

      try {
         if (signature.charAt(0) != '(') {
            throw new ClassFormatError("Invalid method signature: " + signature);
         }

         for(int index = 1; signature.charAt(index) != ')'; index += consumed_chars) {
            vec.add(signatureToString(signature.substring(index), chopit));
         }
      } catch (StringIndexOutOfBoundsException var6) {
         throw new ClassFormatError("Invalid method signature: " + signature);
      }

      String[] types = new String[vec.size()];
      vec.toArray(types);
      return types;
   }

   public static final String methodSignatureReturnType(String signature) throws ClassFormatError {
      return methodSignatureReturnType(signature, true);
   }

   public static final String methodSignatureReturnType(String signature, boolean chopit) throws ClassFormatError {
      try {
         int index = signature.lastIndexOf(41) + 1;
         String type = signatureToString(signature.substring(index), chopit);
         return type;
      } catch (StringIndexOutOfBoundsException var5) {
         throw new ClassFormatError("Invalid method signature: " + signature);
      }
   }

   public static final String methodSignatureToString(String signature, String name, String access) {
      return methodSignatureToString(signature, name, access, true);
   }

   public static final String methodSignatureToString(String signature, String name, String access, boolean chopit) {
      return methodSignatureToString(signature, name, access, chopit, (LocalVariableTable)null);
   }

   public static final String methodSignatureToString(String signature, String name, String access, boolean chopit, LocalVariableTable vars) throws ClassFormatError {
      StringBuffer buf = new StringBuffer("(");
      int var_index = access.indexOf("static") >= 0 ? 0 : 1;

      String type;
      try {
         if (signature.charAt(0) != '(') {
            throw new ClassFormatError("Invalid method signature: " + signature);
         }

         int index = 1;

         while(true) {
            if (signature.charAt(index) == ')') {
               ++index;
               type = signatureToString(signature.substring(index), chopit);
               break;
            }

            buf.append(signatureToString(signature.substring(index), chopit));
            if (vars != null) {
               LocalVariable l = vars.getLocalVariable(var_index);
               if (l != null) {
                  buf.append(" " + l.getName());
               }
            } else {
               buf.append(" arg" + var_index);
            }

            ++var_index;
            buf.append(", ");
            index += consumed_chars;
         }
      } catch (StringIndexOutOfBoundsException var10) {
         throw new ClassFormatError("Invalid method signature: " + signature);
      }

      if (buf.length() > 1) {
         buf.setLength(buf.length() - 2);
      }

      buf.append(")");
      return access + (access.length() > 0 ? " " : "") + type + " " + name + buf.toString();
   }

   private static final int pow2(int n) {
      return 1 << n;
   }

   public static final String replace(String str, String old, String new_) {
      StringBuffer buf = new StringBuffer();

      try {
         if (str.indexOf(old) != -1) {
            int index;
            int old_index;
            for(old_index = 0; (index = str.indexOf(old, old_index)) != -1; old_index = index + old.length()) {
               buf.append(str.substring(old_index, index));
               buf.append(new_);
            }

            buf.append(str.substring(old_index));
            str = buf.toString();
         }
      } catch (StringIndexOutOfBoundsException var7) {
         System.err.println(var7);
      }

      return str;
   }

   public static final String signatureToString(String signature) {
      return signatureToString(signature, true);
   }

   public static final String signatureToString(String signature, boolean chopit) {
      Utility.consumed_chars = 1;

      try {
         int n;
         switch (signature.charAt(0)) {
            case 'B':
               return "byte";
            case 'C':
               return "char";
            case 'D':
               return "double";
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
               throw new ClassFormatError("Invalid signature: `" + signature + "'");
            case 'F':
               return "float";
            case 'I':
               return "int";
            case 'J':
               return "long";
            case 'L':
               n = signature.indexOf(59);
               if (n < 0) {
                  throw new ClassFormatError("Invalid signature: " + signature);
               }

               Utility.consumed_chars = n + 1;
               return compactClassName(signature.substring(1, n), chopit);
            case 'S':
               return "short";
            case 'V':
               return "void";
            case 'Z':
               return "boolean";
            case '[':
               StringBuffer brackets = new StringBuffer();

               for(n = 0; signature.charAt(n) == '['; ++n) {
                  brackets.append("[]");
               }

               String type = signatureToString(signature.substring(n), chopit);
               Utility.consumed_chars += n;
               return type + brackets.toString();
         }
      } catch (StringIndexOutOfBoundsException var8) {
         throw new ClassFormatError("Invalid signature: " + var8 + ":" + signature);
      }
   }

   public static String getSignature(String type) {
      StringBuffer buf = new StringBuffer();
      char[] chars = type.toCharArray();
      boolean char_found = false;
      boolean delim = false;
      int index = -1;

      label58:
      for(int i = 0; i < chars.length; ++i) {
         switch (chars[i]) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
               if (char_found) {
                  delim = true;
               }
               break;
            case '[':
               if (!char_found) {
                  throw new RuntimeException("Illegal type: " + type);
               }

               index = i;
               break label58;
            default:
               char_found = true;
               if (!delim) {
                  buf.append(chars[i]);
               }
         }
      }

      int brackets = 0;
      if (index > 0) {
         brackets = countBrackets(type.substring(index));
      }

      type = buf.toString();
      buf.setLength(0);

      for(int i = 0; i < brackets; ++i) {
         buf.append('[');
      }

      boolean found = false;

      for(int i = 4; i <= 12 && !found; ++i) {
         if (Constants.TYPE_NAMES[i].equals(type)) {
            found = true;
            buf.append(Constants.SHORT_TYPE_NAMES[i]);
         }
      }

      if (!found) {
         buf.append('L' + type.replace('.', '/') + ';');
      }

      return buf.toString();
   }

   private static int countBrackets(String brackets) {
      char[] chars = brackets.toCharArray();
      int count = 0;
      boolean open = false;

      for(int i = 0; i < chars.length; ++i) {
         switch (chars[i]) {
            case '[':
               if (open) {
                  throw new RuntimeException("Illegally nested brackets:" + brackets);
               }

               open = true;
               break;
            case ']':
               if (!open) {
                  throw new RuntimeException("Illegally nested brackets:" + brackets);
               }

               open = false;
               ++count;
         }
      }

      if (open) {
         throw new RuntimeException("Illegally nested brackets:" + brackets);
      } else {
         return count;
      }
   }

   public static final byte typeOfMethodSignature(String signature) throws ClassFormatError {
      try {
         if (signature.charAt(0) != '(') {
            throw new ClassFormatError("Invalid method signature: " + signature);
         } else {
            int index = signature.lastIndexOf(41) + 1;
            return typeOfSignature(signature.substring(index));
         }
      } catch (StringIndexOutOfBoundsException var3) {
         throw new ClassFormatError("Invalid method signature: " + signature);
      }
   }

   public static final byte typeOfSignature(String signature) throws ClassFormatError {
      try {
         switch (signature.charAt(0)) {
            case 'B':
               return 8;
            case 'C':
               return 5;
            case 'D':
               return 7;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
               throw new ClassFormatError("Invalid method signature: " + signature);
            case 'F':
               return 6;
            case 'I':
               return 10;
            case 'J':
               return 11;
            case 'L':
               return 14;
            case 'S':
               return 9;
            case 'V':
               return 12;
            case 'Z':
               return 4;
            case '[':
               return 13;
         }
      } catch (StringIndexOutOfBoundsException var2) {
         throw new ClassFormatError("Invalid method signature: " + signature);
      }
   }

   public static short searchOpcode(String name) {
      name = name.toLowerCase();

      for(short i = 0; i < Constants.OPCODE_NAMES.length; ++i) {
         if (Constants.OPCODE_NAMES[i].equals(name)) {
            return i;
         }
      }

      return -1;
   }

   private static final short byteToShort(byte b) {
      return b < 0 ? (short)(256 + b) : (short)b;
   }

   public static final String toHexString(byte[] bytes) {
      StringBuffer buf = new StringBuffer();

      for(int i = 0; i < bytes.length; ++i) {
         short b = byteToShort(bytes[i]);
         String hex = Integer.toString(b, 16);
         if (b < 16) {
            buf.append('0');
         }

         buf.append(hex);
         if (i < bytes.length - 1) {
            buf.append(' ');
         }
      }

      return buf.toString();
   }

   public static final String format(int i, int length, boolean left_justify, char fill) {
      return fillup(Integer.toString(i), length, left_justify, fill);
   }

   public static final String fillup(String str, int length, boolean left_justify, char fill) {
      int len = length - str.length();
      char[] buf = new char[len < 0 ? 0 : len];

      for(int j = 0; j < buf.length; ++j) {
         buf[j] = fill;
      }

      return left_justify ? str + new String(buf) : new String(buf) + str;
   }

   static final boolean equals(byte[] a, byte[] b) {
      int size;
      if ((size = a.length) != b.length) {
         return false;
      } else {
         for(int i = 0; i < size; ++i) {
            if (a[i] != b[i]) {
               return false;
            }
         }

         return true;
      }
   }

   public static final void printArray(PrintStream out, Object[] obj) {
      out.println(printArray(obj, true));
   }

   public static final void printArray(PrintWriter out, Object[] obj) {
      out.println(printArray(obj, true));
   }

   public static final String printArray(Object[] obj) {
      return printArray(obj, true);
   }

   public static final String printArray(Object[] obj, boolean braces) {
      if (obj == null) {
         return null;
      } else {
         StringBuffer buf = new StringBuffer();
         if (braces) {
            buf.append('{');
         }

         for(int i = 0; i < obj.length; ++i) {
            if (obj[i] != null) {
               buf.append(obj[i].toString());
            } else {
               buf.append("null");
            }

            if (i < obj.length - 1) {
               buf.append(", ");
            }
         }

         if (braces) {
            buf.append('}');
         }

         return buf.toString();
      }
   }

   public static boolean isJavaIdentifierPart(char ch) {
      return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9' || ch == '_';
   }

   public static String encode(byte[] bytes, boolean compress) throws IOException {
      if (compress) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         GZIPOutputStream gos = new GZIPOutputStream(baos);
         gos.write(bytes, 0, bytes.length);
         gos.close();
         baos.close();
         bytes = baos.toByteArray();
      }

      CharArrayWriter caw = new CharArrayWriter();
      JavaWriter jw = new JavaWriter(caw);

      for(int i = 0; i < bytes.length; ++i) {
         int in = bytes[i] & 255;
         jw.write(in);
      }

      return caw.toString();
   }

   public static byte[] decode(String s, boolean uncompress) throws IOException {
      char[] chars = s.toCharArray();
      CharArrayReader car = new CharArrayReader(chars);
      JavaReader jr = new JavaReader(car);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      int ch;
      while((ch = jr.read()) >= 0) {
         bos.write(ch);
      }

      bos.close();
      car.close();
      jr.close();
      byte[] bytes = bos.toByteArray();
      if (uncompress) {
         GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
         byte[] tmp = new byte[bytes.length * 3];

         int count;
         int b;
         for(count = 0; (b = gis.read()) >= 0; tmp[count++] = (byte)b) {
         }

         bytes = new byte[count];
         System.arraycopy(tmp, 0, bytes, 0, count);
      }

      return bytes;
   }

   static {
      int j = 0;
      int k = false;

      for(int i = 65; i <= 90; ++i) {
         CHAR_MAP[j] = i;
         MAP_CHAR[i] = j++;
      }

      for(int i = 103; i <= 122; ++i) {
         CHAR_MAP[j] = i;
         MAP_CHAR[i] = j++;
      }

      CHAR_MAP[j] = 36;
      MAP_CHAR[36] = j++;
      CHAR_MAP[j] = 95;
      MAP_CHAR[95] = j;
   }

   private static class JavaWriter extends FilterWriter {
      public JavaWriter(Writer out) {
         super(out);
      }

      public void write(int b) throws IOException {
         if (Utility.isJavaIdentifierPart((char)b) && b != 36) {
            super.out.write(b);
         } else {
            super.out.write(36);
            if (b >= 0 && b < 48) {
               super.out.write(Utility.CHAR_MAP[b]);
            } else {
               char[] tmp = Integer.toHexString(b).toCharArray();
               if (tmp.length == 1) {
                  super.out.write(48);
                  super.out.write(tmp[0]);
               } else {
                  super.out.write(tmp[0]);
                  super.out.write(tmp[1]);
               }
            }
         }

      }

      public void write(char[] cbuf, int off, int len) throws IOException {
         for(int i = 0; i < len; ++i) {
            this.write(cbuf[off + i]);
         }

      }

      public void write(String str, int off, int len) throws IOException {
         this.write(str.toCharArray(), off, len);
      }
   }

   private static class JavaReader extends FilterReader {
      public JavaReader(Reader in) {
         super(in);
      }

      public int read() throws IOException {
         int b = super.in.read();
         if (b != 36) {
            return b;
         } else {
            int i = super.in.read();
            if (i < 0) {
               return -1;
            } else if ((i < 48 || i > 57) && (i < 97 || i > 102)) {
               return Utility.MAP_CHAR[i];
            } else {
               int j = super.in.read();
               if (j < 0) {
                  return -1;
               } else {
                  char[] tmp = new char[]{(char)i, (char)j};
                  int s = Integer.parseInt(new String(tmp), 16);
                  return s;
               }
            }
         }
      }

      public int read(char[] cbuf, int off, int len) throws IOException {
         for(int i = 0; i < len; ++i) {
            cbuf[off + i] = (char)this.read();
         }

         return len;
      }
   }
}
