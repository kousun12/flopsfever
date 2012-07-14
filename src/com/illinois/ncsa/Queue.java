package com.illinois.ncsa;

import java.util.ArrayList;
import java.util.List;

public class Queue {
	public static final int QUEUE_SIZE = 8;
    List<Job> jobs = new ArrayList<Job>();
	public int currSize;
	
	public Queue(){
		currSize = 0;
	}
	
	public boolean overFlow(){
		if(currSize==QUEUE_SIZE)
			return true;
		return false;
	}
	
	public void addJob(int type, int shape, int x, int y){
		jobs.add(new Job(type, shape, x, y));
		currSize++;
	}

	
	public int getSize(){
		return jobs.size();
	}
	
	public Job getJobinPosition(int i){
		return jobs.get(i);
	}
	
	public void removeJobinPosition(int i){
		jobs.remove(i);
		currSize--;
	}
	
	public void removeJob(Job j){
		jobs.remove(j);
		currSize--;
	}
}
