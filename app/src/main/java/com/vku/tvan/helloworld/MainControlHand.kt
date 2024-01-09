package com.vku.tvan.helloworld

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.OutputStream

class MainControlHand: AppCompatActivity() {

    private var upBtn:ImageButton?=null
    private var downBtn:ImageButton?=null
    private var leftBtn:ImageButton?=null
    private var rightBtn:ImageButton?=null
    private var returnLeftBtn:ImageButton?=null
    private var returnRightBtn:ImageButton?=null
    private var statusBtnC:Button?=null
    private var nameDeviceTVC:TextView?=null
    private var ctr:TextView?=null

    private var speed1SB: SeekBar?=null
    private var speed2SB: SeekBar?=null
    private var speed3SB: SeekBar?=null
    private var speed4SB: SeekBar?=null
    private var sp1Val: TextView?=null
    private var sp2Val: TextView?=null
    private var sp3Val: TextView?=null
    private var sp4Val: TextView?=null
    private var speedBtn:ImageButton?=null

    private var isDeviceConnected = false
    private var up = 0
    private var down = 0
    private var left = 0
    private var right = 0
    private var returnLeft = 0
    private var returnRight = 0
    private var speedSend = ""


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_hand_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN and WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val isDeviceConnected = intent.getBooleanExtra("isDeviceConnected", false)
        val nameDevice = intent.getStringExtra("nameDevice")

//        Toast.makeText(this, isDeviceConnected.toString()+nameDevice,Toast.LENGTH_SHORT).show()

        statusBtnC = findViewById(R.id.statusBtnC)
        nameDeviceTVC = findViewById(R.id.nameDeviceTVC)


        speed1SB = findViewById(R.id.speed1SB)
        speed2SB = findViewById(R.id.speed2SB)
        speed3SB = findViewById(R.id.speed3SB)
        speed4SB = findViewById(R.id.speed4SB)

        sp1Val = findViewById(R.id.speed1Value)
        sp2Val = findViewById(R.id.speed2Value)
        sp3Val = findViewById(R.id.speed3Value)
        sp4Val = findViewById(R.id.speed4Value)
        speedBtn = findViewById(R.id.speedBtn)

        upBtn = findViewById(R.id.upBtn)
        downBtn = findViewById(R.id.downBtn)
        leftBtn = findViewById(R.id.leftBtn)
        rightBtn = findViewById(R.id.rightBtn)
        returnLeftBtn= findViewById(R.id.btn_return_left)
        returnRightBtn = findViewById(R.id.btn_return_right)

        ctr = findViewById(R.id.ctr)


        speed1SB?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                sp1Val?.text = convertLimit(progress).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        speed2SB?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                sp2Val?.text = convertLimit(progress).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        speed3SB?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                sp3Val?.text = convertLimit(progress).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        speed4SB?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                sp4Val?.text = convertLimit(progress).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        speedBtn?.setOnClickListener {
            speedBtn?.setImageResource(R.drawable.btn_speed_down_i)
            val s1 = sp1Val?.text.toString()
            val s2 = sp2Val?.text.toString()
            val s3 = sp3Val?.text.toString()
            val s4 = sp4Val?.text.toString()
            speedSend = "$s1,$s2,$s3,$s4\b"

            Toast.makeText(this, "Tốc độ: $speedSend", Toast.LENGTH_SHORT).show()
            sendCommand(speedSend)

            speedBtn?.setImageResource(R.drawable.btn_speed_up_i)
        }

        statusBtnC?.setBackgroundColor(Color.RED)
        nameDeviceTVC?.text = nameDevice

        if (isDeviceConnected) {
            statusBtnC?.setBackgroundColor(Color.GREEN)

        } else {
            statusBtnC?.setBackgroundColor(Color.RED)
        }

        upBtn?.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    upBtn?.setImageResource(R.drawable.btn_forward_down)
                    up = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    upBtn?.setImageResource(R.drawable.btn_forward_up)
                    up = 0
                    tb()
                }
            }
            true
        }


        downBtn?.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    downBtn?.setImageResource(R.drawable.btn_backward_down)
                    down = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    downBtn?.setImageResource(R.drawable.btn_backward_up)
                    down = 0
                    tb()
                }
            }
            true
        }

        leftBtn?.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    leftBtn?.setImageResource(R.drawable.btn_left_down)
                    left = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    leftBtn?.setImageResource(R.drawable.btn_left_up)
                    left = 0
                    tb()
                }
            }
            true
        }

        rightBtn?.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    rightBtn?.setImageResource(R.drawable.btn_right_down)
                    right = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    rightBtn?.setImageResource(R.drawable.btn_right_up)
                    right = 0
                    tb()
                }
            }
            true
        }
        returnRightBtn?.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    returnRightBtn?.setImageResource(R.drawable.btn_right1)
                    returnRight = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    returnRightBtn?.setImageResource(R.drawable.btn_right)
                    returnRight = 0
                    tb()
                }
            }
            true
        }

        returnLeftBtn?.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    returnLeftBtn?.setImageResource(R.drawable.btn_left1)
                    returnLeft = 1
                    tb()
                }
                MotionEvent.ACTION_UP -> {
                    returnLeftBtn?.setImageResource(R.drawable.btn_left)
                    returnLeft = 0
                    tb()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val ctrIt = Intent(this, MainControl::class.java)
        startActivity(ctrIt)
        disconnectDevice()
        finish()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        registerReceiver(bluetoothStateReceiver, filter)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothStateReceiver)
    }
    private fun disconnectDevice(){
        try {
            sendCommand("D\b")
            MainControl.bluetoothSocket!!.close()
            MainControl.bluetoothSocket = null
            isDeviceConnected = false
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun convertLimit(oldLimit: Int): Int {
        val newMin = 0
        val newMax = 255
        val oldMin = 0
        val oldMax = 100
        return ((oldLimit - oldMin) * (newMax - newMin) / (oldMax - oldMin)) + newMin
    }

    private val bluetoothStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    isDeviceConnected = true
                    Toast.makeText(this@MainControlHand, "Connect with device", Toast.LENGTH_SHORT).show()
                    statusBtnC!!.setBackgroundColor(Color.GREEN)
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    isDeviceConnected = false
                    Toast.makeText(this@MainControlHand, "Disconnect with device", Toast.LENGTH_SHORT).show()
                    statusBtnC!!.setBackgroundColor(Color.RED)
                    nameDeviceTVC!!.text = "No Connection"
                }
                else -> println("Default")
            }
        }
    }

    private fun sendCommand(output: String) {

        if (MainControl.bluetoothSocket != null){
            val outputStream: OutputStream? = MainControl.bluetoothSocket?.outputStream
//            val data: ByteArray = output.toByteArray()
//            outputStream?.write(output.toByteArray(Charsets.UTF_8))
            outputStream?.write(output.toByteArray())
            outputStream?.flush()
        }
        else{
//            Toast.makeText(this, "No connect device so not send command", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tb(){
        if(up == 1) {
            sendCommand("F\b")
            ctr?.text = "F"
        }
        if(down == 1)  {
            sendCommand("B\b")
            ctr?.text = "B"
        }
        if(left == 1)  {
            sendCommand("L\b")
            ctr?.text = "L"
        }
        if(right == 1)  {
            sendCommand("R\b")
            ctr?.text = "R"
        }

        if(returnRight == 1)  {
            sendCommand("P\b")
            ctr?.text = "P"
        }

        if(returnLeft == 1)  {
            sendCommand("Z\b")
            ctr?.text = "Z"
        }

        if(up == 1 && left == 1) {
            sendCommand("G\b")
            ctr?.text = "G"
        }
        if(up == 1 && right == 1) {
            sendCommand("I\b")
            ctr?.text = "I"
        }
        if(down == 1 && left == 1) {
            sendCommand("H\b")
            ctr?.text = "H"
        }
        if(down == 1 && right == 1) {
            sendCommand("J\b")
            ctr?.text = "J"
        }
        if(up == 0 && down == 0 && left == 0 && right == 0 && returnLeft == 0 && returnRight == 0)  {
            sendCommand("S\b")
            ctr?.text = "S"
        }
    }
}