-- Buat database baru jika belum ada
CREATE DATABASE IF NOT EXISTS db_pbo_uas;

-- Gunakan database tersebut
USE db_pbo_uas;

-- 1. Tabel untuk Koordinator
-- Menyimpan data login untuk koordinator.
CREATE TABLE koordinator (
    id VARCHAR(50) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- 2. Tabel untuk Mahasiswa
-- Tabel utama untuk menyimpan semua data pendaftar.
-- Status kelulusan langsung digabungkan di sini karena relasinya satu-ke-satu.
CREATE TABLE mahasiswa (
    nim VARCHAR(15) PRIMARY KEY,
    nama_mahasiswa VARCHAR(125) NOT NULL,
    jenis_kelamin ENUM('Laki-laki', 'Perempuan') NOT NULL,
    prodi ENUM('SI', 'Inf', 'TI', 'Lainnya') NOT NULL,
    email VARCHAR(125) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, 
    nomor_telp VARCHAR(20),
    alamat TEXT,
    status_tahap1 VARCHAR(50) DEFAULT 'Belum Ujian',
    status_tahap2 VARCHAR(50) DEFAULT 'Belum Ujian',
    status_akhir VARCHAR(50) DEFAULT 'Belum Ditentukan'
);

-- 3. Tabel untuk Dokumen
-- Menyimpan data dokumen yang diunggah oleh mahasiswa.
CREATE TABLE dokumen (
    id_dokumen INT PRIMARY KEY AUTO_INCREMENT,
    nim_mahasiswa VARCHAR(15) NOT NULL,
    nama_dokumen VARCHAR(255) NOT NULL,
    path_file TEXT NOT NULL,
    FOREIGN KEY (nim_mahasiswa) REFERENCES mahasiswa(nim) ON DELETE CASCADE
);

-- 4. Tabel untuk Soal Ujian
-- Bank soal untuk ujian tahap 1.
CREATE TABLE soal (
    id_soal INT PRIMARY KEY AUTO_INCREMENT,
    pertanyaan TEXT NOT NULL,
    opsi_a TEXT NOT NULL,
    opsi_b TEXT NOT NULL,
    opsi_c TEXT NOT NULL,
    opsi_d TEXT NOT NULL,
    kunci_jawaban_index INT NOT NULL, -- 0=A, 1=B, 2=C, 3=D
    tipe_ujian VARCHAR(20) NOT NULL DEFAULT 'Tahap1'
);

-- 5. Tabel untuk Ujian
-- Mencatat setiap sesi ujian yang diambil oleh mahasiswa.
CREATE TABLE ujian (
    id_ujian INT PRIMARY KEY AUTO_INCREMENT,
    nim_mahasiswa VARCHAR(15) NOT NULL,
    tipe_ujian VARCHAR(20) NOT NULL,
    waktu_submit TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    skor INT,
    FOREIGN KEY (nim_mahasiswa) REFERENCES mahasiswa(nim) ON DELETE CASCADE
);

-- 6. Tabel untuk Jawaban Ujian
-- Menyimpan setiap jawaban dari seorang mahasiswa pada sebuah sesi ujian.
CREATE TABLE jawaban_ujian (
    id_jawaban INT PRIMARY KEY AUTO_INCREMENT,
    id_ujian INT NOT NULL,
    id_soal INT NOT NULL,
    jawaban_index INT NOT NULL,
    FOREIGN KEY (id_ujian) REFERENCES ujian(id_ujian) ON DELETE CASCADE,
    FOREIGN KEY (id_soal) REFERENCES soal(id_soal) ON DELETE CASCADE
);

-- Menambahkan data dummy awal agar aplikasi bisa langsung digunakan
INSERT INTO koordinator (id, nama, password) VALUES
('admin', 'Admin Koordinator', 'admin');

-- Contoh data dummy soal dari kode Anda
INSERT INTO soal (pertanyaan, opsi_a, opsi_b, opsi_c, opsi_d, kunci_jawaban_index, tipe_ujian) VALUES
('Apa kepanjangan dari UML?', 'Unified Modeling Language', 'Universal Markup Language', 'United Machine Learning', 'Understood Model Logic', 0, 'Tahap1'),
('Diagram UML yang menggambarkan interaksi antar objek berdasarkan urutan waktu adalah...', 'Class Diagram', 'Use Case Diagram', 'Sequence Diagram', 'Activity Diagram', 2, 'Tahap1'),
('Yang BUKAN merupakan aktor dalam sistem di jurnal adalah...', 'Mahasiswa', 'Koordinator UKM', 'Dosen Wali', 'Dekan Fakultas Teknologi', 2, 'Tahap1');
