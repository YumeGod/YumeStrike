package org.apache.fop.afp.ptoca;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;

public abstract class PtocaBuilder implements PtocaConstants {
   private ByteArrayOutputStream baout = new ByteArrayOutputStream(256);
   private int currentX = -1;
   private int currentY = -1;
   private int currentFont = Integer.MIN_VALUE;
   private int currentOrientation = 0;
   private Color currentColor;
   private int currentVariableSpaceCharacterIncrement;
   private int currentInterCharacterAdjustment;
   private static final int TRANSPARENT_MAX_SIZE = 253;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PtocaBuilder() {
      this.currentColor = Color.BLACK;
      this.currentVariableSpaceCharacterIncrement = 0;
      this.currentInterCharacterAdjustment = 0;
   }

   protected abstract OutputStream getOutputStreamForControlSequence(int var1);

   private static byte chained(byte functionType) {
      return (byte)(functionType | 1);
   }

   private void newControlSequence() {
      this.baout.reset();
   }

   private void commit(byte functionType) throws IOException {
      int length = this.baout.size() + 2;
      if (!$assertionsDisabled && length >= 256) {
         throw new AssertionError();
      } else {
         OutputStream out = this.getOutputStreamForControlSequence(length);
         out.write(length);
         out.write(functionType);
         this.baout.writeTo(out);
      }
   }

   private void write(byte[] data, int offset, int length) {
      this.baout.write(data, offset, length);
   }

   private void writeByte(int data) {
      this.baout.write(data);
   }

   private void writeShort(int data) {
      this.baout.write(data >>> 8 & 255);
      this.baout.write(data & 255);
   }

   public void writeIntroducer() throws IOException {
      OutputStream out = this.getOutputStreamForControlSequence(ESCAPE.length);
      out.write(ESCAPE);
   }

   public void setCodedFont(byte font) throws IOException {
      if (this.currentFont != font) {
         this.currentFont = font;
         this.newControlSequence();
         this.writeByte(font);
         this.commit(chained((byte)-16));
      }
   }

   public void absoluteMoveInline(int coordinate) throws IOException {
      if (coordinate != this.currentX) {
         this.newControlSequence();
         this.writeShort(coordinate);
         this.commit(chained((byte)-58));
         this.currentX = coordinate;
      }
   }

   public void relativeMoveInline(int increment) throws IOException {
      this.newControlSequence();
      this.writeShort(increment);
      this.commit(chained((byte)-56));
   }

   public void absoluteMoveBaseline(int coordinate) throws IOException {
      if (coordinate != this.currentY) {
         this.newControlSequence();
         this.writeShort(coordinate);
         this.commit(chained((byte)-46));
         this.currentY = coordinate;
         this.currentX = -1;
      }
   }

   public void addTransparentData(byte[] data) throws IOException {
      if (data.length <= 253) {
         this.addTransparentDataChunk(data);
      } else {
         int numTransData = data.length / 253;
         int currIndex = 0;

         int left;
         for(left = 0; left < numTransData; ++left) {
            this.addTransparentDataChunk(data, currIndex, 253);
            currIndex += 253;
         }

         left = data.length - currIndex;
         this.addTransparentDataChunk(data, currIndex, left);
      }

   }

   private void addTransparentDataChunk(byte[] data) throws IOException {
      this.addTransparentDataChunk(data, 0, data.length);
   }

   private void addTransparentDataChunk(byte[] data, int offset, int length) throws IOException {
      if (length > 253) {
         throw new IllegalArgumentException("Transparent data is longer than 253 bytes");
      } else {
         this.newControlSequence();
         this.write(data, offset, length);
         this.commit(chained((byte)-38));
      }
   }

   public void drawBaxisRule(int length, int width) throws IOException {
      this.newControlSequence();
      this.writeShort(length);
      this.writeShort(width);
      this.writeByte(0);
      this.commit(chained((byte)-26));
   }

   public void drawIaxisRule(int length, int width) throws IOException {
      this.newControlSequence();
      this.writeShort(length);
      this.writeShort(width);
      this.writeByte(0);
      this.commit(chained((byte)-28));
   }

   public void setTextOrientation(int orientation) throws IOException {
      if (orientation != this.currentOrientation) {
         this.newControlSequence();
         switch (orientation) {
            case 90:
               this.writeByte(45);
               this.writeByte(0);
               this.writeByte(90);
               this.writeByte(0);
               break;
            case 180:
               this.writeByte(90);
               this.writeByte(0);
               this.writeByte(135);
               this.writeByte(0);
               break;
            case 270:
               this.writeByte(135);
               this.writeByte(0);
               this.writeByte(0);
               this.writeByte(0);
               break;
            default:
               this.writeByte(0);
               this.writeByte(0);
               this.writeByte(45);
               this.writeByte(0);
         }

         this.commit(chained((byte)-10));
         this.currentOrientation = orientation;
         this.currentX = -1;
         this.currentY = -1;
      }
   }

   public void setExtendedTextColor(Color col) throws IOException {
      if (!col.equals(this.currentColor)) {
         this.newControlSequence();
         if (col.getColorSpace().getType() == 9) {
            this.writeByte(0);
            this.writeByte(4);
            this.writeByte(0);
            this.writeByte(0);
            this.writeByte(0);
            this.writeByte(0);
            this.writeByte(8);
            this.writeByte(8);
            this.writeByte(8);
            this.writeByte(8);
            float[] comps = col.getColorComponents((float[])null);
            if (!$assertionsDisabled && comps.length != 4) {
               throw new AssertionError();
            }

            for(int i = 0; i < 4; ++i) {
               int component = Math.round(comps[i] * 255.0F);
               this.writeByte(component);
            }
         } else {
            this.writeByte(0);
            this.writeByte(1);
            this.writeByte(0);
            this.writeByte(0);
            this.writeByte(0);
            this.writeByte(0);
            this.writeByte(8);
            this.writeByte(8);
            this.writeByte(8);
            this.writeByte(0);
            this.writeByte(col.getRed());
            this.writeByte(col.getGreen());
            this.writeByte(col.getBlue());
         }

         this.commit(chained((byte)-128));
         this.currentColor = col;
      }
   }

   public void setVariableSpaceCharacterIncrement(int incr) throws IOException {
      if (incr != this.currentVariableSpaceCharacterIncrement) {
         if ($assertionsDisabled || incr >= 0 && incr < 65536) {
            this.newControlSequence();
            this.writeShort(Math.abs(incr));
            this.commit(chained((byte)-60));
            this.currentVariableSpaceCharacterIncrement = incr;
         } else {
            throw new AssertionError();
         }
      }
   }

   public void setInterCharacterAdjustment(int incr) throws IOException {
      if (incr != this.currentInterCharacterAdjustment) {
         if ($assertionsDisabled || incr >= -32768 && incr <= 32767) {
            this.newControlSequence();
            this.writeShort(Math.abs(incr));
            this.writeByte(incr >= 0 ? 0 : 1);
            this.commit(chained((byte)-62));
            this.currentInterCharacterAdjustment = incr;
         } else {
            throw new AssertionError();
         }
      }
   }

   public void endChainedControlSequence() throws IOException {
      this.newControlSequence();
      this.commit((byte)-8);
   }

   static {
      $assertionsDisabled = !PtocaBuilder.class.desiredAssertionStatus();
   }
}
