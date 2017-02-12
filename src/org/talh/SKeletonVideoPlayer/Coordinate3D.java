package org.talh.SKeletonVideoPlayer;

public class Coordinate3D {
	double x;
	double y;
	double z;
	
	public Coordinate3D() {
		
	}
	
	public Coordinate3D(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
}
