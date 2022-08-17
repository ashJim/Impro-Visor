package imp.reharm;

import imp.data.Score;

/**
 * This Reharm subclass was designed to demonstrate the pairing of diminished chords with notes outside 
 * the key as a method of reharmonisation. Notes in the key are paired with the diatonic chord with that 
 * note as its root. Notes that are out of key are paired with the diminished 7th chord that note as its 
 * root. At half bar intervals, the system checks the note at that slot and places its paired chord in 
 * that position. Its functionality has since been moved in to the Reharm subclasses NoteLedReharm and 
 * ChordLedReharm, both selectable from the pull down menu in the GUI, while the diminished chord generation 
 * functionality in generateSubstitutions has been migrated to the Reharm superclass for the sake of reusability.
 * @author Jim Ashford
 */
public class DiminishedReharm extends Reharm {
    public DiminishedReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        
        
        // Define the input arrays
        String[] dimChords = new String[5];
        
        // System.out.println(chords[chords.length-1]);
        
        // Populate the dimChords array
        for(int i = 0, j = 0; i < chords.length && j < dimChords.length; i++, j++) {
            if(i == 2 || i == 6) {
                j--;
                continue;
            } else {
                dimChords[j] = chords[i].substring(0, 1) + "#dim7";
            }
        }        
        // Define the output array
        String[][] chordSets = new String[12][1];
        
        // Populate the output array with the contents of the input arrays
        int[] majorScaleIndices = {0, 2, 4, 5, 7, 9, 11};
        
        for(int outputIndex = 0, majInputIndex = 0, dimInputIndex = 0;
        outputIndex < chordSets.length;
        outputIndex++) {
            boolean inKey = false;
            for(int i = 0; i < majorScaleIndices.length; i++) {
                if(outputIndex == majorScaleIndices[i]) {
                    inKey = true;
                }
            }
            // Case 1: outputIndex is the same as one of the chords indices.
            if(inKey) {
                chordSets[outputIndex][0] = chords[majInputIndex];
                majInputIndex++;
            } else {
                // Case 2: outputIndex is the same as one of the dimChords indices.
                chordSets[outputIndex][0] = dimChords[dimInputIndex];
                dimInputIndex++;
            }
        }          
        return chordSets;
    }

    @Override
    public int getChordDuration() {
        return score.getSlotsPerMeasure() / 2;
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        setRootChord(chordSlot);        
    }

    @Override
    public void adjustChordChoice(int slot) {
        // This mode uses only the notes of the melody to inform its chord choices,
        // so there is no implementation of adjustChordChoice.
        
    }
}
