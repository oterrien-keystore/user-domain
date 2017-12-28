package com.ote.user.credentials.business;

import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CredentialTest {

    //@Test
    public void generateNewFile() throws IOException {

        Path path = Paths.get("./src/test/resources", "com/ote/user/credentials/business", "sampleEncryptedPasswords.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            int j = 2;
            for (int i = 0; i < 200; i++) {
                String password = new RandomString(j, ThreadLocalRandom.current()).nextString();
                writer.write(password + "," + Encryptor.getInstance().encrypt(password));
                writer.write("\n");
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
                parallel().
                map(EncryptedPassword::new).
                forEach(p -> Assertions.assertThat(Encryptor.getInstance().encrypt(p.value)).isEqualTo(p.encryptedValue));

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
        final Spliterator<String> splt = Spliterators.spliterator(scanner, Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
        return StreamSupport.stream(splt, false).onClose(scanner::close);
    }
}
