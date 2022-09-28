package org.apache.fop.fonts.autodetect;

import java.io.IOException;
import java.util.List;

public interface FontFinder {
   List find() throws IOException;
}
