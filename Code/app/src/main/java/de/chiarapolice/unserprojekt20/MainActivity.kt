package de.chiarapolice.unserprojekt20

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.chiarapolice.unserprojekt20.Inventar.Inventar
import de.chiarapolice.unserprojekt20.Inventar.InventarAdapter
import de.chiarapolice.unserprojekt20.Shoppinglist.ShoppinglistActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var myDBHelper: MyDBHelper
    }

    var inventarlist = ArrayList<Inventar>()
    val displayList = ArrayList<Inventar>()
    lateinit var adapter: RecyclerView.Adapter<InventarAdapter.ViewHolder>
    lateinit var rv: RecyclerView
    lateinit var toolbar: ActionBar

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.inventar -> {
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.shopplinglist -> {
                val intent = Intent(this@MainActivity, ShoppinglistActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
        toolbar = supportActionBar!!

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(navigation)

        displayList.addAll(inventarlist)
        adapter = InventarAdapter(this, displayList)
        myDBHelper = MyDBHelper(this, null, null, 1)

        viewProducts()

        fab.setOnClickListener {
            val intent = Intent(this, BarcodeScanner::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search, menu)
        val menuItem1 = menu!!.findItem(R.id.search)

        menuInflater.inflate(R.menu.bottom_app_bar, menu)

        //Suchfunktion
        if (menuItem1 != null) {

            val searchView = menuItem1.actionView as SearchView
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
                        for (i in 0..inventarlist.size - 1) {
                            if (inventarlist.get(i).invProductname!!.toLowerCase()
                                    .contains(newText.toString().toLowerCase())
                            )
                                displayList.add(inventarlist[i])
                        }
                        adapter = InventarAdapter(this@MainActivity, displayList)
                        rv.adapter = adapter

                    } else { //if empty
                        displayList.clear()
                        adapter = InventarAdapter(this@MainActivity, inventarlist)
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
    private fun viewProducts() {
        inventarlist = myDBHelper.getProducts(this)
        adapter = InventarAdapter(this, inventarlist)
        rv = findViewById(R.id.rv)
        rv.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager
        rv.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv.adapter as InventarAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        var itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv)

    }

    override fun onResume() {
        viewProducts()
        super.onResume()
    }

}


