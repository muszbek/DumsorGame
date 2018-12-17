package dumsorBase;

import java.util.Random;

public abstract class Weather {
	
	private static final int seasonLength = 100;
	private static final double seasonOffset = 0.5;	//offset of seasonal cycle period
	private static final double cloudPenetration = 0.5;	//sunshine multiplier at maximum cloud
	private static final double cloudAmplitude = (1 - cloudPenetration) / 2;
	private static final double cloudOffset = cloudPenetration + cloudAmplitude;
	private static final double rainScale = 1;	//multiplies cloud amplitude at rain effect
	private static final double cloudDevAmpl = 0.15;	//amplitude of standard deviance of the
	//gaussian random element of cloud, sinusoidally dependent of season
	
	private static double cloudTransparency = 1;
	private static double rainAmount = 1;
	
	private static boolean rainyPopupped = false;
	private static boolean dryPopupped = false;
	

	public Weather() {
		// TODO Auto-generated constructor stub
	}
	
	public static double sunShine(int hour, int day) {	//returns between 1 and 0
		double hourNormal = ((double)hour) / 24;
		hourNormal += 0.5;
		hourNormal *= 2 * Math.PI;
		double intensity = Math.cos(hourNormal);
		intensity *= cloudTransparency;
		//random element
		double deviance = (1 - cloudTransparency) * cloudDevAmpl / cloudPenetration;
		deviance *= new Random().nextGaussian();
		deviance += 1;
		intensity *= deviance;
		
		if (intensity < 0) {
			intensity = 0;
		}
		return intensity;
	}
	
	private static double getCloud(int day) {	//returns between 1 and cloudPenetration
		double dayNormal = ((double)day) / seasonLength;
		dayNormal += seasonOffset;
		dayNormal *= 2 * Math.PI;
		double transparency = Math.sin(dayNormal);
		transparency *= cloudAmplitude;
		transparency += cloudOffset;
		return transparency;
	}
	
	private static double rain() {	//returns around 1, with amplitude of deviance
		double deviance = cloudTransparency - cloudOffset;
		deviance *= rainScale;
		double amount = 1 - deviance;
		return amount;
	}
	
	public static void updateDaily(int day) {
		cloudTransparency = getCloud(day);
		rainAmount = rain();
		seasonPopups();
	}
	
	public static double getRain() {
		return rainAmount;
	}
	
	private static void seasonPopups() {
		if (rainAmount > 1 && rainyPopupped == false) {
			Popups.rainyPopup(60);
			rainyPopupped = true;
			dryPopupped = false;
		}
		else if (rainAmount < 1 && dryPopupped == false) {
			Popups.dryPopup(60);
			rainyPopupped = false;
			dryPopupped = true;
		}
	}

}
