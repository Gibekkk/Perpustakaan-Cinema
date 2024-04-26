public class Denda {
    private Pengembalian pengembalian;
    private int dendaTelat;
    private int dendaRusak;
    private boolean isBayar;
    final private int HARGATELAT = 3000;
    final private int HARGARUSAK = 6000;

    public Denda() {
    }

    public Denda(Pengembalian pengembalian, boolean isTelat) {
        this.pengembalian = pengembalian;
        if(isTelat){
            this.dendaTelat = pengembalian.getPeminjaman().getJumlahPinjam() * HARGATELAT;
        }
        if(pengembalian.getIsRusak()){
            this.dendaRusak = pengembalian.getPeminjaman().getJumlahPinjam() * HARGARUSAK;
        }
        this.isBayar = false;
    }

    public Pengembalian getPengembalian() {
        return this.pengembalian;
    }

    public void setPengembalian(Pengembalian pengembalian) {
        this.pengembalian = pengembalian;
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

    public boolean getIsBayar() {
        return this.isBayar;
    }

    public void setIsBayar(boolean isBayar) {
        this.isBayar = isBayar;
    }

}
