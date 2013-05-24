package proteinprediction;

import java.lang.Thread;

/**
 * This is an interface defining an predictor for the TML/TMH problem
 * 
 * @author Yann
 */
public abstract class Predictor extends Thread {
    
    public abstract void initPredictor();
    public abstract void setInput();
    public abstract void calculatePrediction();
    public abstract void getPredictedResult();
    
}
