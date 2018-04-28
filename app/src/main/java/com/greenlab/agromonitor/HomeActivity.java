package com.greenlab.agromonitor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.greenlab.agromonitor.entity.Project;
import com.greenlab.agromonitor.entity.SpreadsheetValues;
import com.greenlab.agromonitor.entity.User;
import com.greenlab.agromonitor.fragments.HomeFragment;
import com.greenlab.agromonitor.fragments.ReportFragment;
import com.greenlab.agromonitor.fragments.SpreadsheetFragment;
import com.greenlab.agromonitor.interfaces.GetAllProjectsOfUser;
import com.greenlab.agromonitor.managers.SessionManager;
import com.greenlab.agromonitor.utils.Constants;
import com.greenlab.agromonitor.utils.Wrapper;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements GetAllProjectsOfUser {

    private BottomNavigationView bottomNavigationView;
    private User user;
    private List<Project> projectList;
    Button btnNewProject;
    public boolean isNewProject = false;
    MaterialDialog dialogLoading;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle(R.string.title_home);
                    pushFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_spreadsheet:
                    setTitle(R.string.title_spreadsheet);
                    pushFragment(SpreadsheetFragment.newInstance());
                    return true;
                case R.id.navigation_reports:
                    setTitle(R.string.title_reports);
                    pushFragment(ReportFragment.newInstance());
                    return true;

            }
            return false;
        }
    };

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.root_layout, fragment);
                ft.commit();
            }
        }
    }


    public File createPdf(Wrapper wrapper, String notes) {



        Project project = wrapper.getProject();
        List<SpreadsheetValues> spreadsheetValuesList = wrapper.getSpreadsheetValuesList();

        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();

        PDFBoxResourceLoader.init(getApplicationContext());

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA;
        // Or a custom font
//		try {
//			PDType0Font font = PDType0Font.load(document, assetManager.open("MyFontFile.TTF"));
//		} catch(IOException e) {
//			e.printStackTrace();
//		}

        PDPageContentStream contentStream;
        int startLine = 700;

        try {
            // Define a content stream for adding to the PDF
            contentStream = new PDPageContentStream(document, page);

            // Write Hello World in blue text
            contentStream.beginText();
  //          contentStream.setNonStrokingColor(15, 38, 192);
            contentStream.setFont(font, 16);
            contentStream.newLineAtOffset(100, startLine);
            startLine -= 20;
            contentStream.showText("Projeto: " + project.getProjectName());
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLine();
//            contentStream.setNonStrokingColor(15, 38, 192);
            contentStream.setFont(font, 14);
            contentStream.newLineAtOffset(100, startLine);
            startLine -= 20;
            contentStream.showText("Criado em: " + project.getCreationDate());
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLine();
            //contentStream.setNonStrokingColor(15, 38, 192);
            contentStream.setFont(font, 14);
            contentStream.newLineAtOffset(100, startLine);
            startLine -= 20;
            contentStream.showText("Cultura: " + getStringCulture(project.getCultureType()));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLine();
            //contentStream.setNonStrokingColor(15, 38, 192);
            contentStream.setFont(font, 14);
            contentStream.newLineAtOffset(100, startLine);
            startLine -= 20;
            contentStream.showText("Área Amostral: " + project.getAreaAmostral());
            contentStream.endText();

            if (!project.getFarmName().isEmpty() ) {
                contentStream.beginText();
                contentStream.newLine();
                //contentStream.setNonStrokingColor(15, 38, 192);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(100, startLine);
                startLine -= 20;
                contentStream.showText("Fazenda: " + project.getFarmName());
                contentStream.endText();
            }

            if (!project.getTalhao().isEmpty() ) {
                contentStream.beginText();
                contentStream.newLine();
                //contentStream.setNonStrokingColor(15, 38, 192);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(100, startLine);
                startLine -= 20;
                contentStream.showText("Talhão: " + project.getTalhao());
                contentStream.endText();
            }

            if (!project.getFrenteColheita().isEmpty() ) {
                contentStream.beginText();
                contentStream.newLine();
                //contentStream.setNonStrokingColor(15, 38, 192);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(100, startLine);
                startLine -= 20;
                contentStream.showText("Frente Colheita: " + project.getFrenteColheita());
                contentStream.endText();
            }

            if (!project.getMachineID().isEmpty() ) {
                contentStream.beginText();
                contentStream.newLine();
                //contentStream.setNonStrokingColor(15, 38, 192);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(100, startLine);
                startLine -= 20;
                contentStream.showText("ID. Máquinha: " + project.getMachineID());
                contentStream.endText();
            }

            if (!project.getOperatorsName().isEmpty() ) {
                contentStream.beginText();
                contentStream.newLine();
                //contentStream.setNonStrokingColor(15, 38, 192);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(100, startLine);
                startLine -= 20;
                contentStream.showText("Operador: " + project.getOperatorsName());
                contentStream.endText();
            }

            if (!project.getMeasurersName().isEmpty() ) {
                contentStream.beginText();
                contentStream.newLine();
                //contentStream.setNonStrokingColor(15, 38, 192);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(100, startLine);
                startLine -= 20;
                contentStream.showText("Avaliador: " + project.getMeasurersName());
                contentStream.endText();
            }

            if (!notes.isEmpty() ) {
                contentStream.beginText();
                contentStream.newLine();
                //contentStream.setNonStrokingColor(15, 38, 192);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(100, startLine);
                startLine -= 20;
                Log.d("pdfao", notes);
                contentStream.showText("Observações: " + notes);

                contentStream.endText();
            }

            // Make sure that the content stream is closed:
            contentStream.close();

            String filename = getNameProjectOpened() + ".pdf";
            String path = sdCard.getAbsolutePath() + "/agromonitor/";
            File directory = new File(path);
            //String path = sdCard.getAbsolutePath() + "/Download/" + filename;
            document.save(path + filename);
            document.close();

            File file = new File(directory, filename);
            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showNotesDialog(final Wrapper wrapper){

        new MaterialDialog.Builder(HomeActivity.this)
                .customView(R.layout.dialog_notes, true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(final MaterialDialog dialog, DialogAction which) {
                        final String notes;
                        View v = dialog.getCustomView();
                        EditText editNotes =  v.findViewById(R.id.edit_text_obs);
                        notes = editNotes.getText().toString();
                        new AsyncTask<Void, Void, File>() {

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                dialog.dismiss();
                                showLoadingDialog();
                            }

                            @Override
                            protected File doInBackground(Void... voids) {
                                return createPdf(wrapper,notes);
                            }
                            @Override
                            protected void onPostExecute(File file) {
                                dismissLoadingDialog();
                                sendEmail(file);
                            }
                        }.execute();
                        //createPdf(wrapper, notes);
                    }
                })
                .negativeText("Cancelar")
                .negativeColor(Color.RED)
                .positiveText("OK")
                .show();
    }

    public void showLoadingDialog(){
        Log.d("pdfao","asa");
        dialogLoading =new MaterialDialog.Builder(HomeActivity.this)
                .title("Aguarde")
                .cancelable(false)
                .content("Gerando relatório...")
                .progress(true, 0)
                .show();
    }

    public void dismissLoadingDialog(){
        if (dialogLoading != null)
            dialogLoading.dismiss();
    }

    public void displayErrorMessage(){
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage("Você ainda não tem um projeto em aberto");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    }
                });
        alertDialog.show();
    }


    public void getProjectDetails(){
        int idProject = getOpenedProject();
        Log.d("ueh", "uehhhhhh");
        final Project project = new Project();
        project.setId(idProject);
        new AsyncTask<Void, Void, Wrapper>() {
            @Override
            protected Wrapper doInBackground(Void... voids) {
                Wrapper wrapper = new Wrapper();
                wrapper.setSpreadsheetValuesList(project.getSpreadSheetValues(getApplicationContext()));
                wrapper.setProject(project.getActualProject(getApplicationContext()));
                return wrapper;
            }
            @Override
            protected void onPostExecute(Wrapper genericWrapper) {
                showNotesDialog(genericWrapper);
            }
        }.execute();

    }

    public void changeToSpreadsheetScreen(){
        bottomNavigationView.setSelectedItemId(R.id.navigation_spreadsheet);
    }

    public void changeToHomeScreen(){
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        setTitle(R.string.title_home);
        pushFragment(HomeFragment.newInstance());
    }

    public void changeToReportScreen(){
        bottomNavigationView.setSelectedItemId(R.id.navigation_reports);
        setTitle(R.string.title_reports);
        pushFragment(ReportFragment.newInstance());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.title_home);
        projectList = new ArrayList<>();
        btnNewProject = findViewById(R.id.btn_new_project_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Boolean isSaved = extras.getBoolean("success", false);
            long idProject = extras.getLong("id_project", 0);
            String nameProject = extras.getString("name_project");
            int culture = extras.getInt("culture", 0);
            
            if (isSaved) {
                showSnackBar(getResources().getString(R.string.project_save_success));
                setProjectOpened((int)idProject,nameProject);
                isNewProject = true;
            }
            else {
                showSnackBar(getResources().getString(R.string.project_save_error));
            }
        }


        if ( isNewProject )
            changeToSpreadsheetScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //pushFragment(HomeFragment.newInstance());
        user = getSessionUser();
        user.getListOfProjects(getApplicationContext(), HomeActivity.this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.generate_relatorio:
                getProjectDetails();
                return true;
            case R.id.generate_excel:
                checkPermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccessGettingProjects(List<Project> projectList) {
        this.projectList = projectList;

        if ( !projectList.isEmpty() ){
            btnNewProject.setVisibility(View.GONE);
            if (!isNewProject)
                pushFragment(HomeFragment.newInstance());
        }else{
            btnNewProject.setVisibility(View.VISIBLE);
            TapTargetView.showFor(this,
                    TapTarget.forView(findViewById(R.id.btn_new_project_home),  //the view
                            getResources().getString(R.string.title_onboard_no_project), "") //title and description
                            // All options below are optional
                            .outerCircleAlpha(0.42f)            // Specify the alpha amount for the outer circle
                            .targetCircleColor(R.color.white)   // Specify a color for the target circle
                            .titleTextSize(18)                  // Specify the size (in sp) of the title text
                            .titleTextColor(R.color.white)      // Specify the color of the title text
                            .descriptionTextSize(12)            // Specify the size (in sp) of the description text
                            .descriptionTextColor(R.color.light_grey)  // Specify the color of the description text
                            .textColor(R.color.white)            // Specify a color for both the title and description text
                            .textTypeface(Typeface.SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(true)                   // Whether to tint the target view's color
                            .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)// Specify a custom drawable to draw as the target
                            .targetRadius(60),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            //btnNewProject.performClick();
                            Intent it = new Intent(getApplicationContext(), NewProjectActivity.class);
                            startActivityForResult(it, Constants.REQUEST_CODE_NEW_PROJECT);
                        }
                    });
        }

        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_reports)
            changeToReportScreen();
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() != R.id.navigation_home)
            changeToHomeScreen();
        else
            super.onBackPressed();
    }

    public void setProjectOpened(int idProject, String nameProject){
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setCurrentProject(idProject,nameProject);
    }

    public String getNameProjectOpened(){
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        return sessionManager.getNameCurrentProject();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        if (requestCode == Constants.OPEN_CHART) {
            // Make sure the request was successful
            if (resultCode == Constants.RESULT_NO_DATA) {
                showSnackBar("Não há dados inserido na planilha");
            }
        }
    }

}
