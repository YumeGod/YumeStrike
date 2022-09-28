package com.xmlmind.fo.font;

final class DiacriticUtil {
   private static char[] diacritics = new char[768];

   private DiacriticUtil() {
   }

   private static final void mapRange(int var0, int var1, char var2) {
      while(var0 <= var1) {
         diacritics[var0] = var2;
         ++var0;
      }

   }

   private static final void mapChar(int var0, char var1) {
      diacritics[var0] = var1;
   }

   public static final char collapse(char var0) {
      if (var0 >= diacritics.length) {
         return var0;
      } else {
         char var1 = diacritics[var0];
         return var1 == 0 ? var0 : var1;
      }
   }

   static {
      for(int var0 = 0; var0 < diacritics.length; ++var0) {
         diacritics[var0] = (char)var0;
      }

      mapRange(192, 197, 'A');
      mapChar(198, 'æ');
      mapChar(199, 'C');
      mapRange(200, 203, 'E');
      mapRange(204, 207, 'I');
      mapChar(209, 'N');
      mapRange(210, 216, 'O');
      mapRange(217, 220, 'U');
      mapChar(221, 'Y');
      mapRange(224, 229, 'a');
      mapChar(231, 'c');
      mapRange(232, 235, 'e');
      mapRange(236, 239, 'i');
      mapChar(240, 'Ð');
      mapChar(241, 'n');
      mapRange(242, 248, 'o');
      mapRange(249, 252, 'u');
      mapChar(253, 'y');
      mapChar(254, 'Þ');
      mapChar(255, 'y');
      mapRange(256, 261, 'a');
      mapRange(262, 269, 'c');
      mapRange(270, 273, 'd');
      mapRange(274, 283, 'e');
      mapRange(284, 291, 'g');
      mapRange(292, 295, 'h');
      mapRange(296, 305, 'i');
      mapChar(307, 'Ĳ');
      mapRange(308, 309, 'j');
      mapRange(310, 311, 'k');
      mapRange(313, 322, 'l');
      mapRange(323, 329, 'n');
      mapRange(332, 337, 'o');
      mapRange(340, 345, 'r');
      mapRange(346, 353, 's');
      mapRange(354, 359, 't');
      mapRange(360, 371, 'u');
      mapRange(372, 373, 'w');
      mapRange(374, 376, 'y');
      mapRange(377, 382, 'z');
      mapChar(383, 's');
      mapRange(384, 387, 'b');
      mapChar(390, 'o');
      mapRange(391, 392, 'c');
      mapRange(393, 396, 'd');
      mapChar(398, 'E');
      mapChar(400, 'E');
      mapChar(401, 'F');
      mapChar(402, 'f');
      mapChar(403, 'G');
      mapChar(407, 'I');
      mapChar(408, 'K');
      mapChar(409, 'k');
      mapChar(410, 'l');
      mapChar(412, 'M');
      mapChar(413, 'N');
      mapChar(414, 'n');
      mapChar(415, 'O');
      mapChar(416, 'O');
      mapChar(417, 'o');
      mapChar(419, 'Ƣ');
      mapChar(420, 'P');
      mapChar(421, 'p');
      mapChar(427, 't');
      mapChar(428, 'T');
      mapChar(429, 't');
      mapChar(430, 'T');
      mapChar(431, 'U');
      mapChar(432, 'u');
      mapChar(434, 'V');
      mapChar(435, 'Y');
      mapChar(436, 'y');
      mapChar(437, 'Z');
      mapChar(438, 'z');
      mapChar(440, 'Ʒ');
      mapChar(441, 'Ʒ');
      mapChar(442, 'Ʒ');
      mapChar(461, 'A');
      mapChar(462, 'a');
      mapChar(463, 'I');
      mapChar(464, 'i');
      mapChar(465, 'O');
      mapChar(466, 'o');
      mapRange(467, 476, 'u');
      mapChar(477, 'e');
      mapChar(478, 'A');
      mapChar(479, 'a');
      mapChar(480, 'A');
      mapChar(481, 'a');
      mapChar(482, 'Æ');
      mapChar(483, 'Æ');
      mapChar(484, 'G');
      mapChar(485, 'g');
      mapChar(486, 'G');
      mapChar(487, 'g');
      mapChar(488, 'K');
      mapChar(489, 'k');
      mapChar(490, 'O');
      mapChar(491, 'o');
      mapChar(492, 'O');
      mapChar(493, 'o');
      mapChar(494, 'Ʒ');
      mapChar(495, 'Ʒ');
      mapChar(496, 'j');
      mapChar(499, 'Ǳ');
      mapChar(500, 'G');
      mapChar(501, 'g');
      mapChar(506, 'A');
      mapChar(507, 'a');
      mapChar(508, 'Æ');
      mapChar(509, 'Æ');
      mapChar(510, 'O');
      mapChar(511, 'o');
      mapChar(512, 'A');
      mapChar(513, 'a');
      mapChar(514, 'A');
      mapChar(515, 'a');
      mapChar(516, 'E');
      mapChar(517, 'e');
      mapChar(518, 'E');
      mapChar(519, 'e');
      mapChar(520, 'I');
      mapChar(521, 'i');
      mapChar(522, 'I');
      mapChar(523, 'i');
      mapChar(524, 'O');
      mapChar(525, 'o');
      mapChar(526, 'O');
      mapChar(527, 'o');
      mapChar(528, 'R');
      mapChar(529, 'r');
      mapChar(530, 'R');
      mapChar(531, 'r');
      mapChar(532, 'U');
      mapChar(533, 'u');
      mapChar(534, 'U');
      mapChar(535, 'u');
      mapChar(592, 'a');
      mapChar(595, 'b');
      mapChar(596, 'o');
      mapChar(597, 'c');
      mapChar(598, 'd');
      mapChar(599, 'd');
      mapChar(600, 'e');
      mapChar(603, 'e');
      mapChar(604, 'e');
      mapChar(605, 'e');
      mapChar(606, 'e');
      mapChar(607, 'j');
      mapChar(608, 'g');
      mapChar(609, 'g');
      mapChar(610, 'G');
      mapChar(613, 'h');
      mapChar(614, 'h');
      mapChar(616, 'i');
      mapChar(618, 'I');
      mapChar(619, 'l');
      mapChar(620, 'l');
      mapChar(621, 'l');
      mapChar(623, 'm');
      mapChar(624, 'm');
      mapChar(625, 'm');
      mapChar(626, 'n');
      mapChar(627, 'n');
      mapChar(628, 'N');
      mapChar(629, 'o');
      mapChar(630, 'Œ');
      mapChar(633, 'r');
      mapChar(634, 'r');
      mapChar(635, 'r');
      mapChar(636, 'r');
      mapChar(637, 'r');
      mapChar(638, 'r');
      mapChar(639, 'r');
      mapChar(640, 'R');
      mapChar(641, 'R');
      mapChar(642, 's');
      mapChar(644, 'j');
      mapChar(647, 't');
      mapChar(648, 't');
      mapChar(649, 'u');
      mapChar(651, 'v');
      mapChar(652, 'v');
      mapChar(653, 'w');
      mapChar(654, 'z');
      mapChar(655, 'Y');
      mapChar(656, 'z');
      mapChar(657, 'z');
      mapChar(658, 'Ʒ');
      mapChar(659, 'Ʒ');
      mapChar(663, 'c');
      mapChar(665, 'B');
      mapChar(666, 'e');
      mapChar(667, 'G');
      mapChar(668, 'H');
   }
}
