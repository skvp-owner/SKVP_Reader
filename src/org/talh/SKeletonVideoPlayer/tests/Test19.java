package org.talh.SKeletonVideoPlayer.tests;

import java.io.IOException;

import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;

public class Test19 extends AbstractTest {

	protected int run() {
		SKVPReader reader = new SKVPReader();
		try {
			reader.getNextFrame();
		} catch (IOException e) {
			return 1;
		} catch (SKVPNonInitializedReaderException e) {
			return 0;
		} catch (SKVPSyntaxErrorException e) {
			return 2;
		}
		return 3;
	}
	
}
