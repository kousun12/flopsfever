package com.illinois.ncsa;

import java.util.Random;

public class World {
    static final int NUM_PROCESSORS = 4;
    static final int JOB_HEIGHT = 4;
    static final int JOB_WIDTH = 2;
    static final int SCORE_INCREMENT = 1;
    static final float TICK_INITIAL = 0.5f;
    static final int FREE = 0;
    static final int PROCESSING = 1;
    static final int BONUS = 2;

    public float time = 60f;
    public float addpiecetime;
    public float updatescoretime;
    public float runjobtime;
    public boolean gameOver = false;;
    public int score = 0;
    int cores[][];  //possible values: 0->free; 1->X; 2->processing; 3->bonus;
    public Queue queue;
    public RunningJobQueue rQueue;
    public FinishedJobs finishedJobs;
    public boolean timeUp = false;
    
    public final int WORLD_WIDTH;
    public final int WORLD_HEIGHT;
    
    
    Random random = new Random();
    float tickTime = 0;
    static float tick = TICK_INITIAL;

    
    public World(int w, int h){
    	queue = new Queue();
    	rQueue = new RunningJobQueue();
    	WORLD_WIDTH = w;
    	WORLD_HEIGHT = h;
    	cores = new int[NUM_PROCESSORS][8];
    }



    public void update(float deltaTime) {
        if (gameOver || timeUp)
            return;
        if(time<=0)
        	timeUp = true; 
        time -= deltaTime;
        addpiecetime+=deltaTime;
        updatescoretime+=deltaTime;
        if(addpiecetime>3f){
        	addpiecetime=0;
        	queue.addJob(random.nextInt(5),random.nextInt(11), WORLD_WIDTH, 0);
        }
        
        if(updatescoretime>0.1f){
        	updatescoretime = 0;
	        for(int i=0; i<cores.length; i++){
	        	for(int j =0; j<cores[0].length; j++){
	        		if (cores[i][j]==World.PROCESSING)
	        			score+=10;
	        		else if(cores[i][j]==World.BONUS)
	        			score+=20;
	        	}
	        }
        }
        
        for(int i=0; i<rQueue.getSize(); i++){
        	RunningJob c = rQueue.getJobinPosition(i);
        	c.runtime-=deltaTime;
        	if(c.runtime<=0f){
        		finishJob(c);
        	}
        }
        

        
        if(queue.overFlow()){
        	gameOver = true;
        	return;
        }
        
    }
    
    public void runJob(Job j, int p, int c){
    	j.isProcessing = true;
    	queue.removeJob(j);
    	rQueue.addToRunningJobs(new RunningJob(j,c,p));
    }
    
    public void finishJob(RunningJob rj){
    	rj.isProcessing = false;
    	finishedJobs.addToFinishedJobs(rj);
    	rQueue.remove(rj);
    	
    }
    
}
