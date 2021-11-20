package ru.krackdigger.retrofitapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.krackdigger.retrofitapp.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getJson()

        binding.swipeRefreshLayout.setOnRefreshListener {

            getJson()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    fun getJson() {

        if (isNetworkAvailable(this)) {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://dev-tasks.alef.im/task-m-001/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()

            val scalarService: APIService = retrofit.create(APIService::class.java)
            val stringCall: Call<String?>? = scalarService.getStringResponse("list.php")
            stringCall?.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                    if (response.isSuccessful()) {
                        val responseString: String = response.body().toString()
                        val gson = GsonBuilder().create()
                        val list = gson.fromJson<ArrayList<String>>(responseString,
                                object : TypeToken<ArrayList<String>>(){}.type)
                        createRecyclerView(list)
                    }
                }
                override fun onFailure(call: Call<String?>?, t: Throwable?) {}
            })
        } else {
            Toast.makeText(this,"Нет подключения к сети Интернет!",
                    Toast.LENGTH_LONG).show()
        }
    }

    fun createRecyclerView(list: List<String>) {

        val span_count: Int

        val manager = applicationContext
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        span_count = if (Objects.requireNonNull(manager).phoneType == TelephonyManager.PHONE_TYPE_NONE) {
            // Tablet
            3
        } else {
            // Phone
            2
        }

        binding.userList.layoutManager = LinearLayoutManager(this)
        binding.userList.layoutManager = GridLayoutManager(this, span_count)
        binding.userList.setItemViewCacheSize(20);
        binding.userList.adapter = CustomRecyclerAdapter(list)
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}