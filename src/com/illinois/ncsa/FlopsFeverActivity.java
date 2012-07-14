package com.illinois.ncsa;

import framework.Screen;
import framework.impl.AndroidGame;

public class FlopsFeverActivity extends AndroidGame {
    /** Called when the activity is first created. */
    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }
}