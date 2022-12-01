package com.cristisima.sheepclient;

public class Utils {
    public static boolean shouldSkippBLock(String blockKey)
    {
        //noinspection SpellCheckingInspection
        return blockKey.equals("block.minecraft.stone") ||
                blockKey.equals("block.minecraft.gravel") ||
                blockKey.equals("block.minecraft.grass_block") ||
                blockKey.equals("block.minecraft.andesite") ||
                blockKey.equals("block.minecraft.granite") ||
                blockKey.equals("block.minecraft.diorite") ||
                blockKey.equals("block.minecraft.deepslate") ||
                blockKey.equals("block.minecraft.tuff") ||
                blockKey.equals("block.minecraft.soulsand") ||
                blockKey.equals("block.minecraft.soulsoil") ||
                blockKey.equals("block.minecraft.netherrack") ||
                blockKey.equals("block.minecraft.basalt") ||
                blockKey.equals("block.minecraft.blackstone") ||
                blockKey.equals("block.minecraft.dirt");
    }

    public static double precisionRound(double nr, int precision)
    {
        long scale = (long) Math.pow(10, precision);
        nr=(double) Math.round(nr*scale)/scale;
        if(getDecimal(nr,precision+1)!=0)
            nr+=Math.signum(nr)*0.000005;
        return nr;
    }

    public static int getDecimal(double nr, int precision)
    {
        long scale = (long) Math.pow(10, precision);
        return (int) (((long)(nr*scale))%10);
    }

}
