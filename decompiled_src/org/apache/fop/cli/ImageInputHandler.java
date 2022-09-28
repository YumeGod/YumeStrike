package org.apache.fop.cli;

import java.io.File;
import java.io.StringReader;
import java.util.Vector;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class ImageInputHandler extends InputHandler {
   public ImageInputHandler(File imagefile, File xsltfile, Vector params) {
      super(imagefile, xsltfile, params);
   }

   protected Source createMainSource() {
      return new StreamSource(new StringReader("<image>" + this.sourcefile.toURI().toASCIIString() + "</image>"));
   }

   protected Source createXSLTSource() {
      Source src = super.createXSLTSource();
      if (src == null) {
         src = new StreamSource(this.getClass().getResource("image2fo.xsl").toExternalForm());
      }

      return (Source)src;
   }
}
