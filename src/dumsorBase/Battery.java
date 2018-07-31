package dumsorBase;

public class Battery {	//can only charge locally generated energy
	//storing grid energy is a huge clusterfuck
	
	private double capacity = 0;
	private double charge = 0;
	private double chargeRate = 0.2;	//this part of the capacity charges per hour

	public Battery() {
		
	}
	
	public void addBattery(int boughtAmount) {
		capacity += boughtAmount;
	}
	
	public void removeBattery() {
		if (capacity > 0) {
			capacity--;
		}
		else {
			capacity = 0;
		}
	}
	
	public double getCapacity() {
		return capacity;
	}
	
	public double getCharge() {
		return charge;
	}
	
	public double charge(double powerIn) {
		double room = capacity - charge;
		if (powerIn > room) {
			powerIn = room;
		}
		int rate = (int)Math.ceil((capacity * chargeRate));
		if (powerIn > rate) {
			powerIn = rate;
		}
		
		charge += powerIn;
		return powerIn;
	}
	
	public double discharge(double powerOut) {
		if (powerOut > charge) {
			powerOut = charge;
		}
		
		charge -= powerOut;
		return powerOut;
	}

}
//works well, but needs some display