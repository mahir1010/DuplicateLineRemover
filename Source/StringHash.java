package openEndedProblems.removeDuplicateString.algorithm;

import java.nio.charset.StandardCharsets;


/************************************************************
 ************************************************************
	Calculates Two different Hash codes according to 
	the character's ascii value and their position
 ************************************************************
 ***********************************************************/

public class StringHash {
	public StringHash(String inp) {
		s = inp;
		hash1 = hash1Calculator(this);
		hash2 = hash2Calculator(this);
	}

	public String getString() {
		return s;
	}
	public short getHash1() {
		return hash1;
	}

	public short getHash2() {
		return hash2;
	}

	private static short hash1Calculator(StringHash obj) {
		short result = 0;
		int temp;
		byte[] b = obj.getString().getBytes(StandardCharsets.US_ASCII);
		int l=b.length;
		for (int i =1; i <=l/2; i++) {
			temp = b[i - 1] * i + ((1234-b[i]) / i);
			b[i - 1] = (byte)(temp % 127);
		}

		for (byte by : b) {
			result += by;
		}
		return result;
	}

	private static short hash2Calculator(StringHash obj) {
		short result = 0;
		int temp;
		byte[] b = obj.getString().getBytes(StandardCharsets.US_ASCII);
		int l=b.length;
		for (int i = l/2; i < l; i++) {
			temp = (b[i] * i) - (b[i] / i);
			b[i] = ((byte)(temp % 127));
		}
		for (byte by : b) {
			result += by;
		}
		return result;
	}
	private String s;
	private short hash1;
	private short hash2;
}