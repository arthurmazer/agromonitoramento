package com.greenlab.agromonitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.widget.Toast;

import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.SpreadsheetValues;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.managers.SessionManager;
import com.greenlab.agromonitor.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by arthu on 12/02/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    SessionManager sessionManager;


    @Override
    protected void onResume(){
        super.onResume();
        sessionManager = new SessionManager(this);
        if (!sessionManager.isUserLoggedIn()){
            Intent it = new Intent(this, LoginActivity .class);
            startActivity(it);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManager = new SessionManager(this);
        //sessionManager.logout();
    }

    public User getSessionUser(){
        sessionManager = new SessionManager(this);
        User user = new User();
        user.setLogin(sessionManager.getUserName());
        user.setId(sessionManager.getUserId());
        return user;
    }

    public int getOpenedProject(){
        sessionManager = new SessionManager(this);
        return sessionManager.getCurrentProject();
    }

    public String getNameProjectOpened(){
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        return sessionManager.getNameCurrentProject();
    }

    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Constants.OPEN_CHART) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    public String getStringCulture(int cultureIndex){

        switch (cultureIndex){
            case Constants.PROJECT_TYPE_CANA_DE_ACUCAR:
                return "Cana-de-açucar";
            case Constants.PROJECT_TYPE_SOJA:
                return "Soja";
            case Constants.PROJECT_TYPE_ALGODAO:
                return "Algodão";
            case Constants.PROJECT_TYPE_AMENDOIM:
                return "Amendoim";
            case Constants.PROJECT_TYPE_CAFE:
                return "Café";
            case Constants.PROJECT_TYPE_MILHO:
                return "Milho";
            default:
                return "Cana-de-açucar";
        }
    }


    @SuppressLint("StaticFieldLeak")
    public void getExcel(){
        int idProject = getOpenedProject();

        final Project project = new Project();
        project.setId(idProject);
        new AsyncTask<Void, Void, List<SpreadsheetValues>>() {
            @Override
            protected List<SpreadsheetValues> doInBackground(Void... voids) {
                return project.getSpreadSheetValuesNotNull(getApplicationContext() );
            }
            @Override
            protected void onPostExecute(List<SpreadsheetValues> spreadsheetValuesList) {
                loadCurrentProject(spreadsheetValuesList);
            }
        }.execute();

    }

    public void sendEmail(File file){
        //File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        //Uri path = Uri.fromFile(file);
        Uri path = FileProvider.getUriForFile(
                BaseActivity.this,
                "com.greenlab.agromonitor.provider",
                file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {""};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, getNameProjectOpened());
        startActivity(Intent.createChooser(emailIntent , "Enviado e-mail..."));
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    656);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }else {
            getExcel();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public void loadCurrentProject(List<SpreadsheetValues> spreadsheetValuesList){
        int idProject = getOpenedProject();

        final Project mProject = new Project();
        mProject.setId(idProject);
        new AsyncTask<Void, Void, Project>() {
            @Override
            protected Project doInBackground(Void... voids) {
                return mProject.getActualProject(getApplicationContext());
            }
            @Override
            protected void onPostExecute(Project proj) {
                exportToExcel(spreadsheetValuesList, proj);
            }
        }.execute();

    }

    public void exportToExcel(List<SpreadsheetValues> spreadsheetValuesList, Project project){

        final String fileName = getNameProjectOpened()+".xls";


        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();

        File directory = new File(sdCard.getAbsolutePath() + "/agromonitor");

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("pt", "BR"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(getNameProjectOpened(), 0);

            try {
                int idProduct = -1;
                int column = 0;
                int row = 5;

                sheet.addCell(new Label(0,0, "Projeto:"));
                sheet.addCell(new Label(1,0, project.getProjectName()));

                sheet.addCell(new Label(0,1, "Área Amostral (m²):"));
                sheet.addCell(new Label(1,1, String.valueOf(project.getAreaAmostral())));

                sheet.addCell(new Label(0,2, "Umidade de colheita:"));
                sheet.addCell(new Label(1,2, String.valueOf(project.getUmidade())));

                sheet.addCell(new Label(0,3, "Umidade Cooperativa:"));
                sheet.addCell(new Label(1,3, String.valueOf(project.getUmidadeCoop())));

                sheet.getColumnView(0).setAutosize(true);
                for (int i = 0; i < sheet.getColumns(); i++){
                    sheet.getColumnView(i).setAutosize(true);
                }


                for(int i = 0; i < spreadsheetValuesList.size(); i++){
                    SpreadsheetValues sp = spreadsheetValuesList.get(i);

                    if ( i == 0) {
                        idProduct = sp.getId();
                        sheet.addCell(new Label(column,row, sp.getProduct() + " (kg/ha)"));
                        row++;
                    }


                    if ( idProduct != sp.getId()){
                        idProduct = sp.getId();
                        column += 3;
                        row = 5;
                        sheet.addCell(new Label(column,row, sp.getProduct() + " (kg/ha)"));
                        row++;
                    }


                    sheet.addCell(new Label(column,row, ""+getKgHaValue(sp.getValue(), project)));
                    sheet.addCell(new Label(column+1,row, "" + convertTime(sp.getTimestamp())));
                    row++;
                }

                //closing cursor
                //cursor.close();
            } catch (RowsExceededException e) {
                Log.d("excel", "exc " + e.getMessage());
                e.printStackTrace();
            } catch (WriteException e) {
                Log.d("excel", "exc " + e.getMessage());
                e.printStackTrace();
            }
            Log.d("excel", "SUCESSO");
            workbook.write();
            sendEmail(file);
            try {
                workbook.close();
            } catch (WriteException e) {
                Log.d("excel", "exc " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.d("excel", "exc " + e.getMessage());
            e.printStackTrace();
        }
    }

    private float getKgHaValue(Float value, Project proj) {
        float areaAmostral = proj.getAreaAmostral();
         if (proj.getMeasureUnity() == Constants.KILO) {
             return getValueCorrigidoUmidade(value * 10000f / areaAmostral, proj);
        } else {
             return getValueCorrigidoUmidade((value * 10000f / areaAmostral) / 1000, proj);
        }
    }

    private  float getValueCorrigidoUmidade(float value, Project proj) {
        float umidade = proj.getUmidade();
        float umidadeCoop = proj.getUmidadeCoop();
        return value * ((100 - umidade) / (100 - umidadeCoop));
    }

    public String convertTime(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        cal.setTimeInMillis(time);

        return (cal.get(Calendar.DAY_OF_MONTH)+ "/" + (cal.get(Calendar.MONTH) + 1) + "/"
                + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 656: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getExcel();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
