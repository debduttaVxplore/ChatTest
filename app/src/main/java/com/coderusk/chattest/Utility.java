package com.coderusk.chattest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class Utility {
    public static <T> T objectify(String json, Class<T> classOfT){
        try {
            Gson gson = new Gson();
            Object object = gson.fromJson(json, classOfT);
            return classOfT.cast(object);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCongestedCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void playSoundFile(Context context,@RawRes int audio)
    {
        final MediaPlayer mp = MediaPlayer.create(context, audio);
        mp.setOnCompletionListener(mp1 -> mp1.release());
        mp.start();
    }

    /*public static void setViewBackground(Context context, View v, @DrawableRes int id)
    {
        if(context!=null)
        {
            if(v!=null)
            {
                if(id!=0)
                {
                    Drawable drawable = context.getResources().getDrawable(id);
                    if(drawable!=null)
                    {
                        Padding padding = new Padding(v);
                        v.setBackground(drawable);
                        padding.set(v);
                    }
                }
            }
        }
    }*/

    public static SecretKey generateKey(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return new SecretKeySpec(password.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidParameterSpecException,
            IllegalBlockSizeException,
            BadPaddingException,
            UnsupportedEncodingException
    {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidParameterSpecException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException
    {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

}
