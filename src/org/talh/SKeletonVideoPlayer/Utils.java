package org.talh.SKeletonVideoPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.paint.Color;

public class Utils {
	
	static void findSKVPHeaderBeginning(BufferedReader reader) throws IOException, SKVPSyntaxErrorException {
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.equals("")) {
				continue;
			}
			if (! line.equals(Defs.FILE_HEADER_FIRST_LINE)) {
				throw new SKVPSyntaxErrorException("SKVP file must start with line: " + Defs.FILE_HEADER_FIRST_LINE);
			}
			return;
		}
		throw new SKVPSyntaxErrorException("Empty SKVP file");
	}

	static HashMap<String, String> findSKVPVideoStartAndGetHeaderEntries(BufferedReader reader) throws SKVPSyntaxErrorException, IOException {
		HashMap<String, String> entries = new HashMap<String, String>();
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.equals("")) {
				continue;
			}
			if (line.equals(Defs.VIDEO_HEADER_LINE)) {
				return entries;
			}
			String[] parts = line.split("=", 2);
			if (parts.length != 2) {
				throw new SKVPSyntaxErrorException("Header lines must be in the format: ENTRY_NAME=ENTRY_VALUE");
			}
			entries.put(parts[0].trim(), parts[1].trim());
		}
		throw new SKVPSyntaxErrorException("Could not find line which indicates starting of video: " + Defs.VIDEO_HEADER_LINE);
	}

	static HashMap<Integer, HashSet<Integer>> parseConnectionsString(String connectionsString, int numJoints) throws SKVPIllegalValueException, SKVPSyntaxErrorException {
		HashMap<Integer, HashSet<Integer>> connections = createAnEmptyConnectionsHash(numJoints);
		String syntaxErrorMessage = "A connection must be defined as A-B where A,B are numbers representing the joints";
		if (connectionsString.trim().equals("")) {
			return connections;
		}
		String[] connectionsAsStrings = connectionsString.split(",");
		for (String connection : connectionsAsStrings) {
			String[] parts = connection.split("-");
			if (parts.length != 2) {
				throw new SKVPSyntaxErrorException(syntaxErrorMessage);
			}
			int joint_1;
			int joint_2;
			try {
				joint_1 = Integer.parseInt(parts[0]);
				joint_2 = Integer.parseInt(parts[1]);
			} catch (NumberFormatException e) {
				throw new SKVPSyntaxErrorException(syntaxErrorMessage);
			}
			if (joint_1 < 1 || joint_1 > numJoints) {
				throw new SKVPIllegalValueException("Connection can't be defined for a non existing joint: " + joint_1);
			}
			if (joint_2 < 1 || joint_2 > numJoints) {
				throw new SKVPIllegalValueException("Connection can't be defined for a non existing joint: " + joint_2);
			}
			if (joint_1 == joint_2) {
				throw new SKVPIllegalValueException("Cannot connect a joint with itself");
			}
			connections.get(joint_1).add(joint_2);
			connections.get(joint_2).add(joint_1);
		}
		
		return connections;
	}

	public static SkeletonVideoFrame stringToSkeletonVideoFrame(String line, int numJoints, Coordinate3D cameraLocation, Coordinate3D cameraDestination, double cameraSceneRotation) throws SKVPSyntaxErrorException {
		String[] jointsAsStrings = line.split(";");
		if (jointsAsStrings.length != numJoints) {
			throw new SKVPSyntaxErrorException("Frame description does not match number of joints: " + numJoints);
		}
		SkeletonVideoFrame frame = new SkeletonVideoFrame(numJoints);
		for (int i = 0 ; i < numJoints ; i++) {
			frame.setCoordinate(i + 1, stringToCoordinate3D(jointsAsStrings[i]));
		}
		frame.setCameraLocation(cameraLocation);
		frame.setCameraDestination(cameraDestination);
		frame.setCameraSceneRotation(cameraSceneRotation);
		
		return frame;
	}

	public static Coordinate3D stringToCoordinate3D(String jointAsString) throws SKVPSyntaxErrorException {
		String[] parts = jointAsString.split(",");
		if (parts.length != 3) {
			throw new SKVPSyntaxErrorException("A joint 3d coordinate must contain exactly 3 coordinates");
		}
		double x;
		double y;
		double z;
		try {
			x = Double.parseDouble(parts[0]);
			y = Double.parseDouble(parts[1]);
			z = Double.parseDouble(parts[2]);
		} catch (NumberFormatException e) {
			throw new SKVPSyntaxErrorException("Coordinates must be real numbers");
		}
		
		return new Coordinate3D(x, y, z);
	}

	public static HashMap<Integer, HashSet<Integer>> createAnEmptyConnectionsHash(int numJoints) {
		HashMap<Integer, HashSet<Integer>> connections = new HashMap<Integer, HashSet<Integer>>();
		for (int i = 0 ; i < numJoints ; i++) {
			connections.put(i + 1, new HashSet<Integer>());
		}
		
		return connections;
	}

	public static double[] createDefaultRadiusesArray(int numElements) {
		double[] radiuses = new double[numElements];
		for (int i = 0 ; i < numElements ; i++) {
			radiuses[i] = Defs.DEFAULT_RADIUS;
		}
		
		return radiuses;
	}

	public static double[] parseRadiusesString(String radiusesString, int numRequiredInputs) throws SKVPIllegalValueException, SKVPSyntaxErrorException {
		double[] radiuses = new double[numRequiredInputs];
		if (radiusesString.trim().equals("")) {
			return createDefaultRadiusesArray(numRequiredInputs);
		}
		String[] radiusesAsStrings = radiusesString.split(",");
		if (radiusesAsStrings.length != numRequiredInputs) {
			throw new SKVPIllegalValueException("Number of input radiuses does not match number of elements");
		}
		int i = 0;
		for (String radiusAsString : radiusesAsStrings) {
			double radius;
			try {
				radius = Double.parseDouble(radiusAsString.trim());
				if (radius < 0) {
					throw new SKVPIllegalValueException("Radiuses must not be negative");
				}
			} catch (NumberFormatException e) {
				throw new SKVPSyntaxErrorException("Input radius is not a number");
			}
			radiuses[i] = radius;
			i++;
		}
		return radiuses;
	}

	public static ElementColor[] createDefaultColorsArray(int numElements) {
		ElementColor[] colors = new ElementColor[numElements];
		for (int i = 0 ; i < numElements ; i++) {
			colors[i] = new ElementColor();
			colors[i].setDiffuseColor(Defs.DEFAULT_DIFFUSE_COLOR);
			colors[i].setSpecularColor(Defs.DEFAULT_SPECULAR_COLOR);
		}
		
		return colors;
	}

	public static ElementColor[] parseColorsString(String colorsString, int numRequiredInputs) throws SKVPIllegalValueException, SKVPSyntaxErrorException {
		ElementColor[] colors = new ElementColor[numRequiredInputs];
		if (colorsString.trim().equals("")) {
			return createDefaultColorsArray(numRequiredInputs);
		}
		String[] colorsAsStrings = colorsString.split(";");
		if (colorsAsStrings.length != numRequiredInputs) {
			throw new SKVPIllegalValueException("Number of input colors does not match number of elements");
		}
		int i = 0;
		for (String colorTupleAsString : colorsAsStrings) {
			colors[i] = new ElementColor();
			String[] parts = colorTupleAsString.split(",");
			if (parts.length != 2) {
				throw new SKVPSyntaxErrorException("A color element should contain diffuse and specular colors separataed by comma");
			}
			Color diffuseColor;
			Color specularColor;
			try {
				diffuseColor = Color.web(parts[0].trim());
				specularColor = Color.web(parts[1].trim());
			} catch (IllegalArgumentException e) {
				throw new SKVPIllegalValueException("Input colors must be HTML-like colors");
			}
			colors[i].setDiffuseColor(diffuseColor);
			colors[i].setSpecularColor(specularColor);			
			i++;
		}
		
		return colors;
	}

	public static double parseRealDegree(String degreeAsString) throws SKVPSyntaxErrorException {
		double degree;
		try {
			degree = Double.parseDouble(degreeAsString);			
		} catch (NumberFormatException e) {
			throw new SKVPSyntaxErrorException("Input degree is not a real number");
		}
		if (degree < 0) {
			// Setting the degree positive
			long numFullRoutes = ((long)(degree * (-1)) / 360);
			degree = 360 * (numFullRoutes + 1) + degree;
		}
		long degreeFloor = (int)degree;
		double realDiff = degree - degreeFloor;
		
		return degreeFloor % 360 + realDiff;
	}
	
	public static String getValueFromCameraLine(String line, String entryName) throws SKVPSyntaxErrorException {
		String[] parts = line.split("=");
		if (parts.length != 2) {
			throw new SKVPSyntaxErrorException("Illegal camera paremeter line");
		}
		if (entryName != null && (! entryName.equals(parts[0].trim()))) {
			throw new SKVPSyntaxErrorException("Entry name is illegal");
		}
		return parts[1].trim();
	}
	
}
