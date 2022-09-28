package org.apache.fop.pdf;

public abstract class AbstractPDFFontStream extends AbstractPDFStream {
   protected String getDefaultFilterName() {
      return "font";
   }
}
