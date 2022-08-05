package imp.reharm;

import java.util.Random;

import imp.Constants;
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
    public int getChordDuration() {
        return score.getSlotsPerMeasure() / 2;
    }

    @Override
    public void implementChordChoice(int slot) {
        
        String currentNote = getNoteNameAtSlot(slot);    
        String prevNote = getNoteNameAtSlot(slot - chordDuration);
        
        if(isStartOfBar(slot)) {
            // if note is in key and not the same as the note at the last chord position...
            if(inKey(currentNote) && !currentNote.equals(prevNote)) {
                setChordMatch(slot);
            }
            if(!inKey(currentNote)) {
                setDiminishedChord(slot);
            }
            if(isChordNumber(slot - (chordDuration * 2), 5)
            && (isChordNumber(slot, 1))) {
                setAlteredChord(slot - chordDuration, 5);
            }
            // if the chord in the middle of the last bar is tritone sub...
            if(isTritoneSub(slot - chordDuration)) {
                // don't place the same chord as at the start of last bar.
                setDifferentChordTo(slot - (chordDuration * 2), slot);
            }
            // Complete any partial II V I patterns
            if(isChordNumber(slot - (chordDuration * 2), 2)
            && (isChordNumber(slot - chordDuration, 5))) {
                setDiatonicChord(slot, 1);
            } else if(isChordNumber(slot - (chordDuration * 2), 2)
            && (isChordNumber(slot, 1))) {
                setDiatonicChord(slot - chordDuration, 5);
            } else if(isChordNumber(slot - chordDuration, 5)
            && (isChordNumber(slot, 1))) {
                setDiatonicChord(slot - (chordDuration * 2), 2);
            }
        }
        if(!isStartOfBar(slot)) {
            Note startBarNote = score.getPart(0).getNote(slot - chordDuration);
            if(startBarNote instanceof Note && startBarNote.getRhythmValue() == score.getSlotsPerMeasure()
            && inKey(startBarNote.getPitchClassName())) {
                Random random = new Random();
                if(random.nextInt(2) == 0) {
                    setTritoneSub(slot - chordDuration, slot);
                }
            }
            if(inKey(currentNote)) {
                Chord prevChord = score.getChordProg().getPrevChord(slot);
                if(prevChord != null) {
                    String prevChordName = prevChord.getName();
                    if(isChordToneOf(currentNote, prevChordName)) {
                        Random random = new Random();
                        if(random.nextInt(2) == 0) {
                            setTritoneSub(slot - chordDuration, slot);
                        } else {
                            score.getChordProg().delUnit(slot);
                        }
                    } 
                    if(!isChordToneOf(currentNote, prevChordName)) {
                        setHarmonicSub(slot);
                    }
                } 
                if(prevChord == null) {
                    setHarmonicSub(slot);
                }
            } 
            if(!inKey(currentNote)) {
                setDiminishedChord(slot);
            }
        }
        // Set something that resolves for the last bar
        if(isStartOfBar(slot) && isLastBar(slot)) {
            setDiatonicChord(slot, 1);
        }
    }

    @Override
    public void adjustChordChoice(int chordSlot) {
        // TODO Auto-generated method stub
        
    }
}
