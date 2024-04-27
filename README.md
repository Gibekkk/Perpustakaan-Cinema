# Perpustakaan Cinema
 Sistem terintegrasi untuk sebuah sistem perpustakaan

## Tentang Proyek Ini

Project ini diciptakan dengan tujuan pembelajaran dan pembuatan sistem perpustakaan berdasarkan sistematika database yang telah dirancang sebelumnya

## Fitur Utama

Database Structured: Memiliki sistem susunan class dan implementasi sistem CREATE dan READ untuk data-data yang digunakan.

Input Restrictions: Memiliki batasan inputan untuk mencegah adanya inputan yang tidak sesuai format data yang diminta.

Various Inputs: Memiliki banyak input functions yang dapat digunakan di berbagai kondisi dan kasus sehingga bisa digunakan berkali-kali, juga sudah berupa template untuk diubah error outputnya.

RDBMS System: Memiliki system foreign key, unique key, dan primary key di berbagai class atau tabel dimana key tersebut dibutuhkan. Hal ini dilakukan agar data dapat terintegrasi dengan lebih baik.

### Functions Class

`Functions Class` berisi berbagai macam fungsi yang dapat digunakan dalam memformat dan memanipulasi data dan tampilan

- **Input{Data Type}**: `Input yang beragam dengan tipe data yang beragam juga. Bisa digunakan dalam berbagai kasus dan kondisi dimana dibutuhkan kondisi khusus atau tipe data tertentu`
- **Enumerator**: `Menciptakan suatu daftar String pilihan dengan inputan integer berupa nomor urut pilihan. Lalu mengembalikan String berdasarkan pilihan user sebelumnya`
- **Key Check and Makers**: `Membuat dan memverifikasi pembuatan primary key dan unique key yang harus berbeda dari yang lainnya`
- **Table Generator**: `Membuat tabel berdasarkan data dengan array 2 dimensi, judul kolom, dan judul tabel. Ukuran tabel akan menyesuaikan dengan besar data yang dimasukkan`
- **Table Formatter**: `Membaca data yang diberikan dan memberikan format tabel sebagai hasilnya`
- **Navbars**: `Memiliki integrasi enumerator untuk membuat navbar dan memasuki menu berdasarkan inputan user dengan mudah dan tepat`
- **Login and Registers**: `Memiliki kemampuan untuk menambahkan data user ke dalam arraylist dan memeriksa kembali username dan password yang tepat dari dalam array yang telah diregister`
- **Data Checkers**: `Memiliki validasi ulang untuk berbagai jenis inputan dengan tipe data sama seperti halnya email dan tanggal`
- **Searchers**: `Memiliki fitur pencarian untuk berbagai data yang dibutuhkan dengan berbagai parameter untuk memudahkan pengambilan objek dari suatu array`

## Cara Penggunaan

1. Copy keseluruhan atau functions yang dibutuhkan berdasarkan kasus.
2. Extend class Functions ke dalam class yang ingin menggunakan function.
3. Masukkan function yang dibutuhkan yang sudah ada di dalam class Functions.
4. Ubah model output bila diingikan pada class Functions.
5. Tambahkan berbagai function lainnya untuk diintegrasikan ke dalam Functions untuk mempermudah pekerjaan kasus-kasus tertentu.

