package ru.krackdigger.retrofitapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import ru.krackdigger.retrofitapp.databinding.ActivityImageBoxBinding

class ImageBox : AppCompatActivity() {

    private lateinit var binding: ActivityImageBoxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image: String? = intent.getStringExtra("message")
        binding.imageViewBig
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.baseline_download_black_48_2)
            .error(R.drawable.sharp_error_outline_black_48_2)
            .into(binding.imageViewBig)
    }
}