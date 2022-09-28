package org.w3c.css.sac;

public interface Condition {
   short SAC_AND_CONDITION = 0;
   short SAC_OR_CONDITION = 1;
   short SAC_NEGATIVE_CONDITION = 2;
   short SAC_POSITIONAL_CONDITION = 3;
   short SAC_ATTRIBUTE_CONDITION = 4;
   short SAC_ID_CONDITION = 5;
   short SAC_LANG_CONDITION = 6;
   short SAC_ONE_OF_ATTRIBUTE_CONDITION = 7;
   short SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION = 8;
   short SAC_CLASS_CONDITION = 9;
   short SAC_PSEUDO_CLASS_CONDITION = 10;
   short SAC_ONLY_CHILD_CONDITION = 11;
   short SAC_ONLY_TYPE_CONDITION = 12;
   short SAC_CONTENT_CONDITION = 13;

   short getConditionType();
}
