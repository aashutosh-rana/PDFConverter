package com.bcebhagalpur.pdfconverter

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

class PdfViewActivity : AppCompatActivity() {

    private lateinit var pdfView:PDFView
    private lateinit var pdfName:TextView
    private lateinit var moreOption:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

        pdfView=findViewById(R.id.pdfView)
        pdfName=findViewById(R.id.pdfName)
        moreOption=findViewById(R.id.moreOption)

        val name=intent.getStringExtra("pdfName")
        val uri=File(intent.getStringExtra("pdfUri")!!)
        val file=intent.getSerializableExtra("file")

        pdfView.fromFile(uri)
            .defaultPage(0)
            .spacing(10)
            .load()

        pdfName.text=name
        optionMenu()

    }

    private fun optionMenu(){
        val pdfFile=File(intent.getStringExtra("pdfUri")!!)
        moreOption.setOnClickListener {
            val view1: View = (this).layoutInflater.inflate(
                    R.layout.pdf_view_bottomsheet,
                    null
            )
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view1)
            dialog.show()
            val txtPdfTopName:TextView=view1.findViewById(R.id.txtPdfTopName)
            val txtShare:TextView=view1.findViewById(R.id.txtShare)
            val txtAddStarred:TextView=view1.findViewById(R.id.txtAddStarred)
            val txtAddPassword:TextView=view1.findViewById(R.id.txtAddPassword)
            val txtMakeCopy:TextView=view1.findViewById(R.id.txtMakeCopy)
            val txtEditPdf:TextView=view1.findViewById(R.id.txtEditPdf)
            val txtInvertPdf:TextView=view1.findViewById(R.id.txtInvertPdf)
            val txtRename:TextView=view1.findViewById(R.id.txtRename)
            val txtDetails:TextView=view1.findViewById(R.id.txtDetails)
            val txtDelete:TextView=view1.findViewById(R.id.txtDelete)
            txtShare.setOnClickListener {
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, pdfFile.absolutePath.toUri())
                    type = "application/pdf"
                }
                startActivity(
                        Intent.createChooser(shareIntent, "sharing from pdf converter"))
//                shareFile(element.absoluteFile)
            }
            txtPdfTopName.text=pdfFile.name.toString()
            txtRename.setOnClickListener {
                val view2 = LayoutInflater.from(this).inflate(
                        R.layout.rename_dialog,
                        null
                )
                val alertDialog =  AlertDialog.Builder(this).create()
                val etRename=view2.findViewById<EditText>(R.id.etFileReName)
                val str= pdfName.text.toString()
                val absolutStr = str.substring(0, str.length - 4)
                etRename.setText(absolutStr)

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save") { _, _ ->
                    val from: File = File(pdfFile.parentFile, pdfFile.name)
                    val to: File = File(pdfFile.parentFile, etRename.text.toString() + ".pdf")
                    from.renameTo(to)
                    pdfName.setText(etRename.text.toString() + ".pdf")
                    txtPdfTopName.setText(etRename.text.toString() + ".pdf")
                }
                alertDialog.setView(view2)
                alertDialog.show()
            }
        }
    }
}