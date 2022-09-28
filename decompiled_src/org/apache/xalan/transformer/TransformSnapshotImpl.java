package org.apache.xalan.transformer;

import java.util.Stack;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.serializer.NamespaceMappings;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.BoolStack;
import org.apache.xml.utils.IntStack;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.ObjectStack;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;

/** @deprecated */
class TransformSnapshotImpl implements TransformSnapshot {
   private VariableStack m_variableStacks;
   private IntStack m_currentNodes;
   private IntStack m_currentExpressionNodes;
   private Stack m_contextNodeLists;
   private DTMIterator m_contextNodeList;
   private Stack m_axesIteratorStack;
   private BoolStack m_currentTemplateRuleIsNull;
   private ObjectStack m_currentTemplateElements;
   private Stack m_currentMatchTemplates;
   private NodeVector m_currentMatchNodes;
   private CountersTable m_countersTable;
   private Stack m_attrSetStack;
   boolean m_nsContextPushed;
   private NamespaceMappings m_nsSupport;

   /** @deprecated */
   TransformSnapshotImpl(TransformerImpl transformer) {
      try {
         SerializationHandler rtf = transformer.getResultTreeHandler();
         this.m_nsSupport = (NamespaceMappings)rtf.getNamespaceMappings().clone();
         XPathContext xpc = transformer.getXPathContext();
         this.m_variableStacks = (VariableStack)xpc.getVarStack().clone();
         this.m_currentNodes = (IntStack)xpc.getCurrentNodeStack().clone();
         this.m_currentExpressionNodes = (IntStack)xpc.getCurrentExpressionNodeStack().clone();
         this.m_contextNodeLists = (Stack)xpc.getContextNodeListsStack().clone();
         if (!this.m_contextNodeLists.empty()) {
            this.m_contextNodeList = (DTMIterator)xpc.getContextNodeList().clone();
         }

         this.m_axesIteratorStack = (Stack)xpc.getAxesIteratorStackStacks().clone();
         this.m_currentTemplateRuleIsNull = (BoolStack)transformer.m_currentTemplateRuleIsNull.clone();
         this.m_currentTemplateElements = (ObjectStack)transformer.m_currentTemplateElements.clone();
         this.m_currentMatchTemplates = (Stack)transformer.m_currentMatchTemplates.clone();
         this.m_currentMatchNodes = (NodeVector)transformer.m_currentMatchedNodes.clone();
         this.m_countersTable = (CountersTable)transformer.getCountersTable().clone();
         if (transformer.m_attrSetStack != null) {
            this.m_attrSetStack = (Stack)transformer.m_attrSetStack.clone();
         }

      } catch (CloneNotSupportedException var4) {
         throw new WrappedRuntimeException(var4);
      }
   }

   /** @deprecated */
   void apply(TransformerImpl transformer) {
      try {
         SerializationHandler rtf = transformer.getResultTreeHandler();
         if (rtf != null) {
            rtf.setNamespaceMappings((NamespaceMappings)this.m_nsSupport.clone());
         }

         XPathContext xpc = transformer.getXPathContext();
         xpc.setVarStack((VariableStack)this.m_variableStacks.clone());
         xpc.setCurrentNodeStack((IntStack)this.m_currentNodes.clone());
         xpc.setCurrentExpressionNodeStack((IntStack)this.m_currentExpressionNodes.clone());
         xpc.setContextNodeListsStack((Stack)this.m_contextNodeLists.clone());
         if (this.m_contextNodeList != null) {
            xpc.pushContextNodeList((DTMIterator)this.m_contextNodeList.clone());
         }

         xpc.setAxesIteratorStackStacks((Stack)this.m_axesIteratorStack.clone());
         transformer.m_currentTemplateRuleIsNull = (BoolStack)this.m_currentTemplateRuleIsNull.clone();
         transformer.m_currentTemplateElements = (ObjectStack)this.m_currentTemplateElements.clone();
         transformer.m_currentMatchTemplates = (Stack)this.m_currentMatchTemplates.clone();
         transformer.m_currentMatchedNodes = (NodeVector)this.m_currentMatchNodes.clone();
         transformer.m_countersTable = (CountersTable)this.m_countersTable.clone();
         if (this.m_attrSetStack != null) {
            transformer.m_attrSetStack = (Stack)this.m_attrSetStack.clone();
         }

      } catch (CloneNotSupportedException var4) {
         throw new WrappedRuntimeException(var4);
      }
   }
}
