package imp.reharm;

import java.util.HashMap;
import java.util.Random;

import imp.Constants;
import imp.data.Chord;
import imp.data.MelodyPart;
import imp.data.Note;
import imp.data.Score; 

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
     * Executes the reharmonisation of the chords, based on the generated chord set 
     * in the current key signature. This standard version of the method picks a chord 
     * at random from the available chords. This could be overridden in subclasses  
     * requiring more complicated logic.
     */
    public void execute() {
        generateChords(score.getKeySignature());
        // Select the MelodyPart for analysis
        MelodyPart targetPart = score.getPart(0);
        if(targetPart == null) return;
        // For each bar...
        for(int i = 0; i < targetPart.size() ; i = i + score.getSlotsPerMeasure()) {
            // Get the note at the beginning of the bar or 
            // move to the next bar if there is no note.
            Note targetNote = targetPart.getNote(i);
            if(targetNote == null) continue;
            // Get the name of the note at the beginning of the bar
            String targetNoteName = targetNote.getPitchClassName();
            // If the note name is the same as one of the keys in the 
            // keyChords HashMap...
            if(keyChords.containsKey(targetNoteName)) {
                // Get the chords that match to this note
                String[] chordChoices = keyChords.get(targetNoteName);
                // The chord to be instantiated in this position
                String chord;
                // Set up a new Random object.
                Random random = new Random();
                // Randomly select between the chords that map to this note.
                chord = chordChoices[random.nextInt(chordChoices.length)];
                // Set the chord for this position on the score as decided in logic above
                score.getChordProg().setChord(i, new Chord(chord));
            }
        }
    }


    /**
     * Generates a set of diatonic chords for the current key signature of the score.
     */
    public void generateChords(int keySig) {
        keyChords = new HashMap<>();

        switch(keySig) {
            case GBMAJOR:    // Key: Gb maj
                keyChords = generateGFlatChords();
                break;
            case DBMAJOR:    // Key: Db maj
                keyChords = generateDFlatChords();
                break;
            case ABMAJOR:    // Key: Ab maj
                keyChords = generateAFlatChords();
                break;
            case EBMAJOR:    // Key: Eb maj
                keyChords = generateEFlatChords();
                break;
            case BBMAJOR:    // Key: Bb maj
                keyChords = generateBFlatChords();
                break;
            case FMAJOR:    // Key: F maj
                keyChords = generateFChords();
                break;
            case GMAJOR:     // Key: G maj
                keyChords = generateGChords();
                break;
            case DMAJOR:     // Key: D maj
                keyChords = generateDChords();
                break;
            case AMAJOR:     // Key: A maj
                keyChords = generateAChords();
                break;
            case EMAJOR:     // Key: E maj
                keyChords = generateEChords();
                break;
            case BMAJOR:     // Key: B maj
                keyChords = generateBChords();
                break;
            case FSMAJOR:     // Key: F# maj
                keyChords = generateFSharpChords();
                break;
            default:    // Key: C maj
                keyChords = generateCChords();
                break;
        }

    }

    /**
     * Used to map notes to potential chords in the key of C. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateCChords();
    /**
     * Used to map notes to potential chords in the key of G. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateGChords();
    /**
     * Used to map notes to potential chords in the key of D. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateDChords();
    /**
     * Used to map notes to potential chords in the key of A. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateAChords();
    /**
     * Used to map notes to potential chords in the key of E. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateEChords();
    /**
     * Used to map notes to potential chords in the key of B. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateBChords();
    /**
     * Used to map notes to potential chords in the key of F#. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateFSharpChords();
    /**
     * Used to map notes to potential chords in the key of F. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateFChords();
    /**
     * Used to map notes to potential chords in the key of Bb. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateBFlatChords();
    /**
     * Used to map notes to potential chords in the key of Eb. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateEFlatChords();
    /**
     * Used to map notes to potential chords in the key of Ab. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateAFlatChords();
    /**
     * Used to map notes to potential chords in the key of Db. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateDFlatChords();
    /**
     * Used to map notes to potential chords in the key of Gb. Must be 
     * implemented in Reharm subclasses.
     */
    protected abstract HashMap<String, String[]> generateGFlatChords();

}