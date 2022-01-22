package com.bcebhagalpur.pdfconverter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class PdfListAdapter(val context: Context, private val itemList: ArrayList<File>) : RecyclerView.Adapter<PdfListAdapter.WorldViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.pdf_item_list,
            parent,
            false
        )
        return WorldViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: WorldViewHolder, position: Int) {
        val element = itemList[position]
        holder.pdfName.text=element.name
        holder.pdfImage.setImageResource(R.drawable.pdf_icon)
        val sizeInByte=element.length().toDouble()
        val sizeInMb=sizeInByte/(1024*1024)
//        val roundOf= (sizeInMb).roundToLong()
       val lastModeDate= Date(element.lastModified())
        val formatter=SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val originalDate=formatter.format(lastModeDate)
        holder.pdfLastModified.text=originalDate.toString()
        holder.pdfSize.text=sizeInMb.toString()
        holder.itemContent.setOnClickListener {
            Toast.makeText(context, element.name, Toast.LENGTH_SHORT).show()
            val intent=Intent(context,PdfViewActivity::class.java)
            intent.putExtra("pdfUri",element.path)
            intent.putExtra("file",element)
            intent.putExtra("pdfName",element.name)
            context.startActivity(intent)
        }
        holder.optionMenu.setOnClickListener {
            val view1: View = (context as FragmentActivity).layoutInflater.inflate(
                R.layout.files_bottomsheet_dialog,
                null
            )
            val dialog = BottomSheetDialog(context)
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
                    putExtra(Intent.EXTRA_STREAM, element.absolutePath.toUri())
                    type = "application/pdf"
                }
                context.startActivity(
                    Intent.createChooser(
                        shareIntent,
                        "sharing from pdf converter"
                    )
                )
//                shareFile(element.absoluteFile)
            }
            txtPdfTopName.text=element.name.toString()
            txtRename.setOnClickListener {
                val view2 = LayoutInflater.from(context).inflate(
                    R.layout.rename_dialog,
                    null
                )
                val alertDialog =  AlertDialog.Builder(context).create()
                val etRename=view2.findViewById<EditText>(R.id.etFileReName)
                val str=holder.pdfName.text.toString()
                val absolutStr = str.substring(0, str.length - 4)
                etRename.setText(absolutStr)

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save") { _, _ ->
                    val from: File = File(element.parentFile, element.name)
                    val to: File = File(element.parentFile, etRename.text.toString() + ".pdf")
                    from.renameTo(to)
                    holder.pdfName.setText(etRename.text.toString() + ".pdf")
                    txtPdfTopName.setText(etRename.text.toString() + ".pdf")
                }
                alertDialog.setView(view2)
                alertDialog.show()
            }
        }

//        Toast.makeText(context,"Loading pdf from device",Toast.LENGTH_SHORT).show()

    }
    class WorldViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pdfName:TextView=view.findViewById(R.id.txtPdfName)
        val pdfLastModified:TextView=view.findViewById(R.id.txtLastModified)
        val pdfSize:TextView=view.findViewById(R.id.txtSize)
        val pdfImage:ImageView=view.findViewById(R.id.imgPdf)
        val itemContent:LinearLayout=view.findViewById(R.id.linearItem)
        val optionMenu:ImageView=view.findViewById(R.id.optionMenu)
    }

    fun shareFile(myFile: File) {
        val install = Intent(Intent.ACTION_SEND)
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (myFile.exists()) {
            val apkURI = FileProvider.getUriForFile(
                context,
                context.applicationContext
                    .packageName + ".provider", myFile
            )
            install.setDataAndType(apkURI, "application/pdf")
            install.putExtra(
                Intent.EXTRA_SUBJECT,
                "Sharing File from Webkul..."
            );
            install.putExtra(Intent.EXTRA_TEXT, "Sharing File from Webkul to purchase items...");
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(Intent.createChooser(install, "Share File Webkul"))
        }
    }

}