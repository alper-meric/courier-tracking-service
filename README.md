# Kurye Takip Servisi

Bu proje, kuryelerin konumlarını takip eden ve Migros mağazalarına girişlerini kaydeden bir REST API servisidir.

## Özellikler

- Kurye konumlarını gerçek zamanlı takip
- Migros mağazalarına girişleri otomatik tespit ve kayıt
- Kuryelerin toplam seyahat mesafesini hesaplama
- H2 veritabanı ile geliştirme ortamı

## Gereksinimler

- Java 17 veya üzeri
- Maven 3.6 veya üzeri

## Kurulum

1. Projeyi klonlayın:
```bash
git clone https://github.com/yourusername/courier-tracking-service.git
cd courier-tracking-service
```

2. Projeyi derleyin:
```bash
mvn clean install
```

3. Uygulamayı çalıştırın:
```bash
mvn spring-boot:run
```

## API Endpointleri

### Kurye Konumu Güncelleme
```http
POST /api/couriers/{courierId}/location?lat={latitude}&lng={longitude}
```

### Toplam Mesafe Sorgulama
```http
GET /api/couriers/{courierId}/distance
```

## Veritabanı

H2 veritabanı konsoluna erişmek için:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:courierdb
- Kullanıcı adı: admin
- Şifre: ps

## Test

Uygulamayı test etmek için örnek bir curl komutu:

```bash
# Kurye konumu güncelleme
curl -X POST "http://localhost:8080/api/couriers/COURIER1/location?lat=40.9923307&lng=29.1244229"

# Toplam mesafe sorgulama
curl "http://localhost:8080/api/couriers/COURIER1/distance"
```