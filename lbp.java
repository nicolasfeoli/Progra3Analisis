





public class LBPHash {

    public ArrayList<ArrayList<Integer>> valoresHeadersArchivo;

    public String comparar(Bitmap bmp, String bmpName){
        int[] arrayAux = new int[bmp.getWidth()*bmp.getHeight()];
        int [] [] intArray = new int[bmp.getWidth()][bmp.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bmp.getPixels(arrayAux, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int cont = 0;
        int vuelta = 0;
        int x;
        for(int i = 0; i < arrayAux.length; i++){
            x = (vuelta*bmp.getWidth());
            if(cont == x){
                intArray[vuelta][i-x] = arrayAux[i];
                vuelta++;
                cont++;
            }
            else{
                intArray[vuelta][i-x] = arrayAux[i];
                cont++;
            }
        }
        ArrayList<Integer> histograma = generarHistogramaLBP(intArray);
        String histogramaStr = convertToString(histograma);
        escribirNuevoHash(histogramaStr, bmpName);
        return "";
    }

    public void escribirNuevoHash(String hashNuevo,String name){
        try {
            boolean yaExisteArchivo = archivoYaExiste("LBPDiccionary.txt");
            if (!yaExisteArchivo) {
                generateNoteOnSD("LBPDiccionary.txt", "");
            }
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "LBPDiccionary.txt");
            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(hashNuevo+","+name+"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean archivoYaExiste(String fileName) {
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        File archivo = new File(root, fileName);
        if (!archivo.exists())
            return false;
        return true;
    }

    @NonNull
    static String convertToString(ArrayList<Integer> numbers) {
        StringBuilder builder = new StringBuilder();
        // Append all Integers in StringBuilder to the StringBuilder.
        for (int number : numbers) {
            builder.append(number);
            builder.append(":");
        }
        // Remove last delimiter with setLength.
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
    
    
    public ArrayList<Integer> generarHistogramaLBP(int[][] pixels){
        int [][] pixeles  = pixels;
        ArrayList<Integer> histograma = new ArrayList<>();
        for (int i = 0; i < pixeles.length;i++){
            for(int j = 0; j < pixeles[i].length;j++){
                int sumaPos = 0;
                int numCentral = pixeles[i][j];
                try{ //Sacando bit A
                    if (pixeles[i-1][j-1] >= numCentral){
                        sumaPos += 128;
                    }
                }catch (Exception error){} //Se desborda
                try{ //Sacando bit B
                    if (pixeles[i-1][j] >= numCentral){
                        sumaPos += 64;
                    }
                }catch (Exception error){} //Se desborda
                try{ //Sacando bit C
                    if (pixeles[i-1][j+1] >= numCentral){
                        sumaPos += 32;
                    }
                }catch (Exception error){} //Se desborda
                try{ //Sacando bit D
                    if (pixeles[i][j+1] >= numCentral){
                        sumaPos += 16;
                    }
                }catch (Exception error){} //Se desborda
                try{ //Sacando bit E
                    if (pixeles[i+1][j+1] >= numCentral){
                        sumaPos += 8;
                    }
                }catch (Exception error){} //Se desborda
                try{ //Sacando bit F
                    if (pixeles[i+1][j] >= numCentral){
                        sumaPos += 4;
                    }
                }catch (Exception error){} //Se desborda
                try{ //Sacando bit G
                    if (pixeles[i+1][j-1] >= numCentral){
                        sumaPos += 2;
                    }
                }catch (Exception error){} //Se desborda
                try{ //Sacando bit H
                    if (pixeles[i][j-1] >= numCentral){
                        sumaPos += 1;
                    }
                }catch (Exception error){} //Se desborda
                histograma.add(sumaPos);
            }
        }
        return histograma;
    }
    
}




