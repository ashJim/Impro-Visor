package imp.reharm;

import java.util.ArrayList;
import java.util.Collections;

import imp.data.Chord;
import imp.data.Score;

/**
 * This Reharm subclass we designed to demonstrate the use of the most common note in a section to 
 * generate a chord for that section as a method of reharmonisation. Its functionality has since been 
 * moved in to the Reharm subclasses NoteLedReharm and ChordLedReharm, both selectable from the pull 
 * down menu in the GUI, while the functionality in implementChordChoice that generates chords based 
 * on the most common note in the section has been migrated to the Reharm superclass for the sake of 
 * reusability.
 * @author Jim Ashford
 */
public class CommonNoteReharm extends Reharm {

    public CommonNoteReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        String[][] chordSet = new String[chords.length][1];
        for(int i = 0; i < chordSet.length; i++) {
            chordSet[i][0] = chords[i];
        }
        return chordSet;
    }

    @Override
    public int getChordDuration() {
        return score.getSlotsPerMeasure();
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        // a place to store the notes in the bar
        ArrayList<String> notesInBar = new ArrayList<>();
        // For each note in that bar...
        for(int i = chordSlot; i < chordSlot + chordDuration; i++) {
            // get the note
            String noteName = getNoteNameAtSlot(i);
            // move on if not a diatonic note
            if(!keyChords.containsKey(noteName)) continue;
            // add to notesInBar container
            notesInBar.add(noteName);
        }
        String mostFrequentNote = null;
        int freq = 0;
        for(String n : notesInBar) {
            if(Collections.frequency(notesInBar, n) > freq) {
                mostFrequentNote = n;
                freq = Collections.frequency(notesInBar, n);
            }
        }
        String[] chordChoices = keyChords.get(mostFrequentNote);
        if(chordChoices != null) {
            String chord = chordChoices[0];
            // Set the chord for this position on the score as decided in logic above
            if(chord != null) {
            score.getChordProg().setChord(chordSlot, new Chord(chord));
            }
        }
    }

    @Override
    public void adjustChordChoice(int slot) {
        // This mode uses only the notes of the melody to inform its chord choices,
        // so there is no implementation of adjustChordChoice.
        
    }
}
