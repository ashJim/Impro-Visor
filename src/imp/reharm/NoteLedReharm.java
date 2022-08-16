package imp.reharm;

import java.util.Random;

import imp.data.Chord;
import imp.data.Note;
import imp.data.Score;

/**
 * NoteLedReharm is the second iteration of Reharm. It is listed as 'Note Led' 
 * in the pull down menu next to the Reharm button in thg GUI. Following on from 
 * BasicReharm, this version uses the same concept of assigning chords based on 
 * notes in the melody. However, this version handles all 12 notes of the chromatic 
 * scale, more chord choices are included in the chord sets for each note, chords can 
 * be added every half bar and chord implementation is more complex.
 * @author Jim Ashford
 */
public class NoteLedReharm extends Reharm {

    /**
     * The constructor for NoteLedReharm.
     * @param score The Score instance to associate with this NoteLedReharm.
     */
    public NoteLedReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        // Create empty 2d String array for chromatic notes and their corresponding chords...
        String[][] chordSets = chromaticChordSets();
        // ...add the standard chord sets to it...
        initStandardSubstitutions(chordSets);
        // ...then return it in its complete form.
        return chordSets;
    }

    @Override
    public int getChordDuration() {
        // Chords can be added at the and in the middle of each bar.
        return score.getSlotsPerMeasure() / 2;
    }

    @Override
    public void implementChordChoice(int slot) {
        // Some useful variables:
        // The note in the slot that is currently being analysed.
        Note currentNote = score.getPart(0).getCurrentNote(slot);
        // The name of the above note (if there is one).
        String currentNoteName = getNoteNameAtSlot(slot);
        // The name of the note at the previously analysed slot (if there is one).
        String prevNoteName = getNoteNameAtSlot(slot - chordDuration);
        
        // Case 1: There is no note at the slot (regardless of position in bar).
        if(currentNote.isRest()) {
            setChordMatch(slot);
        }
        
        // Case 2: The slot is at the start of the bar.
        if(isStartOfBar(slot)) {
            // if note is in key and not the same as the note at the last chord position...
            if(inKey(currentNoteName) && !currentNoteName.equals(prevNoteName)) {
                setChordMatch(slot);
            }
            // if note is not in key...
            if(!inKey(currentNoteName)) {
                setDiminishedChord(slot);
            }
        }

        // Case 3: The slot is in the middle of a bar.
        if(!isStartOfBar(slot)) {
            // Get the note at the beginning of that bar.
            Note startBarNote = score.getPart(0).getNote(slot - chordDuration);
            // If there is a note at the start of the bar that is in key and lasts the length of the bar...
            if(startBarNote instanceof Note && startBarNote.getRhythmValue() == score.getSlotsPerMeasure()
            && inKey(startBarNote.getPitchClassName())) {
                // ...either set a tritone substitution or do nothing.
                Random random = new Random();
                if(random.nextInt(2) == 0) {
                    setTritoneSub(slot - chordDuration, slot);
                }
            }
            // if the current note is in key...
            if(inKey(currentNoteName)) {
                // Get the previous chord
                Chord prevChord = score.getChordProg().getPrevChord(slot);
                if(prevChord != null) {
                    // Get the name of the previous chord (if there is one)
                    String prevChordName = prevChord.getName();
                    // if the current note is one of the notes of the previous chord...
                    if(isChordToneOf(currentNoteName, prevChordName)) {
                        // ...either set a tritone substitution or leave the slot blank 
                        // (thus continuing the previous chord through this slot).
                        Random random = new Random();
                        if(random.nextInt(2) == 0) {
                            setTritoneSub(slot - chordDuration, slot);
                        } else {
                            score.getChordProg().delUnit(slot);
                        }
                    }
                    // if the current note is not one of the notes from the previous chord...
                    if(!isChordToneOf(currentNoteName, prevChordName)) {
                        // ... set one of the harmonic substitutions paired with the current note.
                        setHarmonicSub(slot);
                    }
                }
                // also set a harmonic substitution from the current note 
                // if there is no chord at the previous slot.
                if(prevChord == null) {
                    setHarmonicSub(slot);
                }
            }
            // set a diminished chord paired with the current note if the 
            // current note is not in key.
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
        // This mode uses only the notes of the melody to inform its chord choices,
        // so there is no implementation of adjustChordChoice.
    }
}