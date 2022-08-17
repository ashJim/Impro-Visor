package imp.reharm;

import imp.data.Score;

/**
 * This Reharm subclass was designed to demonstrate tritone substitution as a method of reharmonisation.
 * At the beginning of each bar, it places a diatonic chord based on the note present in that slot. In 
 * the middle of each bar, it places either the root chord or the tritone substitution based on that same 
 * note from the beginning of the bar. Its functionality has since been moved in to the Reharm subclasses 
 * NoteLedReharm and ChordLedReharm, both selectable from the pull down menu in the GUI, while the tritone 
 * chord generation functionality in generateSubstitutions has been migrated to the Reharm superclass for 
 * the sake of reusability.
 * @author Jim Ashford
 */
public class TritoneSubReharm extends Reharm {
    public TritoneSubReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        String[] tritoneChords = new String[7];
        for(int i = 0; i < chords.length; i++) {
            String chord = chords[(i + 3) % 7];
            if(i == 3) {
                chord = chord.charAt(0) + "7";
            } else if(chord.length() > 1 && chord.charAt(1) == 'b') {
                chord = chord.charAt(0) + "7";
            } else {
                chord = chord.charAt(0) + "#7";
            }
            tritoneChords[i] = chord;
        }
        String[][] chordSets = new String[7][2];
        for(int i = 0; i < chordSets.length; i++) {
            chordSets[i][0] = chords[i];
            chordSets[i][1] = tritoneChords[i];
        }
        return chordSets;
    }

    @Override
    public int getChordDuration() {
        return score.getSlotsPerMeasure() / 2;
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        if(chordSlot % (chordDuration * 2) == 0) {
            setRootChord(chordSlot);
        } else {
            setRandomChordFromNote(chordSlot - (chordDuration), chordSlot);
        }
    }

    @Override
    public void adjustChordChoice(int slot) {
        // This mode uses only the notes of the melody to inform its chord choices,
        // so there is no implementation of adjustChordChoice.
    }
    
}