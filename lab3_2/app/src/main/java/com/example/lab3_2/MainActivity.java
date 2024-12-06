package com.example.lab3_2;

import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// Главное активити приложения
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Сбрасываем таблицу и добавляем 5 записей
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
            dbHelper.addRecord("Новичков", "Петр", "Петрович");
        });

        // Кнопка для обновления последней записи
        Button btnUpdateLast = findViewById(R.id.btnUpdate);
        btnUpdateLast.setOnClickListener(view -> {
            dbHelper.updateLastRecord("Иванов", "Иван", "Иванович");
        });
    }

    // Метод для сброса базы данных при запуске
    private void resetDatabase() {
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2); // Принудительный сброс структуры
        for (int i = 1; i <= 5; i++) {
            dbHelper.addRecord("Фамилия" + i, "Имя" + i, "Отчество" + i);
        }
    }
}
