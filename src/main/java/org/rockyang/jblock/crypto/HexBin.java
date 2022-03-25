package org.rockyang.jblock.crypto;

/**
 * copy from com.sun.org.apache.xerces.internal.impl.dv.util.HexBin
 * @author yangjian
 */
public final class HexBin {
	private static final int BASELENGTH = 128;
	private static final int LOOKUPLENGTH = 16;
	private static final byte[] hexNumberTable = new byte[128];
	private static final char[] lookUpHexAlphabet = new char[16];

	public HexBin() {
	}

	public static String encode(byte[] binaryData) {
		if (binaryData == null) {
			return null;
		} else {
			int lengthData = binaryData.length;
			int lengthEncode = lengthData * 2;
			char[] encodedData = new char[lengthEncode];

			for(int i = 0; i < lengthData; ++i) {
				int temp = binaryData[i];
				if (temp < 0) {
					temp += 256;
				}

				encodedData[i * 2] = lookUpHexAlphabet[temp >> 4];
				encodedData[i * 2 + 1] = lookUpHexAlphabet[temp & 15];
			}

			return new String(encodedData);
		}
	}

	public static byte[] decode(String encoded) {
		if (encoded == null) {
			return null;
		} else {
			int lengthData = encoded.length();
			if (lengthData % 2 != 0) {
				return null;
			} else {
				char[] binaryData = encoded.toCharArray();
				int lengthDecode = lengthData / 2;
				byte[] decodedData = new byte[lengthDecode];

				for(int i = 0; i < lengthDecode; ++i) {
					char tempChar = binaryData[i * 2];
					byte temp1 = tempChar < 128 ? hexNumberTable[tempChar] : -1;
					if (temp1 == -1) {
						return null;
					}

					tempChar = binaryData[i * 2 + 1];
					byte temp2 = tempChar < 128 ? hexNumberTable[tempChar] : -1;
					if (temp2 == -1) {
						return null;
					}

					decodedData[i] = (byte)(temp1 << 4 | temp2);
				}

				return decodedData;
			}
		}
	}

	static {
		int i;
		for(i = 0; i < 128; ++i) {
			hexNumberTable[i] = -1;
		}

		for(i = 57; i >= 48; --i) {
			hexNumberTable[i] = (byte)(i - 48);
		}

		for(i = 70; i >= 65; --i) {
			hexNumberTable[i] = (byte)(i - 65 + 10);
		}

		for(i = 102; i >= 97; --i) {
			hexNumberTable[i] = (byte)(i - 97 + 10);
		}

		for(i = 0; i < 10; ++i) {
			lookUpHexAlphabet[i] = (char)(48 + i);
		}

		for(i = 10; i <= 15; ++i) {
			lookUpHexAlphabet[i] = (char)(65 + i - 10);
		}

	}
}
