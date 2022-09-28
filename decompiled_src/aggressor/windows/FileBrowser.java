package aggressor.windows;

import aggressor.AggressorClient;
import common.AObject;
import console.AssociatedPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import ui.FileBrowserNode;

public class FileBrowser extends AObject {
   protected String bid = "";
   protected AggressorClient client = null;
   protected Files browser = null;
   protected JTree tree = null;

   public FileBrowser(AggressorClient var1, String var2) {
      this.client = var1;
      this.bid = var2;
      this.browser = new Files(var1, var2);
   }

   public JComponent getContent() {
      AssociatedPanel var1 = new AssociatedPanel(this.bid);
      var1.setLayout(new BorderLayout());
      JSplitPane var2 = new JSplitPane(1);
      var2.add(this.getTree());
      var2.add(this.browser.getContent());
      var2.setDividerLocation(320);
      var1.add(var2, "Center");
      var1.add(this.browser.getButtons(), "South");
      this.browser.ls(".");
      return var1;
   }

   public TreeCellRenderer getNewRenderer() {
      DefaultTreeCellRenderer var1 = new DefaultTreeCellRenderer() {
         public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
            super.getTreeCellRendererComponent(var1, (Object)null, false, var4, var5, var6, false);
            DefaultMutableTreeNode var8 = (DefaultMutableTreeNode)var2;
            if (var8 == null) {
               return this;
            } else {
               FileBrowserNode var9 = (FileBrowserNode)var8.getUserObject();
               if (var9 == null) {
                  return this;
               } else {
                  if (FileBrowser.this.browser.getNodes().isSelected(var9)) {
                     super.getTreeCellRendererComponent(var1, (Object)null, true, var4, var5, var6, false);
                  }

                  this.setText(var9.getName());
                  if (var9.isComputer()) {
                     this.setIcon(UIManager.getIcon("FileView.computerIcon"));
                  } else if (var9.isDrive()) {
                     this.setIcon(UIManager.getIcon("FileView.hardDriveIcon"));
                  } else {
                     this.setIcon(UIManager.getIcon("FileView.directoryIcon"));
                  }

                  this.setEnabled(var9.hasCache() || var9.isComputer());
                  return this;
               }
            }
         }
      };
      var1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      return var1;
   }

   public void doMouseClicked(MouseEvent var1) {
      TreePath var2 = this.tree.getPathForLocation(var1.getX(), var1.getY());
      if (var2 != null) {
         DefaultMutableTreeNode var3 = (DefaultMutableTreeNode)var2.getLastPathComponent();
         if (var3 != null) {
            FileBrowserNode var4 = (FileBrowserNode)var3.getUserObject();
            if (var4 != null) {
               this.browser.gotof(var4);
            }
         }
      }
   }

   public JComponent getTree() {
      this.tree = new JTree(this.browser.getTreeModel());
      this.tree.setRootVisible(false);
      this.tree.setCellRenderer(this.getNewRenderer());
      this.tree.setScrollsOnExpand(true);
      this.tree.setPreferredSize((Dimension)null);
      this.tree.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent var1) {
            FileBrowser.this.doMouseClicked(var1);
         }
      });
      this.browser.setTree(this.tree);
      return new JScrollPane(this.tree);
   }
}
