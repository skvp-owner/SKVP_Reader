package org.talh.SKeletonVideoPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class SKVPReader {
	private InputType inputType;
	private boolean headerReadSuccessfully = false;
	private BufferedReader reader;
	private File inputFile;
	private String inputString;
	private double fps;
	private int numJoints;
	private HashMap<Integer, HashSet<Integer>> jointConnections;
	private double[] jointRadiuses;
	private ElementColor[] jointColors;
	private double connectionsRadius;
	private ElementColor connectionsColor;
	private Coordinate3D cameraLocation;
	private Coordinate3D cameraDestination;
	private double cameraSceneRotation;
	private Coordinate3D cameraLocationTemporal;
	private Coordinate3D cameraDestinationTemporal;
	private double cameraSceneRotationTemporal;
	private long numFrames;
	
	/**
	 * Create a new <b>uninitialized</b> instance of SKVPReader
	 */
	public SKVPReader() {
		
	}	
		
	/**
	 * Create a new instance of SKVPReader, initialized with a given file
	 * 
	 * @param inputFile A File object initialized with the source file path
	 * @throws SKVPSyntaxErrorException
	 * @throws IOException
	 * @throws SKVPIllegalValueException
	 */
	public SKVPReader(File inputFile) throws SKVPSyntaxErrorException, IOException, SKVPIllegalValueException {
		setInputFile(inputFile);
	}
	
	/**
	 * Create a new instance of SKVPReader, initialized with a string that represents an SKVP-formatted
	 * file content
	 * 
	 * @param inputString A string that contains SKVP-formatted data
	 * @throws SKVPSyntaxErrorException
	 * @throws IOException
	 * @throws SKVPIllegalValueException
	 */
	public SKVPReader(String inputString) throws SKVPSyntaxErrorException, IOException, SKVPIllegalValueException {
		setInputString(inputString);
	}	
	
	private void readHeader() throws SKVPSyntaxErrorException, IOException, SKVPIllegalValueException {
		reader = null;
		if (inputType == InputType.STRING) {
			reader = new BufferedReader(new StringReader(inputString));
		} else {
			if (inputFile.getName().toLowerCase().endsWith(".gz")) {
				InputStream fileStream = new FileInputStream(inputFile.getPath());
				InputStream gzipStream = new GZIPInputStream(fileStream);
				Reader decoder = new InputStreamReader(gzipStream);
				reader = new BufferedReader(decoder);
			} else {
				reader = new BufferedReader(new FileReader(inputFile));
			}
		}
		Utils.findSKVPHeaderBeginning(reader);
		HashMap<String, String> headerEntriesAsStrings = Utils.findSKVPVideoStartAndGetHeaderEntries(reader);
		processHeader(headerEntriesAsStrings);
		headerReadSuccessfully = true;
	}	
	
	private void processHeader(HashMap<String, String> headerEntriesAsStrings) throws SKVPSyntaxErrorException, SKVPIllegalValueException {
		if (headerEntriesAsStrings.get(Defs.NUM_JOINTS_ENTRY_NAME) == null) {
			throw new SKVPSyntaxErrorException("Header does not contain entry: " + Defs.NUM_JOINTS_ENTRY_NAME);
		}
		if (headerEntriesAsStrings.get(Defs.FPS_ENTRY_NAME) == null) {
			throw new SKVPSyntaxErrorException("Header does not contain entry: " + Defs.FPS_ENTRY_NAME);
		}
		try {
			this.fps = Double.parseDouble(headerEntriesAsStrings.get(Defs.FPS_ENTRY_NAME));
		} catch (NumberFormatException e) {
			throw new SKVPSyntaxErrorException(Defs.FPS_ENTRY_NAME + " value must be a real number");
		}
		if (this.fps <= 0) {
			throw new SKVPIllegalValueException("FPS must be a positive non-zero real number");
		}
		try {
			this.numJoints = Integer.parseInt(headerEntriesAsStrings.get(Defs.NUM_JOINTS_ENTRY_NAME));
		} catch (NumberFormatException e) {
			throw new SKVPSyntaxErrorException(Defs.NUM_JOINTS_ENTRY_NAME + " value must be a natural number");
		}
		if (this.numJoints < 1) {
			throw new SKVPIllegalValueException("Number of joints must be at least 1");
		}
		if (headerEntriesAsStrings.get(Defs.CONNECTIONS_ENTRY_NAME) == null) {
			this.jointConnections = Utils.createAnEmptyConnectionsHash(numJoints);
		} else {
			this.jointConnections = Utils.parseConnectionsString(headerEntriesAsStrings.get(Defs.CONNECTIONS_ENTRY_NAME), numJoints);
		}
		if (headerEntriesAsStrings.get(Defs.JOINT_RADIUSES_ENTRY_NAME) == null) {
			this.jointRadiuses = Utils.createDefaultRadiusesArray(numJoints);
		} else {
			this.jointRadiuses = Utils.parseRadiusesString(headerEntriesAsStrings.get(Defs.JOINT_RADIUSES_ENTRY_NAME), numJoints);
		}
		if (headerEntriesAsStrings.get(Defs.JOINT_COLORS_ENTRY_NAME) == null) {
			this.jointColors = Utils.createDefaultColorsArray(numJoints);
		} else {
			this.jointColors = Utils.parseColorsString(headerEntriesAsStrings.get(Defs.JOINT_COLORS_ENTRY_NAME), numJoints);
		}
		if (headerEntriesAsStrings.get(Defs.CONNECTIONS_RADIUS_ENTRY_NAME) == null) {
			this.connectionsRadius = Defs.DEFAULT_CONNECTION_RADIUS;
		} else {
			this.connectionsRadius = Utils.parseRadiusesString(headerEntriesAsStrings.get(Defs.CONNECTIONS_RADIUS_ENTRY_NAME), 1)[0];
		}
		if (headerEntriesAsStrings.get(Defs.CONNECTIONS_COLOR_ENTRY_NAME) == null) {
			this.connectionsColor = new ElementColor();
			this.connectionsColor.setDiffuseColor(Defs.DEFAULT_CONNECTION_DIFFUSE_COLOR);
			this.connectionsColor.setSpecularColor(Defs.DEFAULT_CONNECTION_SPECULAR_COLOR);
		} else {
			this.connectionsColor = Utils.parseColorsString(headerEntriesAsStrings.get(Defs.CONNECTIONS_COLOR_ENTRY_NAME), 1)[0];
		}
		if (headerEntriesAsStrings.get(Defs.CAMERA_LOCATION_ENTRY_NAME) == null) {
			this.cameraLocation = Utils.stringToCoordinate3D(Defs.CAMERA_LOCATION_DEFAULT_COORDINATE_STR);
		} else {
			this.cameraLocation = Utils.stringToCoordinate3D(headerEntriesAsStrings.get(Defs.CAMERA_LOCATION_ENTRY_NAME));
		}
		this.cameraLocationTemporal = this.cameraLocation;
		if (headerEntriesAsStrings.get(Defs.CAMERA_DESTINATION_ENTRY_NAME) == null) {
			this.cameraDestination = Utils.stringToCoordinate3D(Defs.CAMERA_DESTINATION_DEFAULT_COORDINATE_STR);
		} else {
			this.cameraDestination = Utils.stringToCoordinate3D(headerEntriesAsStrings.get(Defs.CAMERA_DESTINATION_ENTRY_NAME));
		}
		this.cameraDestinationTemporal = this.cameraDestination;
		if (headerEntriesAsStrings.get(Defs.NUM_FRAMES_ENTRY_NAME) == null) {
			this.numFrames = Defs.UNKNOWN_NUM_FRAMES_RETURN_VAL;
		} else {
			try {
				this.numFrames = Long.parseLong(headerEntriesAsStrings.get(Defs.NUM_FRAMES_ENTRY_NAME));
			} catch (NumberFormatException e) {
				throw new SKVPSyntaxErrorException(Defs.NUM_FRAMES_ENTRY_NAME + " value must be a non-negative integer");
			}
			if (this.numFrames < 0) {
				throw new SKVPIllegalValueException("Number of frames must be a non-negative integer");
			}
		}
		if (headerEntriesAsStrings.get(Defs.CAMERA_SCENE_ROTATION_ENTRY_NAME) == null) {
			this.cameraSceneRotation = Defs.DEFAULT_CAMERA_SCENE_ROTATION;
		} else {
			this.cameraSceneRotation = Utils.parseRealDegree(headerEntriesAsStrings.get(Defs.CAMERA_SCENE_ROTATION_ENTRY_NAME));
		}
		this.cameraSceneRotationTemporal = this.cameraSceneRotation;
	}

	/**
	 * Initialize existing instance of SKVPReader with a given input file
	 * 
	 * @param inputFile A File object initialized with the source file path
	 * @throws SKVPSyntaxErrorException
	 * @throws IOException
	 * @throws SKVPIllegalValueException
	 */
	public synchronized void setInputFile(File inputFile) throws SKVPSyntaxErrorException, IOException, SKVPIllegalValueException {
		headerReadSuccessfully = false;
		this.inputFile = inputFile;
		this.inputString = null;
		inputType = InputType.FILE;
		readHeader();		
	}
	
	/**
	 * Initialize existing instance of SKVPReader with a string that represents an SKVP-formatted
	 * file content
	 * 
	 * @param inputString A string that contains SKVP-formatted data
	 * @throws SKVPSyntaxErrorException
	 * @throws IOException
	 * @throws SKVPIllegalValueException
	 */
	public synchronized void setInputString(String inputString) throws SKVPSyntaxErrorException, IOException, SKVPIllegalValueException {
		headerReadSuccessfully = false;
		this.inputString = inputString;
		this.inputFile = null;
		inputType = InputType.STRING;
		readHeader();
	}
	
	/**
	 * Get the FPS rate defined in the current SKVP file
	 * 
	 * @return Frames Per Second rate of the current SKVP file
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized double getFps() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return fps;
	}
	
	/**
	 * Get Number of frames in current SKVP file, according to the optional value
	 * specified in the SKVP header
	 * 
	 * @return Number of frames as specified in SKVP header, (-1) if not specified
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized long getNumFrames() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return numFrames;
	}
	
	/**
	 * Get the number of joints defined in the current SKVP file
	 * 
	 * @return Number of Joints defined in the current SKVP file
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized int getNumJoints() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return numJoints;
	}
	
	/**
	 * Get the colors defined for the joints
	 * 
	 * 
	 * @return An array containing joint colors
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized ElementColor[] getJointColors() throws SKVPNonInitializedReaderException{
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}		
		return jointColors;
	}
	
	/**
	 * Get the radiuses defined for the joints
	 * 
	 * 
	 * @return An array containing joint radiuses
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized double[] getJointRadiuses() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}		
		return jointRadiuses;
	}
	
	/**
	 * Get the radius defined for the connections
	 * 
	 * @return a real number specifying the radius of the connection cylinders
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized double getConnectionsRadius() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}		
		return connectionsRadius;
	}
	
	/**
	 * Get the color defined for the connections
	 * 
	 * @return the color of the connections
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized ElementColor getConnectionsColor() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return connectionsColor;
	}
	
	/**
	 * 
	 * Get the defined location of the camera
	 * 
	 * 
	 * @return a 3D coordinate refers to the camera location
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized Coordinate3D getCameraLocation() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return cameraLocation;
	}
	
	/**
	 * Get the view destination point of the camera
	 * 
	 * 
	 * @return a 3D coordinate refers to the camera destination
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized Coordinate3D getCameraDestination() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return cameraDestination;
	}
	
	/**
	 * Get scene rotation of the camera in degrees
	 * 
	 * @return Scene rotation in degrees
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized double getCameraSceneRotation() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return cameraSceneRotation;
	}
	
	
	/**
	 * Get a hash-map, in which the keys are the joint-IDs (1-n) and the values are hash-sets, containing
	 * the joint-IDs of the joints connected to the key joint
	 * 
	 * @return Map from each joint to its connected joints
	 * @throws SKVPNonInitializedReaderException
	 */
	public synchronized HashMap<Integer, HashSet<Integer>> getJointConnections() throws SKVPNonInitializedReaderException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		return jointConnections;
	}
	
	/**
	 * Get a hash-set containing the joint-IDs of the joints connected to the supplied joint-ID
	 * 
	 * @param jointId Id of the joint to get its connections (ids are between <b>1</b> and <b>n</b> where
	 * <b>n</b> is number of joints in the file)
	 * @return A set containing the IDs of the connected joints
	 * @throws SKVPNonInitializedReaderException
	 * @throws SKVPIllegalValueException
	 */
	public synchronized HashSet<Integer> getJointConnections(int jointId) throws SKVPNonInitializedReaderException, SKVPIllegalValueException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		if (! jointConnections.containsKey(jointId)) {
			throw new SKVPIllegalValueException("Supplied joint-id does not exist");
		}
		return jointConnections.get(jointId);
	}
	
	/**
	 * Get the next frame in the currently read file
	 * 
	 * @return Returns the next frame in the currently read SKVP file. Returns <b>null</b> when reached EOF
	 * @throws IOException
	 * @throws SKVPNonInitializedReaderException
	 * @throws SKVPSyntaxErrorException
	 */
	public synchronized SkeletonVideoFrame getNextFrame() throws IOException, SKVPNonInitializedReaderException, SKVPSyntaxErrorException {
		if (! headerReadSuccessfully) {
			throw new SKVPNonInitializedReaderException();
		}
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.equals("")) {
				continue;
			}
			if (line.startsWith(Defs.CAMERA_LOCATION_ENTRY_NAME)) {
				String value = Utils.getValueFromCameraLine(line, Defs.CAMERA_LOCATION_ENTRY_NAME);
				cameraLocationTemporal = Utils.stringToCoordinate3D(value);
				continue;
			} else if (line.startsWith(Defs.CAMERA_DESTINATION_ENTRY_NAME)) {
				String value = Utils.getValueFromCameraLine(line, Defs.CAMERA_DESTINATION_ENTRY_NAME);
				cameraDestinationTemporal = Utils.stringToCoordinate3D(value);
				continue;
			} else if (line.startsWith(Defs.CAMERA_SCENE_ROTATION_ENTRY_NAME)) {
				String value = Utils.getValueFromCameraLine(line, Defs.CAMERA_SCENE_ROTATION_ENTRY_NAME);
				cameraSceneRotationTemporal = Utils.parseRealDegree(value);
				continue;
			}
			break;
		}
		if (line == null) {
			return null;
		}
		return Utils.stringToSkeletonVideoFrame(line, this.numJoints, cameraLocationTemporal, cameraDestinationTemporal, cameraSceneRotationTemporal);
	}
	
	/**
	 * Get a list containing the next <b>num</b> frames in the currently read file
	 * 
	 * @param num frames to read
	 * @return Returns a list containing the next <b>num</b> frames in the currently read file.
	 * If there are less than <b>num</b> frames remaining to read, returning a shorter list.
	 * An empty list indicates EOF
	 * @throws IOException
	 * @throws SKVPNonInitializedReaderException
	 * @throws SKVPSyntaxErrorException
	 */
	public synchronized List<SkeletonVideoFrame> getNextFrames(int num) throws IOException, SKVPNonInitializedReaderException, SKVPSyntaxErrorException {
		List<SkeletonVideoFrame> frames = new LinkedList<SkeletonVideoFrame>();
		for (int i = 0 ; i < num ; i++) {
			SkeletonVideoFrame frame = getNextFrame();
			if (frame == null) {
				break;
			}
			frames.add(frame);
		}		
		return frames;
	}
	
	/**
	 * Get a list containing all the remaining frames in the currently read file
	 * 
	 * @return Returns a list containing all the remaining frames in the currently read file.
	 * An empty list indicates EOF
	 * @throws IOException
	 * @throws SKVPNonInitializedReaderException
	 * @throws SKVPSyntaxErrorException
	 */
	public synchronized List<SkeletonVideoFrame> getAllFrames() throws IOException, SKVPNonInitializedReaderException, SKVPSyntaxErrorException {
		List<SkeletonVideoFrame> frames = new LinkedList<SkeletonVideoFrame>();
		while (true) {
			SkeletonVideoFrame frame = getNextFrame();
			if (frame == null) {
				break;
			}
			frames.add(frame);
		}		
		return frames;
	}
	
}
