package com.example.lab_3;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class VDataActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper; // Экземпляр DatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vdata);

        // Устанавливаем название таблицы
        TextView tvTableName = findViewById(R.id.tvTableName);
        tvTableName.setText(DatabaseHelper.TABLE_NAME);

        dbHelper = new DatabaseHelper(this); // Инициализация DatabaseHelper
        ListView listView = findViewById(R.id.listView); // Находим ListView

        // Получаем данные из таблицы
        Cursor cursor = dbHelper.getAllData();
        List<HashMap<String, String>> data = new ArrayList<>();

        // Форматирование времени
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        while (cursor.moveToNext()) {
            // Извлекаем данные записи
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIMESTAMP));

            // Добавляем запись в список
            HashMap<String, String> map = new HashMap<>();
            map.put("id", DatabaseHelper.COLUMN_ID + ": " + id);
            map.put("name", DatabaseHelper.COLUMN_NAME + ": " + name);
            map.put("timestamp", "Добавлено: " + sdf.format(timestamp));
            data.add(map);
        }
        cursor.close(); // Закрываем курсор

        // Создаем адаптер для связывания данных с ListView
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.item_row,
                new String[]{"id","name", "timestamp"},
                new int[]{R.id.tvId ,R.id.tvName, R.id.tvTimestamp}
        );

        // Устанавливаем адаптер для ListView
        listView.setAdapter(adapter);
    }
}
