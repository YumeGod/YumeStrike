package org.apache.xalan.templates;

import java.util.Vector;
import javax.xml.transform.TransformerException;

public class StylesheetComposed extends Stylesheet {
   static final long serialVersionUID = -3444072247410233923L;
   private int m_importNumber = -1;
   private int m_importCountComposed;
   private int m_endImportCountComposed;
   private transient Vector m_includesComposed;

   public StylesheetComposed(Stylesheet parent) {
      super(parent);
   }

   public boolean isAggregatedType() {
      return true;
   }

   public void recompose(Vector recomposableElements) throws TransformerException {
      int n = this.getIncludeCountComposed();

      for(int i = -1; i < n; ++i) {
         Stylesheet included = this.getIncludeComposed(i);
         int s = included.getOutputCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getOutput(j));
         }

         s = included.getAttributeSetCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getAttributeSet(j));
         }

         s = included.getDecimalFormatCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getDecimalFormat(j));
         }

         s = included.getKeyCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getKey(j));
         }

         s = included.getNamespaceAliasCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getNamespaceAlias(j));
         }

         s = included.getTemplateCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getTemplate(j));
         }

         s = included.getVariableOrParamCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getVariableOrParam(j));
         }

         s = included.getStripSpaceCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getStripSpace(j));
         }

         s = included.getPreserveSpaceCount();

         for(int j = 0; j < s; ++j) {
            recomposableElements.addElement(included.getPreserveSpace(j));
         }
      }

   }

   void recomposeImports() {
      this.m_importNumber = this.getStylesheetRoot().getImportNumber(this);
      StylesheetRoot root = this.getStylesheetRoot();
      int globalImportCount = root.getGlobalImportCount();
      this.m_importCountComposed = globalImportCount - this.m_importNumber - 1;
      int count = this.getImportCount();
      if (count > 0) {
         for(this.m_endImportCountComposed += count; count > 0; this.m_endImportCountComposed += this.getImport(count).getEndImportCountComposed()) {
            --count;
         }
      }

      count = this.getIncludeCountComposed();

      while(count > 0) {
         --count;
         int imports = this.getIncludeComposed(count).getImportCount();

         int var10001;
         Stylesheet var10002;
         for(this.m_endImportCountComposed += imports; imports > 0; this.m_endImportCountComposed = var10001 + var10002.getImport(imports).getEndImportCountComposed()) {
            var10001 = this.m_endImportCountComposed;
            var10002 = this.getIncludeComposed(count);
            --imports;
         }
      }

   }

   public StylesheetComposed getImportComposed(int i) throws ArrayIndexOutOfBoundsException {
      StylesheetRoot root = this.getStylesheetRoot();
      return root.getGlobalImport(1 + this.m_importNumber + i);
   }

   public int getImportCountComposed() {
      return this.m_importCountComposed;
   }

   public int getEndImportCountComposed() {
      return this.m_endImportCountComposed;
   }

   void recomposeIncludes(Stylesheet including) {
      int n = including.getIncludeCount();
      if (n > 0) {
         if (null == this.m_includesComposed) {
            this.m_includesComposed = new Vector();
         }

         for(int i = 0; i < n; ++i) {
            Stylesheet included = including.getInclude(i);
            this.m_includesComposed.addElement(included);
            this.recomposeIncludes(included);
         }
      }

   }

   public Stylesheet getIncludeComposed(int i) throws ArrayIndexOutOfBoundsException {
      if (-1 == i) {
         return this;
      } else if (null == this.m_includesComposed) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return (Stylesheet)this.m_includesComposed.elementAt(i);
      }
   }

   public int getIncludeCountComposed() {
      return null != this.m_includesComposed ? this.m_includesComposed.size() : 0;
   }

   public void recomposeTemplates(boolean flushFirst) throws TransformerException {
   }
}
