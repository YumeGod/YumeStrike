package org.apache.xalan.xsltc.dom;

public final class EmptyFilter implements Filter {
   public boolean test(int node) {
      return true;
   }
}
