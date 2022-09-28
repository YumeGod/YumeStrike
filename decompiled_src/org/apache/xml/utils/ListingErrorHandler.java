package org.apache.xml.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xml.res.XMLMessages;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ListingErrorHandler implements ErrorHandler, ErrorListener {
   protected PrintWriter m_pw = null;
   protected boolean throwOnWarning = false;
   protected boolean throwOnError = true;
   protected boolean throwOnFatalError = true;

   public ListingErrorHandler(PrintWriter pw) {
      if (null == pw) {
         throw new NullPointerException(XMLMessages.createXMLMessage("ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER", (Object[])null));
      } else {
         this.m_pw = pw;
      }
   }

   public ListingErrorHandler() {
      this.m_pw = new PrintWriter(System.err, true);
   }

   public void warning(SAXParseException exception) throws SAXException {
      logExceptionLocation(this.m_pw, exception);
      this.m_pw.println("warning: " + exception.getMessage());
      this.m_pw.flush();
      if (this.getThrowOnWarning()) {
         throw exception;
      }
   }

   public void error(SAXParseException exception) throws SAXException {
      logExceptionLocation(this.m_pw, exception);
      this.m_pw.println("error: " + exception.getMessage());
      this.m_pw.flush();
      if (this.getThrowOnError()) {
         throw exception;
      }
   }

   public void fatalError(SAXParseException exception) throws SAXException {
      logExceptionLocation(this.m_pw, exception);
      this.m_pw.println("fatalError: " + exception.getMessage());
      this.m_pw.flush();
      if (this.getThrowOnFatalError()) {
         throw exception;
      }
   }

   public void warning(TransformerException exception) throws TransformerException {
      logExceptionLocation(this.m_pw, exception);
      this.m_pw.println("warning: " + exception.getMessage());
      this.m_pw.flush();
      if (this.getThrowOnWarning()) {
         throw exception;
      }
   }

   public void error(TransformerException exception) throws TransformerException {
      logExceptionLocation(this.m_pw, exception);
      this.m_pw.println("error: " + exception.getMessage());
      this.m_pw.flush();
      if (this.getThrowOnError()) {
         throw exception;
      }
   }

   public void fatalError(TransformerException exception) throws TransformerException {
      logExceptionLocation(this.m_pw, exception);
      this.m_pw.println("error: " + exception.getMessage());
      this.m_pw.flush();
      if (this.getThrowOnError()) {
         throw exception;
      }
   }

   public static void logExceptionLocation(PrintWriter pw, Throwable exception) {
      if (null == pw) {
         pw = new PrintWriter(System.err, true);
      }

      SourceLocator locator = null;
      Throwable cause = exception;

      do {
         if (cause instanceof SAXParseException) {
            locator = new SAXSourceLocator((SAXParseException)cause);
         } else if (cause instanceof TransformerException) {
            SourceLocator causeLocator = ((TransformerException)cause).getLocator();
            if (null != causeLocator) {
               locator = causeLocator;
            }
         }

         if (cause instanceof TransformerException) {
            cause = ((TransformerException)cause).getCause();
         } else if (cause instanceof WrappedRuntimeException) {
            cause = ((WrappedRuntimeException)cause).getException();
         } else if (cause instanceof SAXException) {
            cause = ((SAXException)cause).getException();
         } else {
            cause = null;
         }
      } while(null != cause);

      if (null != locator) {
         String id = ((SourceLocator)locator).getPublicId() != ((SourceLocator)locator).getPublicId() ? ((SourceLocator)locator).getPublicId() : (null != ((SourceLocator)locator).getSystemId() ? ((SourceLocator)locator).getSystemId() : "SystemId-Unknown");
         pw.print(id + ":Line=" + ((SourceLocator)locator).getLineNumber() + ";Column=" + ((SourceLocator)locator).getColumnNumber() + ": ");
         pw.println("exception:" + exception.getMessage());
         pw.println("root-cause:" + (null != cause ? ((Throwable)cause).getMessage() : "null"));
         logSourceLine(pw, (SourceLocator)locator);
      } else {
         pw.print("SystemId-Unknown:locator-unavailable: ");
         pw.println("exception:" + exception.getMessage());
         pw.println("root-cause:" + (null != cause ? ((Throwable)cause).getMessage() : "null"));
      }

   }

   public static void logSourceLine(PrintWriter pw, SourceLocator locator) {
      if (null != locator) {
         if (null == pw) {
            pw = new PrintWriter(System.err, true);
         }

         String url = locator.getSystemId();
         if (null == url) {
            pw.println("line: (No systemId; cannot read file)");
            pw.println();
         } else {
            try {
               int line = locator.getLineNumber();
               int column = locator.getColumnNumber();
               pw.println("line: " + getSourceLine(url, line));
               StringBuffer buf = new StringBuffer("line: ");

               for(int i = 1; i < column; ++i) {
                  buf.append(' ');
               }

               buf.append('^');
               pw.println(buf.toString());
            } catch (Exception var7) {
               pw.println("line: logSourceLine unavailable due to: " + var7.getMessage());
               pw.println();
            }

         }
      }
   }

   protected static String getSourceLine(String sourceUrl, int lineNum) throws Exception {
      URL url = null;

      try {
         url = new URL(sourceUrl);
      } catch (MalformedURLException var13) {
         int indexOfColon = sourceUrl.indexOf(58);
         int indexOfSlash = sourceUrl.indexOf(47);
         if (indexOfColon != -1 && indexOfSlash != -1 && indexOfColon < indexOfSlash) {
            throw var13;
         }

         url = new URL(SystemIDResolver.getAbsoluteURI(sourceUrl));
      }

      String line = null;
      InputStream is = null;
      BufferedReader br = null;

      try {
         URLConnection uc = url.openConnection();
         is = uc.getInputStream();
         br = new BufferedReader(new InputStreamReader(is));

         for(int i = 1; i <= lineNum; ++i) {
            line = br.readLine();
         }
      } finally {
         br.close();
         is.close();
      }

      return line;
   }

   public void setThrowOnWarning(boolean b) {
      this.throwOnWarning = b;
   }

   public boolean getThrowOnWarning() {
      return this.throwOnWarning;
   }

   public void setThrowOnError(boolean b) {
      this.throwOnError = b;
   }

   public boolean getThrowOnError() {
      return this.throwOnError;
   }

   public void setThrowOnFatalError(boolean b) {
      this.throwOnFatalError = b;
   }

   public boolean getThrowOnFatalError() {
      return this.throwOnFatalError;
   }
}
