package dumsorBase;

import javax.swing.*;
import org.apache.commons.lang.WordUtils;
import dumsorPanels.*;

public abstract class Popups {

	private static ImageIcon lineIcon = new ImageIcon(ResLoader.loadImg("pics/line_icon.gif"));
	private static ImageIcon noMoneyIcon = new ImageIcon(ResLoader.loadImg("pics/nomoney.jpg"));
	private static ImageIcon lineDmgIcon = new ImageIcon(ResLoader.loadImg("pics/line_dmg.jpg"));
	private static ImageIcon PVDmgIcon = new ImageIcon(ResLoader.loadImg("pics/solar_dmg.jpg"));
	private static ImageIcon battDmgIcon = new ImageIcon(ResLoader.loadImg("pics/batt_dmg.jpg"));
	private static ImageIcon engIcon1 = new ImageIcon(ResLoader.loadImg("pics/engineer1.jpg"));
	private static ImageIcon engIcon2 = new ImageIcon(ResLoader.loadImg("pics/engineer2.jpg"));
	private static ImageIcon engIcon3 = new ImageIcon(ResLoader.loadImg("pics/engineer3.jpg"));
	private static ImageIcon engIcon4 = new ImageIcon(ResLoader.loadImg("pics/engineer4.jpg"));
	private static ImageIcon rainyIcon = new ImageIcon(ResLoader.loadImg("pics/rainy.jpg"));
	private static ImageIcon dryIcon = new ImageIcon(ResLoader.loadImg("pics/dry.jpg"));
	private static ImageIcon spendIcon = new ImageIcon(ResLoader.loadImg("pics/spend_icon.jpg"));
	private static ImageIcon maintIcon = new ImageIcon(ResLoader.loadImg("pics/maintenance.jpg"));
	private static ImageIcon lineConstIcon = new ImageIcon(ResLoader.loadImg(
			"pics/line_const.jpg"));
	private static ImageIcon lineFinishIcon = new ImageIcon(ResLoader.loadImg(
			"pics/line_finish.jpg"));
	private static boolean lineFinishPopupped = false;
	private static ImageIcon timeIcon = new ImageIcon(ResLoader.loadImg("pics/time.jpg"));
	private static ImageIcon solarIcon = new ImageIcon(ResLoader.loadImg("pics/solar.jpg"));
	private static ImageIcon batteryIcon = new ImageIcon(ResLoader.loadImg("pics/battery.jpg"));
	private static boolean solarPopupped = false;	//handled here because popup is called
	private static boolean batteryPopupped = false; //in many instances
	private static ImageIcon districtElectIcon = new ImageIcon(ResLoader.loadImg(
			"pics/district_elect2.jpg"));
	private static boolean districtElectPopupped = false;
	private static ImageIcon houseElectIcon = new ImageIcon(ResLoader.loadImg(
			"pics/house_elect.jpg"));
	private static boolean houseElectPopupped = false;
	private static ImageIcon shedWarnIcon = new ImageIcon(ResLoader.loadImg("pics/shed_warn.jpg"));
	private static ImageIcon collapseIcon = new ImageIcon(ResLoader.loadImg("pics/collapse.jpg"));
	private static ImageIcon discIcon = new ImageIcon(ResLoader.loadImg("pics/disconnect.jpg"));
	private static ImageIcon recIcon = new ImageIcon(ResLoader.loadImg("pics/reconnect.jpg"));
	private static ImageIcon dumsorRageIcon = new ImageIcon(ResLoader.loadImg(
			"pics/dumsor_rage.jpg"));
	private static boolean dumsorRagePopupped = false;
	private static ImageIcon fullElectIcon = new ImageIcon(ResLoader.loadImg(
			"pics/fullelect.jpg"));
	private static ImageIcon fullConsIcon = new ImageIcon(ResLoader.loadImg(
			"pics/fullconsume.jpg"));
	private static ImageIcon allElectIcon = new ImageIcon(ResLoader.loadImg(
			"pics/allelect.jpg"));
	private static ImageIcon winIcon = new ImageIcon(ResLoader.loadImg("pics/win.jpg"));
	
	//for maintDamagePopup text
	private static final String intro = "Due to lack of maintenance, we lost a portion" +
			" of our equipment!";
	private static final String lineIntro = "Power lines between the following nodes" +
			" have been damaged:";
	private static final String PVIntro = "Some portion of the PV panels installed in the " +
			"following nodes have been damaged:";
	private static final String battIntro = "Some portion of the batteries installed in the " +
			"following nodes have been damaged:";
	private static String lineSentence;
	private static String PVSentence;
	private static String battSentence;
	
	private static boolean popupsEnabled = false;
			
	public Popups() {
		
	}
	
	public static boolean getPopupsEnabled() {
		return popupsEnabled;
	}
	
	public static int buildLinePopup(Node nodeFrom, Node nodeTo) {
		int linePrice = MoneyPanel.priceLine(nodeFrom, nodeTo);
		String message = "Do you want to construct a power line between \n" + 
				nodeFrom.getName() + " and " + nodeTo.getName() +
				" for " + linePrice + " GHS?";
		TimePanel.autoPause();
		int answer = JOptionPane.showConfirmDialog(DumsorRoot.getMapPane(),
				message, "Confirm line construction", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, lineIcon);
		TimePanel.autoResume();
		return answer;
	}
	
	public static int skipMaintPopup(int wrapLength) {
		String message = "Sir, are you sure you want to stop paying for maintenance? " +
				"Remember, our equipment might start failing!";
		message = WordUtils.wrap(message, wrapLength);
		TimePanel.autoPause();
		int answer = JOptionPane.showConfirmDialog(DumsorRoot.getMapPane(),
				message, "Confirm canceling maintenance", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, maintIcon);
		TimePanel.autoResume();
		return answer;
	}
	
	public static void fullElectPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text1 = "Sir, we have managed to connect all residents to the power " +
					"grid in one district! In time, they will exploit new possibilities " +
					"given by electricity - buy electrical appliances and live a western" +
					" lifestyle.";
			text1 = WordUtils.wrap(text1, wrapLength);
			String text2 = "We need to provide them uninterrupted power if possible," +
					" so our customers can get comfortable and buy more power.";
			text2 = WordUtils.wrap(text2, wrapLength);
			String text = text1 + "\n\n" + text2;
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"A district is fully powered!", JOptionPane.WARNING_MESSAGE, 
					fullElectIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void fullConsPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text = "Sir, the consumption habits of the residents in one district " +
					"have matured to their peak. They live comfortably with sufficient " +
					"supply of power. We should do well to keep it that way.";
			text = WordUtils.wrap(text, wrapLength);
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"A district is consuming maximum!", JOptionPane.WARNING_MESSAGE, 
					fullConsIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void allElectPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text1 = "Sir, we have managed to connect all residents to the power " +
					"grid in whole Sunyani-area! We are getting close to reaching our " +
					"objective of providing sufficient power for every resident.";
			text1 = WordUtils.wrap(text1, wrapLength);
			String text2 = "We still exept an increase in power demand, as the recently " +
					"connected households get equipped with appliances. We further " +
					"need to sustain our supply of power, and eliminate Dumsor completely.";
			text2 = WordUtils.wrap(text2, wrapLength);
			String text = text1 + "\n\n" + text2;
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"All districts are fully powered!", JOptionPane.WARNING_MESSAGE, 
					allElectIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void winPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text1 = "Congratulations, Sir, we have reached our objective! Under your" +
					"leadership, we have managed to provide sufficient power for all " +
					"residents of Sunyani-area in a sustainable way. Your work has greatly " +
					"contributed to the development of the region.";
			text1 = WordUtils.wrap(text1, wrapLength);
			String text2 = "YOU HAVE WON THE GAME!";
			text2 = WordUtils.wrap(text2, wrapLength);
			String text = text1 + "\n\n" + text2;
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"Congratulations! You have won the game!", JOptionPane.WARNING_MESSAGE, 
					winIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void dumsorRagePopup(int wrapLength) {
		if (popupsEnabled == true) {
			if (dumsorRagePopupped == false) {
				dumsorRagePopupped = true;
				String text1 = "Sir, a district seems to be having too much of dumsor! The " +
						"residents have started to look for alternative sources of power - " +
						"fuel generators and electricity theft. They are losing trust in our " +
						"company, and we perceive it as a decrease of consumption demand.";
				text1 = WordUtils.wrap(text1, wrapLength);
				String text2 = "In short, we are missing out of revenues. Sir, if I might " +
						"suggest, we should provide electricity to them as soon as possible! " +
						"Their consumption willingness will slowly but surely recover if " +
						"they have access to uninterrupted power.";
				text2 = WordUtils.wrap(text2, wrapLength);
				String text = text1 + "\n\n" + text2;
				TimePanel.autoPause();
				JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
						"People are getting angry!", JOptionPane.WARNING_MESSAGE, 
						dumsorRageIcon);
				TimePanel.autoResume();
			}
		}
	}
	
	public static void disconnectPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text1 = "'Dumsor dumsor' means 'on-off on-off' in twi - the Ghanaian term " +
					"for power outages.";
			text1 = WordUtils.wrap(text1, wrapLength);
			String text2 = "Sir, you have disconnected a district from the subgrid it was " +
					"connected " +
					"to, and the residents now only have access to locally generated (solar) " +
					"power. If that is not sufficient, the residents will become more and " +
					"more unhappy about dumsor, until you reconnect them.";
			text2 = WordUtils.wrap(text2, wrapLength);
			String text = text1 + "\n\n" + text2;
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"Dumsor!", JOptionPane.WARNING_MESSAGE, discIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void reconnectPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text = "Sir, you have attempted to reconnect a district to a subgrid. If " +
					"said " +
					"district has no direct contact to another district with access to power " +
					"(green), then it cannot be reconnected, nothing happens. If it has such " +
					"contact, it gets reconnected and the residents gain access to " +
					"electricity.";
			text = WordUtils.wrap(text, wrapLength);
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"District reconnection", JOptionPane.WARNING_MESSAGE, recIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void shedWarnPopup(int wrapLength) {
		String text = "Sir, in one of our subgrids our customers are consuming more power " +
				"than we can produce. We have to disconnect at least one district, otherwise " +
				"the whole subgrid will shut down in order to protect our equipment and the " +
				"utilities of our customers.";
		text = WordUtils.wrap(text, wrapLength);
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
				"Load shedding required!", JOptionPane.WARNING_MESSAGE, shedWarnIcon);
		TimePanel.autoResume();
	}
	
	public static void shedCollapsePopup(int wrapLength) {
		String text = "Sir, one of the subgrids have been completely shut down due to " +
				"extensive overconsumption! Now all of the districts located in that subgrid " +
				"are disconnected, and we have to systematically reconnect them manually.";
		text = WordUtils.wrap(text, wrapLength);
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
				"Subgrid collapsed!", JOptionPane.WARNING_MESSAGE, collapseIcon);
		TimePanel.autoResume();
	}
	
	public static void noMoneyPopup() {
		String message = "Out of budget, \n we cannot afford to pay for that!";
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), message, "No money", 
				JOptionPane.WARNING_MESSAGE, noMoneyIcon);
		TimePanel.autoResume();
	}
	
	public static void spendMoneyPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text1 = "Sir, you have bought a piece of equipment, and spent money to pay" +
					" for it. " +
					"You can see that the price has been subtracted from your balance " +
					"(blue figure).";
			text1 = WordUtils.wrap(text1, wrapLength);
			String text2 = "At the same time, a small amount has been added to the aggregated" +
					" maintenance costs (orange figure). Our equipment has to be " +
					"maintained periodically in order " +
					"to keep them operational, and this costs money. Under your balance it is" +
					" indicated when the next maintenance session is scheduled, and the cost " +
					"will be automatically deducted from your balance.";
			text2 = WordUtils.wrap(text2, wrapLength);
			String text3 = "Turning on the 'Stop maint' button, you can choose not to pay " +
					"for maintencance, or this automatically happens if you do not have " +
					"enough money for it. Sir, I strongly urge you not to do so, because each" +
					" piece of our equipment has a chance of failure, if we skip maintenance.";
			text3 = WordUtils.wrap(text3, wrapLength);
			String text = text1 + "\n\n" + text2 + "\n\n" + text3;
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"You have spent money", JOptionPane.WARNING_MESSAGE, spendIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void lineConstPopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text1 = "You can now start the construction of power lines. Select a " +
					"district, and then click on another one. A popup will ask you " +
					"to confirm your decision, then construction will begin between the " +
					"two selected districts. You will see a yellow line growing between the " +
					"starting and end-points.";
			text1 = WordUtils.wrap(text1, wrapLength);
			String text2 = "Remember, Sir, that " +
					"building power lines is a costly, time-consuming and irreversible " +
					"process. Once the comission is dispached, your decision is permanent. " +
					"You pay in andvance for the process, and construction begins immediately.";
			text2 = WordUtils.wrap(text2, wrapLength);
			String text = text1 + "\n\n" + text2;
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"Constructing power lines", JOptionPane.WARNING_MESSAGE, lineConstIcon);
			TimePanel.autoResume();
		}
	}
	
	public static void lineFinishPopup(int wrapLength) {
		if (popupsEnabled == true) {
			if (lineFinishPopupped == false) {
				lineFinishPopupped = true;
				String text1 = "Sir, we have finished constructing a power line, a connection" +
						" between two districts is now established!";
				text1 = WordUtils.wrap(text1, wrapLength);
				String text2 = "If one of the connected districts originally had access to " +
						"electricity, now both have. It is indicated by green color that the" +
						" line " +
						"is transmitting power. If neither of the districts were electrified," +
						" the " +
						"new connection changed nothing at this point. The line is colored" +
						" red in " +
						"this case. If a connected district already had inactive (red) " +
						"connections, the new one will also be inactive (red) for security " +
						"reasons. You can turn them on in the 'Load shedding' game section.";
				text2 = WordUtils.wrap(text2, wrapLength);
				String text = text1 + "\n\n" + text2;
				TimePanel.autoPause();
				JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
						"Finished power line construction", JOptionPane.WARNING_MESSAGE, 
						lineFinishIcon);
				TimePanel.autoResume();
			}
		}
	}
	
	public static void solarPopup(int wrapLength) {
		if (popupsEnabled == true) {
			if (solarPopupped == false) {
				solarPopupped = true;
				
				String text1 = "Sir, we are installing solar power generators in a district!" +
						" The equipment will arrive and start to operate tomorrow. The " +
						"district will be able to generate power for itself from sunlight." +
						" The output peaks " +
						"around midday, and it is zero during nighttime.";
				text1 = WordUtils.wrap(text1, wrapLength);
				String text2 = "Be aware, Sir, that the generated power may be insufficient" +
						" to supply for a whole district. We need a lot of solar capacity to" +
						" meet the " +
						"consumption demands of our customers. They would also like to have " +
						"electricity after sunset, so you might consider investing in " +
						"batteries as " +
						"well. If we do manage to provide for the district, it can " +
						"generate for other districts that are connected by power lines.";
				text2 = WordUtils.wrap(text2, wrapLength);
				String text = text1 + "\n\n" + text2;
				TimePanel.autoPause();
				JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
						"PV capacity installed", JOptionPane.WARNING_MESSAGE, solarIcon);
				TimePanel.autoResume();
			}
		}
	}
	
	public static void batteryPopup(int wrapLength) {
		if (popupsEnabled == true) {
			if (batteryPopupped == false) {
				batteryPopupped = true;
				
				String text = "Sir, we are installing batteries in a district! The equipment" +
						" will arrive and start to operate tomorrow. If the district " +
						"generates more power than it consumes at a point, the excess power" +
						" will " +
						"be stored up until the capacity of the battery. If consumption is" +
						" higher " +
						"than production, the battery will supply power until it is depleted." +
						" This " +
						"is especially useful at night, when photovoltaic panels are not" +
						" generating electricity.";
				text = WordUtils.wrap(text, wrapLength);
				TimePanel.autoPause();
				JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
						"Battery installed", JOptionPane.WARNING_MESSAGE, batteryIcon);
				TimePanel.autoResume();
			}
		}
	}
	
	public static void districtElectPopup(int wrapLength) {
		if (popupsEnabled == true) {
			if (districtElectPopupped == false) {
				districtElectPopupped = true;
				
				String text1 = "Sir, a district now has access to electricity! You can start" +
						" connecting local households to the grid to actually start power " +
						"consumption.";
				text1 = WordUtils.wrap(text1, wrapLength);
				String text2 = "If the district is receiving sufficient power, its name is " +
						"colored green on the map. If it is colored red, that means it is " +
						"isolated from power sources, and it does not generate sufficient " +
						"power for itself. You can try to reconnect it in the 'Load shedding'" +
						" game section, or increase local power generation. Be aware, Sir," +
						" that when you try to reconnect, you are turning on the power " +
						"lines branching into the district. You can only turn on lines that " +
						"are connecting to a powered (green) district.";
				text2 = WordUtils.wrap(text2, wrapLength);
				String text = text1 + "\n\n" + text2;
				TimePanel.autoPause();
				JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
						"A district has access to electricity!", 
						JOptionPane.WARNING_MESSAGE, districtElectIcon);
				TimePanel.autoResume();
			}
		}
	}
	
	public static void houseElectPopup(int wrapLength) {
		if (popupsEnabled == true) {
			if (houseElectPopupped == false) {
				houseElectPopupped = true;
				
				String text1 = "Sir, we have connected households to the power grid, giving " +
						"the residents access to electricity. From now on, they will pay us " +
						"money for their power consumption. This is our primary source of " +
						"revenues. It will take some time until " +
						"they catch up with the wonders of technology, and their consumption " +
						"demands will increase gradually. People " +
						"normally consume more power during the day, but they also like to " +
						"use lighting, ventillation, and sometimes other appliances during " +
						"the night.";
				text1 = WordUtils.wrap(text1, wrapLength);
				String text2 = "Remember, Sir, that we should provide uninterrupted power " +
						"to our customers. Power outages will slow down their " +
						"progression of power consumption, and in extreme cases even reverse" +
						" it. You can observe consumer satisfaction in the 'Load shedding'" +
						" game section.";
				text2 = WordUtils.wrap(text2, wrapLength);
				String text = text1 + "\n\n" + text2;
				TimePanel.autoPause();
				JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
						"Households gained access to power!", 
						JOptionPane.WARNING_MESSAGE, houseElectIcon);
				TimePanel.autoResume();
			}
		}
		
	}
	
	public static void timePopup(int wrapLength) {
		if (popupsEnabled == true) {
			String text = "We can measure our grid performance with one hour resolution. On " +
					"the globe rotating on the middle left, you can see how fast time is" +
					" running." +
					" At any time, you can decide to pause to take decisions, or to speed up," +
					" if things are happening too slowly. Sir, I urge you to switch to the " +
					"fastest time with caution. A lot can change in a couple of days.";
			text = WordUtils.wrap(text, wrapLength);
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"Controlling time", JOptionPane.WARNING_MESSAGE, timeIcon);
			TimePanel.autoResume();
		}
		
	}
	
	public static void creditPopup(int wrapLength) {
		String text1 = "This game application was written by Tamas Muszbek.";
		text1 = WordUtils.wrap(text1, wrapLength);
		String text2 = "This is a game." +
				" It does not describe accurately the reality of operating a power grid." +
				" I tried to be somewhat realistic, but I had to simplify and exaggerate" +
				" in order to make the application enjoyable.";
		text2 = WordUtils.wrap(text2, wrapLength);
		String text3 = "The images are from google, " +
				"I do not own them. The names occuring are partially real, partially" +
				" fictious. If you are familiar with the locations I took inspiration of, " +
				"please forgive me the inaccuracies. I hope I do not offend anyone. Enjoy!";
		text3 = WordUtils.wrap(text3, wrapLength);
		String text = text1 + "\n\n" + text2 + "\n\n" + text3;
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
				"Credits", JOptionPane.PLAIN_MESSAGE);
		TimePanel.autoResume();
	}
	
	public static void startPopup(int wrapLength) {
		String text1 = "Welcome, Sir! The Bui Hydroelectric Power Station has been finished," +
				" and so the Electricity Company of Ghana is expanding to " +
				"Brong-Ahafo region. You have been assigned as the regional Head-Manager to " +
				"oversee the electrification of Sunyani-area.";
		text1 = WordUtils.wrap(text1, wrapLength);
		String text2 = "I am Kofi Boatwe, your technical advisor. I can give you counsel" +
				" on power grid construction and operation throughout your work here.";
		text2 = WordUtils.wrap(text2, wrapLength);
		String text3 = "I understand that the company has given you a starting budget, and" +
				" the liberty to manage the revenues collected from this area as you see fit." +
				" It is expected of you that a robust power network is constructed, " +
				"supplying the population of Sunyani and its suburbs with sufficient power.";
		text3 = WordUtils.wrap(text3, wrapLength);
		String text4 = "Sir, do you require my counsel in the future?";
		text4 = WordUtils.wrap(text4, wrapLength);
		String text = text1 + "\n\n" + text2 + "\n\n" + text3 + "\n\n" + text4;
		//TimePanel.autoPause();
		int answer = JOptionPane.showConfirmDialog(DumsorRoot.getMapPane(), text, 
				"Welcome, Sir!", JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, engIcon3);
		//TimePanel.autoResume();
		if (answer == JOptionPane.YES_OPTION) {	//this question will determine whether
			popupsEnabled = true;				//new popups are allowed or not
		}
		
		if (getPopupsEnabled() == true) {	//open next popup here, once OK-d
			overViewPopupStart(70);
		}
	}
	
	public static void overViewPopupStart(int wrapLength) {
		String text1 = "You are now in the 'Overview' game section. You can change sections " +
				"by clicking one of the upper left buttons. In the middle left, you can see " +
				"a globe indicating the flow of time, and four buttons to control it. " +
				"Below the time control your financial indicators are located.";
		text1 = WordUtils.wrap(text1, wrapLength);
		String text2 = "On the right side, you can see the map of the city Sunyani and " +
				"its surroundings. The area is symbolically divided into districts, each " +
				"with its own population. Right now none of them have access to electricity. " +
				"The Substation, indicated with a different icon, is the point where the " +
				"hydropower plant supplies the town with energy. It acts as a source of " +
				"power, you can connect it to other districts in the 'Construction' game " +
				"section.";
		text2 = WordUtils.wrap(text2, wrapLength);
		String text3 = "If you select one of the districts, you can see the local power " +
				"production, consumption and net balance in the upper left box. " +
				"If none of the districts are selected, you can see the same data aggregated " +
				"for all existing subgrids.";
		text3 = WordUtils.wrap(text3, wrapLength);
		String text = text1 + "\n\n" + text2 + "\n\n" + text3;
		//TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
				"Overview section", JOptionPane.QUESTION_MESSAGE, engIcon1);
		//TimePanel.autoResume();
	}
	
	public static void overViewPopup(int wrapLength) {
		TimePanel.autoPause();
		overViewPopupStart(wrapLength);
		TimePanel.autoResume();
	}
	
	public static void constPopup(int wrapLength) {
		String text1 = "You are now in the 'Construction' game section. You can construct " +
				"new equipment into your network here.";
		text1 = WordUtils.wrap(text1, wrapLength);
		String text2 = "If you turn on the 'Construct line' button, you can build power " +
				"lines connecting any two districts. If one of them has access to " +
				"electricity, it will supply the other as well.";
		text2 = WordUtils.wrap(text2, wrapLength);
		String text3 = "If you select a district, you can connect its households to the " +
				"power grid individually. This can only be done if the district itself " +
				"has access to electricity. You can also install photovoltaic (PV)- or solar " +
				"power panels, to produce electricity locally from sunlight. Furthermore, " +
				"you can buy high capacity batteries to store excess solar energy during the " +
				"day and supply at nighttime.";
		text3 = WordUtils.wrap(text3, wrapLength);
		String text = text1 + "\n\n" + text2 + "\n\n" + text3;
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
				"Construction section", JOptionPane.QUESTION_MESSAGE, engIcon4);
		TimePanel.autoResume();
	}
	
	public static void loadSheddingPopup(int wrapLength) {
		String text1 = "You are now in the 'Load Shedding' game section. Here you can " +
				"isolate districts from the grid or reconnect them. You can also observe " +
				"how satisfied are the consumers of each district (if there are any) with " +
				"the supply of electricity.";
		text1 = WordUtils.wrap(text1, wrapLength);
		String text2 = "If you are doing a perfect job and every consumer has uninterrupted " +
				"access to power, you won't use this section much. However if power demand " +
				"exceeds our supply at one point, you will be forced to shut off some " +
				"districts temporarily in order to keep the integrity of the whole network. " +
				"You can do this by selecting a district and turning on the 'Disconnect' " +
				"button. You can also reconnect the district by turning off the same button.";
		text2 = WordUtils.wrap(text2, wrapLength);
		String text3 = "Remember, Sir, that our customers do not like to be shut off.";
		text3 = WordUtils.wrap(text3, wrapLength);
		String text = text1 + "\n\n" + text2 + "\n\n" + text3;
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
				"Load Shedding section", JOptionPane.QUESTION_MESSAGE, engIcon2);
		TimePanel.autoResume();
	}
	
	public static void rainyPopup(int wrapLength) {
		String message = "Sir, we have entered the wet season! The output of the hydropower " +
				"plant will probably to increase, so the incoming power line at the" +
				" substation is expected to yield more power. At the same time, solar" +
				" panels are going to be less productive due to clouds blocking " +
				"the sunlight.";
		message = WordUtils.wrap(message, wrapLength);
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), message, 
				"The rainy season has begun!", JOptionPane.WARNING_MESSAGE, rainyIcon);
		TimePanel.autoResume();
	}
	
	public static void dryPopup(int wrapLength) {
		String message = "Sir, we have entered the dry season! Solar panels are expected to " +
				"be more productive due to the increased amount sunlight. Unfortunately " +
				"the draughts affect the output of the hydropower plant negatively, so the " +
				"substation is going to transmit less power.";
		message = WordUtils.wrap(message, wrapLength);
		TimePanel.autoPause();
		JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), message, 
				"The dry season has begun!", JOptionPane.WARNING_MESSAGE, dryIcon);
		TimePanel.autoResume();
	}
	
	public static void maintDamagePopup() {
		String text = produceText(50);	//here I can determine the width of the text
		if (text.equals("") == false) {	//popup only appears if there is destroyed equipment
			ImageIcon icon = null;	//the displayed images have a hierarchy of topic
			if (PVSentence.equals("") == false) {
				icon = PVDmgIcon;
			}
			if (battSentence.equals("") == false) {
				icon = battDmgIcon;
			}
			if (lineSentence.equals("") == false) {
				icon = lineDmgIcon;
			}
			TimePanel.autoPause();
			JOptionPane.showMessageDialog(DumsorRoot.getMapPane(), text, 
					"Equipment damage", JOptionPane.WARNING_MESSAGE, icon);
			TimePanel.autoResume();
		}
	}
	
	//string methods for managing the text of maintDamagePopup
	private static String produceText(int wrapLength) {
		String returnText = "";
		if (lineSentence.equals("") == false) {
			returnText += WordUtils.wrap(lineIntro + "\n", wrapLength);
			returnText += WordUtils.wrap(lineSentence, wrapLength);
			returnText += "\n\n";
		}
		if (PVSentence.equals("") == false) {
			returnText += WordUtils.wrap(PVIntro + "\n", wrapLength);
			returnText += WordUtils.wrap(PVSentence, wrapLength);
			returnText += "\n\n";
		}
		if (battSentence.equals("") == false) {
			returnText += WordUtils.wrap(battIntro + "\n", wrapLength);
			returnText += WordUtils.wrap(battSentence, wrapLength);
		}
		if (returnText.equals("") == false) {
			returnText = WordUtils.wrap(intro, wrapLength) + "\n" + returnText;
		}
		return returnText;
	}
	
	public static void eraseSentences() {
		lineSentence = "";
		PVSentence = "";
		battSentence = "";
	}
	
	public static void appendLineSent(String appendix) {
		lineSentence += appendix;
	}
	
	public static void termLineSent() {
		if (lineSentence.equals("") == false) {
			lineSentence = lineSentence.substring(0, lineSentence.length()-1);
			lineSentence += ".";
		}
	}
	
	public static void appendPVSent(String appendix) {
		PVSentence += appendix;
	}
	
	public static void termPVSent() {
		if (PVSentence.equals("") == false) {
			PVSentence = PVSentence.substring(0, PVSentence.length()-1);
			PVSentence += ".";
		}
	}
	
	public static void appendBattSent(String appendix) {
		battSentence += appendix;
	}
	
	public static void termBattSent() {
		if (battSentence.equals("") == false) {
			battSentence = battSentence.substring(0, battSentence.length()-1);
			battSentence += ".";
		}
	}

}