package com.csii.sh.core.crypto;

import java.util.ArrayList;
import java.util.List;

public class DES3Util {
    public DES3Util() {
    }

    public String encode(String data, String firstKey, String secondKey, String thirdKey) {
        int leng = data.length();
        String encData = "";
        List firstKeyBt = null;
        List secondKeyBt = null;
        List thirdKeyBt = null;
        int firstLength = 0;
        int secondLength = 0;
        int thirdLength = 0;
        if(firstKey != null && firstKey != "") {
            firstKeyBt = this.getKeyBytes(firstKey);
            firstLength = firstKeyBt.size();
        }

        if(secondKey != null && secondKey != "") {
            secondKeyBt = this.getKeyBytes(secondKey);
            secondLength = secondKeyBt.size();
        }

        if(thirdKey != null && thirdKey != "") {
            thirdKeyBt = this.getKeyBytes(thirdKey);
            thirdLength = thirdKeyBt.size();
        }

        if(leng > 0) {
            if(leng < 4) {
                int[] iterator = this.strToBt(data);
                int[] remainder = null;
                int[] i;
                int tempByte;
                int var25;
                if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "" && thirdKey != null && thirdKey != "") {
                    i = iterator;

                    for(var25 = 0; var25 < firstLength; ++var25) {
                        i = this.enc(i, (int[])firstKeyBt.get(var25));
                    }

                    for(tempByte = 0; tempByte < secondLength; ++tempByte) {
                        i = this.enc(i, (int[])secondKeyBt.get(tempByte));
                    }

                    for(int encByte = 0; encByte < thirdLength; ++encByte) {
                        i = this.enc(i, (int[])thirdKeyBt.get(encByte));
                    }

                    remainder = i;
                } else if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "") {
                    i = iterator;

                    for(var25 = 0; var25 < firstLength; ++var25) {
                        i = this.enc(i, (int[])firstKeyBt.get(var25));
                    }

                    for(tempByte = 0; tempByte < secondLength; ++tempByte) {
                        i = this.enc(i, (int[])secondKeyBt.get(tempByte));
                    }

                    remainder = i;
                } else if(firstKey != null && firstKey != "") {
                    boolean remainderData = false;
                    i = iterator;

                    for(var25 = 0; var25 < firstLength; ++var25) {
                        i = this.enc(i, (int[])firstKeyBt.get(var25));
                    }

                    remainder = i;
                }

                encData = this.bt64ToHex(remainder);
            } else {
                int var23 = leng / 4;
                int var24 = leng % 4;
                boolean var26 = false;

                int[] tempBt;
                int x;
                int y;
                int z;
                String var28;
                int[] var29;
                int[] var30;
                for(int var27 = 0; var27 < var23; ++var27) {
                    var28 = data.substring(var27 * 4 + 0, var27 * 4 + 4);
                    var29 = this.strToBt(var28);
                    var30 = null;
                    if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "" && thirdKey != null && thirdKey != "") {
                        tempBt = var29;

                        for(x = 0; x < firstLength; ++x) {
                            tempBt = this.enc(tempBt, (int[])firstKeyBt.get(x));
                        }

                        for(y = 0; y < secondLength; ++y) {
                            tempBt = this.enc(tempBt, (int[])secondKeyBt.get(y));
                        }

                        for(z = 0; z < thirdLength; ++z) {
                            tempBt = this.enc(tempBt, (int[])thirdKeyBt.get(z));
                        }

                        var30 = tempBt;
                    } else if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "") {
                        tempBt = var29;

                        for(x = 0; x < firstLength; ++x) {
                            tempBt = this.enc(tempBt, (int[])firstKeyBt.get(x));
                        }

                        for(y = 0; y < secondLength; ++y) {
                            tempBt = this.enc(tempBt, (int[])secondKeyBt.get(y));
                        }

                        var30 = tempBt;
                    } else if(firstKey != null && firstKey != "") {
                        tempBt = var29;

                        for(x = 0; x < firstLength; ++x) {
                            tempBt = this.enc(tempBt, (int[])firstKeyBt.get(x));
                        }

                        var30 = tempBt;
                    }

                    encData = encData + this.bt64ToHex(var30);
                }

                if(var24 > 0) {
                    var28 = data.substring(var23 * 4 + 0, leng);
                    var29 = this.strToBt(var28);
                    var30 = null;
                    if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "" && thirdKey != null && thirdKey != "") {
                        tempBt = var29;

                        for(x = 0; x < firstLength; ++x) {
                            tempBt = this.enc(tempBt, (int[])firstKeyBt.get(x));
                        }

                        for(y = 0; y < secondLength; ++y) {
                            tempBt = this.enc(tempBt, (int[])secondKeyBt.get(y));
                        }

                        for(z = 0; z < thirdLength; ++z) {
                            tempBt = this.enc(tempBt, (int[])thirdKeyBt.get(z));
                        }

                        var30 = tempBt;
                    } else if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "") {
                        tempBt = var29;

                        for(x = 0; x < firstLength; ++x) {
                            tempBt = this.enc(tempBt, (int[])firstKeyBt.get(x));
                        }

                        for(y = 0; y < secondLength; ++y) {
                            tempBt = this.enc(tempBt, (int[])secondKeyBt.get(y));
                        }

                        var30 = tempBt;
                    } else if(firstKey != null && firstKey != "") {
                        tempBt = var29;

                        for(x = 0; x < firstLength; ++x) {
                            tempBt = this.enc(tempBt, (int[])firstKeyBt.get(x));
                        }

                        var30 = tempBt;
                    }

                    encData = encData + this.bt64ToHex(var30);
                }
            }
        }

        return encData;
    }

    public String decode(String data, String firstKey, String secondKey, String thirdKey) {
        int leng = data.length();
        String decStr = "";
        List firstKeyBt = null;
        List secondKeyBt = null;
        List thirdKeyBt = null;
        int firstLength = 0;
        int secondLength = 0;
        int thirdLength = 0;
        if(firstKey != null && firstKey != "") {
            firstKeyBt = this.getKeyBytes(firstKey);
            firstLength = firstKeyBt.size();
        }

        if(secondKey != null && secondKey != "") {
            secondKeyBt = this.getKeyBytes(secondKey);
            secondLength = secondKeyBt.size();
        }

        if(thirdKey != null && thirdKey != "") {
            thirdKeyBt = this.getKeyBytes(thirdKey);
            thirdLength = thirdKeyBt.size();
        }

        int iterator = leng / 16;
        boolean i = false;

        for(int var24 = 0; var24 < iterator; ++var24) {
            String tempData = data.substring(var24 * 16 + 0, var24 * 16 + 16);
            String strByte = this.hexToBt64(tempData);
            int[] intByte = new int[64];
            boolean j = false;

            for(int var25 = 0; var25 < 64; ++var25) {
                intByte[var25] = Integer.parseInt(strByte.substring(var25, var25 + 1));
            }

            int[] decByte = null;
            int[] tempBt;
            int x;
            int y;
            if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "" && thirdKey != null && thirdKey != "") {
                tempBt = intByte;

                for(x = thirdLength - 1; x >= 0; --x) {
                    tempBt = this.dec(tempBt, (int[])thirdKeyBt.get(x));
                }

                for(y = secondLength - 1; y >= 0; --y) {
                    tempBt = this.dec(tempBt, (int[])secondKeyBt.get(y));
                }

                for(int z = firstLength - 1; z >= 0; --z) {
                    tempBt = this.dec(tempBt, (int[])firstKeyBt.get(z));
                }

                decByte = tempBt;
            } else if(firstKey != null && firstKey != "" && secondKey != null && secondKey != "") {
                tempBt = intByte;

                for(x = secondLength - 1; x >= 0; --x) {
                    tempBt = this.dec(tempBt, (int[])secondKeyBt.get(x));
                }

                for(y = firstLength - 1; y >= 0; --y) {
                    tempBt = this.dec(tempBt, (int[])firstKeyBt.get(y));
                }

                decByte = tempBt;
            } else if(firstKey != null && firstKey != "") {
                tempBt = intByte;

                for(x = firstLength - 1; x >= 0; --x) {
                    tempBt = this.dec(tempBt, (int[])firstKeyBt.get(x));
                }

                decByte = tempBt;
            }

            decStr = decStr + this.byteToString(decByte);
        }

        return decStr;
    }

    private List getKeyBytes(String key) {
        ArrayList keyBytes = new ArrayList();
        int leng = key.length();
        int iterator = leng / 4;
        int remainder = leng % 4;
        boolean i = false;

        int var7;
        for(var7 = 0; var7 < iterator; ++var7) {
            keyBytes.add(var7, this.strToBt(key.substring(var7 * 4 + 0, var7 * 4 + 4)));
        }

        if(remainder > 0) {
            keyBytes.add(var7, this.strToBt(key.substring(var7 * 4 + 0, leng)));
        }

        return keyBytes;
    }

    private int[] strToBt(String str) {
        int leng = str.length();
        int[] bt = new int[64];
        int var11;
        int var14;
        int var15;
        if(leng < 4) {
            boolean i = false;
            boolean k = false;
            boolean j = false;
            boolean pow = false;

            int pow1;
            boolean m1;
            int var18;
            for(var11 = 0; var11 < leng; ++var11) {
                char m = str.charAt(var11);

                for(int var12 = 0; var12 < 16; ++var12) {
                    pow1 = 1;
                    m1 = false;

                    for(var18 = 15; var18 > var12; --var18) {
                        pow1 *= 2;
                    }

                    bt[16 * var11 + var12] = m / pow1 % 2;
                }
            }

            for(var14 = leng; var14 < 4; ++var14) {
                byte var16 = 0;

                for(var15 = 0; var15 < 16; ++var15) {
                    pow1 = 1;
                    m1 = false;

                    for(var18 = 15; var18 > var15; --var18) {
                        pow1 *= 2;
                    }

                    bt[16 * var14 + var15] = var16 / pow1 % 2;
                }
            }
        } else {
            for(var11 = 0; var11 < 4; ++var11) {
                char var13 = str.charAt(var11);

                for(var14 = 0; var14 < 16; ++var14) {
                    var15 = 1;

                    for(int var17 = 15; var17 > var14; --var17) {
                        var15 *= 2;
                    }

                    bt[16 * var11 + var14] = var13 / var15 % 2;
                }
            }
        }

        return bt;
    }

    private String bt4ToHex(String binary) {
        String hex = "";
        if(binary.equalsIgnoreCase("0000")) {
            hex = "0";
        } else if(binary.equalsIgnoreCase("0001")) {
            hex = "1";
        } else if(binary.equalsIgnoreCase("0010")) {
            hex = "2";
        } else if(binary.equalsIgnoreCase("0011")) {
            hex = "3";
        } else if(binary.equalsIgnoreCase("0100")) {
            hex = "4";
        } else if(binary.equalsIgnoreCase("0101")) {
            hex = "5";
        } else if(binary.equalsIgnoreCase("0110")) {
            hex = "6";
        } else if(binary.equalsIgnoreCase("0111")) {
            hex = "7";
        } else if(binary.equalsIgnoreCase("1000")) {
            hex = "8";
        } else if(binary.equalsIgnoreCase("1001")) {
            hex = "9";
        } else if(binary.equalsIgnoreCase("1010")) {
            hex = "A";
        } else if(binary.equalsIgnoreCase("1011")) {
            hex = "B";
        } else if(binary.equalsIgnoreCase("1100")) {
            hex = "C";
        } else if(binary.equalsIgnoreCase("1101")) {
            hex = "D";
        } else if(binary.equalsIgnoreCase("1110")) {
            hex = "E";
        } else if(binary.equalsIgnoreCase("1111")) {
            hex = "F";
        }

        return hex;
    }

    private String hexToBt4(String hex) {
        String binary = "";
        if(hex.equalsIgnoreCase("0")) {
            binary = "0000";
        } else if(hex.equalsIgnoreCase("1")) {
            binary = "0001";
        }

        if(hex.equalsIgnoreCase("2")) {
            binary = "0010";
        }

        if(hex.equalsIgnoreCase("3")) {
            binary = "0011";
        }

        if(hex.equalsIgnoreCase("4")) {
            binary = "0100";
        }

        if(hex.equalsIgnoreCase("5")) {
            binary = "0101";
        }

        if(hex.equalsIgnoreCase("6")) {
            binary = "0110";
        }

        if(hex.equalsIgnoreCase("7")) {
            binary = "0111";
        }

        if(hex.equalsIgnoreCase("8")) {
            binary = "1000";
        }

        if(hex.equalsIgnoreCase("9")) {
            binary = "1001";
        }

        if(hex.equalsIgnoreCase("A")) {
            binary = "1010";
        }

        if(hex.equalsIgnoreCase("B")) {
            binary = "1011";
        }

        if(hex.equalsIgnoreCase("C")) {
            binary = "1100";
        }

        if(hex.equalsIgnoreCase("D")) {
            binary = "1101";
        }

        if(hex.equalsIgnoreCase("E")) {
            binary = "1110";
        }

        if(hex.equalsIgnoreCase("F")) {
            binary = "1111";
        }

        return binary;
    }

    private String byteToString(int[] byteData) {
        String str = "";

        for(int i = 0; i < 4; ++i) {
            int count = 0;

            for(int j = 0; j < 16; ++j) {
                int pow = 1;

                for(int m = 15; m > j; --m) {
                    pow *= 2;
                }

                count += byteData[16 * i + j] * pow;
            }

            if(count != 0) {
                str = str + (char)count;
            }
        }

        return str;
    }

    private String bt64ToHex(int[] byteData) {
        String hex = "";

        for(int i = 0; i < 16; ++i) {
            String bt = "";

            for(int j = 0; j < 4; ++j) {
                bt = bt + byteData[i * 4 + j];
            }

            hex = hex + this.bt4ToHex(bt);
        }

        return hex;
    }

    private String hexToBt64(String hex) {
        String binary = "";

        for(int i = 0; i < 16; ++i) {
            binary = binary + this.hexToBt4(hex.substring(i, i + 1));
        }

        return binary;
    }

    private int[] enc(int[] dataByte, int[] keyByte) {
        int[][] keys = this.generateKeys(keyByte);
        int[] ipByte = this.initPermute(dataByte);
        int[] ipLeft = new int[32];
        int[] ipRight = new int[32];
        int[] tempLeft = new int[32];
        boolean i = false;
        boolean j = false;
        boolean k = false;
        boolean m = false;
        boolean n = false;

        for(int var17 = 0; var17 < 32; ++var17) {
            ipLeft[var17] = ipByte[var17];
            ipRight[var17] = ipByte[32 + var17];
        }

        int[] finalData;
        int var15;
        for(var15 = 0; var15 < 16; ++var15) {
            for(int var16 = 0; var16 < 32; ++var16) {
                tempLeft[var16] = ipLeft[var16];
                ipLeft[var16] = ipRight[var16];
            }

            finalData = new int[48];

            for(int var18 = 0; var18 < 48; ++var18) {
                finalData[var18] = keys[var15][var18];
            }

            int[] tempRight = this.xor(this.pPermute(this.sBoxPermute(this.xor(this.expandPermute(ipRight), finalData))), tempLeft);

            for(int var19 = 0; var19 < 32; ++var19) {
                ipRight[var19] = tempRight[var19];
            }
        }

        finalData = new int[64];

        for(var15 = 0; var15 < 32; ++var15) {
            finalData[var15] = ipRight[var15];
            finalData[32 + var15] = ipLeft[var15];
        }

        return this.finallyPermute(finalData);
    }

    private int[] dec(int[] dataByte, int[] keyByte) {
        int[][] keys = this.generateKeys(keyByte);
        int[] ipByte = this.initPermute(dataByte);
        int[] ipLeft = new int[32];
        int[] ipRight = new int[32];
        int[] tempLeft = new int[32];
        boolean i = false;
        boolean j = false;
        boolean k = false;
        boolean m = false;
        boolean n = false;

        for(int var17 = 0; var17 < 32; ++var17) {
            ipLeft[var17] = ipByte[var17];
            ipRight[var17] = ipByte[32 + var17];
        }

        int[] finalData;
        int var15;
        for(var15 = 15; var15 >= 0; --var15) {
            for(int var16 = 0; var16 < 32; ++var16) {
                tempLeft[var16] = ipLeft[var16];
                ipLeft[var16] = ipRight[var16];
            }

            finalData = new int[48];

            for(int var18 = 0; var18 < 48; ++var18) {
                finalData[var18] = keys[var15][var18];
            }

            int[] tempRight = this.xor(this.pPermute(this.sBoxPermute(this.xor(this.expandPermute(ipRight), finalData))), tempLeft);

            for(int var19 = 0; var19 < 32; ++var19) {
                ipRight[var19] = tempRight[var19];
            }
        }

        finalData = new int[64];

        for(var15 = 0; var15 < 32; ++var15) {
            finalData[var15] = ipRight[var15];
            finalData[32 + var15] = ipLeft[var15];
        }

        return this.finallyPermute(finalData);
    }

    private int[] initPermute(int[] originalData) {
        int[] ipByte = new int[64];
        boolean i = false;
        boolean m = true;
        boolean n = false;
        int var8 = 0;
        int var9 = 1;

        for(int var10 = 0; var8 < 4; var10 += 2) {
            int j = 7;

            for(int k = 0; j >= 0; ++k) {
                ipByte[var8 * 8 + k] = originalData[j * 8 + var9];
                ipByte[var8 * 8 + k + 32] = originalData[j * 8 + var10];
                --j;
            }

            ++var8;
            var9 += 2;
        }

        return ipByte;
    }

    private int[] expandPermute(int[] rightData) {
        int[] epByte = new int[48];

        for(int i = 0; i < 8; ++i) {
            if(i == 0) {
                epByte[i * 6 + 0] = rightData[31];
            } else {
                epByte[i * 6 + 0] = rightData[i * 4 - 1];
            }

            epByte[i * 6 + 1] = rightData[i * 4 + 0];
            epByte[i * 6 + 2] = rightData[i * 4 + 1];
            epByte[i * 6 + 3] = rightData[i * 4 + 2];
            epByte[i * 6 + 4] = rightData[i * 4 + 3];
            if(i == 7) {
                epByte[i * 6 + 5] = rightData[0];
            } else {
                epByte[i * 6 + 5] = rightData[i * 4 + 4];
            }
        }

        return epByte;
    }

    private int[] xor(int[] byteOne, int[] byteTwo) {
        int[] xorByte = new int[byteOne.length];

        for(int i = 0; i < byteOne.length; ++i) {
            xorByte[i] = byteOne[i] ^ byteTwo[i];
        }

        return xorByte;
    }

    private int[] sBoxPermute(int[] expandByte) {
        int[] sBoxByte = new int[32];
        String binary = "";
        int[][] s1 = new int[][]{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7}, {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8}, {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0}, {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};
        int[][] s2 = new int[][]{{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10}, {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5}, {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15}, {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};
        int[][] s3 = new int[][]{{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8}, {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1}, {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7}, {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};
        int[][] s4 = new int[][]{{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15}, {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9}, {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4}, {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};
        int[][] s5 = new int[][]{{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9}, {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6}, {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14}, {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};
        int[][] s6 = new int[][]{{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11}, {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8}, {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6}, {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};
        int[][] s7 = new int[][]{{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1}, {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6}, {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2}, {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};
        int[][] s8 = new int[][]{{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7}, {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2}, {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8}, {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

        for(int m = 0; m < 8; ++m) {
            boolean i = false;
            boolean j = false;
            int var15 = expandByte[m * 6 + 0] * 2 + expandByte[m * 6 + 5];
            int var16 = expandByte[m * 6 + 1] * 2 * 2 * 2 + expandByte[m * 6 + 2] * 2 * 2 + expandByte[m * 6 + 3] * 2 + expandByte[m * 6 + 4];
            switch(m) {
            case 0:
                binary = this.getBoxBinary(s1[var15][var16]);
                break;
            case 1:
                binary = this.getBoxBinary(s2[var15][var16]);
                break;
            case 2:
                binary = this.getBoxBinary(s3[var15][var16]);
                break;
            case 3:
                binary = this.getBoxBinary(s4[var15][var16]);
                break;
            case 4:
                binary = this.getBoxBinary(s5[var15][var16]);
                break;
            case 5:
                binary = this.getBoxBinary(s6[var15][var16]);
                break;
            case 6:
                binary = this.getBoxBinary(s7[var15][var16]);
                break;
            case 7:
                binary = this.getBoxBinary(s8[var15][var16]);
            }

            sBoxByte[m * 4 + 0] = Integer.parseInt(binary.substring(0, 1));
            sBoxByte[m * 4 + 1] = Integer.parseInt(binary.substring(1, 2));
            sBoxByte[m * 4 + 2] = Integer.parseInt(binary.substring(2, 3));
            sBoxByte[m * 4 + 3] = Integer.parseInt(binary.substring(3, 4));
        }

        return sBoxByte;
    }

    private int[] pPermute(int[] sBoxByte) {
        int[] pBoxPermute = new int[]{sBoxByte[15], sBoxByte[6], sBoxByte[19], sBoxByte[20], sBoxByte[28], sBoxByte[11], sBoxByte[27], sBoxByte[16], sBoxByte[0], sBoxByte[14], sBoxByte[22], sBoxByte[25], sBoxByte[4], sBoxByte[17], sBoxByte[30], sBoxByte[9], sBoxByte[1], sBoxByte[7], sBoxByte[23], sBoxByte[13], sBoxByte[31], sBoxByte[26], sBoxByte[2], sBoxByte[8], sBoxByte[18], sBoxByte[12], sBoxByte[29], sBoxByte[5], sBoxByte[21], sBoxByte[10], sBoxByte[3], sBoxByte[24]};
        return pBoxPermute;
    }

    private int[] finallyPermute(int[] endByte) {
        int[] fpByte = new int[]{endByte[39], endByte[7], endByte[47], endByte[15], endByte[55], endByte[23], endByte[63], endByte[31], endByte[38], endByte[6], endByte[46], endByte[14], endByte[54], endByte[22], endByte[62], endByte[30], endByte[37], endByte[5], endByte[45], endByte[13], endByte[53], endByte[21], endByte[61], endByte[29], endByte[36], endByte[4], endByte[44], endByte[12], endByte[52], endByte[20], endByte[60], endByte[28], endByte[35], endByte[3], endByte[43], endByte[11], endByte[51], endByte[19], endByte[59], endByte[27], endByte[34], endByte[2], endByte[42], endByte[10], endByte[50], endByte[18], endByte[58], endByte[26], endByte[33], endByte[1], endByte[41], endByte[9], endByte[49], endByte[17], endByte[57], endByte[25], endByte[32], endByte[0], endByte[40], endByte[8], endByte[48], endByte[16], endByte[56], endByte[24]};
        return fpByte;
    }

    private String getBoxBinary(int i) {
        String binary = "";
        switch(i) {
        case 0:
            binary = "0000";
            break;
        case 1:
            binary = "0001";
            break;
        case 2:
            binary = "0010";
            break;
        case 3:
            binary = "0011";
            break;
        case 4:
            binary = "0100";
            break;
        case 5:
            binary = "0101";
            break;
        case 6:
            binary = "0110";
            break;
        case 7:
            binary = "0111";
            break;
        case 8:
            binary = "1000";
            break;
        case 9:
            binary = "1001";
            break;
        case 10:
            binary = "1010";
            break;
        case 11:
            binary = "1011";
            break;
        case 12:
            binary = "1100";
            break;
        case 13:
            binary = "1101";
            break;
        case 14:
            binary = "1110";
            break;
        case 15:
            binary = "1111";
        }

        return binary;
    }

    private int[][] generateKeys(int[] keyByte) {
        int[] key = new int[56];
        int[][] keys = new int[16][48];
        int[] loop = new int[]{1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

        int i;
        int tempLeft;
        int tempRight;
        for(i = 0; i < 7; ++i) {
            tempLeft = 0;

            for(tempRight = 7; tempLeft < 8; --tempRight) {
                key[i * 8 + tempLeft] = keyByte[8 * tempRight + i];
                ++tempLeft;
            }
        }

        boolean var10 = false;

        label167:
        for(i = 0; i < 16; ++i) {
            boolean var11 = false;
            boolean var12 = false;

            int m;
            for(int tempKey = 0; tempKey < loop[i]; ++tempKey) {
                tempLeft = key[0];
                tempRight = key[28];

                for(m = 0; m < 27; ++m) {
                    key[m] = key[m + 1];
                    key[28 + m] = key[29 + m];
                }

                key[27] = tempLeft;
                key[55] = tempRight;
            }

            int[] var13 = new int[]{key[13], key[16], key[10], key[23], key[0], key[4], key[2], key[27], key[14], key[5], key[20], key[9], key[22], key[18], key[11], key[3], key[25], key[7], key[15], key[6], key[26], key[19], key[12], key[1], key[40], key[51], key[30], key[36], key[46], key[54], key[29], key[39], key[50], key[44], key[32], key[47], key[43], key[48], key[38], key[55], key[33], key[52], key[45], key[41], key[49], key[35], key[28], key[31]};
            switch(i) {
            case 0:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[0][m] = var13[m];
                    ++m;
                }
            case 1:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[1][m] = var13[m];
                    ++m;
                }
            case 2:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[2][m] = var13[m];
                    ++m;
                }
            case 3:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[3][m] = var13[m];
                    ++m;
                }
            case 4:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[4][m] = var13[m];
                    ++m;
                }
            case 5:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[5][m] = var13[m];
                    ++m;
                }
            case 6:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[6][m] = var13[m];
                    ++m;
                }
            case 7:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[7][m] = var13[m];
                    ++m;
                }
            case 8:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[8][m] = var13[m];
                    ++m;
                }
            case 9:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[9][m] = var13[m];
                    ++m;
                }
            case 10:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[10][m] = var13[m];
                    ++m;
                }
            case 11:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[11][m] = var13[m];
                    ++m;
                }
            case 12:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[12][m] = var13[m];
                    ++m;
                }
            case 13:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[13][m] = var13[m];
                    ++m;
                }
            case 14:
                m = 0;

                while(true) {
                    if(m >= 48) {
                        continue label167;
                    }

                    keys[14][m] = var13[m];
                    ++m;
                }
            case 15:
                for(m = 0; m < 48; ++m) {
                    keys[15][m] = var13[m];
                }
            }
        }

        return keys;
    }
}
