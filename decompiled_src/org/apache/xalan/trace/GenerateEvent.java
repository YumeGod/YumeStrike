package org.apache.xalan.trace;

import java.util.EventListener;
import org.apache.xalan.transformer.TransformerImpl;
import org.xml.sax.Attributes;

public class GenerateEvent implements EventListener {
   public TransformerImpl m_processor;
   public int m_eventtype;
   public char[] m_characters;
   public int m_start;
   public int m_length;
   public String m_name;
   public String m_data;
   public Attributes m_atts;

   public GenerateEvent(TransformerImpl processor, int eventType) {
      this.m_processor = processor;
      this.m_eventtype = eventType;
   }

   public GenerateEvent(TransformerImpl processor, int eventType, String name, Attributes atts) {
      this.m_name = name;
      this.m_atts = atts;
      this.m_processor = processor;
      this.m_eventtype = eventType;
   }

   public GenerateEvent(TransformerImpl processor, int eventType, char[] ch, int start, int length) {
      this.m_characters = ch;
      this.m_start = start;
      this.m_length = length;
      this.m_processor = processor;
      this.m_eventtype = eventType;
   }

   public GenerateEvent(TransformerImpl processor, int eventType, String name, String data) {
      this.m_name = name;
      this.m_data = data;
      this.m_processor = processor;
      this.m_eventtype = eventType;
   }

   public GenerateEvent(TransformerImpl processor, int eventType, String data) {
      this.m_data = data;
      this.m_processor = processor;
      this.m_eventtype = eventType;
   }
}
