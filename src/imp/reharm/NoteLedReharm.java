package imp.reharm;

import java.util.Random;

import imp.data.Chord;
import imp.data.Note;
import imp.data.Score;

public class NoteLedReharm extends Reharm {

    public NoteLedReharm(Score score) {
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
        
        Note currentNote = score.getPart(0).getCurrentNote(slot);
        String currentNoteName = getNoteNameAtSlot(slot);    
        String prevNote = getNoteNameAtSlot(slot - chordDuration);
        
        if(currentNote.isRest()) {
            setChordMatch(slot);
        }

        if(isStartOfBar(slot)) {
            // if note is in key and not the same as the note at the last chord position...
            if(inKey(currentNoteName) && !currentNoteName.equals(prevNote)) {
                setChordMatch(slot);
            }
            if(!inKey(currentNoteName)) {
                setDiminishedChord(slot);
            }
            if(isChordNumber(slot - (chordDuration * 2), 5)
            && (isChordNumber(slot, 1))) {
                setAlteredChord(slot - chordDuration, 5);
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
            if(inKey(currentNoteName)) {
                Chord prevChord = score.getChordProg().getPrevChord(slot);
                if(prevChord != null) {
                    String prevChordName = prevChord.getName();
                    if(isChordToneOf(currentNoteName, prevChordName)) {
                        Random random = new Random();
                        if(random.nextInt(2) == 0) {
                            setTritoneSub(slot - chordDuration, slot);
                        } else {
                            score.getChordProg().delUnit(slot);
                        }
                    } 
                    if(!isChordToneOf(currentNoteName, prevChordName)) {
                        setHarmonicSub(slot);
                    }
                } 
                if(prevChord == null) {
                    setHarmonicSub(slot);
                }
            } 
            if(!inKey(currentNoteName)) {
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
