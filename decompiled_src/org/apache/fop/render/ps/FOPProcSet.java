package org.apache.fop.render.ps;

import java.io.IOException;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSProcSet;

public final class FOPProcSet extends PSProcSet {
   public static final FOPProcSet INSTANCE = new FOPProcSet();

   private FOPProcSet() {
      super("Apache FOP Std ProcSet", 1.0F, 0);
   }

   public void writeTo(PSGenerator gen) throws IOException {
      gen.writeDSCComment("BeginResource", new Object[]{"procset", this.getName(), Float.toString(this.getVersion()), Integer.toString(this.getRevision())});
      gen.writeDSCComment("Version", new Object[]{Float.toString(this.getVersion()), Integer.toString(this.getRevision())});
      gen.writeDSCComment("Copyright", (Object)"Copyright 2009 The Apache Software Foundation. License terms: http://www.apache.org/licenses/LICENSE-2.0");
      gen.writeDSCComment("Title", (Object)"Basic set of procedures used by Apache FOP");
      gen.writeln("/TJ { % Similar but not equal to PDF's TJ operator");
      gen.writeln("  {");
      gen.writeln("    dup type /stringtype eq");
      gen.writeln("    { show }");
      gen.writeln("    { neg 1000 div 0 rmoveto }");
      gen.writeln("    ifelse");
      gen.writeln("  } forall");
      gen.writeln("} bd");
      gen.writeln("/ATJ { % As TJ but adds letter-spacing");
      gen.writeln("  /ATJls exch def");
      gen.writeln("  {");
      gen.writeln("    dup type /stringtype eq");
      gen.writeln("    { ATJls 0 3 2 roll ashow }");
      gen.writeln("    { neg 1000 div 0 rmoveto }");
      gen.writeln("    ifelse");
      gen.writeln("  } forall");
      gen.writeln("} bd");
      gen.writeDSCComment("EndResource");
      gen.getResourceTracker().registerSuppliedResource(this);
   }
}
