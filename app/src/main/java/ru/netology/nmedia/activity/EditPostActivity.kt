package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.AcEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AcEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editText = intent.getStringExtra(Intent.EXTRA_INTENT)
        binding.edit.setText(editText)
        binding.cancelEdit.setOnClickListener {
            finish()
        }
        binding.save.setOnClickListener {
            if (binding.edit.text.isNullOrBlank()) {
                Toast.makeText(
                    this@EditPostActivity,
                    "Content cant be empty",
                    Toast.LENGTH_SHORT
                ).show()
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val intent = Intent()
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}