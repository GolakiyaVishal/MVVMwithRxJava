package go.task.mvvmwithrxjava.ui.main

import android.os.Bundle
import android.view.Menu
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import go.task.mvvmwithrxjava.R
import go.task.mvvmwithrxjava.model.Enterprise
import go.task.mvvmwithrxjava.utils.Common
import go.task.mvvmwithrxjava.viewmodel.MainActivityViewModel
import go.task.mvvmwithrxjava.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var _viewModelFactory: ViewModelFactory? = null
    private var _viewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _viewModelFactory = ViewModelFactory(application)
        _viewModel = _viewModelFactory?.create(MainActivityViewModel::class.java)

        recycler_view.adapter = _viewModel?.adapter
        _viewModel?.getAllEmails()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchView: SearchView = menu!!.findItem(R.id.menu_search).actionView as SearchView
        _viewModel?.setupSearch(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        if (!Common.isOnline(this)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Problem")
            builder.setMessage(getString(R.string.alert_no_internet))
            builder.setIcon(R.drawable.ic_warning)
            builder.setCancelable(false)
            builder.create().show()
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        _viewModel?.onStop()
    }
}
