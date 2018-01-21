package com.ote.user.credentials.business.encryptor;

import com.ote.user.credentials.api.EncryptorServiceProvider;
import com.ote.user.credentials.api.IEncryptorService;
import com.ote.user.credentials.api.IUserCredentialService;
import com.ote.user.credentials.api.UserCredentialServiceProvider;
import com.ote.user.credentials.api.exception.UserNotFoundException;
import com.ote.user.credentials.spi.IUserCredentialRepository;
import mockito.MockitoExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
public class CredentialServiceTest {

    @Mock
    private IUserCredentialRepository userCredentialRepository;
    private IUserCredentialService userCredentialService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.userCredentialService = UserCredentialServiceProvider.getInstance().getFactory().createService(userCredentialRepository);
    }

    @Test
    @DisplayName("Searching unexisting user should raise UserNotFoundException")
    public void testUserNotFoundException() {

        String user = "steve.jobs";
        String password = "password";

        Mockito.when(userCredentialRepository.isUserDefined(user)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> userCredentialService.areCredentialsCorrect(user, password)).
                isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should return false when passwords are different")
    public void testPasswordMismatch() throws Exception {

        String user = "steve.jobs";
        String password = "password";

        Mockito.when(userCredentialRepository.isUserDefined(user)).thenReturn(true);
        Mockito.when(userCredentialRepository.getPassword(user)).thenReturn("BAD");

        boolean actual = userCredentialService.areCredentialsCorrect(user, password);
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("Should return true when passwords are equal")
    public void testPasswordMatch() throws Exception {

        IEncryptorService encryptorService = EncryptorServiceProvider.getInstance().getFactory().createService();

        String user = "steve.jobs";
        String password = "password";

        Mockito.when(userCredentialRepository.isUserDefined(user)).thenReturn(true);
        Mockito.when(userCredentialRepository.getPassword(user)).thenReturn(encryptorService.encrypt(password));

        boolean actual = userCredentialService.areCredentialsCorrect(user, password);
        Assertions.assertThat(actual).isTrue();
    }
}
