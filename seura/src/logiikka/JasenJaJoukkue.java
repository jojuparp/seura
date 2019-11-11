package logiikka;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Liimaluokka j�senen ja joukkueen yhdist�miselle. Vastuualueet alla:
 * Osaa muuttaa merkkijonon 1|3 relaation tiedoiksi
 * Osaa antaa relaation tiedot merkkijonona
 * 
 * Avustajaluokat: --
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class JasenJaJoukkue {

    
    private int jasenId = 0;
    private int joukkueId = 0;
    
    
    /**
     * Oletuskonstruktori
     */
    public JasenJaJoukkue() {/**/}    
    
    
    /** Konstruktori, jossa kahdesta kokonaisluvusta otetaan id-tiedot
     * @param jaid j�senen id
     * @param joid joukkueen id
     */
    public JasenJaJoukkue(int jaid, int joid) {
        jasenId = jaid;
        joukkueId = joid;
    }
    
    
    /** Metodi palauttaa 
     * @return j�sen-id:n
     * @example
     * <pre name="test">
     * JasenJaJoukkue relaatio = new JasenJaJoukkue(1, 2);
     * relaatio.getJasenId() === 1;
     * </pre>
     */
    public int getJasenId() { return jasenId; }
    
    
    /** metodi palauttaa joukkue-id:n
     * @return joukkue-id
     * @example
     * <pre name="test">
     * JasenJaJoukkue relaatio = new JasenJaJoukkue(1, 2);
     * relaatio.getJoukkueId() === 2;
     * </pre>
     */
    public int getJoukkueId() { return joukkueId; }
    
    
    /** Metodi tarkistaa kahden relaation identtisyyden
     * @param relaatio relaatio johon verrataan
     * @return 'true', jos identtiset, 'false' muuten
     * @example
     * <pre name="test">
     * JasenJaJoukkue r1 = new JasenJaJoukkue(1,2);
     * JasenJaJoukkue r2 = new JasenJaJoukkue(1,2);
     * r1.equals(r2) === true;
     * </pre>
     */
    public boolean equals(JasenJaJoukkue relaatio) {
        return ( relaatio.getJasenId() == jasenId && relaatio.getJoukkueId() == joukkueId );
    }
    
    
    /** Metodi relaation j�sen-id:n asettamiselle
     * @param id asetettava id
     */
    public void setJasenId(int id) { jasenId = id; }
    
    
    /** metodi relaation j�sen-id:n asettamiseksi
     * @param id asetettava id
     */
    public void setJoukkueId(int id) { joukkueId = id; }
    
    
    /** metodi asettaa jäsenen ja joukkueen id-tiedot merkkijonosta
     * @param tiedot tiedot merkkijonona
     */
    public void parse(String tiedot) {
        
        if ( tiedot.isEmpty() ) return;
        
        String[] tied = tiedot.split("\\|");
        
        jasenId = Integer.valueOf(tied[0]); joukkueId = Integer.valueOf(tied[1]);
    }
    
    
    /**
     * @return jäsenId ja joukkueId merkkijonoon: jasenId|joukkueId
     */
    @Override
    public String toString() { return jasenId + "|" + joukkueId; }
    
    
    //===============================SQL-jutut alla====================================
    //=================================================================================
    
    
    /** Hakee Joukkueet-taulun kentät SQL-kyselyä varten
     * TODO: hae kentät suoraan SQL-taulusta. (onko mahdollista?)
     * @param indeksi lukua vastaava kenttä Jasenet-taulussa
     * @return kentän otsake
     */
    public String getKysymys(int indeksi) {
        
        String[] kysymykset = { "jasenID" , "joukkueID" };
        
        return kysymykset[indeksi];
    }
    
    
    /**
     * @return SQL-taulun "JasenetJaJoukkueet"-luontilauseke
     */
    public String annaLuontilauseke() {
        
        return "CREATE TABLE JasenetJaJoukkueet (\n" + 
                " jasenID INTEGER PRIMARYKEY, \n" + 
                " joukkueID INTEGER PRIMARYKEY\n" + 
                ")";
    }
    
    
    /**
     * @param yhteys tietokantayhteys
     * @return Jäsen-Joukkue -relaation lisäyslauseke
     * @throws SQLException jos jokin menee vikaan.
     */
    public PreparedStatement annaLisayslauseke(Connection yhteys) throws SQLException {
        
        PreparedStatement sql = yhteys.prepareStatement (
                "INSERT INTO JasenetJaJoukkueet" + 
        "(jasenID, nimi, syntaika, puhno, sapo, osoite, vanhempi, vanhemmanPuhno, vanhemmanSapo, lisatietoja)" + 
        "VALUES (?,?)" 
                                );
        
        sql.setInt(1, getJasenId()); sql.setInt(2, getJoukkueId());
        
        return sql;
        
    }
    
    
    /** Tarkistetaan, onko Jäsen-joukkue -relaation id-numerot muuttuneet lisäyksessä
     * @param tulos lisäyslausekkeen resultset
     * @throws SQLException jos jokin menee pieleen
     * 
     */
    public void tarkistaId(ResultSet tulos) throws SQLException {
        
        if ( !tulos.next() ) return;
        int jaID = tulos.getInt(1); int joID = tulos.getInt(2);
        
        if ( jaID == getJasenId() && joID == getJoukkueId() ) return;
        setJasenId(jaID); setJoukkueId(joID);
    }
    
    
    /** Haetaan Jäsen-Joukkue -relaation tiedot ResultSetistä
     * @param tulos tulokset mistä tiedot otetaan
     * @throws SQLException jos jokin menee vikaan.
     */
    public void parseSQL(ResultSet tulos) throws SQLException {
        
        setJasenId(tulos.getInt(1)); setJoukkueId(tulos.getInt(2));
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
    }

}
