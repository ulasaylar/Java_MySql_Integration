import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.sql.PreparedStatement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ekofitjavaproje?useUnicode=true&useSSL=false&characterEncoding=UTF-8";
        String user = "root";
        String password = "1234";

        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            while (true) {
                anaMenu();
                int secim = sc.nextInt();
                sc.nextLine();

                if (secim == 1) {
                    girisYap(conn, sc);
                } else if (secim == 2) {
                    uyeOl(conn, sc);
                } else if (secim == 0) {
                    System.out.println("Çıkılıyor...");
                    break;
                } else {
                    System.out.println("Geçersiz seçim!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Bağlantı hatası: " + e.getMessage());
        }
    }

    public static void anaMenu() {
        System.out.println("==== Ana Menü ====");
        System.out.println("1 - Giriş Yap");
        System.out.println("2 - Üye Ol");
        System.out.println("0 - Çıkış");
        System.out.print("Seçiminiz: ");
    }

    public static void girisYap(Connection conn, Scanner sc) {
        System.out.print("Öğrenci Numarası Giriniz: ");
        long k_ogrNo = sc.nextLong();
        sc.nextLine();

        System.out.print("Şifre Giriniz: ");
        long k_sifre = sc.nextLong();
        sc.nextLine();

        String sql = "SELECT * FROM uyeler WHERE uyeOgrNo = ? AND uyeSifre = ?";

        try (PreparedStatement com = conn.prepareStatement(sql)) {
            com.setLong(1, k_ogrNo);
            com.setLong(2, k_sifre);

            var check = com.executeQuery();

            if (check.next()) {
                System.out.println("Giriş Başarılı. Hoş geldiniz, " + check.getString("uyeAd") + "!");
                menu(sc);
            } else {
                System.out.println("Giriş Başarısız. Öğrenci Numarası veya şifre hatalı.");
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }
    }

    public static void uyeOl(Connection conn, Scanner sc) {
        System.out.print("Adınızı girin: ");
        String ad = sc.nextLine();

        System.out.print("Öğrenci Numaranızı girin: ");
        long ogrNo = sc.nextLong();

        System.out.print("Şifrenizi girin: ");
        long sifre = sc.nextLong();

        String sql = "INSERT INTO uyeler (uyeAd, uyeOgrNo, uyeSifre) VALUES (?, ?, ?)";

        try (PreparedStatement com = conn.prepareStatement(sql)) {
            com.setString(1, ad);
            com.setLong(2, ogrNo);
            com.setLong(3, sifre);

            int affectedRows = com.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Üyelik başarıyla oluşturuldu!");
            } else {
                System.out.println("Üyelik oluşturulamadı.");
            }
        } catch (SQLException e) {
            System.out.println("Veritabanı hatası: " + e.getMessage());
        }
    }

    public static void menu(Scanner sc) {
        while (true) {
            System.out.println("==== Kullanıcı Menüsü ====");
            System.out.println("1 - Kalori Hesapla");
            System.out.println("2 - Spor Önerisi Yap");
            System.out.println("3 - Haftalık Spor Programı Öner");
            System.out.println("0 - Çıkış");
            System.out.print("Seçiminiz: ");

            int secim = sc.nextInt();
            sc.nextLine();

            if (secim == 1) {
                System.out.println("Egzersiz sürenizi giriniz : ");
                int sure = sc.nextInt();
                int sonuc = kaloriHesapla(sure);
                System.out.println("Yakılan kalori: " + sonuc + " kcal");
            } else if (secim == 2) {
                System.out.println("Kalori giriniz : ");
                int kalori = sc.nextInt();
                sporOnerisiYap(kalori);
            } else if (secim == 3) {
                haftalikPlaniGoster();
            } else if (secim == 0) {
                System.out.println("Ana menüye dönülüyor...");
                break;
            } else {
                System.out.println("Hatalı Seçim");
            }
        }
    }

    public static int kaloriHesapla(int dakika) {
        return dakika * 5;
    }

    public static void sporOnerisiYap(int kalori) {
        if (kalori > 0) {
            if (kalori <= 100) {
                System.out.println("Size Önerimiz : Yürüyüş");
            } else if (kalori <= 200) {
                System.out.println("Size Önerimiz : Koşu");
            } else if (kalori <= 300) {
                System.out.println("Size Önerimiz : Bisiklet");
            } else if (kalori <= 400) {
                System.out.println("Size Önerimiz : Yüzme");
            } else if (kalori <= 500) {
                System.out.println("Size Önerimiz : Fonksiyonel Antrenman");
            } else if (kalori <= 600) {
                System.out.println("Size Önerimiz : Kuvvet Antrenmanı");
            } else {
                System.out.println("Size Önerimiz : HIIT");
            }
        } else {
            System.out.println("Lütfen pozitif değer giriniz!");
        }
    }

    public static void haftalikPlaniGoster() {
        System.out.println("Pazartesi -> " + rastgeleSporSec());
        System.out.println("Salı -> " + rastgeleSporSec());
        System.out.println("Çarşamba -> " + rastgeleSporSec());
        System.out.println("Perşembe -> " + rastgeleSporSec());
        System.out.println("Cuma -> " + rastgeleSporSec());
        System.out.println("Cumartesi -> " + rastgeleSporSec());
        System.out.println("Pazar -> " + rastgeleSporSec());
    }

    public static String rastgeleSporSec(){
        Random rnd = new Random();
        int indeks = rnd.nextInt(7);
        String[] sporDizi = {"Yürüyüş", "Koşu", "Bisiklet", "Yüzme", "Fonksiyonel Antrenman", "Kuvvet Antrenmanı", "HIIT"};
        return sporDizi[indeks];
    }
}