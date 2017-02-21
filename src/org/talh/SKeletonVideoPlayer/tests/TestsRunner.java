package org.talh.SKeletonVideoPlayer.tests;

import java.util.LinkedList;
import java.util.List;

public class TestsRunner {
	public static void main(String args[]) {
		List<AbstractTest> tests = new LinkedList<AbstractTest>();
		tests.add(new Test1());
		tests.add(new Test2());
		tests.add(new Test3());
		tests.add(new Test4());
		tests.add(new Test5());
		tests.add(new Test6());
		tests.add(new Test7());
		tests.add(new Test8());
		tests.add(new Test9());
		tests.add(new Test10());
		tests.add(new Test11());
		tests.add(new Test12());
		tests.add(new Test13());
		tests.add(new Test14());
		tests.add(new Test15());
		tests.add(new Test16());
		tests.add(new Test17());
		tests.add(new Test18());
		tests.add(new Test19());
		tests.add(new Test20());
		tests.add(new Test21());
		tests.add(new Test22());
		tests.add(new Test23());
		tests.add(new Test24());
		tests.add(new Test25());
		tests.add(new Test26());
		tests.add(new Test27());
		tests.add(new Test28());
		tests.add(new Test29());
		tests.add(new Test30());
		tests.add(new Test31());
		tests.add(new Test32());
		tests.add(new Test33());
		tests.add(new Test34());
		tests.add(new Test35());
		tests.add(new Test36());
		tests.add(new Test37());
		tests.add(new Test38());
		tests.add(new Test39());
		tests.add(new Test40());
		tests.add(new Test41());
		tests.add(new Test42());
		tests.add(new Test43());
		tests.add(new Test44());
		tests.add(new Test45());
		tests.add(new Test46());
		tests.add(new Test47());
		tests.add(new Test48());
		tests.add(new Test49());
		tests.add(new Test50());
		tests.add(new Test51());
		tests.add(new Test52());
		tests.add(new Test53());
		tests.add(new Test54());
		tests.add(new Test55());
		tests.add(new Test56());
		tests.add(new Test57());
		tests.add(new Test58());
		int num = 0;		
		int numPassed = 0;
		for (AbstractTest test : tests) {
			System.out.println("Running test " + (num + 1) + " of " + tests.size());
			int status = test.runTest();
			if (status == 0) {
				numPassed += 1;
			}
			num++;
		}
		System.out.println();
		System.out.println("** SUMAMRY **");
		System.out.println(" TOTAL TESTS: " + num);
		System.out.println("TESTS PASSED: " + numPassed);
		System.out.println("TESTS FAILED: " + (num - numPassed));
		System.out.println();
		System.out.println("SUCCESS RATE: " + (int)(100.0 * numPassed / num) + "%");
	}
}


