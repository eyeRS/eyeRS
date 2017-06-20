package com.github.eyers;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO: comment code.
 * <p>
 * Created by Matthew on 2017/06/19.
 */
public final class EyeRS {

    /**
     *
     */
    public static final String PREFS_NAME = "EyeRS";
    /**
     *
     */
    public static EyeRS app;


    /**
     * @param context
     */
    public EyeRS(Context context) {
    }

    /**
     * @param password
     * @return
     * @throws RuntimeException No Such Algorithm Exception
     */
    public static String sha256(String password) throws RuntimeException {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passHash = sha256.digest((password + "0c@RFe-5G47|GTN").getBytes());

            StringBuilder str = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                str.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }

            return str.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * TODO.
     *
     * @return <code>this</code> for chaining
     */
    @Deprecated
    public EyeRS log() {
        return this;
    }
}
