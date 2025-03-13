package com.example.rickandmorty.activity

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.ViewModel.MainViewModel
import com.example.rickandmorty.models.CharactersModelAPI

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView = findViewById<RecyclerView>(R.id.rview_characters)
        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 1)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        val searchView: SearchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.setSearchName(searchView.query.toString())
                return false
            }
        })

        mainViewModel.loadEpisodes()

        mainViewModel.SearchName.observe(this, Observer { name ->
            mainViewModel.loadCharacters(name)
        })

        mainViewModel.CharacterList.observe(this, Observer { list ->
            recyclerView.adapter = CharactersAdapter(list)
        })
    }
}


class CharactersAdapter(private val charactersList: List<CharactersModelAPI.Result>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class HumanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(character: CharactersModelAPI.Result) {

            val tvName = itemView.findViewById<TextView>(R.id.tvCharacterName)
            tvName.text = character.name
            tvName.paintFlags = tvName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            itemView.findViewById<TextView>(R.id.tvCharacterLastLocation).text =
                character.location.name

            itemView.findViewById<TextView>(R.id.tvCharacterFirstSeen).text = character.episode[0]

            Glide.with(itemView.context)
                .load(character.image)
                .into(itemView.findViewById(R.id.imgCharacterIcon))
        }
    }

    class AlienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(character: CharactersModelAPI.Result) {
            itemView.findViewById<TextView>(R.id.tvCharacterName).text = character.name
            itemView.findViewById<TextView>(R.id.tvCharacterLastLocation).text =
                character.location.name
            itemView.findViewById<TextView>(R.id.tvCharacterFirstSeen).text = character.episode[0]

            Glide.with(itemView.context)
                .load(character.image)
                .into(itemView.findViewById(R.id.imgCharacterIcon))
        }
    }

    class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(character: CharactersModelAPI.Result) {
            itemView.findViewById<TextView>(R.id.tvCharacterName).text = character.name
            itemView.findViewById<TextView>(R.id.tvCharacterLastLocation).text =
                character.location.name
            itemView.findViewById<TextView>(R.id.tvCharacterFirstSeen).text = character.episode[0]

            Glide.with(itemView.context)
                .load(character.image)
                .into(itemView.findViewById(R.id.imgCharacterIcon))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (CharacterType.fromInt(viewType)) {
            CharacterType.VIEW_TYPE_HUMAN -> HumanViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.character_item, parent, false)
            )

            CharacterType.VIEW_TYPE_ALIEN -> AlienViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.character_item, parent, false)
            )

            CharacterType.VIEW_TYPE_OTHER -> OtherViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.character_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val character = charactersList[position]
        when (holder) {
            is HumanViewHolder -> holder.bind(character)
            is AlienViewHolder -> holder.bind(character)
            is OtherViewHolder -> holder.bind(character)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (charactersList[position].species) {
            "Human" -> CharacterType.VIEW_TYPE_HUMAN.ordinal
            "Alien" -> CharacterType.VIEW_TYPE_ALIEN.ordinal
            else -> CharacterType.VIEW_TYPE_OTHER.ordinal
        }
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    enum class CharacterType(val value: Int) {
        VIEW_TYPE_HUMAN(0),
        VIEW_TYPE_ALIEN(1),
        VIEW_TYPE_OTHER(2);

        companion object {
            fun fromInt(value: Int) = entries.first { it.value == value }
        }
    }
}

