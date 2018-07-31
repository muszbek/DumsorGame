package dumsorBase;

import java.util.ArrayList;

import dumsorPanels.MaintenancePanel;
import dumsorPanels.TimePanel;


public class Time extends Thread {
	private ArrayList<Node> nodes;
	private int timeStep;
	private long startTime;
	private Thread currentThread;
	private boolean suspended = false;
	private int hour;
	private int day;

	public Time(ArrayList<Node> nodeArray) {
		hour = 0;
		day = 0;
		nodes = nodeArray;
		timeStep = 1000;
		this.start();
	}
	
	public void run() {
		startTime = System.currentTimeMillis();
        // Remember which thread we are.
        currentThread = Thread.currentThread();
        // This is the animation loop.
        while (currentThread == this) {
            update();
            pausePoint();
            // Delay depending on how far we are behind.
            try {
                startTime += timeStep;
                Thread.sleep(Math.max(0, startTime-System.currentTimeMillis()));
                //pausePoint();
            }
            catch (InterruptedException e) {
                break;
            }
        }
	}
	
	private void update() {
		//System.out.println("ping");
		hourPass();
		TimePanel.refreshAnim();
		ElectricGrid.updateLineConst();
		ElectricGrid.getPowers();
		ElectricGrid.update();
		MaintenancePanel.updateMaint();
		for (Node aNode : ElectricGrid.getNodes()) {
			if (aNode instanceof District) {
				((District) aNode).checkEqDelay();
			}
		}
	}
	
	public synchronized void pausePoint() {
		try {
			while (suspended == true) {
				wait();
			}
		}
		catch (InterruptedException e) {
			System.out.println("time thread interrupted.");
		}
	}
	
	public synchronized void pause() {
		suspended = true;
	}
	
	public synchronized void unpause() {
		startTime = System.currentTimeMillis();
		suspended = false;
		notify();
	}
	
	public void changeTimeStep (int newStep) {
		timeStep = newStep;
	}
	
	private void hourPass() {
		if (hour == 23) {
			hour = 0;
			day++;
			Weather.updateDaily(day);
		}
		else {
			hour++;
		}
		//System.out.println("Sun: "+Weather.sunShine(hour, day)+"; Rain: "+Weather.getRain());
	}
	
	public int getHour() {
		return hour;
	}
	
	public double getSunShine() {
		return Weather.sunShine(hour, day);
	}
	
	public double getRain() {
		return Weather.getRain();
	}

}