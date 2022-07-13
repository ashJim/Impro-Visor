package imp.data;

import java.util.HashMap;

public class BasicReharm extends Reharm {

    public BasicReharm(Score score) {
        super(score);
    }

    @Override
    protected HashMap<String, String[]> generateCChords() {
        HashMap<String, String[]> cChords = new HashMap<>();

        String[] Array1 = {"C"};
        String[] Array2 = {"Dm"};
        String[] Array3 = {"Em"};
        String[] Array4 = {"F"};
        String[] Array5 = {"G"};
        String[] Array6 = {"Am"};
        String[] Array7 = {"Bm7b5"};

        cChords.put("c", Array1);
        cChords.put("d", Array2);
        cChords.put("e", Array3);
        cChords.put("f", Array4);
        cChords.put("g", Array5);
        cChords.put("a", Array6);
        cChords.put("b", Array7);

        return cChords;
    }

    @Override
    protected HashMap<String, String[]> generateFSharpChords() {
        HashMap<String, String[]> fSharpChords = new HashMap<>();

        String[] Array1 = {"F#"};
        String[] Array2 = {"G#m"};
        String[] Array3 = {"A#m"};
        String[] Array4 = {"B"};
        String[] Array5 = {"C#"};
        String[] Array6 = {"D#m"};
        String[] Array7 = {"E#m7b5"};

        fSharpChords.put("f#", Array1);
        fSharpChords.put("g#", Array2);
        fSharpChords.put("a#", Array3);
        fSharpChords.put("b", Array4);
        fSharpChords.put("c#", Array5);
        fSharpChords.put("d#", Array6);
        fSharpChords.put("e#", Array7);

        return fSharpChords;
    }

    @Override
    protected HashMap<String, String[]> generateBChords() {
        HashMap<String, String[]> bChords = new HashMap<>();

        String[] Array1 = {"B"};
        String[] Array2 = {"C#m"};
        String[] Array3 = {"D#m"};
        String[] Array4 = {"E"};
        String[] Array5 = {"F#"};
        String[] Array6 = {"G#m"};
        String[] Array7 = {"A#m7b5"};

        bChords.put("b", Array1);
        bChords.put("c#", Array2);
        bChords.put("d#", Array3);
        bChords.put("e", Array4);
        bChords.put("f#", Array5);
        bChords.put("g#", Array6);
        bChords.put("a#", Array7);

        return bChords;
    }

    @Override
    protected HashMap<String, String[]> generateEChords() {
        HashMap<String, String[]> eChords = new HashMap<>();

        String[] Array1 = {"E"};
        String[] Array2 = {"F#m"};
        String[] Array3 = {"G#m"};
        String[] Array4 = {"A"};
        String[] Array5 = {"B"};
        String[] Array6 = {"C#m"};
        String[] Array7 = {"D#m7b5"};

        eChords.put("e", Array1);
        eChords.put("f#", Array2);
        eChords.put("g#", Array3);
        eChords.put("a", Array4);
        eChords.put("b", Array5);
        eChords.put("c#", Array6);
        eChords.put("d#", Array7);

        return eChords;
    }

    @Override
    protected HashMap<String, String[]> generateAChords() {
        HashMap<String, String[]> aChords = new HashMap<>();

        String[] Array1 = {"A"};
        String[] Array2 = {"Bm"};
        String[] Array3 = {"C#m"};
        String[] Array4 = {"D"};
        String[] Array5 = {"E"};
        String[] Array6 = {"F#m"};
        String[] Array7 = {"G#m7b5"};

        aChords.put("a", Array1);
        aChords.put("b", Array2);
        aChords.put("c#", Array3);
        aChords.put("d", Array4);
        aChords.put("e", Array5);
        aChords.put("f#", Array6);
        aChords.put("g#", Array7);

        return aChords;
    }

    @Override
    protected HashMap<String, String[]> generateDChords() {
        HashMap<String, String[]> dChords = new HashMap<>();

        String[] Array1 = {"D"};
        String[] Array2 = {"Em"};
        String[] Array3 = {"F#m"};
        String[] Array4 = {"G"};
        String[] Array5 = {"A"};
        String[] Array6 = {"Bm"};
        String[] Array7 = {"C#m7b5"};

        dChords.put("d", Array1);
        dChords.put("e", Array2);
        dChords.put("f#", Array3);
        dChords.put("g", Array4);
        dChords.put("a", Array5);
        dChords.put("b", Array6);
        dChords.put("c#", Array7);

        return dChords;
    }

    @Override
    protected HashMap<String, String[]> generateGChords() {
        HashMap<String, String[]> gChords = new HashMap<>();

        String[] Array1 = {"G"};
        String[] Array2 = {"Am"};
        String[] Array3 = {"Bm"};
        String[] Array4 = {"C"};
        String[] Array5 = {"D"};
        String[] Array6 = {"Em"};
        String[] Array7 = {"F#m7b5"};

        gChords.put("g", Array1);
        gChords.put("a", Array2);
        gChords.put("b", Array3);
        gChords.put("c", Array4);
        gChords.put("d", Array5);
        gChords.put("e", Array6);
        gChords.put("f#", Array7);

        return gChords;
    }

    @Override
    protected HashMap<String, String[]> generateFChords() {
        HashMap<String, String[]> fChords = new HashMap<>();

        String[] Array1 = {"F"};
        String[] Array2 = {"Gm"};
        String[] Array3 = {"A#"};
        String[] Array4 = {"Bb"};
        String[] Array5 = {"C"};
        String[] Array6 = {"Dm"};
        String[] Array7 = {"Em7b5"};

        fChords.put("f", Array1);
        fChords.put("g", Array2);
        fChords.put("a", Array3);
        fChords.put("bb", Array4);
        fChords.put("c", Array5);
        fChords.put("d", Array6);
        fChords.put("e", Array7);

        return fChords;
    }

    @Override
    protected HashMap<String, String[]> generateBFlatChords() {
        HashMap<String, String[]> bFlatChords = new HashMap<>();

        String[] Array1 = {"Bb"};
        String[] Array2 = {"Cm"};
        String[] Array3 = {"Dm"};
        String[] Array4 = {"Eb"};
        String[] Array5 = {"F"};
        String[] Array6 = {"Gm"};
        String[] Array7 = {"Am7b5"};

        bFlatChords.put("bb", Array1);
        bFlatChords.put("c", Array2);
        bFlatChords.put("d", Array3);
        bFlatChords.put("eb", Array4);
        bFlatChords.put("f", Array5);
        bFlatChords.put("g", Array6);
        bFlatChords.put("a", Array7);

        return bFlatChords;
    }

    @Override
    protected HashMap<String, String[]> generateEFlatChords() {
        HashMap<String, String[]> eFlatChords = new HashMap<>();

        String[] Array1 = {"Eb"};
        String[] Array2 = {"Fm"};
        String[] Array3 = {"Gm"};
        String[] Array4 = {"Ab"};
        String[] Array5 = {"Bb"};
        String[] Array6 = {"Cm"};
        String[] Array7 = {"Dm7b5"};

        eFlatChords.put("eb", Array1);
        eFlatChords.put("f", Array2);
        eFlatChords.put("g", Array3);
        eFlatChords.put("ab", Array4);
        eFlatChords.put("bb", Array5);
        eFlatChords.put("c", Array6);
        eFlatChords.put("d", Array7);

        return eFlatChords;
    }

    @Override
    protected HashMap<String, String[]> generateAFlatChords() {
        HashMap<String, String[]> aFlatChords = new HashMap<>();

        String[] Array1 = {"Ab"};
        String[] Array2 = {"Bbm"};
        String[] Array3 = {"Cm"};
        String[] Array4 = {"Db"};
        String[] Array5 = {"Eb"};
        String[] Array6 = {"Fm"};
        String[] Array7 = {"Gm7b5"};

        aFlatChords.put("ab", Array1);
        aFlatChords.put("bb", Array2);
        aFlatChords.put("c", Array3);
        aFlatChords.put("db", Array4);
        aFlatChords.put("eb", Array5);
        aFlatChords.put("f", Array6);
        aFlatChords.put("g", Array7);

        return aFlatChords;
    }

    @Override
    protected HashMap<String, String[]> generateDFlatChords() {
        HashMap<String, String[]> dFlatChords = new HashMap<>();

        String[] Array1 = {"Db"};
        String[] Array2 = {"Ebm"};
        String[] Array3 = {"Fm"};
        String[] Array4 = {"Gb"};
        String[] Array5 = {"Ab"};
        String[] Array6 = {"Bbm"};
        String[] Array7 = {"Cm7b5"};

        dFlatChords.put("db", Array1);
        dFlatChords.put("eb", Array2);
        dFlatChords.put("f", Array3);
        dFlatChords.put("gb", Array4);
        dFlatChords.put("ab", Array5);
        dFlatChords.put("bb", Array6);
        dFlatChords.put("c", Array7);

        return dFlatChords;
    }

    @Override
    protected HashMap<String, String[]> generateGFlatChords() {
        HashMap<String, String[]> gFlatChords = new HashMap<>();

        String[] Array1 = {"Gb"};
        String[] Array2 = {"Abm"};
        String[] Array3 = {"Bbm"};
        String[] Array4 = {"Cb"};
        String[] Array5 = {"Db"};
        String[] Array6 = {"Ebm"};
        String[] Array7 = {"Fm7b5"};

        gFlatChords.put("gb", Array1);
        gFlatChords.put("ab", Array2);
        gFlatChords.put("bb", Array3);
        gFlatChords.put("cb", Array4);
        gFlatChords.put("db", Array5);
        gFlatChords.put("eb", Array6);
        gFlatChords.put("f", Array7);

        return gFlatChords;
    }

}
