package org.apache.fop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;

public class FopPrintServlet extends FopServlet {
   protected void render(Source src, Transformer transformer, HttpServletResponse response) throws FOPException, TransformerException, IOException {
      FOUserAgent foUserAgent = this.getFOUserAgent();
      Fop fop = this.fopFactory.newFop("application/X-fop-print", foUserAgent);
      Result res = new SAXResult(fop.getDefaultHandler());
      transformer.transform(src, res);
      this.reportOK(response);
   }

   private void reportOK(HttpServletResponse response) throws IOException {
      String sMsg = "<html><title>Success</title>\n<body><h1>FopPrintServlet: </h1><h3>The requested data was printed to the default printer.</h3></body></html>";
      response.setContentType("text/html");
      response.setContentLength(sMsg.length());
      PrintWriter out = response.getWriter();
      out.println(sMsg);
      out.flush();
   }
}
