package kanta;

import java.io.OutputStream
;

import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/** Luokassa alustetaan p�iv�m��r�-olion toiminnallisuus. Voidaan k�ytt�� esim. syntym�ajan oikeellisuustarkistukseen.
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class Pvm {

    private int p, kk, v;
    
    private int kPituudet[] = {31,28,31,30,31,30,31,31,30,31,30,31};
    
    
    /**
     * Oletusarvot parametrittomalle konstruktorille
     */
    public Pvm() { p = 0; kk = 0; v = 0; }
    
    
    /** Konstruktorissa alustetaan p�iv�, kuukausi ja vuosi
     * @param paiva p�iv�
     * @param kuukausi kuukausi
     * @param vuosi vuosi
     */
    public Pvm(int paiva, int kuukausi, int vuosi) { alusta(paiva, kuukausi, vuosi); }
    
    
    /** konstruktori merkkijonoparemetriselle p�iv�m��r�oliolle
     * @param pv merkkijono
     */
    public Pvm(String pv) { parse(pv); alusta(p, kk, v); }
    
    
    /** Alustetaan p�iv�m��r�-olio. Nolla-arvot eiv�t vaikuta attribuuttien arvoihin. 
     * @param ip p�iv�
     * @param ikk kuukausi
     * @param iv vuosi
     * Pvm paiva = new Pvm(1,4,1999);
     * paiva.alusta(2, 8, 2007);
     * paiva.toString() === "2.8.2007";
     */
    public void alusta(int ip, int ikk, int iv) {
        
        if ( ip < 0 || ikk < 0 || ikk > 12 || iv < 0 || iv > 2999 ) return;
              
        if ( onkoKarkausvuosi(iv) ) kPituudet[1] = 29; else kPituudet[1] = 28;
        
        if ( ip == 0 ) 
        { if ( getPaiva() > kPituudet[getKuukausi()] ) return; }
        
        if ( ikk > 0 || ip > 0) {
            
            if (ip > kPituudet[ikk-1] ) return;
            kk = ikk; p = ip; 
        }
        
        if ( iv == 0 ) return;
        v = iv;
    }
    
    
    /** Metodi tarkastaa, onko p�iv�m��r� formatoitu oikein.
     * Oikein formatoituna: *p.k.vvvv*
     * @param pvm p�iv�m��r�
     * @return 'true', jos formatoitu oikein. 'false' muuten.'
     * @example
     * <pre name="test">
     * tarkistaPvm("30.2.2001") === false;
     * tarkistaPvm("1.2.2003") === true;
     * </pre>
     */
    public static boolean tarkistaPvm(String pvm) {
        Pvm paiva = new Pvm();
        paiva.parse(pvm);
        if ( paiva.getPaiva() > 0 && paiva.getKuukausi() > 0 && paiva.getVuosi() > 0 ) { paiva = null; return true;}
        return false;
    }
    
    
    /**
     * metodi p�iv�m��r�tietojen saamiseen yhdest� samasta merkkijonosta
     * @param kokoRoska p�iv�m��r�tiedot yhdess� merkkijonossa
     * Pvm p = new Pvm();
     * p.parse("21.1.2003");
     * p.getPaiva() === 21;
     * p.getKuukausi() === 1;
     * p.getVuosi() === 2003;
     * 
     */
    public void parse(String kokoRoska) {
        StringBuilder apu = new StringBuilder(kokoRoska);
        int p_apu = Mjonot.erota(apu, '.', 0);
        int kk_apu = Mjonot.erota(apu, '.', 0);
        int v_apu = Mjonot.erotaInt(apu, 0);
        
        alusta(p_apu, kk_apu, v_apu);
    }
    
    
    /** metodi p�iv�n saamiselle
     * @return p�iv� kokonaislukuna
     * @example
     * <pre name="test">
     * Pvm pv = new Pvm("10.11.2031");
     * pv.getPaiva() === 10;
     * </pre>
     */
    public int getPaiva() { return p; }
    
    
    /** metodi kuukauden saamiselle
     * @return kuukausi kokonaislukuna
     * @example
     * <pre name="test">
     * Pvm pv = new Pvm("10.11.2031");
     * pv.getKuukausi() === 11;
     * </pre>
     */
    public int getKuukausi() { return kk;}
    
    
    /** metodi vuoden samiselle
     * @return vuosi kokonaislukuna
     * @example
     * <pre name="test">
     * Pvm pv = new Pvm("10.11.2031");
     * pv.getVuosi() === 2031;
     * </pre>
     */
    public int getVuosi() { return v; }
    
    
    /** metodi kertoo, onko vuosi karkausvuosi
     * @param vuosi vuosi, jota tarkastellaan
     * @return onko karkausvuosi
     * @example
     * <pre name="test">
     * onkoKarkausvuosi(2000) === true;
     * onkoKarkausvuosi(1900) === false;
     * onkoKarkausvuosi(2004) === true;
     * </pre>
     */
    public static boolean onkoKarkausvuosi(int vuosi) {        
        
        if ((vuosi % 4 == 0 && vuosi % 100 != 0) || vuosi % 400 == 0) return true;
        return false;
    }
    
    
    /** metodi antaa pvm-olion kuukauden p�ivin�
     * @return kuukauden p�ivien m��r� kokonaislukuna
     * @example
     * <pre name="test">
     * Pvm p = new Pvm(1,2,2003);
     * p.getKkPaivina() === 28;
     * </pre>
     */
    public int getKkPaivina() { return kPituudet[this.getKuukausi()-1]; }
    
    
    /** metodi palauttaa pvm-olion vuoden p�ivin�. karkausvuosi otetaan huomioon. 
     * @return vuosi p�ivin�
     * @example
     * <pre name="test">
     * Pvm paiva = new Pvm("11.3.2002");
     * paiva.getVuosiPaivina() === 730730;
     * </pre>
     */
    public int getVuosiPaivina() {
        
        if (onkoKarkausvuosi(this.getVuosi())) return this.getVuosi()*365+1;
        return this.getVuosi()*365;  
    }
    
    
    /** metodi palauttaa Pvm-olion p�iv�m��r�n p�ivin�. Karkausvuosi otetaaan huomioon. 
     * @return p�iv�t kokonaislukuna. 
     * @example
     * <pre name="test">
     * Pvm paiva = new Pvm("11.3.2002");
     * paiva.getPvmPaivina() === 730772;
     * </pre>
     */
    public int getPvmPaivina() { return this.getVuosiPaivina() + this.getKkPaivina() + this.getPaiva(); }
    
    /**
     * Metodi muuttaa p�iv�m��r�olion tiedot merkkijonoksi
     * @return olion tiedot merkkijonona
     * @example
     * <pre name="test">
     * Pvm pv1 = new Pvm(1, 3, 1999);
     * pv1.toString() === "1.3.1999"
     * </pre>
     */
    @Override
    public String toString() { return getPaiva() + "." + getKuukausi() + "." + getVuosi(); }
    

    /** luodaan oma tulostusmetodi. Tulostaa p�iv�m��r�n kokonaisuudessaan. 
     * @param os tuloskanava
     */
    public void tulosta (OutputStream os) {
        
        PrintStream out = new PrintStream(os);
        out.println(this.toString());
    }
    
    /** funktio kertoo kahden p�iv�m��r�olion v�lisen aikaj�rjestyksen
     * @param pv1 ensimm�inen p�iv�m��r�
     * @param pv2 toinen p�iv�m��r�
     * @return -1, mik�li pv1 on ennen pv2, 0 mik�li p�iv�m��r�t ovat samat, 1 mik�li pv2 on ennen pv1
     * @example
     * <pre name="test">
     * Pvm p1 = new Pvm(1,2,2001);
     * Pvm p2 = new Pvm("9.3.2002");
     * compareTo(p1, p2) === -1;
     * </pre>
     */
    public static int compareTo(Pvm pv1, Pvm pv2) {
        if (pv1.getPvmPaivina() == pv2.getPvmPaivina()) return 0;
        if (pv1.getPvmPaivina() < pv2.getPvmPaivina()) return -1;
        return 1;
        
    }
    
    
    /** metodi vertailee p�iv�m��r�oliota toiseen p�iv�m��r�olioon
     * @param pv p�iv�m��r�, johon vertaillaan
     * @return -1, jos olioviitteen pvm ensin, 1 jos argumentin pvm ensin. 0 jos samat p�iv�m��r�t
     * @example
     * <pre name="test">
     * Pvm p1 = new Pvm(1,2,2001);
     * Pvm p2 = new Pvm("9.3.2002");
     * p1.compareTo(p2) === -1;
     * </pre>
     */
    public int compareTo(Pvm pv) {
        if (getPvmPaivina() == pv.getPvmPaivina()) return 0;
        if (getPvmPaivina() < pv.getPvmPaivina()) return -1;
        return 1;
    }
    
    
    /** metodi tarkistaa, onko p�iv�m��r�olio sama kuin toinen p�iv�m��r�olio
     * @param pv p�iv�, johon verrataan
     * @return onko samat
     * Pvm p1 = new Pvm(1,2,2001);
     * Pvm p2 = new Pvm("9.3.2002");
     * p1.equals(p2) === false;
     */
    public boolean equals(Pvm pv) { return this.getPvmPaivina() == pv.getPvmPaivina(); }

    
    /** testip��ohjelma. aliohjelmakutsut ja alustuksia
     * @param args ei k�yt�ss�
     */
    public static void main(String[] args) {
        
    Pvm p1 = new Pvm(1, 1, 1999); 
    System.out.println(p1);
    Pvm p2 = new Pvm();
    p2.parse("1.1.1990");
    Pvm p3 = new Pvm(1,1,2000);
    p2.tulosta(System.out);
    System.out.println(p2.getVuosi());
    int p = compareTo(p1, p2);
    System.out.println(p);
    int k = p2.compareTo(p1);
    System.out.println(k);
    boolean samat = p1.equals(p3);
    System.out.println(samat);
    Pvm p4 = new Pvm();
    p4.alusta(21, 1, 2012);
    p4.alusta(31, 3, 0);
    p4.tulosta(System.out);
    //Pvm p5 = new Pvm();
    System.out.println(tarkistaPvm("30.2.2017"));
    
    }
}