package dumsorBase;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import dumsorPanels.*;

public class District extends Node {
	
	private static final int eqDelayTime = 24;	//24 hours
	
	private ConstructionDataPanel constDataPanel;	//constructionPanel will add this
	private int PVcapacity;
	private int PVNominal;
	private final int houses;	//amount of households
	private int housesElectrified;	//electrify them gradually so they consume
	private ConsumptionHabit consumptionHabit = null;
	private HappinessPanel happinessPanel;
	private Battery battery;
	private int batNominal;
	private Point PVDelay = null;	//int-int relation, contra-intuitive but easy
	private Point batDelay = null;
	private boolean fullElectPopupped = false;
	private boolean fullConsPopupped = false;
	
	public District(String inName, int x, int y, int inHouses) {
		super(inName, x, y);
		icon = "pics/plug.gif";

		try {
			super.iconImage = ImageIO.read(ResLoader.loadStr(icon));
		} catch (IOException e) {
			System.out.println("icon image not found");
		}
		
		houses = inHouses;
		housesElectrified = 0;
		constDataPanel = new ConstructionDataPanel(houses);
		
		super.consumedPower = 0;
		super.producedPower = 0;
		
		battery = new Battery();
		
		super.fullyElectrified = false;
	}
	
	public void buyPV(int boughtAmount) {
		if (boughtAmount > 0) {
			if (MoneyPanel.spendMoney(MoneyPanel.pvUnitPrice * boughtAmount) == true) {
//				if (PVcapacity == 0) {
//					ElectricGrid.setSource(true, this);
//				}
				if (PVDelay == null) {
					PVDelay = new Point(boughtAmount, eqDelayTime);
				}
				else {
					PVDelay = new Point(PVDelay.x + boughtAmount, eqDelayTime);
				}
				
				PVNominal += boughtAmount;
				constDataPanel.updatePVtext(PVNominal);
				Popups.solarPopup(60);
			}
		}
	}
	
	private void addPV(int boughtAmount) {
		electrify(true);
		PVcapacity += boughtAmount;
		MaintenancePanel.addMaintCost(boughtAmount * MaintenancePanel.PVUnitMaint);
		//System.out.println("pv-s added");
	}
	
	public void buyBattery(int boughtAmount) {
		if (boughtAmount > 0) {
			if (MoneyPanel.spendMoney(MoneyPanel.batteryUnitPrice * boughtAmount) == true) {
				if (batDelay == null) {
					batDelay = new Point(boughtAmount, eqDelayTime);
				}
				else {
					batDelay = new Point(batDelay.x + boughtAmount, eqDelayTime);
				}
				
				batNominal += boughtAmount;
				constDataPanel.updateBatText(batNominal);
				Popups.batteryPopup(55);
			}
		}
	}
	
	private void addBattery(int boughtAmount) {
		battery.addBattery(boughtAmount);
		MaintenancePanel.addMaintCost(boughtAmount * MaintenancePanel.batUnitMaint);
	}
	
	public void checkEqDelay() {
		if (PVDelay != null) {
			if (PVDelay.y > 0) {
				PVDelay.setLocation(new Point(PVDelay.x, PVDelay.y-1));
			}
			else {
				addPV(PVDelay.x);
				PVDelay = null;
			}
		}
		if (batDelay != null) {
			if (batDelay.y > 0) {
				batDelay.setLocation(new Point(batDelay.x, batDelay.y-1));
			}
			else {
				addBattery(batDelay.x);
				batDelay = null;
			}
		}
	}
	
	public void destroyRandomPV() {
		//Random random = new Random();
		int removed = 0;
		for (int i=0; i<PVcapacity; i++) {	//THINK about PV buying stacks, this is by single kW
			if (Math.random() < MaintenancePanel.PVDestroyPercent) {
				PVcapacity--;
				PVNominal--;
				constDataPanel.updatePVtext(PVNominal);
				MaintenancePanel.removeMaintCost(MaintenancePanel.PVUnitMaint);
				removed++;
			}
		}
		if (removed > 0) {
			Popups.appendPVSent(" " + removed + " kW in " + this.getName() + ",");
		}
	}
	
	public void destroyRandomBatt() {
		//Random random = new Random();
		int removed = 0;
		for (int j=0; j<battery.getCapacity(); j++) {
			if (Math.random() < MaintenancePanel.batDestroyPercent) {
				battery.removeBattery();
				batNominal--;
				constDataPanel.updateBatText(batNominal);
				MaintenancePanel.removeMaintCost(MaintenancePanel.batUnitMaint);
				removed++;
			}
		}
		if (removed > 0) {
			Popups.appendBattSent(" " + removed + " kW in " + this.getName() + ",");
		}
	}
	
	public void electrifyHouse(int electrifiedAmount) {
		if (MoneyPanel.spendMoney(MoneyPanel.electrifyPrice * electrifiedAmount) == true) {
			if (housesElectrified == 0) {
				happinessPanel = new HappinessPanel(super.name);
			}
			
			housesElectrified += electrifiedAmount;
			
			for (int i=0; i<electrifiedAmount; i++) {
				consumptionHabit.electrifySingleHouse();
			}
			
			constDataPanel.updateHouseText(housesElectrified, houses);
			if (housesElectrified >= houses) {
				constDataPanel.maxHouses();
				fullyElectrified = true;
				if (fullElectPopupped == false) {
					fullElectPopupped = true;
					Popups.fullElectPopup(50);
				}
			}
			constDataPanel.setHouseSpinnerMax(houses - housesElectrified);
			Popups.houseElectPopup(50);
		}
	}
	
	public Component getConstDataPanel() {
		return constDataPanel;
	}
	
	public void updateHappiness() {
		if (happinessPanel != null) {
			happinessPanel.updatePercent(consumptionHabit.getHappines().getPercentage());
		}
	}
	
	//from nodes the overriden methods are called just like that, in ElectricGrid
	
	@Override
	public void produce() {
		producedPower = (PVcapacity * ElectricGrid.getTime().getSunShine());
		if (super.getSource() == true) {
			if (consumedPower >= producedPower) {
				//super.setSource(false);
				ElectricGrid.setSource(false, this);
			}
		}
		else {
			if (consumedPower < producedPower) {
				//super.setSource(true);
				ElectricGrid.setSource(true, this);
			}
		}
		
		payConsumption();
		useBattery();
		setHappiness();
	}
	
	@Override
	public void consume() {
		if (getElectrified() == true) {
			//consumedPower = housesElectrified * 20;
			consumedPower = consumptionHabit.getCons(housesElectrified);
			
			//just popupping
			if (housesElectrified >= houses) {
				if (fullConsPopupped == false) {
					//System.out.println(consumptionHabit.getConsIntroDelay());
					if (consumptionHabit.getConsIntroDelay() == 0.0) {
						fullConsPopupped = true;
						Popups.fullConsPopup(50);
					}
				}
			}
		}
	}
	
	private void payConsumption() {
		if (consumptionHabit != null) {
			if (super.getNodeEnabled() == true) {
				MoneyPanel.earnMoney(consumedPower * MoneyPanel.unitPowerPrice);
			}
			else {
				double balance = producedPower - consumedPower;
				if (balance > 0) {
					MoneyPanel.earnMoney(consumedPower * MoneyPanel.unitPowerPrice);
				}
				else if (balance < 0) {
					MoneyPanel.earnMoney(producedPower * MoneyPanel.unitPowerPrice);
				}
			}
		}
	}
	
	private void useBattery() {
		if (battery.getCapacity() > 0) {
			double balance = producedPower - consumedPower;
			if (balance > 0) {
				double charge = battery.charge(balance);
				consumedPower += charge;
			}
			else if (balance < 0) {
				double charge = battery.discharge(-balance);
				producedPower += charge;
			}
			//I did not add this label, does not look good
			super.getPowerLabels().updateBatText(battery.getCharge());
			//System.out.println(battery.getCharge());
		}
	}
	
	private void setHappiness() {
		if (consumptionHabit != null) {
			if (super.getNodeEnabled() == true) {
				consumptionHabit.getHappy();
			}
			else {
				double balance = producedPower - consumedPower;
				if (balance > 0) {
					consumptionHabit.getHappy();
				}
				else if (balance < 0) {
					consumptionHabit.getRage();
				}
				//System.out.println(consumptionHabit.getHappines().getBuildup());
			}
		}
	}
	
	@Override
	public boolean getFullyMaxed() {	//for querying winning condition
		boolean answer = false;
		if (fullyElectrified == true && consumptionHabit.getConsIntroDelay() == 0.0) {
			answer = true;
		}
		return answer;
	}
	
	@Override
	public void electrify(boolean isEnabled) {
		if (consumptionHabit == null) {
			consumptionHabit = new ConsumptionHabit();
			//happinessPanel = new HappinessPanel(super.name);
			//MyDumsor.getControlPanel().addHappinessPanel(happinessPanel);
		}
		super.electrify(isEnabled);
		constDataPanel.houseEnable(true);
		Popups.districtElectPopup(50);
	}

	@Override
	public void setNodeEnabled(boolean inEnabled) {
		super.setNodeEnabled(inEnabled);
		if (happinessPanel != null) {
			happinessPanel.setColorEnabled(inEnabled);
		}
	}
	
	@Override
	public void setNodeSelected(boolean inSelected) {
		if (happinessPanel != null) {
			happinessPanel.setFontSelected(inSelected);
		}
		super.setNodeSelected(inSelected);
	}
}

//total houses should be custom instead of 10