package imp.reharm;

import imp.data.Score;

public class BasicReharm extends Reharm {

    public BasicReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        String[][] chordSets = diatonicChordSets();
        addDiatonicChords(chordSets);
        return chordSets;
    }

    @Override
    public void setChordDuration() {
        setChordDuration(score.getSlotsPerMeasure());
    }

        @Override
    public void implementChordChoice(int chordSlot) {
        setRootChord(chordSlot);
    }
}
