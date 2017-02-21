package org.talh.SKeletonVideoPlayer;

public class SkeletonVideoFrame {
	private Coordinate3D[] jointCoordinates;
	private Coordinate3D cameraLocation;
	private Coordinate3D cameraDestination;
	private double cameraSceneRotation;
	
	public SkeletonVideoFrame(int numJoints) {
		this.jointCoordinates = new Coordinate3D[numJoints];
	}
	
	public void setCoordinate(int num, Coordinate3D coordinate) {
		jointCoordinates[num - 1] = coordinate;
	}
	
	public Coordinate3D getCoordinate(int num) {
		return jointCoordinates[num - 1];
	}
	
	public Coordinate3D[] getAllCoordinates() {
		return jointCoordinates;
	}

	public Coordinate3D getCameraLocation() {
		return cameraLocation;
	}

	public void setCameraLocation(Coordinate3D cameraLocation) {
		this.cameraLocation = cameraLocation;
	}

	public Coordinate3D getCameraDestination() {
		return cameraDestination;
	}

	public void setCameraDestination(Coordinate3D cameraDestination) {
		this.cameraDestination = cameraDestination;
	}

	public double getCameraSceneRotation() {
		return cameraSceneRotation;
	}

	public void setCameraSceneRotation(double cameraSceneRotation) {
		this.cameraSceneRotation = cameraSceneRotation;
	}
	
}
