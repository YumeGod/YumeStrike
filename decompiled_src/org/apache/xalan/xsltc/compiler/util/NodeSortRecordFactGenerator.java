package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.Stylesheet;

public final class NodeSortRecordFactGenerator extends ClassGenerator {
   public NodeSortRecordFactGenerator(String className, String superClassName, String fileName, int accessFlags, String[] interfaces, Stylesheet stylesheet) {
      super(className, superClassName, fileName, accessFlags, interfaces, stylesheet);
   }

   public boolean isExternal() {
      return true;
   }
}
