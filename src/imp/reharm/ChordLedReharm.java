package imp.reharm;

import java.util.Random;

import imp.data.Chord;
import imp.data.Score;

/**
 * ChordLedReharm is the third iteration of Reharm. It is listed as 'Chord Led' 
 * in the pull down menu next to the Reharm button in the GUI. While NoteLedReharm
 * only uses the melodic content of the score to inform its note choices, this 
 * iteration loops through the score again after the chords have been set, so that 
 * it can check the chords against each other and adjust certain chords for maximum
 * harmonic cohesion.
 * @author Jim Ashford
 */
public class ChordLedReharm extends Reharm {

    /**
     * The constructor for ChordLedReharm.
     * @param score The Score instance to associate with this ChordLedReharm.
     */
    public ChordLedReharm(Score score) {
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
        // Chords can be added at the end and in the middle of each bar.
        return score.getSlotsPerMeasure() / 2;
    }

    @Override
    public void implementChordChoice(int slot) {
        // The name of the note at the slot that is currently being analysed.
        String currentNoteName = getNoteNameAtSlot(slot);
        
        // Set a chord that matches the most common note in the slot.
        setChordMatch(slot);
        
        // if the note at the start of the current slot is not in key...
        if(!inKey(currentNoteName)) {
            setDiminishedChord(slot);
        }
    }

    @Override
    public void adjustChordChoice(int slot) {
        // Useful positions to check:
        int prevSlot = slot - chordDuration;
        int nextSlot = slot + chordDuration;
        int oneBarBehind = slot - (chordDuration * 2);
        int oneBarAhead = slot + (chordDuration * 2);

        // if current slot is at the start of the bar...
        if(isStartOfBar(slot)) {
            // complete any incomplete II V I progs starting from this slot
            if(isChordNumber(nextSlot, 5) 
            && isChordNumber(oneBarAhead, 1)) {
                setDiatonicChord(slot, 2);
                lock(slot);
                lock(nextSlot);
                lock(oneBarAhead);
            }
            if(isChordNumber(slot, 2) 
            && isChordNumber(oneBarAhead, 1)) {
                setDiatonicChord(nextSlot, 5);
                lock(slot);
                lock(nextSlot);
                lock(oneBarAhead);
            }
            if(isChordNumber(slot, 2) 
            && isChordNumber(nextSlot, 5)) {
                setDiatonicChord(oneBarAhead, 1);
                lock(slot);
                lock(nextSlot);
                lock(oneBarAhead);
            }
            
            // if the current slot is the end of a II V I
            if(isChordNumber(oneBarBehind, 2)
            && isChordNumber(prevSlot, 5)
            && isChordNumber(slot, 1)) {
                // Delete chord in last half bar if this is the last bar
                if(isLastBar(slot)) {
                    unlock(nextSlot);
                    score.getChordProg().delUnit(nextSlot);
                    lock(nextSlot);
                // if the current slot is bar 7 of any 8-bar phrase...
                } else if((slot + (score.getSlotsPerMeasure() * 2)) % (score.getSlotsPerMeasure() * 8) == 0) {
                    // Ending turnaround for the last 2 bars of head
                    if(isLastBar(slot + score.getSlotsPerMeasure())) {
                        setDiatonicChord(nextSlot, 4);
                        setDiatonicChord(oneBarAhead, 5);
                        setDiatonicChord(oneBarAhead + chordDuration, 1);
                        lock(nextSlot);
                        lock(oneBarAhead);
                        lock(oneBarAhead + chordDuration);
                    // I VI II V turnaround if not at the end of the song.
                    } else {
                    setDiatonicChord(nextSlot, 6);
                    setDiatonicChord(oneBarAhead, 2);
                    setDiatonicChord(oneBarAhead + chordDuration, 5);
                    lock(nextSlot);
                    lock(oneBarAhead);
                    lock(oneBarAhead + chordDuration);
                    }
                // if the end of a II V I but not end of piece or last 2 bars of 8-bar phrase...
                } else {
                    Random random = new Random();
                    switch(random.nextInt(3)) {
                        case 0:
                            score.getChordProg().delUnit(nextSlot);
                            break;
                        case 1:
                            setDiatonicChord(nextSlot, 4);
                            break;
                        case 2:
                            setAlteredChord(nextSlot, 6);
                    }
                    lock(nextSlot);
                }
                // Get the chord at the current slot.
                Chord thisChord = score.getChordProg().getChord(slot);
                // if most notes in the slot are not in key or the chord in this 
                // slot is not one of the best chord matches for the slot...
                if(thisChord != null 
                && (!majorityInKey(slot) || !getBestChordMatches(slot).contains(getRoot(thisChord.getName())))) {
                    // Unlock the necessary slots...
                    unlock(slot);
                    unlock(prevSlot);
                    unlock(oneBarBehind);
                    unlock(nextSlot);
                    // ...set different chords in them...
                    setDiatonicChord(oneBarBehind, 2);
                    setTritoneSub(oneBarBehind, prevSlot);
                    setAlteredChord(slot, 5);
                    score.getChordProg().delUnit(nextSlot);
                    // ...then lock them again.
                    lock(slot);
                    lock(prevSlot);
                    lock(oneBarBehind);
                    lock(nextSlot);
                }   
            }

            // Complete any incomplete minor II V I progs
            if(isChordNumber(nextSlot, 3) 
            && isChordNumber(oneBarAhead, 6)) {
                setDiatonicChord(slot, 7);
                setAlteredChord(nextSlot, 3);
                lock(slot);
                lock(nextSlot);
                lock(oneBarAhead);
            }
            if(isChordNumber(slot, 7) 
            && isChordNumber(oneBarAhead, 6)) {
                setAlteredChord(nextSlot, 3);
                lock(slot);
                lock(nextSlot);
                lock(oneBarAhead);
            }
            if(isChordNumber(slot, 7) 
            && isChordNumber(nextSlot, 3)) {
                setDiatonicChord(oneBarAhead, 6);
                setAlteredChord(nextSlot, 3);
                lock(slot);
                lock(nextSlot);
                lock(oneBarAhead);
            }

            // If the current slot is the end of a minor II V I
            if(isChordNumber(oneBarBehind, 7)
            && isChordNumber(prevSlot, 3)
            && isChordNumber(slot, 6)) {
                // Remove any chords after the minor II V I if end of song.
                if(isLastBar(slot)) {
                    unlock(nextSlot);
                    score.getChordProg().delUnit(nextSlot);
                    lock(nextSlot);
                // Maybe put a nice final chord after the minor II V I.
                } else {
                    Random random = new Random();
                    switch(random.nextInt(3)) {
                        case 0:
                            score.getChordProg().delUnit(nextSlot);
                            break;
                        case 1:
                            setAlteredChord(nextSlot, 1);
                            break;
                        case 2:
                            setDiatonicChord(nextSlot, 4);
                    }
                    lock(nextSlot);
                }
            }

            // add either an altered dominant or tritone sub between any V I progs
            if(isChordNumber(slot, 5)
            && isChordNumber(oneBarAhead, 1)) {
                Random random = new Random();
                switch(random.nextInt(2)) {
                    case 0:
                        setAlteredChord(nextSlot, 5);
                        break;
                    case 1:
                        setTritoneSub(slot, nextSlot);
                        break;
                }
                lock(slot);
                lock(nextSlot);
                lock(oneBarAhead);
            }
            
            // Get rid of repeated chords
            Chord thisChord = score.getChordProg().getChord(slot);
            Chord nextChord = score.getChordProg().getChord(nextSlot);
            if(thisChord != null && nextChord != null) {
                String thisChordRoot = getRoot(thisChord.getName());
                String nextChordRoot = getRoot(nextChord.getName());
                if(thisChordRoot.equals(nextChordRoot)) {
                    Random random = new Random();
                    switch(random.nextInt(2)) {
                        case 0:
                            setDifferentChordTo(slot, nextSlot);
                            break;
                        case 1:
                            score.getChordProg().delUnit(nextSlot);
                            break;
                    }
                    lock(slot);
                    lock(nextSlot);
                }
            }
        }
    }
}