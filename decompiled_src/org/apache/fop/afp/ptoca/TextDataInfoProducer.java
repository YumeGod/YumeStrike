package org.apache.fop.afp.ptoca;

import java.io.IOException;
import org.apache.fop.afp.AFPTextDataInfo;

public class TextDataInfoProducer implements PtocaProducer, PtocaConstants {
   private AFPTextDataInfo textDataInfo;

   public TextDataInfoProducer(AFPTextDataInfo textDataInfo) {
      this.textDataInfo = textDataInfo;
   }

   public void produce(PtocaBuilder builder) throws IOException {
      builder.setTextOrientation(this.textDataInfo.getRotation());
      builder.absoluteMoveBaseline(this.textDataInfo.getY());
      builder.absoluteMoveInline(this.textDataInfo.getX());
      builder.setVariableSpaceCharacterIncrement(this.textDataInfo.getVariableSpaceCharacterIncrement());
      builder.setInterCharacterAdjustment(this.textDataInfo.getInterCharacterAdjustment());
      builder.setExtendedTextColor(this.textDataInfo.getColor());
      builder.setCodedFont((byte)this.textDataInfo.getFontReference());
      String textString = this.textDataInfo.getString();
      String encoding = this.textDataInfo.getEncoding();
      byte[] data = textString.getBytes(encoding);
      builder.addTransparentData(data);
   }
}
