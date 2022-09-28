package org.apache.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.output.NullOutputStream;

public class FileUtils {
   public static final long ONE_KB = 1024L;
   public static final long ONE_MB = 1048576L;
   public static final long ONE_GB = 1073741824L;
   public static final File[] EMPTY_FILE_ARRAY = new File[0];

   public static FileInputStream openInputStream(File file) throws IOException {
      if (file.exists()) {
         if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
         } else if (!file.canRead()) {
            throw new IOException("File '" + file + "' cannot be read");
         } else {
            return new FileInputStream(file);
         }
      } else {
         throw new FileNotFoundException("File '" + file + "' does not exist");
      }
   }

   public static FileOutputStream openOutputStream(File file) throws IOException {
      if (file.exists()) {
         if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
         }

         if (!file.canWrite()) {
            throw new IOException("File '" + file + "' cannot be written to");
         }
      } else {
         File parent = file.getParentFile();
         if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("File '" + file + "' could not be created");
         }
      }

      return new FileOutputStream(file);
   }

   public static String byteCountToDisplaySize(long size) {
      String displaySize;
      if (size / 1073741824L > 0L) {
         displaySize = size / 1073741824L + " GB";
      } else if (size / 1048576L > 0L) {
         displaySize = size / 1048576L + " MB";
      } else if (size / 1024L > 0L) {
         displaySize = size / 1024L + " KB";
      } else {
         displaySize = size + " bytes";
      }

      return displaySize;
   }

   public static void touch(File file) throws IOException {
      if (!file.exists()) {
         OutputStream out = openOutputStream(file);
         IOUtils.closeQuietly((OutputStream)out);
      }

      boolean success = file.setLastModified(System.currentTimeMillis());
      if (!success) {
         throw new IOException("Unable to set the last modification time for " + file);
      }
   }

   public static File[] convertFileCollectionToFileArray(Collection files) {
      return (File[])files.toArray(new File[files.size()]);
   }

   private static void innerListFiles(Collection files, File directory, IOFileFilter filter) {
      File[] found = directory.listFiles(filter);
      if (found != null) {
         for(int i = 0; i < found.length; ++i) {
            if (found[i].isDirectory()) {
               innerListFiles(files, found[i], filter);
            } else {
               files.add(found[i]);
            }
         }
      }

   }

   public static Collection listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
      if (!directory.isDirectory()) {
         throw new IllegalArgumentException("Parameter 'directory' is not a directory");
      } else if (fileFilter == null) {
         throw new NullPointerException("Parameter 'fileFilter' is null");
      } else {
         IOFileFilter effFileFilter = FileFilterUtils.andFileFilter(fileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE));
         IOFileFilter effDirFilter;
         if (dirFilter == null) {
            effDirFilter = FalseFileFilter.INSTANCE;
         } else {
            effDirFilter = FileFilterUtils.andFileFilter(dirFilter, DirectoryFileFilter.INSTANCE);
         }

         Collection files = new LinkedList();
         innerListFiles(files, directory, FileFilterUtils.orFileFilter(effFileFilter, effDirFilter));
         return files;
      }
   }

   public static Iterator iterateFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
      return listFiles(directory, fileFilter, dirFilter).iterator();
   }

   private static String[] toSuffixes(String[] extensions) {
      String[] suffixes = new String[extensions.length];

      for(int i = 0; i < extensions.length; ++i) {
         suffixes[i] = "." + extensions[i];
      }

      return suffixes;
   }

   public static Collection listFiles(File directory, String[] extensions, boolean recursive) {
      Object filter;
      if (extensions == null) {
         filter = TrueFileFilter.INSTANCE;
      } else {
         String[] suffixes = toSuffixes(extensions);
         filter = new SuffixFileFilter(suffixes);
      }

      return listFiles(directory, (IOFileFilter)filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
   }

   public static Iterator iterateFiles(File directory, String[] extensions, boolean recursive) {
      return listFiles(directory, extensions, recursive).iterator();
   }

   public static boolean contentEquals(File file1, File file2) throws IOException {
      boolean file1Exists = file1.exists();
      if (file1Exists != file2.exists()) {
         return false;
      } else if (!file1Exists) {
         return true;
      } else if (!file1.isDirectory() && !file2.isDirectory()) {
         if (file1.length() != file2.length()) {
            return false;
         } else if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            return true;
         } else {
            InputStream input1 = null;
            InputStream input2 = null;

            boolean var5;
            try {
               input1 = new FileInputStream(file1);
               input2 = new FileInputStream(file2);
               var5 = IOUtils.contentEquals((InputStream)input1, (InputStream)input2);
            } finally {
               IOUtils.closeQuietly((InputStream)input1);
               IOUtils.closeQuietly((InputStream)input2);
            }

            return var5;
         }
      } else {
         throw new IOException("Can't compare directories, only files");
      }
   }

   public static File toFile(URL url) {
      if (url != null && url.getProtocol().equals("file")) {
         String filename = url.getFile().replace('/', File.separatorChar);
         int pos = 0;

         while((pos = filename.indexOf(37, pos)) >= 0) {
            if (pos + 2 < filename.length()) {
               String hexStr = filename.substring(pos + 1, pos + 3);
               char ch = (char)Integer.parseInt(hexStr, 16);
               filename = filename.substring(0, pos) + ch + filename.substring(pos + 3);
            }
         }

         return new File(filename);
      } else {
         return null;
      }
   }

   public static File[] toFiles(URL[] urls) {
      if (urls != null && urls.length != 0) {
         File[] files = new File[urls.length];

         for(int i = 0; i < urls.length; ++i) {
            URL url = urls[i];
            if (url != null) {
               if (!url.getProtocol().equals("file")) {
                  throw new IllegalArgumentException("URL could not be converted to a File: " + url);
               }

               files[i] = toFile(url);
            }
         }

         return files;
      } else {
         return EMPTY_FILE_ARRAY;
      }
   }

   public static URL[] toURLs(File[] files) throws IOException {
      URL[] urls = new URL[files.length];

      for(int i = 0; i < urls.length; ++i) {
         urls[i] = files[i].toURL();
      }

      return urls;
   }

   public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
      copyFileToDirectory(srcFile, destDir, true);
   }

   public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
      if (destDir == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (destDir.exists() && !destDir.isDirectory()) {
         throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
      } else {
         copyFile(srcFile, new File(destDir, srcFile.getName()), preserveFileDate);
      }
   }

   public static void copyFile(File srcFile, File destFile) throws IOException {
      copyFile(srcFile, destFile, true);
   }

   public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
      if (srcFile == null) {
         throw new NullPointerException("Source must not be null");
      } else if (destFile == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!srcFile.exists()) {
         throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
      } else if (srcFile.isDirectory()) {
         throw new IOException("Source '" + srcFile + "' exists but is a directory");
      } else if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
         throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
      } else if (destFile.getParentFile() != null && !destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs()) {
         throw new IOException("Destination '" + destFile + "' directory cannot be created");
      } else if (destFile.exists() && !destFile.canWrite()) {
         throw new IOException("Destination '" + destFile + "' exists but is read-only");
      } else {
         doCopyFile(srcFile, destFile, preserveFileDate);
      }
   }

   private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
      if (destFile.exists() && destFile.isDirectory()) {
         throw new IOException("Destination '" + destFile + "' exists but is a directory");
      } else {
         FileInputStream input = new FileInputStream(srcFile);

         try {
            FileOutputStream output = new FileOutputStream(destFile);

            try {
               IOUtils.copy((InputStream)input, (OutputStream)output);
            } finally {
               IOUtils.closeQuietly((OutputStream)output);
            }
         } finally {
            IOUtils.closeQuietly((InputStream)input);
         }

         if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
         } else {
            if (preserveFileDate) {
               destFile.setLastModified(srcFile.lastModified());
            }

         }
      }
   }

   public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
      if (srcDir == null) {
         throw new NullPointerException("Source must not be null");
      } else if (srcDir.exists() && !srcDir.isDirectory()) {
         throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
      } else if (destDir == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (destDir.exists() && !destDir.isDirectory()) {
         throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
      } else {
         copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
      }
   }

   public static void copyDirectory(File srcDir, File destDir) throws IOException {
      copyDirectory(srcDir, destDir, true);
   }

   public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
      if (srcDir == null) {
         throw new NullPointerException("Source must not be null");
      } else if (destDir == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!srcDir.exists()) {
         throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
      } else if (!srcDir.isDirectory()) {
         throw new IOException("Source '" + srcDir + "' exists but is not a directory");
      } else if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
         throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
      } else {
         doCopyDirectory(srcDir, destDir, preserveFileDate);
      }
   }

   private static void doCopyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
      if (destDir.exists()) {
         if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' exists but is not a directory");
         }
      } else {
         if (!destDir.mkdirs()) {
            throw new IOException("Destination '" + destDir + "' directory cannot be created");
         }

         if (preserveFileDate) {
            destDir.setLastModified(srcDir.lastModified());
         }
      }

      if (!destDir.canWrite()) {
         throw new IOException("Destination '" + destDir + "' cannot be written to");
      } else {
         File[] files = srcDir.listFiles();
         if (files == null) {
            throw new IOException("Failed to list contents of " + srcDir);
         } else {
            for(int i = 0; i < files.length; ++i) {
               File copiedFile = new File(destDir, files[i].getName());
               if (files[i].isDirectory()) {
                  doCopyDirectory(files[i], copiedFile, preserveFileDate);
               } else {
                  doCopyFile(files[i], copiedFile, preserveFileDate);
               }
            }

         }
      }
   }

   public static void copyURLToFile(URL source, File destination) throws IOException {
      InputStream input = source.openStream();

      try {
         FileOutputStream output = openOutputStream(destination);

         try {
            IOUtils.copy((InputStream)input, (OutputStream)output);
         } finally {
            IOUtils.closeQuietly((OutputStream)output);
         }
      } finally {
         IOUtils.closeQuietly(input);
      }

   }

   public static void deleteDirectory(File directory) throws IOException {
      if (directory.exists()) {
         cleanDirectory(directory);
         if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
         }
      }
   }

   public static void cleanDirectory(File directory) throws IOException {
      String message;
      if (!directory.exists()) {
         message = directory + " does not exist";
         throw new IllegalArgumentException(message);
      } else if (!directory.isDirectory()) {
         message = directory + " is not a directory";
         throw new IllegalArgumentException(message);
      } else {
         File[] files = directory.listFiles();
         if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
         } else {
            IOException exception = null;

            for(int i = 0; i < files.length; ++i) {
               File file = files[i];

               try {
                  forceDelete(file);
               } catch (IOException var6) {
                  exception = var6;
               }
            }

            if (null != exception) {
               throw exception;
            }
         }
      }
   }

   public static boolean waitFor(File file, int seconds) {
      int timeout = 0;
      int tick = 0;

      while(!file.exists()) {
         if (tick++ >= 10) {
            tick = 0;
            if (timeout++ > seconds) {
               return false;
            }
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var6) {
         } catch (Exception var7) {
            break;
         }
      }

      return true;
   }

   public static String readFileToString(File file, String encoding) throws IOException {
      InputStream in = null;

      String var3;
      try {
         in = openInputStream(file);
         var3 = IOUtils.toString((InputStream)in, encoding);
      } finally {
         IOUtils.closeQuietly((InputStream)in);
      }

      return var3;
   }

   public static String readFileToString(File file) throws IOException {
      return readFileToString(file, (String)null);
   }

   public static byte[] readFileToByteArray(File file) throws IOException {
      InputStream in = null;

      byte[] var2;
      try {
         in = openInputStream(file);
         var2 = IOUtils.toByteArray((InputStream)in);
      } finally {
         IOUtils.closeQuietly((InputStream)in);
      }

      return var2;
   }

   public static List readLines(File file, String encoding) throws IOException {
      InputStream in = null;

      List var3;
      try {
         in = openInputStream(file);
         var3 = IOUtils.readLines(in, encoding);
      } finally {
         IOUtils.closeQuietly((InputStream)in);
      }

      return var3;
   }

   public static List readLines(File file) throws IOException {
      return readLines(file, (String)null);
   }

   public static LineIterator lineIterator(File file, String encoding) throws IOException {
      InputStream in = null;

      try {
         in = openInputStream(file);
         return IOUtils.lineIterator(in, encoding);
      } catch (IOException var5) {
         IOUtils.closeQuietly((InputStream)in);
         throw var5;
      } catch (RuntimeException var6) {
         IOUtils.closeQuietly((InputStream)in);
         throw var6;
      }
   }

   public static LineIterator lineIterator(File file) throws IOException {
      return lineIterator(file, (String)null);
   }

   public static void writeStringToFile(File file, String data, String encoding) throws IOException {
      OutputStream out = null;

      try {
         out = openOutputStream(file);
         IOUtils.write((String)data, (OutputStream)out, encoding);
      } finally {
         IOUtils.closeQuietly((OutputStream)out);
      }

   }

   public static void writeStringToFile(File file, String data) throws IOException {
      writeStringToFile(file, data, (String)null);
   }

   public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
      OutputStream out = null;

      try {
         out = openOutputStream(file);
         out.write(data);
      } finally {
         IOUtils.closeQuietly((OutputStream)out);
      }

   }

   public static void writeLines(File file, String encoding, Collection lines) throws IOException {
      writeLines(file, encoding, lines, (String)null);
   }

   public static void writeLines(File file, Collection lines) throws IOException {
      writeLines(file, (String)null, lines, (String)null);
   }

   public static void writeLines(File file, String encoding, Collection lines, String lineEnding) throws IOException {
      OutputStream out = null;

      try {
         out = openOutputStream(file);
         IOUtils.writeLines(lines, lineEnding, out, encoding);
      } finally {
         IOUtils.closeQuietly((OutputStream)out);
      }

   }

   public static void writeLines(File file, Collection lines, String lineEnding) throws IOException {
      writeLines(file, (String)null, lines, lineEnding);
   }

   public static void forceDelete(File file) throws IOException {
      if (file.isDirectory()) {
         deleteDirectory(file);
      } else {
         if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file);
         }

         if (!file.delete()) {
            String message = "Unable to delete file: " + file;
            throw new IOException(message);
         }
      }

   }

   public static void forceDeleteOnExit(File file) throws IOException {
      if (file.isDirectory()) {
         deleteDirectoryOnExit(file);
      } else {
         file.deleteOnExit();
      }

   }

   private static void deleteDirectoryOnExit(File directory) throws IOException {
      if (directory.exists()) {
         cleanDirectoryOnExit(directory);
         directory.deleteOnExit();
      }
   }

   private static void cleanDirectoryOnExit(File directory) throws IOException {
      String message;
      if (!directory.exists()) {
         message = directory + " does not exist";
         throw new IllegalArgumentException(message);
      } else if (!directory.isDirectory()) {
         message = directory + " is not a directory";
         throw new IllegalArgumentException(message);
      } else {
         File[] files = directory.listFiles();
         if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
         } else {
            IOException exception = null;

            for(int i = 0; i < files.length; ++i) {
               File file = files[i];

               try {
                  forceDeleteOnExit(file);
               } catch (IOException var6) {
                  exception = var6;
               }
            }

            if (null != exception) {
               throw exception;
            }
         }
      }
   }

   public static void forceMkdir(File directory) throws IOException {
      String message;
      if (directory.exists()) {
         if (directory.isFile()) {
            message = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
            throw new IOException(message);
         }
      } else if (!directory.mkdirs()) {
         message = "Unable to create directory " + directory;
         throw new IOException(message);
      }

   }

   public static long sizeOfDirectory(File directory) {
      String message;
      if (!directory.exists()) {
         message = directory + " does not exist";
         throw new IllegalArgumentException(message);
      } else if (!directory.isDirectory()) {
         message = directory + " is not a directory";
         throw new IllegalArgumentException(message);
      } else {
         long size = 0L;
         File[] files = directory.listFiles();
         if (files == null) {
            return 0L;
         } else {
            for(int i = 0; i < files.length; ++i) {
               File file = files[i];
               if (file.isDirectory()) {
                  size += sizeOfDirectory(file);
               } else {
                  size += file.length();
               }
            }

            return size;
         }
      }
   }

   public static boolean isFileNewer(File file, File reference) {
      if (reference == null) {
         throw new IllegalArgumentException("No specified reference file");
      } else if (!reference.exists()) {
         throw new IllegalArgumentException("The reference file '" + file + "' doesn't exist");
      } else {
         return isFileNewer(file, reference.lastModified());
      }
   }

   public static boolean isFileNewer(File file, Date date) {
      if (date == null) {
         throw new IllegalArgumentException("No specified date");
      } else {
         return isFileNewer(file, date.getTime());
      }
   }

   public static boolean isFileNewer(File file, long timeMillis) {
      if (file == null) {
         throw new IllegalArgumentException("No specified file");
      } else if (!file.exists()) {
         return false;
      } else {
         return file.lastModified() > timeMillis;
      }
   }

   public static boolean isFileOlder(File file, File reference) {
      if (reference == null) {
         throw new IllegalArgumentException("No specified reference file");
      } else if (!reference.exists()) {
         throw new IllegalArgumentException("The reference file '" + file + "' doesn't exist");
      } else {
         return isFileOlder(file, reference.lastModified());
      }
   }

   public static boolean isFileOlder(File file, Date date) {
      if (date == null) {
         throw new IllegalArgumentException("No specified date");
      } else {
         return isFileOlder(file, date.getTime());
      }
   }

   public static boolean isFileOlder(File file, long timeMillis) {
      if (file == null) {
         throw new IllegalArgumentException("No specified file");
      } else if (!file.exists()) {
         return false;
      } else {
         return file.lastModified() < timeMillis;
      }
   }

   public static long checksumCRC32(File file) throws IOException {
      CRC32 crc = new CRC32();
      checksum(file, crc);
      return crc.getValue();
   }

   public static Checksum checksum(File file, Checksum checksum) throws IOException {
      if (file.isDirectory()) {
         throw new IllegalArgumentException("Checksums can't be computed on directories");
      } else {
         InputStream in = null;

         try {
            in = new CheckedInputStream(new FileInputStream(file), checksum);
            IOUtils.copy((InputStream)in, (OutputStream)(new NullOutputStream()));
         } finally {
            IOUtils.closeQuietly((InputStream)in);
         }

         return checksum;
      }
   }
}
