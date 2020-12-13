package com.elwonder;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
	public static void transform() {
		String s = BinaryStdIn.readString();
		CircularSuffixArray suffixes = new CircularSuffixArray(s);
		int defaultSIndex = 0;
		while (suffixes.index(defaultSIndex) != 0) {
			defaultSIndex++;
		}
		BinaryStdOut.write(defaultSIndex);
		for (int i = 0; i < s.length(); i++) {
			BinaryStdOut.write(s.charAt(
				(suffixes.index(i) + s.length() - 1) % s.length()));
		}
		BinaryStdOut.close();
	}

	public static void inverseTransform() {

	}

	public static void main(final String[] args) {
		String sign = args[0];
		if (sign.equals("-")) {
			transform();
		}
		if (sign.equals("+")) {
			inverseTransform();
		}
	}
}
