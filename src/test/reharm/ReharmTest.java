package test.reharm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import imp.data.Score;
import imp.reharm.BasicReharm;
import imp.reharm.Reharm;
import imp.util.Preferences;

public class ReharmTest {

    @Test
    public void eShouldBeInCMajor() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(0);
        Reharm reharm = new BasicReharm(score);
        assertTrue(reharm.isChordToneOf("e", "CM"));
    }

    @Test
    public void fShouldBeInDMinor() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(0);
        Reharm reharm = new BasicReharm(score);
        assertTrue(reharm.isChordToneOf("f", "Dm"));
    }

    @Test
    public void eShouldNotBeInCMinor() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(-3); // Must be set to a key where the C chord is minor (such as key of Eb)
        Reharm reharm = new BasicReharm(score);
        assertFalse(reharm.isChordToneOf("e", "Cm"));
    }

    @Test
    public void rootOfEMajShouldBeE() {
        Preferences.loadPreferences();
        Score score = new Score();
        Reharm reharm = new BasicReharm(score);
        assertEquals("e", reharm.getRoot("EM"));
    }

    @Test
    public void rootOfFSharpDominant7ShouldBeF() {
        Preferences.loadPreferences();
        Score score = new Score();
        Reharm reharm = new BasicReharm(score);
        assertEquals("f#", reharm.getRoot("F#7"));
    }

    @Test
    public void rootOfDFlatMinor7ShouldBeDFlat() {
        Preferences.loadPreferences();
        Score score = new Score();
        Reharm reharm = new BasicReharm(score);
        assertEquals("db", reharm.getRoot("Dbm7"));
    }

    @Test
    public void gIsInKeyOfC() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(0);
        Reharm reharm = new BasicReharm(score);
        assertTrue(reharm.inKey("g"));
    }

    @Test
    public void gIsNotInKeyOfA() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(3);
        Reharm reharm = new BasicReharm(score);
        assertFalse(reharm.inKey("g"));
    }

    @Test
    public void fSharpIsInKeyOfG() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(1);
        Reharm reharm = new BasicReharm(score);
        assertTrue(reharm.inKey("f#"));
    }

    @Test
    public void fSharpIsNotInKeyOfC() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(0);
        Reharm reharm = new BasicReharm(score);
        assertFalse(reharm.inKey("f#"));
    }

    @Test
    public void AFlatIsInKeyOfEFlat() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(-3);
        Reharm reharm = new BasicReharm(score);
        assertTrue(reharm.inKey("ab"));
    }

    @Test
    public void AFlatIsNotInKeyOfF() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(-1);
        Reharm reharm = new BasicReharm(score);
        assertFalse(reharm.inKey("ab"));
    }

    @Test
    public void diatonicChordSetsShouldBeLengthSeven() {
        Preferences.loadPreferences();
        Score score = new Score();
        Reharm reharm = new BasicReharm(score);
        assertTrue(reharm.diatonicChordSets().length == 7);
    }

    @Test
    public void chromaticChordSetsShouldBeLengthTwelve() {
        Preferences.loadPreferences();
        Score score = new Score();
        Reharm reharm = new BasicReharm(score);
        assertTrue(reharm.chromaticChordSets().length == 12);
    }

    @Test
    public void chordTwoInCShouldBeDMinor() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(0);
        Reharm reharm = new BasicReharm(score);
        String[][] diatonicChordSets = reharm.diatonicChordSets();
        String[][] chromaticChordSets = reharm.chromaticChordSets();
        reharm.addDiatonicChords(diatonicChordSets);
        reharm.addDiatonicChords(chromaticChordSets);
        assertEquals("Dm", diatonicChordSets[1][0]);
        assertEquals("Dm", chromaticChordSets[2][0]);
    }

    @Test
    public void chordFiveInFShouldBeC() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(-1);
        Reharm reharm = new BasicReharm(score);
        String[][] diatonicChordSets = reharm.diatonicChordSets();
        String[][] chromaticChordSets = reharm.chromaticChordSets();
        reharm.addDiatonicChords(diatonicChordSets);
        reharm.addDiatonicChords(chromaticChordSets);
        assertEquals("C", diatonicChordSets[4][0]);
        assertEquals("C", chromaticChordSets[7][0]);
    }

    @Test
    public void outsidePositionOneInCShouldBeCSharpDim() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(0);
        Reharm reharm = new BasicReharm(score);
        String[][] chordSets = reharm.chromaticChordSets();
        reharm.addDimChords(chordSets);
        assertEquals("C#dim7", chordSets[1][0]);
    }

    @Test
    public void tritoneSubOfEMajInEShouldBeASharp() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(4);
        Reharm reharm = new BasicReharm(score);
        String[][] diatonicChordSets = reharm.diatonicChordSets();
        String[][] chromaticChordSets = reharm.chromaticChordSets();
        reharm.addTritoneSubs(diatonicChordSets);
        reharm.addTritoneSubs(chromaticChordSets);
        assertEquals("A#", diatonicChordSets[0][1]);
        assertEquals("A#", chromaticChordSets[0][1]);
    }

    @Test
    public void harmonicSubOfAInBFlatShouldBeF() {
        Preferences.loadPreferences();
        Score score = new Score();
        score.setKeySignature(-2);
        Reharm reharm = new BasicReharm(score);
        String[][] diatonicChordSets = reharm.diatonicChordSets();
        String[][] chromaticChordSets = reharm.chromaticChordSets();
        reharm.addHarmonicSubs(diatonicChordSets);
        reharm.addHarmonicSubs(chromaticChordSets);
        assertEquals("F", diatonicChordSets[6][2]);
        assertEquals("F", chromaticChordSets[11][2]);
    }

}