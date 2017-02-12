package org.talh.SKeletonVideoPlayer.tests;

import java.io.File;
import java.io.IOException;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;

public class Test1 extends AbstractTest {

	protected int run() {
		try {
			new SKVPReader(new File("/path/to/nowhere"));
		} catch (SKVPSyntaxErrorException e) {
			return 1;
		} catch (IOException e) {
			return 0;
		} catch (SKVPIllegalValueException e) {
			return 2;
		}
		return 3;		
	}
	
}
