package org.talh.SKeletonVideoPlayer.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.talh.SKeletonVideoPlayer.Coordinate3D;
import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;
import org.talh.SKeletonVideoPlayer.SkeletonVideoFrame;

public class Test58 extends AbstractTest {

	protected int run() {
		ClassLoader cl = this.getClass().getClassLoader();
		URL resourceUrl = cl.getResource("org/talh/SKeletonVideoPlayer/tests/resources/file56.skvp");
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
		for (int i = 0 ; i < 20 ; i++) {
			SkeletonVideoFrame frame = null;
			boolean gotSyntaxError = false;
			try {
				frame = reader.getNextFrame();
			} catch (IOException e) {
				return 5;
			} catch (SKVPNonInitializedReaderException e) {
				return 6;
			} catch (SKVPSyntaxErrorException e) {
				if (i != 10 && i != 13 && i != 14 && i != 15) {
					return 7;
				}
				gotSyntaxError = true;
			}
			if (i == 10 && i == 13 && i == 14 && i == 15 && (! gotSyntaxError)) {
				return 77;
			}
			Coordinate3D cameraLocation;
			Coordinate3D cameraDestination;
			double cameraSceneRotation;
			try {
				cameraLocation = reader.getCameraLocation();
				cameraDestination = reader.getCameraDestination();
				cameraSceneRotation = reader.getCameraSceneRotation();
			} catch (SKVPNonInitializedReaderException e1) {
				return 8;
			}			
			if (i == 3) {
				if (cameraLocation.getX() != frame.getCameraLocation().getX()) {
					return 9;
				}
				if (cameraDestination.getX() != frame.getCameraDestination().getX()) {
					return 10;
				}
				if (! areDoublesEqual(cameraSceneRotation, frame.getCameraSceneRotation())) {
					return 11;
				}
			}
			if (i == 4) {
				if (cameraLocation.getX() == frame.getCameraLocation().getX()) {
					return 12;
				}
				if (frame.getCameraLocation().getX() != 3) {
					return 13;
				}
				if (cameraDestination.getX() != frame.getCameraDestination().getX()) {
					return 14;
				}
				if (! areDoublesEqual(cameraSceneRotation, frame.getCameraSceneRotation())) {
					return 15;
				}
			}
			if (i == 6) {
				if (cameraLocation.getX() == frame.getCameraLocation().getX()) {
					return 16;
				}
				if (cameraDestination.getX() == frame.getCameraDestination().getX()) {
					return 17;
				}
				if (areDoublesEqual(cameraSceneRotation, frame.getCameraSceneRotation())) {
					return 18;
				}
				if (frame.getCameraLocation().getX() != 3) {
					return 19;
				}
				if (frame.getCameraDestination().getX() != 6) {
					return 20;
				}
				if ( ! areDoublesEqual(frame.getCameraSceneRotation(), 9.87)) {
					return 21;
				}
			}
			if (i == 8) {
				if (cameraLocation.getX() == frame.getCameraLocation().getX()) {
					return 22;
				}
				if (cameraDestination.getX() == frame.getCameraDestination().getX()) {
					return 23;
				}
				if (areDoublesEqual(cameraSceneRotation, frame.getCameraSceneRotation())) {
					return 24;
				}
				if (frame.getCameraLocation().getX() != 33) {
					return 25;
				}
				if (frame.getCameraDestination().getX() != 66) {
					return 26;
				}
				if ( ! areDoublesEqual(frame.getCameraSceneRotation(), 99.8877)) {
					return 27;
				}
			}
		}
		
		return 0;
	}
	
}
