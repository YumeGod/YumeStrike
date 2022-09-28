package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;

public class RtfTemplate {
   private static RtfTemplate instance = null;
   private String templateFilePath = null;

   private RtfTemplate() {
   }

   public static RtfTemplate getInstance() {
      if (instance == null) {
         instance = new RtfTemplate();
      }

      return instance;
   }

   public void setTemplateFilePath(String templateFilePath) throws IOException {
      if (templateFilePath == null) {
         this.templateFilePath = null;
      } else {
         this.templateFilePath = templateFilePath.trim();
      }

   }

   public void writeTemplate(RtfHeader header) throws IOException {
      if (this.templateFilePath != null && this.templateFilePath.length() != 0) {
         header.writeGroupMark(true);
         header.writeControlWord("template");
         header.writeRtfString(this.templateFilePath);
         header.writeGroupMark(false);
         header.writeGroupMark(true);
         header.writeControlWord("linkstyles");
         header.writeGroupMark(false);
      }
   }
}
