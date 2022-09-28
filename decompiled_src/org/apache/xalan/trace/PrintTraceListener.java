package org.apache.xalan.trace;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.w3c.dom.Node;

public class PrintTraceListener implements TraceListenerEx3 {
   PrintWriter m_pw;
   public boolean m_traceTemplates = false;
   public boolean m_traceElements = false;
   public boolean m_traceGeneration = false;
   public boolean m_traceSelection = false;
   public boolean m_traceExtension = false;
   int m_indent = 0;

   public PrintTraceListener(PrintWriter pw) {
      this.m_pw = pw;
   }

   public void _trace(TracerEvent ev) {
      switch (ev.m_styleNode.getXSLToken()) {
         case 19:
            if (this.m_traceTemplates || this.m_traceElements) {
               ElemTemplate et = (ElemTemplate)ev.m_styleNode;
               this.m_pw.print(et.getSystemId() + " Line #" + et.getLineNumber() + ", " + "Column #" + et.getColumnNumber() + ": " + et.getNodeName() + " ");
               if (null != et.getMatch()) {
                  this.m_pw.print("match='" + et.getMatch().getPatternString() + "' ");
               }

               if (null != et.getName()) {
                  this.m_pw.print("name='" + et.getName() + "' ");
               }

               this.m_pw.println();
            }
            break;
         case 78:
            if (this.m_traceElements) {
               this.m_pw.print(ev.m_styleNode.getSystemId() + " Line #" + ev.m_styleNode.getLineNumber() + ", " + "Column #" + ev.m_styleNode.getColumnNumber() + " -- " + ev.m_styleNode.getNodeName() + ": ");
               ElemTextLiteral etl = (ElemTextLiteral)ev.m_styleNode;
               String chars = new String(etl.getChars(), 0, etl.getChars().length);
               this.m_pw.println("    " + chars.trim());
            }
            break;
         default:
            if (this.m_traceElements) {
               this.m_pw.println(ev.m_styleNode.getSystemId() + " Line #" + ev.m_styleNode.getLineNumber() + ", " + "Column #" + ev.m_styleNode.getColumnNumber() + ": " + ev.m_styleNode.getNodeName());
            }
      }

   }

   public void trace(TracerEvent ev) {
      this._trace(ev);
   }

   public void traceEnd(TracerEvent ev) {
   }

   public void selected(SelectionEvent ev) throws TransformerException {
      if (this.m_traceSelection) {
         ElemTemplateElement ete = ev.m_styleNode;
         Node sourceNode = ev.m_sourceNode;
         SourceLocator locator = null;
         if (sourceNode instanceof DTMNodeProxy) {
            int nodeHandler = ((DTMNodeProxy)sourceNode).getDTMNodeNumber();
            locator = ((DTMNodeProxy)sourceNode).getDTM().getSourceLocatorFor(nodeHandler);
         }

         if (locator != null) {
            this.m_pw.println("Selected source node '" + sourceNode.getNodeName() + "', at " + locator);
         } else {
            this.m_pw.println("Selected source node '" + sourceNode.getNodeName() + "'");
         }

         if (ev.m_styleNode.getLineNumber() == 0) {
            ElemTemplateElement parent = ete.getParentElem();
            if (parent == ete.getStylesheetRoot().getDefaultRootRule()) {
               this.m_pw.print("(default root rule) ");
            } else if (parent == ete.getStylesheetRoot().getDefaultTextRule()) {
               this.m_pw.print("(default text rule) ");
            } else if (parent == ete.getStylesheetRoot().getDefaultRule()) {
               this.m_pw.print("(default rule) ");
            }

            this.m_pw.print(ete.getNodeName() + ", " + ev.m_attributeName + "='" + ev.m_xpath.getPatternString() + "': ");
         } else {
            this.m_pw.print(ev.m_styleNode.getSystemId() + " Line #" + ev.m_styleNode.getLineNumber() + ", " + "Column #" + ev.m_styleNode.getColumnNumber() + ": " + ete.getNodeName() + ", " + ev.m_attributeName + "='" + ev.m_xpath.getPatternString() + "': ");
         }

         if (ev.m_selection.getType() == 4) {
            this.m_pw.println();
            DTMIterator nl = ev.m_selection.iter();
            int currentPos = true;
            int currentPos = nl.getCurrentPos();
            nl.setShouldCacheNodes(true);
            DTMIterator clone = null;

            try {
               clone = nl.cloneWithReset();
            } catch (CloneNotSupportedException var10) {
               this.m_pw.println("     [Can't trace nodelist because it it threw a CloneNotSupportedException]");
               return;
            }

            int pos = clone.nextNode();
            if (-1 == pos) {
               this.m_pw.println("     [empty node list]");
            } else {
               while(-1 != pos) {
                  DTM dtm = ev.m_processor.getXPathContext().getDTM(pos);
                  this.m_pw.print("     ");
                  this.m_pw.print(Integer.toHexString(pos));
                  this.m_pw.print(": ");
                  this.m_pw.println(dtm.getNodeName(pos));
                  pos = clone.nextNode();
               }
            }

            nl.runTo(-1);
            nl.setCurrentPos(currentPos);
         } else {
            this.m_pw.println(ev.m_selection.str());
         }
      }

   }

   public void selectEnd(EndSelectionEvent ev) throws TransformerException {
   }

   public void generated(GenerateEvent ev) {
      if (this.m_traceGeneration) {
         String chars;
         switch (ev.m_eventtype) {
            case 1:
               this.m_pw.println("STARTDOCUMENT");
               break;
            case 2:
               this.m_pw.println("ENDDOCUMENT");
               break;
            case 3:
               this.m_pw.println("STARTELEMENT: " + ev.m_name);
               break;
            case 4:
               this.m_pw.println("ENDELEMENT: " + ev.m_name);
               break;
            case 5:
               chars = new String(ev.m_characters, ev.m_start, ev.m_length);
               this.m_pw.println("CHARACTERS: " + chars);
               break;
            case 6:
               this.m_pw.println("IGNORABLEWHITESPACE");
               break;
            case 7:
               this.m_pw.println("PI: " + ev.m_name + ", " + ev.m_data);
               break;
            case 8:
               this.m_pw.println("COMMENT: " + ev.m_data);
               break;
            case 9:
               this.m_pw.println("ENTITYREF: " + ev.m_name);
               break;
            case 10:
               chars = new String(ev.m_characters, ev.m_start, ev.m_length);
               this.m_pw.println("CDATA: " + chars);
         }
      }

   }

   public void extension(ExtensionEvent ev) {
      if (this.m_traceExtension) {
         switch (ev.m_callType) {
            case 0:
               this.m_pw.println("EXTENSION: " + ((Class)ev.m_method).getName() + "#<init>");
               break;
            case 1:
               this.m_pw.println("EXTENSION: " + ((Method)ev.m_method).getDeclaringClass().getName() + "#" + ((Method)ev.m_method).getName());
               break;
            case 2:
               this.m_pw.println("EXTENSION: " + ((Constructor)ev.m_method).getDeclaringClass().getName() + "#<init>");
         }
      }

   }

   public void extensionEnd(ExtensionEvent ev) {
   }
}
