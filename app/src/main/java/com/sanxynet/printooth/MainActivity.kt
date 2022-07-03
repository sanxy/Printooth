package com.sanxynet.printooth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import com.sanxynet.printooth.databinding.ActivityMainBinding
import net.glxn.qrgen.android.QRCode
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var printing : Printing? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* check if printer has been paired or initialize pairing */
        if (Printooth.hasPairedPrinter())
            printing = Printooth.printer()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initListeners()

    }

    private fun initListeners() {
        binding!!.buttonPrint.setOnClickListener {

            if (!Printooth.hasPairedPrinter())
                resultLauncher.launch(
                Intent(
                    this@MainActivity,
                    ScanningActivity::class.java
                ),
            )
            else printDetails()


        }

        /* callback from printooth to get printer process */
        printing?.printingCallback = object : PrintingCallback {
            override fun connectingWithPrinter() {
                Toast.makeText(this@MainActivity, "Connecting with printer", Toast.LENGTH_SHORT).show()
            }

            override fun printingOrderSentSuccessfully() {
                Toast.makeText(this@MainActivity, "Order sent to printer", Toast.LENGTH_SHORT).show()
            }

            override fun connectionFailed(error: String) {
                Toast.makeText(this@MainActivity, "Failed to connect printer", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: String) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            }

            override fun onMessage(message: String) {
                Toast.makeText(this@MainActivity, "Message: $message", Toast.LENGTH_SHORT).show()
            }

            override fun disconnected() {
                Toast.makeText(this@MainActivity, "Disconnected Printer", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun printDetails() {
        val printables = getSomePrintables()
        printing?.print(printables)
    }

    /* Customize your printer here with text, logo and QR code */
    private fun getSomePrintables() = ArrayList<Printable>().apply {

        add(RawPrintable.Builder(byteArrayOf(27, 100, 4)).build()) // feed lines example in raw mode


        //logo
//            add(ImagePrintable.Builder(R.drawable.bold, resources)
//                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
//                    .build())


        add(
            TextPrintable.Builder()
            .setText("Printer")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
            .setNewLinesAfter(1)
            .build())


        add(
            TextPrintable.Builder()
            .setText("TID: 1111123322" )
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
            .setNewLinesAfter(1)
            .build())

        add(
            TextPrintable.Builder()
            .setText("RRN: : 234566dfgg4456")
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
            .setNewLinesAfter(1)
            .build())

        add(
            TextPrintable.Builder()
            .setText("Amount: NGN$200,000")
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
            .setNewLinesAfter(2)
            .build())


        add(
            TextPrintable.Builder()
            .setText("APPROVED")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
            .setNewLinesAfter(1)
            .build())


        add(
            TextPrintable.Builder()
            .setText("Transaction: Withdrawal")
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
            .setNewLinesAfter(1)
            .build())


        val qr: Bitmap = QRCode.from("RRN: : 234566dfgg4456\nAmount: NGN\$200,000\n")
            .withSize(200, 200).bitmap()

        add(
            ImagePrintable.Builder(qr)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .build())


        add(TextPrintable.Builder()
            .setText("Hello World")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON)
            .setNewLinesAfter(1)
            .build())

        add(TextPrintable.Builder()
            .setText("Hello World")
            .setAlignment(DefaultPrinter.ALIGNMENT_RIGHT)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON)
            .setNewLinesAfter(1)
            .build())

        add(RawPrintable.Builder(byteArrayOf(27, 100, 4)).build())

    }

    /* Inbuilt activity to pair device with printer or select from list of pair bluetooth devices */
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == ScanningActivity.SCANNING_FOR_PRINTER &&  result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
//            val intent = result.data
                printDetails()
        }
    }


}