package imp.reharm;

import java.util.Random;

import imp.data.Chord;
import imp.data.Note;
import imp.data.Score;

public class AdvancedReharm extends Reharm {

    public AdvancedReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        // Create an array to put the chord sets for each of the 12 chromatic notes.
        String[][] chordSets = chromaticChordSets();
        // Initialise the chordSets array with the standard chord options for each note.
        initStandardSubstitutions(chordSets);
        return chordSets;
    }

    @Override
    public void setChordDuration() {
        setChordDuration(score.getSlotsPerMeasure() / 2);
    }

    @Override
    public void implementChordChoice(int chordSlot) {
        
        String currentNote = getNoteNameAtSlot(chordSlot);    
        String prevNote = getNoteNameAtSlot(chordSlot - chordDuration);
                
        if(isStartOfBar(chordSlot)) {
            // if note is in key and not the same as the note at the last chord position...
            if(inKey(currentNote) && !currentNote.equals(prevNote)) {
                setChordMatch(chordSlot);
            }
            if(!inKey(currentNote)) {
                setDiminishedChord(chordSlot);
            }
            if(isChordVthenI(chordSlot - (chordDuration * 2), chordSlot)) {
                setAlteredChordV(chordSlot - chordDuration);
            }
            // if the chord in the middle of the last bar is tritone sub...
            if(isTritoneSub(chordSlot - chordDuration)) {
                // don't place the same chord as at the start of last bar.
                setDifferentChordTo(chordSlot - (chordDuration * 2), chordSlot);
            }
        }
        if(!isStartOfBar(chordSlot)) {
            if(inKey(currentNote)) {
                Chord prevChord = score.getChordProg().getPrevChord(chordSlot);
                if(prevChord != null) {
                    String prevChordName = prevChord.getName();
                    if(isChordToneOf(currentNote, prevChordName)) {
                        Random random = new Random();
                        if(random.nextInt(2) == 0) {
                            setTritoneSub(chordSlot - chordDuration, chordSlot);
                        } else {
                            score.getChordProg().delUnit(chordSlot);
                        }
                    } 
                    if(!isChordToneOf(currentNote, prevChordName)) {
                        setHarmonicSub(chordSlot);
                    }
                } 
                if(prevChord == null) {
                    setHarmonicSub(chordSlot);
                }
            } 
            if(!inKey(currentNote)) {
                setDiminishedChord(chordSlot);
            }
        }           
    }
}
