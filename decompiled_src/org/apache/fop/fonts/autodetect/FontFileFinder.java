package org.apache.fop.fonts.autodetect;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FontFileFinder extends DirectoryWalker implements FontFinder {
   private final Log log;
   public static final int DEFAULT_DEPTH_LIMIT = -1;

   public FontFileFinder() {
      super(getDirectoryFilter(), getFileFilter(), -1);
      this.log = LogFactory.getLog(FontFileFinder.class);
   }

   public FontFileFinder(int depthLimit) {
      super(getDirectoryFilter(), getFileFilter(), depthLimit);
      this.log = LogFactory.getLog(FontFileFinder.class);
   }

   protected static IOFileFilter getDirectoryFilter() {
      return FileFilterUtils.andFileFilter(FileFilterUtils.directoryFileFilter(), FileFilterUtils.notFileFilter(FileFilterUtils.prefixFileFilter(".")));
   }

   protected static IOFileFilter getFileFilter() {
      return FileFilterUtils.andFileFilter(FileFilterUtils.fileFileFilter(), new WildcardFileFilter(new String[]{"*.ttf", "*.otf", "*.pfb", "*.ttc"}, IOCase.INSENSITIVE));
   }

   protected boolean handleDirectory(File directory, int depth, Collection results) {
      return true;
   }

   protected void handleFile(File file, int depth, Collection results) {
      try {
         results.add(file.toURI().toURL());
      } catch (MalformedURLException var5) {
         this.log.debug("MalformedURLException" + var5.getMessage());
      }

   }

   protected void handleDirectoryEnd(File directory, int depth, Collection results) {
      if (this.log.isDebugEnabled()) {
         this.log.debug(directory + ": found " + results.size() + " font" + (results.size() == 1 ? "" : "s"));
      }

   }

   public List find() throws IOException {
      String osName = System.getProperty("os.name");
      Object fontDirFinder;
      if (osName.startsWith("Windows")) {
         fontDirFinder = new WindowsFontDirFinder();
      } else if (osName.startsWith("Mac")) {
         fontDirFinder = new MacFontDirFinder();
      } else {
         fontDirFinder = new UnixFontDirFinder();
      }

      List fontDirs = ((FontFinder)fontDirFinder).find();
      List results = new ArrayList();
      Iterator iter = fontDirs.iterator();

      while(iter.hasNext()) {
         File dir = (File)iter.next();
         super.walk(dir, results);
      }

      return results;
   }

   public List find(String dir) throws IOException {
      List results = new ArrayList();
      super.walk(new File(dir), results);
      return results;
   }
}
