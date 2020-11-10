package com.greenlab.agromonitor.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.greenlab.agromonitor.R
import com.greenlab.agromonitor.entity.Project
import com.greenlab.agromonitor.entity.SpreadsheetValues
import com.greenlab.agromonitor.managers.SessionManager
import com.greenlab.agromonitor.utils.Constants
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


open class SummaryDialog(context: Context, val prodId: Int) : Dialog(context) {

    var currentProject: Project? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_summary)

        setupView()
    }

    private fun setupView() {
        val etPrice = findViewById<EditText>(R.id.etPrice)
        getTotalLostPt()

        etPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val num: String = s.toString()
                if (!num.isBlank()) {
                    val price = num.toFloat()
                    showResultMessage(price)
                }

            }
        })
    }

    private fun getOpenedProject(): Int {
        val sessionManager = SessionManager(context)
        return sessionManager.currentProject
    }

    @SuppressLint("StaticFieldLeak")
    open fun loadCurrentProject() {
        val idProject = getOpenedProject()
        val mProject = Project()
        mProject.id = idProject
        object : AsyncTask<Void?, Void?, Project?>() {

            override fun onPostExecute(proj: Project?) {
                currentProject = proj
                if (proj != null) {
                    loadValuesFromProject()
                }
            }

            override fun doInBackground(vararg params: Void?): Project? {
                return mProject.getActualProject(context)
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun loadValuesFromProject() {
        val idProject: Int = getOpenedProject()
        val project = Project()
        project.id = idProject
        object : AsyncTask<Void?, Void?, List<SpreadsheetValues?>?>() {

            override fun onPostExecute(spreadsheetValuesList: List<SpreadsheetValues?>?) {
                val valuesList = ArrayList<Float>()
                spreadsheetValuesList?.forEach {
                    if (it != null){
                        valuesList.add(it.value)
                    }
                }
                val tvPt = findViewById<TextView>(R.id.tvPt)
                val kgHaValue =  getKgHaValue(valuesList, currentProject ?: return)
                val dec = DecimalFormat("###,###,###,###,###,###,###.00")
                val finalStr = dec.format(kgHaValue).replace(",", ";").replace(".", ",").replace(";", ".")
                tvPt.text = finalStr
                //tvPt.text = String.format("%.2f", kgHaValue)

            }

            override fun doInBackground(vararg params: Void?): List<SpreadsheetValues?>? {
                return project.getProductValuesNotNullFromProject(context, prodId)
            }

        }.execute()
    }

    private fun getKgHaValue(barValues: ArrayList<Float>, proj: Project): Float {
        var sum = 0f
        val areaAmostral = proj.areaAmostral.toFloat()
        for (value in barValues) {
            sum += value
        }
        return if (proj.measureUnity == Constants.KILO) {
            getValueCorrigidoUmidade(sum * 10000f / areaAmostral, proj)
        } else {
            getValueCorrigidoUmidade(sum * 10000f / areaAmostral / 1000, proj)
        }
    }

    private  fun getValueCorrigidoUmidade(value: Float, proj: Project): Float {
        val umidade = proj.umidade
        val umidadeCoop = proj.umidadeCoop
        return value * ((100 - umidade) / (100 - umidadeCoop))
    }

    private fun getTotalLostPt() {
        val idProduct = loadCurrentProject()
    }

    private fun showResultMessage(price: Float){
        val tvResult = findViewById<TextView>(R.id.tvResult)
        tvResult.visibility = View.VISIBLE

        val total = calcLostPrice(price)
        if (total != null) {
            val locale = Locale("pt", "BR")
            val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
            tvResult.text = context.getString(R.string.perda_em_hectares, currencyFormatter.format(total))
        }
    }

    private fun calcLostPrice(price: Float): Float? {
        val tvPt = findViewById<TextView>(R.id.tvPt).text

        return if (!tvPt.isNullOrBlank()){
            val perdaTotal = tvPt.toString().replace(".", ";").replace(",", ".").replace(";", "").toFloat()
            (perdaTotal/60)*price
        }else{
            null
        }
    }
}