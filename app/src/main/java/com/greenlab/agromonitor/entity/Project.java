package com.greenlab.agromonitor.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.greenlab.agromonitor.interfaces.GetSpreadsheetValues;
import com.greenlab.agromonitor.managers.UserManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by mazer on 1/19/2018.
 *
 */


@Entity(tableName = "project")
public class Project implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private String projectName;
    private String creationDate;
    private int cultureType; //0 - Cana, 1 - Soja
    private int turn;
    private String farmName;
    private String talhao;
    private String frenteColheita;
    private String machineID;
    private String operatorsName;
    private String measurersName;
    private int measureUnity;
    private int areaAmostral;
    private float umidade;

    @Ignore
    private List<Product> listOfProducts;
    @Ignore
    private List<SpreadsheetValues> listOfProjectProduct;
    @Ignore
    private List<Variables> listOfStringProducts;
    @Ignore
    public boolean isOpened;

    public Project(){
        listOfProducts = new ArrayList<>();
        listOfStringProducts = new ArrayList<>();
        projectName = "";
        creationDate = "";
        farmName = "";
        talhao = "";
        frenteColheita = "";
        machineID = "";
        operatorsName = "";
        measurersName = "";
    }

    public int getId() {
        return id;
    }

    public List<SpreadsheetValues> getSpreadSheetValues(Context ctx){
        UserManager userManager = new UserManager(ctx);
        return userManager.getSpreadsheetValues(this.id);
    }

    public Project getActualProject(Context ctx){
        UserManager userManager = new UserManager(ctx);
        return userManager.getProjectById(this.id);
    }

    public void updateAreaAndUnity(Context ctx, int areaAmostral, int measureUnity){
        UserManager userManager = new UserManager(ctx);
        userManager.updateProjectAreaAndUnity(this.id,areaAmostral,measureUnity);
    }

    public List<SpreadsheetValues> getSpreadSheetValuesNotNull(Context ctx){
        UserManager userManager = new UserManager(ctx);
        return userManager.getSpreadsheetValuesNotNull(this.id);
    }

    public List<SpreadsheetValues> getProductValuesNotNullFromProject(Context ctx, int idProduct){
        UserManager userManager = new UserManager(ctx);
        return userManager.getProductValuesNotNullFromProject(this.id, idProduct);
    }

    public List<Product> getVariablesOfProject(Context ctx){
        UserManager userManager = new UserManager(ctx);
        return userManager.getVariablesFromProject(this.id);
    }


    public void insertProjectProduct(Context ctx, ProjectProduct projectProduct){
        UserManager userManager = new UserManager(ctx);
        userManager.insertProjectProduct(projectProduct);
    }

    public void removeProjectProduct(Context ctx, int idProduct){
        UserManager userManager = new UserManager(ctx);
        userManager.removeProjectProduct(idProduct);
    }

    public void updateProjectProduct(Context ctx, ProjectProduct projectProduct){
        UserManager userManager = new UserManager(ctx);
        userManager.updateProjectProduct(projectProduct);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getCultureType() {
        return cultureType;
    }

    public void setCultureType(int cultureType) {
        this.cultureType = cultureType;
    }



    public List<Product> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public List<SpreadsheetValues> getListOfProjectProduct() {
        return listOfProjectProduct;
    }

    public void setListOfProjectProduct(List<SpreadsheetValues> listOfProjectProduct) {
        this.listOfProjectProduct = listOfProjectProduct;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getTalhao() {
        return talhao;
    }

    public void setTalhao(String talhao) {
        this.talhao = talhao;
    }

    public String getFrenteColheita() {
        return frenteColheita;
    }

    public void setFrenteColheita(String frenteColheita) {
        this.frenteColheita = frenteColheita;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getOperatorsName() {
        return operatorsName;
    }

    public void setOperatorsName(String operatorsName) {
        this.operatorsName = operatorsName;
    }

    public String getMeasurersName() {
        return measurersName;
    }

    public void setMeasurersName(String measurersName) {
        this.measurersName = measurersName;
    }

    public int getMeasureUnity() {
        return measureUnity;
    }

    public void setMeasureUnity(int measureUnity) {
        this.measureUnity = measureUnity;
    }


    public List<Variables> getListOfStringProducts() {
        return listOfStringProducts;
    }

    public void setListOfStringProducts(List<Variables> listOfStringProducts) {
        this.listOfStringProducts = listOfStringProducts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getAreaAmostral() {
        return areaAmostral;
    }

    public void setAreaAmostral(int areaAmostral) {
        this.areaAmostral = areaAmostral;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.projectName);
        dest.writeString(this.creationDate);
        dest.writeInt(this.cultureType);
        dest.writeInt(this.turn);
        dest.writeString(this.farmName);
        dest.writeString(this.talhao);
        dest.writeString(this.frenteColheita);
        dest.writeString(this.machineID);
        dest.writeString(this.operatorsName);
        dest.writeString(this.measurersName);
        dest.writeInt(this.measureUnity);
        dest.writeList(this.listOfStringProducts);
        dest.writeByte(this.isOpened ? (byte) 1 : (byte) 0);
    }

    protected Project(Parcel in) {
        this.id = in.readInt();
        this.projectName = in.readString();
        this.creationDate = in.readString();
        this.cultureType = in.readInt();
        this.turn = in.readInt();
        this.farmName = in.readString();
        this.talhao = in.readString();
        this.frenteColheita = in.readString();
        this.machineID = in.readString();
        this.operatorsName = in.readString();
        this.measurersName = in.readString();
        this.measureUnity = in.readInt();
        this.listOfStringProducts = new ArrayList<>();
        in.readList(this.listOfStringProducts, Product.class.getClassLoader());
        this.isOpened = in.readByte() != 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
