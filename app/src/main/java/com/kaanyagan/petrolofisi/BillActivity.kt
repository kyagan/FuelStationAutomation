package com.kaanyagan.petrolofisi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kaanyagan.petrolofisi.BuyActivity.Companion.AMOUNT
import com.kaanyagan.petrolofisi.BuyActivity.Companion.BENZIN_PRICE
import com.kaanyagan.petrolofisi.BuyActivity.Companion.DIZEL_PRICE
import com.kaanyagan.petrolofisi.BuyActivity.Companion.OPTION
import com.kaanyagan.petrolofisi.BuyActivity.Companion.OTOGAZ_PRICE

class BillActivity : AppCompatActivity() {

    lateinit var tvCarNameAndPlate:TextView
    lateinit var tvCarFuelType:TextView
    lateinit var tvCarFuelCapacity:TextView
    lateinit var tvCarCurrentFuel:TextView
    lateinit var tvFuelPrice:TextView
    lateinit var tvBoughtFuelAmount:TextView
    lateinit var tvTotalPrice:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        tvCarNameAndPlate = findViewById(R.id.tvCarNameAndPlate)
        tvCarFuelType = findViewById(R.id.tvCarFuelType)
        tvCarFuelCapacity = findViewById(R.id.tvCarFuelCapacity)
        tvCarCurrentFuel = findViewById(R.id.tvCarCurrentFuel)
        tvFuelPrice = findViewById(R.id.tvFuelPrice)
        tvBoughtFuelAmount = findViewById(R.id.tvBoughtFuelAmount)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)

        val receivedIntent = intent
        val receivedOption = receivedIntent.getStringExtra(OPTION)
        var receivedAmount = receivedIntent.getStringExtra(AMOUNT)
        val car = intent.getParcelableExtra<Car>(BuyActivity.CAR)
        val pump = intent.getParcelableExtra<Pump>(BuyActivity.PUMP)


        car?.let{
            tvCarNameAndPlate.text = "${car.name}, ${car.plate}"
            tvCarFuelType.text = "${car.fuelType}"
            tvCarFuelCapacity.text = "${car.fuelCapacity}"
            tvCarCurrentFuel.text = "${car.currentFuel}"

            if(car.fuelType.toString()=="BENZIN") tvFuelPrice.setText("$BENZIN_PRICE")
            else if (car.fuelType.toString()=="DIZEL") tvFuelPrice.setText("$DIZEL_PRICE")
            if(car.fuelType.toString()=="OTOGAZ") tvFuelPrice.setText("$OTOGAZ_PRICE")

            if(receivedOption=="Litre")
            {
                if (receivedAmount != null) {
                    tvBoughtFuelAmount.text = "${receivedAmount.toDouble()}"
                    val totalPriceDouble = tvFuelPrice.text.toString().toDouble() * receivedAmount.toDouble()
                    tvTotalPrice.text = totalPriceDouble.toString()
                }

            }
            else if(receivedOption=="Para"){
                val receivedAmount = receivedAmount.toString().toDouble()
                tvTotalPrice.text = receivedAmount.toString()

                    if(car.fuelType.toString()=="BENZIN") {
                        tvBoughtFuelAmount.text =  "${receivedAmount / BENZIN_PRICE}"
                    }
                    else if(car.fuelType.toString()=="DIZEL") {
                        tvBoughtFuelAmount.text = "${receivedAmount / DIZEL_PRICE}"
                    }
                    else if(car.fuelType.toString()=="OTOGAZ") {
                        tvBoughtFuelAmount.text =  "${receivedAmount / OTOGAZ_PRICE}"
                    }
                    else{ AlertDialog.Builder(this).setTitle("Hata").setMessage("Sistem gitti.").create().show()}

            }
            else if(receivedOption=="Full"){
                if (receivedAmount != null) {
                    tvBoughtFuelAmount.text = "${receivedAmount.toDouble()}"
                    val totalPriceDouble = tvFuelPrice.text.toString().toDouble() * receivedAmount.toDouble()
                    tvTotalPrice.text = totalPriceDouble.toString()
                }
            }
            else{
                AlertDialog.Builder(this).setTitle("Hata").setMessage("Sistem gitti.").create().show()
            }

        }?:run{

        }






    }
}