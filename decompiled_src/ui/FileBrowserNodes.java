package ui;

import common.CommonUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileBrowserNodes {
   protected Map cache = new HashMap();
   protected List all = new LinkedList();
   protected FileBrowserNode selected = null;
   protected DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode());

   public boolean isSelected(FileBrowserNode var1) {
      return var1 == this.selected;
   }

   public FileBrowserNode getSelected() {
      return this.selected;
   }

   public void setSelected(FileBrowserNode var1) {
      this.selected = var1;
   }

   public TreeModel getModel() {
      return this.model;
   }

   public void refresh(final JTree var1) {
      DefaultMutableTreeNode var2 = null;
      final DefaultMutableTreeNode var3 = new DefaultMutableTreeNode();
      HashMap var4 = new HashMap();
      var4.put("", var3);
      Collections.sort(this.all);

      FileBrowserNode var6;
      DefaultMutableTreeNode var7;
      for(Iterator var5 = this.all.iterator(); var5.hasNext(); var4.put(var6.getPath().toLowerCase(), var7)) {
         var6 = (FileBrowserNode)var5.next();
         var7 = var6.getTreeNode();
         DefaultMutableTreeNode var8 = (DefaultMutableTreeNode)var4.get(var6.getParent().toLowerCase());
         if (var8 != null) {
            var8.add(var7);
         }

         if (this.isSelected(var6)) {
            var2 = var7;
         }
      }

      final DefaultMutableTreeNode var9 = var2 != null && var2.isLeaf() ? (DefaultMutableTreeNode)var2.getParent() : var2;
      CommonUtils.runSafe(new Runnable() {
         public void run() {
            FileBrowserNodes.this.model.setRoot(var3);
            if (var9 != null) {
               var1.expandPath(new TreePath(var9.getPath()));
            }

         }
      });
   }

   public FileBrowserNode getNodeFromCache(String var1) {
      var1 = var1.toLowerCase();
      if (this.cache.containsKey(var1)) {
         return (FileBrowserNode)this.cache.get(var1);
      } else {
         return this.cache.containsKey(var1 + "\\") ? (FileBrowserNode)this.cache.get(var1 + "\\") : null;
      }
   }

   public FileBrowserNode getNode(String var1) {
      String var2 = var1.toLowerCase();
      if (this.cache.containsKey(var2)) {
         return (FileBrowserNode)this.cache.get(var2);
      } else if (this.cache.containsKey(var2 + "\\")) {
         return (FileBrowserNode)this.cache.get(var2 + "\\");
      } else {
         FileBrowserNode var3 = new FileBrowserNode(var1);
         this.cache.put(var3.getPath().toLowerCase(), var3);
         this.all.add(var3);
         if (!"".equals(var3.getParent())) {
            this.getNode(var3.getParent());
         }

         return var3;
      }
   }

   public FileBrowserNode addNode(String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(var1);
      if (!var1.endsWith("\\")) {
         var3.append("\\");
      }

      var3.append(var2);
      return this.getNode(var3.toString());
   }
}
