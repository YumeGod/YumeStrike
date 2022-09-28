package org.apache.fop.util;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;

public class DefaultErrorListener implements ErrorListener {
   private Log log;

   public DefaultErrorListener(Log log) {
      this.log = log;
   }

   public void warning(TransformerException exc) {
      this.log.warn(exc.toString());
   }

   public void error(TransformerException exc) throws TransformerException {
      throw exc;
   }

   public void fatalError(TransformerException exc) throws TransformerException {
      throw exc;
   }
}
