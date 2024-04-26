public class Peminjaman {
    private Transaksi transaksi;
    private int jumlahPinjam;
    private Koleksi koleksi;

    public Peminjaman() {
    }

    public Peminjaman(Transaksi transaksi, int jumlahPinjam, Koleksi koleksi) {
        this.transaksi = transaksi;
        this.jumlahPinjam = jumlahPinjam;
        this.koleksi = koleksi;
    }

    public Transaksi getTransaksi() {
        return this.transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public int getJumlahPinjam() {
        return this.jumlahPinjam;
    }

    public void setJumlahPinjam(int jumlahPinjam) {
        this.jumlahPinjam = jumlahPinjam;
    }

    public Koleksi getKoleksi() {
        return this.koleksi;
    }

    public void setKoleksi(Koleksi koleksi) {
        this.koleksi = koleksi;
    }

}
