package logiikka;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Seura-luokka, joka huolehtii seuran j�senist�st�. V�liluokka Jasenet-luokkaan. Vastuualueet alla:
 * Hoitaa luokkien yhdistelyn
 * Huoltehtii Jasenet- ja Joukkueet-luokkien v�lisest� yhteisty�st� ja v�litt�� n�ilt� tietoja pyydett�ess�
 * Lukee ja kirjoittaa Seuran tiedoston pyyt�m�ll� avustajilta apua
 * 
 * Avustajaluokat: Jasen, Jasenet, Joukkue, Joukkueet, JasenJaJoukkue, JasenetJaJoukkueet
 * 
 * TODO: joitain (luokka)testejä
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class Seura {

    private Jasenet jasenet = new Jasenet();
    private Joukkueet joukkueet = new Joukkueet();
    private JasenetJaJoukkueet relaatiot = new JasenetJaJoukkueet();
    
    
    /** metodi jäsenmäärän palauttamiselle seurasta käsin
     * @return jäsenten määrä seurassa
     */
    public int getJasenia() { return jasenet.getLkm(); }
    
    
    /** Metodi joukkuemäärän palauttamsielle seurasta käsin
     * @return joukkudeiden määrä seurassa
     */
    public int getJoukkueita() { return joukkueet.getLkm(); }
    
    
    /** Metodi identtisten relaatioiden löytämiseen seurasta käsin
     * @param relaatio etsittävä relaatio
     * @return 'true', jos identtinen relaatio olemassa, 'false' muuten
     */
    public boolean onkoOlemassa(JasenJaJoukkue relaatio) { return relaatiot.onkoOlemassa(relaatio); }
    
    
    /** Metodi jäsenen joukkue -relaatioiden etsimiseen
     * @param jasenId jäsen-id
     * @return lista relaatioista
     */
    public List<JasenJaJoukkue> etsiJasenenJoukkueet(int jasenId) { return relaatiot.etsiJasenenJoukkueet(jasenId); }
    
    
    /** Metodi joukkueen jäsen -relaation etsimiseen seurasta käsin
     * @param joukkueId joukkue-id
     * @return lista relaatioista
     */
    public List<JasenJaJoukkue> etsiJoukkueenJasenet(int joukkueId) { return relaatiot.etsiJoukkeenJasenet(joukkueId); }
    
    
    /** metodin jäsenen antamiselle taulukon indeksistä i seurasta käsin
     * @param i indeksi
     * @return jäsen
     * @throws IndexOutOfBoundsException jos i liian iso
     * @example
     * <pre name="test">
     * Jasen jake = new Jasen();
     * Seura seura = new Seura();
     * seura.lisaa(jake);
     * seura.annaJasen(0) === jake;
     * </pre>
     */
    public Jasen annaJasen(int i) throws IndexOutOfBoundsException { return jasenet.anna(i); }
    
    
    /** metodi joukkueen antamiselle tietystä indeksistä i seurasta käsin
     * @param i indeksi
     * @return joukkue
     * @throws IndexOutOfBoundsException jos i liian iso
     * @example
     * <pre name="test">
     * Joukkue j = new Joukkue();
     * Seura seura = new Seura();
     * seura.lisaa(j);
     * seura.annaJoukkue(0) === j;
     * </pre>
     */
    public Joukkue annaJoukkue(int i) throws IndexOutOfBoundsException { return joukkueet.anna(i); }
    
    
    /** Metodi joukkueen etsimiselle id:n avulla seurasta k�sin
     * @param joukkueId joukkue-id
     * @return lista jäsenen joukkueista
     */
    public Joukkue etsiJoukkue(int joukkueId) { return joukkueet.etsiJoukkue(joukkueId); }
    
    
    /** Metodi jäsenen etsimiselle id:n avulla seurasta k�sin
     * @param jasenId jäsen-id
     * @return jäsen
     */
    public Jasen etsiJasen(int jasenId) { return jasenet.etsiJasen(jasenId); }
        
    
    /** Metodi jäsenen lisäykselle seurasta k�sin
     * @param jasen lisättävä jäsen
     * @example
     * <pre name="test">
     * Jasen jake = new Jasen();
     * Seura seura = new Seura();
     * seura.lisaa(jake);
     * seura.annaJasen(0) === jake;
     * </pre>
     */
    public void lisaa(Jasen jasen) { jasenet.lisaa(jasen); }
    
    
    /** Metodi joukkuueen lisäykselle seurasta käsin
     * @param joukkue lisättävä joukkue
     * Joukkue apinat = new Joukkue();
     * Seura klubi = new Seura();
     * seura.lisaa(apinat);
     * seura.annaJoukkue(0) === apinat;
     */
    public void lisaa(Joukkue joukkue) { joukkueet.lisaa(joukkue); }
    
    
    /** Metodi relaation lisäämiselle seurasta käsin
     * @param r lisättävä relaatio
     */
    public void lisaa(JasenJaJoukkue r) { relaatiot.lisaa(r); }
    
    
    /** jäsenen poisto seurasta käsin
     * @param j poistettava jäsen
     */
    public void poista(Jasen j) { 
        jasenet.poista(j); 
        
        //jäsenen relaatioiden poisto:
        List<JasenJaJoukkue> poistettavat = relaatiot.etsiJasenenJoukkueet(j.getId());
        
        for ( int i = 0; i < relaatiot.getLkm(); i++ ) {
            
            for ( int i2 = 0; i2 < poistettavat.size(); i2++ ) {
                if ( relaatiot.anna(i).equals(poistettavat.get(i2)) ) relaatiot.poista(relaatiot.anna(i));
            }
        }
    }
    
    
    /** Joukkueen poisto seurasta käsin
     * @param j joukkue
     */
    public void poista(Joukkue j) { joukkueet.poista(j); }
    
    
    /** Relaation poisto seurasta käsin
     * @param r relaatio
     */
    public void poista(JasenJaJoukkue r) { relaatiot.poista(r); }
    
    
    /** Metodi luo joukkueen ja jäsenen relaation seurasta käsin
     * @param jasenId jäsen id
     * @param joukkueId joukkue id
     * @return luotu relaatio
     * Testattu annaJasenenJoukkueet()-metodissa toimivaksi
     */
    public JasenJaJoukkue luoRelaatio(int jasenId, int joukkueId) {
        JasenJaJoukkue rel = new JasenJaJoukkue(jasenId, joukkueId);
        return rel;
    }    
    
    

    /** Antaa tietyn jäsenen kaikki joukkueet merkkijonona
     * @param jäs jäsen
     * @return jäsenen joukkueet merkkijonona
     * @example
     * <pre name="test">
     * Seura seura = new Seura();
     * Jasen pekka = new Jasen();
     * Joukkue apinat = new Joukkue();
     * pekka.rekisteroi(); apinat.rekisteroi(); apinat.setNimi("apinat");
     * seura.lisaa(pekka);
     * seura.lisaa(apinat);
     * pekka.getId() === 1;
     * apinat.getId() === 1;
     * JasenJaJoukkue r = seura.luoRelaatio(pekka.getId(), apinat.getId());
     * seura.lisaa(r);
     * seura.annaJasenenJoukkueet(pekka) === "apinat";
     * </pre>
     */
    public String annaJasenenJoukkueet(Jasen jäs) {
        
        StringBuilder tiimit = new StringBuilder();
        
        List<JasenJaJoukkue> rt = etsiJasenenJoukkueet(jäs.getId());
        
        List<Joukkue> jt = new ArrayList<Joukkue>();
        
        for ( JasenJaJoukkue r : rt ) {
            jt.add(etsiJoukkue(r.getJoukkueId()));
        }
        
        
        String erotin = "";
        for ( Joukkue j : jt ) {
            tiimit.append(erotin);
            tiimit.append(j.getNimi());
            erotin = ", ";
        }
        
        return tiimit.toString();
    }
    
    
    /** jäsenen etsiminen nimen perusteella seurasta käsin
     * @param nimi nimi
     * @return lista jäsenistä
     */
    public List<Jasen> haeJasenet(String nimi) { return jasenet.haeJasenet(nimi); }
    
    
    /** Jäsenen korvaaminen tai lisääminen seurasta käsin
     * @param jasen jäsen
     */
    public void korvaaTaiLisaa(Jasen jasen) { jasenet.korvaaTaiLisaa(jasen); }
    
    
    /** Asettaa tiedostonjen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedostojenNimet(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemisto = "";
        if ( !nimi.isEmpty() ) hakemisto = nimi + "/";
        jasenet.setTiedostonOletusNimi(hakemisto + "jasenet");
        joukkueet.setTiedostonOletusNimi(hakemisto + "joukkueet");
        relaatiot.setTiedostonOletusNimi(hakemisto + "relaatiot");
    }

        
    /** Lukee seuran tiedot tiedostosta. 
     * @throws IOException jos jokin meni vikaan
     */
    public void lueTiedosto() throws IOException {
        jasenet = new Jasenet();
        joukkueet = new Joukkueet();
        relaatiot = new JasenetJaJoukkueet();
        
        jasenet.lueTiedosto("jasenet"); joukkueet.lueTiedosto("joukkueet"); relaatiot.lueTiedosto("relaatiot");
        
    }
    
    
    /**
     * Kirjoittaa seuran tiedot tiedostoon
     */
    public void kirjoitaTiedosto() {
        jasenet.kirjoitaTiedosto();
        joukkueet.kirjoitaTiedosto();
        relaatiot.kirjoitaTiedosto();
    }
       
    
    /** Testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        
        Seura ols = new Seura();
        
        Jasen peksi = new Jasen();  peksi.rekisteroi(); peksi.taytaTiedot();
        Jasen late = new Jasen();   late.rekisteroi();  late.taytaTiedot();
        
        ols.lisaa(peksi); ols.lisaa(late);
        
        System.out.println("===================Seuran testi=======================\n");
        
//        for (int i = 0; i < ols.getJasenia(); i++) {
//            Jasen jasen = ols.annaJasen(i); 
//            jasen.taytaTiedot();
//            System.out.println("J�sen paikassa " + i + ":");
//            jasen.tulosta(System.out);
//        }
        
        ols.poista(peksi);

    }

}
