package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface LookupSubtableFactory {
   LookupSubtable read(int var1, RandomAccessFile var2, int var3) throws IOException;
}
