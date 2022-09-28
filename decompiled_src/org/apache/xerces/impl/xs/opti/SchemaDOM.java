package org.apache.xerces.impl.xs.opti;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SchemaDOM extends DefaultDocument {
   static final int relationsRowResizeFactor = 15;
   static final int relationsColResizeFactor = 10;
   NodeImpl[][] relations;
   ElementImpl parent;
   int currLoc;
   int nextFreeLoc;
   boolean hidden;
   boolean inCDATA;
   StringBuffer fAnnotationBuffer = null;

   public SchemaDOM() {
      this.reset();
   }

   public void startElement(QName var1, XMLAttributes var2, int var3, int var4, int var5) {
      ElementImpl var6 = new ElementImpl(var3, var4, var5);
      this.processElement(var1, var2, var6);
      this.parent = var6;
   }

   public void emptyElement(QName var1, XMLAttributes var2, int var3, int var4, int var5) {
      ElementImpl var6 = new ElementImpl(var3, var4, var5);
      this.processElement(var1, var2, var6);
   }

   public void startElement(QName var1, XMLAttributes var2, int var3, int var4) {
      this.startElement(var1, var2, var3, var4, -1);
   }

   public void emptyElement(QName var1, XMLAttributes var2, int var3, int var4) {
      this.emptyElement(var1, var2, var3, var4, -1);
   }

   private void processElement(QName var1, XMLAttributes var2, ElementImpl var3) {
      var3.prefix = var1.prefix;
      var3.localpart = var1.localpart;
      var3.rawname = var1.rawname;
      var3.uri = var1.uri;
      var3.schemaDOM = this;
      Attr[] var4 = new Attr[var2.getLength()];

      for(int var5 = 0; var5 < var2.getLength(); ++var5) {
         var4[var5] = new AttrImpl((Element)null, var2.getPrefix(var5), var2.getLocalName(var5), var2.getQName(var5), var2.getURI(var5), var2.getValue(var5));
      }

      var3.attrs = var4;
      if (this.nextFreeLoc == this.relations.length) {
         this.resizeRelations();
      }

      if (this.relations[this.currLoc][0] != this.parent) {
         this.relations[this.nextFreeLoc][0] = this.parent;
         this.currLoc = this.nextFreeLoc++;
      }

      boolean var6 = false;
      boolean var7 = true;

      int var8;
      for(var8 = 1; var8 < this.relations[this.currLoc].length; ++var8) {
         if (this.relations[this.currLoc][var8] == null) {
            var6 = true;
            break;
         }
      }

      if (!var6) {
         this.resizeRelations(this.currLoc);
      }

      this.relations[this.currLoc][var8] = var3;
      this.parent.parentRow = this.currLoc;
      var3.row = this.currLoc;
      var3.col = var8;
   }

   public void endElement() {
      this.currLoc = this.parent.row;
      this.parent = (ElementImpl)this.relations[this.currLoc][0];
   }

   void comment(XMLString var1) {
      this.fAnnotationBuffer.append("<!--").append(var1.toString()).append("-->");
   }

   void processingInstruction(String var1, String var2) {
      this.fAnnotationBuffer.append("<?").append(var1).append(" ").append(var2).append("?>");
   }

   void characters(XMLString var1) {
      if (!this.inCDATA) {
         for(int var2 = var1.offset; var2 < var1.offset + var1.length; ++var2) {
            char var3 = var1.ch[var2];
            if (var3 == '&') {
               this.fAnnotationBuffer.append("&amp;");
            } else if (var3 == '<') {
               this.fAnnotationBuffer.append("&lt;");
            } else if (var3 == '>') {
               this.fAnnotationBuffer.append("&gt;");
            } else if (var3 == '\r') {
               this.fAnnotationBuffer.append("&#xD;");
            } else {
               this.fAnnotationBuffer.append(var3);
            }
         }
      } else {
         this.fAnnotationBuffer.append(var1.ch, var1.offset, var1.length);
      }

   }

   void endAnnotationElement(QName var1, boolean var2) {
      if (var2) {
         this.fAnnotationBuffer.append("\n</").append(var1.rawname).append(">");
         ElementImpl var3 = (ElementImpl)this.relations[this.currLoc][1];
         if (this.nextFreeLoc == this.relations.length) {
            this.resizeRelations();
         }

         int var4 = var3.parentRow = this.nextFreeLoc++;
         boolean var5 = false;

         int var6;
         for(var6 = 1; var6 < this.relations[var4].length; ++var6) {
            if (this.relations[var4][var6] == null) {
               var5 = true;
               break;
            }
         }

         if (!var5) {
            this.resizeRelations(var4);
         }

         this.relations[var4][var6] = new TextImpl(this.fAnnotationBuffer, this, var4, var6);
         this.fAnnotationBuffer = null;
      } else {
         this.fAnnotationBuffer.append("</").append(var1.rawname).append(">");
      }

   }

   void endSyntheticAnnotationElement(QName var1, boolean var2) {
      if (var2) {
         this.fAnnotationBuffer.append("\n</").append(var1.rawname).append(">");
         this.parent.fSyntheticAnnotation = this.fAnnotationBuffer.toString();
         this.fAnnotationBuffer = null;
      } else {
         this.fAnnotationBuffer.append("</").append(var1.rawname).append(">");
      }

   }

   void startAnnotationCDATA() {
      this.inCDATA = true;
      this.fAnnotationBuffer.append("<![CDATA[");
   }

   void endAnnotationCDATA() {
      this.fAnnotationBuffer.append("]]>");
      this.inCDATA = false;
   }

   private void resizeRelations() {
      NodeImpl[][] var1 = new NodeImpl[this.relations.length + 15][];
      System.arraycopy(this.relations, 0, var1, 0, this.relations.length);

      for(int var2 = this.relations.length; var2 < var1.length; ++var2) {
         var1[var2] = new NodeImpl[10];
      }

      this.relations = var1;
   }

   private void resizeRelations(int var1) {
      NodeImpl[] var2 = new NodeImpl[this.relations[var1].length + 10];
      System.arraycopy(this.relations[var1], 0, var2, 0, this.relations[var1].length);
      this.relations[var1] = var2;
   }

   public void reset() {
      if (this.relations != null) {
         for(int var1 = 0; var1 < this.relations.length; ++var1) {
            for(int var2 = 0; var2 < this.relations[var1].length; ++var2) {
               this.relations[var1][var2] = null;
            }
         }
      }

      this.relations = new NodeImpl[15][];
      this.parent = new ElementImpl(0, 0, 0);
      this.parent.rawname = "DOCUMENT_NODE";
      this.currLoc = 0;
      this.nextFreeLoc = 1;
      this.inCDATA = false;

      for(int var3 = 0; var3 < 15; ++var3) {
         this.relations[var3] = new NodeImpl[10];
      }

      this.relations[this.currLoc][0] = this.parent;
   }

   public void printDOM() {
   }

   public static void traverse(Node var0, int var1) {
      indent(var1);
      System.out.print("<" + var0.getNodeName());
      if (var0.hasAttributes()) {
         NamedNodeMap var2 = var0.getAttributes();

         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            System.out.print("  " + ((Attr)var2.item(var3)).getName() + "=\"" + ((Attr)var2.item(var3)).getValue() + "\"");
         }
      }

      if (var0.hasChildNodes()) {
         System.out.println(">");
         var1 += 4;

         for(Node var4 = var0.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
            traverse(var4, var1);
         }

         var1 -= 4;
         indent(var1);
         System.out.println("</" + var0.getNodeName() + ">");
      } else {
         System.out.println("/>");
      }

   }

   public static void indent(int var0) {
      for(int var1 = 0; var1 < var0; ++var1) {
         System.out.print(' ');
      }

   }

   public Element getDocumentElement() {
      return (ElementImpl)this.relations[0][1];
   }

   void startAnnotation(QName var1, XMLAttributes var2, NamespaceContext var3) {
      if (this.fAnnotationBuffer == null) {
         this.fAnnotationBuffer = new StringBuffer(256);
      }

      this.fAnnotationBuffer.append("<").append(var1.rawname).append(" ");
      Vector var4 = new Vector();

      String var7;
      String var8;
      for(int var5 = 0; var5 < var2.getLength(); ++var5) {
         String var6 = var2.getValue(var5);
         var7 = var2.getPrefix(var5);
         var8 = var2.getQName(var5);
         if (var7 == XMLSymbols.PREFIX_XMLNS || var8 == XMLSymbols.PREFIX_XMLNS) {
            var4.addElement(var7 == XMLSymbols.PREFIX_XMLNS ? var2.getLocalName(var5) : XMLSymbols.EMPTY_STRING);
         }

         this.fAnnotationBuffer.append(var8).append("=\"").append(processAttValue(var6)).append("\" ");
      }

      Enumeration var9 = var3.getAllPrefixes();

      while(var9.hasMoreElements()) {
         var7 = (String)var9.nextElement();
         var8 = var3.getURI(var7);
         if (var8 == null) {
            var8 = XMLSymbols.EMPTY_STRING;
         }

         if (!var4.contains(var7)) {
            if (var7 == XMLSymbols.EMPTY_STRING) {
               this.fAnnotationBuffer.append("xmlns").append("=\"").append(processAttValue(var8)).append("\" ");
            } else {
               this.fAnnotationBuffer.append("xmlns:").append(var7).append("=\"").append(processAttValue(var8)).append("\" ");
            }
         }
      }

      this.fAnnotationBuffer.append(">\n");
   }

   void startAnnotationElement(QName var1, XMLAttributes var2) {
      this.fAnnotationBuffer.append("<").append(var1.rawname);

      for(int var3 = 0; var3 < var2.getLength(); ++var3) {
         String var4 = var2.getValue(var3);
         this.fAnnotationBuffer.append(" ").append(var2.getQName(var3)).append("=\"").append(processAttValue(var4)).append("\"");
      }

      this.fAnnotationBuffer.append(">");
   }

   private static String processAttValue(String var0) {
      int var1 = var0.length();

      for(int var2 = 0; var2 < var1; ++var2) {
         char var3 = var0.charAt(var2);
         if (var3 == '"' || var3 == '<' || var3 == '&' || var3 == '\t' || var3 == '\n' || var3 == '\r') {
            return escapeAttValue(var0, var2);
         }
      }

      return var0;
   }

   private static String escapeAttValue(String var0, int var1) {
      int var3 = var0.length();
      StringBuffer var4 = new StringBuffer(var3);
      var4.append(var0.substring(0, var1));

      for(int var2 = var1; var2 < var3; ++var2) {
         char var5 = var0.charAt(var2);
         if (var5 == '"') {
            var4.append("&quot;");
         } else if (var5 == '<') {
            var4.append("&lt;");
         } else if (var5 == '&') {
            var4.append("&amp;");
         } else if (var5 == '\t') {
            var4.append("&#x9;");
         } else if (var5 == '\n') {
            var4.append("&#xA;");
         } else if (var5 == '\r') {
            var4.append("&#xD;");
         } else {
            var4.append(var5);
         }
      }

      return var4.toString();
   }
}
