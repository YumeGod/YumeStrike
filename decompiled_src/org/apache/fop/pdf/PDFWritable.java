package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public interface PDFWritable {
   void outputInline(OutputStream var1, Writer var2) throws IOException;
}
