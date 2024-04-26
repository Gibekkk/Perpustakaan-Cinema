public class Pengembalian {
    private Transaksi transaksi;
    private boolean isRusak;
    private Koleksi koleksi;

    public Pengembalian() {
    }

    public Pengembalian(Transaksi transaksi, boolean isRusak, Koleksi koleksi) {
        this.transaksi = transaksi;
        this.isRusak = isRusak;
        this.koleksi = koleksi;
    }

    public Transaksi getTransaksi() {
        return this.transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
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

}
