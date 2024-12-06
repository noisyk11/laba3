package com.example.lab_3;

import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// Главное активити приложения
public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper; // Экземпляр DatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this); // Инициализация DatabaseHelper

        // Сбрасываем таблицу и добавляем 5 записей при старте
        resetDatabase();

        // Кнопка для просмотра данных
        Button btnViewData = findViewById(R.id.btnView);
        btnViewData.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VDataActivity.class);
            startActivity(intent);
        });

        // Кнопка для добавления новой записи
        Button btnAddRecord = findViewById(R.id.btnAdd);
        btnAddRecord.setOnClickListener(view -> {
            dbHelper.addRecord("Смирнов Олег Алексондрович");
        });

        // Кнопка для обновления последней записи
        Button btnUpdateLast = findViewById(R.id.btnUpdate);
        btnUpdateLast.setOnClickListener(view -> {
            dbHelper.updateLastRecord("Кактусов Петр Петрович");
        });
    }

    // Метод для сброса базы данных при запуске
    private void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получаем доступ к базе данных
        dbHelper.resetTable(db); // Сбрасываем таблицу и добавляем записи
    }
}
