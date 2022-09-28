package org.apache.xalan.xsltc.compiler;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.utils.XML11Char;

final class Output extends TopLevelElement {
   private String _version;
   private String _method;
   private String _encoding;
   private boolean _omitHeader = false;
   private String _standalone;
   private String _doctypePublic;
   private String _doctypeSystem;
   private String _cdata;
   private boolean _indent = false;
   private String _mediaType;
   private String _indentamount;
   private boolean _disabled = false;
   private static final String STRING_SIG = "Ljava/lang/String;";
   private static final String XML_VERSION = "1.0";
   private static final String HTML_VERSION = "4.0";

   public void display(int indent) {
      this.indent(indent);
      Util.println("Output " + this._method);
   }

   public void disable() {
      this._disabled = true;
   }

   public boolean enabled() {
      return !this._disabled;
   }

   public String getCdata() {
      return this._cdata;
   }

   public String getOutputMethod() {
      return this._method;
   }

   private void transferAttribute(Output previous, String qname) {
      if (!this.hasAttribute(qname) && previous.hasAttribute(qname)) {
         this.addAttribute(qname, previous.getAttribute(qname));
      }

   }

   public void mergeOutput(Output previous) {
      this.transferAttribute(previous, "version");
      this.transferAttribute(previous, "method");
      this.transferAttribute(previous, "encoding");
      this.transferAttribute(previous, "doctype-system");
      this.transferAttribute(previous, "doctype-public");
      this.transferAttribute(previous, "media-type");
      this.transferAttribute(previous, "indent");
      this.transferAttribute(previous, "omit-xml-declaration");
      this.transferAttribute(previous, "standalone");
      if (previous.hasAttribute("cdata-section-elements")) {
         this.addAttribute("cdata-section-elements", previous.getAttribute("cdata-section-elements") + ' ' + this.getAttribute("cdata-section-elements"));
      }

      String prefix = this.lookupPrefix("http://xml.apache.org/xalan");
      if (prefix != null) {
         this.transferAttribute(previous, prefix + ':' + "indent-amount");
      }

      prefix = this.lookupPrefix("http://xml.apache.org/xslt");
      if (prefix != null) {
         this.transferAttribute(previous, prefix + ':' + "indent-amount");
      }

   }

   public void parseContents(Parser parser) {
      Properties outputProperties = new Properties();
      parser.setOutput(this);
      if (!this._disabled) {
         String attrib = null;
         this._version = this.getAttribute("version");
         if (this._version.equals("")) {
            this._version = null;
         } else {
            outputProperties.setProperty("version", this._version);
         }

         this._method = this.getAttribute("method");
         if (this._method.equals("")) {
            this._method = null;
         }

         if (this._method != null) {
            this._method = this._method.toLowerCase();
            if (!this._method.equals("xml") && !this._method.equals("html") && !this._method.equals("text") && (!XML11Char.isXML11ValidQName(this._method) || this._method.indexOf(":") <= 0)) {
               this.reportError(this, parser, "INVALID_METHOD_IN_OUTPUT", this._method);
            } else {
               outputProperties.setProperty("method", this._method);
            }
         }

         this._encoding = this.getAttribute("encoding");
         if (this._encoding.equals("")) {
            this._encoding = null;
         } else {
            try {
               String canonicalEncoding = Encodings.convertMime2JavaEncoding(this._encoding);
               new OutputStreamWriter(System.out, canonicalEncoding);
            } catch (UnsupportedEncodingException var8) {
               ErrorMsg msg = new ErrorMsg("UNSUPPORTED_ENCODING", this._encoding, this);
               parser.reportError(4, msg);
            }

            outputProperties.setProperty("encoding", this._encoding);
         }

         attrib = this.getAttribute("omit-xml-declaration");
         if (!attrib.equals("")) {
            if (attrib.equals("yes")) {
               this._omitHeader = true;
            }

            outputProperties.setProperty("omit-xml-declaration", attrib);
         }

         this._standalone = this.getAttribute("standalone");
         if (this._standalone.equals("")) {
            this._standalone = null;
         } else {
            outputProperties.setProperty("standalone", this._standalone);
         }

         this._doctypeSystem = this.getAttribute("doctype-system");
         if (this._doctypeSystem.equals("")) {
            this._doctypeSystem = null;
         } else {
            outputProperties.setProperty("doctype-system", this._doctypeSystem);
         }

         this._doctypePublic = this.getAttribute("doctype-public");
         if (this._doctypePublic.equals("")) {
            this._doctypePublic = null;
         } else {
            outputProperties.setProperty("doctype-public", this._doctypePublic);
         }

         this._cdata = this.getAttribute("cdata-section-elements");
         if (this._cdata.equals("")) {
            this._cdata = null;
         } else {
            StringBuffer expandedNames = new StringBuffer();

            String qname;
            for(StringTokenizer tokens = new StringTokenizer(this._cdata); tokens.hasMoreTokens(); expandedNames.append(parser.getQName(qname).toString()).append(' ')) {
               qname = tokens.nextToken();
               if (!XML11Char.isXML11ValidQName(qname)) {
                  ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", qname, this);
                  parser.reportError(3, err);
               }
            }

            this._cdata = expandedNames.toString();
            outputProperties.setProperty("cdata-section-elements", this._cdata);
         }

         attrib = this.getAttribute("indent");
         if (!attrib.equals("")) {
            if (attrib.equals("yes")) {
               this._indent = true;
            }

            outputProperties.setProperty("indent", attrib);
         } else if (this._method != null && this._method.equals("html")) {
            this._indent = true;
         }

         this._indentamount = this.getAttribute(this.lookupPrefix("http://xml.apache.org/xalan"), "indent-amount");
         if (this._indentamount.equals("")) {
            this._indentamount = this.getAttribute(this.lookupPrefix("http://xml.apache.org/xslt"), "indent-amount");
         }

         if (!this._indentamount.equals("")) {
            outputProperties.setProperty("indent_amount", this._indentamount);
         }

         this._mediaType = this.getAttribute("media-type");
         if (this._mediaType.equals("")) {
            this._mediaType = null;
         } else {
            outputProperties.setProperty("media-type", this._mediaType);
         }

         if (this._method != null) {
            if (this._method.equals("html")) {
               if (this._version == null) {
                  this._version = "4.0";
               }

               if (this._mediaType == null) {
                  this._mediaType = "text/html";
               }
            } else if (this._method.equals("text") && this._mediaType == null) {
               this._mediaType = "text/plain";
            }
         }

         parser.getCurrentStylesheet().setOutputProperties(outputProperties);
      }
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      if (!this._disabled) {
         ConstantPoolGen cpg = classGen.getConstantPool();
         InstructionList il = methodGen.getInstructionList();
         int field = false;
         il.append(classGen.loadTranslet());
         int field;
         if (this._version != null && !this._version.equals("1.0")) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_version", "Ljava/lang/String;");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, this._version)));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         if (this._method != null) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_method", "Ljava/lang/String;");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, this._method)));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         if (this._encoding != null) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_encoding", "Ljava/lang/String;");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, this._encoding)));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         if (this._omitHeader) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_omitHeader", "Z");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, this._omitHeader)));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         if (this._standalone != null) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_standalone", "Ljava/lang/String;");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, this._standalone)));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_doctypeSystem", "Ljava/lang/String;");
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, this._doctypeSystem)));
         il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_doctypePublic", "Ljava/lang/String;");
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, this._doctypePublic)));
         il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         if (this._mediaType != null) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_mediaType", "Ljava/lang/String;");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, this._mediaType)));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         if (this._indent) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_indent", "Z");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, this._indent)));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         if (this._indentamount != null && !this._indentamount.equals("")) {
            field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_indentamount", "I");
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((CompoundInstruction)(new PUSH(cpg, Integer.parseInt(this._indentamount))));
            il.append((org.apache.bcel.generic.Instruction)(new PUTFIELD(field)));
         }

         if (this._cdata != null) {
            int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addCdataElement", "(Ljava/lang/String;)V");
            StringTokenizer tokens = new StringTokenizer(this._cdata);

            while(tokens.hasMoreTokens()) {
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
               il.append((CompoundInstruction)(new PUSH(cpg, tokens.nextToken())));
               il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(index)));
            }
         }

         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.POP);
      }
   }
}
