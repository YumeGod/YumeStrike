package org.apache.batik.apps.svgbrowser;

import java.util.ArrayList;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HistoryBrowserInterface {
   private static final String ATTRIBUTE_ADDED_COMMAND = "Attribute added: ";
   private static final String ATTRIBUTE_REMOVED_COMMAND = "Attribute removed: ";
   private static final String ATTRIBUTE_MODIFIED_COMMAND = "Attribute modified: ";
   private static final String NODE_INSERTED_COMMAND = "Node inserted: ";
   private static final String NODE_REMOVED_COMMAND = "Node removed: ";
   private static final String CHAR_DATA_MODIFIED_COMMAND = "Node value changed: ";
   private static final String OUTER_EDIT_COMMAND = "Document changed outside DOM Viewer";
   private static final String COMPOUND_TREE_NODE_DROP = "Node moved";
   private static final String REMOVE_SELECTED_NODES = "Nodes removed";
   protected HistoryBrowser historyBrowser;
   protected AbstractCompoundCommand currentCompoundCommand;

   public HistoryBrowserInterface(HistoryBrowser.CommandController var1) {
      this.historyBrowser = new HistoryBrowser(var1);
   }

   public void setCommmandController(HistoryBrowser.CommandController var1) {
      this.historyBrowser.setCommandController(var1);
   }

   public CompoundUpdateCommand createCompoundUpdateCommand(String var1) {
      CompoundUpdateCommand var2 = new CompoundUpdateCommand(var1);
      return var2;
   }

   public CompoundUpdateCommand createNodeChangedCommand(Node var1) {
      return new CompoundUpdateCommand(this.getNodeChangedCommandName(var1));
   }

   public CompoundUpdateCommand createNodesDroppedCommand(ArrayList var1) {
      return new CompoundUpdateCommand("Node moved");
   }

   public CompoundUpdateCommand createRemoveSelectedTreeNodesCommand(ArrayList var1) {
      return new CompoundUpdateCommand("Nodes removed");
   }

   public void performCompoundUpdateCommand(UndoableCommand var1) {
      this.historyBrowser.addCommand(var1);
   }

   public HistoryBrowser getHistoryBrowser() {
      return this.historyBrowser;
   }

   public void nodeInserted(Node var1, Node var2, Node var3) {
      this.historyBrowser.addCommand(this.createNodeInsertedCommand(var1, var2, var3));
   }

   public NodeInsertedCommand createNodeInsertedCommand(Node var1, Node var2, Node var3) {
      return new NodeInsertedCommand("Node inserted: " + this.getBracketedNodeName(var3), var1, var2, var3);
   }

   public void nodeRemoved(Node var1, Node var2, Node var3) {
      this.historyBrowser.addCommand(this.createNodeRemovedCommand(var1, var2, var3));
   }

   public NodeRemovedCommand createNodeRemovedCommand(Node var1, Node var2, Node var3) {
      return new NodeRemovedCommand("Node removed: " + this.getBracketedNodeName(var3), var1, var2, var3);
   }

   public void attributeAdded(Element var1, String var2, String var3, String var4) {
      this.historyBrowser.addCommand(this.createAttributeAddedCommand(var1, var2, var3, var4));
   }

   public AttributeAddedCommand createAttributeAddedCommand(Element var1, String var2, String var3, String var4) {
      return new AttributeAddedCommand("Attribute added: " + this.getBracketedNodeName(var1), var1, var2, var3, var4);
   }

   public void attributeRemoved(Element var1, String var2, String var3, String var4) {
      this.historyBrowser.addCommand(this.createAttributeRemovedCommand(var1, var2, var3, var4));
   }

   public AttributeRemovedCommand createAttributeRemovedCommand(Element var1, String var2, String var3, String var4) {
      return new AttributeRemovedCommand("Attribute removed: " + this.getBracketedNodeName(var1), var1, var2, var3, var4);
   }

   public void attributeModified(Element var1, String var2, String var3, String var4, String var5) {
      this.historyBrowser.addCommand(this.createAttributeModifiedCommand(var1, var2, var3, var4, var5));
   }

   public AttributeModifiedCommand createAttributeModifiedCommand(Element var1, String var2, String var3, String var4, String var5) {
      return new AttributeModifiedCommand("Attribute modified: " + this.getBracketedNodeName(var1), var1, var2, var3, var4, var5);
   }

   public void charDataModified(Node var1, String var2, String var3) {
      this.historyBrowser.addCommand(this.createCharDataModifiedCommand(var1, var2, var3));
   }

   public CharDataModifiedCommand createCharDataModifiedCommand(Node var1, String var2, String var3) {
      return new CharDataModifiedCommand("Node value changed: " + this.getBracketedNodeName(var1), var1, var2, var3);
   }

   public void appendChild(Node var1, Node var2) {
      this.historyBrowser.addCommand(this.createAppendChildCommand(var1, var2));
   }

   public AppendChildCommand createAppendChildCommand(Node var1, Node var2) {
      return new AppendChildCommand(this.getAppendChildCommandName(var1, var2), var1, var2);
   }

   public void insertChildBefore(Node var1, Node var2, Node var3) {
      if (var2 == null) {
         this.historyBrowser.addCommand(this.createAppendChildCommand(var1, var3));
      } else {
         this.historyBrowser.addCommand(this.createInsertNodeBeforeCommand(var1, var2, var3));
      }

   }

   public UndoableCommand createInsertChildCommand(Node var1, Node var2, Node var3) {
      return (UndoableCommand)(var2 == null ? this.createAppendChildCommand(var1, var3) : this.createInsertNodeBeforeCommand(var1, var2, var3));
   }

   public InsertNodeBeforeCommand createInsertNodeBeforeCommand(Node var1, Node var2, Node var3) {
      return new InsertNodeBeforeCommand(this.getInsertBeforeCommandName(var1, var3, var2), var1, var2, var3);
   }

   public void replaceChild(Node var1, Node var2, Node var3) {
   }

   public void removeChild(Node var1, Node var2) {
      this.historyBrowser.addCommand(this.createRemoveChildCommand(var1, var2));
   }

   public RemoveChildCommand createRemoveChildCommand(Node var1, Node var2) {
      return new RemoveChildCommand(this.getRemoveChildCommandName(var1, var2), var1, var2);
   }

   public void setNodeValue(Node var1, String var2) {
      this.historyBrowser.addCommand(this.createChangeNodeValueCommand(var1, var2));
   }

   public ChangeNodeValueCommand createChangeNodeValueCommand(Node var1, String var2) {
      return new ChangeNodeValueCommand(this.getChangeNodeValueCommandName(var1, var2), var1, var2);
   }

   public AbstractCompoundCommand getCurrentCompoundCommand() {
      if (this.currentCompoundCommand == null) {
         this.currentCompoundCommand = this.createCompoundUpdateCommand("Document changed outside DOM Viewer");
      }

      return this.currentCompoundCommand;
   }

   public void addToCurrentCompoundCommand(AbstractUndoableCommand var1) {
      this.getCurrentCompoundCommand().addCommand(var1);
      this.historyBrowser.fireDoCompoundEdit(new HistoryBrowser.HistoryBrowserEvent(this.getCurrentCompoundCommand()));
   }

   public void performCurrentCompoundCommand() {
      if (this.getCurrentCompoundCommand().getCommandNumber() > 0) {
         this.historyBrowser.addCommand(this.getCurrentCompoundCommand());
         this.historyBrowser.fireCompoundEditPerformed(new HistoryBrowser.HistoryBrowserEvent(this.currentCompoundCommand));
         this.currentCompoundCommand = null;
      }

   }

   private String getNodeAsString(Node var1) {
      String var2 = "";
      if (var1.getNodeType() == 1) {
         Element var3 = (Element)var1;
         var2 = var3.getAttributeNS((String)null, "id");
      }

      return var2.length() != 0 ? var1.getNodeName() + " \"" + var2 + "\"" : var1.getNodeName();
   }

   private String getBracketedNodeName(Node var1) {
      return "(" + this.getNodeAsString(var1) + ")";
   }

   private String getAppendChildCommandName(Node var1, Node var2) {
      return "Append " + this.getNodeAsString(var2) + " to " + this.getNodeAsString(var1);
   }

   private String getInsertBeforeCommandName(Node var1, Node var2, Node var3) {
      return "Insert " + this.getNodeAsString(var2) + " to " + this.getNodeAsString(var1) + " before " + this.getNodeAsString(var3);
   }

   private String getRemoveChildCommandName(Node var1, Node var2) {
      return "Remove " + this.getNodeAsString(var2) + " from " + this.getNodeAsString(var1);
   }

   private String getChangeNodeValueCommandName(Node var1, String var2) {
      return "Change " + this.getNodeAsString(var1) + " value to " + var2;
   }

   private String getNodeChangedCommandName(Node var1) {
      return "Node " + this.getNodeAsString(var1) + " changed";
   }

   public static class ChangeNodeValueCommand extends AbstractUndoableCommand {
      protected Node contextNode;
      protected String newValue;

      public ChangeNodeValueCommand(String var1, Node var2, String var3) {
         this.setName(var1);
         this.contextNode = var2;
         this.newValue = var3;
      }

      public void execute() {
         String var1 = this.contextNode.getNodeValue();
         this.contextNode.setNodeValue(this.newValue);
         this.newValue = var1;
      }

      public void undo() {
         this.execute();
      }

      public void redo() {
         this.execute();
      }

      public boolean shouldExecute() {
         return this.contextNode != null;
      }
   }

   public static class RemoveChildCommand extends AbstractUndoableCommand {
      protected Node parentNode;
      protected Node childNode;
      protected int indexInChildrenArray;

      public RemoveChildCommand(String var1, Node var2, Node var3) {
         this.setName(var1);
         this.parentNode = var2;
         this.childNode = var3;
      }

      public void execute() {
         this.indexInChildrenArray = DOMUtilities.getChildIndex(this.childNode, this.parentNode);
         this.parentNode.removeChild(this.childNode);
      }

      public void undo() {
         Node var1 = this.parentNode.getChildNodes().item(this.indexInChildrenArray);
         this.parentNode.insertBefore(this.childNode, var1);
      }

      public void redo() {
         this.parentNode.removeChild(this.childNode);
      }

      public boolean shouldExecute() {
         return this.parentNode != null && this.childNode != null;
      }
   }

   public static class ReplaceChildCommand extends AbstractUndoableCommand {
      protected Node oldParent;
      protected Node oldNextSibling;
      protected Node newNextSibling;
      protected Node parent;
      protected Node child;

      public ReplaceChildCommand(String var1, Node var2, Node var3, Node var4) {
         this.setName(var1);
         this.oldParent = var4.getParentNode();
         this.oldNextSibling = var4.getNextSibling();
         this.parent = var2;
         this.child = var4;
         this.newNextSibling = var3;
      }

      public void execute() {
         if (this.newNextSibling != null) {
            this.parent.insertBefore(this.child, this.newNextSibling);
         } else {
            this.parent.appendChild(this.child);
         }

      }

      public void undo() {
         if (this.oldParent != null) {
            this.oldParent.insertBefore(this.child, this.oldNextSibling);
         } else {
            this.parent.removeChild(this.child);
         }

      }

      public void redo() {
         this.execute();
      }

      public boolean shouldExecute() {
         return this.parent != null && this.child != null;
      }
   }

   public static class InsertNodeBeforeCommand extends AbstractUndoableCommand {
      protected Node oldParent;
      protected Node oldNextSibling;
      protected Node newNextSibling;
      protected Node parent;
      protected Node child;

      public InsertNodeBeforeCommand(String var1, Node var2, Node var3, Node var4) {
         this.setName(var1);
         this.oldParent = var4.getParentNode();
         this.oldNextSibling = var4.getNextSibling();
         this.parent = var2;
         this.child = var4;
         this.newNextSibling = var3;
      }

      public void execute() {
         if (this.newNextSibling != null) {
            this.parent.insertBefore(this.child, this.newNextSibling);
         } else {
            this.parent.appendChild(this.child);
         }

      }

      public void undo() {
         if (this.oldParent != null) {
            this.oldParent.insertBefore(this.child, this.oldNextSibling);
         } else {
            this.parent.removeChild(this.child);
         }

      }

      public void redo() {
         this.execute();
      }

      public boolean shouldExecute() {
         return this.parent != null && this.child != null;
      }
   }

   public static class AppendChildCommand extends AbstractUndoableCommand {
      protected Node oldParentNode;
      protected Node oldNextSibling;
      protected Node parentNode;
      protected Node childNode;

      public AppendChildCommand(String var1, Node var2, Node var3) {
         this.setName(var1);
         this.oldParentNode = var3.getParentNode();
         this.oldNextSibling = var3.getNextSibling();
         this.parentNode = var2;
         this.childNode = var3;
      }

      public void execute() {
         this.parentNode.appendChild(this.childNode);
      }

      public void undo() {
         if (this.oldParentNode != null) {
            this.oldParentNode.insertBefore(this.childNode, this.oldNextSibling);
         } else {
            this.parentNode.removeChild(this.childNode);
         }

      }

      public void redo() {
         this.execute();
      }

      public boolean shouldExecute() {
         return this.parentNode != null && this.childNode != null;
      }
   }

   public static class CharDataModifiedCommand extends AbstractUndoableCommand {
      protected Node contextNode;
      protected String oldValue;
      protected String newValue;

      public CharDataModifiedCommand(String var1, Node var2, String var3, String var4) {
         this.setName(var1);
         this.contextNode = var2;
         this.oldValue = var3;
         this.newValue = var4;
      }

      public void execute() {
      }

      public void undo() {
         this.contextNode.setNodeValue(this.oldValue);
      }

      public void redo() {
         this.contextNode.setNodeValue(this.newValue);
      }

      public boolean shouldExecute() {
         return this.contextNode != null;
      }
   }

   public static class AttributeModifiedCommand extends AbstractUndoableCommand {
      protected Element contextElement;
      protected String attributeName;
      protected String prevAttributeValue;
      protected String newAttributeValue;
      protected String namespaceURI;

      public AttributeModifiedCommand(String var1, Element var2, String var3, String var4, String var5, String var6) {
         this.setName(var1);
         this.contextElement = var2;
         this.attributeName = var3;
         this.prevAttributeValue = var4;
         this.newAttributeValue = var5;
         this.namespaceURI = var6;
      }

      public void execute() {
      }

      public void undo() {
         this.contextElement.setAttributeNS(this.namespaceURI, this.attributeName, this.prevAttributeValue);
      }

      public void redo() {
         this.contextElement.setAttributeNS(this.namespaceURI, this.attributeName, this.newAttributeValue);
      }

      public boolean shouldExecute() {
         return this.contextElement != null && this.attributeName.length() != 0;
      }
   }

   public static class AttributeRemovedCommand extends AbstractUndoableCommand {
      protected Element contextElement;
      protected String attributeName;
      protected String prevValue;
      protected String namespaceURI;

      public AttributeRemovedCommand(String var1, Element var2, String var3, String var4, String var5) {
         this.setName(var1);
         this.contextElement = var2;
         this.attributeName = var3;
         this.prevValue = var4;
         this.namespaceURI = var5;
      }

      public void execute() {
      }

      public void undo() {
         this.contextElement.setAttributeNS(this.namespaceURI, this.attributeName, this.prevValue);
      }

      public void redo() {
         this.contextElement.removeAttributeNS(this.namespaceURI, this.attributeName);
      }

      public boolean shouldExecute() {
         return this.contextElement != null && this.attributeName.length() != 0;
      }
   }

   public static class AttributeAddedCommand extends AbstractUndoableCommand {
      protected Element contextElement;
      protected String attributeName;
      protected String newValue;
      protected String namespaceURI;

      public AttributeAddedCommand(String var1, Element var2, String var3, String var4, String var5) {
         this.setName(var1);
         this.contextElement = var2;
         this.attributeName = var3;
         this.newValue = var4;
         this.namespaceURI = var5;
      }

      public void execute() {
      }

      public void undo() {
         this.contextElement.removeAttributeNS(this.namespaceURI, this.attributeName);
      }

      public void redo() {
         this.contextElement.setAttributeNS(this.namespaceURI, this.attributeName, this.newValue);
      }

      public boolean shouldExecute() {
         return this.contextElement != null && this.attributeName.length() != 0;
      }
   }

   public static class NodeRemovedCommand extends AbstractUndoableCommand {
      protected Node oldSibling;
      protected Node oldParent;
      protected Node contextNode;

      public NodeRemovedCommand(String var1, Node var2, Node var3, Node var4) {
         this.setName(var1);
         this.oldParent = var2;
         this.contextNode = var4;
         this.oldSibling = var3;
      }

      public void execute() {
      }

      public void undo() {
         if (this.oldSibling != null) {
            this.oldParent.insertBefore(this.contextNode, this.oldSibling);
         } else {
            this.oldParent.appendChild(this.contextNode);
         }

      }

      public void redo() {
         this.oldParent.removeChild(this.contextNode);
      }

      public boolean shouldExecute() {
         return this.oldParent != null && this.contextNode != null;
      }
   }

   public static class NodeInsertedCommand extends AbstractUndoableCommand {
      protected Node newSibling;
      protected Node newParent;
      protected Node contextNode;

      public NodeInsertedCommand(String var1, Node var2, Node var3, Node var4) {
         this.setName(var1);
         this.newParent = var2;
         this.contextNode = var4;
         this.newSibling = var3;
      }

      public void execute() {
      }

      public void undo() {
         this.newParent.removeChild(this.contextNode);
      }

      public void redo() {
         if (this.newSibling != null) {
            this.newParent.insertBefore(this.contextNode, this.newSibling);
         } else {
            this.newParent.appendChild(this.contextNode);
         }

      }

      public boolean shouldExecute() {
         return this.newParent != null && this.contextNode != null;
      }
   }

   public static class CompoundUpdateCommand extends AbstractCompoundCommand {
      public CompoundUpdateCommand(String var1) {
         this.setName(var1);
      }
   }
}
