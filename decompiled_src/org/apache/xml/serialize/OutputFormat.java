package org.apache.xml.serialize;

import java.io.UnsupportedEncodingException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

public class OutputFormat {
   private String _method;
   private String _version;
   private int _indent;
   private String _encoding;
   private EncodingInfo _encodingInfo;
   private boolean _allowJavaNames;
   private String _mediaType;
   private String _doctypeSystem;
   private String _doctypePublic;
   private boolean _omitXmlDeclaration;
   private boolean _omitDoctype;
   private boolean _omitComments;
   private boolean _stripComments;
   private boolean _standalone;
   private String[] _cdataElements;
   private String[] _nonEscapingElements;
   private String _lineSeparator;
   private int _lineWidth;
   private boolean _preserve;
   private boolean _preserveEmptyAttributes;

   public OutputFormat() {
      this._indent = 0;
      this._encoding = "UTF-8";
      this._encodingInfo = null;
      this._allowJavaNames = false;
      this._omitXmlDeclaration = false;
      this._omitDoctype = false;
      this._omitComments = false;
      this._stripComments = false;
      this._standalone = false;
      this._lineSeparator = "\n";
      this._lineWidth = 72;
      this._preserve = false;
      this._preserveEmptyAttributes = false;
   }

   public OutputFormat(String var1, String var2, boolean var3) {
      this._indent = 0;
      this._encoding = "UTF-8";
      this._encodingInfo = null;
      this._allowJavaNames = false;
      this._omitXmlDeclaration = false;
      this._omitDoctype = false;
      this._omitComments = false;
      this._stripComments = false;
      this._standalone = false;
      this._lineSeparator = "\n";
      this._lineWidth = 72;
      this._preserve = false;
      this._preserveEmptyAttributes = false;
      this.setMethod(var1);
      this.setEncoding(var2);
      this.setIndenting(var3);
   }

   public OutputFormat(Document var1) {
      this._indent = 0;
      this._encoding = "UTF-8";
      this._encodingInfo = null;
      this._allowJavaNames = false;
      this._omitXmlDeclaration = false;
      this._omitDoctype = false;
      this._omitComments = false;
      this._stripComments = false;
      this._standalone = false;
      this._lineSeparator = "\n";
      this._lineWidth = 72;
      this._preserve = false;
      this._preserveEmptyAttributes = false;
      this.setMethod(whichMethod(var1));
      this.setDoctype(whichDoctypePublic(var1), whichDoctypeSystem(var1));
      this.setMediaType(whichMediaType(this.getMethod()));
   }

   public OutputFormat(Document var1, String var2, boolean var3) {
      this(var1);
      this.setEncoding(var2);
      this.setIndenting(var3);
   }

   public String getMethod() {
      return this._method;
   }

   public void setMethod(String var1) {
      this._method = var1;
   }

   public String getVersion() {
      return this._version;
   }

   public void setVersion(String var1) {
      this._version = var1;
   }

   public int getIndent() {
      return this._indent;
   }

   public boolean getIndenting() {
      return this._indent > 0;
   }

   public void setIndent(int var1) {
      if (var1 < 0) {
         this._indent = 0;
      } else {
         this._indent = var1;
      }

   }

   public void setIndenting(boolean var1) {
      if (var1) {
         this._indent = 4;
         this._lineWidth = 72;
      } else {
         this._indent = 0;
         this._lineWidth = 0;
      }

   }

   public String getEncoding() {
      return this._encoding;
   }

   public void setEncoding(String var1) {
      this._encoding = var1;
      this._encodingInfo = null;
   }

   public void setEncoding(EncodingInfo var1) {
      this._encoding = var1.getIANAName();
      this._encodingInfo = var1;
   }

   public EncodingInfo getEncodingInfo() throws UnsupportedEncodingException {
      if (this._encodingInfo == null) {
         this._encodingInfo = Encodings.getEncodingInfo(this._encoding, this._allowJavaNames);
      }

      return this._encodingInfo;
   }

   public void setAllowJavaNames(boolean var1) {
      this._allowJavaNames = var1;
   }

   public boolean setAllowJavaNames() {
      return this._allowJavaNames;
   }

   public String getMediaType() {
      return this._mediaType;
   }

   public void setMediaType(String var1) {
      this._mediaType = var1;
   }

   public void setDoctype(String var1, String var2) {
      this._doctypePublic = var1;
      this._doctypeSystem = var2;
   }

   public String getDoctypePublic() {
      return this._doctypePublic;
   }

   public String getDoctypeSystem() {
      return this._doctypeSystem;
   }

   public boolean getOmitComments() {
      return this._omitComments;
   }

   public void setOmitComments(boolean var1) {
      this._omitComments = var1;
   }

   public boolean getOmitDocumentType() {
      return this._omitDoctype;
   }

   public void setOmitDocumentType(boolean var1) {
      this._omitDoctype = var1;
   }

   public boolean getOmitXMLDeclaration() {
      return this._omitXmlDeclaration;
   }

   public void setOmitXMLDeclaration(boolean var1) {
      this._omitXmlDeclaration = var1;
   }

   public boolean getStandalone() {
      return this._standalone;
   }

   public void setStandalone(boolean var1) {
      this._standalone = var1;
   }

   public String[] getCDataElements() {
      return this._cdataElements;
   }

   public boolean isCDataElement(String var1) {
      if (this._cdataElements == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < this._cdataElements.length; ++var2) {
            if (this._cdataElements[var2].equals(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public void setCDataElements(String[] var1) {
      this._cdataElements = var1;
   }

   public String[] getNonEscapingElements() {
      return this._nonEscapingElements;
   }

   public boolean isNonEscapingElement(String var1) {
      if (this._nonEscapingElements == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < this._nonEscapingElements.length; ++var2) {
            if (this._nonEscapingElements[var2].equals(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public void setNonEscapingElements(String[] var1) {
      this._nonEscapingElements = var1;
   }

   public String getLineSeparator() {
      return this._lineSeparator;
   }

   public void setLineSeparator(String var1) {
      if (var1 == null) {
         this._lineSeparator = "\n";
      } else {
         this._lineSeparator = var1;
      }

   }

   public boolean getPreserveSpace() {
      return this._preserve;
   }

   public void setPreserveSpace(boolean var1) {
      this._preserve = var1;
   }

   public int getLineWidth() {
      return this._lineWidth;
   }

   public void setLineWidth(int var1) {
      if (var1 <= 0) {
         this._lineWidth = 0;
      } else {
         this._lineWidth = var1;
      }

   }

   public boolean getPreserveEmptyAttributes() {
      return this._preserveEmptyAttributes;
   }

   public void setPreserveEmptyAttributes(boolean var1) {
      this._preserveEmptyAttributes = var1;
   }

   public char getLastPrintable() {
      return (char)(this.getEncoding() != null && this.getEncoding().equalsIgnoreCase("ASCII") ? 'ÿ' : '\uffff');
   }

   public static String whichMethod(Document var0) {
      if (var0 instanceof HTMLDocument) {
         return "html";
      } else {
         for(Node var1 = var0.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
            if (var1.getNodeType() == 1) {
               if (var1.getNodeName().equalsIgnoreCase("html")) {
                  return "html";
               }

               if (var1.getNodeName().equalsIgnoreCase("root")) {
                  return "fop";
               }

               return "xml";
            }

            if (var1.getNodeType() == 3) {
               String var2 = var1.getNodeValue();

               for(int var3 = 0; var3 < var2.length(); ++var3) {
                  if (var2.charAt(var3) != ' ' && var2.charAt(var3) != '\n' && var2.charAt(var3) != '\t' && var2.charAt(var3) != '\r') {
                     return "xml";
                  }
               }
            }
         }

         return "xml";
      }
   }

   public static String whichDoctypePublic(Document var0) {
      DocumentType var1 = var0.getDoctype();
      if (var1 != null) {
         try {
            return var1.getPublicId();
         } catch (Error var3) {
         }
      }

      return var0 instanceof HTMLDocument ? "-//W3C//DTD XHTML 1.0 Strict//EN" : null;
   }

   public static String whichDoctypeSystem(Document var0) {
      DocumentType var1 = var0.getDoctype();
      if (var1 != null) {
         try {
            return var1.getSystemId();
         } catch (Error var3) {
         }
      }

      return var0 instanceof HTMLDocument ? "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" : null;
   }

   public static String whichMediaType(String var0) {
      if (var0.equalsIgnoreCase("xml")) {
         return "text/xml";
      } else if (var0.equalsIgnoreCase("html")) {
         return "text/html";
      } else if (var0.equalsIgnoreCase("xhtml")) {
         return "text/html";
      } else if (var0.equalsIgnoreCase("text")) {
         return "text/plain";
      } else {
         return var0.equalsIgnoreCase("fop") ? "application/pdf" : null;
      }
   }

   public static class Defaults {
      public static final int Indent = 4;
      public static final String Encoding = "UTF-8";
      public static final int LineWidth = 72;
   }

   public static class DTD {
      public static final String HTMLPublicId = "-//W3C//DTD HTML 4.01//EN";
      public static final String HTMLSystemId = "http://www.w3.org/TR/html4/strict.dtd";
      public static final String XHTMLPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
      public static final String XHTMLSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
   }
}
