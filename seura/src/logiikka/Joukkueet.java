package logiikka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Monikkouokka joukkueiden käsittelyyn. Vastuualueet alla:
 * Osaa hakea joukkueen id:n perusteella
 * Osaa lisätä joukkueen
 * Osaa poistaa joukkueen
 * Lukee ja kirjoittaa joukkuet-tiedostoon
 * 
 * Avustajaluokat: Joukkue
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class Joukkueet {

    private int lkm = 0;
    private List<Joukkue> alkiot = new ArrayList<Joukkue>();
    private boolean muutettu = false;
    private String tiedostonOletusNimi = "joukkueet";    
    
    //SQL:
    private static Joukkue apuJoukkue = new Joukkue();
    private Kanta kanta;
    
    
    /**
     * Oletuskonstruktori
     */
    public Joukkueet() {/**/}
    
    
    /** Metodi joukkueen lisäämiselle
     * @param j joukkue, mikä lisätään
     * @example
     * <pre name="test">
     * Joukkue tiimi = new Joukkue();
     * Joukkueet tiimit = new Joukkueet();
     * tiimit.lisaa(tiimi);
     * tiimit.getLkm() === 1;
     * </pre>
     */
    public void lisaa(Joukkue j) { alkiot.add(j); lkm++; muutettu = true; }
    
    
    /** poistometodin raakile
     * @param j poistettava joukkue
     * @example
     * <pre name="test">
     * Joukkueet t = new Joukkueet();
     * Joukkue j1 = new Joukkue();
     * Joukkue j2 = new Joukkue();
     * j1.taytaTiedot(); j1.rekisteroi();
     * j2.taytaTiedot(); j2.rekisteroi();
     * t.lisaa(j1); t.lisaa(j2);
     * t.poista(j1);
     * t.anna(0).getId() === 2;
     * </pre>
     */
    public void poista(Joukkue j) { alkiot.remove(j); lkm--; muutettu = true;}
    
    
    /** Metodi joukkueiden lukumäärän antamiselle
     * @return joukkueiden lukumäärä
     * @example
     * <pre name="test">
     * Joukkue tiimi = new Joukkue();
     * Joukkueet tiimit = new Joukkueet();
     * tiimit.lisaa(tiimi);
     * tiimit.getLkm() === 1;
     * </pre>
     */
    public int getLkm() { return lkm; }
    
    
    /**
     * @return tiedoston oletusnimi
     */
    public String getTiedostonOletusNimi() { return tiedostonOletusNimi + ".dat"; }
    
    
    /**
     * @return varmuuskopiotiedoston nimi
     */
    public String getVkNimi() { return tiedostonOletusNimi + ".bak"; }
    
    
    /** asetetaan uusi tiedoston oletusnimi
     * @param nimi asetettava nimi
     */
    public void setTiedostonOletusNimi(String nimi) { tiedostonOletusNimi = nimi; }
    
    
    /** Antaa joukkueen tietyst� listan indeksist�
     * @param i indeksi
     * @return joukkue
     * @throws IndexOutOfBoundsException jos i liian iso
     * Joukkue tiimi = new Joukkue();
     * Joukkueet tiimit = new Joukkueet();
     * tiimit.lisaa(tiimi);
     * tiimit.anna(0) === tiimi;
     */
    public Joukkue anna(int i) throws IndexOutOfBoundsException { 
        if ( i < 0 || lkm <= i ) throw new IndexOutOfBoundsException( "Laiton indeksi" + i );
        return alkiot.get(i); 
    }
    
    

    /** Metodi joukkueen etsimiseen id:n perusteella
     * @param joukkueId joukkueen id
     * @return joukkue
     */
    public Joukkue etsiJoukkue(int joukkueId) { 
        
        for ( Joukkue tiimi : alkiot ) {
            if ( tiimi.getId() == joukkueId ) return tiimi;
        }
        
        return null;
    }
    
    
    /**
     * Metodi jäsenen tietotiedoston kirjoittamiselle.
     */
    public void kirjoitaTiedosto() {
        
        if ( !muutettu ) return;
        
        File vKopio = new File(getVkNimi());
        File tied = new File(getTiedostonOletusNimi());
        vKopio.delete();
        tied.renameTo(vKopio);
        
        String tiedNimi = getTiedostonOletusNimi();
        try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {
            
            for ( Joukkue j : alkiot ) { fo.println(j.toString()); }
            
        } catch (FileNotFoundException ex) { System.err.println("tiedosto ei aukea" + ex.getMessage()); }
        
        
        muutettu = false;
    }
    
    
    /**
     * Metodi tiedoston lukemiselle
     * @param tied tiedoston nimi
     */
    public void lueTiedosto(String tied) {
        setTiedostonOletusNimi(tied);
        
        File dir = new File("seura");
        dir.mkdir();
        
        try ( Scanner lukija = new Scanner(new FileReader(getTiedostonOletusNimi()))) {
            
            
            //String rivi;
            while ( lukija.hasNextLine() ) {
                String rivi = lukija.nextLine();
                Joukkue joukkue = new Joukkue();
                joukkue.parse(rivi);
                lisaa(joukkue);
            }
            
        } catch (FileNotFoundException ex) { 
            System.err.println("tiedosto ei aukea " + ex.getMessage());
        } 
        
    }
    
    
    /**
     * Luetaan aikaisemmin nimetystä tiedostosta
     */
    public void lueTiedosto() { lueTiedosto(getTiedostonOletusNimi()); }
    
    
    //-------------------------------SQL-jutut alla-------------------------------
    //----------------------------------------------------------------------------
    
    
    /**
     * Tarkistetaan että kannassa on joukkueiden tarvitsema taulu
     * @param nimi tietokannan nimi
     */
    public Joukkueet(String nimi) {
        
        kanta = Kanta.alustaKanta(nimi);
        try ( Connection yhteys = kanta.annaKantayhteys() ) {
            
            DatabaseMetaData meta = yhteys.getMetaData();
            
            try ( ResultSet taulu = meta.getTables(null, null, "Joukkueet", null) ) {
                if ( !taulu.next() ) { //luodaan harrastukset-taulu:
                    
                    try ( PreparedStatement sql = yhteys.prepareStatement(apuJoukkue.annaLuontiLauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch ( SQLException e ) { System.out.println("Tietokantavirhe: " + e.getMessage()); }
    }
    
    
    /** Lisätään uusi jokkue seuralle
     * @param joukkue lisättävä joukkue 
     */
    public void lisaaSQL(Joukkue joukkue) {
        
        try ( Connection yhteys = kanta.annaKantayhteys(); PreparedStatement sql = joukkue.annaLisaysLauseke(yhteys) ) {
            
            sql.executeUpdate();
            
            try ( ResultSet tulos = sql.getGeneratedKeys() ) {
                joukkue.tarkistaId(tulos);
            }
            
        } catch ( SQLException e ) { System.out.println("Tietokantavirhe: " + e.getMessage()); }
    }
    
    
    /** Palauttaa jäsenet listassa hakuehdon mukaisesti
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi
     * @return jäsenet listassa
     * @throws SQLException  jos jokin menee vikaan
     */
    public Collection<Joukkue> etsi(String hakuehto, int k) throws SQLException {
        
        String ehto = hakuehto;
        String kysymys = "";
        if ( k < 0 ) { kysymys = apuJoukkue.getKysymys(0); ehto = ""; } else { kysymys = apuJoukkue.getKysymys(k); }
        //KYSYMYS EI SAA OLLA OHJELMAN KÄYTTÄJÄN SYÖTETTÄ!
        
        
        ArrayList<Joukkue> loydetyt = new ArrayList<Joukkue>();
        //avataan tietokantayhteys:
        try ( Connection yhteys = kanta.annaKantayhteys();
              PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Jasenet WHERE " + kysymys + " LIKE ?") ) {
            
            
            sql.setString(1, "%" + ehto + "%");     //eli tässä siis ykkönen ja ylempi kysymysmerkki vaihtaa paikkaa
            
            try ( ResultSet tulokset = sql.executeQuery() ) {
                
                while( tulokset.next() ) {
                    
                    Joukkue j = new Joukkue();
                    j.parseSQL(tulokset);
                    loydetyt.add(j);
                }
            }
            return loydetyt;
            
        } catch ( SQLException e ) {
            System.out.println("Tietokantavirhe: " + e.getMessage());
        }
        
        return loydetyt;
        
    }
    
    
    
    /** Testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        //Joukkueet tiimit = new Joukkueet();
        
        Joukkue pantterit = new Joukkue();  pantterit.taytaTiedot();
        Joukkue simpanssit = new Joukkue(); simpanssit.taytaTiedot();
        
        
    
    }

}
