package com.illinois.ncsa;

import java.util.ArrayList;
import java.util.List;

public class FinishedJobs {

    List<RunningJob> jobs = new ArrayList<RunningJob>();
	public int numGalaxy;
	public int numEpidemic;
	public int numChemistry;
	public int numStorm;
	public int numEngineering;
	
	

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
