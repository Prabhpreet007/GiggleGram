package com.example.gigglegram

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gigglegram.databinding.ActivityMainBinding
import org.json.JSONObject
import java.util.Objects

class MainActivity : AppCompatActivity() {
    val url:String="https://meme-api.com/gimme"
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMeme()
    }
    private fun loadMeme(){
        val queue = Volley.newRequestQueue(this)
        findViewById<ProgressBar>(R.id.progressBar).visibility=View.VISIBLE

// Request a string response from the provided URL.

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val resoneObject=JSONObject(response)
                resoneObject.get("url")
//                binding.memeImage.
                Glide.with(this@MainActivity).load(resoneObject.get("url")).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                        return false
                    }


                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                    return false}
                }).into(binding.memeImage)

            },
            Response.ErrorListener {
                Toast.makeText(this,"Something went wront",Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    fun loadNext(view: View) {loadMeme()}
    fun memeShare(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this cool meme I got from Reddit $url")
        val chooser=Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
}
