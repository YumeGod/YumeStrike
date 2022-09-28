package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.fop.render.rtf.rtflib.exceptions.RtfStructureException;

public class RtfFile extends RtfContainer {
   private RtfHeader header;
   private RtfPageArea pageArea;
   private RtfListTable listTable;
   private RtfDocumentArea docArea;
   private RtfContainer listTableContainer;
   private int listNum = 0;

   public RtfFile(Writer w) throws IOException {
      super((RtfContainer)null, w);
   }

   public RtfHeader startHeader() throws IOException {
      if (this.header != null) {
         throw new RtfStructureException("startHeader called more than once");
      } else {
         this.header = new RtfHeader(this, this.writer);
         this.listTableContainer = new RtfContainer(this, this.writer);
         return this.header;
      }
   }

   public RtfListTable startListTable(RtfAttributes attr) throws IOException {
      ++this.listNum;
      if (this.listTable != null) {
         return this.listTable;
      } else {
         this.listTable = new RtfListTable(this, this.writer, new Integer(this.listNum), attr);
         this.listTableContainer.addChild(this.listTable);
         return this.listTable;
      }
   }

   public RtfListTable getListTable() {
      return this.listTable;
   }

   public RtfPageArea startPageArea() throws IOException {
      if (this.pageArea != null) {
         throw new RtfStructureException("startPageArea called more than once");
      } else {
         if (this.header == null) {
            this.startHeader();
         }

         this.header.close();
         this.pageArea = new RtfPageArea(this, this.writer);
         this.addChild(this.pageArea);
         return this.pageArea;
      }
   }

   public RtfPageArea getPageArea() throws IOException {
      return this.pageArea == null ? this.startPageArea() : this.pageArea;
   }

   public RtfDocumentArea startDocumentArea() throws IOException {
      if (this.docArea != null) {
         throw new RtfStructureException("startDocumentArea called more than once");
      } else {
         if (this.header == null) {
            this.startHeader();
         }

         this.header.close();
         this.docArea = new RtfDocumentArea(this, this.writer);
         this.addChild(this.docArea);
         return this.docArea;
      }
   }

   public RtfDocumentArea getDocumentArea() throws IOException {
      return this.docArea == null ? this.startDocumentArea() : this.docArea;
   }

   protected void writeRtfPrefix() throws IOException {
      this.writeGroupMark(true);
      this.writeControlWord("rtf1");
   }

   protected void writeRtfSuffix() throws IOException {
      this.writeGroupMark(false);
   }

   public synchronized void flush() throws IOException {
      this.writeRtf();
      this.writer.flush();
   }

   public static void main(String[] args) throws Exception {
      Writer w = null;
      if (args.length != 0) {
         String outFile = args[0];
         System.err.println("Outputting RTF to file '" + outFile + "'");
         w = new BufferedWriter(new FileWriter(outFile));
      } else {
         System.err.println("Outputting RTF code to standard output");
         w = new BufferedWriter(new OutputStreamWriter(System.out));
      }

      RtfFile f = new RtfFile(w);
      RtfSection sect = f.startDocumentArea().newSection();
      RtfParagraph p = sect.newParagraph();
      p.newText("Hello, RTF world.\n", (RtfAttributes)null);
      RtfAttributes attr = new RtfAttributes();
      attr.set("b");
      attr.set("i");
      attr.set("fs", 36);
      p.newText("This is bold, italic, 36 points", attr);
      f.flush();
      System.err.println("RtfFile test: all done.");
   }
}
