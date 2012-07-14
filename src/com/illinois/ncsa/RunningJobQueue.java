package com.illinois.ncsa;

import java.util.ArrayList;
import java.util.List;

public class RunningJobQueue {


	    List<RunningJob> rjobs;
		public int currSize;
		
		public RunningJobQueue(){
			currSize = 0;
			rjobs = new ArrayList<RunningJob>();
		}
		
		public void addToRunningJobs(RunningJob j){
			rjobs.add(j);
			currSize++;
		}
		
		public int getSize(){
			return rjobs.size();
		}
		
		public RunningJob getJobinPosition(int i){
			return rjobs.get(i);
		}
		
		public void removeJobinPosition(int i){
			rjobs.remove(i);
			currSize--;
		}
		public void remove(RunningJob rj){
			rjobs.remove(rj);
			currSize--;
		}

	

}
