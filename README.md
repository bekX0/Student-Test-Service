# Student Test Service

Student Test Service, öğrencilerin çevrimiçi testlere katılım sağlamasını ve test sonuçlarının yönetilmesini mümkün kılan bir Spring Boot uygulamasıdır.

## İçindekiler
- [Proje Hakkında](#proje-hakkında)
- [Teknoloji Stack](#teknoloji-stack)
- [Kurulum](#kurulum)
- [API Dokümantasyonu](#api-dokümantasyonu)
- [Proje Mimarisi](#proje-mimarisi)
- [Veritabanı Yapısı](#veritabanı-yapısı)
- [Kullanım](#kullanım)

## Proje Hakkında

Bu proje, eğitim kurumları için geliştirilmiş bir test yönetim sistemidir. Sistem aşağıdaki temel işlevleri sağlar:

- **Öğrenci Yönetimi**: Öğrenci bilgilerinin eklenmesi, güncellenmesi ve yönetimi
- **Test Oluşturma**: Farklı kategorilerde testler oluşturma (Genel Kültür, Tarih, Matematik, Türkçe)
- **Soru ve Cevap Yönetimi**: Çoktan seçmeli sorular ve doğru cevapların belirlenmesi
- **Test Katılımı**: Öğrencilerin testlere katılım sağlaması
- **Sonuç Takibi**: Test sonuçlarının kaydedilmesi ve raporlanması

## Teknoloji Stack

- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Database**: H2 Database (In-Memory)
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Spring Boot Validation
- **Caching**: Spring Boot Cache
- **Build Tool**: Maven
- **Development Tools**: Spring Boot DevTools

## Kurulum

### Ön Gereksinimler
- Java 17 veya üzeri
- Maven 3.6 veya üzeri

### Adımlar

1. **Projeyi klonlayın:**
```bash
git clone https://github.com/bekX0/Student-Test-Service.git
cd student-test-service
```

2. **Bağımlılıkları yükleyin:**
```bash
mvn clean install
```

3. **Uygulamayı çalıştırın:**
```bash
mvn spring-boot:run
```

4. **H2 Database Console'a erişim:**
   - URL: http://localhost:8080/db
   - JDBC URL: `jdbc:h2:mem:studentdb`
   - Username: `admin`
   - Password: (boş bırakın)

## API Dokümantasyonu

### Student API Endpoints

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/students` | Tüm öğrencileri listele |
| GET | `/students/{id}` | ID'ye göre öğrenci getir |
| POST | `/students` | Yeni öğrenci oluştur |

**Student Model Örneği:**
```json
{
  "firstName": "Ahmet",
  "lastName": "Yılmaz",
  "number": "20230001"
}
```

### Test API Endpoints

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/tests` | Tüm testleri listele |
| POST | `/tests` | Yeni test oluştur |

**Test Model Örneği:**
```json
{
  "name": "Matematik Testi",
  "type": "MATEMATIK",
  "questions": [
    {
      "content": "2 + 2 = ?",
      "answers": [
        {"content": "3", "correct": false},
        {"content": "4", "correct": true},
        {"content": "5", "correct": false}
      ]
    }
  ]
}
```

### Test Participation API Endpoints

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/participations?studentId={}&testId={}` | Test katılımı oluştur |
| GET | `/participations/by-student-test?studentId={}&testId={}` | Öğrenci-test katılımını getir |
| GET | `/participations/student/{studentId}` | Öğrencinin tüm katılımları |
| GET | `/participations/test/{testId}` | Teste ait tüm katılımlar |
| GET | `/participations` | Tüm katılımları listele |

### Student Answer API Endpoints

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/answers` | Cevap gönder |
| GET | `/answers/stats/{participationId}` | Test katılımı istatistikleri |

## Proje Mimarisi

Proje, katmanlı mimari (Layered Architecture) prensibini takip eder:

```
src/main/java/com/bekx/studenttestservice/
├── controller/          # REST Controller katmanı
├── service/            # İş mantığı katmanı
├── repository/         # Veri erişim katmanı
├── model/             # Entity sınıfları
├── dto/               # Data Transfer Object'leri
└── exception/         # Özel exception sınıfları
```

### Katmanlar

1. **Controller Katmanı**: HTTP isteklerini karşılar ve yanıtları döner
   - HelloController: Basit test endpoint'i
   - StudentController: Öğrenci CRUD işlemleri (Create, Read)
   - TestController: Test CRUD işlemleri (Create, Read)
   - TestParticipationController: Test katılım yönetimi
   - StudentAnswerController: Cevap gönderme ve istatistik görüntüleme

2. **Service Katmanı**: İş mantığını içerir ve işlemleri koordine eder
   - StudentService: Öğrenci işlemleri ve caching
   - TestService: Test kaydetme ve soru-cevap ilişkilendirme
   - TestParticipationService: Katılım yönetimi ve caching
   - StudentAnswerService: Cevap kaydetme ve istatistik hesaplama

3. **Repository Katmanı**: Veritabanı işlemlerini yönetir (Spring Data JPA)
4. **Model Katmanı**: Veritabanı entity'lerini temsil eder
5. **DTO Katmanı**: İstemci ile sunucu arasında veri transferi için kullanılır

## Veritabanı Yapısı

### Ana Entity'ler

1. **Student** (Öğrenci)
   - id, firstName, lastName, number
   - Öğrencinin test katılımları ile One-to-Many ilişki

2. **Test** (Test)
   - id, name, type (TestType enum)
   - Sorular ile One-to-Many ilişki
   - Test katılımları ile One-to-Many ilişki

3. **Question** (Soru)
   - id, content
   - Test ile Many-to-One ilişki
   - Cevaplar ile One-to-Many ilişki

4. **Answer** (Cevap)
   - id, content, isCorrect
   - Soru ile Many-to-One ilişki

5. **TestParticipation** (Test Katılımı)
   - id, student, test, participationDate
   - Öğrenci ve Test ile Many-to-One ilişki
   - Öğrenci cevapları ile One-to-Many ilişki

6. **StudentAnswer** (Öğrenci Cevabı)
   - id, student, question, selectedAnswer, participation
   - Test katılımı sırasında verilen cevapları saklar
   - isCorrect() method'u ile doğru cevap kontrolü

### DTO Sınıfları

1. **AnswerSubmissionDto**: Cevap gönderme için kullanılır
   - studentId, questionId, answerId, participationId

2. **TestParticipationDto**: Test katılım bilgileri için kullanılır
   - id, testName

3. **StudentTestParticipationDto**: Detaylı katılım bilgileri için kullanılır
   - participationId, studentName, testName

### Test Tipleri (TestType Enum)
- `GENEL_KULTUR`
- `TARIH`
- `MATEMATIK`
- `TURKCE`

## Kullanım

### 1. Öğrenci Oluşturma
```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ahmet","lastName":"Yılmaz","number":"20230001"}'
```

### 2. Test Oluşturma
```bash
curl -X POST http://localhost:8080/tests \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Matematik Testi",
    "type": "MATEMATIK",
    "questions": [
      {
        "content": "2 + 2 = ?",
        "answers": [
          {"content": "3", "correct": false},
          {"content": "4", "correct": true},
          {"content": "5", "correct": false}
        ]
      }
    ]
  }'
```

### 3. Test Katılımı Oluşturma
```bash
curl -X POST "http://localhost:8080/participations?studentId=1&testId=1"
```

### 4. Cevap Gönderme
```bash
curl -X POST http://localhost:8080/answers \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "questionId": 1,
    "answerId": 2,
    "participationId": 1
  }'
```

### 5. Test İstatistiklerini Görüntüleme
```bash
curl -X GET http://localhost:8080/answers/stats/1
```

## Özellikler

- **Validation**: Tüm input'lar için doğrulama (@Valid, @NotBlank annotationları)
- **Error Handling**: GlobalExceptionHandler ile merkezi hata yönetimi
- **Caching**: TestParticipationService ve StudentService'de @Cacheable annotationları
- **H2 Console**: Geliştirme sürecinde veri görüntüleme (/db endpoint'i)
- **JSON Serialization**: @JsonManagedReference ve @JsonBackReference ile döngüsel referansları önleme
- **Business Rules**: Question entity'sinde tek doğru cevap kuralı (hasOnlyOneCorrectAnswer validation)
- **Test Coverage**: Tüm controller'lar için unit testler mevcut
- **Repository Pattern**: Spring Data JPA ile otomatik repository implementasyonu
- **DTO Pattern**: Veri transfer nesneleri ile temiz API tasarımı

## Geliştirme

Proje geliştirme ortamında çalışırken Spring Boot DevTools sayesinde otomatik yeniden başlatma özelliğini kullanabilirsiniz.

### Test Çalıştırma
```bash
mvn test
```

### Package Oluşturma
```bash
mvn clean package
```
