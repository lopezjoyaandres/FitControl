package com.example.fitcontroluser.databases.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Repetition")
public class Repetition {
    public static final String TABLE_NAME = "Repetition";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "idRep")
    private int idRep;


    @ColumnInfo(name = "nameMovement")
    private String nameMovement;



    @ColumnInfo(name = "date")
    private String date;



    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "inicio")
    private long inicio;

    @ColumnInfo(name = "fin")
    private long fin;

    @ColumnInfo(name = "mediaXA")
    private double mediaXA;

    @ColumnInfo(name = "mediaYA")
    private double mediaYA;

    @ColumnInfo(name = "mediaZA")
    private double mediaZA;

    @ColumnInfo(name = "varianzaXA")
    private double varianzaXA;

    @ColumnInfo(name = "varianzaYA")
    private double varianzaYA;

    @ColumnInfo(name = "varianzaZA")
    private double varianzaZA;

    @ColumnInfo(name = "skewnessXA")
    private double skewnessXA;

    @ColumnInfo(name = "skewnessYA")
    private double skewnessYA;

    @ColumnInfo(name = "skewnessZA")
    private double skewnessZA;

    @ColumnInfo(name = "mediaXG")
    private double mediaXG;

    @ColumnInfo(name = "mediaYG")
    private double mediaYG;

    @ColumnInfo(name = "mediaZG")
    private double mediaZG;

    @ColumnInfo(name = "varianzaXG")
    private double varianzaXG;

    @ColumnInfo(name = "varianzaYG")
    private double varianzaYG;

    @ColumnInfo(name = "varianzaZG")
    private double varianzaZG;

    @ColumnInfo(name = "skewnessXG")
    private double skewnessXG;

    @ColumnInfo(name = "skewnessYG")
    private double skewnessYG;

    @ColumnInfo(name = "skewnessZG")
    private double skewnessZG;

    @ColumnInfo(name = "minXG")
    private double minXG;

    @ColumnInfo(name = "minYG")
    private double minYG;

    @ColumnInfo(name = "minZG")
    private double minZG;

    @ColumnInfo(name = "maxXG")
    private double maxXG;

    @ColumnInfo(name = "maxYG")
    private double maxYG;

    @ColumnInfo(name = "maxZG")
    private double maxZG;

    @ColumnInfo(name = "repOK")
    private int repOK;

    @ColumnInfo(name = "task")
    private int task;

    public Repetition(String nameMovement, String date, String name, long inicio, long fin,int task) {
        this.nameMovement = nameMovement;
        this.date = date;
        this.name = name;
        this.inicio = inicio;
        this.fin = fin;
        this.task = task;
    }

    public void add_data_acel(double mediaXA,double mediaYA,double mediaZA,double varianzaXA,double varianzaYA,double varianzaZA,double skewnessXA,double skewnessYA,double skewnessZA,int ok){
        this.mediaXA=mediaXA;
        this.mediaYA=mediaYA;
        this.mediaZA=mediaZA;
        this.varianzaXA=varianzaXA;
        this.varianzaYA=varianzaYA;
        this.varianzaZA=varianzaZA;
        this.skewnessXA=skewnessXA;
        this.skewnessYA=skewnessYA;
        this.skewnessZA=skewnessZA;
        this.repOK=ok;
    }
    public void add_data_gyr(double mediaXG,double mediaYG,double mediaZG,double varianzaXG,double varianzaYG,double varianzaZG,double skewnessXG,double skewnessYG,double skewnessZG,double minXG,double minYG,double minZG,double maxXG,double maxYG,double maxZG){
        this.mediaXG=mediaXG;
        this.mediaYG=mediaYG;
        this.mediaZG=mediaZG;
        this.varianzaXG=varianzaXG;
        this.varianzaYG=varianzaYG;
        this.varianzaZG=varianzaZG;
        this.skewnessXG=skewnessXG;
        this.skewnessYG=skewnessYG;
        this.skewnessZG=skewnessZG;
        this.minXG=minXG;
        this.minYG=minYG;
        this.minZG=minZG;
        this.maxXG=maxXG;
        this.maxYG=maxYG;
        this.maxZG=maxZG;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public long getInicio() {
        return inicio;
    }

    public void setInicio(long inicio) {
        this.inicio = inicio;
    }

    public long getFin() {
        return fin;
    }

    public void setFin(long fin) {
        this.fin = fin;
    }

    public String getNameMovement() {
        return nameMovement;
    }

    public void setNameMovement(String nameMovement) {
        this.nameMovement = nameMovement;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdRep() {
        return idRep;
    }

    public void setIdRep(int idRep) {
        this.idRep = idRep;
    }

    public int getRepOK() {
        return repOK;
    }

    public void setRepOK(int repOK) {
        this.repOK = repOK;
    }


    public double getMediaXA() {
        return mediaXA;
    }

    public void setMediaXA(double mediaXA) {
        this.mediaXA = mediaXA;
    }

    public double getMediaYA() {
        return mediaYA;
    }

    public void setMediaYA(double mediaYA) {
        this.mediaYA = mediaYA;
    }

    public double getMediaZA() {
        return mediaZA;
    }

    public void setMediaZA(double mediaZA) {
        this.mediaZA = mediaZA;
    }

    public double getVarianzaXA() {
        return varianzaXA;
    }

    public void setVarianzaXA(double varianzaXA) {
        this.varianzaXA = varianzaXA;
    }

    public double getVarianzaYA() {
        return varianzaYA;
    }

    public void setVarianzaYA(double varianzaYA) {
        this.varianzaYA = varianzaYA;
    }

    public double getVarianzaZA() {
        return varianzaZA;
    }

    public void setVarianzaZA(double varianzaZA) {
        this.varianzaZA = varianzaZA;
    }

    public double getSkewnessXA() {
        return skewnessXA;
    }

    public void setSkewnessXA(double skewnessXA) {
        this.skewnessXA = skewnessXA;
    }

    public double getSkewnessYA() {
        return skewnessYA;
    }

    public void setSkewnessYA(double skewnessYA) {
        this.skewnessYA = skewnessYA;
    }

    public double getSkewnessZA() {
        return skewnessZA;
    }

    public void setSkewnessZA(double skewnessZA) {
        this.skewnessZA = skewnessZA;
    }

    public double getMediaXG() {
        return mediaXG;
    }

    public void setMediaXG(double mediaXG) {
        this.mediaXG = mediaXG;
    }

    public double getMediaYG() {
        return mediaYG;
    }

    public void setMediaYG(double mediaYG) {
        this.mediaYG = mediaYG;
    }

    public double getMediaZG() {
        return mediaZG;
    }

    public void setMediaZG(double mediaZG) {
        this.mediaZG = mediaZG;
    }

    public double getVarianzaXG() {
        return varianzaXG;
    }

    public void setVarianzaXG(double varianzaXG) {
        this.varianzaXG = varianzaXG;
    }

    public double getVarianzaYG() {
        return varianzaYG;
    }

    public void setVarianzaYG(double varianzaYG) {
        this.varianzaYG = varianzaYG;
    }

    public double getVarianzaZG() {
        return varianzaZG;
    }

    public void setVarianzaZG(double varianzaZG) {
        this.varianzaZG = varianzaZG;
    }

    public double getSkewnessXG() {
        return skewnessXG;
    }

    public void setSkewnessXG(double skewnessXG) {
        this.skewnessXG = skewnessXG;
    }

    public double getSkewnessYG() {
        return skewnessYG;
    }

    public void setSkewnessYG(double skewnessYG) {
        this.skewnessYG = skewnessYG;
    }

    public double getSkewnessZG() {
        return skewnessZG;
    }

    public void setSkewnessZG(double skewnessZG) {
        this.skewnessZG = skewnessZG;
    }

    public double getMinXG() {
        return minXG;
    }

    public void setMinXG(double minXG) {
        this.minXG = minXG;
    }

    public double getMinYG() {
        return minYG;
    }

    public void setMinYG(double minYG) {
        this.minYG = minYG;
    }

    public double getMinZG() {
        return minZG;
    }

    public void setMinZG(double minZG) {
        this.minZG = minZG;
    }

    public double getMaxXG() {
        return maxXG;
    }

    public void setMaxXG(double maxXG) {
        this.maxXG = maxXG;
    }

    public double getMaxYG() {
        return maxYG;
    }

    public void setMaxYG(double maxYG) {
        this.maxYG = maxYG;
    }

    public double getMaxZG() {
        return maxZG;
    }

    public void setMaxZG(double maxZG) {
        this.maxZG = maxZG;
    }
}
