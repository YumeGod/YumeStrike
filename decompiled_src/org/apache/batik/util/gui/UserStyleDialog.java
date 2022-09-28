package org.apache.batik.util.gui;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;

public class UserStyleDialog extends JDialog implements ActionMap {
   public static final int OK_OPTION = 0;
   public static final int CANCEL_OPTION = 1;
   protected static final String RESOURCES = "org.apache.batik.util.gui.resources.UserStyleDialog";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.UserStyleDialog", Locale.getDefault());
   protected static ResourceManager resources;
   protected Panel panel;
   protected String chosenPath;
   protected int returnCode;
   protected Map listeners = new HashMap();

   public UserStyleDialog(JFrame var1) {
      super(var1);
      this.setModal(true);
      this.setTitle(resources.getString("Dialog.title"));
      this.listeners.put("OKButtonAction", new OKButtonAction());
      this.listeners.put("CancelButtonAction", new CancelButtonAction());
      this.getContentPane().add(this.panel = new Panel());
      this.getContentPane().add(this.createButtonsPanel(), "South");
      this.pack();
   }

   public int showDialog() {
      this.pack();
      this.setVisible(true);
      return this.returnCode;
   }

   public String getPath() {
      return this.chosenPath;
   }

   public void setPath(String var1) {
      this.chosenPath = var1;
      this.panel.fileTextField.setText(var1);
      this.panel.fileCheckBox.setSelected(true);
   }

   protected JPanel createButtonsPanel() {
      JPanel var1 = new JPanel(new FlowLayout(2));
      ButtonFactory var2 = new ButtonFactory(bundle, this);
      var1.add(var2.createJButton("OKButton"));
      var1.add(var2.createJButton("CancelButton"));
      return var1;
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   static {
      resources = new ResourceManager(bundle);
   }

   public static class Panel extends JPanel {
      protected JCheckBox fileCheckBox;
      protected JLabel fileLabel;
      protected JTextField fileTextField;
      protected JButton browseButton;

      public Panel() {
         super(new GridBagLayout());
         this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), UserStyleDialog.resources.getString("Panel.title")));
         ExtendedGridBagConstraints var1 = new ExtendedGridBagConstraints();
         var1.insets = new Insets(5, 5, 5, 5);
         this.fileCheckBox = new JCheckBox(UserStyleDialog.resources.getString("PanelFileCheckBox.text"));
         this.fileCheckBox.addChangeListener(new FileCheckBoxChangeListener());
         var1.weightx = 0.0;
         var1.weighty = 0.0;
         var1.fill = 2;
         var1.setGridBounds(0, 2, 3, 1);
         this.add(this.fileCheckBox, var1);
         this.fileLabel = new JLabel(UserStyleDialog.resources.getString("PanelFileLabel.text"));
         var1.weightx = 0.0;
         var1.weighty = 0.0;
         var1.fill = 2;
         var1.setGridBounds(0, 3, 3, 1);
         this.add(this.fileLabel, var1);
         this.fileTextField = new JTextField(30);
         var1.weightx = 1.0;
         var1.weighty = 0.0;
         var1.fill = 2;
         var1.setGridBounds(0, 4, 2, 1);
         this.add(this.fileTextField, var1);
         ButtonFactory var2 = new ButtonFactory(UserStyleDialog.bundle, (ActionMap)null);
         var1.weightx = 0.0;
         var1.weighty = 0.0;
         var1.fill = 0;
         var1.anchor = 13;
         var1.setGridBounds(2, 4, 1, 1);
         this.browseButton = var2.createJButton("PanelFileBrowseButton");
         this.add(this.browseButton, var1);
         this.browseButton.addActionListener(new FileBrowseButtonAction());
         this.fileLabel.setEnabled(false);
         this.fileTextField.setEnabled(false);
         this.browseButton.setEnabled(false);
      }

      public String getPath() {
         return this.fileCheckBox.isSelected() ? this.fileTextField.getText() : null;
      }

      public void setPath(String var1) {
         if (var1 == null) {
            this.fileTextField.setEnabled(false);
            this.fileCheckBox.setSelected(false);
         } else {
            this.fileTextField.setEnabled(true);
            this.fileTextField.setText(var1);
            this.fileCheckBox.setSelected(true);
         }

      }

      protected class FileBrowseButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            JFileChooser var2 = new JFileChooser(new File("."));
            var2.setFileHidingEnabled(false);
            int var3 = var2.showOpenDialog(Panel.this);
            if (var3 == 0) {
               File var4 = var2.getSelectedFile();

               try {
                  Panel.this.fileTextField.setText(var4.getCanonicalPath());
               } catch (IOException var6) {
               }
            }

         }
      }

      protected class FileCheckBoxChangeListener implements ChangeListener {
         public void stateChanged(ChangeEvent var1) {
            boolean var2 = Panel.this.fileCheckBox.isSelected();
            Panel.this.fileLabel.setEnabled(var2);
            Panel.this.fileTextField.setEnabled(var2);
            Panel.this.browseButton.setEnabled(var2);
         }
      }
   }

   protected class CancelButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         UserStyleDialog.this.returnCode = 1;
         UserStyleDialog.this.dispose();
      }
   }

   protected class OKButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (UserStyleDialog.this.panel.fileCheckBox.isSelected()) {
            String var2 = UserStyleDialog.this.panel.fileTextField.getText();
            if (var2.equals("")) {
               JOptionPane.showMessageDialog(UserStyleDialog.this, UserStyleDialog.resources.getString("StyleDialogError.text"), UserStyleDialog.resources.getString("StyleDialogError.title"), 0);
               return;
            }

            File var3 = new File(var2);
            if (var3.exists()) {
               if (var3.isDirectory()) {
                  var2 = null;
               } else {
                  var2 = "file:" + var2;
               }
            }

            UserStyleDialog.this.chosenPath = var2;
         } else {
            UserStyleDialog.this.chosenPath = null;
         }

         UserStyleDialog.this.returnCode = 0;
         UserStyleDialog.this.dispose();
      }
   }
}
