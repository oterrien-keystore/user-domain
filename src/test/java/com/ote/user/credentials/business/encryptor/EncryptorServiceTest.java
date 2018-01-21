package com.ote.user.credentials.business.encryptor;

import com.ote.user.credentials.api.EncryptorServiceProvider;
import com.ote.user.credentials.api.IEncryptorService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class EncryptorServiceTest {

    private final static IEncryptorService encryptorService = EncryptorServiceProvider.getInstance().getFactory().createService();

    @Test
    public void encrypt() throws Exception {

        String password = "password";
        String encryptedPassword = "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86";
        Assertions.assertThat(encryptorService.encrypt(password)).isEqualTo(encryptedPassword);
    }

    //@Test
    public void generateNewFile() throws Exception {

        Path path = Paths.get("./src/test/resources", "com/ote/user/credentials/business", "sampleEncryptedPasswords.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            int j = 2;
            for (int i = 0; i < 200; i++) {
                String password = new RandomString(j, ThreadLocalRandom.current()).nextString();
                writer.write(password + "," + encryptorService.encrypt(password));
                writer.newLine();
                if (i % 10 == 0 & i < 100) {
                    j *= 2;
                }
                if (i % 10 == 0 && i > 100) {
                    j++;
                }
            }
        }
    }

    @Test
    public void testEncryption() {

        Scanner scanner = new Scanner(ClassLoader.getSystemResourceAsStream("com/ote/user/credentials/business/sampleEncryptedPasswords.csv"));
        streamScanner(scanner).
                map(EncryptedPassword::new).
                forEach(p -> {
                    try {
                        Assertions.assertThat(encryptorService.encrypt(p.value)).describedAs("Bad encryption for '" + p.value + "'").isEqualTo(p.encryptedValue);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private class EncryptedPassword {

        private final String value;
        private final String encryptedValue;

        EncryptedPassword(String value) {
            String[] csv = value.split(",");
            this.value = csv[0];
            this.encryptedValue = csv[1];
        }
    }

    private static Stream<String> streamScanner(final Scanner scanner) {
        final Spliterator<String> spliterator = Spliterators.spliterator(scanner, Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
        return StreamSupport.stream(spliterator, false).onClose(scanner::close);
    }

    public static class RandomString {

        /**
         * Generate a random string.
         */
        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
        }

        public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static final String lower = upper.toLowerCase(Locale.ROOT);

        public static final String digits = "0123456789";

        public static final String alphanum = upper + lower + digits;

        private final Random random;

        private final char[] symbols;

        private final char[] buf;

        public RandomString(int length, Random random, String symbols) {
            if (length < 1) throw new IllegalArgumentException();
            if (symbols.length() < 2) throw new IllegalArgumentException();
            this.random = Objects.requireNonNull(random);
            this.symbols = symbols.toCharArray();
            this.buf = new char[length];
        }

        /**
         * Create an alphanumeric string generator.
         */
        public RandomString(int length, Random random) {
            this(length, random, alphanum);
        }

        /**
         * Create an alphanumeric strings from a secure generator.
         */
        public RandomString(int length) {
            this(length, new SecureRandom());
        }

        /**
         * Create session identifiers.
         */
        public RandomString() {
            this(21);
        }

    }
}
