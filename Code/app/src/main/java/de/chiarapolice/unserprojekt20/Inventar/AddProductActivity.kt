package de.chiarapolice.unserprojekt20.Inventar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.chiarapolice.unserprojekt20.*
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_add_product.code

class AddProductActivity : AppCompatActivity() {


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        findViewById<TextView>(R.id.code)
        findViewById<TextView>(R.id.name).text.toString()
        findViewById<TextView>(R.id.brand).text.toString()
        findViewById<TextView>(R.id.price)
        findViewById<TextView>(R.id.number)


        val barcode = intent.getStringExtra("Code")
        var helper = MyDBHelper(applicationContext, null, null, 1)
        var db = helper.readableDatabase

        code.text = barcode

        fab.setOnClickListener {
            //Aufrufen des Barcode Scanners
            val intent = Intent(this, BarcodeScanner::class.java)
            startActivity(intent)
        }

        var num = 0
        BtnIncrement.setOnClickListener {
            num++
            number.text = num.toString()
        }

        BtnDecrement.setOnClickListener {
            num--
            number.text = num.toString()

        }

        //Überprüft ob das eingescannte Produkt in der Tabelle enthalten ist
        check_btn.setOnClickListener {
            var args = listOf<String>(code.text.toString()).toTypedArray()
            var rs = db.rawQuery("SELECT * FROM PRODUCTS WHERE BARCODE = ?", args)

            if (rs.moveToNext()) {
                Toast.makeText(applicationContext, "Produkt gefunden", Toast.LENGTH_LONG).show()

                name.setText(rs.getString(1))
                brand.setText(rs.getString(2))
                price.setText(rs.getString(3))


            } else {
                Toast.makeText(applicationContext, "Produkt nicht gefunden", Toast.LENGTH_LONG)
                    .show()
            }
        }

        btnAdd.setOnClickListener {
            if (code.text.isEmpty()) {
                Toast.makeText(this, "Produkt bitte einscannen!", Toast.LENGTH_SHORT).show()
            } else {

                val inventar = Inventar(
                    invCode = 0,
                    invPrice = 0.0,
                    invProductbrand = "",
                    invProductname = "",
                    invNumber = 0
                )
                //Produktdaten werden aus Datenbank ausgelesen
                inventar.invCode = code.text.toString().toLong()
                inventar.invProductname = name.text.toString()
                inventar.invProductbrand = brand.text.toString()
                inventar.invPrice = price.text.toString().toDouble()
                inventar.invNumber = number.text.toString().toInt()

                //Produktdaten werden an die Funktion addProducts übergeben
                MainActivity.myDBHelper.addProducts(this, inventar)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            db.close()


        }
    }
}




