package org.apache.batik.script;

import java.net.URL;

public interface InterpreterFactory {
   String[] getMimeTypes();

   Interpreter createInterpreter(URL var1, boolean var2);
}
