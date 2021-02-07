package de.chiarapolice.unserprojekt20.Shoppinglist

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.chiarapolice.unserprojekt20.Inventar.InventarAdapter
import de.chiarapolice.unserprojekt20.MainActivity
import de.chiarapolice.unserprojekt20.R
import de.chiarapolice.unserprojekt20.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.product_row.view.*
import kotlinx.android.synthetic.main.product_update.view.*

class ShoppingAdapter(mCtx: Context, val shoppinglist: ArrayList<Shoppinglist>) :
    RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    var mCtx = mCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtProductName = itemView.txtProductName
        val txtPrice = itemView.txtPrice
        val number = itemView.number
        lateinit var imageView: ImageView
        lateinit var text: TextView

    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.shoppinglist_row, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return shoppinglist.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val shopping: Shoppinglist = shoppinglist[p1]
        p0.txtProductName.text = shopping.shopProductname
        p0.txtPrice.text = shopping.shopPrice.toString()
        p0.number.text = shopping.shopNumber.toString()

        //Erstellen des Updatefensters
        p0.itemView.setOnClickListener {
            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.shopping_update, null)

            val txtProductname: TextView = view.findViewById(R.id.editUpProductname)
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

            txtProductname.text = shopping.shopProductname
            txtPrice.text = shopping.shopPrice.toString()
            number.text = shopping.shopNumber.toString()

            val builder = AlertDialog.Builder(mCtx).setTitle("Update Produkt Info")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                    //Produkt wird geupdatet
                    val isUpdate = ShoppinglistActivity.myDBHelper.updateShopProduct(
                        shopping.shopID.toString(),
                        view.editUpProductname.text.toString(),
                        view.editUpPrice.text.toString().toDouble(),
                        view.editUpNumber.text.toString()
                    )
                    if (isUpdate == true) {
                        shoppinglist[p1].shopProductname = view.editUpProductname.text.toString()
                        shoppinglist[p1].shopPrice = view.editUpPrice.text.toString().toDouble()
                        shoppinglist[p1].shopNumber = view.editUpNumber.text.toString().toInt()

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
        val shopping: Shoppinglist = shoppinglist[position]
        val shopProductname = shopping.shopProductname

        var alertDialog = AlertDialog.Builder(mCtx).setTitle("Warnung")
            .setMessage("Sind Sie sicher dass Sie : $shopProductname löschen möchten?")
            .setPositiveButton("Ja", DialogInterface.OnClickListener { dialog, which ->
                //Produkt wird gelöscht
                if (ShoppinglistActivity.myDBHelper.deleteShopProduct(shopping.shopID)) {
                    shoppinglist.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, shoppinglist.size)

                    Toast.makeText(mCtx, "$shopProductname gelöscht", Toast.LENGTH_SHORT).show()
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
