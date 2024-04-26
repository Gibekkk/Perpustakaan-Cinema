public class BukuMajalah extends Koleksi {
    private String edisi;

    public BukuMajalah() {
    }
    
    public BukuMajalah(int idKoleksi, int stok, String judul, int tahunTerbit, String kategori, String edisi){
        super(idKoleksi, stok, judul, tahunTerbit, kategori);
        this.edisi = edisi;
    }

    public String getEdisi() {
        return this.edisi;
    }

    public void setEdisi(String edisi) {
        this.edisi = edisi;
    }

}
