package org.talh.SKeletonVideoPlayer.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPNonInitializedReaderException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;
import org.talh.SKeletonVideoPlayer.SkeletonVideoFrame;


public class Test36 extends AbstractTest {

	protected int run() {
		ClassLoader cl = this.getClass().getClassLoader();
		URL resourceUrl = cl.getResource("org/talh/SKeletonVideoPlayer/tests/resources/file34.skvp");
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
		HashMap<Integer, HashSet<Integer>> con;
		try {
			con = reader.getJointConnections();
		} catch (SKVPNonInitializedReaderException e1) {
			return 37;
		}
		if (con.get(1).size() != 0) {
			return 38;
		}
		if (con.get(2).size() != 0) {
			return 39;
		}
		if (con.get(3).size() != 0) {
			return 40;
		}
		HashSet<Integer> con1;
		@SuppressWarnings("unused")
		HashSet<Integer> con5;
		try {
			con1 = reader.getJointConnections(1);
		} catch (SKVPNonInitializedReaderException e1) {
			return 41;
		} catch (SKVPIllegalValueException e1) {
			return 42;
		}
		if (con1 != con.get(1)) {
			return 43;
		}
		boolean gotValueException = false;
		try {
			con5 = reader.getJointConnections(5);
		} catch (SKVPNonInitializedReaderException e1) {
			return 44;
		} catch (SKVPIllegalValueException e1) {
			gotValueException = true;
		}
		if (! gotValueException) {
			return 45;
		}		
		SkeletonVideoFrame frame;
		try {
			frame = reader.getNextFrame();
		} catch (IOException e) {
			return 5;
		} catch (SKVPNonInitializedReaderException e) {
			return 6;
		} catch (SKVPSyntaxErrorException e) {
			return 7;
		}
		if (frame == null) {
			return 8;
		}
		if (frame.getCoordinate(1).getZ() != 3.0) {
			return 9;
		}
		if (frame.getAllCoordinates()[1].getY() != 5) {
			return 10;
		}
		List<SkeletonVideoFrame> frames;
		try {
			frames = reader.getNextFrames(5);
		} catch (IOException e) {
			return 11;
		} catch (SKVPNonInitializedReaderException e) {
			return 12;
		} catch (SKVPSyntaxErrorException e) {
			return 13;
		}
		if (frames.size() != 5) {
			return 14;
		}
		if (frames.get(4).getCoordinate(1).getX() != 6) {
			return 15;
		}
		try {
			frames = reader.getNextFrames(5);
		} catch (IOException e) {
			return 16;
		} catch (SKVPNonInitializedReaderException e) {
			return 17;
		} catch (SKVPSyntaxErrorException e) {
			return 18;
		}
		if (frames.size() != 4) {
			return 19;
		}
		if (frames.get(3).getCoordinate(3).getZ() != 9.7) {
			return 20;
		}
		try {
			frames = reader.getNextFrames(5);
		} catch (IOException e) {
			return 21;
		} catch (SKVPNonInitializedReaderException e) {
			return 22;
		} catch (SKVPSyntaxErrorException e) {
			return 23;
		}
		if (frames.size() != 0) {
			return 24;
		}
		try {
			frames = reader.getAllFrames();
		} catch (IOException e) {
			return 25;
		} catch (SKVPNonInitializedReaderException e) {
			return 26;
		} catch (SKVPSyntaxErrorException e) {
			return 27;
		}
		if (frames.size() != 0) {
			return 28;
		}
		try {
			frame = reader.getNextFrame();
		} catch (IOException e) {
			return 29;
		} catch (SKVPNonInitializedReaderException e) {
			return 30;
		} catch (SKVPSyntaxErrorException e) {
			return 31;
		}
		if (frame != null) {
			return 32;
		}
		try {
			if (reader.getFps() != 28.4) {
				return 33;
			}
		} catch (SKVPNonInitializedReaderException e) {
			return 34;
		}
		try {
			if (reader.getNumJoints() != 3) {
				return 35;
			}
		} catch (SKVPNonInitializedReaderException e) {
			return 36;
		}
		try {
			if (reader.getConnectionsRadius() != 4.2){
				return 37;
			}			
		} catch (SKVPNonInitializedReaderException e) {
			return 38;
		}
		
		return 0;
	}
	
}
