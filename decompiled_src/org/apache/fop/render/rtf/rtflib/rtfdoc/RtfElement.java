package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public abstract class RtfElement {
   protected final Writer writer;
   protected final RtfContainer parent;
   protected final RtfAttributes attrib;
   private boolean written;
   private boolean closed;
   private final int id;
   private static int idCounter;

   RtfElement(RtfContainer parent, Writer w) throws IOException {
      this(parent, w, (RtfAttributes)null);
   }

   RtfElement(RtfContainer parent, Writer w, RtfAttributes attr) throws IOException {
      this.id = idCounter++;
      this.parent = parent;
      this.attrib = attr != null ? attr : new RtfAttributes();
      if (this.parent != null) {
         this.parent.addChild(this);
      }

      this.writer = w;
      this.written = false;
   }

   public final void close() throws IOException {
      this.closed = true;
   }

   public final void writeRtf() throws IOException {
      if (!this.written) {
         this.written = true;
         if (this.okToWriteRtf()) {
            this.writeRtfPrefix();
            this.writeRtfContent();
            this.writeRtfSuffix();
         }
      }

   }

   public void newLine() throws IOException {
      this.writer.write("\n");
   }

   protected final void writeControlWord(String word) throws IOException {
      this.writer.write(92);
      this.writer.write(word);
      this.writer.write(32);
   }

   protected final void writeStarControlWord(String word) throws IOException {
      this.writer.write("\\*\\");
      this.writer.write(word);
      this.writer.write(32);
   }

   protected final void writeStarControlWordNS(String word) throws IOException {
      this.writer.write("\\*\\");
      this.writer.write(word);
   }

   protected final void writeControlWordNS(String word) throws IOException {
      this.writer.write(92);
      this.writer.write(word);
   }

   protected void writeRtfPrefix() throws IOException {
   }

   protected abstract void writeRtfContent() throws IOException;

   protected void writeRtfSuffix() throws IOException {
   }

   protected final void writeGroupMark(boolean isStart) throws IOException {
      this.writer.write(isStart ? "{" : "}");
   }

   protected void writeAttributes(RtfAttributes attr, String[] nameList) throws IOException {
      if (attr != null) {
         String name;
         if (nameList != null) {
            for(int i = 0; i < nameList.length; ++i) {
               name = nameList[i];
               if (attr.isSet(name)) {
                  this.writeOneAttribute(name, attr.getValue(name));
               }
            }
         } else {
            Iterator it = attr.nameIterator();

            while(it.hasNext()) {
               name = (String)it.next();
               if (attr.isSet(name)) {
                  this.writeOneAttribute(name, attr.getValue(name));
               }
            }
         }

      }
   }

   protected void writeOneAttribute(String name, Object value) throws IOException {
      String cw = name;
      if (value instanceof Integer) {
         cw = name + value;
      } else if (value instanceof String) {
         cw = name + value;
      } else if (value instanceof RtfAttributes) {
         this.writeControlWord(name);
         this.writeAttributes((RtfAttributes)value, (String[])null);
         return;
      }

      this.writeControlWord(cw);
   }

   protected void writeOneAttributeNS(String name, Object value) throws IOException {
      String cw = name;
      if (value instanceof Integer) {
         cw = name + value;
      } else if (value instanceof String) {
         cw = name + value;
      } else if (value instanceof RtfAttributes) {
         this.writeControlWord(name);
         this.writeAttributes((RtfAttributes)value, (String[])null);
         return;
      }

      this.writeControlWordNS(cw);
   }

   protected boolean okToWriteRtf() {
      return true;
   }

   void dump(Writer w, int indent) throws IOException {
      for(int i = 0; i < indent; ++i) {
         w.write(32);
      }

      w.write(this.toString());
      w.write(10);
      w.flush();
   }

   public String toString() {
      return this == null ? "null" : this.getClass().getName() + " #" + this.id;
   }

   boolean isClosed() {
      return this.closed;
   }

   RtfFile getRtfFile() {
      Object result;
      for(result = this; ((RtfElement)result).parent != null; result = ((RtfElement)result).parent) {
      }

      return (RtfFile)result;
   }

   RtfElement getParentOfClass(Class c) {
      RtfElement result = null;
      RtfElement current = this;

      while(((RtfElement)current).parent != null) {
         current = ((RtfElement)current).parent;
         if (c.isAssignableFrom(current.getClass())) {
            result = current;
            break;
         }
      }

      return (RtfElement)result;
   }

   public abstract boolean isEmpty();

   protected void writeExceptionInRtf(Exception ie) throws IOException {
      this.writeGroupMark(true);
      this.writeControlWord("par");
      this.writeControlWord("fs48");
      RtfStringConverter.getInstance().writeRtfString(this.writer, ie.getClass().getName());
      this.writeControlWord("fs20");
      RtfStringConverter.getInstance().writeRtfString(this.writer, " " + ie.toString());
      this.writeControlWord("par");
      this.writeGroupMark(false);
   }

   public RtfAttributes getRtfAttributes() {
      return this.attrib;
   }
}
