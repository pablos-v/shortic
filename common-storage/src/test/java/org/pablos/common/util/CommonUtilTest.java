package org.pablos.common.util;

import org.pablos.common.exception.*;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;

public class CommonUtilTest {

    public static final String SHORT_LINK_TOO_LONG = "LinkTooLong";
    public static final String SHORT_LINK_WITH_SPACE = "short L";
    public static final String SHORT_LINK_WITH_INVALID_CHARS = "shortL*/-+";
    public static final int LENGTH_OF_TOO_LONG_LINK = 4097;
    public static final String PASSWORD = "12345";

    @Test
    public void testValidateDTOShortLinkThrowsLinkProcessingException() {
        assertThatThrownBy(() -> CommonUtil.validateDTOShortLink(() -> SHORT_LINK_TOO_LONG))
                .isInstanceOf(LinkProcessingException.class);
        assertThatThrownBy(() -> CommonUtil.validateDTOShortLink(() -> SHORT_LINK_WITH_SPACE))
                .isInstanceOf(LinkProcessingException.class);
        assertThatThrownBy(() -> CommonUtil.validateDTOShortLink(() -> SHORT_LINK_WITH_INVALID_CHARS))
                .isInstanceOf(LinkProcessingException.class);
        assertThatThrownBy(() -> CommonUtil.validateDTOShortLink(() -> null))
                .isInstanceOf(LinkProcessingException.class);
    }

    @Test
    public void testValidateDTOShortLinkWithNull() {
        assertThatThrownBy(() -> CommonUtil.validateDTOShortLink(null))
                .isInstanceOf(ObjectNotProvidedException.class);
    }

    @Test
    public void testValidateDTOFullLinkWithNull() {
        assertThatThrownBy(() -> CommonUtil.validateDTOFullLink(() -> null))
                .isInstanceOf(FullLinkNotProvidedException.class);
        assertThatThrownBy(() -> CommonUtil.validateDTOFullLink(null))
                .isInstanceOf(FullLinkNotProvidedException.class);
    }

    @Test
    public void testValidateDTOFullLinkThrowsSizeException() {
        assertThatThrownBy(() -> CommonUtil.validateDTOFullLink(() -> generateTooLongLink()))
                .isInstanceOf(FullLinkSizeException.class);
    }
    private static String generateTooLongLink() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH_OF_TOO_LONG_LINK);
        for (int i = 0; i < LENGTH_OF_TOO_LONG_LINK; i++) {
            int index = random.nextInt(CommonUtil.CHARACTERS.length());
            sb.append(CommonUtil.CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Test
    public void testValidatePasswordSuccess() {
        assertThatCode(() -> CommonUtil.validatePassword(PASSWORD))
                .doesNotThrowAnyException();
    }

    @Test
    public void testValidatePasswordThrowsWrongInputException() {
        assertThatThrownBy(() -> CommonUtil.validatePassword("0" + PASSWORD))
                .isInstanceOf(WrongInputException.class);
        assertThatThrownBy(() -> CommonUtil.validatePassword(null))
                .isInstanceOf(WrongInputException.class);
    }

    @Test
    public void testEncodePassword() {
        String encoded = CommonUtil.encodePassword(PASSWORD);

        assertThat(encoded).isEqualTo(Base64.getEncoder().encodeToString(PASSWORD.getBytes()));
    }

    @Test
    public void testDecodePassword() {
        String decoded = CommonUtil.decodePassword(Base64.getEncoder().encodeToString(PASSWORD.getBytes()));

        assertThat(decoded).isEqualTo(PASSWORD);
    }

}




