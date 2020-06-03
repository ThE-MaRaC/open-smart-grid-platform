package org.opensmartgridplatform.secretmgmt.application.services.encryption.providers;

import org.opensmartgridplatform.secretmgmt.application.services.encryption.EncryptedSecret;
import org.opensmartgridplatform.secretmgmt.application.services.encryption.Secret;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class AbstractEncryptionProvider {

    private static final Logger LOGGER = Logger.getLogger(AbstractEncryptionProvider.class.getName());

    public abstract EncryptionProviderType getType();
    protected abstract Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException;
    protected abstract AlgorithmParameterSpec getAlgorithmParameterSpec();
    protected abstract Key getSecretEncryptionKey() throws Exception;
    protected abstract int getIVLength();

    public EncryptedSecret encrypt(Secret secret) throws Exception {
        try {
            final Cipher cipher = this.getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, this.getSecretEncryptionKey(), this.getAlgorithmParameterSpec());
            return new EncryptedSecret(this.getType(), cipher.doFinal(secret.getSecret()));
        } catch (Exception e) {
            //InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
            //NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException
            LOGGER.warning(e.getMessage());
            throw e;
        }
    }

    public Secret decrypt(EncryptedSecret secret) throws Exception {

        if (secret.getEncryptionProviderType() != this.getType()) {
            throw new IllegalStateException("EncryptionProvider for type " + this.getType().name() + " cannot decrypt secrets of type " + secret.getEncryptionProviderType().name());
        }

        try {
            final Cipher cipher = this.getCipher();
            cipher.init(Cipher.DECRYPT_MODE, this.getSecretEncryptionKey(), this.getAlgorithmParameterSpec());
            final byte[] decryptedData = cipher.doFinal(secret.getSecret());

            if (this.checkNullBytesPrepended(decryptedData)) {
                return new Secret(Arrays.copyOfRange(decryptedData, this.getIVLength(), decryptedData.length));
            } else {
                return new Secret(decryptedData);
            }
        } catch (Exception e) {
            //InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
            //NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException
            LOGGER.warning(e.getMessage());
            throw e;
        }
    }

    /**
     * - When aes decrypting data (both Java / bouncy castle and openssl) sometimes 16 0 bytes are prepended.
     * - Possibly this has to do with padding during encryption
     * - openssl as well as Java / bouncy castle don't prefix iv bytes when aes encrypting data (seen in junit test and commandline)
     * - makeSimulatorKey.sh (device simulator) assumes decrypted data are prepended with 0 bytes, at present this is correct
     *
     * @param bytes
     *            the array to check
     * @return true if the array is prepended with 0 bytes, false otherwise
     */
    private boolean checkNullBytesPrepended(final byte[] bytes) {
        int l = this.getIVLength();
        if (bytes.length > l) {
            boolean nullBytesPrepended = false;
            for (short s = 0; s < l; s++) {
                if (bytes[s] == 0) {
                    nullBytesPrepended = true;
                } else {
                    return false;
                }
            }
            return nullBytesPrepended;
        } else {
            return false;
        }
    }
}