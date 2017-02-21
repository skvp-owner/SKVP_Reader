package org.talh.SKeletonVideoPlayer.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;

public class Test57 extends AbstractTest {

	protected int run() {
		ClassLoader cl = this.getClass().getClassLoader();
		URL resourceUrl = cl.getResource("org/talh/SKeletonVideoPlayer/tests/resources/file55.skvp");
		if (resourceUrl == null) {
			return 1;
		}
		String filePath = resourceUrl.getFile();
		SKVPReader reader;
		try {
			reader = new SKVPReader(new File(filePath));
		} catch (SKVPSyntaxErrorException e) {
			return 2;
		} catch (IOException e) {
			return 3;
		} catch (SKVPIllegalValueException e) {
			return 4;
		}
		try {
			double cameraSceneRotation = reader.getCameraSceneRotation();
			if (! areDoublesEqual(25.42, cameraSceneRotation)) {
				return 5;
			}
		} catch (SKVPNonInitializedReaderException e) {
			return 6;
		}
		
		
		return 0;
	}
	
}
