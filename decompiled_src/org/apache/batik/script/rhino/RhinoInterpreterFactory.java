package org.apache.batik.script.rhino;

import java.net.URL;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterFactory;
import org.apache.batik.script.rhino.svg12.SVG12RhinoInterpreter;

public class RhinoInterpreterFactory implements InterpreterFactory {
   private static final String[] RHINO_MIMETYPES = new String[]{"application/ecmascript", "application/javascript", "text/ecmascript", "text/javascript"};

   public String[] getMimeTypes() {
      return RHINO_MIMETYPES;
   }

   public Interpreter createInterpreter(URL var1, boolean var2) {
      return (Interpreter)(var2 ? new SVG12RhinoInterpreter(var1) : new RhinoInterpreter(var1));
   }
}
