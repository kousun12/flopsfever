package com.illinois.ncsa;

import java.util.ArrayList;
import java.util.List;

public class FinishedJobs {

    List<Job> jobs = new ArrayList<Job>();
	public int numGalaxy;
	public int numEpidemic;
	public int numChemistry;
	public int numStorm;
	public int numEngineering;
	
	
	public static final int GALAXY = 0;
    public static final int EPIDEMIC = 1;
    public static final int CHEMISTRY= 2;
    public static final int STORM = 3;
    public static final int ENGINEERING= 4;
	public FinishedJobs(){
		numGalaxy = 0;
		numEpidemic = 0;
		numChemistry = 0;
		numStorm = 0;
		numEngineering = 0;
	}

	public void addToFinishedJobs(Job j){
		switch(j.type){
		case Job.GALAXY:
			numGalaxy++;
			break;
		case Job.EPIDEMIC:
			numEpidemic++;
			break;
		case Job.CHEMISTRY:
			numChemistry++;
			break;
		case Job.STORM:
			numStorm++;
			break;
		case Job.ENGINEERING:
			numEngineering++;
			break;
		}
		
	}
	

}
