package com.illinois.ncsa;

import java.util.ArrayList;
import java.util.List;

import framework.math.Vector2;


public class Job {

	public int type;
	public static final int GALAXY = 0;
    public static final int EPIDEMIC = 1;
    public static final int CHEMISTRY= 2;
    public static final int STORM = 3;
    public static final int ENGINEERING= 4;
    
    public int shape;
	public static final int SINGLE = 0;
    public static final int TUPLE = 1;
    public static final int rTUPLE = 11;
    public static final int TRIPLE= 2;
    public static final int CORNER = 3;
    public static final int rCORNER1 = 12;
    public static final int rCORNER2 = 13;
    public static final int rCORNER3 = 14;
    
    public static final int I= 4;
    public static final int S= 5;
    public static final int Z= 6;
    
    public static final int L= 7;
    public static final int rL= 15;
    
    public static final int J= 8;
    public static final int rJ= 16;

    public static final int T= 9;
    public static final int rT= 17;

    public static final int O= 10;
    
    public static final float SCALE_RATIO = .9f;
    
    public int state;
    public int numPieces=0;
    public boolean isSelected;
    public boolean isProcessing;
    public float scaleFactor;
    int width;
    int height;

    public List<Sprite> parts = new ArrayList<Sprite>();
    
    public Vector2 position;
    
    public Job(Job j){
    	state = j.state;
    	type = j.type;
    	shape = j.shape;
    	isSelected = j.isSelected;
    	isProcessing = j.isProcessing;
    	scaleFactor = j.scaleFactor;
    	width = j.width;
    	height = j.height;
    	numPieces = j.numPieces;
    	position = j.position;
    	parts = j.parts;
    	
    }
    
    
    public Job(int t, int s, int x, int y){
    	state = 0;
    	type = t;
    	shape = s;
    	isSelected = false;
    	isProcessing = false;
    	position = new Vector2(x,y);

    	
    	switch(shape){
    	
    	case SINGLE:
    		parts.add(new Sprite(0));
    		numPieces = 1;
    		width = 1;
    		height = 1;
    		scaleFactor = SCALE_RATIO; 
    		break;
    	case TUPLE:
    		parts.add(new Sprite(0));
    		parts.add(new Sprite(1));
    		numPieces = 2;
    		width = 2;
    		height = 1;
    		scaleFactor = SCALE_RATIO / 2;
    		break;
    	case TRIPLE:
    		parts.add(new Sprite(0));
    		parts.add(new Sprite(2));
    		parts.add(new Sprite(4));
    		numPieces = 3;
    		width = 1;
    		height = 3;
    		scaleFactor = SCALE_RATIO / 3;
    		break;
    	case CORNER:
    		parts.add(new Sprite(0));
    		parts.add(new Sprite(1));
    		parts.add(new Sprite(2));
    		numPieces = 3;
    		width = 2;
    		height = 2;
    		scaleFactor = SCALE_RATIO / 2;
    		break;
    	case I:
    		parts.add(new Sprite(0));
    		parts.add(new Sprite(2));
    		parts.add(new Sprite(4));
    		parts.add(new Sprite(6));
    		numPieces = 4;
    		width = 1;
    		height = 4;
    		scaleFactor = SCALE_RATIO / 4;
    		break;
    	case S:
    		parts.add(new Sprite(0));
    		parts.add(new Sprite(2));
    		parts.add(new Sprite(3));
    		parts.add(new Sprite(5));
    		numPieces = 4;
    		width = 2;
    		height = 3;
    		scaleFactor = SCALE_RATIO / 3;
    		break;
    	case Z:
    		parts.add(new Sprite(1));
    		parts.add(new Sprite(2));
    		parts.add(new Sprite(3));
    		parts.add(new Sprite(4));
    		numPieces = 4;
    		width = 2;
    		height = 3;
    		scaleFactor = SCALE_RATIO / 3;
    		break;
    	case L:
    		parts.add(new Sprite(0));
    		parts.add(new Sprite(2));
    		parts.add(new Sprite(4));
    		parts.add(new Sprite(5));    		
    		numPieces = 4;
    		width = 2;
    		height = 3;
    		scaleFactor = SCALE_RATIO / 3;
    		break;
    	case J:
    		parts.add(new Sprite(1));
    		parts.add(new Sprite(3));
    		parts.add(new Sprite(4));
    		parts.add(new Sprite(5));
    		numPieces = 4;
    		width = 2;
    		height = 3;
    		scaleFactor = SCALE_RATIO / 3;
    		break;
    	case T:
    		parts.add(new Sprite(1));
    		parts.add(new Sprite(2));
    		parts.add(new Sprite(3));
    		parts.add(new Sprite(5));
    		numPieces = 4;
    		width = 2;
    		height = 3;
    		scaleFactor = SCALE_RATIO / 3;
    		break;
    	case O:
    		parts.add(new Sprite(0));
    		parts.add(new Sprite(1));
    		parts.add(new Sprite(2));
    		parts.add(new Sprite(3));
    		numPieces = 4;
    		width = 2;
    		height = 2;
    		scaleFactor = SCALE_RATIO / 2;
    		break;
    	}
    }
    
    
    public int getNumPieces(){
    	return parts.size();
    }
    
    public void setPos(int x, int y){
    	position.x = x;
    	position.y = y;
    }
    
    public void setPos(float x, float y){
    	position.x = x;
    	position.y = y;
    }
    
    public Sprite getSprite(int i){
    	return parts.get(i);
    }
    
    public boolean onJob(int x, int y, int spriteWidth){
    		int length = (int)(spriteWidth*SCALE_RATIO);
    		int span = (int)(spriteWidth*SCALE_RATIO);
    		if(x>position.x && x<position.x + span && y>position.y && y<position.y+length)
    			return true;
    		
    		return false;
    	
    }
    public void rotate(){
    	if(shape==TUPLE || shape==CORNER || shape==L || shape==J || shape==T || shape==rTUPLE || shape==rCORNER1|| shape==rCORNER2|| shape==rCORNER3|| shape==rL|| shape==rJ|| shape==rT){
    		switch(shape){
    		
    		case(TUPLE):
    			parts.clear();
    			parts.add(new Sprite(0));
    			parts.add(new Sprite(2));
	    		width = 1;
	    		height = 2;
	    		shape = 11;
    			break;
    		case(rTUPLE):
    			parts.clear();
    			parts.add(new Sprite(0));
    			parts.add(new Sprite(1));
    			width = 2;
    			height = 1;
    			shape = TUPLE;
    			break;
    		case(CORNER):
    			parts.clear();
				parts.add(new Sprite(0));
				parts.add(new Sprite(1));
				parts.add(new Sprite(3));
	    		shape = rCORNER1;
    			break;
    		case(rCORNER1):
    			parts.clear();
				parts.add(new Sprite(1));
				parts.add(new Sprite(2));
				parts.add(new Sprite(3));
	    		shape = rCORNER2;
    			break;
    		case(rCORNER2):
    			parts.clear();
				parts.add(new Sprite(0));
				parts.add(new Sprite(2));
				parts.add(new Sprite(3));
	    		shape = rCORNER3;
    			break;
    		case(rCORNER3):
    			parts.clear();
				parts.add(new Sprite(0));
				parts.add(new Sprite(1));
				parts.add(new Sprite(2));
	    		shape = CORNER;
    			break;
    		case(L):
    			parts.clear();
				parts.add(new Sprite(0));
				parts.add(new Sprite(1));
				parts.add(new Sprite(3));
				parts.add(new Sprite(5));
	    		shape = rL;    			
    			break;
    		case(rL):
    			parts.clear();
				parts.add(new Sprite(0));
				parts.add(new Sprite(2));
				parts.add(new Sprite(4));
				parts.add(new Sprite(5));
	    		shape = L;    			
    			break;
    		case(J):
    			parts.clear();
				parts.add(new Sprite(0));
				parts.add(new Sprite(1));
				parts.add(new Sprite(2));
				parts.add(new Sprite(4));
	    		shape = rJ;    			
    			break;
    		case(rJ):
    			parts.clear();
				parts.add(new Sprite(1));
				parts.add(new Sprite(3));
				parts.add(new Sprite(4));
				parts.add(new Sprite(5));
	    		shape = J;    			
    			break;
    		case(T):
    			parts.clear();
				parts.add(new Sprite(0));
				parts.add(new Sprite(2));
				parts.add(new Sprite(3));
				parts.add(new Sprite(4));
	    		shape = rT;    			
    			break;
    		case(rT):
    			parts.clear();
				parts.add(new Sprite(1));
				parts.add(new Sprite(2));
				parts.add(new Sprite(3));
				parts.add(new Sprite(5));
	    		shape = T;    			
    			break;

    		}
    	}
    	else return;
    }
    

}
