/**
 * 
 */
package logiikka;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


/**
 * Luokka yhden joukkueen k�sittelyyn. Vastuualueet alla:
 * Osaa antaa joukkueen i.:n kent�n tiedot
 * Osaa muuttaa merkkijono "2|pantterit" joukkueen tiedoiksi
 * Osaa antaa merkkijonona i.:n kent�n tiedot
 * Osaa laittaa merkkijonon i.:nneksi kent�ksi
 * 
 * Avustajaluokat: --
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class Joukkue {
    
    private int testiNo = 0; //testinumero olioviitteiden oikeellisuuden tarkistamiselle. Poistetaan myöhemmin.
    
    private int id = 0;
    //private int jasenId = 0;
    private static int seuraavaId = 1;
    private String nimi = "";
    
    
    /** Oletuskonstruktori. Vielä kesken.
     * 
     */
    public Joukkue() {/**/}    
    
    
    /** Alustetaan joukkue tietylle j�senelle
     * @param jid j�sen-id
     */
    //public Joukkue(int jid) { jasenId = jid; }
    
    
    /** Konstruktori j�senen ja joukkueen yhdist�mist� varten.
     * @param jaid j�sen-id
     * @param joid joukkue-id
     */
    //public Joukkue(int jaid, int joid) { jasenId = jaid; id = joid; }
    
    
    /**
     * Rekisteräi jäsenelle seuraavan vapaana olevan id-numeron
     * @return jäsenen id-numero
     * @example
     * <pre name="test">
     * Joukkue tiikerit = new Joukkue();
     * tiikerit.getId() === 0;
     * tiikerit.rekisteroi();
     * tiikerit.getId() === 1;
     * tiikerit.rekisteroi();
     * tiikerit.getId() === 1;
     * </pre>
     */
    public int rekisteroi() {
        
        if ( id != 0 ) return id; //jos yritetään rekister�id� jo aiemmin rekister�ity� joukkuetta
        id = seuraavaId;
        seuraavaId++;
        return id;  
    }
    
    
    /** Metodi palauttaa joukkue-olion id-numeron
     * @return id numero
     * @example
     * <pre name="test">
     * Joukkue lepakot = new Joukkue();
     * lepakot.rekisteroi();
     * lepakot.getId() === 2;
     * </pre>
     */
    public int getId() { return id; }
    
    
    /** Metodi joukkueen nimen antamiselle
     * @return joukkueen nimi
     * @example
     * <pre name="test">
     * Joukkue kissat = new Joukkue();
     * kissat.setNimi("kissat");
     * kissat.getNimi() === "kissat";
     * </pre>
     */
    public String getNimi() { return nimi; }
    
    
    /** asetetaan id-numero
     * @param n asetettava numero
     * @return uusi id-numero
     */
    public int setId(int n) { id = n; return id; }
    
    
    /** Metodi joukkueen nimen asettamiselle
     * @param nimi nimi, joka halutaan asettaa
     * @return joukkueen nimi
     * <pre name="test">
     * Joukkue kissat = new Joukkue();
     * kissat.setNimi("kissat");
     * kissat.getNimi() === "kissat";
     * </pre>
     */
    public String setNimi(String nimi) { return this.nimi = nimi; }
        
    
    /** Tulostusmetodi joukkueen tiedoille.
     * @param out mihin tulostetaan
     */
    public void tulosta(PrintStream out) { out.println("Joukkue: " + nimi + "\n"); }
    
    
    /** Tulostusmetodi joukkueen tiedoille
     * @param os mihin tulostetaan
     */
    public void tulosta(OutputStream os) { tulosta(new PrintStream(os)); }
    
    
    /**
     * Metodi antaa joukkueen tiedot merkkijonona: id|nimi
     * @return tiedot merkkijonona
     */
    @Override
    public String toString() { return id + "|" + nimi; }
    
    
    /** metodi asettaa joukkueen tiedot merkkijonosta
     * @param tiedot tiedot merkkijonona
     */
    public void parse(String tiedot) {
        
        if ( tiedot.isEmpty() ) return;
        
        String[] tied = tiedot.split("\\|");
        
        id = Integer.valueOf(tied[0]); nimi = tied[1];
    }
    
    
    /** ====================RAKENNUSTELINE==========================
     * Metodilla täytetään joukkueen tiedot. Käytetään testitarkoitukseen ht 5-vaiheessa.
     */
    public void taytaTiedot() { setNimi("Pantterit"); }
    
    
    //================================SQL-jutut alapuolella!===============================
    //=====================================================================================
    
    
    /** Hakee Joukkueet-taulun kentät SQL-kyselyä varten
     * TODO: hae kentät suoraan SQL-taulusta. (onko mahdollista?)
     * @param indeksi lukua vastaava kenttä Jasenet-taulussa
     * @return kentän otsake
     */
    public String getKysymys(int indeksi) {
        
        String[] kysymykset = { "joukkeID" , "nimi" };
        
        return kysymykset[indeksi];
    }
    
    
    /**
     * @return joukkue-taulun SQL-luontilauseke.
     */
    public String annaLuontiLauseke() {
        
        return "CREATE TABLE Joukkueet (\n" +
                "JoukkueID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "nimi VARCHAR(100) DEFAULT ''\n)";
    }
    
    
    /** Antaa joukkueen lisäyslausekkeen
     * @param yhteys tietokantayhteys
     * @return joukkueen lisäyslauseke
     * @throws SQLException jos jokin menee pieleen
     */
    public PreparedStatement annaLisaysLauseke(Connection yhteys) throws SQLException {
        
        PreparedStatement sql = yhteys.prepareStatement(
                "INSERT INTO Joukkueet (JoukkueID, nimi) VALUES (?,?)"
                );
        
        if ( id != 0 ) sql.setInt(1, id);
        sql.setString(2, nimi);
        
        return sql;
    }
    
    
    /**
     * @param result lisäyslauseen ResultSet
     * @throws SQLException jos jokin menee pieleen
     */
    public void tarkistaId(ResultSet result) throws SQLException {
        if ( !result.next() ) return;
        int tunnus = result.getInt(1);
        if ( id == tunnus ) return;
        setId(tunnus);
    }
    
    
    /**
     * @param result tiedot ResultSetistä
     * @throws SQLException jos jokin menee pieleen
     */
    public void parseSQL(ResultSet result) throws SQLException {
        setId(result.getInt("JoukkueID"));
        setNimi(result.getString("nimi"));
        
    }
    
    
    /** testipääohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Joukkue tiikerit = new Joukkue();   tiikerit.taytaTiedot();
        Joukkue apinat = new Joukkue();     apinat.taytaTiedot();
        
        tiikerit.tulosta(System.out);
        apinat.tulosta(System.out);
    
    }

}
