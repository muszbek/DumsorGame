package dumsorBase;

public class Happiness {
	
	private double buildup;
	private static final double stepsBreakEven = ConsumptionHabit.delayDecrCap;
	//amount of steps to stop increasing consumption
	private static final double stepsCapRate = 1.5;
	private static final double stepsCap = stepsBreakEven * stepsCapRate;
	//max steps of aggregated hate --> consumption increment decreases
	private static final double buildupCap = 500;	//tune it
	private static final double recoverRate = 0.5;	//ratio of hate buildup/recovery
	private static final double mercyMinNormCons = 0.5;	//if no consumption, a minimal recovery
	//rate is needed in order not to get stuck in 0
	private static final int baseMin = 0;	//minimum base to be considered, 
	//in order to decrease the effect of hate on small consumers
	private int percentage;	//for displaying happiness

	public Happiness() {
		buildup = 0;
		percentage = 100;
	}
	
	public void getRage(double base, double demand) {	//hourly called
		if (base < baseMin) {
			base = baseMin;
		}
		//hourly consumption variations affect hate buildup, but overall development does not-->
		double normDemand = demand / base;
		buildup += normDemand;
	}
	
	public void getHappy(double base, double consumption) {	//hourly called
		//hourly consumption variations affect hate buildup, but overall development does not-->
		double normCons = 0;
		if (base > 0) {
//			if (consumption < 1) {	
//				consumption = 1;
//			}
			normCons = consumption / base;
		}
		if (normCons <= 0) {	//do not get stuck at permanent 0 consumption
			normCons = 0.4;
		}
		normCons *= recoverRate;
		buildup -= normCons;
	}
	
	public double getDelay() {
		if (buildup < 0) {
			buildup = 0;
		}
		else if (buildup > buildupCap) {
			buildup = buildupCap;
		}
		setPercentage();
		
		double delay = (buildup / buildupCap) * stepsCap;
		return delay;
	}
	
	public double getBuildup() {
		return buildup;
	}
	
	private void setPercentage() {
		double hate = (buildup / buildupCap) * 100;
		percentage = 100 - (int)hate;
	}
	
	public int getPercentage() {
		return percentage;
	}
}
//keep tuning