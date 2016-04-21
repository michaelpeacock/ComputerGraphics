package voltron.objects;

public class LionObject {
	private int listID;
	private double xRotation;
	private double yRotation;
	private double zRotation;
	private float length;
	private float height;

	public int getListID() {
		return listID;
	}

	public void setListID(int listID) {
		this.listID = listID;
	}

	public double getxRotation() {
		return xRotation;
	}

	public void setxRotation(double xRotation) {
		this.xRotation = xRotation;
	}

	public double getyRotation() {
		return yRotation;
	}

	public void setyRotation(double yRotation) {
		this.yRotation = yRotation;
	}

	public double getzRotation() {
		return zRotation;
	}

	public void setzRotation(float zRotation) {
		this.zRotation = zRotation;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
