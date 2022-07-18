package imp.reharm;

import imp.data.Score;

public class BasicReharm extends Reharm {

    public BasicReharm(Score score) {
        super(score);
    }

    @Override
    public String[][] generateSubstitutions(String[] chords) {
        String[][] chordSets = new String[7][1];
        for(int i = 0; i < chordSets.length; i++) {
            chordSets[i][0] = chords[i];
        }
        return chordSets;
    }

    @Override
    public void setChordDuration() {
        setChordDuration(score.getSlotsPerMeasure());
    }

        @Override
    public void implementChordChoice(int chordSlot) {
        setRootChordFromNote(chordSlot);
    }
}
