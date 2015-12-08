package Utils;

import java.io.Serializable;

public class Smokation implements Serializable {

	private static final long serialVersionUID = 1L;
	private double Latitude;
	private double Longitude;

	public Smokation(){
		
	}
	public Smokation(double lat, double longi) {
		this.Latitude = lat;
		this.Longitude = longi;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	@Override
	public String toString() {
		return "Smokation [Latitude=" + Latitude + ", Longitude=" + Longitude + "]";
	}

}
