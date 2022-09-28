package org.apache.batik.script;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.apache.batik.i18n.Localizable;

public interface Interpreter extends Localizable {
   Object evaluate(Reader var1, String var2) throws InterpreterException, IOException;

   Object evaluate(Reader var1) throws InterpreterException, IOException;

   Object evaluate(String var1) throws InterpreterException;

   void bindObject(String var1, Object var2);

   void setOut(Writer var1);

   void dispose();
}
