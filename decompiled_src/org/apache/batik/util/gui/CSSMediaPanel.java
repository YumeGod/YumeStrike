package org.apache.batik.util.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;

public class CSSMediaPanel extends JPanel implements ActionMap {
   protected static final String RESOURCES = "org.apache.batik.util.gui.resources.CSSMediaPanel";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.CSSMediaPanel", Locale.getDefault());
   protected static ResourceManager resources;
   protected JButton removeButton;
   protected JButton addButton;
   protected JButton clearButton;
   protected DefaultListModel listModel = new DefaultListModel();
   protected JList mediaList;
   protected Map listeners = new HashMap();

   public CSSMediaPanel() {
      super(new GridBagLayout());
      this.listeners.put("AddButtonAction", new AddButtonAction());
      this.listeners.put("RemoveButtonAction", new RemoveButtonAction());
      this.listeners.put("ClearButtonAction", new ClearButtonAction());
      this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), resources.getString("Panel.title")));
      ExtendedGridBagConstraints var1 = new ExtendedGridBagConstraints();
      var1.insets = new Insets(5, 5, 5, 5);
      this.mediaList = new JList();
      this.mediaList.setSelectionMode(0);
      this.mediaList.setModel(this.listModel);
      this.mediaList.addListSelectionListener(new MediaListSelectionListener());
      this.listModel.addListDataListener(new MediaListDataListener());
      JScrollPane var2 = new JScrollPane();
      var2.setBorder(BorderFactory.createLoweredBevelBorder());
      var1.weightx = 1.0;
      var1.weighty = 1.0;
      var1.fill = 1;
      var1.setGridBounds(0, 0, 1, 3);
      var2.getViewport().add(this.mediaList);
      this.add(var2, var1);
      ButtonFactory var3 = new ButtonFactory(bundle, this);
      var1.weightx = 0.0;
      var1.weighty = 0.0;
      var1.fill = 2;
      var1.anchor = 11;
      this.addButton = var3.createJButton("AddButton");
      var1.setGridBounds(1, 0, 1, 1);
      this.add(this.addButton, var1);
      this.removeButton = var3.createJButton("RemoveButton");
      var1.setGridBounds(1, 1, 1, 1);
      this.add(this.removeButton, var1);
      this.clearButton = var3.createJButton("ClearButton");
      var1.setGridBounds(1, 2, 1, 1);
      this.add(this.clearButton, var1);
      this.updateButtons();
   }

   protected void updateButtons() {
      this.removeButton.setEnabled(!this.mediaList.isSelectionEmpty());
      this.clearButton.setEnabled(!this.listModel.isEmpty());
   }

   public void setMedia(List var1) {
      this.listModel.removeAllElements();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.listModel.addElement(var2.next());
      }

   }

   public void setMedia(String var1) {
      this.listModel.removeAllElements();
      StringTokenizer var2 = new StringTokenizer(var1, " ");

      while(var2.hasMoreTokens()) {
         this.listModel.addElement(var2.nextToken());
      }

   }

   public List getMedia() {
      ArrayList var1 = new ArrayList(this.listModel.size());
      Enumeration var2 = this.listModel.elements();

      while(var2.hasMoreElements()) {
         var1.add(var2.nextElement());
      }

      return var1;
   }

   public String getMediaAsString() {
      StringBuffer var1 = new StringBuffer();
      Enumeration var2 = this.listModel.elements();

      while(var2.hasMoreElements()) {
         var1.append((String)var2.nextElement());
         var1.append(' ');
      }

      return var1.toString();
   }

   public static int showDialog(Component var0, String var1) {
      return showDialog(var0, var1, "");
   }

   public static int showDialog(Component var0, String var1, List var2) {
      Dialog var3 = new Dialog(var0, var1, var2);
      var3.setModal(true);
      var3.pack();
      var3.setVisible(true);
      return var3.getReturnCode();
   }

   public static int showDialog(Component var0, String var1, String var2) {
      Dialog var3 = new Dialog(var0, var1, var2);
      var3.setModal(true);
      var3.pack();
      var3.setVisible(true);
      return var3.getReturnCode();
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   public static void main(String[] var0) {
      String var1 = "all aural braille embossed handheld print projection screen tty tv";
      int var2 = showDialog((Component)null, "Test", (String)var1);
      System.out.println(var2);
      System.exit(0);
   }

   static {
      resources = new ResourceManager(bundle);
   }

   public static class Dialog extends JDialog implements ActionMap {
      public static final int OK_OPTION = 0;
      public static final int CANCEL_OPTION = 1;
      protected int returnCode;
      protected Map listeners;

      public Dialog() {
         this((Component)null, "", (String)"");
      }

      public Dialog(Component var1, String var2, List var3) {
         super(JOptionPane.getFrameForComponent(var1), var2);
         this.listeners = new HashMap();
         this.listeners.put("OKButtonAction", new OKButtonAction());
         this.listeners.put("CancelButtonAction", new CancelButtonAction());
         CSSMediaPanel var4 = new CSSMediaPanel();
         var4.setMedia(var3);
         this.getContentPane().add(var4, "Center");
         this.getContentPane().add(this.createButtonsPanel(), "South");
      }

      public Dialog(Component var1, String var2, String var3) {
         super(JOptionPane.getFrameForComponent(var1), var2);
         this.listeners = new HashMap();
         this.listeners.put("OKButtonAction", new OKButtonAction());
         this.listeners.put("CancelButtonAction", new CancelButtonAction());
         CSSMediaPanel var4 = new CSSMediaPanel();
         var4.setMedia(var3);
         this.getContentPane().add(var4, "Center");
         this.getContentPane().add(this.createButtonsPanel(), "South");
      }

      public int getReturnCode() {
         return this.returnCode;
      }

      protected JPanel createButtonsPanel() {
         JPanel var1 = new JPanel(new FlowLayout(2));
         ButtonFactory var2 = new ButtonFactory(CSSMediaPanel.bundle, this);
         var1.add(var2.createJButton("OKButton"));
         var1.add(var2.createJButton("CancelButton"));
         return var1;
      }

      public Action getAction(String var1) throws MissingListenerException {
         return (Action)this.listeners.get(var1);
      }

      protected class CancelButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            Dialog.this.returnCode = 1;
            Dialog.this.dispose();
         }
      }

      protected class OKButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            Dialog.this.returnCode = 0;
            Dialog.this.dispose();
         }
      }
   }

   public static class AddMediumDialog extends JDialog implements ActionMap {
      public static final int OK_OPTION = 0;
      public static final int CANCEL_OPTION = 1;
      protected JComboBox medium;
      protected int returnCode;
      protected Map listeners = new HashMap();

      public AddMediumDialog(Component var1) {
         super(JOptionPane.getFrameForComponent(var1), CSSMediaPanel.resources.getString("AddMediumDialog.title"));
         this.setModal(true);
         this.listeners.put("OKButtonAction", new OKButtonAction());
         this.listeners.put("CancelButtonAction", new CancelButtonAction());
         this.getContentPane().add(this.createContentPanel(), "Center");
         this.getContentPane().add(this.createButtonsPanel(), "South");
      }

      public String getMedium() {
         return (String)this.medium.getSelectedItem();
      }

      protected Component createContentPanel() {
         JPanel var1 = new JPanel(new BorderLayout());
         var1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
         var1.add(new JLabel(CSSMediaPanel.resources.getString("AddMediumDialog.label")), "West");
         this.medium = new JComboBox();
         this.medium.setEditable(true);
         String var2 = CSSMediaPanel.resources.getString("Media.list");
         StringTokenizer var3 = new StringTokenizer(var2, " ");

         while(var3.hasMoreTokens()) {
            this.medium.addItem(var3.nextToken());
         }

         var1.add(this.medium, "Center");
         return var1;
      }

      protected Component createButtonsPanel() {
         JPanel var1 = new JPanel(new FlowLayout(2));
         ButtonFactory var2 = new ButtonFactory(CSSMediaPanel.bundle, this);
         var1.add(var2.createJButton("OKButton"));
         var1.add(var2.createJButton("CancelButton"));
         return var1;
      }

      public int getReturnCode() {
         return this.returnCode;
      }

      public Action getAction(String var1) throws MissingListenerException {
         return (Action)this.listeners.get(var1);
      }

      protected class CancelButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            AddMediumDialog.this.returnCode = 1;
            AddMediumDialog.this.dispose();
         }
      }

      protected class OKButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            AddMediumDialog.this.returnCode = 0;
            AddMediumDialog.this.dispose();
         }
      }
   }

   protected class MediaListDataListener implements ListDataListener {
      public void contentsChanged(ListDataEvent var1) {
         CSSMediaPanel.this.updateButtons();
      }

      public void intervalAdded(ListDataEvent var1) {
         CSSMediaPanel.this.updateButtons();
      }

      public void intervalRemoved(ListDataEvent var1) {
         CSSMediaPanel.this.updateButtons();
      }
   }

   protected class MediaListSelectionListener implements ListSelectionListener {
      public void valueChanged(ListSelectionEvent var1) {
         CSSMediaPanel.this.updateButtons();
      }
   }

   protected class ClearButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         CSSMediaPanel.this.mediaList.clearSelection();
         CSSMediaPanel.this.listModel.removeAllElements();
      }
   }

   protected class RemoveButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         int var2 = CSSMediaPanel.this.mediaList.getSelectedIndex();
         CSSMediaPanel.this.mediaList.clearSelection();
         if (var2 >= 0) {
            CSSMediaPanel.this.listModel.removeElementAt(var2);
         }

      }
   }

   protected class AddButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         AddMediumDialog var2 = new AddMediumDialog(CSSMediaPanel.this);
         var2.pack();
         var2.setVisible(true);
         if (var2.getReturnCode() != 1 && var2.getMedium() != null) {
            String var3 = var2.getMedium().trim();
            if (var3.length() != 0 && !CSSMediaPanel.this.listModel.contains(var3)) {
               for(int var4 = 0; var4 < CSSMediaPanel.this.listModel.size() && var3 != null; ++var4) {
                  String var5 = (String)CSSMediaPanel.this.listModel.getElementAt(var4);
                  int var6 = var3.compareTo(var5);
                  if (var6 == 0) {
                     var3 = null;
                  } else if (var6 < 0) {
                     CSSMediaPanel.this.listModel.insertElementAt(var3, var4);
                     var3 = null;
                  }
               }

               if (var3 != null) {
                  CSSMediaPanel.this.listModel.addElement(var3);
               }

            }
         }
      }
   }
}
