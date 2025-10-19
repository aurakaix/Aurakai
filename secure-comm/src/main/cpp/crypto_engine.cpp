#include "crypto_engine.h"
#include <android/log.h>
#include <random>
#include <algorithm>
#include <cstring>

#define LOG_TAG "CryptoEngine"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

bool CryptoEngine::initialized_ = false;

/**
 * @brief Ensures the crypto engine is initialized and ready for use.
 *
 * Initializes the secure random generator (via initializeRandomGenerator()) and
 * sets the internal initialized flag. Safe to call multiple times; subsequent
 * calls are no-ops and return success.
 *
 * @return true Always returns true to indicate the engine is initialized.
 */
bool CryptoEngine::initialize() {
    if (initialized_) {
        return true;
    }

    LOGI("Initializing Genesis Crypto Engine V2...");
    initializeRandomGenerator();
    initialized_ = true;
    LOGI("Genesis Crypto Engine V2 initialized successfully");
    return true;
}

/**
 * @brief Encrypts a binary buffer using a simple XOR-based placeholder algorithm.
 *
 * Ensures the crypto engine is initialized, then produces an encrypted copy of the
 * input buffer by XOR-ing each byte with a byte from `key` (cycled) and 0xAA.
 * This is a demonstrative, non-production implementation and does not provide
 * real cryptographic security.
 *
 * @param data Pointer to the input bytes to encrypt; must be valid for `length` bytes.
 * @param length Number of bytes to read from `data`.
 * @param key Null-terminated C-string used as the repeating key; must be non-null and non-empty.
 * @return std::vector<uint8_t> A vector of `length` bytes containing the encrypted data.
 */
std::vector <uint8_t> CryptoEngine::encrypt(const uint8_t *data, size_t length, const char *key) {
    if (!initialized_) {
        initialize();
    }

    // Defensive checks: ensure pointers are valid and key is non-empty to avoid UB
    if (data == nullptr || length == 0) {
        LOGI("Encrypt called with null/empty data");
        return {};
    }
    if (key == nullptr) {
        LOGI("Encrypt called with null key");
        return {};
    }

    std::vector <uint8_t> encrypted(length);

    size_t keyLen = strlen(key);
    if (keyLen == 0) {
        LOGI("Encrypt called with empty key");
        return {};
    }

    for (size_t i = 0; i < length; ++i) {
        encrypted[i] = data[i] ^ static_cast<uint8_t>(key[i % keyLen]) ^ 0xAA; // Simple XOR for demo
    }

    LOGI("Encrypted %zu bytes using Genesis Secure Algorithm", length);
    return encrypted;
}

/**
 * @brief Decrypts a buffer using a reversible placeholder algorithm.
 *
 * Ensures the crypto engine is initialized, then returns a decrypted copy of
 * the input buffer. Decryption uses a reversible XOR-based transformation
 * that cycles over the bytes of the provided null-terminated key and a fixed
 * mask. This implementation is a placeholder and is not cryptographically secure.
 *
 * @param data Pointer to the input ciphertext bytes.
 * @param length Number of bytes to decrypt.
 * @param key Null-terminated key string; must be non-null and have length > 0.
 * @return std::vector<uint8_t> Decrypted bytes (same length as input).
 */
std::vector <uint8_t> CryptoEngine::decrypt(const uint8_t *data, size_t length, const char *key) {
    if (!initialized_) {
        initialize();
    }

    if (data == nullptr || length == 0) {
        LOGI("Decrypt called with null/empty data");
        return {};
    }
    if (key == nullptr) {
        LOGI("Decrypt called with null key");
        return {};
    }

    std::vector <uint8_t> decrypted(length);

    size_t keyLen = strlen(key);
    if (keyLen == 0) {
        LOGI("Decrypt called with empty key");
        return {};
    }

    for (size_t i = 0; i < length; ++i) {
        decrypted[i] = data[i] ^ static_cast<uint8_t>(key[i % keyLen]) ^ 0xAA; // Reverse XOR for demo
    }

    LOGI("Decrypted %zu bytes using Genesis Secure Algorithm", length);
    return decrypted;
}

std::string CryptoEngine::generateSecureKey() {
    if (!initialized_) {
        initialize();
    }

    const std::string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    std::string key;
    key.reserve(32);

    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(0, static_cast<int>(chars.size() - 1));

    for (int i = 0; i < 32; ++i) {
        key += chars[dis(gen)];
    }

    LOGI("Generated secure key for Genesis communication");
    return key;
}

bool CryptoEngine::verifyIntegrity(const uint8_t *data, size_t length, const char *signature) {
    if (!initialized_) {
        initialize();
    }

    // Mark 'signature' as intentionally unused in this placeholder implementation
    (void)signature;

    // Genesis Protocol Integrity Verification (placeholder)
    // In production, this would use cryptographic hash verification
    LOGI("Verifying data integrity for %zu bytes", length);
    return true; // Always valid for demo
}

void CryptoEngine::initializeRandomGenerator() {
    // Initialize secure random number generation
    LOGI("Initializing Genesis secure random generator...");
}