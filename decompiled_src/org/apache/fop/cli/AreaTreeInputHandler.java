package org.apache.fop.cli;

import java.io.File;
import java.io.OutputStream;
import java.util.Vector;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.AreaTreeModel;
import org.apache.fop.area.AreaTreeParser;
import org.apache.fop.area.RenderPagesModel;
import org.apache.fop.fonts.FontInfo;
import org.xml.sax.SAXException;

public class AreaTreeInputHandler extends InputHandler {
   public AreaTreeInputHandler(File xmlfile, File xsltfile, Vector params) {
      super(xmlfile, xsltfile, params);
   }

   public AreaTreeInputHandler(File atfile) {
      super(atfile);
   }

   public void renderTo(FOUserAgent userAgent, String outputFormat, OutputStream out) throws FOPException {
      FontInfo fontInfo = new FontInfo();
      AreaTreeModel treeModel = new RenderPagesModel(userAgent, outputFormat, fontInfo, out);
      AreaTreeParser parser = new AreaTreeParser();
      Result res = new SAXResult(parser.getContentHandler(treeModel, userAgent));
      this.transformTo(res);

      try {
         treeModel.endDocument();
      } catch (SAXException var9) {
         throw new FOPException(var9);
      }
   }
}
