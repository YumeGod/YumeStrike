package org.apache.xalan.xsltc.compiler;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xalan.xsltc.compiler.util.MethodType;

final class SymbolTable {
   private final Hashtable _stylesheets = new Hashtable();
   private final Hashtable _primops = new Hashtable();
   private Hashtable _variables = null;
   private Hashtable _templates = null;
   private Hashtable _attributeSets = null;
   private Hashtable _aliases = null;
   private Hashtable _excludedURI = null;
   private Hashtable _decimalFormats = null;
   private int _nsCounter = 0;
   private SyntaxTreeNode _current = null;

   public DecimalFormatting getDecimalFormatting(QName name) {
      return this._decimalFormats == null ? null : (DecimalFormatting)this._decimalFormats.get(name);
   }

   public void addDecimalFormatting(QName name, DecimalFormatting symbols) {
      if (this._decimalFormats == null) {
         this._decimalFormats = new Hashtable();
      }

      this._decimalFormats.put(name, symbols);
   }

   public Stylesheet addStylesheet(QName name, Stylesheet node) {
      return (Stylesheet)this._stylesheets.put(name, node);
   }

   public Stylesheet lookupStylesheet(QName name) {
      return (Stylesheet)this._stylesheets.get(name);
   }

   public Template addTemplate(Template template) {
      QName name = template.getName();
      if (this._templates == null) {
         this._templates = new Hashtable();
      }

      return (Template)this._templates.put(name, template);
   }

   public Template lookupTemplate(QName name) {
      return this._templates == null ? null : (Template)this._templates.get(name);
   }

   public Variable addVariable(Variable variable) {
      if (this._variables == null) {
         this._variables = new Hashtable();
      }

      String name = variable.getName().getStringRep();
      return (Variable)this._variables.put(name, variable);
   }

   public Param addParam(Param parameter) {
      if (this._variables == null) {
         this._variables = new Hashtable();
      }

      String name = parameter.getName().getStringRep();
      return (Param)this._variables.put(name, parameter);
   }

   public Variable lookupVariable(QName qname) {
      if (this._variables == null) {
         return null;
      } else {
         String name = qname.getStringRep();
         Object obj = this._variables.get(name);
         return obj instanceof Variable ? (Variable)obj : null;
      }
   }

   public Param lookupParam(QName qname) {
      if (this._variables == null) {
         return null;
      } else {
         String name = qname.getStringRep();
         Object obj = this._variables.get(name);
         return obj instanceof Param ? (Param)obj : null;
      }
   }

   public SyntaxTreeNode lookupName(QName qname) {
      if (this._variables == null) {
         return null;
      } else {
         String name = qname.getStringRep();
         return (SyntaxTreeNode)this._variables.get(name);
      }
   }

   public AttributeSet addAttributeSet(AttributeSet atts) {
      if (this._attributeSets == null) {
         this._attributeSets = new Hashtable();
      }

      return (AttributeSet)this._attributeSets.put(atts.getName(), atts);
   }

   public AttributeSet lookupAttributeSet(QName name) {
      return this._attributeSets == null ? null : (AttributeSet)this._attributeSets.get(name);
   }

   public void addPrimop(String name, MethodType mtype) {
      Vector methods = (Vector)this._primops.get(name);
      if (methods == null) {
         this._primops.put(name, methods = new Vector());
      }

      methods.addElement(mtype);
   }

   public Vector lookupPrimop(String name) {
      return (Vector)this._primops.get(name);
   }

   public String generateNamespacePrefix() {
      return new String("ns" + this._nsCounter++);
   }

   public void setCurrentNode(SyntaxTreeNode node) {
      this._current = node;
   }

   public String lookupNamespace(String prefix) {
      return this._current == null ? "" : this._current.lookupNamespace(prefix);
   }

   public void addPrefixAlias(String prefix, String alias) {
      if (this._aliases == null) {
         this._aliases = new Hashtable();
      }

      this._aliases.put(prefix, alias);
   }

   public String lookupPrefixAlias(String prefix) {
      return this._aliases == null ? null : (String)this._aliases.get(prefix);
   }

   public void excludeURI(String uri) {
      if (uri != null) {
         if (this._excludedURI == null) {
            this._excludedURI = new Hashtable();
         }

         Integer refcnt = (Integer)this._excludedURI.get(uri);
         if (refcnt == null) {
            refcnt = new Integer(1);
         } else {
            refcnt = new Integer(refcnt + 1);
         }

         this._excludedURI.put(uri, refcnt);
      }
   }

   public void excludeNamespaces(String prefixes) {
      if (prefixes != null) {
         StringTokenizer tokens = new StringTokenizer(prefixes);

         while(tokens.hasMoreTokens()) {
            String prefix = tokens.nextToken();
            String uri;
            if (prefix.equals("#default")) {
               uri = this.lookupNamespace("");
            } else {
               uri = this.lookupNamespace(prefix);
            }

            if (uri != null) {
               this.excludeURI(uri);
            }
         }
      }

   }

   public boolean isExcludedNamespace(String uri) {
      if (uri != null && this._excludedURI != null) {
         Integer refcnt = (Integer)this._excludedURI.get(uri);
         return refcnt != null && refcnt > 0;
      } else {
         return false;
      }
   }

   public void unExcludeNamespaces(String prefixes) {
      if (this._excludedURI != null) {
         if (prefixes != null) {
            StringTokenizer tokens = new StringTokenizer(prefixes);

            while(tokens.hasMoreTokens()) {
               String prefix = tokens.nextToken();
               String uri;
               if (prefix.equals("#default")) {
                  uri = this.lookupNamespace("");
               } else {
                  uri = this.lookupNamespace(prefix);
               }

               Integer refcnt = (Integer)this._excludedURI.get(uri);
               if (refcnt != null) {
                  this._excludedURI.put(uri, new Integer(refcnt - 1));
               }
            }
         }

      }
   }
}
