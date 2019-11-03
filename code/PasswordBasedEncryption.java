import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Key;

/**
 * Example of using Password-based encryption
 */

public class PasswordBasedEncryption {
    public static void main(String[] args) throws Exception {
        // setup iteration count
        int iterationCount = 5;

        // Setup passwords used to encrypt values

        String[] passwords = new String[] { "abc", "P@ssW0rD", "Th!$IsAV3ryL0n9pA$$w0rd" };
        System.out.println("Password based encryption timings");

        for (int i = 0; i < passwords.length; i++) {
            System.out.println("-----------------------------");
            // initialise time array
            double[] time = new double[6];
            System.out.println("Password used: " + passwords[i]);

            for (int j = 0; j < iterationCount; j++) {
                PBEKeySpec pbeKeySpec;
                PBEParameterSpec pbeParamSpec;
                SecretKeyFactory keyFac;
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
                byte[] cleartext = "This is an example string".getBytes();

                // Encrypt the plaintext
                byte[] ciphertext = pbeCipher.doFinal(cleartext);
                System.out.println("Cipher text: " + Utils.toHex(ciphertext));

                pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

                // Decrypt the plaintext
                byte[] ciphertext2 = pbeCipher.doFinal(ciphertext);
                String plainText = new String(ciphertext2);

                // end time
                long endTime = System.nanoTime();

                // calculate total time and add to the time array
                long totalTime = endTime - startTime;

                double totalTimeMs = totalTime / 1000000.0;
                BigDecimal totalTimeMsRounded = new BigDecimal(totalTimeMs).setScale(2, RoundingMode.HALF_EVEN);
                double roundedTotalTime = totalTimeMsRounded.doubleValue();
                time[j] = roundedTotalTime;

                // output of all times and key components of the encryption algorithm
                System.out.println("Decrypted text: " + plainText);
                System.out.println("loop " + (j + 1) + ": " + roundedTotalTime + "ms");
            }
            // summed time calculation to output average over the iteration counts.
            double summedTime = 0;
            for (var k = 0; k < time.length; k++) {
                summedTime += time[k];
            }

            // Work out and print the average time for each password

            System.out.println("Total time:" + new BigDecimal(summedTime).setScale(2, RoundingMode.HALF_EVEN));
            double avgTime = summedTime / iterationCount;
            System.out.println("average time:" + new BigDecimal(avgTime).setScale(2, RoundingMode.HALF_EVEN));

        }
    }
}