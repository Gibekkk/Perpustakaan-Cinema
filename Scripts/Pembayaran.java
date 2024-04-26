public class Pembayaran {
    private Denda denda;
    private String jenisPembayaran;
    private int jumlahBayar;

    public Pembayaran() {
    }

    public Pembayaran(Denda denda, String jenisPembayaran, int jumlahBayar) {
        this.denda = denda;
        this.jenisPembayaran = jenisPembayaran;
        this.jumlahBayar = jumlahBayar;
    }

    public Denda getDenda() {
        return this.denda;
    }

    public void setDenda(Denda denda) {
        this.denda = denda;
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
