package org.apache.xmlgraphics.java2d.ps;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import org.apache.xmlgraphics.ps.DSCConstants;

public class PSDocumentGraphics2D extends AbstractPSDocumentGraphics2D {
   public PSDocumentGraphics2D(boolean textAsShapes) {
      super(textAsShapes);
   }

   public PSDocumentGraphics2D(boolean textAsShapes, OutputStream stream, int width, int height) throws IOException {
      this(textAsShapes);
      this.setupDocument(stream, width, height);
   }

   public void nextPage() throws IOException {
      this.closePage();
   }

   protected void writeFileHeader() throws IOException {
      Long pagewidth = new Long((long)this.width);
      Long pageheight = new Long((long)this.height);
      this.gen.writeln("%!PS-Adobe-3.0");
      this.gen.writeDSCComment("Creator", (Object[])(new String[]{"Apache XML Graphics Commons: PostScript Generator for Java2D"}));
      this.gen.writeDSCComment("CreationDate", new Object[]{new Date()});
      this.gen.writeDSCComment("Pages", DSCConstants.ATEND);
      this.gen.writeDSCComment("BoundingBox", new Object[]{ZERO, ZERO, pagewidth, pageheight});
      this.gen.writeDSCComment("LanguageLevel", (Object)(new Integer(this.gen.getPSLevel())));
      this.gen.writeDSCComment("EndComments");
      this.gen.writeDSCComment("BeginDefaults");
      this.gen.writeDSCComment("EndDefaults");
      this.gen.writeDSCComment("BeginProlog");
      this.gen.writeDSCComment("EndProlog");
      this.gen.writeDSCComment("BeginSetup");
      this.writeProcSets();
      if (this.customTextHandler instanceof PSTextHandler) {
         ((PSTextHandler)this.customTextHandler).writeSetup();
      }

      this.gen.writeDSCComment("EndSetup");
   }

   protected void writePageHeader() throws IOException {
      Integer pageNumber = new Integer(this.pagecount);
      this.gen.writeDSCComment("Page", new Object[]{pageNumber.toString(), pageNumber});
      this.gen.writeDSCComment("PageBoundingBox", new Object[]{ZERO, ZERO, new Integer(this.width), new Integer(this.height)});
      this.gen.writeDSCComment("BeginPageSetup");
      this.gen.writeln("<<");
      this.gen.writeln("/PageSize [" + this.width + " " + this.height + "]");
      this.gen.writeln("/ImagingBBox null");
      this.gen.writeln(">> setpagedevice");
      if (this.customTextHandler instanceof PSTextHandler) {
         ((PSTextHandler)this.customTextHandler).writePageSetup();
      }

   }

   protected void writePageTrailer() throws IOException {
      this.gen.showPage();
   }

   public PSDocumentGraphics2D(PSDocumentGraphics2D g) {
      super(g);
   }
}
