package com.decagon.android.sq007.pokemonapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decagon.android.sq007.R

import com.decagon.android.sq007.pokemonapp.data.Result
import com.decagon.android.sq007.pokemonapp.ui.PokiDetailActivity
import java.util.*


class RecyclerAdapter(val context: Context): RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    private var pokemonList: List<Result> = listOf()
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvRecyclerItemPokeyName = itemView.findViewById<TextView>(R.id.tvPoikiName)
        val ivRecyclerItemImg = itemView.findViewById<ImageView>(R.id.ivPokiImage)

        var currentPokey: Result? = null
        var currentPos = 0
        var currentUrl = ""
        var currentId = 0


        // The onClickListener is set on the init here in the MyViewHolder inner class
        // This is used to launch a new activity which goes along with the necessary information.

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PokiDetailActivity::class.java).apply {
                    putExtra("NAME", currentPokey?.name)
                    putExtra("URL", currentUrl)
                    putExtra("ID", currentId)
                }
                itemView.context.startActivity(intent)
            }
        }


        fun setData(pokey: Result?, position: Int) {
            tvRecyclerItemPokeyName.text = pokey!!.name.toUpperCase(Locale.ROOT)
            val pokeyUrl = pokey.url
            val imgUrl = getPokeyId(pokeyUrl)
            this.currentUrl = imgUrl
            this.currentPokey = pokey
            this.currentPos = position
            this.currentId = pokeyUrl.substring(34, pokeyUrl.length - 1).toInt()

            Glide.with(context).load(imgUrl).into(ivRecyclerItemImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokimon_item, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pokey = pokemonList[position]
        holder.setData(pokey, position)
    }

    fun setPokemonList(pokemonList: List<Result>) {
        this.pokemonList = pokemonList
    }

    private fun getPokeyId(item: String): String {
        val id = item.substring(34, item.length - 1).toInt()
        return "https://pokeres.bastionbot.org/images/pokemon/$id.png"
    }
}