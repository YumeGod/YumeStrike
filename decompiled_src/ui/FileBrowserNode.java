package ui;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileBrowserNode implements Comparable {
   protected String display;
   protected String path;
   protected String[] walk;
   protected String cache = null;
   protected boolean isUNC = false;

   public FileBrowserNode(String var1) {
      if (var1.startsWith("\\\\")) {
         var1 = var1.substring(2);
         this.isUNC = true;
      }

      this.walk = var1.split("\\\\");
      this.path = this.getPath();
      if (this.walk.length == 0) {
         this.isUNC = true;
         this.display = "[error]";
         this.path = "[error]";
      } else {
         this.display = this.walk[this.walk.length - 1];
      }

   }

   public int compareTo(Object var1) {
      FileBrowserNode var2 = (FileBrowserNode)var1;
      return this.path.toLowerCase().compareTo(var2.path.toLowerCase());
   }

   public boolean isComputer() {
      return this.isDrive() && this.isUNC;
   }

   public boolean isDrive() {
      return this.walk.length == 1;
   }

   public DefaultMutableTreeNode getTreeNode() {
      return new DefaultMutableTreeNode(this);
   }

   public boolean hasCache() {
      return this.cache != null;
   }

   public String getCache() {
      return this.cache;
   }

   public void setCache(String var1) {
      this.cache = var1;
   }

   public String getParent() {
      if (this.isComputer()) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         if (this.isUNC) {
            var1.append("\\\\");
         }

         for(int var2 = 0; var2 < this.walk.length - 1; ++var2) {
            var1.append(this.walk[var2]);
            var1.append("\\");
         }

         return var1.toString();
      }
   }

   public String getPathNoTrailingSlash() {
      String var1 = this.getPath();
      return var1.substring(0, var1.length() - 1);
   }

   public String getPath() {
      StringBuffer var1 = new StringBuffer();
      if (this.isUNC) {
         var1.append("\\\\");
      }

      for(int var2 = 0; var2 < this.walk.length; ++var2) {
         var1.append(this.walk[var2]);
         var1.append("\\");
      }

      return var1.toString();
   }

   public String getChild(String var1) {
      return this.getPath() + var1;
   }

   public String getName() {
      return this.display;
   }

   public String toString() {
      return this.getName();
   }
}
