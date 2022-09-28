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
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;

public class URIChooser extends JDialog implements ActionMap {
   public static final int OK_OPTION = 0;
   public static final int CANCEL_OPTION = 1;
   protected static final String RESOURCES = "org.apache.batik.util.gui.resources.URIChooserMessages";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.util.gui.resources.URIChooserMessages", Locale.getDefault());
   protected static ResourceManager resources;
   protected ButtonFactory buttonFactory;
   protected JTextField textField;
   protected JButton okButton;
   protected JButton clearButton;
   protected String currentPath = ".";
   protected FileFilter fileFilter;
   protected int returnCode;
   protected String chosenPath;
   protected Map listeners = new HashMap(10);

   public URIChooser(JDialog var1) {
      super(var1);
      this.initialize();
   }

   public URIChooser(JFrame var1) {
      super(var1);
      this.initialize();
   }

   public int showDialog() {
      this.pack();
      this.setVisible(true);
      return this.returnCode;
   }

   public String getText() {
      return this.chosenPath;
   }

   public void setFileFilter(FileFilter var1) {
      this.fileFilter = var1;
   }

   protected void initialize() {
      this.setModal(true);
      this.listeners.put("BrowseButtonAction", new BrowseButtonAction());
      this.listeners.put("OKButtonAction", new OKButtonAction());
      this.listeners.put("CancelButtonAction", new CancelButtonAction());
      this.listeners.put("ClearButtonAction", new ClearButtonAction());
      this.setTitle(resources.getString("Dialog.title"));
      this.buttonFactory = new ButtonFactory(bundle, this);
      this.getContentPane().add(this.createURISelectionPanel(), "North");
      this.getContentPane().add(this.createButtonsPanel(), "South");
   }

   protected JPanel createURISelectionPanel() {
      JPanel var1 = new JPanel(new GridBagLayout());
      var1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      ExtendedGridBagConstraints var2 = new ExtendedGridBagConstraints();
      var2.insets = new Insets(5, 5, 5, 5);
      var2.weightx = 0.0;
      var2.weighty = 0.0;
      var2.fill = 2;
      var2.setGridBounds(0, 0, 2, 1);
      var1.add(new JLabel(resources.getString("Dialog.label")), var2);
      this.textField = new JTextField(30);
      this.textField.getDocument().addDocumentListener(new DocumentAdapter());
      var2.weightx = 1.0;
      var2.weighty = 0.0;
      var2.fill = 2;
      var2.setGridBounds(0, 1, 1, 1);
      var1.add(this.textField, var2);
      var2.weightx = 0.0;
      var2.weighty = 0.0;
      var2.fill = 0;
      var2.setGridBounds(1, 1, 1, 1);
      var1.add(this.buttonFactory.createJButton("BrowseButton"), var2);
      return var1;
   }

   protected JPanel createButtonsPanel() {
      JPanel var1 = new JPanel(new FlowLayout());
      var1.add(this.okButton = this.buttonFactory.createJButton("OKButton"));
      var1.add(this.buttonFactory.createJButton("CancelButton"));
      var1.add(this.clearButton = this.buttonFactory.createJButton("ClearButton"));
      this.okButton.setEnabled(false);
      this.clearButton.setEnabled(false);
      return var1;
   }

   protected void updateOKButtonAction() {
      this.okButton.setEnabled(!this.textField.getText().equals(""));
   }

   protected void updateClearButtonAction() {
      this.clearButton.setEnabled(!this.textField.getText().equals(""));
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   static {
      resources = new ResourceManager(bundle);
   }

   protected class ClearButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         URIChooser.this.textField.setText("");
      }
   }

   protected class CancelButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         URIChooser.this.returnCode = 1;
         URIChooser.this.dispose();
         URIChooser.this.textField.setText(URIChooser.this.chosenPath);
      }
   }

   protected class OKButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         URIChooser.this.returnCode = 0;
         URIChooser.this.chosenPath = URIChooser.this.textField.getText();
         URIChooser.this.dispose();
      }
   }

   protected class BrowseButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JFileChooser var2 = new JFileChooser(URIChooser.this.currentPath);
         var2.setFileHidingEnabled(false);
         var2.setFileSelectionMode(2);
         if (URIChooser.this.fileFilter != null) {
            var2.setFileFilter(URIChooser.this.fileFilter);
         }

         int var3 = var2.showOpenDialog(URIChooser.this);
         if (var3 == 0) {
            File var4 = var2.getSelectedFile();

            try {
               URIChooser.this.textField.setText(URIChooser.this.currentPath = var4.getCanonicalPath());
            } catch (IOException var6) {
            }
         }

      }
   }

   protected class DocumentAdapter implements DocumentListener {
      public void changedUpdate(DocumentEvent var1) {
         URIChooser.this.updateOKButtonAction();
         URIChooser.this.updateClearButtonAction();
      }

      public void insertUpdate(DocumentEvent var1) {
         URIChooser.this.updateOKButtonAction();
         URIChooser.this.updateClearButtonAction();
      }

      public void removeUpdate(DocumentEvent var1) {
         URIChooser.this.updateOKButtonAction();
         URIChooser.this.updateClearButtonAction();
      }
   }
}
