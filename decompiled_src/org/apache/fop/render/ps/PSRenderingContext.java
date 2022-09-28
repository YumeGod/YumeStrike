package org.apache.fop.render.ps;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.render.AbstractRenderingContext;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PSRenderingContext extends AbstractRenderingContext {
   private PSGenerator gen;
   private FontInfo fontInfo;
   private boolean createForms;

   public PSRenderingContext(FOUserAgent userAgent, PSGenerator gen, FontInfo fontInfo) {
      this(userAgent, gen, fontInfo, false);
   }

   public PSRenderingContext(FOUserAgent userAgent, PSGenerator gen, FontInfo fontInfo, boolean createForms) {
      super(userAgent);
      this.gen = gen;
      this.fontInfo = fontInfo;
      this.createForms = createForms;
   }

   public String getMimeType() {
      return "application/postscript";
   }

   public PSGenerator getGenerator() {
      return this.gen;
   }

   public FontInfo getFontInfo() {
      return this.fontInfo;
   }

   public boolean isCreateForms() {
      return this.createForms;
   }

   public PSRenderingContext toFormContext() {
      return new PSRenderingContext(this.getUserAgent(), this.getGenerator(), this.getFontInfo(), true);
   }
}
