# Etiya Telekom Frontend

Telekomünikasyon müşteri hizmetleri web arayüzü.

## Renkler
- **Birincil:** RGB(245, 130, 32)
- **İkincil:** RGB(36, 36, 65)

## Başlatma
```bash
npm install
npm run dev
```
Uygulama varsayılan olarak http://localhost:5173 adresinde çalışır.

## Backend Bağlantısı
Backend API varsayılan olarak `http://localhost:8080` adresinde beklenir. Değiştirmek için `.env` dosyası oluşturup `VITE_API_URL` tanımlayın:
```
VITE_API_URL=http://localhost:8080/api
```

## Özellikler
- Ana sayfa
- Müşteri kayıt ve giriş
- Şikayet oluşturma ve listeleme
- Geçersiz/ belirsiz AI analizi durumunda bilgilendirme (talep oluşturulmaz)
