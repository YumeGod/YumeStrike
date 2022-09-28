package org.apache.xalan.xsltc.trax;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.runtime.Hashtable;

public final class TemplatesImpl implements Templates, Serializable {
   static final long serialVersionUID = 673094361519270707L;
   private static String ABSTRACT_TRANSLET = "org.apache.xalan.xsltc.runtime.AbstractTranslet";
   private String _name = null;
   private byte[][] _bytecodes = null;
   private Class[] _class = null;
   private int _transletIndex = -1;
   private Hashtable _auxClasses = null;
   private Properties _outputProperties;
   private int _indentNumber;
   private transient URIResolver _uriResolver = null;
   private transient ThreadLocal _sdom = new ThreadLocal();
   private transient TransformerFactoryImpl _tfactory = null;

   protected TemplatesImpl(byte[][] bytecodes, String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
      this._bytecodes = bytecodes;
      this._name = transletName;
      this._outputProperties = outputProperties;
      this._indentNumber = indentNumber;
      this._tfactory = tfactory;
   }

   protected TemplatesImpl(Class[] transletClasses, String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
      this._class = transletClasses;
      this._name = transletName;
      this._transletIndex = 0;
      this._outputProperties = outputProperties;
      this._indentNumber = indentNumber;
      this._tfactory = tfactory;
   }

   public TemplatesImpl() {
   }

   private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
      is.defaultReadObject();
      if (is.readBoolean()) {
         this._uriResolver = (URIResolver)is.readObject();
      }

      this._tfactory = new TransformerFactoryImpl();
   }

   private void writeObject(ObjectOutputStream os) throws IOException, ClassNotFoundException {
      os.defaultWriteObject();
      if (this._uriResolver instanceof Serializable) {
         os.writeBoolean(true);
         os.writeObject((Serializable)this._uriResolver);
      } else {
         os.writeBoolean(false);
      }

   }

   public synchronized void setURIResolver(URIResolver resolver) {
      this._uriResolver = resolver;
   }

   protected synchronized void setTransletBytecodes(byte[][] bytecodes) {
      this._bytecodes = bytecodes;
   }

   public synchronized byte[][] getTransletBytecodes() {
      return this._bytecodes;
   }

   public synchronized Class[] getTransletClasses() {
      try {
         if (this._class == null) {
            this.defineTransletClasses();
         }
      } catch (TransformerConfigurationException var2) {
      }

      return this._class;
   }

   public synchronized int getTransletIndex() {
      try {
         if (this._class == null) {
            this.defineTransletClasses();
         }
      } catch (TransformerConfigurationException var2) {
      }

      return this._transletIndex;
   }

   protected synchronized void setTransletName(String name) {
      this._name = name;
   }

   protected synchronized String getTransletName() {
      return this._name;
   }

   private void defineTransletClasses() throws TransformerConfigurationException {
      if (this._bytecodes == null) {
         ErrorMsg err = new ErrorMsg("NO_TRANSLET_CLASS_ERR");
         throw new TransformerConfigurationException(err.toString());
      } else {
         TransletClassLoader loader = (TransletClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return new TransletClassLoader(ObjectFactory.findClassLoader());
            }
         });

         ErrorMsg err;
         try {
            int classCount = this._bytecodes.length;
            this._class = new Class[classCount];
            if (classCount > 1) {
               this._auxClasses = new Hashtable();
            }

            for(int i = 0; i < classCount; ++i) {
               this._class[i] = loader.defineClass(this._bytecodes[i]);
               Class superClass = this._class[i].getSuperclass();
               if (superClass.getName().equals(ABSTRACT_TRANSLET)) {
                  this._transletIndex = i;
               } else {
                  this._auxClasses.put(this._class[i].getName(), this._class[i]);
               }
            }

            if (this._transletIndex < 0) {
               err = new ErrorMsg("NO_MAIN_TRANSLET_ERR", this._name);
               throw new TransformerConfigurationException(err.toString());
            }
         } catch (ClassFormatError var5) {
            ErrorMsg err = new ErrorMsg("TRANSLET_CLASS_ERR", this._name);
            throw new TransformerConfigurationException(err.toString());
         } catch (LinkageError var6) {
            err = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
            throw new TransformerConfigurationException(err.toString());
         }
      }
   }

   private Translet getTransletInstance() throws TransformerConfigurationException {
      try {
         if (this._name == null) {
            return null;
         } else {
            if (this._class == null) {
               this.defineTransletClasses();
            }

            AbstractTranslet translet = (AbstractTranslet)this._class[this._transletIndex].newInstance();
            translet.postInitialization();
            translet.setTemplates(this);
            if (this._auxClasses != null) {
               translet.setAuxiliaryClasses(this._auxClasses);
            }

            return translet;
         }
      } catch (InstantiationException var4) {
         ErrorMsg err = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
         throw new TransformerConfigurationException(err.toString());
      } catch (IllegalAccessException var5) {
         ErrorMsg err = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
         throw new TransformerConfigurationException(err.toString());
      }
   }

   public synchronized Transformer newTransformer() throws TransformerConfigurationException {
      TransformerImpl transformer = new TransformerImpl(this.getTransletInstance(), this._outputProperties, this._indentNumber, this._tfactory);
      if (this._uriResolver != null) {
         transformer.setURIResolver(this._uriResolver);
      }

      if (this._tfactory.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
         transformer.setSecureProcessing(true);
      }

      return transformer;
   }

   public synchronized Properties getOutputProperties() {
      try {
         return this.newTransformer().getOutputProperties();
      } catch (TransformerConfigurationException var2) {
         return null;
      }
   }

   public DOM getStylesheetDOM() {
      return (DOM)this._sdom.get();
   }

   public void setStylesheetDOM(DOM sdom) {
      this._sdom.set(sdom);
   }

   static final class TransletClassLoader extends ClassLoader {
      TransletClassLoader(ClassLoader parent) {
         super(parent);
      }

      Class defineClass(byte[] b) {
         return this.defineClass((String)null, b, 0, b.length);
      }
   }
}
