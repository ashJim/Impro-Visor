package imp.data;

import java.util.HashMap;
import java.util.Random;

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

    /**
     * Execute the reharmonisation of the chords.
     */
    public void execute() {
        generateChords(score.getKeySignature());

        for(int i = 0; i < score.getPart(0).size() ; i = i + score.getSlotsPerMeasure()) {
            MelodyPart targetPart = score.getPart(0);
            if(targetPart == null) return;
    
            Note targetNote = targetPart.getNote(i);
            if(targetNote == null) continue;
    
            String targetNoteName = targetNote.getPitchClassName();
    
            if(keyChords.containsKey(targetNoteName)) {
                String[] chordChoice = keyChords.get(targetNoteName);
                Random random = new Random();
                String chord = chordChoice[random.nextInt(chordChoice.length)];
                score.getChordProg().setChord(i, new Chord(chord));
            }
        }
    }


    public abstract void generateChords(int keySig);

}