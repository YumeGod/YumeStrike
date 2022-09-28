package org.apache.batik.util.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.basic.BasicButtonUI;
import org.apache.batik.util.resources.ResourceManager;

public class DropDownComponent extends JPanel {
   private JButton mainButton;
   private JButton dropDownButton;
   private Icon enabledDownArrow;
   private Icon disabledDownArrow;
   private ScrollablePopupMenu popupMenu = this.getPopupMenu();
   private boolean isDropDownEnabled;
   // $FF: synthetic field
   static Class class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener;

   public DropDownComponent(JButton var1) {
      super(new BorderLayout());
      this.mainButton = var1;
      this.add(this.mainButton, "West");
      this.mainButton.setMaximumSize(new Dimension(24, 24));
      this.mainButton.setPreferredSize(new Dimension(24, 24));
      this.enabledDownArrow = new SmallDownArrow();
      this.disabledDownArrow = new SmallDisabledDownArrow();
      this.dropDownButton = new JButton(this.disabledDownArrow);
      this.dropDownButton.setBorderPainted(false);
      this.dropDownButton.setDisabledIcon(this.disabledDownArrow);
      this.dropDownButton.addMouseListener(new DropDownListener());
      this.dropDownButton.setMaximumSize(new Dimension(18, 24));
      this.dropDownButton.setMinimumSize(new Dimension(18, 10));
      this.dropDownButton.setPreferredSize(new Dimension(18, 10));
      this.dropDownButton.setFocusPainted(false);
      this.add(this.dropDownButton, "East");
      this.setEnabled(false);
   }

   public ScrollablePopupMenu getPopupMenu() {
      if (this.popupMenu == null) {
         this.popupMenu = new ScrollablePopupMenu(this);
         this.popupMenu.setEnabled(false);
         this.popupMenu.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent var1) {
               DropDownComponent.this.setEnabled((Boolean)var1.getNewValue());
            }
         });
         this.popupMenu.addListener(new ScrollablePopupMenuAdapter() {
            public void itemsWereAdded(ScrollablePopupMenuEvent var1) {
               DropDownComponent.this.updateMainButtonTooltip(var1.getDetails());
            }

            public void itemsWereRemoved(ScrollablePopupMenuEvent var1) {
               DropDownComponent.this.updateMainButtonTooltip(var1.getDetails());
            }
         });
      }

      return this.popupMenu;
   }

   public void setEnabled(boolean var1) {
      this.isDropDownEnabled = var1;
      this.mainButton.setEnabled(var1);
      this.dropDownButton.setEnabled(var1);
      this.dropDownButton.setIcon(var1 ? this.enabledDownArrow : this.disabledDownArrow);
   }

   public boolean isEnabled() {
      return this.isDropDownEnabled;
   }

   public void updateMainButtonTooltip(String var1) {
      this.mainButton.setToolTipText(var1);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class ScrollablePopupMenuAdapter implements ScrollablePopupMenuListener {
      public void itemsWereAdded(ScrollablePopupMenuEvent var1) {
      }

      public void itemsWereRemoved(ScrollablePopupMenuEvent var1) {
      }
   }

   public interface ScrollablePopupMenuListener extends EventListener {
      void itemsWereAdded(ScrollablePopupMenuEvent var1);

      void itemsWereRemoved(ScrollablePopupMenuEvent var1);
   }

   public static class ScrollablePopupMenuEvent extends EventObject {
      public static final int ITEMS_ADDED = 1;
      public static final int ITEMS_REMOVED = 2;
      private int type;
      private int itemNumber;
      private String details;

      public ScrollablePopupMenuEvent(Object var1, int var2, int var3, String var4) {
         super(var1);
         this.initEvent(var2, var3, var4);
      }

      public void initEvent(int var1, int var2, String var3) {
         this.type = var1;
         this.itemNumber = var2;
         this.details = var3;
      }

      public String getDetails() {
         return this.details;
      }

      public int getItemNumber() {
         return this.itemNumber;
      }

      public int getType() {
         return this.type;
      }
   }

   public static class ScrollablePopupMenu extends JPopupMenu {
      private static final String RESOURCES = "org.apache.batik.util.gui.resources.ScrollablePopupMenuMessages";
      private static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.ScrollablePopupMenuMessages", Locale.getDefault());
      private static ResourceManager resources;
      private JPanel menuPanel = new JPanel();
      private JScrollPane scrollPane;
      private int preferredHeight;
      private ScrollablePopupMenuModel model;
      private JComponent ownerComponent;
      private ScrollablePopupMenuItem footer;
      private EventListenerList eventListeners;

      public ScrollablePopupMenu(JComponent var1) {
         this.preferredHeight = resources.getInteger("PreferredHeight");
         this.eventListeners = new EventListenerList();
         this.setLayout(new BorderLayout());
         this.menuPanel.setLayout(new GridLayout(0, 1));
         this.ownerComponent = var1;
         this.init();
      }

      private void init() {
         super.removeAll();
         this.scrollPane = new JScrollPane();
         this.scrollPane.setViewportView(this.menuPanel);
         this.scrollPane.setBorder((Border)null);
         int var1 = resources.getInteger("ScrollPane.minWidth");
         int var2 = resources.getInteger("ScrollPane.minHeight");
         int var3 = resources.getInteger("ScrollPane.maxWidth");
         int var4 = resources.getInteger("ScrollPane.maxHeight");
         this.scrollPane.setMinimumSize(new Dimension(var1, var2));
         this.scrollPane.setMaximumSize(new Dimension(var3, var4));
         this.scrollPane.setHorizontalScrollBarPolicy(31);
         this.add(this.scrollPane, "Center");
         this.addFooter(new DefaultScrollablePopupMenuItem(this, ""));
      }

      public void showMenu(Component var1, Component var2) {
         this.model.processBeforeShowed();
         Point var3 = new Point(0, var2.getHeight());
         SwingUtilities.convertPointToScreen(var3, var2);
         this.setLocation(var3);
         this.setInvoker(var1);
         this.setVisible(true);
         this.revalidate();
         this.repaint();
         this.model.processAfterShowed();
      }

      public void add(ScrollablePopupMenuItem var1, int var2, int var3, int var4) {
         this.menuPanel.add((Component)var1, var2);
         if (var3 == 0) {
            this.setEnabled(true);
         }

      }

      public void remove(ScrollablePopupMenuItem var1, int var2, int var3) {
         this.menuPanel.remove((Component)var1);
         if (var3 == 0) {
            this.setEnabled(false);
         }

      }

      private int getPreferredWidth() {
         Component[] var1 = this.menuPanel.getComponents();
         int var2 = 0;

         int var3;
         for(var3 = 0; var3 < var1.length; ++var3) {
            int var4 = var1[var3].getPreferredSize().width;
            if (var2 < var4) {
               var2 = var4;
            }
         }

         var3 = ((Component)this.footer).getPreferredSize().width;
         if (var3 > var2) {
            var2 = var3;
         }

         byte var5 = 30;
         return var2 + var5;
      }

      private int getPreferredHeight() {
         if (this.scrollPane.getPreferredSize().height < this.preferredHeight) {
            byte var1 = 10;
            return this.scrollPane.getPreferredSize().height + ((Component)this.footer).getPreferredSize().height + var1;
         } else {
            return this.preferredHeight + ((Component)this.footer).getPreferredSize().height;
         }
      }

      public Dimension getPreferredSize() {
         return new Dimension(this.getPreferredWidth(), this.getPreferredHeight());
      }

      public void selectionChanged(ScrollablePopupMenuItem var1, boolean var2) {
         Component[] var3 = this.menuPanel.getComponents();
         int var4 = var3.length;
         int var5;
         ScrollablePopupMenuItem var6;
         if (!var2) {
            for(var5 = var4 - 1; var5 >= 0; --var5) {
               var6 = (ScrollablePopupMenuItem)var3[var5];
               var6.setSelected(var2);
            }
         } else {
            for(var5 = 0; var5 < var4; ++var5) {
               var6 = (ScrollablePopupMenuItem)var3[var5];
               if (var6 == var1) {
                  break;
               }

               var6.setSelected(true);
            }
         }

         this.footer.setText(this.model.getFooterText() + this.getSelectedItemsCount());
         this.repaint();
      }

      public void setModel(ScrollablePopupMenuModel var1) {
         this.model = var1;
         this.footer.setText(var1.getFooterText());
      }

      public ScrollablePopupMenuModel getModel() {
         return this.model;
      }

      public int getSelectedItemsCount() {
         int var1 = 0;
         Component[] var2 = this.menuPanel.getComponents();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            ScrollablePopupMenuItem var4 = (ScrollablePopupMenuItem)var2[var3];
            if (var4.isSelected()) {
               ++var1;
            }
         }

         return var1;
      }

      public void processItemClicked() {
         this.footer.setText(this.model.getFooterText() + 0);
         this.setVisible(false);
         this.model.processItemClicked();
      }

      public JComponent getOwner() {
         return this.ownerComponent;
      }

      private void addFooter(ScrollablePopupMenuItem var1) {
         this.footer = var1;
         this.footer.setEnabled(false);
         this.add((Component)this.footer, "South");
      }

      public ScrollablePopupMenuItem getFooter() {
         return this.footer;
      }

      public void addListener(ScrollablePopupMenuListener var1) {
         this.eventListeners.add(DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener == null ? (DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener = DropDownComponent.class$("org.apache.batik.util.gui.DropDownComponent$ScrollablePopupMenuListener")) : DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener, var1);
      }

      public void fireItemsWereAdded(ScrollablePopupMenuEvent var1) {
         Object[] var2 = this.eventListeners.getListenerList();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; var4 += 2) {
            if (var2[var4] == (DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener == null ? (DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener = DropDownComponent.class$("org.apache.batik.util.gui.DropDownComponent$ScrollablePopupMenuListener")) : DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener)) {
               ((ScrollablePopupMenuListener)var2[var4 + 1]).itemsWereAdded(var1);
            }
         }

      }

      public void fireItemsWereRemoved(ScrollablePopupMenuEvent var1) {
         Object[] var2 = this.eventListeners.getListenerList();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; var4 += 2) {
            if (var2[var4] == (DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener == null ? (DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener = DropDownComponent.class$("org.apache.batik.util.gui.DropDownComponent$ScrollablePopupMenuListener")) : DropDownComponent.class$org$apache$batik$util$gui$DropDownComponent$ScrollablePopupMenuListener)) {
               ((ScrollablePopupMenuListener)var2[var4 + 1]).itemsWereRemoved(var1);
            }
         }

      }

      static {
         resources = new ResourceManager(bundle);
      }
   }

   public interface ScrollablePopupMenuModel {
      String getFooterText();

      void processItemClicked();

      void processBeforeShowed();

      void processAfterShowed();
   }

   public static class DefaultScrollablePopupMenuItem extends JButton implements ScrollablePopupMenuItem {
      public static final Color MENU_HIGHLIGHT_BG_COLOR = UIManager.getColor("MenuItem.selectionBackground");
      public static final Color MENU_HIGHLIGHT_FG_COLOR = UIManager.getColor("MenuItem.selectionForeground");
      public static final Color MENUITEM_BG_COLOR = UIManager.getColor("MenuItem.background");
      public static final Color MENUITEM_FG_COLOR = UIManager.getColor("MenuItem.foreground");
      private ScrollablePopupMenu parent;

      public DefaultScrollablePopupMenuItem(ScrollablePopupMenu var1, String var2) {
         super(var2);
         this.parent = var1;
         this.init();
      }

      private void init() {
         this.setUI(BasicButtonUI.createUI(this));
         this.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 20));
         this.setMenuItemDefaultColors();
         this.setAlignmentX(0.0F);
         this.setSelected(false);
         this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent var1) {
               if (DefaultScrollablePopupMenuItem.this.isEnabled()) {
                  DefaultScrollablePopupMenuItem.this.setSelected(true);
                  DefaultScrollablePopupMenuItem.this.parent.selectionChanged(DefaultScrollablePopupMenuItem.this, true);
               }

            }

            public void mouseExited(MouseEvent var1) {
               if (DefaultScrollablePopupMenuItem.this.isEnabled()) {
                  DefaultScrollablePopupMenuItem.this.setSelected(false);
                  DefaultScrollablePopupMenuItem.this.parent.selectionChanged(DefaultScrollablePopupMenuItem.this, false);
               }

            }

            public void mouseClicked(MouseEvent var1) {
               DefaultScrollablePopupMenuItem.this.parent.processItemClicked();
            }
         });
      }

      private void setMenuItemDefaultColors() {
         this.setBackground(MENUITEM_BG_COLOR);
         this.setForeground(MENUITEM_FG_COLOR);
      }

      public void setSelected(boolean var1) {
         super.setSelected(var1);
         if (var1) {
            this.setBackground(MENU_HIGHLIGHT_BG_COLOR);
            this.setForeground(MENU_HIGHLIGHT_FG_COLOR);
         } else {
            this.setMenuItemDefaultColors();
         }

      }

      public String getText() {
         return super.getText();
      }

      public void setText(String var1) {
         super.setText(var1);
      }

      public void setEnabled(boolean var1) {
         super.setEnabled(var1);
      }
   }

   public interface ScrollablePopupMenuItem {
      void setSelected(boolean var1);

      boolean isSelected();

      String getText();

      void setText(String var1);

      void setEnabled(boolean var1);
   }

   private static class SmallDisabledDownArrow extends SmallDownArrow {
      public SmallDisabledDownArrow() {
         super(null);
         this.arrowColor = new Color(140, 140, 140);
      }

      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         super.paintIcon(var1, var2, var3, var4);
         var2.setColor(Color.white);
         var2.drawLine(var3 + 3, var4 + 2, var3 + 4, var4 + 1);
         var2.drawLine(var3 + 3, var4 + 3, var3 + 5, var4 + 1);
      }
   }

   private static class SmallDownArrow implements Icon {
      protected Color arrowColor;

      private SmallDownArrow() {
         this.arrowColor = Color.black;
      }

      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         var2.setColor(this.arrowColor);
         var2.drawLine(var3, var4, var3 + 4, var4);
         var2.drawLine(var3 + 1, var4 + 1, var3 + 3, var4 + 1);
         var2.drawLine(var3 + 2, var4 + 2, var3 + 2, var4 + 2);
      }

      public int getIconWidth() {
         return 6;
      }

      public int getIconHeight() {
         return 4;
      }

      // $FF: synthetic method
      SmallDownArrow(Object var1) {
         this();
      }
   }

   private class DropDownListener extends MouseAdapter {
      private DropDownListener() {
      }

      public void mousePressed(MouseEvent var1) {
         if (DropDownComponent.this.popupMenu.isShowing() && DropDownComponent.this.isDropDownEnabled) {
            DropDownComponent.this.popupMenu.setVisible(false);
         } else if (DropDownComponent.this.isDropDownEnabled) {
            DropDownComponent.this.popupMenu.showMenu((Component)var1.getSource(), DropDownComponent.this);
         }

      }

      public void mouseEntered(MouseEvent var1) {
         DropDownComponent.this.dropDownButton.setBorderPainted(true);
      }

      public void mouseExited(MouseEvent var1) {
         DropDownComponent.this.dropDownButton.setBorderPainted(false);
      }

      // $FF: synthetic method
      DropDownListener(Object var2) {
         this();
      }
   }
}
