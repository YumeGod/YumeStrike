package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.io.StringReader;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.gui.xmleditor.XMLTextEditor;
import org.apache.batik.util.resources.ResourceManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class NodePickerPanel extends JPanel implements ActionMap {
   private static final int VIEW_MODE = 1;
   private static final int EDIT_MODE = 2;
   private static final int ADD_NEW_ELEMENT = 3;
   private static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.NodePickerPanelMessages";
   private static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.NodePickerPanelMessages", Locale.getDefault());
   private static ResourceManager resources;
   private JTable attributesTable;
   private TableModelListener tableModelListener;
   private JScrollPane attributePane;
   private JPanel attributesPanel;
   private ButtonFactory buttonFactory;
   private JButton addButton;
   private JButton removeButton;
   private JLabel attributesLabel;
   private JButton applyButton;
   private JButton resetButton;
   private JPanel choosePanel;
   private SVGInputPanel svgInputPanel;
   private JLabel isWellFormedLabel;
   private JLabel svgInputPanelNameLabel;
   private boolean shouldProcessUpdate = true;
   private Element previewElement;
   private Element clonedElement;
   private Node parentElement;
   private int mode;
   private boolean isDirty;
   private EventListenerList eventListeners = new EventListenerList();
   private NodePickerController controller;
   private Map listeners = new HashMap(10);
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$NodePickerPanel;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener;

   public NodePickerPanel(NodePickerController var1) {
      super(new GridBagLayout());
      this.controller = var1;
      this.initialize();
   }

   private void initialize() {
      this.addButtonActions();
      GridBagConstraints var1 = new GridBagConstraints();
      var1.gridx = 1;
      var1.gridy = 1;
      var1.anchor = 18;
      var1.fill = 0;
      var1.insets = new Insets(5, 5, 0, 5);
      this.attributesLabel = new JLabel();
      String var2 = resources.getString("AttributesTable.name");
      this.attributesLabel.setText(var2);
      this.add(this.attributesLabel, var1);
      var1.gridx = 1;
      var1.gridy = 2;
      var1.gridwidth = 2;
      var1.weightx = 1.0;
      var1.weighty = 0.3;
      var1.fill = 1;
      var1.anchor = 10;
      var1.insets = new Insets(0, 0, 0, 5);
      this.add(this.getAttributesPanel(), var1);
      var1.weightx = 0.0;
      var1.weighty = 0.0;
      var1.gridwidth = 1;
      var1.gridx = 1;
      var1.gridy = 3;
      var1.anchor = 18;
      var1.fill = 0;
      var1.insets = new Insets(0, 5, 0, 5);
      this.svgInputPanelNameLabel = new JLabel();
      String var3 = resources.getString("InputPanelLabel.name");
      this.svgInputPanelNameLabel.setText(var3);
      this.add(this.svgInputPanelNameLabel, var1);
      var1.gridx = 1;
      var1.gridy = 4;
      var1.gridwidth = 2;
      var1.weightx = 1.0;
      var1.weighty = 1.0;
      var1.fill = 1;
      var1.anchor = 10;
      var1.insets = new Insets(0, 5, 0, 10);
      this.add(this.getSvgInputPanel(), var1);
      var1.weightx = 0.0;
      var1.weighty = 0.0;
      var1.gridwidth = 1;
      var1.gridx = 1;
      var1.gridy = 5;
      var1.anchor = 18;
      var1.fill = 0;
      var1.insets = new Insets(5, 5, 0, 5);
      this.isWellFormedLabel = new JLabel();
      String var4 = resources.getString("IsWellFormedLabel.wellFormed");
      this.isWellFormedLabel.setText(var4);
      this.add(this.isWellFormedLabel, var1);
      var1.weightx = 0.0;
      var1.weighty = 0.0;
      var1.gridwidth = 1;
      var1.gridx = 2;
      var1.gridy = 5;
      var1.anchor = 13;
      var1.insets = new Insets(0, 0, 0, 5);
      this.add(this.getChoosePanel(), var1);
      this.enterViewMode();
   }

   private ButtonFactory getButtonFactory() {
      if (this.buttonFactory == null) {
         this.buttonFactory = new ButtonFactory(bundle, this);
      }

      return this.buttonFactory;
   }

   private void addButtonActions() {
      this.listeners.put("ApplyButtonAction", new ApplyButtonAction());
      this.listeners.put("ResetButtonAction", new ResetButtonAction());
      this.listeners.put("AddButtonAction", new AddButtonAction());
      this.listeners.put("RemoveButtonAction", new RemoveButtonAction());
   }

   private JButton getAddButton() {
      if (this.addButton == null) {
         this.addButton = this.getButtonFactory().createJButton("AddButton");
         this.addButton.addFocusListener(new NodePickerEditListener());
      }

      return this.addButton;
   }

   private JButton getRemoveButton() {
      if (this.removeButton == null) {
         this.removeButton = this.getButtonFactory().createJButton("RemoveButton");
         this.removeButton.addFocusListener(new NodePickerEditListener());
      }

      return this.removeButton;
   }

   private JButton getApplyButton() {
      if (this.applyButton == null) {
         this.applyButton = this.getButtonFactory().createJButton("ApplyButton");
      }

      return this.applyButton;
   }

   private JButton getResetButton() {
      if (this.resetButton == null) {
         this.resetButton = this.getButtonFactory().createJButton("ResetButton");
      }

      return this.resetButton;
   }

   private JPanel getAttributesPanel() {
      if (this.attributesPanel == null) {
         this.attributesPanel = new JPanel(new GridBagLayout());
         GridBagConstraints var1 = new GridBagConstraints();
         var1.gridx = 1;
         var1.gridy = 1;
         var1.fill = 1;
         var1.anchor = 10;
         var1.weightx = 4.0;
         var1.weighty = 1.0;
         var1.gridheight = 5;
         var1.gridwidth = 2;
         var1.insets = new Insets(5, 5, 5, 0);
         GridBagConstraints var2 = new GridBagConstraints();
         var2.gridx = 3;
         var2.gridy = 1;
         var2.fill = 2;
         var2.anchor = 11;
         var2.insets = new Insets(5, 20, 0, 5);
         var2.weightx = 1.0;
         GridBagConstraints var3 = new GridBagConstraints();
         var3.gridx = 3;
         var3.gridy = 3;
         var3.fill = 2;
         var3.anchor = 11;
         var3.insets = new Insets(5, 20, 0, 5);
         var3.weightx = 1.0;
         this.attributesTable = new JTable();
         this.attributesTable.setModel(new AttributesTableModel(10, 2));
         this.tableModelListener = new AttributesTableModelListener();
         this.attributesTable.getModel().addTableModelListener(this.tableModelListener);
         this.attributesTable.addFocusListener(new NodePickerEditListener());
         this.attributePane = new JScrollPane();
         this.attributePane.getViewport().add(this.attributesTable);
         this.attributesPanel.add(this.attributePane, var1);
         this.attributesPanel.add(this.getAddButton(), var2);
         this.attributesPanel.add(this.getRemoveButton(), var3);
      }

      return this.attributesPanel;
   }

   private SVGInputPanel getSvgInputPanel() {
      if (this.svgInputPanel == null) {
         this.svgInputPanel = new SVGInputPanel();
         this.svgInputPanel.getNodeXmlArea().getDocument().addDocumentListener(new XMLAreaListener());
         this.svgInputPanel.getNodeXmlArea().addFocusListener(new NodePickerEditListener());
      }

      return this.svgInputPanel;
   }

   private JPanel getChoosePanel() {
      if (this.choosePanel == null) {
         this.choosePanel = new JPanel(new GridBagLayout());
         GridBagConstraints var1 = new GridBagConstraints();
         var1.gridx = 1;
         var1.gridy = 1;
         var1.weightx = 0.5;
         var1.anchor = 17;
         var1.fill = 2;
         var1.insets = new Insets(5, 5, 5, 5);
         GridBagConstraints var2 = new GridBagConstraints();
         var2.gridx = 2;
         var2.gridy = 1;
         var2.weightx = 0.5;
         var2.anchor = 13;
         var2.fill = 2;
         var2.insets = new Insets(5, 5, 5, 5);
         this.choosePanel.add(this.getApplyButton(), var1);
         this.choosePanel.add(this.getResetButton(), var2);
      }

      return this.choosePanel;
   }

   public String getResults() {
      return this.getSvgInputPanel().getNodeXmlArea().getText();
   }

   private void updateViewAfterSvgInput(Element var1, Element var2) {
      String var3;
      if (var1 != null) {
         var3 = resources.getString("IsWellFormedLabel.wellFormed");
         this.isWellFormedLabel.setText(var3);
         this.getApplyButton().setEnabled(true);
         this.attributesTable.setEnabled(true);
         this.updateElementAttributes(var2, var1);
         this.shouldProcessUpdate = false;
         this.updateAttributesTable(var2);
         this.shouldProcessUpdate = true;
      } else {
         var3 = resources.getString("IsWellFormedLabel.notWellFormed");
         this.isWellFormedLabel.setText(var3);
         this.getApplyButton().setEnabled(false);
         this.attributesTable.setEnabled(false);
      }

   }

   private void updateElementAttributes(Element var1, Element var2) {
      this.removeAttributes(var1);
      NamedNodeMap var3 = var2.getAttributes();

      for(int var4 = var3.getLength() - 1; var4 >= 0; --var4) {
         Node var5 = var3.item(var4);
         String var6 = var5.getNodeName();
         String var7 = var5.getNodeValue();
         String var8 = DOMUtilities.getPrefix(var6);
         String var9 = this.getNamespaceURI(var8);
         var1.setAttributeNS(var9, var6, var7);
      }

   }

   private void updateElementAttributes(Element var1, AttributesTableModel var2) {
      this.removeAttributes(var1);

      for(int var3 = 0; var3 < var2.getRowCount(); ++var3) {
         String var4 = (String)var2.getAttrNameAt(var3);
         String var5 = (String)var2.getAttrValueAt(var3);
         if (var4 != null && var4.length() > 0) {
            String var6;
            if (var4.equals("xmlns")) {
               var6 = "http://www.w3.org/2000/xmlns/";
            } else {
               String var7 = DOMUtilities.getPrefix(var4);
               var6 = this.getNamespaceURI(var7);
            }

            if (var5 != null) {
               var1.setAttributeNS(var6, var4, var5);
            } else {
               var1.setAttributeNS(var6, var4, "");
            }
         }
      }

   }

   private void removeAttributes(Element var1) {
      NamedNodeMap var2 = var1.getAttributes();
      int var3 = var2.getLength();

      for(int var4 = var3 - 1; var4 >= 0; --var4) {
         var1.removeAttributeNode((Attr)var2.item(var4));
      }

   }

   private String getNamespaceURI(String var1) {
      String var2 = null;
      if (var1 != null) {
         if (var1.equals("xmlns")) {
            var2 = "http://www.w3.org/2000/xmlns/";
         } else {
            AbstractNode var3;
            if (this.mode == 2) {
               var3 = (AbstractNode)this.previewElement;
               var2 = var3.lookupNamespaceURI(var1);
            } else if (this.mode == 3) {
               var3 = (AbstractNode)this.parentElement;
               var2 = var3.lookupNamespaceURI(var1);
            }
         }
      }

      return var2;
   }

   private void updateAttributesTable(Element var1) {
      NamedNodeMap var2 = var1.getAttributes();
      AttributesTableModel var3 = (AttributesTableModel)this.attributesTable.getModel();

      int var4;
      String var6;
      for(var4 = var3.getRowCount() - 1; var4 >= 0; --var4) {
         String var5 = (String)var3.getValueAt(var4, 0);
         var6 = "";
         if (var5 != null) {
            var6 = var1.getAttributeNS((String)null, var5);
         }

         if (var5 == null || var6.length() == 0) {
            var3.removeRow(var4);
         }

         if (var6.length() > 0) {
            var3.setValueAt(var6, var4, 1);
         }
      }

      for(var4 = 0; var4 < var2.getLength(); ++var4) {
         Node var9 = var2.item(var4);
         var6 = var9.getNodeName();
         String var7 = var9.getNodeValue();
         if (var3.getValueForName(var6) == null) {
            Vector var8 = new Vector();
            var8.add(var6);
            var8.add(var7);
            var3.addRow(var8);
         }
      }

   }

   private void updateNodeXmlArea(Node var1) {
      this.getSvgInputPanel().getNodeXmlArea().setText(DOMUtilities.getXML(var1));
   }

   private Element getPreviewElement() {
      return this.previewElement;
   }

   public void setPreviewElement(Element var1) {
      if (this.previewElement == var1 || !this.isDirty || this.promptForChanges()) {
         this.previewElement = var1;
         this.enterViewMode();
         this.updateNodeXmlArea(var1);
         this.updateAttributesTable(var1);
      }
   }

   boolean panelHiding() {
      return !this.isDirty || this.promptForChanges();
   }

   private int getMode() {
      return this.mode;
   }

   public void enterViewMode() {
      if (this.mode != 1) {
         this.mode = 1;
         this.getApplyButton().setEnabled(false);
         this.getResetButton().setEnabled(false);
         this.getRemoveButton().setEnabled(true);
         this.getAddButton().setEnabled(true);
         String var1 = resources.getString("IsWellFormedLabel.wellFormed");
         this.isWellFormedLabel.setText(var1);
      }

   }

   public void enterEditMode() {
      if (this.mode != 2) {
         this.mode = 2;
         this.clonedElement = (Element)this.previewElement.cloneNode(true);
         this.getApplyButton().setEnabled(true);
         this.getResetButton().setEnabled(true);
      }

   }

   public void enterAddNewElementMode(Element var1, Node var2) {
      if (this.mode != 3) {
         this.mode = 3;
         this.previewElement = var1;
         this.clonedElement = (Element)var1.cloneNode(true);
         this.parentElement = var2;
         this.updateNodeXmlArea(var1);
         this.getApplyButton().setEnabled(true);
         this.getResetButton().setEnabled(true);
      }

   }

   public void updateOnDocumentChange(String var1, Node var2) {
      if (this.mode == 1 && this.isShowing() && this.shouldUpdate(var1, var2, this.getPreviewElement())) {
         this.setPreviewElement(this.getPreviewElement());
      }

   }

   private boolean shouldUpdate(String var1, Node var2, Node var3) {
      if (var1.equals("DOMNodeInserted")) {
         if (DOMUtilities.isAncestorOf(var3, var2)) {
            return true;
         }
      } else if (var1.equals("DOMNodeRemoved")) {
         if (DOMUtilities.isAncestorOf(var3, var2)) {
            return true;
         }
      } else if (var1.equals("DOMAttrModified")) {
         if (DOMUtilities.isAncestorOf(var3, var2) || var3 == var2) {
            return true;
         }
      } else if (var1.equals("DOMCharDataModified") && DOMUtilities.isAncestorOf(var3, var2)) {
         return true;
      }

      return false;
   }

   private Element parseXml(String var1) {
      Document var2 = null;
      DocumentBuilderFactory var3 = DocumentBuilderFactory.newInstance();

      try {
         DocumentBuilder var4 = var3.newDocumentBuilder();
         var4.setErrorHandler(new ErrorHandler() {
            public void error(SAXParseException var1) throws SAXException {
            }

            public void fatalError(SAXParseException var1) throws SAXException {
            }

            public void warning(SAXParseException var1) throws SAXException {
            }
         });
         var2 = var4.parse(new InputSource(new StringReader(var1)));
      } catch (ParserConfigurationException var5) {
      } catch (SAXException var6) {
      } catch (IOException var7) {
      }

      return var2 != null ? var2.getDocumentElement() : null;
   }

   public void setEditable(boolean var1) {
      this.getSvgInputPanel().getNodeXmlArea().setEditable(var1);
      this.getResetButton().setEnabled(var1);
      this.getApplyButton().setEnabled(var1);
      this.getAddButton().setEnabled(var1);
      this.getRemoveButton().setEnabled(var1);
      this.attributesTable.setEnabled(var1);
   }

   private boolean isANodePickerComponent(Component var1) {
      return SwingUtilities.getAncestorOfClass(class$org$apache$batik$apps$svgbrowser$NodePickerPanel == null ? (class$org$apache$batik$apps$svgbrowser$NodePickerPanel = class$("org.apache.batik.apps.svgbrowser.NodePickerPanel")) : class$org$apache$batik$apps$svgbrowser$NodePickerPanel, var1) != null;
   }

   public boolean promptForChanges() {
      if (this.getApplyButton().isEnabled() && this.isElementModified()) {
         String var1 = resources.getString("ConfirmDialog.message");
         int var2 = JOptionPane.showConfirmDialog(this.getSvgInputPanel(), var1);
         if (var2 == 0) {
            this.getApplyButton().doClick();
         } else {
            if (var2 == 2) {
               return false;
            }

            this.getResetButton().doClick();
         }
      } else {
         this.getResetButton().doClick();
      }

      this.isDirty = false;
      return true;
   }

   private boolean isElementModified() {
      if (this.getMode() == 2) {
         return !DOMUtilities.getXML(this.previewElement).equals(this.getSvgInputPanel().getNodeXmlArea().getText());
      } else {
         return this.getMode() == 3;
      }
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   public void fireUpdateElement(NodePickerEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener == null ? (class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener = class$("org.apache.batik.apps.svgbrowser.NodePickerPanel$NodePickerListener")) : class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener)) {
            ((NodePickerListener)var2[var4 + 1]).updateElement(var1);
         }
      }

   }

   public void fireAddNewElement(NodePickerEvent var1) {
      Object[] var2 = this.eventListeners.getListenerList();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; var4 += 2) {
         if (var2[var4] == (class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener == null ? (class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener = class$("org.apache.batik.apps.svgbrowser.NodePickerPanel$NodePickerListener")) : class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener)) {
            ((NodePickerListener)var2[var4 + 1]).addNewElement(var1);
         }
      }

   }

   public void addListener(NodePickerListener var1) {
      this.eventListeners.add(class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener == null ? (class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener = class$("org.apache.batik.apps.svgbrowser.NodePickerPanel$NodePickerListener")) : class$org$apache$batik$apps$svgbrowser$NodePickerPanel$NodePickerListener, var1);
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

   public static class NameEditorDialog extends JDialog implements ActionMap {
      public static final int OK_OPTION = 0;
      public static final int CANCEL_OPTION = 1;
      protected static final String RESOURCES = "org.apache.batik.apps.svgbrowser.resources.NameEditorDialogMessages";
      protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.NameEditorDialogMessages", Locale.getDefault());
      protected static ResourceManager resources;
      protected int returnCode;
      protected JPanel mainPanel;
      protected ButtonFactory buttonFactory;
      protected JLabel nodeNameLabel;
      protected JTextField nodeNameField;
      protected JButton okButton;
      protected JButton cancelButton;
      protected Map listeners = new HashMap(10);

      public NameEditorDialog(Frame var1) {
         super(var1, true);
         this.setResizable(false);
         this.setModal(true);
         this.initialize();
      }

      protected void initialize() {
         this.setSize(resources.getInteger("Dialog.width"), resources.getInteger("Dialog.height"));
         this.setTitle(resources.getString("Dialog.title"));
         this.addButtonActions();
         this.setContentPane(this.getMainPanel());
      }

      protected ButtonFactory getButtonFactory() {
         if (this.buttonFactory == null) {
            this.buttonFactory = new ButtonFactory(bundle, this);
         }

         return this.buttonFactory;
      }

      protected void addButtonActions() {
         this.listeners.put("OKButtonAction", new OKButtonAction());
         this.listeners.put("CancelButtonAction", new CancelButtonAction());
      }

      public int showDialog() {
         this.setVisible(true);
         return this.returnCode;
      }

      protected JButton getOkButton() {
         if (this.okButton == null) {
            this.okButton = this.getButtonFactory().createJButton("OKButton");
            this.getRootPane().setDefaultButton(this.okButton);
         }

         return this.okButton;
      }

      protected JButton getCancelButton() {
         if (this.cancelButton == null) {
            this.cancelButton = this.getButtonFactory().createJButton("CancelButton");
         }

         return this.cancelButton;
      }

      protected JPanel getMainPanel() {
         if (this.mainPanel == null) {
            this.mainPanel = new JPanel(new GridBagLayout());
            GridBagConstraints var1 = new GridBagConstraints();
            var1.gridx = 1;
            var1.gridy = 1;
            var1.fill = 0;
            var1.insets = new Insets(5, 5, 5, 5);
            this.mainPanel.add(this.getNodeNameLabel(), var1);
            var1.gridx = 2;
            var1.weightx = 1.0;
            var1.weighty = 1.0;
            var1.fill = 2;
            var1.anchor = 10;
            this.mainPanel.add(this.getNodeNameField(), var1);
            var1.gridx = 1;
            var1.gridy = 2;
            var1.weightx = 0.0;
            var1.weighty = 0.0;
            var1.anchor = 13;
            var1.fill = 2;
            this.mainPanel.add(this.getOkButton(), var1);
            var1.gridx = 2;
            var1.gridy = 2;
            var1.anchor = 13;
            this.mainPanel.add(this.getCancelButton(), var1);
         }

         return this.mainPanel;
      }

      public JLabel getNodeNameLabel() {
         if (this.nodeNameLabel == null) {
            this.nodeNameLabel = new JLabel();
            this.nodeNameLabel.setText(resources.getString("Dialog.label"));
         }

         return this.nodeNameLabel;
      }

      protected JTextField getNodeNameField() {
         if (this.nodeNameField == null) {
            this.nodeNameField = new JTextField();
         }

         return this.nodeNameField;
      }

      public String getResults() {
         return this.nodeNameField.getText();
      }

      public Action getAction(String var1) throws MissingListenerException {
         return (Action)this.listeners.get(var1);
      }

      static {
         resources = new ResourceManager(bundle);
      }

      protected class CancelButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            NameEditorDialog.this.returnCode = 1;
            NameEditorDialog.this.dispose();
         }
      }

      protected class OKButtonAction extends AbstractAction {
         public void actionPerformed(ActionEvent var1) {
            NameEditorDialog.this.returnCode = 0;
            NameEditorDialog.this.dispose();
         }
      }
   }

   protected class SVGInputPanel extends JPanel {
      protected XMLTextEditor nodeXmlArea;

      public SVGInputPanel() {
         super(new BorderLayout());
         this.add(new JScrollPane(this.getNodeXmlArea()));
      }

      protected XMLTextEditor getNodeXmlArea() {
         if (this.nodeXmlArea == null) {
            this.nodeXmlArea = new XMLTextEditor();
            this.nodeXmlArea.setEditable(true);
         }

         return this.nodeXmlArea;
      }
   }

   public static class NodePickerAdapter implements NodePickerListener {
      public void addNewElement(NodePickerEvent var1) {
      }

      public void updateElement(NodePickerEvent var1) {
      }
   }

   public interface NodePickerListener extends EventListener {
      void updateElement(NodePickerEvent var1);

      void addNewElement(NodePickerEvent var1);
   }

   public static class NodePickerEvent extends EventObject {
      public static final int EDIT_ELEMENT = 1;
      public static final int ADD_NEW_ELEMENT = 2;
      private int type;
      private String result;
      private Node contextNode;

      public NodePickerEvent(Object var1, String var2, Node var3, int var4) {
         super(var1);
         this.result = var2;
         this.contextNode = var3;
      }

      public String getResult() {
         return this.result;
      }

      public Node getContextNode() {
         return this.contextNode;
      }

      public int getType() {
         return this.type;
      }
   }

   public static class AttributesTableModel extends DefaultTableModel {
      public AttributesTableModel(int var1, int var2) {
         super(var1, var2);
      }

      public String getColumnName(int var1) {
         return var1 == 0 ? NodePickerPanel.resources.getString("AttributesTable.column1") : NodePickerPanel.resources.getString("AttributesTable.column2");
      }

      public Object getValueForName(Object var1) {
         for(int var2 = 0; var2 < this.getRowCount(); ++var2) {
            if (this.getValueAt(var2, 0) != null && this.getValueAt(var2, 0).equals(var1)) {
               return this.getValueAt(var2, 1);
            }
         }

         return null;
      }

      public Object getAttrNameAt(int var1) {
         return this.getValueAt(var1, 0);
      }

      public Object getAttrValueAt(int var1) {
         return this.getValueAt(var1, 1);
      }

      public int getRow(Object var1) {
         for(int var2 = 0; var2 < this.getRowCount(); ++var2) {
            if (this.getValueAt(var2, 0) != null && this.getValueAt(var2, 0).equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   protected class RemoveButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (NodePickerPanel.this.getMode() == 1) {
            NodePickerPanel.this.enterEditMode();
         }

         Element var2 = NodePickerPanel.this.clonedElement;
         if (NodePickerPanel.this.getMode() == 3) {
            var2 = NodePickerPanel.this.previewElement;
         }

         DefaultTableModel var3 = (DefaultTableModel)NodePickerPanel.this.attributesTable.getModel();
         int[] var4 = NodePickerPanel.this.attributesTable.getSelectedRows();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = (String)var3.getValueAt(var4[var5], 0);
            if (var6 != null) {
               String var7 = DOMUtilities.getPrefix(var6);
               String var8 = DOMUtilities.getLocalName(var6);
               String var9 = NodePickerPanel.this.getNamespaceURI(var7);
               var2.removeAttributeNS(var9, var8);
            }
         }

         NodePickerPanel.this.shouldProcessUpdate = false;
         NodePickerPanel.this.updateAttributesTable(var2);
         NodePickerPanel.this.shouldProcessUpdate = true;
         NodePickerPanel.this.updateNodeXmlArea(var2);
      }
   }

   protected class AddButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (NodePickerPanel.this.getMode() == 1) {
            NodePickerPanel.this.enterEditMode();
         }

         DefaultTableModel var2 = (DefaultTableModel)NodePickerPanel.this.attributesTable.getModel();
         NodePickerPanel.this.shouldProcessUpdate = false;
         var2.addRow((Vector)null);
         NodePickerPanel.this.shouldProcessUpdate = true;
      }
   }

   protected class ResetButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         NodePickerPanel.this.isDirty = false;
         NodePickerPanel.this.setPreviewElement(NodePickerPanel.this.getPreviewElement());
      }
   }

   protected class ApplyButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         NodePickerPanel.this.isDirty = false;
         String var2 = NodePickerPanel.this.getResults();
         if (NodePickerPanel.this.getMode() == 2) {
            NodePickerPanel.this.fireUpdateElement(new NodePickerEvent(NodePickerPanel.this, var2, NodePickerPanel.this.previewElement, 1));
         } else if (NodePickerPanel.this.getMode() == 3) {
            NodePickerPanel.this.fireAddNewElement(new NodePickerEvent(NodePickerPanel.this, var2, NodePickerPanel.this.parentElement, 2));
         }

         NodePickerPanel.this.enterViewMode();
      }
   }

   protected class AttributesTableModelListener implements TableModelListener {
      public void tableChanged(TableModelEvent var1) {
         if (var1.getType() == 0 && NodePickerPanel.this.shouldProcessUpdate) {
            this.updateNodePicker(var1);
         }

      }

      private void updateNodePicker(TableModelEvent var1) {
         if (NodePickerPanel.this.getMode() == 2) {
            NodePickerPanel.this.updateElementAttributes(NodePickerPanel.this.clonedElement, (AttributesTableModel)var1.getSource());
            NodePickerPanel.this.updateNodeXmlArea(NodePickerPanel.this.clonedElement);
         } else if (NodePickerPanel.this.getMode() == 3) {
            NodePickerPanel.this.updateElementAttributes(NodePickerPanel.this.previewElement, (AttributesTableModel)var1.getSource());
            NodePickerPanel.this.updateNodeXmlArea(NodePickerPanel.this.previewElement);
         }

      }
   }

   protected class XMLAreaListener implements DocumentListener {
      public void changedUpdate(DocumentEvent var1) {
         NodePickerPanel.this.isDirty = NodePickerPanel.this.isElementModified();
      }

      public void insertUpdate(DocumentEvent var1) {
         this.updateNodePicker(var1);
         NodePickerPanel.this.isDirty = NodePickerPanel.this.isElementModified();
      }

      public void removeUpdate(DocumentEvent var1) {
         this.updateNodePicker(var1);
         NodePickerPanel.this.isDirty = NodePickerPanel.this.isElementModified();
      }

      private void updateNodePicker(DocumentEvent var1) {
         if (NodePickerPanel.this.getMode() == 2) {
            NodePickerPanel.this.updateViewAfterSvgInput(NodePickerPanel.this.parseXml(NodePickerPanel.this.svgInputPanel.getNodeXmlArea().getText()), NodePickerPanel.this.clonedElement);
         } else if (NodePickerPanel.this.getMode() == 3) {
            NodePickerPanel.this.updateViewAfterSvgInput(NodePickerPanel.this.parseXml(NodePickerPanel.this.svgInputPanel.getNodeXmlArea().getText()), NodePickerPanel.this.previewElement);
         }

      }
   }

   protected class NodePickerEditListener extends FocusAdapter {
      public void focusGained(FocusEvent var1) {
         if (NodePickerPanel.this.getMode() == 1) {
            NodePickerPanel.this.enterEditMode();
         }

         NodePickerPanel.this.setEditable(NodePickerPanel.this.controller.isEditable() && NodePickerPanel.this.controller.canEdit(NodePickerPanel.this.previewElement));
         NodePickerPanel.this.isDirty = NodePickerPanel.this.isElementModified();
      }
   }
}
