package logiikka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

/** Luokka SQLite yhteyden muodostamiseksi. Tavoite on että yhdelle nimelle on vain yksi tällainen Kanta-olio
 * 
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class Kanta {

    private static HashMap<String, Kanta> kannat = new HashMap<String, Kanta>();
    private String tiedostonPerusnimi = "";
    
    
    /**
     * Alustetaan kanta
     * @param nimi kannan nimi
     */
    private Kanta(String nimi) { setTiedostonNimi(nimi); }
    
    
    /**
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() { return tiedostonPerusnimi + ".db"; }
    
    
    /** Asetetaan uusi tiedoston perusnimi
     * @param nimi nimi
     */
    public void setTiedostonNimi(String nimi) { tiedostonPerusnimi = nimi; }
    
    
    /** Alustetaan kantayhteys
     * @param nimi kannan nimi
     * @return kanna tiedot, joilla voidaan operoida
     */
    public static Kanta alustaKanta(String nimi) {
        
        if ( kannat.containsKey(nimi) ) return kannat.get(nimi);
        Kanta uusi = new Kanta(nimi);
        kannat.put(nimi, uusi);
        return uusi;
    }
    
    
    /** Antaa tietokantayhteyden
     * @return tietokantayhteys
     * @throws SQLException Jos yhteyden avaamisessa on ongelmia
     */
    public Connection annaKantayhteys() throws SQLException {
        
        String sDriver = "org.sqlite.JDBC";
        try {
            Class.forName(sDriver);
        } catch (ClassNotFoundException e) {
            System.err.println("Virhe luokan " + sDriver + " lataamisessa: " + e.getMessage());
        }
        return DriverManager.getConnection("jdbc:sqlite:" + getTiedostonNimi());
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
    }

}
