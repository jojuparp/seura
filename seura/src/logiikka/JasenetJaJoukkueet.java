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
 * Vastuualueet alla:
 * Osaa etsi jäsenen joukkueen nimen perusteella
 * Kirjoittaa ja lukee jäsenen ja joukkueen id-tiedostoa
 * Osaa lisätä ja poistaa tietyn jäsen-joukkue -relaation
 * 
 * Avustajaluokat: JasenJaJoukkue
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class JasenetJaJoukkueet {
    
    private String tiedostonOletusNimi = "JasenetJaJoukkueet";
    private ArrayList<JasenJaJoukkue> relaatiot = new ArrayList<JasenJaJoukkue>();
    private boolean muutettu = false;
    
    //SQL:
    private Kanta kanta;
    private static JasenJaJoukkue apuJasenJaJoukkue = new JasenJaJoukkue();
    
    
    /**
     * Oletuskonstruktori
     */
    public JasenetJaJoukkueet() { /**/ }
    
    
    /** metodi lisää relaation listaan
     * @param r relaatio
     * <pre name="test">
     * JasenetJaJoukkueet relt = new JasenetJaJoukkueet();
     * JasenJaJoukkue r = new JasenJaJoukkue();
     * relt.lisaa(r);
     * relt.anna(0) === r;
     * </pre>
     */
    public void lisaa(JasenJaJoukkue r) { relaatiot.add(r); muutettu = true; }
    
    
    /** Metodi relaation palauttamiseen indeksist� i. K�ytet��n vain testatessa muita metodeja.
     * 
     * @param i indeksi
     * @return relaatio
     * @example
     * <pre name="test">
     * JasenetJaJoukkueet relt = new JasenetJaJoukkueet();
     * JasenJaJoukkue r = new JasenJaJoukkue();
     * relt.lisaa(r);
     * relt.anna(0) === r;
     * </pre>
     */
    public JasenJaJoukkue anna(int i) { return relaatiot.get(i); }
    
    
    /** Metodi relaatioiden lukumäärän saamiseen
     * @return relaatioiden lukumäärä
     * @example
     * <pre name="test">
     * JasenetJaJoukkueet relaatiot = new JasenetJaJoukkueet();
     * JasenJaJoukkue r = new JasenJaJoukkue();
     * relaatiot.lisaa(r);
     * relaatiot.getLkm() === 1;
     * </pre>
     */
    public int getLkm() { return relaatiot.size(); }
    
    
    /**
     * @return tiedoston oletusnimi
     */
    public String getTiedostonOletusNimi() { return tiedostonOletusNimi + ".dat"; }
    
    
    /**
     * @return varmuuskopiotiedoston nimi
     */
    public String getVkNimi() { return tiedostonOletusNimi + ".bak"; }
    
    
    /** asetetaan tiedoston nimi
     * @param nimi asetettava nimi
     */
    public void setTiedostonOletusNimi(String nimi) { tiedostonOletusNimi = nimi; }
    
    
    /** Metodi kertoo sisältääkä relaatiokanta tietyn relaation
     * @param rel relaatio
     * @return 'true', jos sisältää. 'false' muuten.
     * @example
     * <pre name="test">
     * JasenetJaJoukkueet relaatiot = new JasenetJaJoukkueet();
     * JasenJaJoukkue r = new JasenJaJoukkue(2,4);
     * JasenJaJoukkue r2 = new JasenJaJoukkue(2,4);
     * relaatiot.lisaa(r);
     * relaatiot.onkoOlemassa(r2) === true;
     * </pre>
     */
    public boolean onkoOlemassa(JasenJaJoukkue rel) {
        
        for ( int i = 0; i < getLkm(); i++ ) {
            if ( anna(i).equals(rel) ) return true;
        }
        return false;
    }
    
    
    /** Metodi jäsenen joukkue -relaatioiden etsimiseen
     * @param jasenId jäsen-id
     * @return lista relaatioista
     */
    public List<JasenJaJoukkue> etsiJasenenJoukkueet(int jasenId) {
        
        List<JasenJaJoukkue> loydetyt = new ArrayList<JasenJaJoukkue>();
        
        for ( JasenJaJoukkue relaatio : relaatiot ) {
            if ( relaatio.getJasenId() == jasenId ) loydetyt.add(relaatio);
        }
        
        return loydetyt;
    }
    
    
    /** Metodi joukkueen jäsen -relaatioiden etsimiseen
     * @param joukkueId joukkue-id
     * @return lista relaatioista
     */
    public List<JasenJaJoukkue> etsiJoukkeenJasenet(int joukkueId) {
        
        List<JasenJaJoukkue> loydetyt = new ArrayList<JasenJaJoukkue>();
        
        for ( JasenJaJoukkue relaatio : relaatiot ) {
            if (relaatio.getJasenId() == joukkueId ) loydetyt.add(relaatio);
        }
        
        return loydetyt;
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
            
            for ( JasenJaJoukkue r : relaatiot ) { fo.println(r.toString()); }
            
        } catch (FileNotFoundException ex) { System.err.println("tiedosto ei aukea" + ex.getMessage()); }
        
        
        muutettu = false;
    }
    
    
    /**
     * Metodi tiedoston lukemiselle
     * @param tied luettava tiedsoto
     */
    public void lueTiedosto(String tied) {
        setTiedostonOletusNimi(tied);
        
        try ( Scanner lukija = new Scanner(new FileReader(getTiedostonOletusNimi()))) {
            
            
            //String rivi;
            while ( lukija.hasNextLine() ) {
                String rivi = lukija.nextLine();
                JasenJaJoukkue j = new JasenJaJoukkue();
                j.parse(rivi);
                lisaa(j);
            }
            
        } catch (FileNotFoundException ex) { 
            System.err.println("tiedosto ei aukea " + ex.getMessage());
        } 
        
    }
    
    
    /**
     * Luetaan aikaisemmin nimetystä tiedostosta
     */
    public void lueTiedosto() { lueTiedosto(getTiedostonOletusNimi()); }
    
    
    /**
     * Metodi relaation poistamiseen
     * @param r poistettava relaatio
     */
    public void poista(JasenJaJoukkue r) { relaatiot.remove(r); muutettu = true; }
    
    
    //=============================SQL-jutut=======================================
    //=============================================================================
    
    
    /** Tarkistetaan, että kannassa on relaatioiden tarvitsema taulu
     * @param nimi tietokannan nimi
     */
    public JasenetJaJoukkueet(String nimi) {
        
        kanta = Kanta.alustaKanta(nimi);
        
        try ( Connection yhteys = kanta.annaKantayhteys() ) {
            
            DatabaseMetaData meta = yhteys.getMetaData();
            
            try ( ResultSet taulu = meta.getTables(null, null, "JasenetJaJoukkueet", null) ) {
                if ( !taulu.next() ) { //luodaan harrastukset-taulu:
                    
                    try ( PreparedStatement sql = yhteys.prepareStatement(apuJasenJaJoukkue.annaLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch ( SQLException e ) { System.out.println("Tietokantavirhe: " + e.getMessage()); }
        
    }

    
    /** Lisää uuden relaation tietokantaan. Ottaa jäsenen omistukseensa. 
     * @param r lisättävän relaation viite
     * @throws SQLException jos jokin menee vikaan
     */
    public void lisaaSQL(JasenJaJoukkue r) throws SQLException {
        
        try ( Connection yhteys = kanta.annaKantayhteys(); PreparedStatement sql = r.annaLisayslauseke(yhteys) ) { 
            sql.executeUpdate();
            
            try ( ResultSet tulos = sql.getGeneratedKeys() ) {
                r.tarkistaId(tulos);
            } 
            
        } catch ( SQLException e ) {
            System.out.println("Tietokantavirhe: " + e.getMessage());
        }
    }
    
    
    /** Palauttaa Jäsen-joukkue -relaatiot listassa hakuehdon mukaisesti
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi
     * @return jäsenet listassa
     * @throws SQLException  jos jokin menee vikaan
     */
    public Collection<JasenJaJoukkue> etsi(String hakuehto, int k) throws SQLException {
        
        String ehto = hakuehto;
        String kysymys = "";
        if ( k < 0 ) { kysymys = apuJasenJaJoukkue.getKysymys(0); ehto = ""; } else { kysymys = apuJasenJaJoukkue.getKysymys(k); }
        //KYSYMYS EI SAA OLLA OHJELMAN KÄYTTÄJÄN SYÖTETTÄ!
        
        
        ArrayList<JasenJaJoukkue> loydetyt = new ArrayList<JasenJaJoukkue>();
        //avataan tietokantayhteys:
        try ( Connection yhteys = kanta.annaKantayhteys();
              PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Jasenet WHERE " + kysymys + " LIKE ?") ) {
            
            
            sql.setString(1, "%" + ehto + "%");     //eli tässä siis ykkönen ja ylempi kysymysmerkki vaihtaa paikkaa
            
            try ( ResultSet tulokset = sql.executeQuery() ) {
                
                while( tulokset.next() ) {
                    
                    JasenJaJoukkue j = new JasenJaJoukkue();
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
    
    
    /**
     * Metodi tietyn jäsenen jäsen-joukkue -relaatioiden antamiselle
     * @param jasenId jäsenen id
     * @return lista relaatioista
     */
    public List<JasenJaJoukkue> annaJasenenJoukkuuet(int jasenId) {
        
        List<JasenJaJoukkue> loydetyt = new ArrayList<JasenJaJoukkue>();
        
        try ( Connection yhteys = kanta.annaKantayhteys(); 
              PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM JasenetJaJoukkueet WHERE jasenID LIKE ?") ) {
                
        }
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
    
    }
}
