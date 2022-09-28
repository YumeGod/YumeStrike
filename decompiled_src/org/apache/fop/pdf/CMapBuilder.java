package org.apache.fop.pdf;

import java.io.IOException;
import java.io.Writer;

public class CMapBuilder {
   protected String name;
   protected Writer writer;

   public CMapBuilder(Writer writer, String name) {
      this.writer = writer;
      this.name = name;
   }

   public void writeCMap() throws IOException {
      this.writePreStream();
      this.writeStreamComments();
      this.writeCIDInit();
      this.writeCIDSystemInfo();
      this.writeVersion("1");
      this.writeType("1");
      this.writeName(this.name);
      this.writeCodeSpaceRange();
      this.writeCIDRange();
      this.writeBFEntries();
      this.writeWrapUp();
      this.writeStreamAfterComments();
      this.writeUseCMap();
   }

   protected void writePreStream() throws IOException {
   }

   protected void writeStreamComments() throws IOException {
      this.writer.write("%!PS-Adobe-3.0 Resource-CMap\n");
      this.writer.write("%%DocumentNeededResources: ProcSet (CIDInit)\n");
      this.writer.write("%%IncludeResource: ProcSet (CIDInit)\n");
      this.writer.write("%%BeginResource: CMap (" + this.name + ")\n");
      this.writer.write("%%EndComments\n");
   }

   protected void writeCIDInit() throws IOException {
      this.writer.write("/CIDInit /ProcSet findresource begin\n");
      this.writer.write("12 dict begin\n");
      this.writer.write("begincmap\n");
   }

   protected void writeCIDSystemInfo(String registry, String ordering, int supplement) throws IOException {
      this.writer.write("/CIDSystemInfo 3 dict dup begin\n");
      this.writer.write("  /Registry (");
      this.writer.write(registry);
      this.writer.write(") def\n");
      this.writer.write("  /Ordering (");
      this.writer.write(ordering);
      this.writer.write(") def\n");
      this.writer.write("  /Supplement ");
      this.writer.write(Integer.toString(supplement));
      this.writer.write(" def\n");
      this.writer.write("end def\n");
   }

   protected void writeCIDSystemInfo() throws IOException {
      this.writeCIDSystemInfo("Adobe", "Identity", 0);
   }

   protected void writeVersion(String version) throws IOException {
      this.writer.write("/CMapVersion ");
      this.writer.write(version);
      this.writer.write(" def\n");
   }

   protected void writeType(String type) throws IOException {
      this.writer.write("/CMapType ");
      this.writer.write(type);
      this.writer.write(" def\n");
   }

   protected void writeName(String name) throws IOException {
      this.writer.write("/CMapName /");
      this.writer.write(name);
      this.writer.write(" def\n");
   }

   protected void writeCodeSpaceRange() throws IOException {
      this.writeCodeSpaceRange(false);
   }

   protected void writeCodeSpaceRange(boolean singleByte) throws IOException {
      this.writer.write("1 begincodespacerange\n");
      if (singleByte) {
         this.writer.write("<00> <FF>\n");
      } else {
         this.writer.write("<0000> <FFFF>\n");
      }

      this.writer.write("endcodespacerange\n");
   }

   protected void writeCIDRange() throws IOException {
      this.writer.write("1 begincidrange\n");
      this.writer.write("<0000> <FFFF> 0\n");
      this.writer.write("endcidrange\n");
   }

   protected void writeBFEntries() throws IOException {
   }

   protected void writeWrapUp() throws IOException {
      this.writer.write("endcmap\n");
      this.writer.write("CMapName currentdict /CMap defineresource pop\n");
      this.writer.write("end\n");
      this.writer.write("end\n");
   }

   protected void writeStreamAfterComments() throws IOException {
      this.writer.write("%%EndResource\n");
      this.writer.write("%%EOF\n");
   }

   protected void writeUseCMap() {
   }
}
