package logiikka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

//import com.sun.tools.javac.util.ArrayUtils;

/**
 * Luokka koko jäsenistön käsittelyyn. Vastualueet alla:
 * Osaa lisätä jäseniä
 * Osaa poistaa jäseniä
 * Osaa hakea jäsenen
 * Osaa antaa jäsenen id-numeron perusteella
 * Osaa lukea ja kirjoittaa tiedostoon
 * 
 * Avustajaluokat: Jasen
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class Jasenet {
    
    
    private static int MAX_JASENIA = 8;
    private int lkm = 0;
    private Jasen[] alkiot = new Jasen[MAX_JASENIA];
    private boolean muutettu = false;
    private String tiedostonOletusNimi = "jasenet";
    
    //SQL:
    private Kanta kanta;
    private static Jasen apuJasen = new Jasen(); //tämän avulla pyydetään tarvittavat kantatiedot, jos muuta jäsentä ei ole käytössä
    
    
    /**
     * Oletuskonstruktori.
     */
    public Jasenet() {/**/}
    
        
    /** metodi lisää jäsenen tietorakenteeseen
     * @param j lisättävä jäsen
     * @example
     * <pre name="test">
     * Jasenet tyypit = new Jasenet();
     * Jasen raipe = new Jasen();
     * tyypit.getLkm() === 0;
     * tyypit.lisaa(raipe); tyypit.getLkm() === 1;
     * tyypit.anna(0) === raipe;
     * </pre>
     */
    public void lisaa(Jasen j) { 
        if ( lkm == MAX_JASENIA ) {
            MAX_JASENIA = MAX_JASENIA+10;
            Jasen[] temp = new Jasen[MAX_JASENIA];
            
            for (int i = 0; i < alkiot.length; i++) {
                temp[i] = alkiot[i];
            }
            
            alkiot = temp;
            
        }
        
        alkiot[lkm] = j; lkm++; muutettu = true;
        
    }
    
    
    /** Metodi poistaa jäsenen
     * @param j poistettava jäsen
     * @example
     * <pre name="test">
     * Jasenet jasenisto = new Jasenet();
     * Jasen peksi = new Jasen();
     * Jasen jake = new Jasen();
     * peksi.rekisteroi(); peksi.taytaTiedot();
     * jake.rekisteroi(); jake.taytaTiedot();
     * jasenisto.lisaa(peksi);
     * jasenisto.lisaa(jake);
     * 
     * jasenisto.poista(peksi);
     * jasenisto.getLkm() === 1;
     * jasenisto.poista(jake);
     * jasenisto.getLkm() === 0;
     * jasenisto.poista(jake);
     * jasenisto.getLkm() === 0;
     * </pre>
     */
    public void poista(Jasen j) {
        
        if ( getLkm() < 1 ) return;
        
        //poisto:
        int poistettavanPaikka = -1;
        
        for ( int i = 0; i < getLkm(); i++ ) {
            
            if ( j.equals(anna(i)) ) { poistettavanPaikka = i; lkm--; muutettu = true; break; }

        }
        
    
        for (int i = poistettavanPaikka; i < getLkm(); i++) {
            alkiot[i] = alkiot[i+1];
        }
        
        alkiot[getLkm()] = null;
    }
    
    
    /** Metodi korvaa aiemman jäsenen jäsenistöstä tai lisää uuden jäsenen.
     * @param jasen korvattava tai lisättävä jäsen
     */
    public void korvaaTaiLisaa(Jasen jasen) {
        int id = jasen.getId();
        for ( int i = 0; i < getLkm(); i++ ) {
            if ( alkiot[i].getId() == id ) { alkiot[i] = jasen; muutettu = true; return; }
        } 
        lisaa(jasen);
        
    }
    
    
    /** Jäsenen etsiminen nimen perusteella
     * @param nimi nimi
     * @return lista jäsenistä
     */
    public List<Jasen> haeJasenet(String nimi) {
        
        List<Jasen> osumat = new ArrayList<Jasen>();
        
        //String[] nimet = nimi.split(" ");
        
        for ( int i = 0; i < getLkm(); i++ ) {
            if ( anna(i).getNimi().toLowerCase().equals(nimi.toLowerCase()) ) osumat.add(anna(i));
        }
        
        return osumat;
    }
        
    
    /** metodi Jasen-olion palauttamiseen taukukon indeksistä i
     * =================HUOM! RAKENNUSTELINE!!!=========================
     * @param i indeksi
     * @return jäsen-olio
     * @throws IndexOutOfBoundsException jos i ei sallitulla alueella
     */
    public Jasen anna(int i) throws IndexOutOfBoundsException {
        
        if ( i < 0 || lkm <= i ) throw new IndexOutOfBoundsException( "Laiton indeksi" + i );
        return alkiot[i];
    }
    
    
    /** Metodi jäsenen hakemiselle taulukosta id:n perusteella
     * @param jasenId jäsen id
     * @return jäsen
     */
    public Jasen etsiJasen(int jasenId) {
        
        for ( Jasen j : alkiot ) {
            if ( j.getId() == jasenId ) return j;
        }
        return null;
    }
    
    
    /** Palauttaa seuran jäsenten lukumäärän
     * @return jäsenten lukumäärä
     */
    public int getLkm() {  return lkm; }
    
    
    /** Metodi antaa alkioiden "maksimimäärän", eli käytännössä pituuden.
     * Käytetään lähinnä apu- ja testausmetodina.
     * @return alkiot-taulon pituus
     * Jasen pekka = new Jasen();
     * Jasenet pojat = new Jasenet();
     * lkm = MAX_JASENIA;
     * pojat.lisaa(pekka);
     * pojat.getMaxAlkiot() === 16;
     */
    public int getMaxAlkiot() { return alkiot.length; }
    
    
    /**
     * @return tiedonston oletusnimi
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
    
    
//    /**
//     * Metodi jäsenen tietotiedoston kirjoittamiselle.
//     * Metodi testatttu lukumetodin yhteydessä toimivaksi.
//     */
//    public void kirjoitaTiedosto() {
//        
//        if ( !muutettu ) return;
//        
//        
//        File vKopio = new File(getVkNimi());
//        File tied = new File(getTiedostonOletusNimi());
//        vKopio.delete();
//        tied.renameTo(vKopio);
//        
//        String tiedNimi = getTiedostonOletusNimi();
//        try (PrintStream fo = new PrintStream(new FileOutputStream(tiedNimi, false))) {
//            
//            for ( int i = 0; i < getLkm(); i++ ) { fo.println(alkiot[i].toString()); }
//            fo.close();
//            
//        } catch (FileNotFoundException ex) { System.err.println("tiedosto ei aukea" + ex.getMessage()); }
//        
//        muutettu = false;
//    }
//    
//    
//    /**
//     * Metodi tiedoston lukemiselle
//     * @param tied tiedoston nimi
//     * @example
//     * <pre name="test">
//     * #import java.io.*;
//     * 
//     * String testi = "jasenetTesti";
//     * Jasenet jasenet = new Jasenet();
//     * jasenet.setTiedostonOletusNimi(testi);
//     * Jasen aku1 = new Jasen(); Jasen aku2 = new Jasen();
//     * aku1.taytaTiedot(); aku2.taytaTiedot();
//     * aku1.rekisteroi(); aku2.rekisteroi();
//     * jasenet.lisaa(aku1); jasenet.lisaa(aku2);
//     * jasenet.kirjoitaTiedosto();
//     * jasenet = new Jasenet(); //tyhjäys
//     * jasenet.lueTiedosto(testi);
//     * jasenet.anna(0).getId() === 1;
//     * </pre>
//     */
//    public void lueTiedosto(String tied) {
//        setTiedostonOletusNimi(tied);
//        
//        
//        try ( Scanner lukija = new Scanner(new FileReader(getTiedostonOletusNimi()))) {
//            
//            while ( lukija.hasNextLine() ) {
//                String rivi = lukija.nextLine();
//                Jasen jasen = new Jasen();
//                jasen.parse(rivi);
//                lisaa(jasen);
//            }
//            
//        } catch (FileNotFoundException ex) { 
//            System.err.println("tiedosto ei aukea " + ex.getMessage());
//        } 
//        
//    }
    
    
//    /**
//     * Luetaan aikaisemmin nimetystä tiedostosta
//     * @throws IOException jos jokin menee vikaan
//     */
//    public void lueTiedosto() throws IOException { lueTiedosto(getTiedostonOletusNimi()); }
    
    
    //=============================SQL-jutut alla!==========================================
    //======================================================================================
    
    
    /**
     * @param nimi tietokannan nimi
     * @throws SQLException jos jokin menee vikaan
     */
    @SuppressWarnings("resource")
    public Jasenet(String nimi) throws SQLException {
        
        kanta = Kanta.alustaKanta(nimi);
        
        try ( Connection yhteys = kanta.annaKantayhteys(); ) {
        // Hankitaan tietokannan metadata ja tarkistetaan siitä onko
        // Jasenet nimistä taulua olemassa.
        // Jos ei ole, luodaan se. Ei puututa siihen, onko
        // mahdollisesti olemassa olevalla taululla oikea rakenne,
        // käyttäjä saa kuulla siitä virheilmoituksen kautta
            DatabaseMetaData meta = yhteys.getMetaData();
        
            try ( ResultSet taulu = meta.getTables(null, null, "Jasenet", null); ) {
                if ( !taulu.next() ) { //luodaan Jasenet-taulu
                    
                    PreparedStatement sql = yhteys.prepareStatement(apuJasen.annaLuontiLauseke());
                    sql.execute();             
                }   
            } 
            
        } catch ( SQLException e ) {
            System.out.println("Ongelmia kannan kanssa: " + e.getMessage());
        }
        
    }
    
    
    /** Lisää uuden jäsenen tietokantaan. Ottaa jäsenen omistukseensa. 
     * @param jasen lisättävän jäsenen viite
     * @throws SQLException jos jokin menee vikaan
     */
    public void lisaaSQL(Jasen jasen) throws SQLException {
        
        try ( Connection yhteys = kanta.annaKantayhteys(); PreparedStatement sql = jasen.annaLisayslauseke(yhteys) ) { 
            sql.executeUpdate();
            
            try ( ResultSet tulos = sql.getGeneratedKeys() ) {
                jasen.tarkistaId(tulos);
            } 
            
        } catch ( SQLException e ) {
            System.out.println("Tietokantavirhe: " + e.getMessage());
        }
    }
    
    
    /** Palauttaa jäsenet listassa hakuehdon mukaisesti
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi
     * @return jäsenet listassa
     * @throws SQLException  jos jokin menee vikaan
     */
    public Collection<Jasen> etsi(String hakuehto, int k) throws SQLException {
        
        String ehto = hakuehto;
        String kysymys = "";
        if ( k < 0 ) { kysymys = apuJasen.getKysymys(0); ehto = ""; } else { kysymys = apuJasen.getKysymys(k); }
        //KYSYMYS EI SAA OLLA OHJELMAN KÄYTTÄJÄN SYÖTETTÄ!
        
        
        ArrayList<Jasen> loydetyt = new ArrayList<Jasen>();
        //avataan tietokantayhteys:
        try ( Connection yhteys = kanta.annaKantayhteys();
              PreparedStatement sql = yhteys.prepareStatement("SELECT * FROM Jasenet WHERE " + kysymys + " LIKE ?") ) {
            
            
            sql.setString(1, "%" + ehto + "%");     //eli tässä siis ykkönen ja ylempi kysymysmerkki vaihtaa paikkaa
            
            try ( ResultSet tulokset = sql.executeQuery() ) {
                
                while( tulokset.next() ) {
                    
                    Jasen j = new Jasen();
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
    
        try {
            
            new File("kokeilu.db").delete();
            Jasenet jasenet = new Jasenet("kokeilu");
            
            Jasen pekka = new Jasen(); Jasen jaakko = new Jasen();
            pekka.taytaTiedot(); jaakko.taytaTiedot();
            
            jasenet.lisaaSQL(pekka); jasenet.lisaaSQL(jaakko);
            pekka.tulosta(System.out);
            
            System.out.println("sql-testi\n-------------\n");
            
            
            int i = 0;
            for ( Jasen jasen : jasenet.etsi("", -1) ) {
                System.out.println("Jäsen nro: " + i++);
                jasen.tulosta(System.out);
            }
            
            new File("kokeilu.db").delete();
            
        } catch ( SQLException e ) { System.out.println("virhe: " + e.getMessage()); }
    

        
    
    }

}
