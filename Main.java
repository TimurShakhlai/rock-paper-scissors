package com.company;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Main {
    public static SecureRandom random = new SecureRandom();
    public static int PCmove = 0;
    public static String codemsg = null;
    public static String Key = null;
    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
        int lntg = args.length;
        int excpt = 0;
        Set set = new HashSet();
        for (String move : args) {
            if (!set.add(move)) {
                excpt = 1;
            }
        }
        if (lntg % 2 == 0 || lntg < 2 || excpt == 1) {
            System.out.println("An error, an even number of options, or options less than 3, or among the options there are repetitions!");
            System.out.println("Right example: rock paper scissors i love Travis Scott");
            return;
        }
        PCmove = random.nextInt(args.length-1);
        codemsg = args[PCmove];
        Key = KeyGen();
        Crypting(Key);
        AppInterface(args);
    }
    public static void AppInterface(String[] args){
        int Human_move;
        int checker = 0;
        do {
            System.out.println("Available moves:");
            for (int i = 0; i<args.length; i++){
                System.out.println(i+1+". "+ args[i]);
            }
            System.out.println("0. Exit game");
            System.out.println("Enter your move:");
            Scanner sc = new Scanner(System.in);
            Human_move = sc.nextInt() - 1;
            if (Human_move == -1) {
                return;
            }
            if (Human_move > -1 & Human_move < args.length) {
                checker = 1;
            }
        } while (checker ==0);
        System.out.println("Your move:"+args[Human_move]);
        WinCheck(Human_move, PCmove, args.length/2);
        System.out.println("Computer move:"+codemsg);
        System.out.println("HMAC key:"+Key);
    }
    public static void WinCheck(int HM, int PCM, int HL) {
        if (HM == PCM){
            System.out.println("DRAW!");
        }
        if(HM-PCM<0 | HM-PCM>HL){
            System.out.println("YOU LOSE!");
        } else if (HM-PCM>0 & HM-PCM<=HL){
            System.out.println("YOU WIN!");
        }
    }
    public static String KeyGen() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return byteArrayToHex(bytes);
    }
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public static void Crypting(String Secret) throws NoSuchAlgorithmException, InvalidKeyException {

        final Charset asciiCs = Charset.forName("US-ASCII");
        final Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        final SecretKeySpec secret_key = new SecretKeySpec(asciiCs.encode(Secret).array(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        final byte[] mac_data = sha256_HMAC.doFinal(asciiCs.encode(codemsg).array());
        StringBuilder hmac = new StringBuilder();
        for (final byte element : mac_data)
        {
            hmac.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println("HMAC:" + hmac);
    }
}