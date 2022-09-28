package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.fop.afp.AFPLineDataInfo;
import org.apache.fop.afp.Completable;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.ptoca.PtocaProducer;

public abstract class AbstractPageObject extends AbstractNamedAFPObject implements Completable {
   protected ActiveEnvironmentGroup activeEnvironmentGroup = null;
   private PresentationTextObject currentPresentationTextObject = null;
   protected List tagLogicalElements = null;
   protected List objects = new ArrayList();
   private int width;
   private int height;
   protected int rotation = 0;
   protected boolean complete = false;
   private int widthRes;
   private int heightRes;
   protected final Factory factory;

   public AbstractPageObject(Factory factory) {
      this.factory = factory;
   }

   public AbstractPageObject(Factory factory, String name) {
      super(name);
      this.factory = factory;
   }

   public AbstractPageObject(Factory factory, String name, int width, int height, int rotation, int widthRes, int heightRes) {
      super(name);
      this.factory = factory;
      this.width = width;
      this.height = height;
      this.rotation = rotation;
      this.widthRes = widthRes;
      this.heightRes = heightRes;
   }

   public void createFont(int fontReference, AFPFont font, int size) {
      this.getActiveEnvironmentGroup().createFont(fontReference, font, size, 0);
   }

   public void createLine(AFPLineDataInfo lineDataInfo) {
      this.getPresentationTextObject().createLineData(lineDataInfo);
   }

   public void createText(PtocaProducer producer) throws UnsupportedEncodingException {
      this.getPresentationTextObject().createControlSequences(producer);
   }

   public void endPage() {
      if (this.currentPresentationTextObject != null) {
         this.currentPresentationTextObject.endControlSequence();
      }

      this.setComplete(true);
   }

   protected void endPresentationObject() {
      if (this.currentPresentationTextObject != null) {
         this.currentPresentationTextObject.endControlSequence();
         this.currentPresentationTextObject = null;
      }

   }

   public PresentationTextObject getPresentationTextObject() {
      if (this.currentPresentationTextObject == null) {
         PresentationTextObject presentationTextObject = this.factory.createPresentationTextObject();
         this.addObject(presentationTextObject);
         this.currentPresentationTextObject = presentationTextObject;
      }

      return this.currentPresentationTextObject;
   }

   protected List getTagLogicalElements() {
      if (this.tagLogicalElements == null) {
         this.tagLogicalElements = new ArrayList();
      }

      return this.tagLogicalElements;
   }

   public void createTagLogicalElement(String name, String value, int tleID) {
      TagLogicalElement tle = new TagLogicalElement(name, value, tleID);
      List list = this.getTagLogicalElements();
      list.add(tle);
   }

   public void createNoOperation(String content) {
      this.addObject(new NoOperation(content));
   }

   public void createIncludePageSegment(String name, int x, int y, boolean hard) {
      IncludePageSegment ips = this.factory.createIncludePageSegment(name, x, y);
      this.addObject(ips);
      if (hard) {
         this.getActiveEnvironmentGroup().addMapPageSegment(name);
      }

   }

   public ActiveEnvironmentGroup getActiveEnvironmentGroup() {
      if (this.activeEnvironmentGroup == null) {
         this.activeEnvironmentGroup = this.factory.createActiveEnvironmentGroup(this.width, this.height, this.widthRes, this.heightRes);
         if (this.rotation != 0) {
            switch (this.rotation) {
               case 90:
                  this.activeEnvironmentGroup.setObjectAreaPosition(this.width, 0, this.rotation);
                  break;
               case 180:
                  this.activeEnvironmentGroup.setObjectAreaPosition(this.width, this.height, this.rotation);
                  break;
               case 270:
                  this.activeEnvironmentGroup.setObjectAreaPosition(0, this.height, this.rotation);
            }
         }
      }

      return this.activeEnvironmentGroup;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public int getRotation() {
      return this.rotation;
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      this.writeObjects(this.objects, os);
   }

   public void addObject(Object obj) {
      this.objects.add(obj);
   }

   public void setComplete(boolean complete) {
      this.complete = complete;
   }

   public boolean isComplete() {
      return this.complete;
   }
}
