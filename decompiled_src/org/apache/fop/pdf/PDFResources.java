package org.apache.fop.pdf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.fop.fonts.FontDescriptor;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.fonts.base14.Symbol;
import org.apache.fop.fonts.base14.ZapfDingbats;
import org.apache.fop.util.ColorProfileUtil;

public class PDFResources extends PDFObject {
   protected Map fonts = new HashMap();
   protected Set xObjects = new HashSet();
   protected Set patterns = new HashSet();
   protected Set shadings = new HashSet();
   protected Set gstates = new HashSet();
   protected Map colorSpaces = new HashMap();
   protected Map iccColorSpaces = new HashMap();

   public PDFResources(int objnum) {
      this.setObjectNumber(objnum);
   }

   public void addFont(PDFFont font) {
      this.fonts.put(font.getName(), font);
   }

   public void addFonts(PDFDocument doc, FontInfo fontInfo) {
      Map usedFonts = fontInfo.getUsedFonts();
      Iterator e = usedFonts.keySet().iterator();

      while(true) {
         String f;
         Typeface font;
         do {
            if (!e.hasNext()) {
               return;
            }

            f = (String)e.next();
            font = (Typeface)usedFonts.get(f);
         } while(!font.hadMappingOperations());

         FontDescriptor desc = null;
         if (font instanceof FontDescriptor) {
            desc = (FontDescriptor)font;
         }

         String encoding = font.getEncodingName();
         if (font instanceof Symbol || font instanceof ZapfDingbats) {
            encoding = null;
         }

         this.addFont(doc.getFactory().makeFont(f, font.getEmbedFontName(), encoding, font, desc));
      }
   }

   public void addGState(PDFGState gs) {
      this.gstates.add(gs);
   }

   public void addShading(PDFShading theShading) {
      this.shadings.add(theShading);
   }

   public void addPattern(PDFPattern thePattern) {
      this.patterns.add(thePattern);
   }

   public void addXObject(PDFXObject xObject) {
      this.xObjects.add(xObject);
   }

   public void addColorSpace(PDFICCBasedColorSpace colorSpace) {
      this.colorSpaces.put(colorSpace.getName(), colorSpace);
      String desc = ColorProfileUtil.getICCProfileDescription(colorSpace.getICCStream().getICCProfile());
      this.iccColorSpaces.put(desc, colorSpace);
   }

   public PDFICCBasedColorSpace getICCColorSpaceByProfileName(String desc) {
      PDFICCBasedColorSpace cs = (PDFICCBasedColorSpace)this.iccColorSpaces.get(desc);
      return cs;
   }

   public PDFICCBasedColorSpace getColorSpace(String name) {
      PDFICCBasedColorSpace cs = (PDFICCBasedColorSpace)this.colorSpaces.get(name);
      return cs;
   }

   public String toPDFString() {
      StringBuffer p = new StringBuffer(128);
      p.append(this.getObjectID() + "<<\n");
      Iterator currentShading;
      String currentPattern;
      if (!this.fonts.isEmpty()) {
         p.append("/Font <<\n");
         currentShading = this.fonts.keySet().iterator();

         while(currentShading.hasNext()) {
            currentPattern = (String)currentShading.next();
            p.append("  /" + currentPattern + " " + ((PDFFont)this.fonts.get(currentPattern)).referencePDF() + "\n");
         }

         p.append(">>\n");
      }

      currentShading = null;
      if (!this.shadings.isEmpty()) {
         p.append("/Shading <<\n");
         Iterator iter = this.shadings.iterator();

         while(iter.hasNext()) {
            PDFShading currentShading = (PDFShading)iter.next();
            p.append("  /" + currentShading.getName() + " " + currentShading.referencePDF() + " ");
         }

         p.append(">>\n");
      }

      currentShading = null;
      currentPattern = null;
      Iterator iter;
      if (!this.patterns.isEmpty()) {
         p.append("/Pattern <<\n");
         iter = this.patterns.iterator();

         while(iter.hasNext()) {
            PDFPattern currentPattern = (PDFPattern)iter.next();
            p.append("  /" + currentPattern.getName() + " " + currentPattern.referencePDF() + " ");
         }

         p.append(">>\n");
      }

      currentPattern = null;
      p.append("/ProcSet [ /PDF /ImageB /ImageC /Text ]\n");
      if (this.xObjects != null && !this.xObjects.isEmpty()) {
         p = p.append("/XObject <<\n");

         PDFXObject xobj;
         for(iter = this.xObjects.iterator(); iter.hasNext(); p = p.append("  " + xobj.getName() + " " + xobj.referencePDF() + "\n")) {
            xobj = (PDFXObject)iter.next();
         }

         p = p.append(">>\n");
      }

      if (!this.gstates.isEmpty()) {
         p = p.append("/ExtGState <<\n");

         PDFGState gs;
         for(iter = this.gstates.iterator(); iter.hasNext(); p = p.append("  /" + gs.getName() + " " + gs.referencePDF() + "\n")) {
            gs = (PDFGState)iter.next();
         }

         p = p.append(">>\n");
      }

      if (!this.colorSpaces.isEmpty()) {
         p = p.append("/ColorSpace <<\n");

         PDFICCBasedColorSpace colorSpace;
         for(iter = this.colorSpaces.values().iterator(); iter.hasNext(); p = p.append("  /" + colorSpace.getName() + " " + colorSpace.referencePDF() + "\n")) {
            colorSpace = (PDFICCBasedColorSpace)iter.next();
         }

         p = p.append(">>\n");
      }

      p = p.append(">>\nendobj\n");
      return p.toString();
   }
}
