package cc.telepath.rhizome

import org.bouncycastle.*
import org.bouncycastle.util.encoders.Base64Encoder
import java.security.*
import java.util.*

import javax.crypto.Cipher


    /**
     * Generate an RSA Keypair
     */
    fun generateKeypair(): KeyPair{
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
        kpg.initialize(4096)
        val newKeyPair: KeyPair = kpg.generateKeyPair()
        return newKeyPair
    }

    /**
     * Sign the base64 encoding of a message. Return Base64 representation of signature.
     */
    fun signMessage(signingKey: PrivateKey, message: String): String{
        val sig: Signature = Signature.getInstance("SHA512withRSA")
        sig.initSign(signingKey)
        val b64Encoder: Base64.Encoder = Base64.getEncoder()
        sig.update(b64Encoder.encode(message.toByteArray()))
        return String(b64Encoder.encode(sig.sign()))
    }

    /**
     * Takes a base64 encoded signature and verifies that it was created by a given public key
     */
    fun verifyMessage(signingKey: PublicKey, message: String,  signature: String) : Boolean{
        val sig: Signature = Signature.getInstance("SHA512withRSA")
        val b64Decoder: Base64.Decoder = Base64.getDecoder()
        val base64Encoder: Base64.Encoder = Base64.getEncoder()
        sig.initVerify(signingKey)
        sig.update(base64Encoder.encode(message.toByteArray()))
        return sig.verify(b64Decoder.decode(signature))
    }
