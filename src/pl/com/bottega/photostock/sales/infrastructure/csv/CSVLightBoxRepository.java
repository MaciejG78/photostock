package pl.com.bottega.photostock.sales.infrastructure.csv;

import com.sun.deploy.util.StringUtils;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.product.Product;
import pl.com.bottega.photostock.sales.model.product.ProductRepository;

import java.io.*;
import java.util.*;

/**
 * Created by macie on 16.01.2017.
 */
public class CSVLightBoxRepository implements LightBoxRepository{

    private String path, tmpPath, folderPath;

    private ProductRepository productRepository;


    public CSVLightBoxRepository(String folderPath, ProductRepository productRepository) {
        this.folderPath = folderPath;
        this.path = folderPath + File.separator + "lightboxes.csv";
        this.productRepository = productRepository;
        this.tmpPath = path + ".tmp";
    }


//TODO dorobić metodę put
    @Override
    public void put(LightBox lightBox) {
        Client owner = lightBox.getOwner();
        Boolean isNewLightBoxForClient = true;
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                PrintWriter printWriter = new PrintWriter(new FileWriter(tmpPath))
        ){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] attributes = line.trim().split(",");
                String numberOfClient = attributes[0];
                if (numberOfClient.equals(owner.getNumber()) && lightBox.getName().equals(attributes[1])) {
                    writeLightBox(lightBox, printWriter);
                    isNewLightBoxForClient = false;
                }
                else
                    printWriter.println(line);
            }
            if (isNewLightBoxForClient){
                writeLightBox(lightBox, printWriter);
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        replaceFiles();
    }

    private void writeLightBox(LightBox lightBox, PrintWriter printWriter) {
        String productNumbersInLightBox = "";
        for (Product productInLightBox : lightBox) {
            productNumbersInLightBox += productInLightBox.getNumber() + "|";
            productNumbersInLightBox = productNumbersInLightBox.substring(0, productNumbersInLightBox.length() - 1);
        }
        if (productNumbersInLightBox.equals(""))
            productNumbersInLightBox = " ";

        String[] attributes = new String[] {
                lightBox.getOwner().getNumber(),
                lightBox.getName(),
                productNumbersInLightBox
        };
        printWriter.println(StringUtils.join(Arrays.asList(attributes), ","));
    }

    //clientNumber, lightBoxName, numberOfProducts
    @Override
    public Collection<LightBox> getFor(Client client) {
        Collection<LightBox> temporaryStorage = new HashSet<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] attributes = line.trim().split(",");
                String numberOfClient = attributes[0];
                if (numberOfClient.equals(client.getNumber())) {
                    LightBox temporaryLightBox = new LightBox(client, attributes[1]);
                    String[] productsForClient = attributes[2].split("\\|");

                    //TODO metodę add zmienić na coś innego

                        for (String product : productsForClient) {
                            Product checkedProduct = productRepository.checkIfAvailable(product, true);
                            if (checkedProduct != null)
                                temporaryLightBox.add(checkedProduct);

                        }
                    temporaryStorage.add(temporaryLightBox);
                }
            }
                return temporaryStorage;

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public LightBox findLightBox(Client client, String lightBoxName) {
        Collection<LightBox> lightBoxes = getFor(client);
        if(lightBoxes != null)
            for(LightBox lb : lightBoxes)
                if(lb.getName().equals(lightBoxName))
                    return lb;
        return null;
    }

    @Override
    public Collection<String> getLightBoxNames(Client client) {
        Collection<String> lightBoxNames = new LinkedList<>();
        Collection<LightBox> lightBoxes = getFor(client);
        if(lightBoxes != null)
            for(LightBox lb : lightBoxes)
                lightBoxNames.add(lb.getName());
        return lightBoxNames;
    }



    private void replaceFiles() {
        File file = new File(tmpPath);
        new File(path).delete();
        file.renameTo(new File(path));
    }

}
