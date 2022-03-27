package com.berkecoban.casestudy.util

class Utils {
    companion object{
        fun generateImageUrl (id:Int):String{
            return "https://picsum.photos/300/300?random=$id&grayscale"
        }
    }


}