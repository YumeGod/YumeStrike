package org.apache.xml.utils;

import java.io.File;
import javax.xml.transform.TransformerException;

public class SystemIDResolver {
   public static String getAbsoluteURIFromRelative(String localPath) {
      if (localPath != null && localPath.length() != 0) {
         String absolutePath = localPath;
         if (!isAbsolutePath(localPath)) {
            try {
               absolutePath = getAbsolutePathFromRelativePath(localPath);
            } catch (SecurityException var3) {
               return "file:" + localPath;
            }
         }

         String urlString;
         if (null != absolutePath) {
            if (absolutePath.startsWith(File.separator)) {
               urlString = "file://" + absolutePath;
            } else {
               urlString = "file:///" + absolutePath;
            }
         } else {
            urlString = "file:" + localPath;
         }

         return replaceChars(urlString);
      } else {
         return "";
      }
   }

   private static String getAbsolutePathFromRelativePath(String relativePath) {
      return (new File(relativePath)).getAbsolutePath();
   }

   public static boolean isAbsoluteURI(String systemId) {
      if (isWindowsAbsolutePath(systemId)) {
         return false;
      } else {
         int fragmentIndex = systemId.indexOf(35);
         int queryIndex = systemId.indexOf(63);
         int slashIndex = systemId.indexOf(47);
         int colonIndex = systemId.indexOf(58);
         int index = systemId.length() - 1;
         if (fragmentIndex > 0) {
            index = fragmentIndex;
         }

         if (queryIndex > 0 && queryIndex < index) {
            index = queryIndex;
         }

         if (slashIndex > 0 && slashIndex < index) {
            index = slashIndex;
         }

         return colonIndex > 0 && colonIndex < index;
      }
   }

   public static boolean isAbsolutePath(String systemId) {
      if (systemId == null) {
         return false;
      } else {
         File file = new File(systemId);
         return file.isAbsolute();
      }
   }

   private static boolean isWindowsAbsolutePath(String systemId) {
      if (!isAbsolutePath(systemId)) {
         return false;
      } else {
         return systemId.length() > 2 && systemId.charAt(1) == ':' && Character.isLetter(systemId.charAt(0)) && (systemId.charAt(2) == '\\' || systemId.charAt(2) == '/');
      }
   }

   private static String replaceChars(String str) {
      StringBuffer buf = new StringBuffer(str);
      int length = buf.length();

      for(int i = 0; i < length; ++i) {
         char currentChar = buf.charAt(i);
         if (currentChar == ' ') {
            buf.setCharAt(i, '%');
            buf.insert(i + 1, "20");
            length += 2;
            i += 2;
         } else if (currentChar == '\\') {
            buf.setCharAt(i, '/');
         }
      }

      return buf.toString();
   }

   public static String getAbsoluteURI(String systemId) {
      String absoluteURI = systemId;
      if (isAbsoluteURI(systemId)) {
         if (!systemId.startsWith("file:")) {
            return systemId;
         } else {
            String str = systemId.substring(5);
            if (str != null && str.startsWith("/")) {
               if (str.startsWith("///") || !str.startsWith("//")) {
                  int secondColonIndex = systemId.indexOf(58, 5);
                  if (secondColonIndex > 0) {
                     String localPath = systemId.substring(secondColonIndex - 1);

                     try {
                        if (!isAbsolutePath(localPath)) {
                           absoluteURI = systemId.substring(0, secondColonIndex - 1) + getAbsolutePathFromRelativePath(localPath);
                        }
                     } catch (SecurityException var6) {
                        return systemId;
                     }
                  }
               }

               return replaceChars(absoluteURI);
            } else {
               return getAbsoluteURIFromRelative(systemId.substring(5));
            }
         }
      } else {
         return getAbsoluteURIFromRelative(systemId);
      }
   }

   public static String getAbsoluteURI(String urlString, String base) throws TransformerException {
      if (base == null) {
         return getAbsoluteURI(urlString);
      } else {
         String absoluteBase = getAbsoluteURI(base);
         URI uri = null;

         try {
            URI baseURI = new URI(absoluteBase);
            uri = new URI(baseURI, urlString);
         } catch (URI.MalformedURIException var5) {
            throw new TransformerException(var5);
         }

         return replaceChars(uri.toString());
      }
   }
}
