package com.salmi.quran

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.main_green.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var windows: Window
    private lateinit var toolbar: Toolbar
    private var themeValue : Boolean = false
    private var styleValue: Int = 0
    private val arrayName = arrayListOf<String>()
    private val arrayLink = arrayListOf<String>()
    private lateinit var progressBar: ProgressBar
    private lateinit var listView: ListView
    private val requestCodePermission = 19911509
    private val cic = CheckInternet(this@MainActivity)
    private lateinit var adapter: ListAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferencesStyle = getSharedPreferences("FONTS", Context.MODE_PRIVATE)
        styleValue = preferencesStyle.getInt("font", 3)
        if (this.styleValue == 0) {
            setTheme(R.style.AppTheme_1)
        } else if (this.styleValue == 1) {
            setTheme(R.style.AppTheme_2)
        } else if (this.styleValue == 2) {
            setTheme(R.style.AppTheme_3)
        } else if (this.styleValue == 3) {
            setTheme(R.style.AppTheme_4)
        } else if (this.styleValue == 4) {
            setTheme(R.style.AppTheme_5)
        }
        super.onCreate(savedInstanceState)
        windows = this@MainActivity.window

        val preferencesTheme = getSharedPreferences("THEMES", Context.MODE_PRIVATE)
        this.themeValue = preferencesTheme!!.getBoolean("theme", false)
        if (this.themeValue == false) {
            windows.statusBarColor = resources.getColor(R.color.green)
            windows.navigationBarColor = resources.getColor(R.color.green)
            setContentView(R.layout.main_green)
        } else {
            windows.statusBarColor = resources.getColor(R.color.dark)
            windows.navigationBarColor = resources.getColor(R.color.dark)
            setContentView(R.layout.main_dark)
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val directionLayout = resources.configuration
        if (directionLayout.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            if (this.themeValue == false) {
                toolbar.setNavigationIcon(R.drawable.ic_forward_black)
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_forward_white)
            }
        } else {
            if (this.themeValue == false) {
                toolbar.setNavigationIcon(R.drawable.ic_back_black)
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_back_white)
            }
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }

        progressBar = findViewById(R.id.progressBar)
        listView = findViewById(R.id.listView)

        refreshLayout = findViewById(R.id.swiperefresh)
        refreshLayout.setOnRefreshListener {
            if (cic.isConnectingToInternet) {
                refreshData()
            } else {
                InternetEnableSheet(this@MainActivity)
                refreshLayout.isRefreshing = false
            }
        }
        //Internet Check
        if (cic.isConnectingToInternet) {
            progressBar.visibility = View.VISIBLE
            refreshData()
        } else {
            InternetEnableSheet(this@MainActivity)
        }

        ViewCompat.setNestedScrollingEnabled(listView, true)
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            var mLastFirstVisibleItem = 5
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

            }
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (view.getId() == listView.getId()) {
                    val currentFirstVisibleItem = listView.getFirstVisiblePosition()
                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        //toolbar.animate().translationY(toolbar.top.toFloat()).setInterpolator(AccelerateInterpolator()).start()
                        supportActionBar!!.hide()
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        //toolbar.animate().translationY(toolbar.bottom.toFloat()).setInterpolator(DecelerateInterpolator()).start()
                        supportActionBar!!.show()
                    }
                    mLastFirstVisibleItem = currentFirstVisibleItem
                }
            }
        })

        //ListView Click Item
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Task(i).execute()
        }
        ///=============long Click download=============
        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val mPosition = position
            var alertTheme: Int? = null
            if (this.themeValue == false) alertTheme = AlertDialog.THEME_HOLO_LIGHT else alertTheme = AlertDialog.THEME_HOLO_DARK
            val builder = AlertDialog.Builder(this@MainActivity, alertTheme)
            builder.setTitle("هل تريد تحميل صورة " + arrayName[mPosition] + " ؟")
            builder.setCancelable(false)
            builder.setPositiveButton(R.string.yes) { dialog, which ->
                try {
                    if (checkPermission() == false) {
                        checkPermission()
                    } else {
                        val ch = CheckInternet(this@MainActivity)
                        if (ch.isConnectingToInternet) {
                            DownloadManagerNotification(this@MainActivity, refreshLayout,
                                    arrayLink[mPosition], arrayName[mPosition], mPosition.toString())
                        } else {
                            InternetEnableSheet(this@MainActivity)
                        }
                    }
                } catch (e: Exception) {
                    ErrorsMessage().showMessage(this@MainActivity.mainlayout, e.message.toString())
                }
            }
            builder.setNegativeButton(R.string.no) { dialogInterface, i -> }
            builder.show()
            false
        }
    }

    //function get and refresh Data
    private fun refreshData() {
        arrayName.clear()
        arrayLink.clear()
        // Read from the database
        val fireData = FirebaseDatabase.getInstance().getReference("sorah")
        fireData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (getData in dataSnapshot.children) {
                    val value = getData.getValue(User::class.java)
                    arrayName.add(value!!.soraName!!)
                    arrayLink.add(value.soraLink!!)
                }
                listView.animation = Animations(this@MainActivity).listViewAnim()
                adapter = ListAdapter(this@MainActivity, arrayName)
                listView.adapter = adapter
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                listView.visibility = View.VISIBLE

                if (refreshLayout.isRefreshing) {
                    refreshLayout.isRefreshing = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
               ErrorsMessage().showMessage(this@MainActivity.refreshLayout, error.message)
            }
        })
    }

    //////===============Permissions=================
    internal fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        requestCodePermission)
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            requestCodePermission -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "PERMISSION GRANTED",
                        Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this@MainActivity, "PERMISSION DENIED",
                        Toast.LENGTH_SHORT).show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /////=========================End RunTime Premissions=================

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        Search Item
        menuInflater.inflate(R.menu.menu_main, menu)
        if (this.themeValue == false) {
            menu!!.findItem(R.id.toast_download).setIcon(R.drawable.ic_download_black)
            menu!!.findItem(R.id.action_settings).setIcon(R.drawable.ic_more_black)
            menu!!.findItem(R.id.action_search).setIcon(R.drawable.ic_search_black)
        } else {
            menu!!.findItem(R.id.toast_download).setIcon(R.drawable.ic_download_white)
            menu!!.findItem(R.id.action_settings).setIcon(R.drawable.ic_more_white)
            menu!!.findItem(R.id.action_search).setIcon(R.drawable.ic_search_white)
        }

        val searchItem = menu!!.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setSubmitButtonEnabled(true)
        searchView.setQueryHint("إبحث")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter("${newText}")
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> try {
                startActivity(Intent(this@MainActivity, CustomSettings::class.java))
                finish()
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@MainActivity.mainlayout, e.message.toString())
            }
            R.id.toast_download -> try {
                checkPermission()
                Snackbar.make(findViewById(R.id.swiperefresh), "للتحميل إضغط مطولا على اسم الصورة.",
                        Snackbar.LENGTH_LONG).show()
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@MainActivity.mainlayout, e.message.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class Task(getPosition: Int) : AsyncTask<Unit, Unit, Unit>() {
        val position = getPosition
        override fun onPreExecute() {
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Unit?) {
            try {
                ///Use Offline From Localy
                val path = "/mnt/sdcard/Elkouchi/"
                val file = File("${path + position}.mp3")
                var intPosission = Intent(this@MainActivity, MainPlayer::class.java)
                val nName = arrayName[position]
                val nLink = arrayLink[position]

                if (file.exists() && position == position) {
                    intPosission.putExtra("link", "${path + position}.mp3")
                    intPosission.putExtra("name", "$nName من الجهاز")
                    startActivity(intPosission)
                } else {
                    when (cic.isConnectingToInternet) {
                        true -> {
                            intPosission = Intent(this@MainActivity, MainPlayer::class.java)
                            intPosission.putExtra("link", nLink)
                            intPosission.putExtra("name", nName)
                            startActivity(intPosission)
                        }
                        false -> {
                            InternetEnableSheet(this@MainActivity)
                        }
                    }
                }
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@MainActivity.mainlayout, e.message.toString())
            }
        }

        override fun onPostExecute(result: Unit?) {
            if (progressBar.isEnabled) {
                progressBar.visibility = View.GONE
            }
            super.onPostExecute(result)
        }

    }

}

