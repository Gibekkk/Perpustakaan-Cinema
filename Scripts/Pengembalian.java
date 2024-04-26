public class Pengembalian {
    private Peminjaman peminjaman;
    private boolean isRusak;
    private Koleksi koleksi;
    private int totalRusak;

    public Pengembalian() {
    }

    public Pengembalian(Peminjaman peminjaman, boolean isRusak, Koleksi koleksi, int totalRusak) {
        this.peminjaman = peminjaman;
        this.isRusak = isRusak;
        this.koleksi = koleksi;
        this.totalRusak = totalRusak;
    }

    public Peminjaman getPeminjaman() {
        return this.peminjaman;
    }

    public void setPeminjaman(Peminjaman peminjaman) {
        this.peminjaman = peminjaman;
    }

    public boolean isIsRusak() {
        return this.isRusak;
    }

    public boolean getIsRusak() {
        return this.isRusak;
    }

    public void setIsRusak(boolean isRusak) {
        this.isRusak = isRusak;
    }

    public Koleksi getKoleksi() {
        return this.koleksi;
    }

    public void setKoleksi(Koleksi koleksi) {
        this.koleksi = koleksi;
    }

    public int getTotalRusak() {
        return this.totalRusak;
    }

    public void setTotalRusak(int totalRusak) {
        this.totalRusak = totalRusak;
    }

}
