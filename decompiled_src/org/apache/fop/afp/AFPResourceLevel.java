package org.apache.fop.afp;

public class AFPResourceLevel {
   public static final int INLINE = 0;
   public static final int PAGE = 1;
   public static final int PAGE_GROUP = 2;
   public static final int DOCUMENT = 3;
   public static final int PRINT_FILE = 4;
   public static final int EXTERNAL = 5;
   private static final String NAME_INLINE = "inline";
   private static final String NAME_PAGE = "page";
   private static final String NAME_PAGE_GROUP = "page-group";
   private static final String NAME_DOCUMENT = "document";
   private static final String NAME_PRINT_FILE = "print-file";
   private static final String NAME_EXTERNAL = "external";
   private static final String[] NAMES = new String[]{"inline", "page", "page-group", "document", "print-file", "external"};
   private int level = 4;
   private String extFilePath = null;

   public static AFPResourceLevel valueOf(String levelString) {
      if (levelString == null) {
         return null;
      } else {
         levelString = levelString.toLowerCase();
         AFPResourceLevel resourceLevel = null;

         for(int i = 0; i < NAMES.length; ++i) {
            if (NAMES[i].equals(levelString)) {
               resourceLevel = new AFPResourceLevel(i);
               break;
            }
         }

         return resourceLevel;
      }
   }

   public AFPResourceLevel(int level) {
      this.setLevel(level);
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public boolean isPage() {
      return this.level == 1;
   }

   public boolean isPageGroup() {
      return this.level == 2;
   }

   public boolean isDocument() {
      return this.level == 3;
   }

   public boolean isExternal() {
      return this.level == 5;
   }

   public boolean isPrintFile() {
      return this.level == 4;
   }

   public boolean isInline() {
      return this.level == 0;
   }

   public String getExternalFilePath() {
      return this.extFilePath;
   }

   public void setExternalFilePath(String filePath) {
      this.extFilePath = filePath;
   }

   public String toString() {
      return NAMES[this.level] + (this.isExternal() ? ", file=" + this.extFilePath : "");
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && obj instanceof AFPResourceLevel) {
         AFPResourceLevel rl = (AFPResourceLevel)obj;
         return this.level == this.level && (this.extFilePath == rl.extFilePath || this.extFilePath != null && this.extFilePath.equals(rl.extFilePath));
      } else {
         return false;
      }
   }

   public int hashCode() {
      int hash = 7;
      hash = 31 * hash + this.level;
      hash = 31 * hash + (null == this.extFilePath ? 0 : this.extFilePath.hashCode());
      return hash;
   }
}
