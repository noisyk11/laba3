package com.example.lab3_2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Имя базы данных
    private static final String DATABASE_NAME = "groupmates_new.db";
    // Версия базы данных (увеличена)
    private static final int DATABASE_VERSION = 2;

    // Имя таблицы
    public static final String TABLE_NAME = "Одногруппники";

    // Поля таблицы
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_LAST_NAME = "Фамилия";
    public static final String COLUMN_FIRST_NAME = "Имя";
    public static final String COLUMN_MIDDLE_NAME = "Отчество";
    public static final String COLUMN_TIMESTAMP = "Время";

    // Конструктор класса
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Метод вызывается при первом создании базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL-запрос для создания таблицы с новой структурой
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_MIDDLE_NAME + " TEXT, " +
                COLUMN_TIMESTAMP + " TEXT)";
        db.execSQL(createTableQuery);
    }

    // Метод вызывается при изменении версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старую таблицу
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Создаем новую таблицу с обновленной структурой
        onCreate(db);
    }

    // Метод для получения всех данных из таблицы
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase(); // Открываем базу данных для чтения
        // Выполняем запрос к таблице
        return db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID + " ASC");
    }

    // Метод для добавления новой записи в таблицу
    public void addRecord(String lastName, String firstName, String middleName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_NAME, lastName); // Фамилия
        values.put(COLUMN_FIRST_NAME, firstName); // Имя
        values.put(COLUMN_MIDDLE_NAME, middleName); // Отчество
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis()); // Время
        db.insert(TABLE_NAME, null, values);
    }

    // Метод для обновления последней записи
    public void updateLastRecord(String newLastName, String newFirstName, String newMiddleName) {
        SQLiteDatabase db = this.getWritableDatabase(); // Открываем базу данных для записи
        // Находим последнюю запись в таблице
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID + " DESC", "1");
        if (cursor.moveToFirst()) { // Проверяем, существует ли запись
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)); // Получаем ID записи
            ContentValues values = new ContentValues();
            values.put(COLUMN_LAST_NAME, newLastName); // Устанавливаем новую фамилию
            values.put(COLUMN_FIRST_NAME, newFirstName); // Устанавливаем новое имя
            values.put(COLUMN_MIDDLE_NAME, newMiddleName); // Устанавливаем новое отчество
            // Обновляем запись в таблице по ID
            db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        }
        cursor.close(); // Закрываем курсор
    }
}

