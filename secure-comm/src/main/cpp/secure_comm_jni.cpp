#include <jni.h>
#include <android/log.h>
#include <string>
#include "crypto_engine.h"

#define LOG_TAG "SecureCommNative"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" /**
 * @brief Return the native Secure Communication component version string.
 *
 * Returns a Java UTF-8 string with the native library version ("Genesis Secure Comm V2.0.0").
 *
 * @return jstring Java string containing the version identifier.
 */
JNIEXPORT jstring

JNICALL
Java_dev_aurakai_auraframefx_securecomm_SecureCommNative_getVersion(
        JNIEnv *env,
        jobject /* this */) {

    LOGI("Secure Communication Native - Genesis Protocol v3.0");
    std::string version = "Genesis Secure Comm V2.0.0";
    return env->NewStringUTF(version.c_str());
}

extern "C" /**
 * @brief Initialize the native cryptographic subsystem used by SecureCommNative.
 *
 * Performs any required setup for the underlying crypto engine so subsequent
 * encrypt/decrypt operations can be used.
 *
 * @return jboolean JNI_TRUE on successful initialization, JNI_FALSE otherwise.
 */
JNIEXPORT jboolean

JNICALL
Java_dev_aurakai_auraframefx_securecomm_SecureCommNative_initializeCrypto(
        JNIEnv *env,
        jobject /* this */) {

    LOGI("Initializing Genesis Secure Communication...");
    return CryptoEngine::initialize();
}

extern "C" /**
 * @brief Encrypts a byte array using the provided key and returns the ciphertext.
 *
 * The function reads the bytes from the Java `data` array and the UTF-8 contents of
 * `key`, calls the native CryptoEngine to produce encrypted bytes, and returns a
 * newly-allocated Java `byte[]` containing the ciphertext.
 *
 * @param data Java `byte[]` containing the plaintext to encrypt. The input array is
 *             not modified; native access is released with `JNI_ABORT` (no copy-back).
 * @param key  Java `String` used as the encryption key (interpreted as UTF-8).
 * @return A new Java `byte[]` containing the encrypted data (ciphertext).
 */
JNIEXPORT jbyteArray

JNICALL
Java_dev_aurakai_auraframefx_securecomm_SecureCommNative_encrypt(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray data,
        jstring key) {

    // Get input data
    jsize dataLen = env->GetArrayLength(data);
    jbyte *dataBytes = env->GetByteArrayElements(data, nullptr);

    // Get key
    const char *keyStr = env->GetStringUTFChars(key, nullptr);

    // Perform encryption (placeholder)
    std::vector <uint8_t> encrypted = CryptoEngine::encrypt(
            reinterpret_cast<uint8_t *>(dataBytes), dataLen, keyStr
    );

    // Create result array
    jbyteArray result = env->NewByteArray(encrypted.size());
    env->SetByteArrayRegion(result, 0, encrypted.size(),
                            reinterpret_cast<const jbyte *>(encrypted.data()));

    // Cleanup
    env->ReleaseByteArrayElements(data, dataBytes, JNI_ABORT);
    env->ReleaseStringUTFChars(key, keyStr);

    return result;
}

extern "C" /**
 * @brief Decrypts a byte array using the provided UTF-8 key and returns the plaintext bytes.
 *
 * Decrypts the contents of `encryptedData` with `key` and returns a new Java byte[] containing
 * the decrypted bytes. The key is interpreted as a UTF-8 C string. If decryption produces an
 * empty payload, an empty Java byte[] is returned.
 *
 * @param encryptedData Java byte array containing the ciphertext to decrypt.
 * @param key UTF-8 Java string used as the decryption key.
 * @return jbyteArray A newly allocated Java byte array with the decrypted data.
 */
JNIEXPORT jbyteArray

JNICALL
Java_dev_aurakai_auraframefx_securecomm_SecureCommNative_decrypt(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray encryptedData,
        jstring key) {

    // Get input data
    jsize dataLen = env->GetArrayLength(encryptedData);
    jbyte *dataBytes = env->GetByteArrayElements(encryptedData, nullptr);

    // Get key
    const char *keyStr = env->GetStringUTFChars(key, nullptr);

    // Perform decryption (placeholder)
    std::vector <uint8_t> decrypted = CryptoEngine::decrypt(
            reinterpret_cast<uint8_t *>(dataBytes), dataLen, keyStr
    );

    // Create result array
    jbyteArray result = env->NewByteArray(decrypted.size());
    env->SetByteArrayRegion(result, 0, decrypted.size(),
                            reinterpret_cast<const jbyte *>(decrypted.data()));

    // Cleanup
    env->ReleaseByteArrayElements(encryptedData, dataBytes, JNI_ABORT);
    env->ReleaseStringUTFChars(key, keyStr);

    return result;
}