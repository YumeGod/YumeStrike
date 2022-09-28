package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.util.HashMap;
import java.util.Iterator;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class RtfAttributes implements Cloneable {
   private HashMap values = new HashMap();
   private Attributes xslAttributes = null;

   public RtfAttributes set(RtfAttributes attrs) {
      if (attrs != null) {
         Iterator it = attrs.nameIterator();

         while(it.hasNext()) {
            String name = (String)it.next();
            if (attrs.getValue(name) instanceof Integer) {
               Integer value = (Integer)attrs.getValue(name);
               if (value == null) {
                  this.set(name);
               } else {
                  this.set(name, value);
               }
            } else if (attrs.getValue(name) instanceof String) {
               String value = (String)attrs.getValue(name);
               if (value == null) {
                  this.set(name);
               } else {
                  this.set(name, value);
               }
            } else {
               this.set(name);
            }
         }

         this.setXslAttributes(attrs.getXslAttributes());
      }

      return this;
   }

   public RtfAttributes set(String name) {
      this.values.put(name, (Object)null);
      return this;
   }

   public RtfAttributes unset(String name) {
      this.values.remove(name);
      return this;
   }

   public String toString() {
      return this.values.toString() + "(" + super.toString() + ")";
   }

   public Object clone() {
      RtfAttributes result = new RtfAttributes();
      result.values = (HashMap)this.values.clone();
      if (this.xslAttributes != null) {
         result.xslAttributes = new AttributesImpl(this.xslAttributes);
      }

      return result;
   }

   public RtfAttributes set(String name, int value) {
      this.values.put(name, new Integer(value));
      return this;
   }

   public RtfAttributes set(String name, String type) {
      this.values.put(name, type);
      return this;
   }

   public RtfAttributes set(String name, RtfAttributes value) {
      this.values.put(name, value);
      return this;
   }

   public Object getValue(String name) {
      return this.values.get(name);
   }

   public Integer getValueAsInteger(String name) {
      return (Integer)this.values.get(name);
   }

   public boolean isSet(String name) {
      return this.values.containsKey(name);
   }

   public Iterator nameIterator() {
      return this.values.keySet().iterator();
   }

   public Attributes getXslAttributes() {
      return this.xslAttributes;
   }

   public void setXslAttributes(Attributes pAttribs) {
      if (pAttribs != null) {
         if (this.xslAttributes != null) {
            for(int i = 0; i < pAttribs.getLength(); ++i) {
               String wKey = pAttribs.getQName(i);
               int wPos = this.xslAttributes.getIndex(wKey);
               if (wPos == -1) {
                  ((AttributesImpl)this.xslAttributes).addAttribute(pAttribs.getURI(i), pAttribs.getLocalName(i), pAttribs.getQName(i), pAttribs.getType(i), pAttribs.getValue(i));
               } else {
                  ((AttributesImpl)this.xslAttributes).setAttribute(wPos, pAttribs.getURI(i), pAttribs.getLocalName(i), pAttribs.getQName(i), pAttribs.getType(i), pAttribs.getValue(i));
               }
            }
         } else {
            this.xslAttributes = new AttributesImpl(pAttribs);
         }

      }
   }

   public void addIntegerValue(int addValue, String name) {
      Integer value = (Integer)this.getValue(name);
      int v = value != null ? value : 0;
      this.set(name, v + addValue);
   }
}
