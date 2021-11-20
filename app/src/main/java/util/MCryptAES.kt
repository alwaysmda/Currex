package util

import android.util.Base64
import java.security.Key
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MCryptAES(key: String, bit: Int = 128, iv: String? = null) {
    /**
     * Preset Initialization Vector，16 Bits Zero
     */
    private val DEFAULT_IV = IvParameterSpec(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

    /**
     * Using AES
     */
    private val ALGORITHM = "AES"

    /**
     * AES Using CBC mode and PKCS5Padding
     */
    private val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    /**
     * AES En/Decrypt Key
     */
    private var key: Key? = null

    /**
     * AES CBC Mode Using Initialization Vector
     */
    private var iv: IvParameterSpec? = null

    /**
     * Cipher
     */
    private var cipher: Cipher? = null

    /**
     * @param key AES key
     * @param bit AEK key length 128 or 256 (Bits)
     * @param iv  IVString
     */
    init {
        if (bit == 256) {
            this.key = SecretKeySpec(getHash("SHA-256", key), ALGORITHM)
        } else {
            this.key = SecretKeySpec(getHash("MD5", key), ALGORITHM)
        }
        if (iv != null) {
            this.iv = IvParameterSpec(getHash("MD5", iv))
        } else {
            this.iv = DEFAULT_IV
        }

        init()
    }

    /**
     * Using 128 Bits AES md5 and default IV
     *
     * @param key AES key
     */

    /**
     * get string hash
     *
     * @param algorithm Hash type
     * @param text      the string your want to hash
     * @return hash
     */
    private fun getHash(algorithm: String, text: String): ByteArray {
        try {
            return getHash(algorithm, text.toByteArray(Charsets.UTF_8))
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }

    }

    /**
     * get string hash
     *
     * @param algorithm Hash type
     * @param data      the bytes your want to hash
     * @return Hash bytes
     */
    private fun getHash(algorithm: String, data: ByteArray): ByteArray {
        try {
            val digest = MessageDigest.getInstance(algorithm)
            digest.update(data)
            return digest.digest()
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }

    }

    /**
     * initiate
     */
    private fun init() {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION)
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }

    }

    /**
     * encrypt string
     *
     * @param str string
     * @return encrypted string
     */
    fun encrypt(str: String): String {
        try {
            return encrypt(str.toByteArray(Charsets.UTF_8))
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }

    }

    /**
     * encrypt bytes
     *
     * @param data the bytes your want to encrypt
     * @return encrypted string
     */
    fun encrypt(data: ByteArray): String {
        try {
            cipher!!.init(Cipher.ENCRYPT_MODE, key, iv)
            val encryptData = cipher!!.doFinal(data)
            return String(Base64.encode(encryptData, Base64.NO_WRAP), Charsets.UTF_8)
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }

    }

    /**
     * decrypt
     *
     * @param str the string need to decrypt
     * @return string
     */
    fun decrypt(str: String): String {
        try {
            return decrypt(Base64.decode(str, Base64.NO_WRAP))
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }

    }

    /**
     * decrypt data
     *
     * @param data the bytes your want to decrypt
     * @return string
     */
    fun decrypt(data: ByteArray): String {
        try {
            cipher!!.init(Cipher.DECRYPT_MODE, key, iv)
            val decryptData = cipher!!.doFinal(data)
            return String(decryptData, Charsets.UTF_8)
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }

    }
}