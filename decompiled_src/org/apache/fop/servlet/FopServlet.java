package org.apache.fop.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;

public class FopServlet extends HttpServlet {
   protected static final String FO_REQUEST_PARAM = "fo";
   protected static final String XML_REQUEST_PARAM = "xml";
   protected static final String XSLT_REQUEST_PARAM = "xslt";
   protected TransformerFactory transFactory = null;
   protected FopFactory fopFactory = null;
   protected URIResolver uriResolver;

   public void init() throws ServletException {
      this.uriResolver = new ServletContextURIResolver(this.getServletContext());
      this.transFactory = TransformerFactory.newInstance();
      this.transFactory.setURIResolver(this.uriResolver);
      this.fopFactory = FopFactory.newInstance();
      this.fopFactory.setURIResolver(this.uriResolver);
      this.configureFopFactory();
   }

   protected void configureFopFactory() {
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
      try {
         String foParam = request.getParameter("fo");
         String xmlParam = request.getParameter("xml");
         String xsltParam = request.getParameter("xslt");
         if (foParam != null) {
            this.renderFO(foParam, response);
         } else if (xmlParam != null && xsltParam != null) {
            this.renderXML(xmlParam, xsltParam, response);
         } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>Error</title></head>\n<body><h1>FopServlet Error</h1><h3>No 'fo' request param given.</body></html>");
         }

      } catch (Exception var7) {
         throw new ServletException(var7);
      }
   }

   protected Source convertString2Source(String param) {
      Object src;
      try {
         src = this.uriResolver.resolve(param, (String)null);
      } catch (TransformerException var4) {
         src = null;
      }

      if (src == null) {
         src = new StreamSource(new File(param));
      }

      return (Source)src;
   }

   private void sendPDF(byte[] content, HttpServletResponse response) throws IOException {
      response.setContentType("application/pdf");
      response.setContentLength(content.length);
      response.getOutputStream().write(content);
      response.getOutputStream().flush();
   }

   protected void renderFO(String fo, HttpServletResponse response) throws FOPException, TransformerException, IOException {
      Source foSrc = this.convertString2Source(fo);
      Transformer transformer = this.transFactory.newTransformer();
      transformer.setURIResolver(this.uriResolver);
      this.render(foSrc, transformer, response);
   }

   protected void renderXML(String xml, String xslt, HttpServletResponse response) throws FOPException, TransformerException, IOException {
      Source xmlSrc = this.convertString2Source(xml);
      Source xsltSrc = this.convertString2Source(xslt);
      Transformer transformer = this.transFactory.newTransformer(xsltSrc);
      transformer.setURIResolver(this.uriResolver);
      this.render(xmlSrc, transformer, response);
   }

   protected void render(Source src, Transformer transformer, HttpServletResponse response) throws FOPException, TransformerException, IOException {
      FOUserAgent foUserAgent = this.getFOUserAgent();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Fop fop = this.fopFactory.newFop("application/pdf", foUserAgent, out);
      Result res = new SAXResult(fop.getDefaultHandler());
      transformer.transform(src, res);
      this.sendPDF(out.toByteArray(), response);
   }

   protected FOUserAgent getFOUserAgent() {
      FOUserAgent userAgent = this.fopFactory.newFOUserAgent();
      return userAgent;
   }
}
