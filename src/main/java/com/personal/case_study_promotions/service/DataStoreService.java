package com.personal.case_study_promotions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.personal.case_study_promotions.model.Item;
import com.personal.case_study_promotions.repository.ConsistentHashing;
import com.personal.case_study_promotions.model.Node;
import com.personal.case_study_promotions.utils.Utility;


@Service
@Component
public class DataStoreService {
    private final Map<String, Node> nodes = new HashMap<>();
    private final ConsistentHashing consistentHashing;
    private ExecutorService executor;

    //@Value("${number.of.nodes}")
    //private int numberOfNodes; // in ideal scenario, this should be read from a configuration file

    //@Value("${csv.file.path}")
    private String csvConfigPath = "src/main/resources/promotions.csv";

    @Autowired
    public DataStoreService(ConsistentHashing consistentHashing) {
        this.consistentHashing = consistentHashing;
        initializeDataStore();
    }

    @Scheduled(fixedRate = 1800000) // Schedule the method to run every 30 minutes (1800000 milliseconds)
    public void initializeDataStore() {
        System.out.println("Cleanup old data");

        // Properly shut down the existing executor
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
        clearAllNodes();
        // Clear the existing nodes in the consistent hash
        if(consistentHashing != null) consistentHashing.clear();
        System.out.println("Data store cleanup complete");

        System.out.println("Initializing data store");
        int numberOfNodes = 5; // in ideal scenario, this should be calculated based on input file
        List<String> nodeNames = new ArrayList<>();
        for (int i = 0; i < numberOfNodes; i++) {
            String nodeName = "Node" + i;
            nodes.put(nodeName, new Node(nodeName));
            nodeNames.add(nodeName);
        }

        consistentHashing.addNodes(nodeNames);
        this.executor = Executors.newFixedThreadPool(numberOfNodes);
        loadData(csvConfigPath); // assuming this file will be replaced every 30 mins
        System.out.println("Data store initialized");
    }

    public void clearAllNodes() {
        nodes.values().forEach(Node::clear);
        System.out.println("All nodes cleared");
    }

    public void loadData(String csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    String id = values[0];
                    double data = Double.parseDouble(values[1]);
                    String expiryDateStr = values[2];
                    if(id == null || id.isEmpty() || expiryDateStr == null || expiryDateStr.isEmpty()) {
                        System.out.println("Invalid data: " + line);
                        continue;
                    }
                    String expiryDate = Utility.convertToUTCDate(expiryDateStr);
                    Item item = new Item(id, data, expiryDate);

                    String node = consistentHashing.get(id);
                    executor.submit(() -> nodes.get(node).put(id, item));
                } else {
                    System.out.println("Invalid data: " + line);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading data from file: " + csvFile + " : " + e);
            throw new RuntimeException("Error loading data from file: " + csvFile);
        }
    }

    public Item getData(String id) {
        String node = consistentHashing.get(id);
        if(node == null) {
            System.out.println("Node not found for id: " + id);
            return null;
        }
        System.out.println("Node: " + node + " for id: " + id);
        return nodes.get(node).get(id);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
