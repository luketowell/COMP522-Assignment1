import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;

/**
 * Example of using Password-based encryption
 */

public class PasswordBasedEncryption {
    public static void main(String[] args) throws Exception {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // setup iteration count
        int iterationCount = 5;

        // Setup passwords used to encrypt values
        String[] passwords = new String[] { "abc", "P@ssW0rD", "Th!$IsAV3ryL0n9pA$$w0rd" };

        for (int i = 0; i < passwords.length; i++) {

            // initialise time array
            long[] time = new long[6];

            for (int j = 0; j < iterationCount; j++) {
                // start timing
                long startTime = System.nanoTime();
                // Salt
                byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8,
                        (byte) 0xee, (byte) 0x99 };

                // Iteration count
                int count = 1024;

                // Create PBE parameter set
                pbeParamSpec = new PBEParameterSpec(salt, count);

                // Initialization of the password
                char[] password = passwords[i].toCharArray();

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
                pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

                // Our plaintext
                byte[] cleartext = "This is another example".getBytes();

                // Encrypt the plaintext
                byte[] ciphertext = pbeCipher.doFinal(cleartext);

                pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

                // Decrypt the plaintext
                byte[] ciphertext2 = pbeCipher.doFinal(ciphertext);
                String plainText = new String(ciphertext2);

                // end time
                long endTime = System.nanoTime();

                // calculate total time and add to the time array
                long totalTime = endTime - startTime;
                long totalTimeMs = totalTime / 1000000;
                time[j] = totalTimeMs;

                // output of all times and key components of the encryption algorithm
                System.out.println("password used: " + passwords[i]);
                System.out.println("cipher : " + Utils.toHex(ciphertext));
                System.out.println("Encrypted value: " + plainText);
                System.out
                        .println("time taken(ms) to run encryption and decryption for loop " + j + ": " + totalTimeMs);
            }
            // summed time calculation to output average over the iteration counts.
            long summedTime = 0;
            for (var k = 0; k < time.length; k++) {
                summedTime += time[k];
            }

            // Work out and print the average time for each password
            System.out.println("Total time to run 5 iterations: " + summedTime);
            long avgTime = summedTime / iterationCount;
            System.out.println("average time take for " + iterationCount + " iterations: " + avgTime);
        }
    }
}