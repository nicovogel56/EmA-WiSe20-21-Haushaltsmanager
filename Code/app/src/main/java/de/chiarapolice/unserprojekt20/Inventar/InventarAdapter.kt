package de.chiarapolice.unserprojekt20.Inventar

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import de.chiarapolice.unserprojekt20.MainActivity
import de.chiarapolice.unserprojekt20.MyDBHelper
import de.chiarapolice.unserprojekt20.R
import de.chiarapolice.unserprojekt20.Shoppinglist.Shoppinglist
import de.chiarapolice.unserprojekt20.Shoppinglist.ShoppinglistActivity
import kotlinx.android.synthetic.main.product_row.view.*
import kotlinx.android.synthetic.main.product_update.view.*

class InventarAdapter(mCtx: Context, val inventarlist: ArrayList<Inventar>) :
    RecyclerView.Adapter<InventarAdapter.ViewHolder>() {

    var mCtx = mCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code = itemView.code
        val txtProductName = itemView.txtProductName
        val txtProductBrand = itemView.txtProductBrand
        val txtPrice = itemView.txtPrice
        val number = itemView.number

    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.product_row, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return inventarlist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val inventar: Inventar = inventarlist[p1]
        p0.code.text = inventar.invCode.toString()
        p0.txtProductName.text = inventar.invProductname
        p0.txtProductBrand.text = inventar.invProductbrand
        p0.txtPrice.text = inventar.invPrice.toString()
        p0.number.text = inventar.invNumber.toString()

        //Erstellen des Updatefensters
        p0.itemView.setOnClickListener {
            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.product_update, null)

            val txtProductname: TextView = view.findViewById(R.id.editUpProductname)
            val txtProductbrand: TextView = view.findViewById(R.id.editUpProductbrand)
            val txtPrice: TextView = view.findViewById(R.id.editUpPrice)
            val number: TextView = view.findViewById(R.id.editUpNumber)
            val BtnIncrement: ImageView = view.findViewById(R.id.BtnIncrement)
            val BtnDecrement: ImageView = view.findViewById(R.id.BtnDecrement)
            var num = 0

            BtnIncrement.setOnClickListener {
                num++
                number.text = num.toString()
            }

            BtnDecrement.setOnClickListener {
                num--
                number.text = num.toString()

            }

            txtProductname.text = inventar.invProductname
            txtProductbrand.text = inventar.invProductbrand
            txtPrice.text = inventar.invPrice.toString()
            number.text = inventar.invNumber.toString()


            val builder = AlertDialog.Builder(mCtx).setTitle("Update Produkt Info")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                    val isUpdate = MainActivity.myDBHelper.updateProduct(
                        inventar.invCode.toString(),
                        view.editUpProductname.text.toString(),
                        view.editUpProductbrand.text.toString(),
                        view.editUpPrice.text.toString(),
                        view.editUpNumber.text.toString()
                    )
                    if (isUpdate == true) {
                        inventarlist[p1].invProductname = view.editUpProductname.text.toString()
                        inventarlist[p1].invProductbrand = view.editUpProductbrand.text.toString()
                        inventarlist[p1].invPrice = view.editUpPrice.text.toString().toDouble()
                        inventarlist[p1].invNumber = view.editUpNumber.text.toString().toInt()

                        notifyDataSetChanged()
                        Toast.makeText(mCtx, "Erfolgreich geupdatet", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mCtx, "Update fehlgeschlagen", Toast.LENGTH_SHORT).show()
                    }
                }).setNegativeButton("Abbrechen", DialogInterface.OnClickListener { dialog, which ->

                })
            val alert = builder.create()
            alert.show()

        }

    }

    //Erstellen des Löschfensters
    fun removeAt(position: Int) {
        val inventar: Inventar = inventarlist[position]
        val invProductname = inventar.invProductname

        var alertDialog = AlertDialog.Builder(mCtx).setTitle("Warnung")
            .setMessage("Sind Sie sicher dass Sie : $invProductname löschen möchten?")
            .setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, which ->
                if (MainActivity.myDBHelper.deleteProduct(inventar.invCode)) {
                    inventarlist.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, inventarlist.size)

                    Toast.makeText(mCtx, "$invProductname gelöscht", Toast.LENGTH_SHORT).show()

                    //weiteres Dialogfenster, wenn man Produkt gelöscht hat
                    var alertDialog = AlertDialog.Builder(mCtx).setTitle("Information")
                        .setMessage("Möchten Sie $invProductname auf Ihre Einkaufsliste setzen?")
                        .setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, which ->

                            val shopping = Shoppinglist(
                                shopID = 0,
                                shopProductname = "",
                                shopPrice = 0.0,
                                shopNumber = 0
                            )
                            shopping.shopProductname = inventar.invProductname
                            shopping.shopPrice = inventar.invPrice
                            shopping.shopNumber = inventar.invNumber

                            //Produktdaten werden an die Funktion addShopProducts übergeben, um sie auf die Einkaufsliste zu setzen
                            ShoppinglistActivity.myDBHelper.addShopProducts(mCtx, shopping)
                            Toast.makeText(
                                mCtx,
                                "$invProductname wurde zur Einkaufsliste hinzugefügt",
                                Toast.LENGTH_SHORT
                            ).show()

                        }).setNegativeButton(
                            "Nein",
                            DialogInterface.OnClickListener { dialog, which ->
                            }).show()

                } else
                    Toast.makeText(mCtx, "Löschen fehlgeschlagen", Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton("Nein", DialogInterface.OnClickListener { dialog, which ->
                notifyItemChanged(position)
            })
            .setIcon(R.drawable.ic_baseline_warning_24)
            .show()


    }

}