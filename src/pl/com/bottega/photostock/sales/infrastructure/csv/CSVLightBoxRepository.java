package pl.com.bottega.photostock.sales.infrastructure.csv;

import com.sun.deploy.util.ArrayUtil;
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
public class CSVLightBoxRepository implements LightBoxRepository {

    private String path, tmpPath, folderPath;

    private ProductRepository productRepository;


    public CSVLightBoxRepository(String folderPath, ProductRepository productRepository) {
        this.folderPath = folderPath;
        this.path = folderPath + File.separator + "lightboxes.csv";
        this.productRepository = productRepository;
        this.tmpPath = path + ".tmp";
    }

    //clientNumber, lightBoxName, numberOfProduct|numberOfProduct itd.
    @Override
    public void put(LightBox lightBox) {
        Client owner = lightBox.getOwner();
        Boolean isNewLightBoxForClient = true;
        ensureCSVExist();
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                PrintWriter printWriter = new PrintWriter(new FileWriter(tmpPath))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] attributes = line.trim().split(",");
                String numberOfClient = attributes[0];
                if (numberOfClient.equals(owner.getNumber()) && lightBox.getName().equals(attributes[1])) {
                    writeLightBox(lightBox, printWriter);
                    isNewLightBoxForClient = false;
                } else
                    printWriter.println(line);
            }
            if (isNewLightBoxForClient) {
                writeLightBox(lightBox, printWriter);
            }
            bufferedReader.close();
            printWriter.close();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        replaceFiles();
    }

    private void ensureCSVExist() {
        try {
            new File(path).createNewFile();
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    private void writeLightBox(LightBox lightBox, PrintWriter printWriter) {
        String productNumbersInLightBox = "";
        for (Product productInLightBox : lightBox) {
            productNumbersInLightBox += productInLightBox.getNumber() + "|";
        }
        productNumbersInLightBox = productNumbersInLightBox.substring(0, productNumbersInLightBox.length() - 1);
        String[] attributes;
        if (productNumbersInLightBox.equals("")) {
            attributes = new String[]{
                    lightBox.getOwner().getNumber(),
                    lightBox.getName()
            };
        } else {
            attributes = new String[]{
                    lightBox.getOwner().getNumber(),
                    lightBox.getName(),
                    productNumbersInLightBox
            };
        }
        printWriter.println(StringUtils.join(Arrays.asList(attributes), ","));
    }

    //clientNumber, lightBoxName, numberOfProducts
    @Override
    public Collection<LightBox> getFor(Client client) {
        Collection<LightBox> temporaryStorage = new HashSet<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] attributes = line.trim().split(",");
                String numberOfClient = attributes[0];
                if (numberOfClient.equals(client.getNumber())) {
                    if (attributes.length == 2) {
                        temporaryStorage.add(new LightBox(client, attributes[1]));
                    } else {
                        temporaryStorage.add(new LightBox(client, attributes[1], getProducts(attributes[2])));
                    }
                }
            }
            return temporaryStorage;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private List<Product> getProducts(String numbers) {
        List<Product> items = new LinkedList<>();
        String[] numbersOfProducts = numbers.split("\\|");
        for (String productNumber : numbersOfProducts) {
            Product product = productRepository.get(productNumber);
            if (product == null) {
                throw new IllegalArgumentException(String.format("Brak produktu nr %s", productNumber));
            } else
                items.add(product);
        }
        return items;
    }

    @Override
    public LightBox findLightBox(Client client, String lightBoxName) {
        Collection<LightBox> lightBoxes = getFor(client);
        if (lightBoxes != null)
            for (LightBox lb : lightBoxes)
                if (lb.getName().equals(lightBoxName))
                    return lb;
        return null;
    }

    @Override
    public Collection<String> getLightBoxNames(Client client) {
        Collection<String> lightBoxNames = new LinkedList<>();
        Collection<LightBox> lightBoxes = getFor(client);
        if (lightBoxes != null)
            for (LightBox lb : lightBoxes)
                lightBoxNames.add(lb.getName());
        return lightBoxNames;
    }


    private void replaceFiles() {
        File file = new File(tmpPath);
        new File(path).delete();
        file.renameTo(new File(path));
    }

}