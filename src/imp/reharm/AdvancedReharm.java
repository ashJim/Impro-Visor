package imp.reharm;

import java.util.Arrays;
import java.util.List;

import imp.data.Score;

public class AdvancedReharm extends Reharm {

    public AdvancedReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        // Create an array to put the chord sets for each of the 12 chromatic notes.
        String[][] chordSets = new String[12][];
        // Add the root chords to the diatonic positions in the chordSets array.
        populateDiatonicChordSets(chordSets, 0, chords);
        // Create an array of tritone substitutions to add to chordSets later.
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
        // Add tritone subs to the diatonic positions in the chordSets array.
        populateDiatonicChordSets(chordSets, 1, tritoneChords);
        // Define the input arrays
        String[] dimChords = new String[5];        
        // Populate the dimChords array
        for(int i = 0, j = 0; i < chords.length && j < dimChords.length; i++, j++) {
            if(i == 2 || i == 6) {
                j--;
                continue;
            } else {
                dimChords[j] = chords[i].substring(0, 1) + "#dim7";
            }
        }
        // Add diminished chords to the non-diatonic positions in the chordSets array.
        populateOutsideChordSets(chordSets, 0, dimChords);
        return chordSets;
    }

    @Override
    public void setChordDuration() {
        setChordDuration(score.getSlotsPerMeasure() / 2);
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        List<String> notesInKey = Arrays.asList(getNotesInKey());
        String currentNote = getNoteNameAtSlot(chordSlot);
        if(notesInKey.contains(currentNote) && !currentNote.equals(getNoteNameAtSlot(chordSlot - chordDuration))) {
            setRootChordFromNote(chordSlot);
        }
    }
}
