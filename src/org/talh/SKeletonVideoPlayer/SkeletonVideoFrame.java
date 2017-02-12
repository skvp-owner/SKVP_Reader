package org.talh.SKeletonVideoPlayer;

public class SkeletonVideoFrame {
	private Coordinate3D[] jointCoordinates;
	
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
	
}
