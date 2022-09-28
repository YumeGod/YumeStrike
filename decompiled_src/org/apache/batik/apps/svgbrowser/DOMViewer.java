package org.apache.batik.apps.svgbrowser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.batik.bridge.svg12.ContentManager;
import org.apache.batik.bridge.svg12.DefaultXBLManager;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg12.XBLOMContentElement;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.dom.xbl.NodeXBL;
import org.apache.batik.dom.xbl.XBLManager;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.util.gui.DropDownComponent;
import org.apache.batik.util.gui.resource.ActionMap;
import org.apache.batik.util.gui.resource.ButtonFactory;
import org.apache.batik.util.gui.resource.MissingListenerException;
import org.apache.batik.util.resources.ResourceManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

public class DOMViewer extends JFrame implements ActionMap {
   protected static final String RESOURCE = "org.apache.batik.apps.svgbrowser.resources.DOMViewerMessages";
   protected static ResourceBundle bundle = ResourceBundle.getBundle("org.apache.batik.apps.svgbrowser.resources.DOMViewerMessages", Locale.getDefault());
   protected static ResourceManager resources;
   protected Map listeners = new HashMap();
   protected ButtonFactory buttonFactory;
   protected Panel panel;
   protected boolean showWhitespace = true;
   protected boolean isCapturingClickEnabled;
   protected DOMViewerController domViewerController;
   protected ElementOverlayManager elementOverlayManager;
   protected boolean isElementOverlayEnabled;
   protected HistoryBrowserInterface historyBrowserInterface;
   protected boolean canEdit = true;
   protected JToggleButton overlayButton;

   public DOMViewer(DOMViewerController var1) {
      super(resources.getString("Frame.title"));
      this.setSize(resources.getInteger("Frame.width"), resources.getInteger("Frame.height"));
      this.domViewerController = var1;
      this.elementOverlayManager = this.domViewerController.createSelectionManager();
      if (this.elementOverlayManager != null) {
         this.elementOverlayManager.setController(new DOMViewerElementOverlayController());
      }

      this.historyBrowserInterface = new HistoryBrowserInterface(new HistoryBrowser.DocumentCommandController(var1));
      this.listeners.put("CloseButtonAction", new CloseButtonAction());
      this.listeners.put("UndoButtonAction", new UndoButtonAction());
      this.listeners.put("RedoButtonAction", new RedoButtonAction());
      this.listeners.put("CapturingClickButtonAction", new CapturingClickButtonAction());
      this.listeners.put("OverlayButtonAction", new OverlayButtonAction());
      this.panel = new Panel();
      this.getContentPane().add(this.panel);
      JPanel var2 = new JPanel(new BorderLayout());
      JCheckBox var3 = new JCheckBox(resources.getString("ShowWhitespaceCheckbox.text"));
      var3.setSelected(this.showWhitespace);
      var3.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            DOMViewer.this.setShowWhitespace(var1.getStateChange() == 1);
         }
      });
      var2.add(var3, "West");
      var2.add(this.getButtonFactory().createJButton("CloseButton"), "East");
      this.getContentPane().add(var2, "South");
      Document var4 = this.domViewerController.getDocument();
      if (var4 != null) {
         this.panel.setDocument(var4, (ViewCSS)null);
      }

   }

   public void setShowWhitespace(boolean var1) {
      this.showWhitespace = var1;
      if (this.panel.document != null) {
         this.panel.setDocument(this.panel.document);
      }

   }

   public void setDocument(Document var1) {
      this.panel.setDocument(var1);
   }

   public void setDocument(Document var1, ViewCSS var2) {
      this.panel.setDocument(var1, var2);
   }

   public boolean canEdit() {
      return this.domViewerController.canEdit() && this.canEdit;
   }

   public void setEditable(boolean var1) {
      this.canEdit = var1;
   }

   public void selectNode(Node var1) {
      this.panel.selectNode(var1);
   }

   public void resetHistory() {
      this.historyBrowserInterface.getHistoryBrowser().resetHistory();
   }

   private ButtonFactory getButtonFactory() {
      if (this.buttonFactory == null) {
         this.buttonFactory = new ButtonFactory(bundle, this);
      }

      return this.buttonFactory;
   }

   public Action getAction(String var1) throws MissingListenerException {
      return (Action)this.listeners.get(var1);
   }

   private void addChangesToHistory() {
      this.historyBrowserInterface.performCurrentCompoundCommand();
   }

   protected void toggleOverlay() {
      this.isElementOverlayEnabled = this.overlayButton.isSelected();
      if (!this.isElementOverlayEnabled) {
         this.overlayButton.setToolTipText(resources.getString("OverlayButton.tooltip"));
      } else {
         this.overlayButton.setToolTipText(resources.getString("OverlayButton.disableText"));
      }

      if (this.elementOverlayManager != null) {
         this.elementOverlayManager.setOverlayEnabled(this.isElementOverlayEnabled);
         this.elementOverlayManager.repaint();
      }

   }

   static {
      resources = new ResourceManager(bundle);
   }

   protected static class ContentNodeInfo extends NodeInfo {
      public ContentNodeInfo(Node var1) {
         super(var1);
      }

      public String toString() {
         return "selected content";
      }
   }

   protected static class ShadowNodeInfo extends NodeInfo {
      public ShadowNodeInfo(Node var1) {
         super(var1);
      }

      public String toString() {
         return "shadow tree";
      }
   }

   protected static class NodeInfo {
      protected Node node;

      public NodeInfo(Node var1) {
         this.node = var1;
      }

      public Node getNode() {
         return this.node;
      }

      public String toString() {
         if (this.node instanceof Element) {
            Element var1 = (Element)this.node;
            String var2 = var1.getAttribute("id");
            if (var2.length() != 0) {
               return this.node.getNodeName() + " \"" + var2 + "\"";
            }
         }

         return this.node.getNodeName();
      }
   }

   public class Panel extends JPanel {
      public static final String NODE_INSERTED = "DOMNodeInserted";
      public static final String NODE_REMOVED = "DOMNodeRemoved";
      public static final String ATTRIBUTE_MODIFIED = "DOMAttrModified";
      public static final String CHAR_DATA_MODIFIED = "DOMCharacterDataModified";
      protected Document document;
      protected EventListener nodeInsertion;
      protected EventListener nodeRemoval;
      protected EventListener attrModification;
      protected EventListener charDataModification;
      protected EventListener capturingListener;
      protected ViewCSS viewCSS;
      protected DOMDocumentTree tree;
      protected JSplitPane splitPane;
      protected JPanel rightPanel = new JPanel(new BorderLayout());
      protected JTable propertiesTable = new JTable();
      protected NodePickerPanel attributePanel = new NodePickerPanel(DOMViewer.this.new DOMViewerNodePickerController());
      protected GridBagConstraints attributePanelLayout;
      protected GridBagConstraints propertiesTableLayout;
      protected JPanel elementPanel;
      protected CharacterPanel characterDataPanel;
      protected JTextArea documentInfo;
      protected JPanel documentInfoPanel;

      public Panel() {
         super(new BorderLayout());
         this.attributePanel.addListener(new NodePickerPanel.NodePickerAdapter() {
            public void updateElement(NodePickerPanel.NodePickerEvent var1) {
               String var2 = var1.getResult();
               Element var3 = (Element)var1.getContextNode();
               Element var4 = this.wrapAndParse(var2, var3);
               DOMViewer.this.addChangesToHistory();
               HistoryBrowserInterface.CompoundUpdateCommand var5 = DOMViewer.this.historyBrowserInterface.createNodeChangedCommand(var4);
               Node var6 = var3.getParentNode();
               Node var7 = var3.getNextSibling();
               var5.addCommand(DOMViewer.this.historyBrowserInterface.createRemoveChildCommand(var6, var3));
               var5.addCommand(DOMViewer.this.historyBrowserInterface.createInsertChildCommand(var6, var7, var4));
               DOMViewer.this.historyBrowserInterface.performCompoundUpdateCommand(var5);
               Panel.this.attributePanel.setPreviewElement(var4);
            }

            public void addNewElement(NodePickerPanel.NodePickerEvent var1) {
               String var2 = var1.getResult();
               Element var3 = (Element)var1.getContextNode();
               Element var4 = this.wrapAndParse(var2, var3);
               DOMViewer.this.addChangesToHistory();
               DOMViewer.this.historyBrowserInterface.appendChild(var3, var4);
               Panel.this.attributePanel.setPreviewElement(var4);
            }

            private Element wrapAndParse(String var1, Node var2) {
               HashMap var3 = new HashMap();
               int var4 = 0;

               for(Node var5 = var2; var5 != null; var5 = var5.getParentNode()) {
                  NamedNodeMap var6 = var5.getAttributes();

                  for(int var7 = 0; var6 != null && var7 < var6.getLength(); ++var7) {
                     Attr var8 = (Attr)var6.item(var7);
                     String var9 = var8.getPrefix();
                     String var10 = var8.getLocalName();
                     String var11 = var8.getValue();
                     if (var9 != null && var9.equals("xmlns")) {
                        String var12 = "xmlns:" + var10;
                        if (!var3.containsKey(var12)) {
                           var3.put(var12, var11);
                        }
                     }

                     if ((var4 != 0 || var5 == Panel.this.document.getDocumentElement()) && var8.getNodeName().equals("xmlns") && !var3.containsKey("xmlns")) {
                        var3.put("xmlns", var8.getNodeValue());
                     }
                  }

                  ++var4;
               }

               Document var13 = DOMViewer.this.panel.document;
               SAXDocumentFactory var14 = new SAXDocumentFactory(var13.getImplementation(), XMLResourceDescriptor.getXMLParserClassName());
               URL var15 = null;
               if (var13 instanceof SVGOMDocument) {
                  var15 = ((SVGOMDocument)var13).getURLObject();
               }

               String var16 = var15 == null ? "" : var15.toString();
               Node var17 = DOMUtilities.parseXML(var1, var13, var16, var3, "svg", var14);
               return (Element)var17.getFirstChild();
            }

            private void selectNewNode(final Element var1) {
               DOMViewer.this.domViewerController.performUpdate(new Runnable() {
                  public void run() {
                     Panel.this.selectNode(var1);
                  }
               });
            }
         });
         this.attributePanelLayout = new GridBagConstraints();
         this.attributePanelLayout.gridx = 1;
         this.attributePanelLayout.gridy = 1;
         this.attributePanelLayout.gridheight = 2;
         this.attributePanelLayout.weightx = 1.0;
         this.attributePanelLayout.weighty = 1.0;
         this.attributePanelLayout.fill = 1;
         this.propertiesTableLayout = new GridBagConstraints();
         this.propertiesTableLayout.gridx = 1;
         this.propertiesTableLayout.gridy = 3;
         this.propertiesTableLayout.weightx = 1.0;
         this.propertiesTableLayout.weighty = 1.0;
         this.propertiesTableLayout.fill = 1;
         this.elementPanel = new JPanel(new GridBagLayout());
         JScrollPane var2 = new JScrollPane();
         var2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 2, 2), BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), DOMViewer.resources.getString("CSSValuesPanel.title")), BorderFactory.createLoweredBevelBorder())));
         var2.getViewport().add(this.propertiesTable);
         this.elementPanel.add(this.attributePanel, this.attributePanelLayout);
         this.elementPanel.add(var2, this.propertiesTableLayout);
         this.characterDataPanel = new CharacterPanel(new BorderLayout());
         this.characterDataPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 2, 2), BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), DOMViewer.resources.getString("CDataPanel.title")), BorderFactory.createLoweredBevelBorder())));
         var2 = new JScrollPane();
         JTextArea var3 = new JTextArea();
         this.characterDataPanel.setTextArea(var3);
         var2.getViewport().add(var3);
         this.characterDataPanel.add(var2);
         var3.setEditable(true);
         var3.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent var1) {
               if (DOMViewer.this.canEdit()) {
                  Node var2 = Panel.this.characterDataPanel.getNode();
                  String var3 = Panel.this.characterDataPanel.getTextArea().getText();
                  switch (var2.getNodeType()) {
                     case 3:
                     case 4:
                     case 8:
                        DOMViewer.this.addChangesToHistory();
                        DOMViewer.this.historyBrowserInterface.setNodeValue(var2, var3);
                  }
               }

            }
         });
         this.documentInfo = new JTextArea();
         this.documentInfoPanel = new JPanel(new BorderLayout());
         this.documentInfoPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 2, 2), BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), DOMViewer.resources.getString("DocumentInfoPanel.title")), BorderFactory.createLoweredBevelBorder())));
         var2 = new JScrollPane();
         var2.getViewport().add(this.documentInfo);
         this.documentInfoPanel.add(var2);
         this.documentInfo.setEditable(false);
         this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), DOMViewer.resources.getString("DOMViewerPanel.title")));
         JToolBar var13 = new JToolBar(DOMViewer.resources.getString("DOMViewerToolbar.name"));
         var13.setFloatable(false);
         JButton var14 = DOMViewer.this.getButtonFactory().createJToolbarButton("UndoButton");
         var14.setDisabledIcon(new ImageIcon(this.getClass().getResource(DOMViewer.resources.getString("UndoButton.disabledIcon"))));
         DropDownComponent var4 = new DropDownComponent(var14);
         var4.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
         var4.setMaximumSize(new Dimension(44, 25));
         var4.setPreferredSize(new Dimension(44, 25));
         var13.add(var4);
         DropDownHistoryModel.UndoPopUpMenuModel var5 = new DropDownHistoryModel.UndoPopUpMenuModel(var4.getPopupMenu(), DOMViewer.this.historyBrowserInterface);
         var4.getPopupMenu().setModel(var5);
         JButton var6 = DOMViewer.this.getButtonFactory().createJToolbarButton("RedoButton");
         var6.setDisabledIcon(new ImageIcon(this.getClass().getResource(DOMViewer.resources.getString("RedoButton.disabledIcon"))));
         DropDownComponent var7 = new DropDownComponent(var6);
         var7.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
         var7.setMaximumSize(new Dimension(44, 25));
         var7.setPreferredSize(new Dimension(44, 25));
         var13.add(var7);
         DropDownHistoryModel.RedoPopUpMenuModel var8 = new DropDownHistoryModel.RedoPopUpMenuModel(var7.getPopupMenu(), DOMViewer.this.historyBrowserInterface);
         var7.getPopupMenu().setModel(var8);
         JToggleButton var9 = DOMViewer.this.getButtonFactory().createJToolbarToggleButton("CapturingClickButton");
         var9.setEnabled(true);
         var9.setPreferredSize(new Dimension(32, 25));
         var13.add(var9);
         DOMViewer.this.overlayButton = DOMViewer.this.getButtonFactory().createJToolbarToggleButton("OverlayButton");
         DOMViewer.this.overlayButton.setEnabled(true);
         DOMViewer.this.overlayButton.setPreferredSize(new Dimension(32, 25));
         var13.add(DOMViewer.this.overlayButton);
         this.add(var13, "North");
         DefaultMutableTreeNode var10 = new DefaultMutableTreeNode(DOMViewer.resources.getString("EmptyDocument.text"));
         this.tree = new DOMDocumentTree(var10, DOMViewer.this.new DOMViewerDOMDocumentTreeController());
         this.tree.setCellRenderer(new NodeRenderer());
         this.tree.putClientProperty("JTree.lineStyle", "Angled");
         this.tree.addListener(new DOMDocumentTree.DOMDocumentTreeAdapter() {
            public void dropCompleted(DOMDocumentTree.DOMDocumentTreeEvent var1) {
               DOMDocumentTree.DropCompletedInfo var2 = (DOMDocumentTree.DropCompletedInfo)var1.getSource();
               DOMViewer.this.addChangesToHistory();
               HistoryBrowserInterface.CompoundUpdateCommand var3 = DOMViewer.this.historyBrowserInterface.createNodesDroppedCommand(var2.getChildren());
               int var4 = var2.getChildren().size();

               for(int var5 = 0; var5 < var4; ++var5) {
                  Node var6 = (Node)var2.getChildren().get(var5);
                  if (!DOMUtilities.isAnyNodeAncestorOf(var2.getChildren(), var6)) {
                     var3.addCommand(DOMViewer.this.historyBrowserInterface.createInsertChildCommand(var2.getParent(), var2.getSibling(), var6));
                  }
               }

               DOMViewer.this.historyBrowserInterface.performCompoundUpdateCommand(var3);
            }
         });
         this.tree.addTreeSelectionListener(new DOMTreeSelectionListener());
         this.tree.addMouseListener(new TreePopUpListener());
         JScrollPane var11 = new JScrollPane();
         var11.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0), BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), DOMViewer.resources.getString("DOMViewer.title")), BorderFactory.createLoweredBevelBorder())));
         var11.getViewport().add(this.tree);
         this.splitPane = new JSplitPane(1, true, var11, this.rightPanel);
         int var12 = DOMViewer.resources.getInteger("SplitPane.dividerLocation");
         this.splitPane.setDividerLocation(var12);
         this.add(this.splitPane);
      }

      public void setDocument(Document var1) {
         this.setDocument(var1, (ViewCSS)null);
      }

      public void setDocument(Document var1, ViewCSS var2) {
         if (this.document != null) {
            if (this.document != var1) {
               this.removeDomMutationListeners(this.document);
               this.addDomMutationListeners(var1);
               this.removeCapturingListener(this.document);
               this.addCapturingListener(var1);
            }
         } else {
            this.addDomMutationListeners(var1);
            this.addCapturingListener(var1);
         }

         DOMViewer.this.resetHistory();
         this.document = var1;
         this.viewCSS = var2;
         MutableTreeNode var3 = this.createTree(var1, DOMViewer.this.showWhitespace);
         ((DefaultTreeModel)this.tree.getModel()).setRoot(var3);
         if (this.rightPanel.getComponentCount() != 0) {
            this.rightPanel.remove(0);
            this.splitPane.revalidate();
            this.splitPane.repaint();
         }

      }

      protected void addDomMutationListeners(Document var1) {
         EventTarget var2 = (EventTarget)var1;
         this.nodeInsertion = new NodeInsertionHandler();
         var2.addEventListener("DOMNodeInserted", this.nodeInsertion, true);
         this.nodeRemoval = new NodeRemovalHandler();
         var2.addEventListener("DOMNodeRemoved", this.nodeRemoval, true);
         this.attrModification = new AttributeModificationHandler();
         var2.addEventListener("DOMAttrModified", this.attrModification, true);
         this.charDataModification = new CharDataModificationHandler();
         var2.addEventListener("DOMCharacterDataModified", this.charDataModification, true);
      }

      protected void removeDomMutationListeners(Document var1) {
         if (var1 != null) {
            EventTarget var2 = (EventTarget)var1;
            var2.removeEventListener("DOMNodeInserted", this.nodeInsertion, true);
            var2.removeEventListener("DOMNodeRemoved", this.nodeRemoval, true);
            var2.removeEventListener("DOMAttrModified", this.attrModification, true);
            var2.removeEventListener("DOMCharacterDataModified", this.charDataModification, true);
         }

      }

      protected void addCapturingListener(Document var1) {
         EventTarget var2 = (EventTarget)var1.getDocumentElement();
         this.capturingListener = new CapturingClickHandler();
         var2.addEventListener("click", this.capturingListener, true);
      }

      protected void removeCapturingListener(Document var1) {
         if (var1 != null) {
            EventTarget var2 = (EventTarget)var1.getDocumentElement();
            var2.removeEventListener("click", this.capturingListener, true);
         }

      }

      protected void refreshGUI(Runnable var1) {
         if (DOMViewer.this.canEdit()) {
            try {
               SwingUtilities.invokeAndWait(var1);
            } catch (InterruptedException var3) {
               var3.printStackTrace();
            } catch (InvocationTargetException var4) {
               var4.printStackTrace();
            }
         }

      }

      protected void registerNodeInserted(MutationEvent var1) {
         Node var2 = (Node)var1.getTarget();
         DOMViewer.this.historyBrowserInterface.addToCurrentCompoundCommand(DOMViewer.this.historyBrowserInterface.createNodeInsertedCommand(var2.getParentNode(), var2.getNextSibling(), var2));
      }

      protected void registerNodeRemoved(MutationEvent var1) {
         Node var2 = (Node)var1.getTarget();
         DOMViewer.this.historyBrowserInterface.addToCurrentCompoundCommand(DOMViewer.this.historyBrowserInterface.createNodeRemovedCommand(var1.getRelatedNode(), var2.getNextSibling(), var2));
      }

      protected void registerAttributeAdded(MutationEvent var1) {
         Element var2 = (Element)var1.getTarget();
         DOMViewer.this.historyBrowserInterface.addToCurrentCompoundCommand(DOMViewer.this.historyBrowserInterface.createAttributeAddedCommand(var2, var1.getAttrName(), var1.getNewValue(), (String)null));
      }

      protected void registerAttributeRemoved(MutationEvent var1) {
         Element var2 = (Element)var1.getTarget();
         DOMViewer.this.historyBrowserInterface.addToCurrentCompoundCommand(DOMViewer.this.historyBrowserInterface.createAttributeRemovedCommand(var2, var1.getAttrName(), var1.getPrevValue(), (String)null));
      }

      protected void registerAttributeModified(MutationEvent var1) {
         Element var2 = (Element)var1.getTarget();
         DOMViewer.this.historyBrowserInterface.addToCurrentCompoundCommand(DOMViewer.this.historyBrowserInterface.createAttributeModifiedCommand(var2, var1.getAttrName(), var1.getPrevValue(), var1.getNewValue(), (String)null));
      }

      protected void registerAttributeChanged(MutationEvent var1) {
         switch (var1.getAttrChange()) {
            case 1:
               this.registerAttributeModified(var1);
               break;
            case 2:
               this.registerAttributeAdded(var1);
               break;
            case 3:
               this.registerAttributeRemoved(var1);
               break;
            default:
               this.registerAttributeModified(var1);
         }

      }

      protected void registerCharDataModified(MutationEvent var1) {
         Node var2 = (Node)var1.getTarget();
         DOMViewer.this.historyBrowserInterface.addToCurrentCompoundCommand(DOMViewer.this.historyBrowserInterface.createCharDataModifiedCommand(var2, var1.getPrevValue(), var1.getNewValue()));
      }

      protected boolean shouldRegisterDocumentChange() {
         return DOMViewer.this.canEdit() && DOMViewer.this.historyBrowserInterface.getHistoryBrowser().getState() == 4;
      }

      protected void registerDocumentChange(MutationEvent var1) {
         if (this.shouldRegisterDocumentChange()) {
            String var2 = var1.getType();
            if (var2.equals("DOMNodeInserted")) {
               this.registerNodeInserted(var1);
            } else if (var2.equals("DOMNodeRemoved")) {
               this.registerNodeRemoved(var1);
            } else if (var2.equals("DOMAttrModified")) {
               this.registerAttributeChanged(var1);
            } else if (var2.equals("DOMCharacterDataModified")) {
               this.registerCharDataModified(var1);
            }
         }

      }

      protected MutableTreeNode createTree(Node var1, boolean var2) {
         DefaultMutableTreeNode var3 = new DefaultMutableTreeNode(new NodeInfo(var1));

         for(Node var4 = var1.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
            if (!var2 && var4 instanceof Text) {
               String var5 = var4.getNodeValue();
               if (var5.trim().length() == 0) {
                  continue;
               }
            }

            var3.add(this.createTree(var4, var2));
         }

         if (var1 instanceof NodeXBL) {
            Element var11 = ((NodeXBL)var1).getXblShadowTree();
            if (var11 != null) {
               DefaultMutableTreeNode var13 = new DefaultMutableTreeNode(new ShadowNodeInfo(var11));
               var13.add(this.createTree(var11, var2));
               var3.add(var13);
            }
         }

         if (var1 instanceof XBLOMContentElement) {
            AbstractDocument var12 = (AbstractDocument)var1.getOwnerDocument();
            XBLManager var14 = var12.getXBLManager();
            if (var14 instanceof DefaultXBLManager) {
               DefaultMutableTreeNode var6 = new DefaultMutableTreeNode(new ContentNodeInfo(var1));
               DefaultXBLManager var7 = (DefaultXBLManager)var14;
               ContentManager var8 = var7.getContentManager(var1);
               if (var8 != null) {
                  NodeList var9 = var8.getSelectedContent((XBLOMContentElement)var1);

                  for(int var10 = 0; var10 < var9.getLength(); ++var10) {
                     var6.add(this.createTree(var9.item(var10), var2));
                  }

                  var3.add(var6);
               }
            }
         }

         return var3;
      }

      protected DefaultMutableTreeNode findNode(JTree var1, Node var2) {
         DefaultMutableTreeNode var3 = (DefaultMutableTreeNode)var1.getModel().getRoot();
         Enumeration var4 = var3.breadthFirstEnumeration();

         DefaultMutableTreeNode var5;
         NodeInfo var6;
         do {
            if (!var4.hasMoreElements()) {
               return null;
            }

            var5 = (DefaultMutableTreeNode)var4.nextElement();
            var6 = (NodeInfo)var5.getUserObject();
         } while(var6.getNode() != var2);

         return var5;
      }

      public void selectNode(final Node var1) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               DefaultMutableTreeNode var1x = Panel.this.findNode(Panel.this.tree, var1);
               if (var1x != null) {
                  TreeNode[] var2 = var1x.getPath();
                  TreePath var3 = new TreePath(var2);
                  Panel.this.tree.setSelectionPath(var3);
                  Panel.this.tree.scrollPathToVisible(var3);
               }

            }
         });
      }

      protected JMenu createTemplatesMenu(String var1) {
         NodeTemplates var2 = new NodeTemplates();
         JMenu var3 = new JMenu(var1);
         HashMap var4 = new HashMap();
         ArrayList var5 = var2.getCategories();
         int var6 = var5.size();

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5.get(var7).toString();
            JMenu var9 = new JMenu(var8);
            var3.add(var9);
            var4.put(var8, var9);
         }

         ArrayList var15 = new ArrayList(var2.getNodeTemplatesMap().values());
         Collections.sort(var15, new Comparator() {
            public int compare(Object var1, Object var2) {
               NodeTemplates.NodeTemplateDescriptor var3 = (NodeTemplates.NodeTemplateDescriptor)var1;
               NodeTemplates.NodeTemplateDescriptor var4 = (NodeTemplates.NodeTemplateDescriptor)var2;
               return var3.getName().compareTo(var4.getName());
            }
         });
         Iterator var16 = var15.iterator();

         while(var16.hasNext()) {
            NodeTemplates.NodeTemplateDescriptor var17 = (NodeTemplates.NodeTemplateDescriptor)var16.next();
            String var10 = var17.getXmlValue();
            short var11 = var17.getType();
            String var12 = var17.getCategory();
            JMenuItem var13 = new JMenuItem(var17.getName());
            var13.addActionListener(new NodeTemplateParser(var10, var11));
            JMenu var14 = (JMenu)var4.get(var12);
            var14.add(var13);
         }

         return var3;
      }

      protected class NodeCSSValuesModel extends AbstractTableModel {
         protected Node node;
         protected CSSStyleDeclaration style;
         protected List propertyNames;

         public NodeCSSValuesModel(Node var2) {
            this.node = var2;
            if (Panel.this.viewCSS != null) {
               this.style = Panel.this.viewCSS.getComputedStyle((Element)var2, (String)null);
               this.propertyNames = new ArrayList();
               if (this.style != null) {
                  for(int var3 = 0; var3 < this.style.getLength(); ++var3) {
                     this.propertyNames.add(this.style.item(var3));
                  }

                  Collections.sort(this.propertyNames);
               }
            }

         }

         public String getColumnName(int var1) {
            return var1 == 0 ? DOMViewer.resources.getString("CSSValuesTable.column1") : DOMViewer.resources.getString("CSSValuesTable.column2");
         }

         public int getColumnCount() {
            return 2;
         }

         public int getRowCount() {
            return this.style == null ? 0 : this.style.getLength();
         }

         public boolean isCellEditable(int var1, int var2) {
            return false;
         }

         public Object getValueAt(int var1, int var2) {
            String var3 = (String)this.propertyNames.get(var1);
            return var2 == 0 ? var3 : this.style.getPropertyValue(var3);
         }
      }

      protected class NodeRenderer extends DefaultTreeCellRenderer {
         protected ImageIcon elementIcon;
         protected ImageIcon commentIcon;
         protected ImageIcon piIcon;
         protected ImageIcon textIcon;

         public NodeRenderer() {
            String var2 = DOMViewer.resources.getString("Element.icon");
            this.elementIcon = new ImageIcon(this.getClass().getResource(var2));
            var2 = DOMViewer.resources.getString("Comment.icon");
            this.commentIcon = new ImageIcon(this.getClass().getResource(var2));
            var2 = DOMViewer.resources.getString("PI.icon");
            this.piIcon = new ImageIcon(this.getClass().getResource(var2));
            var2 = DOMViewer.resources.getString("Text.icon");
            this.textIcon = new ImageIcon(this.getClass().getResource(var2));
         }

         public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
            super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
            switch (this.getNodeType(var2)) {
               case 1:
                  this.setIcon(this.elementIcon);
               case 2:
               case 5:
               case 6:
               default:
                  break;
               case 3:
               case 4:
                  this.setIcon(this.textIcon);
                  break;
               case 7:
                  this.setIcon(this.piIcon);
                  break;
               case 8:
                  this.setIcon(this.commentIcon);
            }

            return this;
         }

         protected short getNodeType(Object var1) {
            DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)var1;
            Object var3 = var2.getUserObject();
            if (var3 instanceof NodeInfo) {
               Node var4 = ((NodeInfo)var3).getNode();
               return var4.getNodeType();
            } else {
               return -1;
            }
         }
      }

      protected class DOMTreeSelectionListener implements TreeSelectionListener {
         public void valueChanged(TreeSelectionEvent var1) {
            if (DOMViewer.this.elementOverlayManager != null) {
               this.handleElementSelection(var1);
            }

            DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)Panel.this.tree.getLastSelectedPathComponent();
            if (var2 != null) {
               if (Panel.this.rightPanel.getComponentCount() != 0) {
                  Panel.this.rightPanel.remove(0);
               }

               Object var3 = var2.getUserObject();
               if (var3 instanceof NodeInfo) {
                  Node var4 = ((NodeInfo)var3).getNode();
                  switch (var4.getNodeType()) {
                     case 1:
                        Panel.this.propertiesTable.setModel(Panel.this.new NodeCSSValuesModel(var4));
                        Panel.this.attributePanel.promptForChanges();
                        Panel.this.attributePanel.setPreviewElement((Element)var4);
                        Panel.this.rightPanel.add(Panel.this.elementPanel);
                     case 2:
                     case 5:
                     case 6:
                     case 7:
                     default:
                        break;
                     case 3:
                     case 4:
                     case 8:
                        Panel.this.characterDataPanel.setNode(var4);
                        Panel.this.characterDataPanel.getTextArea().setText(var4.getNodeValue());
                        Panel.this.rightPanel.add(Panel.this.characterDataPanel);
                        break;
                     case 9:
                        Panel.this.documentInfo.setText(this.createDocumentText((Document)var4));
                        Panel.this.rightPanel.add(Panel.this.documentInfoPanel);
                  }
               }

               Panel.this.splitPane.revalidate();
               Panel.this.splitPane.repaint();
            }
         }

         protected String createDocumentText(Document var1) {
            StringBuffer var2 = new StringBuffer();
            var2.append("Nodes: ");
            var2.append(this.nodeCount(var1));
            return var2.toString();
         }

         protected int nodeCount(Node var1) {
            int var2 = 1;

            for(Node var3 = var1.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
               var2 += this.nodeCount(var3);
            }

            return var2;
         }

         protected void handleElementSelection(TreeSelectionEvent var1) {
            TreePath[] var2 = var1.getPaths();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               TreePath var4 = var2[var3];
               DefaultMutableTreeNode var5 = (DefaultMutableTreeNode)var4.getLastPathComponent();
               Object var6 = var5.getUserObject();
               if (var6 instanceof NodeInfo) {
                  Node var7 = ((NodeInfo)var6).getNode();
                  if (var7.getNodeType() == 1) {
                     if (var1.isAddedPath(var4)) {
                        DOMViewer.this.elementOverlayManager.addElement((Element)var7);
                     } else {
                        DOMViewer.this.elementOverlayManager.removeElement((Element)var7);
                     }
                  }
               }
            }

            DOMViewer.this.elementOverlayManager.repaint();
         }
      }

      protected class TreeNodeRemover implements ActionListener {
         public void actionPerformed(ActionEvent var1) {
            DOMViewer.this.addChangesToHistory();
            HistoryBrowserInterface.CompoundUpdateCommand var2 = DOMViewer.this.historyBrowserInterface.createRemoveSelectedTreeNodesCommand((ArrayList)null);
            TreePath[] var3 = Panel.this.tree.getSelectionPaths();

            for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
               TreePath var5 = var3[var4];
               DefaultMutableTreeNode var6 = (DefaultMutableTreeNode)var5.getLastPathComponent();
               NodeInfo var7 = (NodeInfo)var6.getUserObject();
               if (DOMUtilities.isParentOf(var7.getNode(), var7.getNode().getParentNode())) {
                  var2.addCommand(DOMViewer.this.historyBrowserInterface.createRemoveChildCommand(var7.getNode().getParentNode(), var7.getNode()));
               }
            }

            DOMViewer.this.historyBrowserInterface.performCompoundUpdateCommand(var2);
         }
      }

      protected class NodeTemplateParser implements ActionListener {
         protected String toParse;
         protected short nodeType;

         public NodeTemplateParser(String var2, short var3) {
            this.toParse = var2;
            this.nodeType = var3;
         }

         public void actionPerformed(ActionEvent var1) {
            Object var2 = null;
            switch (this.nodeType) {
               case 1:
                  URL var3 = null;
                  if (Panel.this.document instanceof SVGOMDocument) {
                     var3 = ((SVGOMDocument)Panel.this.document).getURLObject();
                  }

                  String var4 = var3 == null ? "" : var3.toString();
                  HashMap var5 = new HashMap();
                  var5.put("xmlns", "http://www.w3.org/2000/svg");
                  var5.put("xmlns:xlink", "http://www.w3.org/1999/xlink");
                  SAXDocumentFactory var6 = new SAXDocumentFactory(Panel.this.document.getImplementation(), XMLResourceDescriptor.getXMLParserClassName());
                  DocumentFragment var7 = (DocumentFragment)DOMUtilities.parseXML(this.toParse, Panel.this.document, var4, var5, "svg", var6);
                  var2 = var7.getFirstChild();
               case 2:
               case 5:
               case 6:
               case 7:
               default:
                  break;
               case 3:
                  var2 = Panel.this.document.createTextNode(this.toParse);
                  break;
               case 4:
                  var2 = Panel.this.document.createCDATASection(this.toParse);
                  break;
               case 8:
                  var2 = Panel.this.document.createComment(this.toParse);
            }

            TreePath[] var8 = Panel.this.tree.getSelectionPaths();
            if (var8 != null) {
               TreePath var9 = var8[var8.length - 1];
               DefaultMutableTreeNode var10 = (DefaultMutableTreeNode)var9.getLastPathComponent();
               NodeInfo var11 = (NodeInfo)var10.getUserObject();
               DOMViewer.this.addChangesToHistory();
               DOMViewer.this.historyBrowserInterface.appendChild(var11.getNode(), (Node)var2);
            }

         }
      }

      protected class TreeNodeAdder implements ActionListener {
         public void actionPerformed(ActionEvent var1) {
            NodePickerPanel.NameEditorDialog var2 = new NodePickerPanel.NameEditorDialog(DOMViewer.this);
            var2.setLocationRelativeTo(DOMViewer.this);
            int var3 = var2.showDialog();
            if (var3 == 0) {
               Element var4 = Panel.this.document.createElementNS("http://www.w3.org/2000/svg", var2.getResults());
               if (Panel.this.rightPanel.getComponentCount() != 0) {
                  Panel.this.rightPanel.remove(0);
               }

               Panel.this.rightPanel.add(Panel.this.elementPanel);
               TreePath[] var5 = Panel.this.tree.getSelectionPaths();
               if (var5 != null) {
                  TreePath var6 = var5[var5.length - 1];
                  DefaultMutableTreeNode var7 = (DefaultMutableTreeNode)var6.getLastPathComponent();
                  NodeInfo var8 = (NodeInfo)var7.getUserObject();
                  Panel.this.attributePanel.enterAddNewElementMode(var4, var8.getNode());
               }
            }

         }
      }

      protected class TreePopUpListener extends MouseAdapter {
         protected JPopupMenu treePopupMenu = new JPopupMenu();

         public TreePopUpListener() {
            this.treePopupMenu.add(Panel.this.createTemplatesMenu(DOMViewer.resources.getString("ContextMenuItem.insertNewNode")));
            JMenuItem var2 = new JMenuItem(DOMViewer.resources.getString("ContextMenuItem.createNewElement"));
            this.treePopupMenu.add(var2);
            var2.addActionListener(Panel.this.new TreeNodeAdder());
            var2 = new JMenuItem(DOMViewer.resources.getString("ContextMenuItem.removeSelection"));
            var2.addActionListener(Panel.this.new TreeNodeRemover());
            this.treePopupMenu.add(var2);
         }

         public void mouseReleased(MouseEvent var1) {
            if (var1.isPopupTrigger() && var1.getClickCount() == 1 && Panel.this.tree.getSelectionPaths() != null) {
               this.showPopUp(var1);
            }

         }

         public void mousePressed(MouseEvent var1) {
            JTree var2 = (JTree)var1.getSource();
            TreePath var3 = var2.getPathForLocation(var1.getX(), var1.getY());
            if (!var1.isControlDown() && !var1.isShiftDown()) {
               var2.setSelectionPath(var3);
            } else {
               var2.addSelectionPath(var3);
            }

            if (var1.isPopupTrigger() && var1.getClickCount() == 1) {
               this.showPopUp(var1);
            }

         }

         private void showPopUp(MouseEvent var1) {
            if (DOMViewer.this.canEdit()) {
               TreePath var2 = Panel.this.tree.getPathForLocation(var1.getX(), var1.getY());
               if (var2 != null && var2.getPathCount() > 1) {
                  this.treePopupMenu.show((Component)var1.getSource(), var1.getX(), var1.getY());
               }
            }

         }
      }

      protected class CapturingClickHandler implements EventListener {
         public void handleEvent(Event var1) {
            if (DOMViewer.this.isCapturingClickEnabled) {
               Element var2 = (Element)var1.getTarget();
               Panel.this.selectNode(var2);
            }

         }
      }

      protected class CharDataModificationHandler implements EventListener {
         public void handleEvent(final Event var1) {
            Runnable var2 = new Runnable() {
               public void run() {
                  MutationEvent var1x = (MutationEvent)var1;
                  Node var2 = (Node)var1x.getTarget();
                  if (Panel.this.characterDataPanel.getNode() == var2) {
                     Panel.this.characterDataPanel.getTextArea().setText(var2.getNodeValue());
                     Panel.this.attributePanel.updateOnDocumentChange(var1x.getType(), var2);
                  }

               }
            };
            Panel.this.refreshGUI(var2);
            if (Panel.this.characterDataPanel.getNode() == var1.getTarget()) {
               Panel.this.registerDocumentChange((MutationEvent)var1);
            }

         }
      }

      protected class AttributeModificationHandler implements EventListener {
         public void handleEvent(final Event var1) {
            Runnable var2 = new Runnable() {
               public void run() {
                  MutationEvent var1x = (MutationEvent)var1;
                  Element var2 = (Element)var1x.getTarget();
                  DefaultTreeModel var3 = (DefaultTreeModel)Panel.this.tree.getModel();
                  var3.nodeChanged(Panel.this.findNode(Panel.this.tree, var2));
                  Panel.this.attributePanel.updateOnDocumentChange(var1x.getType(), var2);
               }
            };
            Panel.this.refreshGUI(var2);
            Panel.this.registerDocumentChange((MutationEvent)var1);
         }
      }

      protected class NodeRemovalHandler implements EventListener {
         public void handleEvent(final Event var1) {
            Runnable var2 = new Runnable() {
               public void run() {
                  MutationEvent var1x = (MutationEvent)var1;
                  Node var2 = (Node)var1x.getTarget();
                  DefaultMutableTreeNode var3 = Panel.this.findNode(Panel.this.tree, var2);
                  DefaultTreeModel var4 = (DefaultTreeModel)Panel.this.tree.getModel();
                  if (var3 != null) {
                     var4.removeNodeFromParent(var3);
                  }

                  Panel.this.attributePanel.updateOnDocumentChange(var1x.getType(), var2);
               }
            };
            Panel.this.refreshGUI(var2);
            Panel.this.registerDocumentChange((MutationEvent)var1);
         }
      }

      protected class NodeInsertionHandler implements EventListener {
         public void handleEvent(final Event var1) {
            Runnable var2 = new Runnable() {
               public void run() {
                  MutationEvent var1x = (MutationEvent)var1;
                  Node var2 = (Node)var1x.getTarget();
                  DefaultMutableTreeNode var3 = Panel.this.findNode(Panel.this.tree, var2.getParentNode());
                  DefaultMutableTreeNode var4 = (DefaultMutableTreeNode)Panel.this.createTree(var2, DOMViewer.this.showWhitespace);
                  DefaultTreeModel var5 = (DefaultTreeModel)Panel.this.tree.getModel();
                  DefaultMutableTreeNode var6 = (DefaultMutableTreeNode)Panel.this.createTree(var2.getParentNode(), DOMViewer.this.showWhitespace);
                  int var7 = NodeInsertionHandler.this.findIndexToInsert(var3, var6);
                  if (var7 != -1) {
                     var5.insertNodeInto(var4, var3, var7);
                  }

                  Panel.this.attributePanel.updateOnDocumentChange(var1x.getType(), var2);
               }
            };
            Panel.this.refreshGUI(var2);
            Panel.this.registerDocumentChange((MutationEvent)var1);
         }

         protected int findIndexToInsert(DefaultMutableTreeNode var1, DefaultMutableTreeNode var2) {
            byte var3 = -1;
            if (var1 != null && var2 != null) {
               Enumeration var4 = var1.children();
               Enumeration var5 = var2.children();

               int var6;
               for(var6 = 0; var4.hasMoreElements(); ++var6) {
                  DefaultMutableTreeNode var7 = (DefaultMutableTreeNode)var4.nextElement();
                  DefaultMutableTreeNode var8 = (DefaultMutableTreeNode)var5.nextElement();
                  Node var9 = ((NodeInfo)var7.getUserObject()).getNode();
                  Node var10 = ((NodeInfo)var8.getUserObject()).getNode();
                  if (var9 != var10) {
                     return var6;
                  }
               }

               return var6;
            } else {
               return var3;
            }
         }
      }

      protected class CharacterPanel extends JPanel {
         protected Node node;
         protected JTextArea textArea = new JTextArea();

         public CharacterPanel(BorderLayout var2) {
            super(var2);
         }

         public JTextArea getTextArea() {
            return this.textArea;
         }

         public void setTextArea(JTextArea var1) {
            this.textArea = var1;
         }

         public Node getNode() {
            return this.node;
         }

         public void setNode(Node var1) {
            this.node = var1;
         }
      }
   }

   protected class DOMViewerElementOverlayController implements ElementOverlayController {
      public boolean isOverlayEnabled() {
         return DOMViewer.this.canEdit() && DOMViewer.this.isElementOverlayEnabled;
      }
   }

   protected class DOMViewerDOMDocumentTreeController implements DOMDocumentTreeController {
      public boolean isDNDSupported() {
         return DOMViewer.this.canEdit();
      }
   }

   protected class DOMViewerNodePickerController implements NodePickerController {
      public boolean isEditable() {
         return DOMViewer.this.canEdit();
      }

      public boolean canEdit(Element var1) {
         if (DOMViewer.this.panel != null && DOMViewer.this.panel.document != null) {
         }

         return true;
      }
   }

   protected class OverlayButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         DOMViewer.this.toggleOverlay();
      }
   }

   protected class CapturingClickButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         JToggleButton var2 = (JToggleButton)var1.getSource();
         DOMViewer.this.isCapturingClickEnabled = var2.isSelected();
         if (!DOMViewer.this.isCapturingClickEnabled) {
            var2.setToolTipText(DOMViewer.resources.getString("CapturingClickButton.tooltip"));
         } else {
            var2.setToolTipText(DOMViewer.resources.getString("CapturingClickButton.disableText"));
         }

      }
   }

   protected class RedoButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         DOMViewer.this.addChangesToHistory();
         DOMViewer.this.historyBrowserInterface.getHistoryBrowser().redo();
      }
   }

   protected class UndoButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         DOMViewer.this.addChangesToHistory();
         DOMViewer.this.historyBrowserInterface.getHistoryBrowser().undo();
      }
   }

   protected class CloseButtonAction extends AbstractAction {
      public void actionPerformed(ActionEvent var1) {
         if (DOMViewer.this.panel.attributePanel.panelHiding()) {
            DOMViewer.this.panel.tree.setSelectionRow(0);
            DOMViewer.this.dispose();
         }

      }
   }
}
