package kanta;


/** Luokassa suoritetaan oikeellisuustarkistus puhelinnumerolle.
 * @author Joni Parpala
 * @version 10.5.2019
 *
 */
public class TarkistaPuhelinnumero {

    
    /** Funktio korjaa puhelinnumerosta viivat ja v�lily�nnit pois
     * @param pno numero jota korjataan
     * @return puhelinnumero ilman viivoja ja v�lily�ntej�
     * @example
     * <pre name="test">
     * korjaaNumero("040-123 1234") === "0401231234";
     * </pre>
     */
    public static String korjaaNumero(String pno) {
        
        StringBuilder apu = new StringBuilder(pno);
        int k = pno.length();
        
        for (int i = 0; i < k; i++) {
            if ( apu.charAt(i) == '-' || apu.charAt(i) == ' ' ) { apu.deleteCharAt(i); k--; }
        }
        
        return apu.toString();
    }
    
    
    /** Oikeellisuustarkistus matkapuhelinnumeron formatoinnille.
     * Palauttaa 'tosi', jos numerossa on joko 10 tai 13 merkki�, ja numeron ensimm�inen merkki on joko '+' tai '0'
     * @param pno annettu numero
     * @return onko oikein formatoitu
     * @example
     * <pre name="test">
     * tarkistaPno("0401231234") === true;
     * tarkistaPno("040123123") === false;
     * </pre>
     */
    public static boolean tarkistaPno(String pno) {
        if ( (pno.length() != 10 && pno.length() != 13)
              ||
             (pno.charAt(0) != '+' && pno.charAt(0) != '0')
                ) return false;
        return true;
    }
    
    
    /**
     * @param args ei k�yt�ss�
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
    
    }

}
