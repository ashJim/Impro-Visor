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

    protected abstract HashMap<String, String[]> generateGFlatChords();
    protected abstract HashMap<String, String[]> generateDFlatChords();
    protected abstract HashMap<String, String[]> generateAFlatChords();
    protected abstract HashMap<String, String[]> generateEFlatChords();
    protected abstract HashMap<String, String[]> generateBFlatChords();
    protected abstract HashMap<String, String[]> generateFChords();
    protected abstract HashMap<String, String[]> generateCChords();
    protected abstract HashMap<String, String[]> generateGChords();
    protected abstract HashMap<String, String[]> generateDChords();
    protected abstract HashMap<String, String[]> generateAChords();
    protected abstract HashMap<String, String[]> generateEChords();
    protected abstract HashMap<String, String[]> generateBChords();
    protected abstract HashMap<String, String[]> generateFSharpChords();

}