package com.amberyork.wakeme;


import java.util.Date;
import java.text.SimpleDateFormat;


public class MovementBin {
    //I am working with epoch because it allows me to round into bins easily
    private long binStartEpoch,binStopEpoch; //for recording actual first and last data point time

    private int binN;//how many samples in bin
    private double binAvX,binAvY,binAvZ;//average movements
    private double binStdX,binStdY,binStdZ;//standard dev movements
    private int binLength;//number of seconds the bin is good for
    private long binXlabelEpoch;//for plotting purposes, this will be X value


    // constructors
    public MovementBin(){
    }

    //constructor loaded up


        public MovementBin(long binStartEpoch, long binStopEpoch, int binN, double binAvX, double binAvY, double binAvZ, double binStdX, double binStdY, double binStdZ, int binLength, long binXlabelEpoch) {
            this.binStartEpoch = binStartEpoch;
            this.binStopEpoch = binStopEpoch;
            this.binN = binN;
            this.binAvX = binAvX;
            this.binAvY = binAvY;
            this.binAvZ = binAvZ;
            this.binStdX = binStdX;
            this.binStdY = binStdY;
            this.binStdZ = binStdZ;
            this.binLength = binLength;
            this.binXlabelEpoch = binXlabelEpoch;
        }

    //getters and setters

    public long getBinStartEpoch() {
        return binStartEpoch;
    }

    public void setBinStartEpoch(long binStartEpoch) {
        this.binStartEpoch = binStartEpoch;
    }

    public long getBinStopEpoch() {
        return binStopEpoch;
    }

    public void setBinStopEpoch(long binStopEpoch) {
        this.binStopEpoch = binStopEpoch;
    }


    public int getBinN() {
        return binN;
    }

    public void setBinN(int binN) {
        this.binN = binN;
    }

    public double getBinAvX() {
        return binAvX;
    }

    public void setBinAvX(double binAvX) {
        this.binAvX = binAvX;
    }

    public double getBinAvY() {
        return binAvY;
    }

    public void setBinAvY(double binAvY) {
        this.binAvY = binAvY;
    }

    public double getBinAvZ() {
        return binAvZ;
    }

    public void setBinAvZ(double binAvZ) {
        this.binAvZ = binAvZ;
    }

    public double getBinStdX() {
        return binStdX;
    }

    public void setBinStdX(double binStdX) {
        this.binStdX = binStdX;
    }

    public double getBinStdY() {
        return binStdY;
    }

    public void setBinStdY(double binStdY) {
        this.binStdY = binStdY;
    }

    public double getBinStdZ() {
        return binStdZ;
    }

    public void setBinStdZ(double binStdZ) {
        this.binStdZ = binStdZ;
    }

    public int getBinLength() {
        return binLength;
    }

    public void setBinLength(int binLength) {
        this.binLength = binLength;
    }

    public long getBinXlabelEpoch() {
        return binXlabelEpoch;
    }

    public void setBinXlabelEpoch(long binXlabelEpoch) {
        this.binXlabelEpoch = binXlabelEpoch;
    }

///when you need the xlabel as string
    public String getBinXLabelString() {
        String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(binXlabelEpoch * 60 * 1000L));
        return dateAsText;
    }

    public String getDataLine(){
        //header: binXlabelEpoch,binLength,binStartEpoch,binStopEpoch,binN,binAvX,binAvY,binAvZ,binStdX,binStdY,binStdZ
        String dataLine = binXlabelEpoch+","+binLength+","+binStartEpoch+","+binStopEpoch+","+binN+","+binAvX+","+binAvY+","+binAvZ+","+
                binStdX+","+binStdY+","+binStdZ;
    return dataLine;

    }
}