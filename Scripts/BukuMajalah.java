public class BukuMajalah extends Koleksi {
    private String edisi;
    private String ISBN;

    public BukuMajalah() {
    }
    
    public BukuMajalah(int idKoleksi, int stok, String judul, int tahunTerbit, String kategori, String edisi, String ISBN){
        super(idKoleksi, stok, judul, tahunTerbit, kategori);
        this.edisi = edisi;
        this.ISBN = ISBN;
    }

    public String getEdisi() {
        return this.edisi;
    }

    public void setEdisi(String edisi) {
        this.edisi = edisi;
    }

    public String getISBN() {
        return this.ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

}
