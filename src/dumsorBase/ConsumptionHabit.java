package dumsorBase;

import java.util.Random;

public class ConsumptionHabit {

	private static final double baseConsCap = 0.5;	//each household caps at this consumption
	private static final double linDivSteps = 20;
	private static final double dailyConsIncr = baseConsCap / linDivSteps;
	//each day consumption increases by this amount
	public static final int delayDecrCap = 20;	//the delay decreases proportionally to the 
	//amount of electrified houses, but max this number
	//public because Happiness uses this
	private static final double dailyConsOffset = 1.7;	//sinusoidal consumption is 
	//shifted upwards by this number, per unit
	private static final double devianceSigma = 0.1;	//per unit standard deviance
	private double consIntroDelay = 0;	//this will increase upon electrifying and 
	//decrease over time, restricts consumption
	private static final double consIntroDelayCapRate = 1.1;
	private double totalConsCap = 0;	//increases upon electrifying
	private double baseCons = 0;	//the base consumption
	private double cons = 0;	//actual consumption
	private Happiness happiness;
	
	
	public ConsumptionHabit() {
		happiness = new Happiness();
	}
	
	public void electrifySingleHouse() {
		totalConsCap += baseConsCap;
		consIntroDelay += baseConsCap;
	}
	
	private void updateBaseCons(int electrifiedHouses) {
		double decrRate = electrifiedHouses;
		if (decrRate > delayDecrCap) {
			decrRate = delayDecrCap;
		}
		//here decrRate will be decreased by the hate
		double delay = happiness.getDelay();
		delay *= (decrRate / delayDecrCap);
		decrRate -= delay;
		
		//System.out.println("Delayed by rage: " + delay);
		
		double introDelayDecr = dailyConsIncr * decrRate;
		
		if (introDelayDecr < consIntroDelay) {
			consIntroDelay -= introDelayDecr;
			double consIntroDelayCap = consIntroDelayCapRate * electrifiedHouses * baseConsCap;
			if (consIntroDelay > consIntroDelayCap) {	//need to have a max so it does not
				consIntroDelay = consIntroDelayCap;		//aggregate irreversibly high
			}
		}
		else {
			consIntroDelay = 0;
		}
		
		baseCons = totalConsCap - consIntroDelay;
		
		if (baseCons < 0)	//for safety, no negative base
			baseCons = 0;
	}
	
	private double getHourlyCons() {	//sinusoidal with offset and peak at 3pm
		//random gaussian element
		int hour = ElectricGrid.getTime().getHour();
		double hourNormal = ((double)hour) / 24;
		hourNormal += 0.625;
		hourNormal *= 2 * Math.PI;
		double hourlyCons = Math.cos(hourNormal);
		hourlyCons += dailyConsOffset;
		
		double deviance = new Random().nextGaussian() * devianceSigma;
		deviance += 1;
		hourlyCons *= deviance;
		
		if (hourlyCons < 0)	{	//no negative consumptions
			hourlyCons = 0;
		}
			
		cons = (baseCons * hourlyCons);
		return cons;
	}
	
	public double getCons(int houses) {
		if (ElectricGrid.getTime().getHour() == 0) {	//only daily update
			updateBaseCons(houses);
		}
		return getHourlyCons();
	}
	
	public void getHappy() {
		happiness.getHappy(baseCons, cons);
	}
	
	public void getRage() {
		happiness.getRage(baseCons, cons);
	}
	
	public double getConsIntroDelay() {
		return consIntroDelay;
	}
	
	public Happiness getHappines() {
		return happiness;
	}
}
