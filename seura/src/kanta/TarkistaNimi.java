package kanta;

/** Luokassa suoritetaan oikeellisuustarkistuksia eri nimien tarkistamiselle
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class TarkistaNimi {

    
    /** Funktio nimen oikeellisuuden tarkistamiselle. Nimen t�ytyy olla kaksiosainen ja osat on erotettava v�lily�nnill�.
     * @param nimi nimi joka tarkistetaan
     * @return 'true' jos nimi ok, 'false' muuten
     * @example
     * <pre name="test">
     * nimiTarkistus("Bo Andersen") === true;
     * nimiTarkistus("Sami") === false;
     * nimiTarkistus("Johannes Tapani Karhu") === true;
     * nimiTarkistus("") === false;
     * </pre>
     */
    public static boolean nimiTarkistus(String nimi) {
        
        String[] tulos = nimi.split(" ");
        
        if ( tulos.length < 2 ) return false;
        
        return true;
    }
    
    
    /** Tarkistusfunktio joukkueen nimelle. Nimen oltava 4-20 merkki� pitk�.
     * @param nimi joukkueen nimi
     * @return 'true', jos nimi ok. false muuten.
     * @example
     * <pre name="test">
     * joukkueTarkistus("kyyt") === true;
     * joukkueTarkistus("") === false;
     * </pre>
     */
    public static boolean joukkueTarkistus(String nimi) {
        if ( nimi.length() < 4 || nimi.length() > 20 ) { return false; } return true;
    }
    
    
    /**
     * @param args ei k�yt�ss�
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
    
    }

}
