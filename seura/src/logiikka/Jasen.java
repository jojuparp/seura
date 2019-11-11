package logiikka;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kanta.Pvm;
//import kanta.TarkistaPuhelinnumero;
//import kanta.TarkistaNimi;
//import kanta.Pvm;
//import kanta.TarkistaOsoite;


/**Luokka yhden jäsenen käsittelyyn. Luokan vastuualueet alla:
 * id-numeron ylläpito
 * Tietää yhden jäsenen kentät(id, nimi, puhnro jne.)
 * Osaa tarkistaa tietyn kentän oikeellisuuden eli syntaksin
 * osaa muuttaa jonon 2|Pekka Koskela|...|.. jäsenen tiedoiksi
 * osaa antaa merkkijonona i.:n kentän tiedot
 * osaa laittaa merkkijonon i.:nneksi kentäksi
 * 
 * Avustajaluokat: --
 * 
 * @author Joni
 * @version 9.3.2019
 *
 */
public class Jasen implements Cloneable{

    private int id = 0; //id-numero, ei voida muuttaa
    private static int seuraavaId = 1; //staattinen muttuja === kaikilla luokan instansseilla yksi yhteinen muuttuja
    private String nimi = "";
    private String syntAika = "";
    private String puhNo = "";
    private String sapo = "";
    private String osoite = "";
    private String vanhempi = ""; //vanhemman nimi
    private String vanhemmanPuhNo = "";
    private String vanhemmanSapo = ""; //vanhemman sähkäpostiosoite
    private String lisatietoja = "";

    

    
    
    /** Metodi palauttaa jäsen-olion id-numeron
     * @return id numero
     * @example
     * <pre name="test">
     * Jasen late = new Jasen();
     * late.rekisteroi();
     * late.getId() === 2;
     * </pre>
     */
    public int getId() { return id; }
    
    
    /**
     * @return tietokenttien lukumäärä
     */
    public int getKenttia() { return 9; }
    
    
    /**
     * @return ensimmäinen kenttä
     */
    public int ekaKentta() { return 1; }
    
    
    /** metodi jäsenen nimen palauttamiselle
     * @return jäsenen nimi
     */
    public String getNimi() { return nimi; }
    
    
    /**
     * @return osoite
     */
    public String getOsoite() { return osoite; }
    
    
    /**
     * @return sähköpostiosoite
     */
    public String getSapo() { return sapo; }
    
    
    /**
     * @return syntymäaika
     */
    public String getSyntaika() { return syntAika; }

    
    /**
     * @return puhelinnumero
     */
    public String getPuhno() { return puhNo; }
    
    
    /**
     * @return vanhemman nimi
     */
    public String getVanhemmanNimi() { return vanhempi; }
    
    
    /**
     * @return vanhemman puhno
     */
    public String getVanhemmanPuhno() { return vanhemmanPuhNo; }
    
    
    /**
     * @return vanhemman sähköpostiosoite
     */
    public String getVanhemmanSapo() { return vanhemmanSapo; }
    
    
    /**
     * @return lisäteitoja
     */
    public String getLisatietoja() { return lisatietoja; }
    
    
    /** Asetetaan uusi tunnusnumero ja palautetaan se.
     * @param n asetettava tunnus
     * @return uusi tunnus
     */
    public int setId(int n) { id = n; return getId(); }
    
    
    /** asetetaan nimi
     * @param n nimi
     * @return nimi
     */
    public String setNimi(String n) { nimi = n; return null; }
    
    
    /** asetetaan osoite
     * @param o osoite
     * @return osoite
     */
    public String setOsoite(String o) { osoite = o; return null; }
    
    
    /** asetetaan sähköpostiosoite
     * @param s sähköpostiosoite
     * @return sähköpostiosoite
     */
    public String setSapo(String s) { sapo = s; return null; }
    
    
    /** asetetaan syntymäaika
     * @param sa syntynmäaika
     * @return syntymäaika
     * 
     */
    public String setSyntaika(String sa) { 
        
        if ( !(Pvm.tarkistaPvm(sa)) ) return "VIRHE! ANNA MUODOSSA: P.KK.VVVV" ;
        
        syntAika = sa; return null;
    }
    
    
    /** asetetaan puhelinnumero
     * @param pn puhelinnumero
     * @return puhelinnumero
     */
    public String setPuhno(String pn) { puhNo = pn; return null; }

    
    /** asetetaan vanhemman nimi
     * @param vn vanhemman nimi
     * @return vanhemman nimi
     */
    public String setVanhemmanNimi(String vn) { vanhempi = vn; return null; }
    
    
    /** asetetaan vanhemman puhelinnumero
     * @param vpn numero
     * @return vanhemman puhelinnumero
     */
    public String setVanhemmanPuhno(String vpn) { vanhemmanPuhNo = vpn; return null; }
    
    
    /** asetetaan vanhemman sähköposti
     * @param vsp asetettava sähköposti
     * @return vanhemman sähköpostiosoite
     */
    public String setVanhemmanSapo(String vsp) { vanhemmanSapo = vsp; return null; }

    
    /** asetetaan lisätiedot
     * @param lt lisätiedot
     * @return lisätietoja
     */
    public String setLisatietoja(String lt) { lisatietoja = lt; return null; }
    
    
    @Override
    public Jasen clone() throws CloneNotSupportedException {
        Jasen uusi;
        uusi = (Jasen) super.clone();
        return uusi;
    }
    
    
    /**
     * Rekisteräi jäsenelle seuraavan vapaana olevan id-numeron
     * @return jäsenen id-numero
     * @example
     * <pre name="test">
     * Jasen seppo = new Jasen();
     * seppo.getId() === 0;
     * seppo.rekisteroi();
     * seppo.getId() === 5;
     * seppo.rekisteroi();
     * seppo.getId() === 5;
     * </pre>
     */
    public int rekisteroi() {
        
        if ( id != 0 ) return id; //jos yritet��n rekister�id� jo aiemmin rekister�ity� j�sent�
        id = seuraavaId;
        seuraavaId++;
        return id;
        
    }
    
    /** Luodaan tulostusmetodi jäsenen tiedoille
     * @param out tietovirta mihin tulostetaan
     */
    public void tulosta(PrintStream out) {
        
        out.println( String.format("%03d", id, 3) + /*", TESTINRO: " + testiNo +*/  "\n" + nimi + ", " + syntAika );
        out.println( "Yhteystiedot:\n" + "puh: " + puhNo + " sp: " + sapo + " os: " + osoite );
        out.println( "Vanhemman yhteystiedot:\n" + vanhempi + "\n" + "sp: " + vanhemmanSapo + " puhno: " + vanhemmanPuhNo) ;
        out.println("Lisätietoja: " + lisatietoja + "\n");
    }
    

    /** Luodaan tulostusmetodi jäsenen tiedoille
     * @param os tietovirta mihin tulostetaan
     */
    public void tulosta(OutputStream os) { tulosta(new PrintStream(os)); }  
    
    /**
     * Metodi muuttaa käyttäjän tiedot muotoon: |id|nimi|synt.aika|...|...|..
     * @return edellämainitun muotoinen merkkijono
     * @example
     * <pre name="test">
     * Jasen lauri = new Jasen();
     * lauri.rekisteroi(); lauri.taytaTiedot(); lauri.setLisatietoja("testi");
     * lauri.toString() === "1|||||||||testi";
     * </pre>
     */
    @Override
    public String toString() {
        
        return (
                getId() + "|" + 
                getNimi() + "|" + 
                getSyntaika() + "|" + 
                getPuhno() + "|" +
                getSapo() + "|" +
                getOsoite() + "|" + 
                getVanhemmanNimi() + "|" + 
                getVanhemmanPuhno() + "|" +
                getVanhemmanSapo() + "|" + 
                getLisatietoja()
                );
    }
    
    
    /**
     * Metodi hakee jäsenen tiedot merkkijonosta: id|nimi|osoite|...
     * @param tiedot jäsenen tiedot
     * @example
     * <pre name="test">
     * Jasen j1 = new Jasen();
     * Jasen j2 = new Jasen();
     * j1.taytaTiedot(); j1.rekisteroi();
     * j2.taytaTiedot(); j2.rekisteroi();
     * j2.parse(j1.toString());
     * j2.getId() === 3;
     * </pre>
     */
    public void parse(String tiedot) {
        
        String[] tied = tiedot.split("\\|");
        
        id = Integer.valueOf(tied[0]); nimi = tied[1]; syntAika = tied[2]; puhNo = tied[3];
        sapo = tied[4]; osoite = tied[5]; vanhempi = tied[6]; vanhemmanPuhNo = tied[7]; 
        vanhemmanSapo = tied[8]; lisatietoja = tied[9];
        
        if ( seuraavaId <= id ) seuraavaId = id+1;
    }
    
    
    /**
     * Metodi jäsenen tietoijen alustamiseen. Alustaa kaikki tiedot tyhjiksi, luukunottamatta "lisätietoja"-attribuuttia, johon asetetaan "-".
     */
    public void taytaTiedot() {
        
        nimi = "matti";
        syntAika = "02.10.2001";
        puhNo = "02301032";
        sapo = "testi@testi.com";
        osoite = "testikuja 1";
        lisatietoja = "ei ole";
        vanhempi = "pena"; 
        vanhemmanPuhNo = "23545234";
        vanhemmanSapo = "testi@testi.com"; 
    }
    
    
    //=======================SQL-jutut alapuolella!========================================
    //=====================================================================================

    
    /** 
     * @return SQL "Jäsen"-taulun luontilauseke
     */
    public String annaLuontiLauseke() {
        
                //"DROP TABLE Jasenet;\n"+
                return "CREATE TABLE Jasenet (\n" +
                "  jasenID INTEGER PRIMARY KEY AUTOINCREMENT,\n"+
                "  nimi VARCHAR(100) NOT NULL,\n"+
                "  syntaika VARCHAR(100) DEFAULT '',\n"+
                "  puhno  VARCHAR(100) DEFAULT '',\n"+
                "  sapo  VARCHAR(100) DEFAULT '',\n"+
                "  osoite  VARCHAR(100) DEFAULT '',\n"+
                "  vanhempi  VARCHAR(100) DEFAULT '',\n"+
                "  vanhemmanPuhno  VARCHAR(100) DEFAULT '',\n"+
                "  vanhemmanSapo  VARCHAR(100) DEFAULT '',\n"+
                "  lisatietoja  VARCHAR(100) DEFAULT ''\n"+
                ")";
    }
    
    
    
    /**
     * Antaa jäsenen lisäyslausekkeen
     * @param yhteys tietokantayhteys
     * @return Jäsenen lisäyslauseke
     * @throws SQLException jos lausekkeessa ongelmia
     */
    public PreparedStatement annaLisayslauseke(Connection yhteys) throws SQLException{
        
        PreparedStatement sql = yhteys.prepareStatement (
                "INSERT INTO Jasenet" + 
        "(jasenID, nimi, syntaika, puhno, sapo, osoite, vanhempi, vanhemmanPuhno, vanhemmanSapo, lisatietoja)" + 
        "VALUES (?,?,?,?,?,?,?,?,?,?)" 
                                );
       
        //syötetään kentät tauluun, vältetään SQL-injektiot.
        //käyttäjän syötettä ei ikinä kyselylauseeseen ennen kuin on tarkistettu injektioiden varalta
        if ( id != 0 ) sql.setInt(1, id); else sql.setString(1, null);
        sql.setString(2, nimi);
        sql.setString(3, syntAika);
        sql.setString(4, puhNo);
        sql.setString(5, sapo);
        sql.setString(6, osoite);
        sql.setString(7, vanhempi);
        sql.setString(8, vanhemmanPuhNo);
        sql.setString(9, vanhemmanSapo);
        sql.setString(10, lisatietoja);
        
       
           return sql;
    }
    
    
    /** Tarkistetaan onko jäsenen id muuttunut lisäyksessä
     * @param result lisäyslauseen ResultSet
     * @throws SQLException jos jokin menee pieleen
     */
    public void tarkistaId(ResultSet result) throws SQLException {
        
        if ( !result.next() ) return;
        int tunnus = result.getInt(1);
        if ( tunnus == id ) return;
        setId(tunnus);
    }
    
    
    /** Otetaan jäsenen tiedot ResultSetistä
     * @param result tulokset mistä tiedot otetaan
     * @throws SQLException jos jokin menee vikaan
     */
    public void parseSQL(ResultSet result) throws SQLException {
        
        setId(result.getInt("jasenID"));
        setNimi(result.getString("nimi"));
        setSyntaika(result.getString("syntaika"));
        setPuhno(result.getString("puhno"));
        setOsoite(result.getString("osoite"));
        setVanhemmanNimi(result.getString("vanhempi"));
        setVanhemmanPuhno(result.getString("vanhemmanPuhno"));
        setVanhemmanSapo(result.getString("vanhemmanSapo"));
        setLisatietoja(result.getString("lisatietoja"));
        
    }
    
    
    /** Hakee Jasenet-taulun SQL-kyselyä varten
     * TODO: hae kentät suoraan SQL-taulusta. (onko mahdollista?)
     * @param indeksi lukua vastaava kenttä Jasenet-taulussa
     * @return kentän otsake
     */
    public String getKysymys(int indeksi) {
        
        String[] kysymykset = {
          "jasenID", "nimi", "syntaika", "puhno", "osoite",
          "vanhempi", "vanhemmanPuhno", "vanhemmanSapo", "lisatietoja"
        };
        
        return kysymykset[indeksi];
    }
    
    
    /** Testipääohjelma. Kokeillaan testitietojen asettamista ja tulostamista. 
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Jasen sami = new Jasen();
        Jasen pekka = new Jasen();
        Jasen jake = new Jasen();
        
        sami.rekisteroi(); //rekisteröi jäsenen (antaa vapaan id-numeron)
        //pekka.rekisteroi();
        jake.rekisteroi();
        
        sami.taytaTiedot(); //täyttää jäsenelle tiedot. Normaalisti käyttäjä täyttää toki.
        pekka.taytaTiedot();
        
        sami.setNimi("sami");
        System.out.println(pekka);
        System.out.println(sami);
        
        String testi = "a|b|c|d|e|f|g";
        String[] t = testi.split("\\|");
        System.out.println(t.length);
        

    }




}
