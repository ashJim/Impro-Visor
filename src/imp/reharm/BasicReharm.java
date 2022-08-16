package imp.reharm;

import imp.data.Score;

/**
 * BasicReharm is the simplest subclass of Reharm. It is listed as 'Basic' in the 
 * pull down menu next to the Reharm button in the GUI. This version pairs each 
 * note in the key with its corresponding diatonic chord. It looks at the notes 
 * in each bar and places at the bar's beginning the diatonic chord paired with 
 * the most common note.
 * @author Jim Ashford
 */
public class BasicReharm extends Reharm {

    /**
     * The constructor for BasicReharm.
     * @param score The Score instance to associate with this BasicReharm.
     */
    public BasicReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        // This mode reacts only to notes that are in key.
        String[][] chordSets = diatonicChordSets();
        // Each note in key is paired with a diatonic chord, built from that note.
        addDiatonicChords(chordSets);
        return chordSets;
    }

    @Override
    public int getChordDuration() {
        // Chords are added at the beginning of each bar only.
        return score.getSlotsPerMeasure();
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        // A chord is selected that best matches the notes in the current slot.
        setChordMatch(chordSlot);
    }

    @Override
    public void adjustChordChoice(int chordSlot) {
        // This mode uses only the notes of the melody to inform its chord choices,
        // so there is no implementation of adjustChordChoice.
    }
}