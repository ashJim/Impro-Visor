package imp.reharm;

import java.util.Random;

import imp.data.Chord;
import imp.data.Score;

public class ChordLedReharm extends Reharm {

    public ChordLedReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        // Create empty 2d String array for chromatic notes and their corresponding chords...
        String[][] chordSets = chromaticChordSets();
        // ...add stuff to it...
        initStandardSubstitutions(chordSets);
        // ...then return it in its complete form.
        return chordSets;
    }

    @Override
    public int getChordDuration() {
        // The interval at which to analyse the score and maybe add stuff.
        return score.getSlotsPerMeasure() / 2;
    }

    @Override
    public void implementChordChoice(int slot) {

        String currentNoteName = getNoteNameAtSlot(slot);

        setChordMatch(slot);
        
        if(!inKey(currentNoteName)) {
            setDiminishedChord(slot);
        }
    }

    @Override
    public void adjustChordChoice(int slot) {
        // Useful positions to check.
        int prevSlot = slot - chordDuration;
        int nextSlot = slot + chordDuration;
        int oneBarBehind = slot - (chordDuration * 2);
        int oneBarAhead = slot + (chordDuration * 2);

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
            
            // If the current slot is the end of a II V I
            if(isChordNumber(oneBarBehind, 2)
            && isChordNumber(prevSlot, 5)
            && isChordNumber(slot, 1)) {
                // Delete chord in last half bar if this is the last bar
                if(isLastBar(slot)) {
                    unlock(nextSlot);
                    score.getChordProg().delUnit(nextSlot);
                    lock(nextSlot);
                // I VI II V turnaround if the current slot is bar 7 of any 8-bar phrase
                } else if((slot + (score.getSlotsPerMeasure() * 2)) % (score.getSlotsPerMeasure() * 8) == 0) {
                    if(isLastBar(slot + score.getSlotsPerMeasure())) {
                        setDiatonicChord(nextSlot, 4);
                        setDiatonicChord(oneBarAhead, 5);
                        setDiatonicChord(oneBarAhead + chordDuration, 1);
                        lock(nextSlot);
                        lock(oneBarAhead);
                        lock(oneBarAhead + chordDuration);
                    // Ending turnaround for the last 2 bars of head
                    } else {
                    setDiatonicChord(nextSlot, 6);
                    setDiatonicChord(oneBarAhead, 2);
                    setDiatonicChord(oneBarAhead + chordDuration, 5);
                    lock(nextSlot);
                    lock(oneBarAhead);
                    lock(oneBarAhead + chordDuration);
                    }
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
                Chord thisChord = score.getChordProg().getChord(slot);
                if(thisChord != null 
                && (!majorityInKey(slot) || !getBestChordMatches(slot).contains(getRoot(thisChord.getName())))) {
                    // unlock(slot);
                    // unlock(prevSlot);
                    // unlock(oneBarBehind);
                    // unlock(nextSlot);
                    setDiatonicChord(oneBarBehind, 2);
                    setTritoneSub(oneBarBehind, prevSlot);
                    setAlteredChord(slot, 5);
                    score.getChordProg().delUnit(nextSlot);
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
                // Sort out the chord that follows the minor II V I
                if(isLastBar(slot)) {
                    unlock(nextSlot);
                    score.getChordProg().delUnit(nextSlot);
                    lock(nextSlot);
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