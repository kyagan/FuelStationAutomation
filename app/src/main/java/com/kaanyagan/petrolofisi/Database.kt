package com.kaanyagan.petrolofisi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object Database {

    val cars = mutableListOf(
        Car(1,"BMW","34 FF 422",FuelType.BENZIN,45.0,25.0),
        Car(2,"Mercedes","24 HG 251",FuelType.DIZEL,60.0,20.0),
        Car(3,"Renault","34 BZA 962",FuelType.OTOGAZ, 50.0, 45.0),
    )
    val pumps = mutableListOf(
        Pump(1,FuelType.BENZIN),
        Pump(2,FuelType.BENZIN),
        Pump(3,FuelType.OTOGAZ),
        Pump(4,FuelType.DIZEL),
        Pump(5,FuelType.DIZEL),
    )

}

@Parcelize
data class Car(val id:Int, val name:String, val plate:String, val fuelType:FuelType, val fuelCapacity:Double, var currentFuel:Double) : Parcelable{
    fun getListName() = "$name, $plate ($fuelType)"
}

@Parcelize
data class Pump(val id:Int, val fuelType:FuelType) : Parcelable{
    fun getListName() = "Pompa $id ($fuelType)"
}

enum class FuelType{
    BENZIN,
    DIZEL,
    OTOGAZ
}