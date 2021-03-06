package model.persistence;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Classe che si occupa di gestire la cifratura delle password
 *
 * @author GruppoNoSuchMethod
 */
public class Encryption {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "hbftrsgyknbhcvfg".getBytes();

    /**
     * Metodo per criptare una stringa
     *
     * @param valueToEnc Stringa da cifrare
     * @return Stringa cifrata
     * @exception RuntimeException Segnala un errore durante la cifratura
     */
    public String encrypt(String valueToEnc) {
        try {
                Key key = generateKey();
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, key);

                byte[] encValue = cipher.doFinal(valueToEnc.getBytes());
                byte[] encryptedByteValue = new Base64().encode(encValue);

                return new String(encryptedByteValue);
        } catch (Exception e) {
                throw new RuntimeException();
        }
    }

    /**
     * Metodo per decifrare una stringa
     *
     * @param valueToDec Stringa da decifrare
     * @return Stringa decifrata
     * @exception RuntimeException Segnala un errore durante l'esecuzione dela decifratura
     */
    public String decrypt(String valueToDec) {
        try {
                Key key = generateKey();
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);

                byte[] decodedBytes = new Base64().decode(valueToDec.getBytes());
                byte[] encValue = cipher.doFinal(decodedBytes);

                return new String(encValue);
        } catch (Exception e) {
                throw new RuntimeException();
        }
    }

    public static Key generateKey() {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }
}
