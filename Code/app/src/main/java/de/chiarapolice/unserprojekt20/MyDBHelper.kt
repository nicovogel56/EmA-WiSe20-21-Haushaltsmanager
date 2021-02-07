package de.chiarapolice.unserprojekt20


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import de.chiarapolice.unserprojekt20.Inventar.Inventar
import de.chiarapolice.unserprojekt20.Shoppinglist.Shoppinglist

class MyDBHelper(
    context: Context,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, "MYDB", factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "MYDB"
        private val DATABASE_VERSION = 1

        val PRODUCTS_TABLE_NAME = "Products"
        val BARCODE = "Barcode"
        val PRODUCTNAME = "Productname"
        val PRODUCTBRAND = "Productbrand"
        val PRICE = "Price"

        val INVENTAR_TABLE_NAME = "Inventar"
        val COLUMN_INVBARCODE = "InvBarcode"
        val COLUMN_INVNAME = "InvProductname"
        val COLUMN_INVBRAND = "InvProductbrand"
        val COLUMN_INVPRICE = "InvPrice"
        val COLUMN_INVNUMBER = "InvNumber"

        val SHOPPING_TABLE_NAME = "Shoppinglist"
        val COLUMN_SHOPID = "ShopID"
        val COLUMN_SHOPNAME = "ShopProductname"
        val COLUMN_SHOPPRICE = "ShopPrice"
        val COLUMN_SHOPNUMBER = "ShopNumber"


    }

    override fun onCreate(db: SQLiteDatabase?) {

        //Erstellen von Tabelle Products
        db?.execSQL("CREATE TABLE PRODUCTS (BARCODE LONG PRIMARY KEY, PRODUCTNAME TEXT, PRODUCTBRAND TEXT, PRICE DOUBLE)")
        //Tabelle Products mit Datensätzen füllen
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('42329930', 'Handcreme', 'treaclemoon', '2.50')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('42376118', 'Mineralwasser Still ohne Kohlensäure', 'Saskia', '0.19')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('20982812', 'Flüssigseife Holunder', 'Cien', '0.69')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('4058172275340', 'Cremedusche Berries', 'Balea', '0.95')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('4337185443428', 'Haltbare Fettarme Milch', 'K Classic', '0.71')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('20422424', 'Der Leichte Tomaten Ketchup', 'Kania', '0.79')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('807680951788', 'Pesto Ricotta e Noci alla Siciliana', 'Barilla', '2.99')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('4026600011952', 'Zahncreme Extra White', 'Odol-med3', '0.80')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('42143673', 'Apfelschorle', 'Solevita', '0.48')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('433718549908', 'Apfelsaft', 'K Classic', '0.79')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('42114321', 'Müllermilch Schoko', 'Müller', '0.89')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('4002971043006', 'Almighurt Schoko Balls Crunchy Vanilla', 'Almighurt', '0.28')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('8719200038080', 'LÄTTA Joghurt', 'LÄTTA', '3.75')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('20117221', 'Hirtenkäse classic', 'Milbona', '0.99')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('4056489220572', 'Choco Duo', 'Mister Choc', '1.79')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('4000406073147', 'Roggenmehl Type 1150', 'Aurora', '1.66')")
        db?.execSQL("INSERT INTO PRODUCTS(BARCODE, PRODUCTNAME, PRODUCTBRAND, PRICE) VALUES('4337185448478', 'Haltbare Vollmilch', 'K Classic', '0.71')")

        //Erstellen von Tabelle Inventar
        db?.execSQL("CREATE TABLE Inventar (InvBarcode LONG PRIMARY KEY, InvProductname TEXT, InvProductbrand TEXT, InvPrice DOUBLE, InvNumber INTEGER)")

        //Erstellen von Tabelle Shoppinglist
        db?.execSQL("CREATE TABLE Shoppinglist (ShopID INTEGER PRIMARY KEY AUTOINCREMENT, ShopProductname TEXT, ShopPrice DOUBLE, ShopNumber INTEGER)")

    }

    // Tabellen werden gelöscht und oben nochmal erstellt, sobald sich die Versionsnummer erhöht
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("DROP TABLE IF EXISTS PRODUCTS")
            db?.execSQL("DROP TABLE IF EXISTS Inventar")
            db?.execSQL("DROP TABLE IF EXISTS Shoppinglist")

        }
    }

    fun getProducts(mCtx: Context): ArrayList<Inventar> {
        val qry = "Select * From INVENTAR"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val inventarlist = ArrayList<Inventar>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "Keine Einträge gefunden!", Toast.LENGTH_SHORT).show()
        else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val inventar = Inventar(
                    invCode = 0,
                    invProductname = "",
                    invProductbrand = "",
                    invPrice = 0.0,
                    invNumber = 0
                )
                inventar.invCode = cursor.getLong(cursor.getColumnIndex(COLUMN_INVBARCODE))
                inventar.invProductname = cursor.getString(cursor.getColumnIndex(COLUMN_INVNAME))
                inventar.invProductbrand = cursor.getString(cursor.getColumnIndex(COLUMN_INVBRAND))
                inventar.invPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_INVPRICE))
                inventar.invNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_INVNUMBER))
                inventarlist.add(inventar)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return inventarlist
    }

    fun getShopProducts(mCtx: Context): ArrayList<Shoppinglist> {
        val qry = "Select * From Shoppinglist"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val shoppinglist = ArrayList<Shoppinglist>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "Keine Einträge gefunden!", Toast.LENGTH_SHORT).show()
        else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val shopping = Shoppinglist(
                    shopID = 0,
                    shopProductname = "",
                    shopPrice = 0.0,
                    shopNumber = 0
                )
                shopping.shopID = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPID))
                shopping.shopProductname = cursor.getString(cursor.getColumnIndex(COLUMN_SHOPNAME))
                shopping.shopPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_SHOPPRICE))
                shopping.shopNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPNUMBER))
                shoppinglist.add(shopping)
                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()
        return shoppinglist
    }

    fun addProducts(mCtx: Context, inventar: Inventar) {
        val values = ContentValues()
        values.put(COLUMN_INVBARCODE, inventar.invCode)
        values.put(COLUMN_INVNAME, inventar.invProductname)
        values.put(COLUMN_INVBRAND, inventar.invProductbrand)
        values.put(COLUMN_INVPRICE, inventar.invPrice)
        values.put(COLUMN_INVNUMBER, inventar.invNumber)
        val db = this.writableDatabase
        try {
            db.insert(INVENTAR_TABLE_NAME, null, values)
            Toast.makeText(mCtx, "Produkt wurde zum Inventar hinzugefügt", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun addShopProducts(mCtx: Context, shopping: Shoppinglist) {
        val values = ContentValues()
        values.put(COLUMN_SHOPNAME, shopping.shopProductname)
        values.put(COLUMN_SHOPPRICE, shopping.shopPrice)
        values.put(COLUMN_SHOPNUMBER, shopping.shopNumber)
        val db = this.writableDatabase
        try {
            db.insert(SHOPPING_TABLE_NAME, null, values)
            Log.i("values hat die Werte: ", "$values")
            Toast.makeText(
                mCtx,
                "${shopping.shopProductname} wurde hinzugefügt",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteProduct(invCode: Long): Boolean {

        val db = this.writableDatabase
        var result: Boolean = false
        try {
            val cursor = db.delete(
                INVENTAR_TABLE_NAME,
                "$COLUMN_INVBARCODE  = ?",
                arrayOf(invCode.toString())
            )

            result = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Löschen fehlgeschlagen")
        }
        db.close()
        return result
    }

    fun deleteShopProduct(shopID: Int): Boolean {
        val db = this.writableDatabase
        var result: Boolean = false
        try {
            val cursor = db.delete(
                SHOPPING_TABLE_NAME,
                "$COLUMN_SHOPID  = ?",
                arrayOf(shopID.toString())
            )

            result = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Löschen fehlgeschlagen")
        }
        db.close()
        return result
    }

    fun updateProduct(
        invCode: String,
        invProductname: String,
        invProductbrand: String,
        invPrice: String,
        invNumber: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result: Boolean = false
        contentValues.put(COLUMN_INVNAME, invProductname)
        contentValues.put(COLUMN_INVBRAND, invProductbrand)
        contentValues.put(COLUMN_INVPRICE, invPrice.toDouble())
        contentValues.put(COLUMN_INVNUMBER, invNumber.toInt())
        try {
            db.update(
                INVENTAR_TABLE_NAME,
                contentValues,
                "$COLUMN_INVBARCODE = ?",
                arrayOf(invCode)
            )
            result = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Update fehlgeschlagen")
            result = false
        }
        db.close()
        return result
    }

    fun updateShopProduct(
        shopID: String,
        shopProductname: String,
        shopPrice: Double,
        shopNumber: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result: Boolean = false
        contentValues.put(COLUMN_SHOPNAME, shopProductname)
        contentValues.put(COLUMN_SHOPPRICE, shopPrice)
        contentValues.put(COLUMN_SHOPNUMBER, shopNumber.toInt())
        try {
            db.update(
                SHOPPING_TABLE_NAME,
                contentValues,
                "$COLUMN_SHOPID = ?",
                arrayOf(shopID)
            )
            result = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Update fehlgeschlagen")
            result = false
        }
        db.close()
        return result
    }

}