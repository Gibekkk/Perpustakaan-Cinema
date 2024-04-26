public class Pengembalian {
    private Transaksi transaksi;
    private boolean isRusak;
    private int dendaTelat;
    private int dendaRusak;
    private Koleksi koleksi;

    public Pengembalian() {
    }

    public Pengembalian(Transaksi transaksi, boolean isRusak, int dendaTelat, int dendaRusak, Koleksi koleksi) {
        this.transaksi = transaksi;
        this.isRusak = isRusak;
        this.dendaTelat = dendaTelat;
        this.dendaRusak = dendaRusak;
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

    public int getDendaTelat() {
        return this.dendaTelat;
    }

    public void setDendaTelat(int dendaTelat) {
        this.dendaTelat = dendaTelat;
    }

    public int getDendaRusak() {
        return this.dendaRusak;
    }

    public void setDendaRusak(int dendaRusak) {
        this.dendaRusak = dendaRusak;
    }

    public Koleksi getKoleksi() {
        return this.koleksi;
    }

    public void setKoleksi(Koleksi koleksi) {
        this.koleksi = koleksi;
    }

}
