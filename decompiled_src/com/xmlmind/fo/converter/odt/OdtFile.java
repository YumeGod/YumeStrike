package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.zip.ZipFile;

public class OdtFile extends ZipFile {
   private static final String mimeType = "application/vnd.oasis.opendocument.text";

   public OdtFile() throws Exception {
      this.add("mimetype", "application/vnd.oasis.opendocument.text".getBytes("ASCII"), true);
   }
}
