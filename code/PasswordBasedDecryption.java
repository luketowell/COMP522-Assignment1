import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.util.Scanner;

/**
 * Example of using Password-based encryption
 */

public class PasswordBasedDecryption {
    public static void main(String[] args) throws Exception {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Salt
        byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee,
                (byte) 0x99 };

        // Iteration count
        int count = 1024;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);

        // Initialization of the password
        char[] password = "newpassword".toCharArray();

        // Create parameter for key generation
        pbeKeySpec = new PBEKeySpec(password);

        // Create instance of SecretKeyFactory for password-based encryption
        // using DES and MD5
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

        // Generate a key
        Key pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        // Take the userinput hex string ciphertext
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter ciphertext:");
        String inputCipherText = input.nextLine();

        // Our cipher text is a hex string so needs to be converted to a byte array
        byte[] cipherTextByteArray = hexStringToByteArray(inputCipherText);

        // Decrypt the plaintext
        byte[] cipherText = pbeCipher.doFinal(cipherTextByteArray);
        String plainText = new String(cipherText);
        System.out.println("Decrypted Text: " + plainText);
        input.close();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}