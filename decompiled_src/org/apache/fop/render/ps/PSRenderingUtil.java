package org.apache.fop.render.ps;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.ps.extensions.PSCommentAfter;
import org.apache.fop.render.ps.extensions.PSCommentBefore;
import org.apache.fop.render.ps.extensions.PSExtensionAttachment;
import org.apache.fop.render.ps.extensions.PSSetupCode;
import org.apache.xmlgraphics.ps.PSGenerator;

public class PSRenderingUtil implements PSConfigurationConstants {
   private FOUserAgent userAgent;
   private boolean safeSetPageDevice = false;
   private boolean dscCompliant = true;
   private boolean autoRotateLandscape = false;
   private int languageLevel = 3;
   private boolean optimizeResources = false;

   PSRenderingUtil(FOUserAgent userAgent) {
      this.userAgent = userAgent;
      this.initialize();
   }

   private void initialize() {
      Object obj = this.userAgent.getRendererOptions().get("auto-rotate-landscape");
      if (obj != null) {
         this.setAutoRotateLandscape(this.booleanValueOf(obj));
      }

      obj = this.userAgent.getRendererOptions().get("language-level");
      if (obj != null) {
         this.setLanguageLevel(this.intValueOf(obj));
      }

      obj = this.userAgent.getRendererOptions().get("optimize-resources");
      if (obj != null) {
         this.setOptimizeResources(this.booleanValueOf(obj));
      }

   }

   private boolean booleanValueOf(Object obj) {
      if (obj instanceof Boolean) {
         return (Boolean)obj;
      } else if (obj instanceof String) {
         return Boolean.valueOf((String)obj);
      } else {
         throw new IllegalArgumentException("Boolean or \"true\" or \"false\" expected.");
      }
   }

   private int intValueOf(Object obj) {
      if (obj instanceof Integer) {
         return (Integer)obj;
      } else if (obj instanceof String) {
         return Integer.parseInt((String)obj);
      } else {
         throw new IllegalArgumentException("Integer or String with a number expected.");
      }
   }

   public static void writeSetupCodeList(PSGenerator gen, List setupCodeList, String type) throws IOException {
      if (setupCodeList != null) {
         Iterator i = setupCodeList.iterator();

         while(i.hasNext()) {
            PSSetupCode setupCode = (PSSetupCode)i.next();
            gen.commentln("%FOPBegin" + type + ": (" + (setupCode.getName() != null ? setupCode.getName() : "") + ")");
            LineNumberReader reader = new LineNumberReader(new StringReader(setupCode.getContent()));

            String line;
            while((line = reader.readLine()) != null) {
               line = line.trim();
               if (line.length() > 0) {
                  gen.writeln(line.trim());
               }
            }

            gen.commentln("%FOPEnd" + type);
            i.remove();
         }
      }

   }

   public static void writeEnclosedExtensionAttachments(PSGenerator gen, Collection attachmentCollection) throws IOException {
      for(Iterator iter = attachmentCollection.iterator(); iter.hasNext(); iter.remove()) {
         PSExtensionAttachment attachment = (PSExtensionAttachment)iter.next();
         if (attachment != null) {
            writeEnclosedExtensionAttachment(gen, attachment);
         }
      }

   }

   public static void writeEnclosedExtensionAttachment(PSGenerator gen, PSExtensionAttachment attachment) throws IOException {
      if (attachment instanceof PSCommentBefore) {
         gen.commentln("%" + attachment.getContent());
      } else if (attachment instanceof PSCommentAfter) {
         gen.commentln("%" + attachment.getContent());
      } else {
         String info = "";
         if (attachment instanceof PSSetupCode) {
            PSSetupCode setupCodeAttach = (PSSetupCode)attachment;
            String name = setupCodeAttach.getName();
            if (name != null) {
               info = info + ": (" + name + ")";
            }
         }

         String type = attachment.getType();
         gen.commentln("%FOPBegin" + type + info);
         LineNumberReader reader = new LineNumberReader(new StringReader(attachment.getContent()));

         String line;
         while((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0) {
               gen.writeln(line);
            }
         }

         gen.commentln("%FOPEnd" + type);
      }

   }

   public void setSafeSetPageDevice(boolean value) {
      this.safeSetPageDevice = value;
   }

   public boolean isSafeSetPageDevice() {
      return this.safeSetPageDevice;
   }

   public void setDSCComplianceEnabled(boolean value) {
      this.dscCompliant = value;
   }

   public boolean isDSCComplianceEnabled() {
      return this.dscCompliant;
   }

   public void setAutoRotateLandscape(boolean value) {
      this.autoRotateLandscape = value;
   }

   public boolean isAutoRotateLandscape() {
      return this.autoRotateLandscape;
   }

   public void setLanguageLevel(int level) {
      if (level != 2 && level != 3) {
         throw new IllegalArgumentException("Only language levels 2 or 3 are allowed/supported");
      } else {
         this.languageLevel = level;
      }
   }

   public int getLanguageLevel() {
      return this.languageLevel;
   }

   public void setOptimizeResources(boolean value) {
      this.optimizeResources = value;
   }

   public boolean isOptimizeResources() {
      return this.optimizeResources;
   }
}
