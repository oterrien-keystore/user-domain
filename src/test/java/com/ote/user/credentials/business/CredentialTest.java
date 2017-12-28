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

    @ParameterizedTest
    @CsvSource({
            "test, ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff",
            "toto, 10e06b990d44de0091a2113fd95c92fc905166af147aa7632639c41aa7f26b1620c47443813c605b924c05591c161ecc35944fc69c4433a49d10fc6b04a33611",
            "monPassword12DD;DEF, 8250780b2e29b8d08b6384025a91b082e328d6ee57f77c42033ec9180fef17bc34939b7c4bd813dae72eb6423f4f586582455c23f410796abaa4c1f3905dc0d8"})
    public void testEncryption(String password, String expectation) {
        Assertions.assertThat(Encryptor.getInstance().encrypt(password)).isEqualTo(expectation);
    }

    @Test
    @Disabled
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
    public void testThread() {

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
