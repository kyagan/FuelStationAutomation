package com.kaanyagan.petrolofisi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EdgeEffect
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.kaanyagan.petrolofisi.Database.cars
import com.kaanyagan.petrolofisi.Database.pumps
import kotlin.math.roundToInt

class BuyActivity : AppCompatActivity() {

    companion object{
        const val OPTION = "option"
        const val AMOUNT = "amount"
        const val CAR = "car"
        const val PUMP = "pump"
        const val BENZIN_PRICE = 36.0
        const val DIZEL_PRICE = 34.0
        const val OTOGAZ_PRICE = 15.0
    }

    lateinit var rgOption:RadioGroup
    lateinit var etAmount:EditText
    var etAmountDouble:Double = 0.0
    lateinit var spCars: Spinner
    lateinit var spPumps: Spinner
    lateinit var btnBuyFuel: Button
    var fuelPrice: Double = 0.0
    lateinit var selectedCar:Car
    lateinit var selectedPump:Pump

    var selectedRadioButtonText:String = "Litre" //RadioButton seçilmediğinde default olarak ata


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)

        rgOption = findViewById(R.id.rgOption)
        etAmount = findViewById(R.id.etAmount)

        spCars = findViewById(R.id.spCars)
        spPumps = findViewById(R.id.spPumps)
        btnBuyFuel = findViewById(R.id.btnBuyFuel)

        val carNames = Database.cars.map { it.getListName() }
        val pumpIds = Database.pumps.map { it.getListName() }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, carNames)
        spCars.adapter = adapter

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pumpIds)
        spPumps.adapter = adapter2

        spCars.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCar = cars[spCars.selectedItemPosition]
                val selectedPump = pumps[spPumps.selectedItemPosition]
                if (selectedCar.fuelType == selectedPump.fuelType){
                }else {
                    ShowAlert("Yanlış Pompa","Aracınıza uygun pompayı seçiniz.")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                ShowAlert("Seçim Yapılmadı" ,"Lütfen bir pompa seçiniz.")
            }
        }

        spPumps.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCar = cars[spCars.selectedItemPosition]
                val selectedPump = pumps[spPumps.selectedItemPosition]
                if (selectedCar.fuelType == selectedPump.fuelType){
                }else {
                    Toast.makeText(this@BuyActivity, "Aracınıza uygun pompayı seçiniz.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("lutfen secim yap....")
            }
        }


        rgOption.setOnCheckedChangeListener { group, checkedId ->
            var selectedRadioButton: RadioButton = findViewById(checkedId)
            selectedRadioButtonText = selectedRadioButton.text as String
            //etAmount.isEnabled = selectedRadioButton.id != R.id.rdbtnFull
            if (selectedRadioButton.id != R.id.rdbtnFull) {
                etAmount.isEnabled = true
                etAmount.setHint(R.string.amountText)
            } else {
                etAmount.isEnabled = false
                etAmount.setHint("Full")
                etAmount.setText("")


            }
        }

        btnBuyFuel.setOnClickListener{
            selectedCar = cars[spCars.selectedItemPosition]
            selectedPump = pumps[spPumps.selectedItemPosition]
            if (etAmount.hint.toString()!="Full")
            {
                if(etAmount.text.isNullOrEmpty()){
                    ShowAlert("Miktar", "Miktar alanı boş bırakılamaz.")
                }
                else{
                    val etAmountText = etAmount.text.toString()
                    etAmountDouble = etAmountText.toDouble()
                }

                if(selectedCar.fuelType == selectedPump.fuelType){
                    if(etAmount.text.isNullOrEmpty()  ||  etAmountDouble == 0.0){
                        ShowAlert("Miktar", "Miktar alanı boş bırakılamaz.")
                    }
                    else{
                        val currentFuel = selectedCar.currentFuel
                        val newCapacity = (currentFuel + (etAmountDouble))
                        if(selectedRadioButtonText == "Litre"){
                            if (newCapacity <= selectedCar.fuelCapacity){
                                selectedCar.currentFuel = newCapacity
                                val intent = Intent(this,BillActivity::class.java)
                                if(selectedRadioButtonText.isNullOrEmpty()) selectedRadioButtonText = "Litre"
                                intent.putExtra(OPTION,selectedRadioButtonText)
                                intent.putExtra(AMOUNT,etAmount.text.toString())
                                intent.putExtra(CAR, selectedCar)
                                intent.putExtra(PUMP, selectedPump)
                                startActivity(intent)
                            }
                            else{
                                ShowAlert("Kapasite Aşımı", "Kapasite aşımı hatası. Kapasite: ${selectedCar.fuelCapacity} Mevcut Yakıt: ${selectedCar.currentFuel}")
                            }
                        }
                        else if(selectedRadioButtonText == "Para"){
                            if (selectedCar.fuelType.toString() == "BENZIN") fuelPrice = BENZIN_PRICE
                            else if (selectedCar.fuelType.toString() == "DIZEL") fuelPrice = DIZEL_PRICE
                            else if (selectedCar.fuelType.toString() == "OTOGAZ") fuelPrice = OTOGAZ_PRICE
                            else{ ShowAlert("Hata", "Sistem gitti.")}

                            val boughtFuel = etAmountDouble / fuelPrice
                            val currentFuel = selectedCar.currentFuel
                            val newCapacity = (currentFuel + boughtFuel)
                            if (newCapacity <= selectedCar.fuelCapacity){
                                selectedCar.currentFuel = newCapacity
                                val intent = Intent(this,BillActivity::class.java)
                                if(selectedRadioButtonText.isNullOrEmpty()) selectedRadioButtonText = "Para"
                                intent.putExtra(OPTION,selectedRadioButtonText)
                                intent.putExtra(AMOUNT,etAmount.text.toString())
                                intent.putExtra(CAR, selectedCar)
                                intent.putExtra(PUMP, selectedPump)
                                startActivity(intent)
                            }
                            else{
                                ShowAlert("Kapasite Aşımı", "Kapasite aşımı hatası. Kapasite: ${selectedCar.fuelCapacity} Mevcut Yakıt: ${selectedCar.currentFuel}")
                            }


                        }else{
                                ShowAlert("Hata", "Sistem gitti.")
                        }
                    }
                }
                else{
                    ShowAlert("Pompa Seçimi","Aracın yakıtı ile pompa yakıtı farklı. Lütfen başka bir pompaya geçiniz.")
                }
            }
            else{
                if(selectedCar.currentFuel == selectedCar.fuelCapacity){
                    ShowAlert("Kapasite Aşımı", "Kapasite aşımı hatası. Kapasite: ${selectedCar.fuelCapacity} Mevcut Yakıt: ${selectedCar.currentFuel}")
                }else {
                    val boughtFuel = selectedCar.fuelCapacity - selectedCar.currentFuel
                    val newCapacity = selectedCar.fuelCapacity
                    selectedCar.currentFuel = newCapacity

                    val intent = Intent(this, BillActivity::class.java)
                    if (selectedRadioButtonText.isNullOrEmpty()) selectedRadioButtonText = "Full"
                    intent.putExtra(OPTION, selectedRadioButtonText)
                    intent.putExtra(AMOUNT, boughtFuel.toString())
                    intent.putExtra(CAR, selectedCar)
                    intent.putExtra(PUMP, selectedPump)
                    startActivity(intent)
                }
            }


    }


}
    fun ShowAlert(title:String, message:String){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Anladım", null)
            .setNegativeButton("İptal", null)
            .setCancelable(true)
            .create().show()
    }

}