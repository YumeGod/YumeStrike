package de.javasoft.plaf.synthetica;

import java.text.ParseException;

public class SyntheticaBlueIceLookAndFeel extends SyntheticaLookAndFeel {
   public SyntheticaBlueIceLookAndFeel() throws ParseException {
      super("blueice/xml/synth.xml");
   }

   public String getID() {
      return "SyntheticaBlueIceLookAndFeel";
   }

   public String getName() {
      return "Synthetica BlueIce Look and Feel";
   }
}
