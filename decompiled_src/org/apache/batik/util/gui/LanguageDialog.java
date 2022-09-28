package org.apache.batik.util.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;

public class LanguageDialog extends JDialog implements ActionMap {
   public static final int OK_OPTION = 0;
   public static final int CANCEL_OPTION = 1;
   protected static final String RESOURCES = "org.apache.batik.util.gui.resources.LanguageDialogMessages";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.LanguageDialogMessages", Locale.getDefault());
   protected static ResourceManager resources;
   protected Map listeners = new HashMap();
   protected Panel panel = new Panel();
   protected int returnCode;
   // $FF: synthetic field
   static Class class$org$apache$batik$util$gui$LanguageDialog$Panel;

   public LanguageDialog(JFrame var1) {
      super(var1);
      this.setModal(true);
      this.setTitle(resources.getString("Dialog.title"));
      this.listeners.put("OKButtonAction", new OKButtonAction());
      this.listeners.put("CancelButtonAction", new CancelButtonAction());
      this.getContentPane().add(this.panel);
      this.getContentPane().add(this.createButtonsPanel(), "South");
      this.pack();
   }

   public int showDialog() {
      this.setVisible(true);
      return this.returnCode;
   }

   public void setLanguages(String var1) {
      this.panel.setLanguages(var1);
   }

   public String getLanguages() {
      return this.panel.getLanguages();
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   protected JPanel createButtonsPanel() {
      JPanel var1 = new JPanel(new FlowLayout(2));
      ButtonFactory var2 = new ButtonFactory(bundle, this);
      var1.add(var2.createJButton("OKButton"));
      var1.add(var2.createJButton("CancelButton"));
      return var1;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      resources = new ResourceManager(bundle);
   }

   protected class CancelButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         LanguageDialog.this.returnCode = 1;
         LanguageDialog.this.dispose();
      }
   }

   protected class OKButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         LanguageDialog.this.returnCode = 0;
         LanguageDialog.this.dispose();
      }
   }

   public static class Panel extends JPanel implements ActionMap {
      protected JList userList;
      protected JList languageList;
      protected DefaultListModel userListModel = new DefaultListModel();
      protected DefaultListModel languageListModel = new DefaultListModel();
      protected JButton addLanguageButton;
      protected JButton removeLanguageButton;
      protected JButton upLanguageButton;
      protected JButton downLanguageButton;
      protected JButton clearLanguageButton;
      protected Map listeners = new HashMap();
      private static Map iconMap = null;

      public Panel() {
         super(new GridBagLayout());
         initCountryIcons();
         this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LanguageDialog.resources.getString("Panel.title")));
         this.listeners.put("AddLanguageButtonAction", new AddLanguageButtonAction());
         this.listeners.put("RemoveLanguageButtonAction", new RemoveLanguageButtonAction());
         this.listeners.put("UpLanguageButtonAction", new UpLanguageButtonAction());
         this.listeners.put("DownLanguageButtonAction", new DownLanguageButtonAction());
         this.listeners.put("ClearLanguageButtonAction", new ClearLanguageButtonAction());
         this.userList = new JList(this.userListModel);
         this.userList.setCellRenderer(new IconAndTextCellRenderer());
         this.languageList = new JList(this.languageListModel);
         this.languageList.setCellRenderer(new IconAndTextCellRenderer());
         StringTokenizer var1 = new StringTokenizer(LanguageDialog.resources.getString("Country.list"), " ");

         while(var1.hasMoreTokens()) {
            this.languageListModel.addElement(var1.nextToken());
         }

         ExtendedGridBagConstraints var2 = new ExtendedGridBagConstraints();
         var2.insets = new Insets(5, 5, 5, 5);
         var2.weightx = 1.0;
         var2.weighty = 1.0;
         var2.fill = 1;
         var2.setGridBounds(0, 0, 1, 1);
         JScrollPane var3 = new JScrollPane();
         var3.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), LanguageDialog.resources.getString("Languages.title")), BorderFactory.createLoweredBevelBorder()));
         var3.getViewport().add(this.languageList);
         this.add(var3, var2);
         this.languageList.setSelectionMode(0);
         this.languageList.addListSelectionListener(new LanguageListSelectionListener());
         var2.setGridBounds(2, 0, 1, 1);
         JScrollPane var4 = new JScrollPane();
         var4.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), LanguageDialog.resources.getString("User.title")), BorderFactory.createLoweredBevelBorder()));
         var4.getViewport().add(this.userList);
         this.add(var4, var2);
         this.userList.setSelectionMode(0);
         this.userList.addListSelectionListener(new UserListSelectionListener());
         var2.setGridBounds(0, 1, 3, 1);
         var2.weightx = 0.0;
         var2.weighty = 0.0;
         this.add(new JLabel(LanguageDialog.resources.getString("InfoLabel.text")), var2);
         ButtonFactory var5 = new ButtonFactory(LanguageDialog.bundle, this);
         JPanel var6 = new JPanel(new GridLayout(5, 1, 0, 3));
         var6.add(this.addLanguageButton = var5.createJButton("AddLanguageButton"));
         this.addLanguageButton.setEnabled(false);
         var6.add(this.removeLanguageButton = var5.createJButton("RemoveLanguageButton"));
         this.removeLanguageButton.setEnabled(false);
         var6.add(this.upLanguageButton = var5.createJButton("UpLanguageButton"));
         this.upLanguageButton.setEnabled(false);
         var6.add(this.downLanguageButton = var5.createJButton("DownLanguageButton"));
         this.downLanguageButton.setEnabled(false);
         var6.add(this.clearLanguageButton = var5.createJButton("ClearLanguageButton"));
         this.clearLanguageButton.setEnabled(false);
         JPanel var7 = new JPanel(new GridBagLayout());
         var2.setGridBounds(1, 0, 1, 1);
         this.add(var7, var2);
         var2.fill = 2;
         var2.setGridBounds(0, 0, 1, 1);
         var2.insets = new Insets(0, 0, 0, 0);
         var7.add(var6, var2);
         var4.setPreferredSize(var3.getPreferredSize());
      }

      public static synchronized void initCountryIcons() {
         if (iconMap == null) {
            iconMap = new HashMap();
            StringTokenizer var0 = new StringTokenizer(LanguageDialog.resources.getString("Country.list"), " ");

            while(var0.hasMoreTokens()) {
               computeCountryIcon(LanguageDialog.class$org$apache$batik$util$gui$LanguageDialog$Panel == null ? (LanguageDialog.class$org$apache$batik$util$gui$LanguageDialog$Panel = LanguageDialog.class$("org.apache.batik.util.gui.LanguageDialog$Panel")) : LanguageDialog.class$org$apache$batik$util$gui$LanguageDialog$Panel, var0.nextToken());
            }
         }

      }

      public String getLanguages() {
         StringBuffer var1 = new StringBuffer();
         if (this.userListModel.getSize() > 0) {
            var1.append(this.userListModel.getElementAt(0));

            for(int var2 = 1; var2 < this.userListModel.getSize(); ++var2) {
               var1.append(',');
               var1.append(this.userListModel.getElementAt(var2));
            }
         }

         return var1.toString();
      }

      public void setLanguages(String var1) {
         int var2 = this.userListModel.getSize();

         for(int var3 = 0; var3 < var2; ++var3) {
            Object var4 = this.userListModel.getElementAt(0);
            this.userListModel.removeElementAt(0);
            String var5 = (String)var4;
            int var6 = this.languageListModel.getSize();

            int var7;
            for(var7 = 0; var7 < var6; ++var7) {
               String var8 = (String)this.languageListModel.getElementAt(var7);
               if (var5.compareTo(var8) > 0) {
                  break;
               }
            }

            this.languageListModel.insertElementAt(var4, var7);
         }

         StringTokenizer var9 = new StringTokenizer(var1, ",");

         while(var9.hasMoreTokens()) {
            String var10 = var9.nextToken();
            this.userListModel.addElement(var10);
            this.languageListModel.removeElement(var10);
         }

         this.updateButtons();
      }

      protected void updateButtons() {
         int var1 = this.userListModel.size();
         int var2 = this.userList.getSelectedIndex();
         boolean var3 = var1 == 0;
         boolean var4 = var2 != -1;
         boolean var5 = var2 == 0;
         boolean var6 = var2 == var1 - 1;
         this.removeLanguageButton.setEnabled(!var3 && var4);
         this.upLanguageButton.setEnabled(!var3 && var4 && !var5);
         this.downLanguageButton.setEnabled(!var3 && var4 && !var6);
         this.clearLanguageButton.setEnabled(!var3);
         var1 = this.languageListModel.size();
         var2 = this.languageList.getSelectedIndex();
         var3 = var1 == 0;
         var4 = var2 != -1;
         this.addLanguageButton.setEnabled(!var3 && var4);
      }

      protected String getCountryText(String var1) {
         return LanguageDialog.resources.getString(var1 + ".text");
      }

      protected Icon getCountryIcon(String var1) {
         return computeCountryIcon(this.getClass(), var1);
      }

      private static Icon computeCountryIcon(Class var0, String var1) {
         ImageIcon var2 = null;

         try {
            if ((var2 = (ImageIcon)iconMap.get(var1)) != null) {
               return var2;
            }

            String var3 = LanguageDialog.resources.getString(var1 + ".icon");
            URL var4 = var0.getResource(var3);
            if (var4 != null) {
               iconMap.put(var1, var2 = new ImageIcon(var4));
               return var2;
            }
         } catch (MissingResourceException var5) {
         }

         return new ImageIcon(var0.getResource("resources/blank.gif"));
      }

      public Action getAction(String var1) throws MissingListenerException {
         return (Action)this.listeners.get(var1);
      }

      protected class IconAndTextCellRenderer extends JLabel implements ListCellRenderer {
         public IconAndTextCellRenderer() {
            this.setOpaque(true);
            this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
         }

         public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
            String var6 = (String)var2;
            this.setText(Panel.this.getCountryText(var6));
            this.setIcon(Panel.this.getCountryIcon(var6));
            this.setEnabled(var1.isEnabled());
            this.setFont(var1.getFont());
            if (var4) {
               this.setBackground(var1.getSelectionBackground());
               this.setForeground(var1.getSelectionForeground());
            } else {
               this.setBackground(var1.getBackground());
               this.setForeground(var1.getForeground());
            }

            return this;
         }
      }

      protected class UserListSelectionListener implements ListSelectionListener {
         public void valueChanged(ListSelectionEvent var1) {
            int var2 = Panel.this.userList.getSelectedIndex();
            Panel.this.languageList.getSelectionModel().clearSelection();
            Panel.this.userList.setSelectedIndex(var2);
            Panel.this.updateButtons();
         }
      }

      protected class LanguageListSelectionListener implements ListSelectionListener {
         public void valueChanged(ListSelectionEvent var1) {
            int var2 = Panel.this.languageList.getSelectedIndex();
            Panel.this.userList.getSelectionModel().clearSelection();
            Panel.this.languageList.setSelectedIndex(var2);
            Panel.this.updateButtons();
         }
      }

      protected class ClearLanguageButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            int var2 = Panel.this.userListModel.getSize();

            for(int var3 = 0; var3 < var2; ++var3) {
               Object var4 = Panel.this.userListModel.getElementAt(0);
               Panel.this.userListModel.removeElementAt(0);
               String var5 = (String)var4;
               int var6 = Panel.this.languageListModel.getSize();

               int var7;
               for(var7 = 0; var7 < var6; ++var7) {
                  String var8 = (String)Panel.this.languageListModel.getElementAt(var7);
                  if (var5.compareTo(var8) > 0) {
                     break;
                  }
               }

               Panel.this.languageListModel.insertElementAt(var4, var7);
            }

            Panel.this.updateButtons();
         }
      }

      protected class DownLanguageButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            int var2 = Panel.this.userList.getSelectedIndex();
            Object var3 = Panel.this.userListModel.getElementAt(var2);
            Panel.this.userListModel.removeElementAt(var2);
            Panel.this.userListModel.insertElementAt(var3, var2 + 1);
            Panel.this.userList.setSelectedIndex(var2 + 1);
         }
      }

      protected class UpLanguageButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            int var2 = Panel.this.userList.getSelectedIndex();
            Object var3 = Panel.this.userListModel.getElementAt(var2);
            Panel.this.userListModel.removeElementAt(var2);
            Panel.this.userListModel.insertElementAt(var3, var2 - 1);
            Panel.this.userList.setSelectedIndex(var2 - 1);
         }
      }

      protected class RemoveLanguageButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            int var2 = Panel.this.userList.getSelectedIndex();
            Object var3 = Panel.this.userListModel.getElementAt(var2);
            Panel.this.userListModel.removeElementAt(var2);
            String var4 = (String)var3;
            int var5 = Panel.this.languageListModel.getSize();

            int var6;
            for(var6 = 0; var6 < var5; ++var6) {
               String var7 = (String)Panel.this.languageListModel.getElementAt(var6);
               if (var4.compareTo(var7) > 0) {
                  break;
               }
            }

            Panel.this.languageListModel.insertElementAt(var3, var6);
            Panel.this.languageList.setSelectedValue(var3, true);
            Panel.this.updateButtons();
         }
      }

      protected class AddLanguageButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            int var2 = Panel.this.languageList.getSelectedIndex();
            Object var3 = Panel.this.languageListModel.getElementAt(var2);
            Panel.this.languageListModel.removeElementAt(var2);
            Panel.this.userListModel.addElement(var3);
            Panel.this.userList.setSelectedValue(var3, true);
         }
      }
   }
}
