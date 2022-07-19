package imp.reharm;

import java.util.ArrayList;
import java.util.Collections;

import imp.data.Chord;
import imp.data.Score;

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
    public void setChordDuration() {
        setChordDuration(score.getSlotsPerMeasure());
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
}
