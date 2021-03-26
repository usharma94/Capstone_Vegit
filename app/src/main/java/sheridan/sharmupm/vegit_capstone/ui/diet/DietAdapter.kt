package sheridan.sharmupm.vegit_capstone.ui.diet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.DietModel


class DietAdapter(
        private val dietList: List<DietModel>,
        private val onClickListener: OnClickListener
) :
    ListAdapter<DietModel, DietAdapter.DietViewHolder>(
            DiffCallback
    ) {

    class DietViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDiet: ImageView = itemView.findViewById(R.id.imgDiet)
        val dietName: TextView = itemView.findViewById(R.id.txtDiet)
        val dietDescription: TextView = itemView.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        return DietViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_diet,
                        parent,
                        false
                )
        )
    }


    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val item: DietModel = dietList[position]

//        holder.imgDiet.setImageResource(R.drawable.vegetarian)
        holder.dietName.text = item.dietName
        holder.dietDescription.text = item.dietDescription
        item.dietImage?.let { holder.imgDiet.setBackgroundResource(it) }


        holder.itemView.setOnClickListener{
            onClickListener.onClick(item)
           holder.itemView.setBackgroundResource(R.drawable.btn_custom)
//            holder.itemView.isSelected = holder.itemView.isSelected
        }

    }


    override fun getItemCount() = dietList.size

    class OnClickListener(val clickListener: (diet: DietModel) -> Unit) {
        fun onClick(diet: DietModel) = clickListener(diet)

    }

    companion object DiffCallback : DiffUtil.ItemCallback<DietModel>() {
        override fun areItemsTheSame(oldItem: DietModel, newItem: DietModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DietModel, newItem: DietModel): Boolean {
            return oldItem.dietName == newItem.dietName
        }
    }
}

/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.example.recyclersample.flowerList
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import sheridan.sharmupm.vegit_capstone.R
//import sheridan.sharmupm.vegit_capstone.models.DietModel
//
//class DietAdapter(private val onClick: (DietModel) -> Unit) :
//        ListAdapter<DietModel, DietAdapter.FlowerViewHolder>(FlowerDiffCallback) {
//
//    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
//    class FlowerViewHolder(itemView: View, val onClick: (DietModel) -> Unit) :
//            RecyclerView.ViewHolder(itemView) {
//        private val flowerTextView: TextView = itemView.findViewById(R.id.txtDiet)
//        private val flowerImageView: ImageView = itemView.findViewById(R.id.txtDescription)
//        private var currentFlower: DietModel? = null
//
//        init {
//            itemView.setOnClickListener {
//                currentFlower?.let {
//                    onClick(it)
//                }
//            }
//        }
//
//        /* Bind flower name and image. */
//        fun bind(flower: DietModel) {
//            currentFlower = flower
//
//            flowerTextView.text = flower.dietName
//            if (flower.dietImage != null) {
//                flowerImageView.setImageResource(flower.dietImage!!)
//            } else {
//                flowerImageView.setImageResource(R.drawable.ic_baseline_restaurant_menu_24)
//            }
//        }
//    }
//
//    /* Creates and inflates view and return FlowerViewHolder. */
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
//        val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_diet, parent, false)
//        return FlowerViewHolder(view, onClick)
//    }
//
//    /* Gets current flower and uses it to bind view. */
//    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
//        val flower = getItem(position)
//        holder.bind(flower)
//
//    }
//}
//
//object FlowerDiffCallback : DiffUtil.ItemCallback<DietModel>() {
//    override fun areItemsTheSame(oldItem: DietModel, newItem: DietModel): Boolean {
//        return oldItem == newItem
//    }
//
//    override fun areContentsTheSame(oldItem: DietModel, newItem: DietModel): Boolean {
//        return oldItem.dietName == newItem.dietName
//    }
//}