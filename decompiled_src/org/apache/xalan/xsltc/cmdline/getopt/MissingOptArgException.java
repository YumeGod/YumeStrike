package org.apache.xalan.xsltc.cmdline.getopt;

class MissingOptArgException extends GetOptsException {
   static final long serialVersionUID = -1972471465394544822L;

   public MissingOptArgException(String msg) {
      super(msg);
   }
}
