/**
 * Project Name:trustsql_sdk
 * File Name:Base58.java
 * Package Name:com.tencent.trustsql.sdk.util
 * Date:Jul 26, 20172:48:58 PM
 * Copyright (c) 2017, Tencent All Rights Reserved.
 *
 */
package org.rockyang.jblock.crypto;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Base58 编码算法实现
 * @author yangjian
 */
public class Base58 {

	public static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
	private static final char ENCODED_ZERO = ALPHABET[0];
	private static final int[] INDEXES = new int[128];

	static {
		Arrays.fill(INDEXES, -1);
		for (int i = 0; i < ALPHABET.length; i++) {
			INDEXES[ALPHABET[i]] = i;
		}
	}

	/**
	 * Encodes the given bytes as a base58 string (no checksum is appended).
	 * @param input the bytes to encode
	 * @return the base58-encoded string
	 */
	public static String encode(byte[] input) {
		if (input.length == 0) {
			return "";
		}
		// Count leading zeros.
		int zeros = 0;
		while (zeros < input.length && input[zeros] == 0) {
			++zeros;
		}
		// Convert base-256 digits to base-58 digits (plus conversion to ASCII
		// characters)
		input = Arrays.copyOf(input, input.length);
		// in-place
		char[] encoded = new char[input.length * 2];
		int outputStart = encoded.length;
		for (int inputStart = zeros; inputStart < input.length;) {
			encoded[--outputStart] = ALPHABET[divmod(input, inputStart, 256, 58)];
			if (input[inputStart] == 0) {
				// optimization - skip leading zeros
				++inputStart;
			}
		}
		// Preserve exactly as many leading encoded zeros in output as there
		// were leading zeros in input.
		while (outputStart < encoded.length && encoded[outputStart] == ENCODED_ZERO) {
			++outputStart;
		}
		while (--zeros >= 0) {
			encoded[--outputStart] = ENCODED_ZERO;
		}
		// Return encoded string (including encoded leading zeros).
		return new String(encoded, outputStart, encoded.length - outputStart);
	}

	/**
	 * Decodes the given base58 string into the original data bytes.
	 *
	 * @param input
	 *            the base58-encoded string to decode
	 * @return the decoded data bytes
	 * @throws RuntimeException
	 *             if the given string is not a valid base58 string
	 */
	public static byte[] decode(String input) throws RuntimeException {
		if (input.length() == 0) {
			return new byte[0];
		}
		// Convert the base58-encoded ASCII chars to a base58 byte sequence
		// (base58 digits).
		byte[] input58 = new byte[input.length()];
		for (int i = 0; i < input.length(); ++i) {
			char c = input.charAt(i);
			int digit = c < 128 ? INDEXES[c] : -1;
			if (digit < 0) {
				throw new RuntimeException("Illegal character " + c + " at position " + i);
			}
			input58[i] = (byte) digit;
		}
		// Count leading zeros.
		int zeros = 0;
		while (zeros < input58.length && input58[zeros] == 0) {
			++zeros;
		}
		// Convert base-58 digits to base-256 digits.
		byte[] decoded = new byte[input.length()];
		int outputStart = decoded.length;
		for (int inputStart = zeros; inputStart < input58.length;) {
			decoded[--outputStart] = divmod(input58, inputStart, 58, 256);
			if (input58[inputStart] == 0) {
				// optimization - skip leading zeros
				++inputStart;
			}
		}
		// Ignore extra leading zeroes that were added during the calculation.
		while (outputStart < decoded.length && decoded[outputStart] == 0) {
			++outputStart;
		}
		// Return decoded data (including original number of leading zeros).
		return Arrays.copyOfRange(decoded, outputStart - zeros, decoded.length);
	}

	public static BigInteger decodeToBigInteger(String input) throws RuntimeException {
		return new BigInteger(1, decode(input));
	}

	/**
	 * Decodes the given base58 string into the original data bytes, using the
	 * checksum in the last 4 bytes of the decoded data to verify that the rest
	 * are correct. The checksum is removed from the returned data.
	 *
	 * @param input
	 *            the base58-encoded string to decode (which should include the
	 *            checksum)
	 * @throws AddressFormatException
	 *             if the input is not base 58 or the checksum does not
	 *             validate.
	 *
	 *             public static byte[] decodeChecked(String input) throws
	 *             AddressFormatException { byte[] decoded = decode(input); if
	 *             (decoded.length < 4) throw new
	 *             AddressFormatException("Input too short"); byte[] data =
	 *             Arrays.copyOfRange(decoded, 0, decoded.length - 4); byte[]
	 *             checksum = Arrays.copyOfRange(decoded, decoded.length - 4,
	 *             decoded.length); byte[] actualChecksum =
	 *             Arrays.copyOfRange(Sha256Hash.hashTwice(data), 0, 4); if
	 *             (!Arrays.equals(checksum, actualChecksum)) throw new
	 *             AddressFormatException("Checksum does not validate"); return
	 *             data; }
	 */

	/**
	 * Divides a number, represented as an array of bytes each containing a
	 * single digit in the specified base, by the given divisor. The given
	 * number is modified in-place to contain the quotient, and the return value
	 * is the remainder.
	 *
	 * @param number
	 *            the number to divide
	 * @param firstDigit
	 *            the index within the array of the first non-zero digit (this
	 *            is used for optimization by skipping the leading zeros)
	 * @param base
	 *            the base in which the number's digits are represented (up to
	 *            256)
	 * @param divisor
	 *            the number to divide by (up to 256)
	 * @return the remainder of the division operation
	 */
	private static byte divmod(byte[] number, int firstDigit, int base, int divisor) {
		// this is just long division which accounts for the base of the input
		// digits
		int remainder = 0;
		for (int i = firstDigit; i < number.length; i++) {
			int digit = (int) number[i] & 0xFF;
			int temp = remainder * base + digit;
			number[i] = (byte) (temp / divisor);
			remainder = temp % divisor;
		}
		return (byte) remainder;
	}
}
