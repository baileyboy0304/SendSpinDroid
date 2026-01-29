package com.sendspindroid.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sendspindroid.databinding.ActivityDemoAppBinding
import com.sendspindroid.headless.SendSpinHeadlessController
import com.sendspindroid.sendspin.protocol.SendSpinProtocol

/**
 * Minimal demo activity showing how to drive PlaybackService in a headless way.
 */
class DemoAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoAppBinding
    private lateinit var controller: SendSpinHeadlessController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = SendSpinHeadlessController(this)
        controller.initialize(
            onReady = { binding.statusText.text = getString(com.sendspindroid.R.string.demo_status_ready) },
            onError = { error ->
                binding.statusText.text = getString(com.sendspindroid.R.string.demo_status_error)
                Toast.makeText(this, error.message ?: "MediaController error", Toast.LENGTH_LONG).show()
            }
        )

        binding.connectButton.setOnClickListener {
            val address = binding.serverAddressInput.text.toString().trim()
            val path = binding.serverPathInput.text.toString().trim().ifBlank {
                SendSpinProtocol.ENDPOINT_PATH
            }
            if (address.isBlank()) {
                Toast.makeText(this, "Enter host:port", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            controller.connectLocal(address, path)
            binding.statusText.text = getString(com.sendspindroid.R.string.demo_status_connecting)
        }

        binding.disconnectButton.setOnClickListener {
            controller.disconnect()
            binding.statusText.text = getString(com.sendspindroid.R.string.demo_status_disconnected)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.release()
    }
}
