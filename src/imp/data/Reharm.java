package imp.data;

import java.util.HashMap;

import imp.Constants; 

public abstract class Reharm implements Constants {
    
    /**
     * The HashMap of chords diatonic to the given key signature.
     */
    protected HashMap<String, String[]> keyChords;
    
    /**
     * The Score instance to reharmonise.
     */
    protected Score score;

    /**
     * Construct a new Reharm instance.
     */
    public Reharm(Score score) {
        this.score = score;
    }

    public abstract void execute();

    public abstract void generateChords(int keySig);

}