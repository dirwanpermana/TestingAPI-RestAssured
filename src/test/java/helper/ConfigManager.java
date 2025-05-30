package helper;

// Mengimpor pustaka Dotenv dari library `java-dotenv` (cdimascio) untuk membaca variabel lingkungan (.env)
import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {
    // Inisialisasi Dotenv. File `.env` akan dibaca saat class ini di-load
    private static final Dotenv dotenv = Dotenv.configure().load();
    private static String runtimeToken;

    // Method static untuk mengambil BASE_URL dari file .env
    public static String getBaseUrl(){
        return dotenv.get("BASE_URL");
    }

    // Method static untuk mengambil ACCOUNT_USERNAME dari file .env
    public static String getName(){
        return dotenv.get("USERNAME");
    }
    // Method static untuk mengambil ACCOUNT_PASSWORD dari file .env
    public static String getPassword(){
        return dotenv.get("PASSWORD");
    }

    // Gunakan token dari runtime jika ada, jika tidak fallback ke .env
    public static String getToken(){
        if (runtimeToken != null && !runtimeToken.isEmpty()) {
            return runtimeToken;
        }
        return dotenv.get("TOKEN"); // fallback
    }

    // Tambahkan setter agar token bisa di-set dari test login
    public static void setToken(String token){
        runtimeToken = token;
    }
}



