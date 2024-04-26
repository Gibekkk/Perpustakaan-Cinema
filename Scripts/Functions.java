import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Functions{
    static Scanner input = new Scanner(System.in);
    static ArrayList<Client> clientList = new ArrayList<Client>();
    static ArrayList<Pustakawan> pustakawanList = new ArrayList<Pustakawan>();
    static ArrayList<Transaksi> transaksiList = new ArrayList<Transaksi>();
    static ArrayList<Pembayaran> pembayaranList = new ArrayList<Pembayaran>();
    static ArrayList<Peminjaman> peminjamanList = new ArrayList<Peminjaman>();
    static ArrayList<Pengembalian> pengembalianList = new ArrayList<Pengembalian>();
    static ArrayList<Koleksi> koleksiList = new ArrayList<Koleksi>();
    static String[] clientNav = new String[]{
        "My Info",
        "Logout"
    };
    static String[] pustakawanNav = new String[]{
        "My Info",
        "Add User",
        "Add Collection",
        "Borrow Collection",
        "List User",
        "List Koleksi",
        "Logout"
    };

    public static void navClient(Client client) throws IOException{
        String pilih = enumerator("Please Choose an Action:", clientNav);
        boolean isLoggedin = true;
        switch(pilih){
            case "Logout":
                isLoggedin = false;
                login();
                break;
            default:
                client.showDetails();
        }
        if(isLoggedin)navClient(client);
    }

    public static void navPustakawan(Pustakawan pustakawan) throws IOException{
        String pilih = enumerator("Please Choose an Action:", pustakawanNav);
        boolean isLoggedin = true;
        switch(pilih){
            case "Logout":
                isLoggedin = false;
                mainMenu();
                break;
            case "Add User":
                addUser();
                break;
            case "Add Collection":
                addKoleksi();
                break;
            case "Borrow Collection":
                pinjamKoleksi(pustakawan);
                break;
            case "List User":
                listUser();
                break;
            case "List Koleksi":
                listKoleksi();
                break;
            default:
                pustakawan.showDetails();
        }
        if(isLoggedin)navPustakawan(pustakawan);
    }

    public static void login() throws IOException{
        System.out.println("\nLogin:");
        String username = inputText("Masukkan Username: ", true);
        String password = inputText("Masukkan Password: ", true);
        String checker = loginCheck(username);
        if(checker.matches("client")){
            Client client = loginClient(username, password);
            if(client.checkNull()){
                System.out.println("\nWelcome, " + client.getNama());
                navClient(client);
            } else{
                System.out.println("\nUsername or Password Invalid!");
                login();
            }
        } else if(checker.matches("pustakawan")){
            Pustakawan pustakawan = loginPustakawan(username, password);
            if(pustakawan.checkNull()){
                System.out.println("\nWelcome, " + pustakawan.getNama());
                navPustakawan(pustakawan);
            } else{
                System.out.println("\nUsername or Password Invalid!");
                login();
            }
        } else{
            System.out.println("\nUsername Not Found!");
            login();
        }
    }

    public static void mainMenu() throws IOException{
        String pilih = enumerator("\nChoose Your Action:", new String[] {"Login", "Exit"});
        switch(pilih){
            case "Login":
                login();
                break;
            case "Exit":
                System.out.println("Bye!");
                System.exit(0);
                break;
        }
        mainMenu();
    }

    public static void listUser() throws IOException{
        Object[] columnNames = new Object[]{
            "No",
            "ID Pengenal",
            "Nama",
            "Fakultas",
            "Prodi",
            "Posisi",
            "Jenis Kelamin",
            "Username",
            "Password"
        };
        Object[][] rowData = dataGenerator(clientList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            Client data = clientList.get(i);
            rowData[i][1] = i + 1;
            rowData[i][1] = data.getIdPengenal();
            rowData[i][2] = data.getNama();
            rowData[i][3] = data.getFakultas();
            rowData[i][4] = data.getProdi();
            rowData[i][5] = data.getPosisi();
            rowData[i][6] = data.getJenisKelamin();
            rowData[i][7] = data.getUsername();
            rowData[i][8] = data.getPassword();
        }
        generateTable("Clients", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
        printTXT("Clients", tableFormatter(rowData, columnNames).split("/")[0], rowData, "Users.txt", columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void listKoleksi() throws IOException{
        Object[] columnNames = new Object[]{
            "No",
            "Judul",
            "Tahun Terbit",
            "Kategori",
            "Edisi",
            "Jenis"
        };
        Object[][] rowData = dataGenerator(koleksiList.size(), columnNames);
        for(int i = 0; i < rowData.length; i++){
            Koleksi data = koleksiList.get(i);
            rowData[i][0] = i + 1;
            rowData[i][1] = data.getJudul();
            rowData[i][2] = data.getTahunTerbit();
            rowData[i][3] = data.getKategori();
            if(data instanceof BukuMajalah){
                BukuMajalah buku = (BukuMajalah) data;
                rowData[i][4] = buku.getEdisi();
                rowData[i][5] = "Buku/Majalah";
            } else{
                rowData[i][4] = "";
                rowData[i][5] = "CD";
            }
        }
        generateTable("Collections", tableFormatter(rowData, columnNames).split("/")[0], rowData, columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
        printTXT("Collections", tableFormatter(rowData, columnNames).split("/")[0], rowData, "Collections.txt", columnNames, Integer.parseInt(tableFormatter(rowData, columnNames).split("/")[1]));
    }

    public static void pinjamKoleksi(Pustakawan pustakawan){
        int userIndex = 0;
        String[] users = new String[clientList.size()];
        for(int i = 0; i < users.length; i++){
            users[i] = clientList.get(i).getNama();
        }
        String nama = enumerator("Pilih Peminjam:", users);
        for(int j = 0; j < users.length; j++){
            if(nama.matches(users[j])){
                userIndex = j;
            }
        }
        transaksiList.add(new Transaksi(generateID("transaksi"), inputInt("Jumlah Pinjaman: ", 1), inputDate("Tanggal Transaksi"), inputDate("Tanggal Kembali"), clientList.get(userIndex), pustakawan));
        for(int i = 0; i < inputInt("Jumlah Judul Pinjaman: ", 1); i++){
            int collectionIndex = 0;
            String[] collections = new String[koleksiList.size()];
            for(int k = 0; k < collections.length; k++){
                collections[k] = clientList.get(k).getNama();
            }
            String judul = enumerator("Pilih Koleksi:", collections);
            for(int j = 0; j < collections.length; j++){
                if(judul.matches(collections[j])){
                    collectionIndex = j;
                }
            }
            peminjamanList.add(new Peminjaman(transaksiList.get(transaksiList.size() - 1), inputInt("Jumlah Pinjaman Jenis Ini: ", 1), koleksiList.get(collectionIndex)));
        }
    }

    public static void addKoleksi(){
        String pilih = enumerator("Jenis Koleksi:", new String[]{"Buku/Majalah", "CD"});
        String[] kategoriBuku = new String[]{
            "Romance",
            "Action"
        };
        String[] kategoriCD = new String[]{
            "Pop",
            "Rock"
        };
        String[] edisi = new String[]{
            "Saku",
            "Jilid"
        };
        switch(pilih){
            case "Buku/Majalah":
                koleksiList.add(new BukuMajalah(generateID("koleksi"), inputInt("Masukkan Stock: "), inputText("Masukkan Judul: ", false), inputInt("Masukkan Tahun Terbit: ", 1900), enumerator("Pilih Genre Buku:", kategoriBuku), enumerator("Pilih Edisi Buku:", edisi)));
                break;
            case "CD":
                koleksiList.add(new CD(generateID("koleksi"), inputInt("Masukkan Stock: "), inputText("Masukkan Judul: ", false), inputInt("Masukkan Tahun Terbit: ", 1900), enumerator("Pilih Genre CD:", kategoriCD)));
                break;
        }
    }

    public static Client loginClient(String username, String password){
        for(Client client : clientList){
            if(client.getUsername().matches(username) && client.getPassword().matches(password)){
                return client;
            }
        }
        return new Client();
    }

    public static Pustakawan loginPustakawan(String username, String password){
        for(Pustakawan pustakawan : pustakawanList){
            if(pustakawan.getUsername().matches(username) && pustakawan.getPassword().matches(password)){
                return pustakawan;
            }
        }
        return new Pustakawan();
    }

    public static String loginCheck(String username){
        if(clientList.size() > 0){
            for(int i = 0; i < clientList.size(); i++){
                if(username.toLowerCase().matches(clientList.get(i).getUsername().toLowerCase())){
                    return "client";
                }
            }
        }
        if(pustakawanList.size() > 0){
            for(int i = 0; i < pustakawanList.size(); i++){
                if(username.toLowerCase().matches(pustakawanList.get(i).getUsername().toLowerCase())){
                    return "pustakawan";
                }
            }
        }
        return "none";
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean userExist(String username){
        if(clientList.size() > 0){
            for(int i = 0; i < clientList.size(); i++){
                if(username.toLowerCase().matches(clientList.get(i).getUsername().toLowerCase())){
                    return false;
                }
            }
        }
        if(pustakawanList.size() > 0){
            for(int i = 0; i < pustakawanList.size(); i++){
                if(username.toLowerCase().matches(pustakawanList.get(i).getUsername().toLowerCase())){
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String userMaker(){
        String username = inputText("Masukkan Username: ", true);
        if(userExist(username)){
            return username;
        } else{
            System.out.println("\nUsername Sudah Ada!");
            return userMaker();
        }
    }

    public static int generateID(String jenisID){
        jenisID = jenisID.toLowerCase();
        int id = 0;
        switch(jenisID){
            case "pustakawan": 
                id = (pustakawanList.size() == 0) ? 0 : pustakawanList.get(pustakawanList.size() - 1).getIdPustakawan() + 1;
                break;
            case "koleksi": 
                id = (koleksiList.size() == 0) ? 0 : koleksiList.get(koleksiList.size() - 1).getIdKoleksi() + 1;
                break;
            case "transaksi": 
                id = (transaksiList.size() == 0) ? 0 : transaksiList.get(transaksiList.size() - 1).getIdTransaksi() + 1;
                break;
        }
        return id;
    }

    public static void addUser(){
        System.out.println("Pendaftaran Client:");
        String posisi = enumerator("Posisi:", new String[] {"Dosen", "Staff", "Mahasiswa"});
        clientList.add(new Client(inputTextNum("Masukkan " + ((posisi.matches("Mahasiswa")) ? "NIM" : "NIK") + ": "), toTitle(inputText("Masukkan Nama: ", false)), null, null, posisi, null, jenisKelamin(), "", null));
        int clientIndex = (clientList.size() > 0) ? clientList.size() - 1 : 0;
        String[] fakultasProdi = prodiSelector();
        clientList.get(clientIndex).setFakultas(fakultasProdi[0]);
        clientList.get(clientIndex).setProdi(fakultasProdi[1]);
        int jumlahTelp = inputInt("Jumlah Nomor Telepon: ", 1);
        String[] noTelp = new String[jumlahTelp];
        for(int i = 0; i < jumlahTelp; i++){
            noTelp[i] = inputTextNum("Nomor Telepon " + (i + 1) + ": ");
        }
        clientList.get(clientIndex).setNoTelp(noTelp);
        clientList.get(clientIndex).setUsername(userMaker());
        clientList.get(clientIndex).setPassword(inputText("Masukkan Password: ", true));
    }

    public static void addPustakawan(){
        System.out.println("Pendaftaran Pustakawan:");
        pustakawanList.add(new Pustakawan(generateID("pustakawan"), toTitle(inputText("Masukkan Nama: ", false)), inputEmail(), jenisKelamin(), null, "", null));
        int pustakawanIndex = (pustakawanList.size() > 0) ? pustakawanList.size() - 1 : 0;
        int jumlahTelp = inputInt("Jumlah Nomor Telepon: ", 1);
        String[] noTelp = new String[jumlahTelp];
        for(int i = 0; i < jumlahTelp; i++){
            noTelp[i] = inputTextNum("Nomor Telepon " + (i + 1) + ": ");
        }
        pustakawanList.get(pustakawanIndex).setNoTelp(noTelp);
        pustakawanList.get(pustakawanIndex).setUsername(userMaker());
        pustakawanList.get(pustakawanIndex).setPassword(inputText("Masukkan Password: ", true));
    }

    public static String[] prodiSelector(){
        String[] prodi = new String[] {
            "Manajemen",
            "Informatika",
            "Desain Komunikasi Visual"
        };
        String fakultas = "";

        String pilih = enumerator("Pogram Studi:", prodi);
        switch(pilih){
            case "Manajemen":
                fakultas = "Ekonomi";
                break;
            case "Informatika":
            case "Desain Komunikasi Visual":
                fakultas = "Teknik";
                break;
        }
        return new String[] {fakultas, pilih};
    }

    public static String toTitle(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
    
        StringBuilder converted = new StringBuilder();
    
        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
    
        return converted.toString();
    }

    public static String enumerator(String enumTitle, String[] choices){
        System.out.println(enumTitle);
        int count = 1;
        for(String choice:choices){
            System.out.println(count + ". " + choice);
            count++;
        }
        System.out.print("Masukkan Pilihan: ");
        int pilihan;
        try{
            pilihan = Integer.parseInt(input.nextLine());
            if(pilihan <= choices.length && pilihan > 0){
                return choices[pilihan-1];
            } else{
                System.out.println("\nWrong Input!");
                return enumerator(enumTitle, choices);
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return enumerator(enumTitle, choices);
        }
        // Copyright By Gibek
    }

    public static char jenisKelamin(){
        if(inputBool("Jenis Kelamin", "M", "F")){
            return 'M';
        } else{
            return 'F';
        }
    }

    public static String inputDate(String prompt){
        System.out.print(prompt + " (DD-MM-YYYY): ");
        try{
            String value = input.nextLine();
            if(value.matches("") || !value.matches("^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$") || value.matches("^[\\s]*$")){
                System.out.println("\nWrong Input!");
                return inputDate(prompt);
            }else {
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputDate(prompt);
        }
    }

    public static String inputText(String prompt, boolean allowNum){
        System.out.print(prompt);
        try{
            String value = input.nextLine();
            if(!allowNum){
                if(value.matches("") || !value.matches("^[a-zA-Z\\s]*$") || value.matches("^[\\s]*$")){
                    System.out.println("\nWrong Input!");
                    return inputText(prompt, allowNum);
                }else {
                    return value;
                }
            } else{
                if(value.matches("") || !value.matches("^[a-zA-Z0-9\\s]*$") || value.matches("^[\\s]*$")){
                    System.out.println("\nWrong Input!");
                    return inputText(prompt, allowNum);
                }else {
                    return value;
                }
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputText(prompt, allowNum);
        }
    }

    public static String inputTextNum(String prompt){
        System.out.print(prompt);
        try{
            String value = input.nextLine();
            if(value.matches("") || !value.matches("^[0-9]*$") || value.matches("^[\\s]*$")){
                System.out.println("\nWrong Input!");
                return inputTextNum(prompt);
            }else {
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputTextNum(prompt);
        }
    }

    public static String inputEmail(){
        System.out.print("Masukkan Email: ");
        try{
            String value = input.nextLine();
            if(value.matches("") || value.matches("^[\\s]*$")){
                System.out.println("\nInput Empty!");
                return inputEmail();
            }else {
                if(isValidEmail(value)){
                    return value;
                } else{
                    System.out.println("\nInvalid Email!");
                    return inputEmail();
                }
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputEmail();
        }
    }

    public static int inputInt(String prompt, int min){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value < min){
                System.out.println("\nYour Input is Less Than " + min + "!");
                return inputInt(prompt, min);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputInt(prompt, min);
        }
    }

    public static int inputIntMax(String prompt, int max){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value > max){
                System.out.println("\nYour Input Exeeds The Limit of " + max + "!");
                return inputIntMax(prompt, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputIntMax(prompt, max);
        }
    }

    public static int inputIntMin(String prompt){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value <= 0){
                System.out.println("\nWrong Input!");
                return inputIntMin(prompt);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputIntMin(prompt);
        }
    }

    public static int inputInt(String prompt, int min, int max){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            if(value < min || value > max){
                System.out.println("\nYour Input Value Doesn't Match The Range!");
                return inputInt(prompt, min, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputInt(prompt, min, max);
        }
    }

    public static int inputInt(String prompt){
        System.out.print(prompt);
        try{
            int value = Integer.parseInt(input.nextLine());
            return value;
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputInt(prompt);
        }
    }

    public static float inputFloat(String prompt){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            return value;
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt);
        }
    }

    public static float inputFloat(String prompt, float min){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value < min){
                System.out.println("\nYour Input is Less Than " + min + "!");
                return inputFloat(prompt, min);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt, min);
        }
    }

    public static float inputFloatMin(String prompt){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value <= 0){
                System.out.println("\nWrong Input!");
                return inputFloat(prompt);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt);
        }
    }

    public static float inputFloatMax(String prompt, float max){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value > max){
                System.out.println("\nYour Input Exeeds The Limit of " + max + "!");
                return inputFloatMax(prompt, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloatMax(prompt, max);
        }
    }

    public static float inputFloat(String prompt, float min, float max){
        System.out.print(prompt);
        try{
            float value = Float.parseFloat(input.nextLine());
            if(value < min || value > max){
                System.out.println("\nYour Input Value Doesn't Match The Range!");
                return inputFloat(prompt, min, max);
            } else{
                return value;
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputFloat(prompt, min, max);
        }
    }

    public static boolean inputBool(String prompt, String ifTrue, String ifFalse){
        System.out.print(prompt + " (" + ifTrue + "/" + ifFalse + "): ");
        String jawaban;
        try{
            jawaban = input.nextLine().toLowerCase();
            if(jawaban.matches(ifTrue.toLowerCase())){
                return true;
            } else if(jawaban.matches(ifFalse.toLowerCase())){
                return false;
            } else{
                System.out.println("\nWrong Input!");
                return inputBool(prompt, ifTrue, ifFalse);
            }
        } catch(Exception e){
            System.out.println("\nWrong Input!");
            return inputBool(prompt, ifTrue, ifFalse);
        }
    }

    public static boolean printTXT(String bannerTitle, String tableFormat, Object[][] rowData, String pathToFile, Object[] columnNames, int dataLengthTotal) throws IOException{
        try{
            PrintWriter txt = new PrintWriter (pathToFile);
            String titleBanner = "+", titlePadding = "";
            if(dataLengthTotal % 2 == 0){
                for(int i=0; i < dataLengthTotal; i++){
                    titleBanner += "-";
                }
            } else{
                for(int i=0; i < dataLengthTotal; i++){
                    titleBanner += "-";
                }
            }
            titleBanner += "+";
            for(int i=0; i < ((dataLengthTotal-bannerTitle.length())/2)-1; i++){
                titlePadding += " ";
            }
            txt.printf(titleBanner + "%n");
            txt.printf("|" + titlePadding + bannerTitle + " " + titlePadding + "|%n");
            txt.printf(titleBanner + "%n");
            txt.printf(tableFormat, columnNames);
            txt.printf(titleBanner + "%n");
            for (int n = 0; n < rowData.length; n++) {
                txt.printf(tableFormat, rowData[n]);
            }
            txt.printf(titleBanner + "%n");
            txt.close();
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public static void generateTable(String bannerTitle, String tableFormat, Object[][] rowData, Object[] columnNames, int dataLengthTotal){
            String titleBanner = "+", titlePadding = "";
            if(dataLengthTotal % 2 == 0){
                for(int i=0; i < dataLengthTotal-1; i++){
                    titleBanner += "-";
                }
            } else{
                for(int i=0; i < dataLengthTotal-1; i++){
                    titleBanner += "-";
                }
            }
            titleBanner += "+";
            for(int i=0; i < ((dataLengthTotal-bannerTitle.length())/2)-1; i++){
                titlePadding += " ";
            }
            System.out.printf(titleBanner + "%n");
            System.out.printf("|" + titlePadding + bannerTitle + " " + titlePadding + "|%n");
            System.out.printf(titleBanner + "%n");
            System.out.printf(tableFormat, columnNames);
            System.out.printf(titleBanner + "%n");
            for (int n = 0; n < rowData.length; n++) {
                System.out.printf(tableFormat, rowData[n]);
            }
            System.out.printf(titleBanner + "%n");
    }

    public static Object[][] dataGenerator(int rowCount, Object[] columnNames){
        Object rowData[][] =  new Object[rowCount][columnNames.length];
        return rowData;
    }

    public static String tableFormatter(Object[][] rowData, Object[] columnNames){
        int[] dataLength = new int[columnNames.length];
        for (int length = 0; length < columnNames.length; length++) {
            if (columnNames[length] != null) {
                int panjangHeader = columnNames[length].toString().length();
                if (panjangHeader > dataLength[length]) {
                    dataLength[length] = panjangHeader;
                }
            }
        }

        for(int i = 0; i < rowData.length; i++){
            for (int l = 0; l < columnNames.length; l++) {
                if (rowData[i][l] != null) {
                    int panjangData = rowData[i][l].toString().length();
                    if (panjangData > dataLength[l]) {
                        dataLength[l] = panjangData;
                    }
                }
            }
        }
        
        String formatTable = "| %";
        int dataLengthTotal = 0;
        for(int a = 0; a < dataLength.length; a++) {
            formatTable += "-" + (dataLength[a] + 2) + "s | %";
            dataLengthTotal += dataLength[a] + 5;
        }
        formatTable += "n";
        return formatTable + "/" + dataLengthTotal;
    }
}