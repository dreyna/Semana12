package com.example.semana12

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val fileName = "archivo_interno.txt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextContent = findViewById<EditText>(R.id.editTextContent)
        val editTextOriginal = findViewById<EditText>(R.id.editTextOriginal)
        val textViewResultado = findViewById<TextView>(R.id.textViewResultado)

        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val texto = editTextContent.text.toString()
            guardarArchivo(texto)
        }

        findViewById<Button>(R.id.btnLeer).setOnClickListener {
            textViewResultado.text = leerArchivo()
        }

        findViewById<Button>(R.id.btnModificar).setOnClickListener {
            val original = editTextOriginal.text.toString()
            val nuevo = editTextContent.text.toString()
            reemplazarLinea(original, nuevo)
        }

        findViewById<Button>(R.id.btnEliminar).setOnClickListener {
            eliminarArchivo()
        }
    }

    private fun guardarArchivo(texto: String) {
        if (texto.isBlank()) return
        openFileOutput(fileName, MODE_APPEND).use {
            it.write((texto + "\n").toByteArray())
        }
        Toast.makeText(this, "Texto guardado", Toast.LENGTH_SHORT).show()
    }

    private fun leerArchivo(): String {
        return try {
            openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.joinToString("\n")
            }
        } catch (e: FileNotFoundException) {
            "Archivo no encontrado"
        } catch (e: IOException) {
            "Error al leer archivo"
        }
    }

    private fun reemplazarLinea(original: String, reemplazo: String) {
        val lineas = leerArchivo().lines().toMutableList()
        val index = lineas.indexOfFirst { it == original }

        if (index != -1) {
            lineas[index] = reemplazo
            openFileOutput(fileName, MODE_PRIVATE).use {
                it.write((lineas.joinToString("\n") + "\n").toByteArray())
            }
            Toast.makeText(this, "Línea modificada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Línea no encontrada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarArchivo() {
        val eliminado = deleteFile(fileName)
        if (eliminado) {
            Toast.makeText(this, "Archivo eliminado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}
