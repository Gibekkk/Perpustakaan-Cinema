public class Pembayaran {
    private Transaksi transaksi;
    private boolean isBayar;
    private String jenisPembayaran;
    private int jumlahBayar;

    public Pembayaran() {
    }

    public Pembayaran(Transaksi transaksi, boolean isBayar, String jenisPembayaran, int jumlahBayar) {
        this.transaksi = transaksi;
        this.isBayar = isBayar;
        this.jenisPembayaran = jenisPembayaran;
        this.jumlahBayar = jumlahBayar;
    }

    public Transaksi getTransaksi() {
        return this.transaksi;
    }

    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public boolean isIsBayar() {
        return this.isBayar;
    }

    public boolean getIsBayar() {
        return this.isBayar;
    }

    public void setIsBayar(boolean isBayar) {
        this.isBayar = isBayar;
    }

    public String getJenisPembayaran() {
        return this.jenisPembayaran;
    }

    public void setJenisPembayaran(String jenisPembayaran) {
        this.jenisPembayaran = jenisPembayaran;
    }

    public int getJumlahBayar() {
        return this.jumlahBayar;
    }

    public void setJumlahBayar(int jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }

}
