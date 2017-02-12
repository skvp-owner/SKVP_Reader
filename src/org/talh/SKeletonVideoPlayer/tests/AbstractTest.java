package org.talh.SKeletonVideoPlayer.tests;

public abstract class AbstractTest {
	protected abstract int run();
	
	public int runTest() {
		int result = run();
		if (result != 0) {
			System.out.println("Test failed with result: " + result);
			return result;
		}
		System.out.println("Test Passed!");
		return 0;
	}
}
