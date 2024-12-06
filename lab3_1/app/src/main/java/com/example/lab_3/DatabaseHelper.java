package com.example.lab_3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Имя базы данных
    private static final String DATABASE_NAME = "groupmates.db";
    // Версия базы данных
    private static final int DATABASE_VERSION = 1;

    // Имя таблицы
    public static final String TABLE_NAME = "Одногруппники";
    // Поля таблицы
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "ФИО";
    public static final String COLUMN_TIMESTAMP = "Время";

    // Конструктор класса
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Метод вызывается при первом создании базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL-запрос для создания таблицы
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Поле ID
                COLUMN_NAME + " TEXT, " + // Поле ФИО
                COLUMN_TIMESTAMP + " TEXT)"; // Поле времени
        db.execSQL(createTableQuery); // Выполнение SQL-запроса

        // Удаление всех записей и добавление 5 записей
        resetTable(db);
    }

    // Метод для удаления всех записей и добавления 5 новых записей
    public void resetTable(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_NAME); // Удаляем все записи из таблицы
        
        // Сбрасываем автоинкрементный счетчик для ID
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + TABLE_NAME + "'");
        for (int i = 1; i <= 5; i++) { // Добавляем 5 записей
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, "Одногруппник " + i); // Поле ФИО
            values.put(COLUMN_TIMESTAMP, System.currentTimeMillis()); // Время добавления
            db.insert(TABLE_NAME, null, values); // Вставка записи в таблицу
        }
    }

    // Метод вызывается при изменении версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Удаляем старую таблицу
        onCreate(db); // Создаем таблицу заново
    }

    // Метод для получения всех данных из таблицы
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase(); // Открываем базу данных для чтения
        // Выполняем запрос к таблице
        return db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID + " ASC");
    }

    // Метод для добавления новой записи в таблицу
    public void addRecord(String name) {
        SQLiteDatabase db = this.getWritableDatabase(); // Открываем базу данных для записи
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name); // Добавляем ФИО
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis()); // Добавляем текущее время
        db.insert(TABLE_NAME, null, values); // Вставляем запись в таблицу
    }

    // Метод для обновления последней записи
    public void updateLastRecord(String newName) {
        SQLiteDatabase db = this.getWritableDatabase(); // Открываем базу данных для записи
        // Находим последнюю запись в таблице
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID + " DESC", "1");
        if (cursor.moveToFirst()) { // Проверяем, существует ли запись
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)); // Получаем ID записи
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, newName); // Устанавливаем новое ФИО
            // Обновляем запись в таблице по ID
            db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        }
        cursor.close(); // Закрываем курсор
    }
}

