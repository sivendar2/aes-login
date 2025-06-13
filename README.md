
#### 🔐 **AES Encryption (GCM mode)** – Mind Map

**1. Input**

* Plain Text (e.g., email, phone number)

**2. Key Material**

* Secret Key (256-bit)
* IV / Nonce (12 bytes for GCM)

**3. Algorithm**

* AES/GCM/NoPadding
* Cipher initialized in `ENCRYPT_MODE`

**4. Encryption Process**

* Key + IV → Cipher → `cipher.doFinal(plaintext)`
* Result = Ciphertext

**5. Output**

* Combine IV + Ciphertext → Base64 encode → Encrypted string

#### 🔓 **AES Decryption (GCM mode)** – Mind Map

**1. Input**

* Encrypted Base64 String

**2. Extraction**

* Decode Base64 → Extract IV and Ciphertext

**3. Key Material**

* Same Secret Key
* Extracted IV

**4. Algorithm**

* AES/GCM/NoPadding
* Cipher initialized in `DECRYPT_MODE`

**5. Decryption Process**

* `cipher.doFinal(ciphertext)`
* Output = Original Plaintext

---

### RSA
Here is a concise **RSA Encryption Mind Map** to help you visualize the key components and flow:

---

### 🧠 **RSA Encryption – Mind Map**

```
                            +---------------------+
                            |    RSA Encryption   |
                            +---------------------+
                                      |
              +-----------------------+------------------------+
              |                        |                        |
        🔐 Key Generation         🔄 Encryption             🔓 Decryption
              |                        |                        |
     - Generate key pair         - Use public key         - Use private key
       (public/private)            to encrypt               to decrypt
     - 2048/4096 bits             - Often used for        - Recovers original
     - Using KeyPairGenerator      short messages           message
              |
              v
  +----------------------------+
  | Public Key vs Private Key |
  +----------------------------+
  |  🔓 Public: Share openly   |
  |  🔐 Private: Keep secure   |
  +----------------------------+

                |
        +------------------+
        |    Use Cases     |
        +------------------+
        | ✔ Digital Signatures |
        | ✔ Secure Key Exchange|
        | ✔ JWT (RS256)         |
        | ✔ TLS/SSL             |
        +------------------+

                |
        +----------------------+
        |  Format & Standards  |
        +----------------------+
        | - PKCS#1 / PKCS#8    |
        | - PEM / DER encoding |
        | - X.509 Certificate  |
        +----------------------+
You can generate **RSA private and public key pairs** using several tools and approaches. Here are the **most common methods**:

---

### ✅ **1. Using OpenSSL (most widely used in real-world systems)**

#### 🔐 Generate 2048-bit Private Key:

```bash
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
```

#### 📤 Extract Public Key from Private Key:

```bash
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

> 🔒 Files created:

* `private_key.pem` → Keep this safe and secret.
* `public_key.pem` → Share this to verify signatures or encrypt data.

---

### ✅ **2. Using Java KeyTool (JDK installed)**

```bash
keytool -genkeypair -alias mykey -keyalg RSA -keysize 2048 -keystore mykeystore.jks
```

To export public key:

```bash
keytool -export -alias mykey -file public_key.der -keystore mykeystore.jks
```

Convert `.der` to `.pem` (optional):

```bash
openssl x509 -inform der -in public_key.der -out public_key.pem
```

---

### ✅ **3. Using Java Programmatically (in code)**

```java
KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
keyGen.initialize(2048);
KeyPair pair = keyGen.generateKeyPair();
PrivateKey privateKey = pair.getPrivate();
PublicKey publicKey = pair.getPublic();
```

You can then encode and store the keys using:

```java
Base64.getEncoder().encodeToString(privateKey.getEncoded())
```

---

### ✅ **4. Using Linux `ssh-keygen` (for SSH, not JWT usually)**

```bash
ssh-keygen -t rsa -b 2048 -f rsa_key
```

Creates:

* `rsa_key` (private)
* `rsa_key.pub` (public)

---

### 🧠 Tip:

Use **PEM (Base64)** format for most Java and JWT RS256 use cases:

* Begin with `-----BEGIN PRIVATE KEY-----`
* End with `-----END PRIVATE KEY-----`

---

Would you like a **ready-made Java utility** to read PEM files and sign/verify using RS256?




