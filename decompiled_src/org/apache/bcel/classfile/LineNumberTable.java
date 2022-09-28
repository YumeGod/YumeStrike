package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class LineNumberTable extends Attribute {
   private int line_number_table_length;
   private LineNumber[] line_number_table;

   public LineNumberTable(LineNumberTable c) {
      this(c.getNameIndex(), c.getLength(), c.getLineNumberTable(), c.getConstantPool());
   }

   public LineNumberTable(int name_index, int length, LineNumber[] line_number_table, ConstantPool constant_pool) {
      super((byte)4, name_index, length, constant_pool);
      this.setLineNumberTable(line_number_table);
   }

   LineNumberTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(name_index, length, (LineNumber[])null, constant_pool);
      this.line_number_table_length = file.readUnsignedShort();
      this.line_number_table = new LineNumber[this.line_number_table_length];

      for(int i = 0; i < this.line_number_table_length; ++i) {
         this.line_number_table[i] = new LineNumber(file);
      }

   }

   public void accept(Visitor v) {
      v.visitLineNumberTable(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      super.dump(file);
      file.writeShort(this.line_number_table_length);

      for(int i = 0; i < this.line_number_table_length; ++i) {
         this.line_number_table[i].dump(file);
      }

   }

   public final LineNumber[] getLineNumberTable() {
      return this.line_number_table;
   }

   public final void setLineNumberTable(LineNumber[] line_number_table) {
      this.line_number_table = line_number_table;
      this.line_number_table_length = line_number_table == null ? 0 : line_number_table.length;
   }

   public final String toString() {
      StringBuffer buf = new StringBuffer();
      StringBuffer line = new StringBuffer();

      for(int i = 0; i < this.line_number_table_length; ++i) {
         line.append(this.line_number_table[i].toString());
         if (i < this.line_number_table_length - 1) {
            line.append(", ");
         }

         if (line.length() > 72) {
            line.append('\n');
            buf.append(line);
            line.setLength(0);
         }
      }

      buf.append(line);
      return buf.toString();
   }

   public int getSourceLine(int pos) {
      int l = 0;
      int r = this.line_number_table_length - 1;
      if (r < 0) {
         return -1;
      } else {
         int min_index = -1;
         int min = -1;

         do {
            int i = (l + r) / 2;
            int j = this.line_number_table[i].getStartPC();
            if (j == pos) {
               return this.line_number_table[i].getLineNumber();
            }

            if (pos < j) {
               r = i - 1;
            } else {
               l = i + 1;
            }

            if (j < pos && j > min) {
               min = j;
               min_index = i;
            }
         } while(l <= r);

         return this.line_number_table[min_index].getLineNumber();
      }
   }

   public Attribute copy(ConstantPool constant_pool) {
      LineNumberTable c = (LineNumberTable)this.clone();
      c.line_number_table = new LineNumber[this.line_number_table_length];

      for(int i = 0; i < this.line_number_table_length; ++i) {
         c.line_number_table[i] = this.line_number_table[i].copy();
      }

      c.constant_pool = constant_pool;
      return c;
   }

   public final int getTableLength() {
      return this.line_number_table_length;
   }
}
