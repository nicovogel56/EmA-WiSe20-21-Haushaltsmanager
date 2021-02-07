package de.chiarapolice.unserprojekt20.Shoppinglist

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.chiarapolice.unserprojekt20.*
import kotlinx.android.synthetic.main.activity_main.*

class ShoppinglistActivity : AppCompatActivity() {

    companion object {
        lateinit var myDBHelper: MyDBHelper
    }

    var shoppinglist = ArrayList<Shoppinglist>()
    val displayList = ArrayList<Shoppinglist>()
    lateinit var adapter: RecyclerView.Adapter<ShoppingAdapter.ViewHolder>
    lateinit var rv: RecyclerView
    lateinit var toolbar: ActionBar



    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.inventar -> {
                val intent = Intent(this@ShoppinglistActivity, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.shopplinglist -> {
                val intent = Intent(this@ShoppinglistActivity, ShoppinglistActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoppinglist)

        myDBHelper = MyDBHelper(this, null, null, 1)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        toolbar = supportActionBar!!

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(navigation)

        displayList.addAll(shoppinglist)
        adapter = ShoppingAdapter(this, displayList)


        viewShopProducts()

        fab.setOnClickListener {
            val intent = Intent(this, AddShopProductActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search, menu)
        val menuItem = menu!!.findItem(R.id.search)

        //Suchfunktion
        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            val editText =
                searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint = "Suche..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!newText!!.isEmpty()) {
                        displayList.clear()
                        for (i in 0..shoppinglist.size - 1) {
                            if (shoppinglist.get(i).shopProductname!!.toLowerCase()
                                    .contains(newText.toString().toLowerCase())
                            )
                                displayList.add(shoppinglist[i])
                        }
                        adapter = ShoppingAdapter(this@ShoppinglistActivity, displayList)
                        rv.adapter = adapter

                    } else { //if empty
                        displayList.clear()
                        adapter = ShoppingAdapter(this@ShoppinglistActivity, shoppinglist)
                        rv.adapter = adapter
                    }
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("WrongConstant")
    private fun viewShopProducts() {
        shoppinglist = myDBHelper.getShopProducts(this)
        adapter = ShoppingAdapter(this, shoppinglist)
        rv = findViewById(R.id.rv)
        rv.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager
        rv.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv.adapter as ShoppingAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        var itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv)

    }

    override fun onResume() {
        viewShopProducts()
        super.onResume()
    }

}