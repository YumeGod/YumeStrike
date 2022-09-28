package org.apache.commons.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class FilenameUtils {
   private static final char EXTENSION_SEPARATOR = '.';
   private static final char UNIX_SEPARATOR = '/';
   private static final char WINDOWS_SEPARATOR = '\\';
   private static final char SYSTEM_SEPARATOR;
   private static final char OTHER_SEPARATOR;

   static boolean isSystemWindows() {
      return SYSTEM_SEPARATOR == '\\';
   }

   private static boolean isSeparator(char ch) {
      return ch == '/' || ch == '\\';
   }

   public static String normalize(String filename) {
      return doNormalize(filename, true);
   }

   public static String normalizeNoEndSeparator(String filename) {
      return doNormalize(filename, false);
   }

   private static String doNormalize(String filename, boolean keepSeparator) {
      if (filename == null) {
         return null;
      } else {
         int size = filename.length();
         if (size == 0) {
            return filename;
         } else {
            int prefix = getPrefixLength(filename);
            if (prefix < 0) {
               return null;
            } else {
               char[] array = new char[size + 2];
               filename.getChars(0, filename.length(), array, 0);

               for(int i = 0; i < array.length; ++i) {
                  if (array[i] == OTHER_SEPARATOR) {
                     array[i] = SYSTEM_SEPARATOR;
                  }
               }

               boolean lastIsDirectory = true;
               if (array[size - 1] != SYSTEM_SEPARATOR) {
                  array[size++] = SYSTEM_SEPARATOR;
                  lastIsDirectory = false;
               }

               for(int i = prefix + 1; i < size; ++i) {
                  if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == SYSTEM_SEPARATOR) {
                     System.arraycopy(array, i, array, i - 1, size - i);
                     --size;
                     --i;
                  }
               }

               for(int i = prefix + 1; i < size; ++i) {
                  if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.' && (i == prefix + 1 || array[i - 2] == SYSTEM_SEPARATOR)) {
                     if (i == size - 1) {
                        lastIsDirectory = true;
                     }

                     System.arraycopy(array, i + 1, array, i - 1, size - i);
                     size -= 2;
                     --i;
                  }
               }

               label106:
               for(int i = prefix + 2; i < size; ++i) {
                  if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.' && array[i - 2] == '.' && (i == prefix + 2 || array[i - 3] == SYSTEM_SEPARATOR)) {
                     if (i == prefix + 2) {
                        return null;
                     }

                     if (i == size - 1) {
                        lastIsDirectory = true;
                     }

                     for(int j = i - 4; j >= prefix; --j) {
                        if (array[j] == SYSTEM_SEPARATOR) {
                           System.arraycopy(array, i + 1, array, j + 1, size - i);
                           size -= i - j;
                           i = j + 1;
                           continue label106;
                        }
                     }

                     System.arraycopy(array, i + 1, array, prefix, size - i);
                     size -= i + 1 - prefix;
                     i = prefix + 1;
                  }
               }

               if (size <= 0) {
                  return "";
               } else if (size <= prefix) {
                  return new String(array, 0, size);
               } else if (lastIsDirectory && keepSeparator) {
                  return new String(array, 0, size);
               } else {
                  return new String(array, 0, size - 1);
               }
            }
         }
      }
   }

   public static String concat(String basePath, String fullFilenameToAdd) {
      int prefix = getPrefixLength(fullFilenameToAdd);
      if (prefix < 0) {
         return null;
      } else if (prefix > 0) {
         return normalize(fullFilenameToAdd);
      } else if (basePath == null) {
         return null;
      } else {
         int len = basePath.length();
         if (len == 0) {
            return normalize(fullFilenameToAdd);
         } else {
            char ch = basePath.charAt(len - 1);
            return isSeparator(ch) ? normalize(basePath + fullFilenameToAdd) : normalize(basePath + '/' + fullFilenameToAdd);
         }
      }
   }

   public static String separatorsToUnix(String path) {
      return path != null && path.indexOf(92) != -1 ? path.replace('\\', '/') : path;
   }

   public static String separatorsToWindows(String path) {
      return path != null && path.indexOf(47) != -1 ? path.replace('/', '\\') : path;
   }

   public static String separatorsToSystem(String path) {
      if (path == null) {
         return null;
      } else {
         return isSystemWindows() ? separatorsToWindows(path) : separatorsToUnix(path);
      }
   }

   public static int getPrefixLength(String filename) {
      if (filename == null) {
         return -1;
      } else {
         int len = filename.length();
         if (len == 0) {
            return 0;
         } else {
            char ch0 = filename.charAt(0);
            if (ch0 == ':') {
               return -1;
            } else if (len == 1) {
               if (ch0 == '~') {
                  return 2;
               } else {
                  return isSeparator(ch0) ? 1 : 0;
               }
            } else {
               int ch1;
               int posUnix;
               if (ch0 == '~') {
                  ch1 = filename.indexOf(47, 1);
                  posUnix = filename.indexOf(92, 1);
                  if (ch1 == -1 && posUnix == -1) {
                     return len + 1;
                  } else {
                     ch1 = ch1 == -1 ? posUnix : ch1;
                     posUnix = posUnix == -1 ? ch1 : posUnix;
                     return Math.min(ch1, posUnix) + 1;
                  }
               } else {
                  ch1 = filename.charAt(1);
                  if (ch1 == 58) {
                     ch0 = Character.toUpperCase(ch0);
                     if (ch0 >= 'A' && ch0 <= 'Z') {
                        return len != 2 && isSeparator(filename.charAt(2)) ? 3 : 2;
                     } else {
                        return -1;
                     }
                  } else if (isSeparator(ch0) && isSeparator((char)ch1)) {
                     posUnix = filename.indexOf(47, 2);
                     int posWin = filename.indexOf(92, 2);
                     if ((posUnix != -1 || posWin != -1) && posUnix != 2 && posWin != 2) {
                        posUnix = posUnix == -1 ? posWin : posUnix;
                        posWin = posWin == -1 ? posUnix : posWin;
                        return Math.min(posUnix, posWin) + 1;
                     } else {
                        return -1;
                     }
                  } else {
                     return isSeparator(ch0) ? 1 : 0;
                  }
               }
            }
         }
      }
   }

   public static int indexOfLastSeparator(String filename) {
      if (filename == null) {
         return -1;
      } else {
         int lastUnixPos = filename.lastIndexOf(47);
         int lastWindowsPos = filename.lastIndexOf(92);
         return Math.max(lastUnixPos, lastWindowsPos);
      }
   }

   public static int indexOfExtension(String filename) {
      if (filename == null) {
         return -1;
      } else {
         int extensionPos = filename.lastIndexOf(46);
         int lastSeparator = indexOfLastSeparator(filename);
         return lastSeparator > extensionPos ? -1 : extensionPos;
      }
   }

   public static String getPrefix(String filename) {
      if (filename == null) {
         return null;
      } else {
         int len = getPrefixLength(filename);
         if (len < 0) {
            return null;
         } else {
            return len > filename.length() ? filename + '/' : filename.substring(0, len);
         }
      }
   }

   public static String getPath(String filename) {
      return doGetPath(filename, 1);
   }

   public static String getPathNoEndSeparator(String filename) {
      return doGetPath(filename, 0);
   }

   private static String doGetPath(String filename, int separatorAdd) {
      if (filename == null) {
         return null;
      } else {
         int prefix = getPrefixLength(filename);
         if (prefix < 0) {
            return null;
         } else {
            int index = indexOfLastSeparator(filename);
            return prefix < filename.length() && index >= 0 ? filename.substring(prefix, index + separatorAdd) : "";
         }
      }
   }

   public static String getFullPath(String filename) {
      return doGetFullPath(filename, true);
   }

   public static String getFullPathNoEndSeparator(String filename) {
      return doGetFullPath(filename, false);
   }

   private static String doGetFullPath(String filename, boolean includeSeparator) {
      if (filename == null) {
         return null;
      } else {
         int prefix = getPrefixLength(filename);
         if (prefix < 0) {
            return null;
         } else if (prefix >= filename.length()) {
            return includeSeparator ? getPrefix(filename) : filename;
         } else {
            int index = indexOfLastSeparator(filename);
            if (index < 0) {
               return filename.substring(0, prefix);
            } else {
               int end = index + (includeSeparator ? 1 : 0);
               return filename.substring(0, end);
            }
         }
      }
   }

   public static String getName(String filename) {
      if (filename == null) {
         return null;
      } else {
         int index = indexOfLastSeparator(filename);
         return filename.substring(index + 1);
      }
   }

   public static String getBaseName(String filename) {
      return removeExtension(getName(filename));
   }

   public static String getExtension(String filename) {
      if (filename == null) {
         return null;
      } else {
         int index = indexOfExtension(filename);
         return index == -1 ? "" : filename.substring(index + 1);
      }
   }

   public static String removeExtension(String filename) {
      if (filename == null) {
         return null;
      } else {
         int index = indexOfExtension(filename);
         return index == -1 ? filename : filename.substring(0, index);
      }
   }

   public static boolean equals(String filename1, String filename2) {
      return equals(filename1, filename2, false, IOCase.SENSITIVE);
   }

   public static boolean equalsOnSystem(String filename1, String filename2) {
      return equals(filename1, filename2, false, IOCase.SYSTEM);
   }

   public static boolean equalsNormalized(String filename1, String filename2) {
      return equals(filename1, filename2, true, IOCase.SENSITIVE);
   }

   public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
      return equals(filename1, filename2, true, IOCase.SYSTEM);
   }

   public static boolean equals(String filename1, String filename2, boolean normalized, IOCase caseSensitivity) {
      if (filename1 != null && filename2 != null) {
         if (normalized) {
            filename1 = normalize(filename1);
            filename2 = normalize(filename2);
         }

         if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
         }

         return caseSensitivity.checkEquals(filename1, filename2);
      } else {
         return filename1 == filename2;
      }
   }

   public static boolean isExtension(String filename, String extension) {
      if (filename == null) {
         return false;
      } else if (extension != null && extension.length() != 0) {
         String fileExt = getExtension(filename);
         return fileExt.equals(extension);
      } else {
         return indexOfExtension(filename) == -1;
      }
   }

   public static boolean isExtension(String filename, String[] extensions) {
      if (filename == null) {
         return false;
      } else if (extensions != null && extensions.length != 0) {
         String fileExt = getExtension(filename);

         for(int i = 0; i < extensions.length; ++i) {
            if (fileExt.equals(extensions[i])) {
               return true;
            }
         }

         return false;
      } else {
         return indexOfExtension(filename) == -1;
      }
   }

   public static boolean isExtension(String filename, Collection extensions) {
      if (filename == null) {
         return false;
      } else if (extensions != null && !extensions.isEmpty()) {
         String fileExt = getExtension(filename);
         Iterator it = extensions.iterator();

         while(it.hasNext()) {
            if (fileExt.equals(it.next())) {
               return true;
            }
         }

         return false;
      } else {
         return indexOfExtension(filename) == -1;
      }
   }

   public static boolean wildcardMatch(String filename, String wildcardMatcher) {
      return wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
   }

   public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
      return wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
   }

   public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
      if (filename == null && wildcardMatcher == null) {
         return true;
      } else if (filename != null && wildcardMatcher != null) {
         if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
         }

         filename = caseSensitivity.convertCase(filename);
         wildcardMatcher = caseSensitivity.convertCase(wildcardMatcher);
         String[] wcs = splitOnTokens(wildcardMatcher);
         boolean anyChars = false;
         int textIdx = 0;
         int wcsIdx = 0;
         Stack backtrack = new Stack();

         do {
            if (backtrack.size() > 0) {
               int[] array = (int[])backtrack.pop();
               wcsIdx = array[0];
               textIdx = array[1];
               anyChars = true;
            }

            for(; wcsIdx < wcs.length; ++wcsIdx) {
               if (wcs[wcsIdx].equals("?")) {
                  ++textIdx;
                  anyChars = false;
               } else if (wcs[wcsIdx].equals("*")) {
                  anyChars = true;
                  if (wcsIdx == wcs.length - 1) {
                     textIdx = filename.length();
                  }
               } else {
                  if (anyChars) {
                     textIdx = filename.indexOf(wcs[wcsIdx], textIdx);
                     if (textIdx == -1) {
                        break;
                     }

                     int repeat = filename.indexOf(wcs[wcsIdx], textIdx + 1);
                     if (repeat >= 0) {
                        backtrack.push(new int[]{wcsIdx, repeat});
                     }
                  } else if (!filename.startsWith(wcs[wcsIdx], textIdx)) {
                     break;
                  }

                  textIdx += wcs[wcsIdx].length();
                  anyChars = false;
               }
            }

            if (wcsIdx == wcs.length && textIdx == filename.length()) {
               return true;
            }
         } while(backtrack.size() > 0);

         return false;
      } else {
         return false;
      }
   }

   static String[] splitOnTokens(String text) {
      if (text.indexOf("?") == -1 && text.indexOf("*") == -1) {
         return new String[]{text};
      } else {
         char[] array = text.toCharArray();
         ArrayList list = new ArrayList();
         StringBuffer buffer = new StringBuffer();

         for(int i = 0; i < array.length; ++i) {
            if (array[i] != '?' && array[i] != '*') {
               buffer.append(array[i]);
            } else {
               if (buffer.length() != 0) {
                  list.add(buffer.toString());
                  buffer.setLength(0);
               }

               if (array[i] == '?') {
                  list.add("?");
               } else if (list.size() == 0 || i > 0 && !list.get(list.size() - 1).equals("*")) {
                  list.add("*");
               }
            }
         }

         if (buffer.length() != 0) {
            list.add(buffer.toString());
         }

         return (String[])list.toArray(new String[list.size()]);
      }
   }

   static {
      SYSTEM_SEPARATOR = File.separatorChar;
      if (isSystemWindows()) {
         OTHER_SEPARATOR = '/';
      } else {
         OTHER_SEPARATOR = '\\';
      }

   }
}
