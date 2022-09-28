package org.apache.xalan.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import org.apache.xalan.extensions.XSLProcessorContext;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class Redirect {
   protected Hashtable m_formatterListeners = new Hashtable();
   protected Hashtable m_outputStreams = new Hashtable();
   public static final boolean DEFAULT_APPEND_OPEN = false;
   public static final boolean DEFAULT_APPEND_WRITE = false;

   public void open(XSLProcessorContext context, ElemExtensionCall elem) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
      String fileName = this.getFilename(context, elem);
      Object flistener = this.m_formatterListeners.get(fileName);
      if (null == flistener) {
         String mkdirsExpr = elem.getAttribute("mkdirs", context.getContextNode(), context.getTransformer());
         boolean mkdirs = mkdirsExpr != null ? mkdirsExpr.equals("true") || mkdirsExpr.equals("yes") : true;
         String appendExpr = elem.getAttribute("append", context.getContextNode(), context.getTransformer());
         boolean append = appendExpr != null ? appendExpr.equals("true") || appendExpr.equals("yes") : false;
         this.makeFormatterListener(context, elem, fileName, true, mkdirs, append);
      }

   }

   public void write(XSLProcessorContext context, ElemExtensionCall elem) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
      String fileName = this.getFilename(context, elem);
      Object flObject = this.m_formatterListeners.get(fileName);
      boolean inTable = false;
      ContentHandler formatter;
      if (null == flObject) {
         String mkdirsExpr = elem.getAttribute("mkdirs", context.getContextNode(), context.getTransformer());
         boolean mkdirs = mkdirsExpr != null ? mkdirsExpr.equals("true") || mkdirsExpr.equals("yes") : true;
         String appendExpr = elem.getAttribute("append", context.getContextNode(), context.getTransformer());
         boolean append = appendExpr != null ? appendExpr.equals("true") || appendExpr.equals("yes") : false;
         formatter = this.makeFormatterListener(context, elem, fileName, true, mkdirs, append);
      } else {
         inTable = true;
         formatter = (ContentHandler)flObject;
      }

      TransformerImpl transf = context.getTransformer();
      this.startRedirection(transf, formatter);
      transf.executeChildTemplates(elem, context.getContextNode(), context.getMode(), formatter);
      this.endRedirection(transf);
      if (!inTable) {
         OutputStream ostream = (OutputStream)this.m_outputStreams.get(fileName);
         if (null != ostream) {
            try {
               formatter.endDocument();
            } catch (SAXException var11) {
               throw new TransformerException(var11);
            }

            ostream.close();
            this.m_outputStreams.remove(fileName);
            this.m_formatterListeners.remove(fileName);
         }
      }

   }

   public void close(XSLProcessorContext context, ElemExtensionCall elem) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
      String fileName = this.getFilename(context, elem);
      Object formatterObj = this.m_formatterListeners.get(fileName);
      if (null != formatterObj) {
         ContentHandler fl = (ContentHandler)formatterObj;

         try {
            fl.endDocument();
         } catch (SAXException var7) {
            throw new TransformerException(var7);
         }

         OutputStream ostream = (OutputStream)this.m_outputStreams.get(fileName);
         if (null != ostream) {
            ostream.close();
            this.m_outputStreams.remove(fileName);
         }

         this.m_formatterListeners.remove(fileName);
      }

   }

   private String getFilename(XSLProcessorContext context, ElemExtensionCall elem) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
      String fileNameExpr = elem.getAttribute("select", context.getContextNode(), context.getTransformer());
      String fileName;
      if (null != fileNameExpr) {
         XPathContext xctxt = context.getTransformer().getXPathContext();
         XPath myxpath = new XPath(fileNameExpr, elem, xctxt.getNamespaceContext(), 0);
         XObject xobj = myxpath.execute(xctxt, context.getContextNode(), elem);
         fileName = xobj.str();
         if (null == fileName || fileName.length() == 0) {
            fileName = elem.getAttribute("file", context.getContextNode(), context.getTransformer());
         }
      } else {
         fileName = elem.getAttribute("file", context.getContextNode(), context.getTransformer());
      }

      if (null == fileName) {
         context.getTransformer().getMsgMgr().error(elem, (Node)elem, (Node)context.getContextNode(), (String)"ER_REDIRECT_COULDNT_GET_FILENAME");
      }

      return fileName;
   }

   private String urlToFileName(String base) {
      if (null != base) {
         if (base.startsWith("file:////")) {
            base = base.substring(7);
         } else if (base.startsWith("file:///")) {
            base = base.substring(6);
         } else if (base.startsWith("file://")) {
            base = base.substring(5);
         } else if (base.startsWith("file:/")) {
            base = base.substring(5);
         } else if (base.startsWith("file:")) {
            base = base.substring(4);
         }
      }

      return base;
   }

   private ContentHandler makeFormatterListener(XSLProcessorContext context, ElemExtensionCall elem, String fileName, boolean shouldPutInTable, boolean mkdirs, boolean append) throws MalformedURLException, FileNotFoundException, IOException, TransformerException {
      File file = new File(fileName);
      TransformerImpl transformer = context.getTransformer();
      File baseFile;
      if (!file.isAbsolute()) {
         Result outputTarget = transformer.getOutputTarget();
         String base;
         if (null != outputTarget && (base = outputTarget.getSystemId()) != null) {
            base = this.urlToFileName(base);
         } else {
            base = this.urlToFileName(transformer.getBaseURLOfSource());
         }

         if (null != base) {
            baseFile = new File(base);
            file = new File(baseFile.getParent(), fileName);
         }
      }

      if (mkdirs) {
         String dirStr = file.getParent();
         if (null != dirStr && dirStr.length() > 0) {
            baseFile = new File(dirStr);
            baseFile.mkdirs();
         }
      }

      OutputProperties format = transformer.getOutputFormat();
      FileOutputStream ostream = new FileOutputStream(file.getPath(), append);

      try {
         SerializationHandler flistener = this.createSerializationHandler(transformer, ostream, file, format);

         try {
            flistener.startDocument();
         } catch (SAXException var14) {
            throw new TransformerException(var14);
         }

         if (shouldPutInTable) {
            this.m_outputStreams.put(fileName, ostream);
            this.m_formatterListeners.put(fileName, flistener);
         }

         return flistener;
      } catch (TransformerException var15) {
         throw new TransformerException(var15);
      }
   }

   public void startRedirection(TransformerImpl transf, ContentHandler formatter) {
   }

   public void endRedirection(TransformerImpl transf) {
   }

   public SerializationHandler createSerializationHandler(TransformerImpl transformer, FileOutputStream ostream, File file, OutputProperties format) throws IOException, TransformerException {
      SerializationHandler serializer = transformer.createSerializationHandler(new StreamResult(ostream), format);
      return serializer;
   }
}
