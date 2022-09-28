package org.apache.batik.parser;

import java.io.Reader;
import org.apache.batik.i18n.Localizable;

public interface Parser extends Localizable {
   void parse(Reader var1) throws ParseException;

   void parse(String var1) throws ParseException;

   void setErrorHandler(ErrorHandler var1);
}
