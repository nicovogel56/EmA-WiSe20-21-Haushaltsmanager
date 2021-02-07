package de.chiarapolice.unserprojekt20.Shoppinglist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.chiarapolice.unserprojekt20.MyDBHelper
import de.chiarapolice.unserprojekt20.R
import kotlinx.android.synthetic.main.activity_add_product.BtnDecrement
import kotlinx.android.synthetic.main.activity_add_product.BtnIncrement
import kotlinx.android.synthetic.main.activity_add_product.btnAdd
import kotlinx.android.synthetic.main.activity_add_product.name
import kotlinx.android.synthetic.main.activity_add_product.number
import kotlinx.android.synthetic.main.activity_add_product.price
import kotlinx.android.synthetic.main.activity_add_shop_product.*
import kotlin.text.Typography.cent
import kotlin.text.Typography.euro

class AddShopProductActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop_product)

        findViewById<TextView>(R.id.name).text.toString()
        findViewById<NumberPicker>(R.id.euro_picker)
        findViewById<NumberPicker>(R.id.cent_picker)
        findViewById<TextView>(R.id.price).text.toString()
        findViewById<TextView>(R.id.number)


        val barcode = intent.getStringExtra("Code")
        var helper = MyDBHelper(applicationContext, null, null, 1)
        var db = helper.readableDatabase


        var num = 0
        BtnIncrement.setOnClickListener {
            num++
            number.text = num.toString()
        }

        BtnDecrement.setOnClickListener {
            num--
            number.text = num.toString()
        }

        euro_picker.minValue = 0
        euro_picker.maxValue = 500
        cent_picker.minValue = 0
        cent_picker.maxValue = 99

        var euro = 0
        var cent = 0

        euro_picker.setOnValueChangedListener { picker, oldVal, newVal ->
            euro = euro_picker.value
        }

        cent_picker.setOnValueChangedListener{ numberPicker, i, i2 ->
            cent = cent_picker.value
        }

        btnAdd.setOnClickListener {
            if (name.text.isEmpty()) {
                Toast.makeText(this, "Produktname bitte eingeben!", Toast.LENGTH_SHORT).show()
            }else {

                val shopping = Shoppinglist(
                    shopID = 0,
                    shopProductname = "",
                    shopPrice = 0.0,
                    shopNumber = 0
                )
                price.text = "$euro.$cent"

                shopping.shopProductname = name.text.toString()
                shopping.shopPrice = price.text.toString().toDouble()
                shopping.shopNumber = number.text.toString().toInt()
                ShoppinglistActivity.myDBHelper.addShopProducts(this, shopping)


                val intent = Intent(this, ShoppinglistActivity::class.java)
                startActivity(intent)

            }
            db.close()


        }
}
}





