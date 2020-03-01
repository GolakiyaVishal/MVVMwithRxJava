package go.task.mvvmwithrxjava.viewmodel

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.AndroidViewModel
import go.task.mvvmwithrxjava.data.Repository
import go.task.mvvmwithrxjava.model.Enterprise
import go.task.mvvmwithrxjava.ui.main.MainAdapter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val thisModel = application
    val adapter: MainAdapter = MainAdapter()
    private val _repository = Repository(application as Context)
    private val disposable = CompositeDisposable()
    var mailList : List<Enterprise>? = null

    fun getEntData(): Single<List<Enterprise>> {
        return _repository.getEntData()
    }

    private fun getObserver(): DisposableSingleObserver<List<Enterprise>> {
        return object : DisposableSingleObserver<List<Enterprise>>() {
            override fun onSuccess(@NonNull email: List<Enterprise>) {
                Collections.sort(email)
                mailList = email
                adapter.notifyDataChange(email)
            }

            override fun onError(@NonNull e: Throwable) {}
        }
    }

    fun getAllEmails() {
        val subscription: DisposableSingleObserver<List<Enterprise>>? = getEntData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(getObserver())

        disposable.add(subscription as Disposable)
    }

    fun onStop() {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    fun setupSearch(searchView: SearchView){
        val subscription: Disposable? = searchObservable(searchView)
            ?.filter(Predicate { text ->
                if (text.isEmpty()) {
                    adapter.notifyDataChange(listOf())
                    false
                } else {
                    true
                }
            })
            ?.distinctUntilChanged()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(Consumer<List<Enterprise>>() {
                adapter.notifyDataChange(it)
            })

        disposable.add(subscription!!)
    }

    private fun searchObservable(searchView: SearchView): Observable<List<Enterprise>>? {
        val subject = PublishSubject.create<List<Enterprise>>()
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                val ll = mutableListOf<Enterprise>()
                for(ent in mailList!!){
                    if(ent.title!!.contains(text)){
                        ll.add(ent)
                    }
                }
                subject.onNext(ll)
                return true
            }
        })
        return subject
    }
}