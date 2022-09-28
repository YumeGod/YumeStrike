package org.apache.fop.tools.anttasks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;

public class FileCompare {
   private String referenceDirectory;
   private String testDirectory;
   private String[] filenameList;
   private String filenames;

   public void setTestDirectory(String testDirectory) {
      if (!(testDirectory.endsWith("/") | testDirectory.endsWith("\\"))) {
         testDirectory = testDirectory + File.separator;
      }

      this.testDirectory = testDirectory;
   }

   public void setReferenceDirectory(String referenceDirectory) {
      if (!(referenceDirectory.endsWith("/") | referenceDirectory.endsWith("\\"))) {
         referenceDirectory = referenceDirectory + File.separator;
      }

      this.referenceDirectory = referenceDirectory;
   }

   public void setFilenames(String filenames) {
      StringTokenizer tokens = new StringTokenizer(filenames, ",");
      List filenameListTmp = new ArrayList(20);

      while(tokens.hasMoreTokens()) {
         filenameListTmp.add(tokens.nextToken());
      }

      this.filenameList = new String[filenameListTmp.size()];
      this.filenameList = (String[])filenameListTmp.toArray(new String[0]);
   }

   public static boolean compareFiles(File f1, File f2) throws IOException {
      return compareFileSize(f1, f2) && compareBytes(f1, f2);
   }

   private static boolean compareBytes(File file1, File file2) throws IOException {
      BufferedInputStream file1Input = new BufferedInputStream(new FileInputStream(file1));
      BufferedInputStream file2Input = new BufferedInputStream(new FileInputStream(file2));
      int charact1 = 0;

      for(int charact2 = 0; charact1 != -1; charact2 = file2Input.read()) {
         if (charact1 != charact2) {
            return false;
         }

         charact1 = file1Input.read();
      }

      return true;
   }

   private static boolean compareFileSize(File oldFile, File newFile) {
      return oldFile.length() == newFile.length();
   }

   private boolean filesExist(File oldFile, File newFile) {
      if (!oldFile.exists()) {
         System.err.println("Task Compare - ERROR: File " + this.referenceDirectory + oldFile.getName() + " doesn't exist!");
         return false;
      } else if (!newFile.exists()) {
         System.err.println("Task Compare - ERROR: File " + this.testDirectory + newFile.getName() + " doesn't exist!");
         return false;
      } else {
         return true;
      }
   }

   private void writeHeader(PrintWriter results) {
      String dateTime = DateFormat.getDateTimeInstance(2, 2).format(new Date());
      results.println("<html><head><title>Test Results</title></head><body>\n");
      results.println("<h2>Compare Results<br>");
      results.println("<font size='1'>created " + dateTime + "</font></h2>");
      results.println("<table cellpadding='10' border='2'><thead><th align='center'>reference file</th><th align='center'>test file</th><th align='center'>identical?</th></thead>");
   }

   public void execute() throws BuildException {
      boolean identical = false;

      try {
         PrintWriter results = new PrintWriter(new FileWriter("results.html"), true);
         this.writeHeader(results);

         for(int i = 0; i < this.filenameList.length; ++i) {
            File oldFile = new File(this.referenceDirectory + this.filenameList[i]);
            File newFile = new File(this.testDirectory + this.filenameList[i]);
            if (this.filesExist(oldFile, newFile)) {
               identical = compareFileSize(oldFile, newFile);
               if (identical) {
                  identical = compareBytes(oldFile, newFile);
               }

               if (!identical) {
                  System.out.println("Task Compare: \nFiles " + this.referenceDirectory + oldFile.getName() + " - " + this.testDirectory + newFile.getName() + " are *not* identical.");
                  results.println("<tr><td><a href='" + this.referenceDirectory + oldFile.getName() + "'>" + oldFile.getName() + "</a> </td><td> <a href='" + this.testDirectory + newFile.getName() + "'>" + newFile.getName() + "</a>" + " </td><td><font color='red'>No</font></td></tr>");
               } else {
                  results.println("<tr><td><a href='" + this.referenceDirectory + oldFile.getName() + "'>" + oldFile.getName() + "</a> </td><td> <a href='" + this.testDirectory + newFile.getName() + "'>" + newFile.getName() + "</a>" + " </td><td>Yes</td></tr>");
               }
            }
         }

         results.println("</table></html>");
      } catch (IOException var6) {
         System.err.println("ERROR: " + var6);
      }

   }
}
