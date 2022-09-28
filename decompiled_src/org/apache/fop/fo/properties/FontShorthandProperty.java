package org.apache.fop.fo.properties;

import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;

public class FontShorthandProperty extends ListProperty {
   private void addProperty(Property prop, int pos) {
      while(this.list.size() < pos + 1) {
         this.list.add((Object)null);
      }

      this.list.set(pos, prop);
   }

   public static class Maker extends PropertyMaker {
      private static final int[] PROP_IDS = new int[]{103, 101, 144, 106, 107, 108};

      public Maker(int propId) {
         super(propId);
      }

      public Property make(PropertyList propertyList, String value, FObj fo) throws PropertyException {
         try {
            FontShorthandProperty newProp = new FontShorthandProperty();
            newProp.setSpecifiedValue(value);
            String specVal = value;
            Property prop = null;
            int spaceIndex;
            if ("inherit".equals(value)) {
               spaceIndex = PROP_IDS.length;

               while(true) {
                  --spaceIndex;
                  if (spaceIndex < 0) {
                     break;
                  }

                  prop = propertyList.getFromParent(PROP_IDS[spaceIndex]);
                  newProp.addProperty(prop, spaceIndex);
               }
            } else {
               spaceIndex = PROP_IDS.length;

               label118:
               while(true) {
                  --spaceIndex;
                  if (spaceIndex < 0) {
                     prop = this.checkEnumValues(value);
                     if (prop != null) {
                        Property.log.warn("Enum values other than \"inherit\" not yet supported for the font shorthand.");
                        return null;
                     }

                     spaceIndex = value.indexOf(32);
                     int quoteIndex = value.indexOf(39) == -1 ? value.indexOf(34) : value.indexOf(39);
                     if (spaceIndex == -1 || quoteIndex != -1 && spaceIndex > quoteIndex) {
                        throw new PropertyException("Invalid property value: font=\"" + value + "\"");
                     }

                     PropertyMaker m = null;
                     int fromIndex = spaceIndex + 1;
                     int toIndex = value.length();
                     boolean fontFamilyParsed = false;
                     int commaIndex = value.indexOf(44);

                     while(true) {
                        while(!fontFamilyParsed) {
                           if (commaIndex == -1) {
                              if (quoteIndex != -1) {
                                 fromIndex = quoteIndex;
                              }

                              m = FObj.getPropertyMakerFor(PROP_IDS[1]);
                              prop = m.make(propertyList, specVal.substring(fromIndex), fo);
                              newProp.addProperty(prop, 1);
                              fontFamilyParsed = true;
                           } else {
                              if (quoteIndex != -1 && quoteIndex < commaIndex) {
                                 fromIndex = quoteIndex;
                                 quoteIndex = -1;
                              } else {
                                 fromIndex = value.lastIndexOf(32, commaIndex) + 1;
                              }

                              commaIndex = -1;
                           }
                        }

                        toIndex = fromIndex - 1;
                        fromIndex = value.lastIndexOf(32, toIndex - 1) + 1;
                        value = specVal.substring(fromIndex, toIndex);
                        int slashIndex = value.indexOf(47);
                        String fontSize = value.substring(0, slashIndex == -1 ? value.length() : slashIndex);
                        m = FObj.getPropertyMakerFor(PROP_IDS[0]);
                        prop = m.make(propertyList, fontSize, fo);
                        propertyList.putExplicit(PROP_IDS[0], prop);
                        newProp.addProperty(prop, 0);
                        String val;
                        if (slashIndex != -1) {
                           val = value.substring(slashIndex + 1);
                           m = FObj.getPropertyMakerFor(PROP_IDS[2]);
                           prop = m.make(propertyList, val, fo);
                           newProp.addProperty(prop, 2);
                        }

                        if (fromIndex != 0) {
                           toIndex = fromIndex - 1;
                           value = specVal.substring(0, toIndex);
                           fromIndex = 0;
                           spaceIndex = value.indexOf(32);

                           do {
                              toIndex = spaceIndex == -1 ? value.length() : spaceIndex;
                              val = value.substring(fromIndex, toIndex);
                              int i = 6;

                              while(true) {
                                 --i;
                                 if (i < 3) {
                                    fromIndex = toIndex + 1;
                                    spaceIndex = value.indexOf(32, fromIndex);
                                    break;
                                 }

                                 if (newProp.list.get(i) == null) {
                                    m = FObj.getPropertyMakerFor(PROP_IDS[i]);
                                    val = m.checkValueKeywords(val);
                                    prop = m.checkEnumValues(val);
                                    if (prop != null) {
                                       newProp.addProperty(prop, i);
                                    }
                                 }
                              }
                           } while(toIndex != value.length());
                        }
                        break label118;
                     }
                  }

                  newProp.addProperty((Property)null, spaceIndex);
               }
            }

            if (newProp.list.get(0) != null && newProp.list.get(1) != null) {
               return newProp;
            } else {
               throw new PropertyException("Invalid property value: font-size and font-family are required for the font shorthand\nfont=\"" + value + "\"");
            }
         } catch (PropertyException var18) {
            var18.setLocator(propertyList.getFObj().getLocator());
            var18.setPropertyName(this.getName());
            throw var18;
         }
      }
   }
}
