package imp.reharm;

import imp.data.Score;

public class OutsideReharm extends Reharm {
    public OutsideReharm(Score score) {
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
        outputIndex < chordSets.length ;
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
    public void setChordDuration() {
        setChordDuration(score.getSlotsPerMeasure() / 2);
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        setRootChordFromNote(chordSlot);        
    }
}
