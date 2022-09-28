package org.apache.fop.cli;

import java.io.File;
import java.io.OutputStream;
import java.util.Vector;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFException;
import org.apache.fop.render.intermediate.IFParser;
import org.apache.fop.render.intermediate.IFUtil;

public class IFInputHandler extends InputHandler {
   public IFInputHandler(File xmlfile, File xsltfile, Vector params) {
      super(xmlfile, xsltfile, params);
   }

   public IFInputHandler(File iffile) {
      super(iffile);
   }

   public void renderTo(FOUserAgent userAgent, String outputFormat, OutputStream out) throws FOPException {
      IFDocumentHandler documentHandler = userAgent.getFactory().getRendererFactory().createDocumentHandler(userAgent, outputFormat);

      try {
         documentHandler.setResult(new StreamResult(out));
         IFUtil.setupFonts(documentHandler);
         IFParser parser = new IFParser();
         Result res = new SAXResult(parser.getContentHandler(documentHandler, userAgent));
         this.transformTo(res);
      } catch (IFException var7) {
         throw new FOPException(var7);
      }
   }
}
