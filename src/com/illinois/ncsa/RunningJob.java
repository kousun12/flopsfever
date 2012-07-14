package com.illinois.ncsa;

public class RunningJob extends Job {
	
	int processorPos;
	int corePos;
	float timeLeft;
	
	RunningJob(Job j, int p, int c){
		super(j);
		processorPos = p;
		corePos = c;
		timeLeft = 3f;
	}
	
	
	

}
