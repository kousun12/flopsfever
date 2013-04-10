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
    static final int XED = 3;

    public float time = 60f;
    public float addpiecetime;
    public float updatescoretime;
    public float runjobtime;
    public boolean gameOver = false;;
    public int score = 0;
    int cores[][];  //possible values: 0->free; 3->X; 1->processing; 2->bonus;
    public Queue queue;
    public RunningJobQueue rQueue;
    public FinishedJobs finishedJobs;
    public boolean timeUp = false;
    
    public final int WORLD_WIDTH;
    public final int WORLD_HEIGHT;
    
    Random random = new Random();
    float tickTime = 0;
    static float tick = TICK_INITIAL;
    public int[] shapes;
    int incrementer=0;

    
    public World(int w, int h, int[]s){
    	queue = new Queue();
    	rQueue = new RunningJobQueue();
    	finishedJobs = new  FinishedJobs();
    	WORLD_WIDTH = w;
    	WORLD_HEIGHT = h;
    	cores = new int[NUM_PROCESSORS][8];
    	addpiecetime = 1.6f;
    	shapes =s;
    }



    public void update(float deltaTime) {
    	if(queue.overFlow()){
        	gameOver = true;
        	return;
        }
        if (gameOver || timeUp)
            return;
        if(time<=0)
        	timeUp = true; 
        time -= deltaTime;
        addpiecetime+=deltaTime;
        updatescoretime+=deltaTime;
        if(addpiecetime>2f&&incrementer<shapes.length){
        	addpiecetime=0;
        	queue.addJob(random.nextInt(5),shapes[incrementer], WORLD_WIDTH, 0);
        	incrementer++;
        }
        
        if(updatescoretime>0.25f){
        	boolean [] bonus = {true,true,true,true};
        	int [] bcount = {0,0,0,0};
        	updatescoretime = 0;
	        for(int i=0; i<cores.length; i++){
	        	for(int j =0; j<cores[0].length; j++){
	        		if (cores[i][j]==World.PROCESSING){
	        			score+=SCORE_INCREMENT;
	        			bcount[i]++;
	        		}
	        		else if(cores[i][j]==World.FREE)
	        			bonus[i] = false;
	        	}
	        	if(bonus[i])
	        		score+=SCORE_INCREMENT*bcount[i]*2;
	        }
        }
        
        for(int i=0; i<rQueue.getSize(); i++){
        	RunningJob c = rQueue.getJobinPosition(i);
        	c.timeLeft-=deltaTime;
        	if(c.timeLeft<=0f){
        		finishJob(c);
        	}
        }
        
        
        
    }
    
    public void runJob(Job j, int p, int c){
    	j.isProcessing = true;
    	queue.removeJob(j);
    	int numP = j.parts.size();
    	int offset = c/2;
    	int realoffset = offset*2;

    	for(int i=0; i<numP; i++){
    		int changeThisSpriteState = j.getSprite(i).getPos() + realoffset;
    		cores[p][changeThisSpriteState] = World.PROCESSING;
    		//System.out.print(changeThisSpriteState);
    	}
    	rQueue.addToRunningJobs(new RunningJob(j,p,c));
    }
    
    public void finishJob(RunningJob rj){
    	//reset states
    	int numP = rj.parts.size();
    	int c = rj.corePos;
    	int p = rj.processorPos;
    	int offset = c/2;
    	int realoffset = offset*2;

    	for(int i=0; i<numP; i++){
    		int changeThisSpriteState = rj.getSprite(i).getPos() + realoffset;
    		cores[p][changeThisSpriteState] = World.FREE;
    		//System.out.print(changeThisSpriteState);
    	}
    	//System.out.println();
    	
    	rj.isProcessing = false;
    	finishedJobs.addToFinishedJobs(rj);
    	rQueue.remove(rj);
    	
    	
    }
    
    
}
