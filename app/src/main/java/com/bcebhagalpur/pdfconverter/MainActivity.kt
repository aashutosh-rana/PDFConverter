package com.bcebhagalpur.pdfconverter

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class MainActivity : AppCompatActivity() {

//    private lateinit var pdfView: PDFView
    private lateinit var recyclerPdf:RecyclerView
    private lateinit var adapter: PdfListAdapter
    private val itemListModel = arrayListOf<PdfListModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        pdfView=findViewById(R.id.pdfView)
        recyclerPdf=findViewById(R.id.recyclerPdf)
//        adapter= PdfListAdapter(this,itemListModel)

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            displayPdf()
        } else {
            requestPermission()
        }

    }

    private fun selectPdfFromStorage() {
        Toast.makeText(this, "selectPDF", Toast.LENGTH_LONG).show()
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(browseStorage, "Select PDF"), 100
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPdfFromStorage = data.data
//            pdfView.fromUri(selectedPdfFromStorage)
//                .defaultPage(0)
//                .spacing(10)
//                .load()
        }
    }

    private fun checkPermission(): Boolean {
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            200
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty()) {
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                    displayPdf()
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun findPdf(file: File): ArrayList<File> {

        val arrayList: ArrayList<File> = ArrayList();
        val files: Array<out File>? = file.listFiles()

            for (singlefile in files!!) {
               if (singlefile.isDirectory && !singlefile.isHidden){
//                   val itemList1=PdfListModel(singlefile.path,singlefile,singlefile.name)
                   arrayList.addAll(findPdf(singlefile))
            }else{
                if (singlefile.name.endsWith(".pdf")){
                    arrayList.add(singlefile)
//                    val item=PdfListModel(singlefile.path,singlefile,singlefile.name)
//                    itemListModel.add(item)
                }
               }
        }
        return arrayList
    }

    private fun displayPdf(){
        recyclerPdf.setHasFixedSize(true)
        recyclerPdf.layoutManager=LinearLayoutManager(this)
        val pdfList=ArrayList<File>()
       pdfList.addAll(findPdf(Environment.getExternalStorageDirectory()))
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            pdfList.sortWith(Comparator.comparing(File::lastModified).reversed())
        }else{
            pdfList.sort()
        }
        adapter=PdfListAdapter(this,pdfList)
        recyclerPdf.adapter=adapter
    }
}
