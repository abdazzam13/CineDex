# CineDex

Proyek ini dibuat untuk keperluan **Android Developer Technical Assessment Test**. Aplikasi ini merupakan katalog film Android yang menampilkan daftar film, detail film, ulasan, serta pengelolaan film favorit menggunakan TMDB API dengan arsitektur Modern Android Development (Jetpack Compose, Clean Architecture, MVVM).

---

## ⚙️ Konfigurasi API (local.properties)

Untuk alasan keamanan kredensial, API Key dan Token TMDB disimpan di file `local.properties` (tidak ter-commit ke version control).

Sebelum menjalankan aplikasi, buat atau tambahkan baris berikut ke dalam file `local.properties` di root project:

```properties
TMDB_API_KEY=your_tmdb_api_key
TMDB_BEARER_TOKEN=your_tmdb_bearer_token
```

* **`TMDB_API_KEY`**: Digunakan untuk query parameter autentikasi API TMDB.
* **`TMDB_BEARER_TOKEN`**: Digunakan sebagai HTTP Authorization header (`Bearer <token>`) untuk akses endpoint TMDB seperti profil akun dan sinkronisasi data favorit.
* Kredensial bisa diperoleh melalui pengaturan akun di [The Movie Database (TMDB)](https://www.themoviedb.org/settings/api).

---

## 🚀 Cara Menjalankan

1. Buka project di **Android Studio**.
2. Pastikan konfigurasi `local.properties` di atas sudah terisi.
3. Lakukan **Sync Project with Gradle Files**.
4. Jalankan aplikasi di Emulator atau Device fisik (**Run ▶️** atau jalankan `./gradlew assembleDebug` melalui terminal).
