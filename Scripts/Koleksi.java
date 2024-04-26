public class Koleksi {
    private int idKoleksi;
    private int stok;
    private String judul;
    private int tahunTerbit;
    private String kategori;
    private int stokTersedia;

    public Koleksi() {
    }

    public Koleksi(int idKoleksi, int stok, String judul, int tahunTerbit, String kategori) {
        this.idKoleksi = idKoleksi;
        this.stok = stok;
        this.judul = judul;
        this.tahunTerbit = tahunTerbit;
        this.kategori = kategori;
        this.stokTersedia = stok;
    }

    public int getIdKoleksi() {
        return this.idKoleksi;
    }

    public void setIdKoleksi(int idKoleksi) {
        this.idKoleksi = idKoleksi;
    }

    public int getStok() {
        return this.stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getStokTersedia() {
        return this.stokTersedia;
    }

    public void setStokTersedia(int stokTersedia) {
        this.stokTersedia = stokTersedia;
    }

    public String getJudul() {
        return this.judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public int getTahunTerbit() {
        return this.tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public String getKategori() {
        return this.kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}