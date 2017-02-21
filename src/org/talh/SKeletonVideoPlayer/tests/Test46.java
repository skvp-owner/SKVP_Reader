package org.talh.SKeletonVideoPlayer.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;

public class Test46 extends AbstractTest {

	protected int run() {
		ClassLoader cl = this.getClass().getClassLoader();
		URL resourceUrl = cl.getResource("org/talh/SKeletonVideoPlayer/tests/resources/file44.skvp");
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
			if (cameraSceneRotation != 0) {
				return 5;
			}
		} catch (SKVPNonInitializedReaderException e) {
			return 6;
		}
		try {
			long numFrames = reader.getNumFrames();
			if (numFrames != -1) {
				return 7;
			}
		} catch (SKVPNonInitializedReaderException e) {
			return 8;
		}
				
		return 0;
	}
	
}
