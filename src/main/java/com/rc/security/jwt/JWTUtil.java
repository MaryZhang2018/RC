package com.rc.security.jwt;

import com.nimbusds.jose.Header;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.*;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

public class JWTUtil {

  // It should be separate Key management, just simple hack here
  private static HashMap<String, PublicKey> publicKeys = new HashMap<>();

  // Process (parse/validate/verify) the JWT as Bearer token string
  public static String process(String jwt) {

    JWT jwtObj = parse(jwt);
    // if(jwtObj instanceof PlainJWT)
    // common validation for all types of JWT (JWS|JWE)
    if(false == validate(jwt, jwtObj)) {
      // log and throw exception
      return null;
    }
    String encodedData = jwtObj.getParsedParts()[1].toString();

    if (jwtObj instanceof SignedJWT) {
      if(false == verify((SignedJWT)jwtObj)) {
        // log and throw exception
        return null;
      }
    } else if (jwtObj instanceof EncryptedJWT) {

    }

    // the encoded data or payload in Base64
    return encodedData;
  }

  public static boolean validate(String jwt, JWT jwtObj) {
    boolean valid = false;

    // validating per RFC7519, some may be done by the JWT lib already?
    if (jwtObj.getParsedParts().length < 2) return  false;

    // Header as first part, Base64 encoding and UTF8 String
    // all done by the JWT lib already
    // Here just do some verification of the parsing?

    // First part = Header
    Base64URL origHeader = jwtObj.getParsedParts()[0];
    Header header = jwtObj.getHeader();
    valid = origHeader.equals(header.toBase64URL());

    // more checks here? supported params?
    // jwtObj.getHeader().getIncludedParams()
    //valid &= (null != header.getAlgorithm());
    //valid &= (null != header.getContentType());

    return valid;
  }

  // parse the JWT string to header.payload.signature
  // Base64 Decode each part to JSON string
  // Construct each part from JSON string to class using Jackson

  public static JWT parse(String jwt) {
    JWT jwtObj = null;
    try {
      jwtObj = JWTParser.parse(jwt);
    } catch (java.text.ParseException pe) {
      // log
    }

    return jwtObj;
  }


  public static boolean verify(SignedJWT signedJWT) {
    try {

      String keyId = signedJWT.getHeader().getKeyID();
      if (keyId == null) {
        throw new SecurityException("No KeyId found in header");
      }

      // we should get the public key from the key manager or store
      // using the KeyID
      PublicKey publicKey = publicKeys.get(keyId);

      JWSVerifier verifier;
      if (publicKey.getAlgorithm().equalsIgnoreCase("RSA")) {
        verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
      } else if (publicKey.getAlgorithm().equalsIgnoreCase("EC")) {
        verifier = new ECDSAVerifier((ECPublicKey) publicKey);
      } else {
        throw new SecurityException(
                "Token signing algorithm not supported: " + publicKey.getAlgorithm());
      }
      signedJWT.verify(verifier);
      JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

      // expiration check
      Date exp = claimsSet.getExpirationTime();
      if (exp.before(new Date())) {
        throw new SecurityException("Token has expired: " + exp);
      }

      // verify claims
    } catch (ParseException e) {
      throw new SecurityException("Failed to parse token", e);
    } catch (JOSEException e) {
      throw new SecurityException("Failed to validate token", e);
    }

    return true;
  }
}
