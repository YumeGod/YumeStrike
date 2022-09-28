package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xml.utils.SystemIDResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

final class Import extends TopLevelElement {
   private Stylesheet _imported = null;

   public Stylesheet getImportedStylesheet() {
      return this._imported;
   }

   public void parseContents(Parser parser) {
      XSLTC xsltc = parser.getXSLTC();
      Stylesheet context = parser.getCurrentStylesheet();

      try {
         String docToLoad = this.getAttribute("href");
         if (context.checkForLoop(docToLoad)) {
            ErrorMsg msg = new ErrorMsg("CIRCULAR_INCLUDE_ERR", docToLoad, this);
            parser.reportError(2, msg);
            return;
         }

         InputSource input = null;
         XMLReader reader = null;
         String currLoadedDoc = context.getSystemId();
         SourceLoader loader = context.getSourceLoader();
         if (loader != null) {
            input = loader.loadSource(docToLoad, currLoadedDoc, xsltc);
            if (input != null) {
               docToLoad = input.getSystemId();
               reader = xsltc.getXMLReader();
            }
         }

         if (input == null) {
            docToLoad = SystemIDResolver.getAbsoluteURI(docToLoad, currLoadedDoc);
            input = new InputSource(docToLoad);
         }

         if (input == null) {
            ErrorMsg msg = new ErrorMsg("FILE_NOT_FOUND_ERR", docToLoad, this);
            parser.reportError(2, msg);
            return;
         }

         SyntaxTreeNode root;
         if (reader != null) {
            root = parser.parse(reader, input);
         } else {
            root = parser.parse(input);
         }

         if (root == null) {
            return;
         }

         this._imported = parser.makeStylesheet(root);
         if (this._imported != null) {
            this._imported.setSourceLoader(loader);
            this._imported.setSystemId(docToLoad);
            this._imported.setParentStylesheet(context);
            this._imported.setImportingStylesheet(context);
            this._imported.setTemplateInlining(context.getTemplateInlining());
            int currPrecedence = parser.getCurrentImportPrecedence();
            int nextPrecedence = parser.getNextImportPrecedence();
            this._imported.setImportPrecedence(currPrecedence);
            context.setImportPrecedence(nextPrecedence);
            parser.setCurrentStylesheet(this._imported);
            this._imported.parseContents(parser);
            Enumeration elements = this._imported.elements();
            Stylesheet topStylesheet = parser.getTopLevelStylesheet();

            while(elements.hasMoreElements()) {
               Object element = elements.nextElement();
               if (element instanceof TopLevelElement) {
                  if (element instanceof Variable) {
                     topStylesheet.addVariable((Variable)element);
                  } else if (element instanceof Param) {
                     topStylesheet.addParam((Param)element);
                  } else {
                     topStylesheet.addElement((TopLevelElement)element);
                  }
               }
            }

            return;
         }
      } catch (Exception var19) {
         var19.printStackTrace();
         return;
      } finally {
         parser.setCurrentStylesheet(context);
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
   }
}
