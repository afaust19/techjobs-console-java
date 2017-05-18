package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    //make allJobs list case insensitive (CASE_INSENSITIVE_ORDER)
    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) { //*******

        // load data, if not already loaded
        loadData();  //*******

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>(); //*******

        for (HashMap<String, String> row : allJobs) {  //*******

            String aValue = row.get(column);  //******* //gets the column the user want to search in


            if (aValue.toUpperCase().contains(value.toUpperCase())) {
            //if (aValue.contains(value)) {  //*******   //does each entry in that column contain the search term?
                jobs.add(row);  //*******
            }
        }


        return jobs; //*******
    }

    // make searchTerm case insensitive (String.CASE_INSENSITIVE_ORDER)

    public static ArrayList<HashMap<String, String>> searchAllColumns(String searchTerm) { //*******

        // load data, if not already loaded
        loadData();  //*******

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>(); //*******

        // each row (job entry) in allJobs is a HashMap
        // iterate through each row in the ArrayList to find each HashMap that contains the searchTerm

        for (HashMap<String, String> row : allJobs) { //*******

            // if the row contains the searchTerm (HashMap value), add the row to jobs
            // searchTerm needs to match exact value of HashMap, not just one word

            //if (row.containsValue(searchTerm)) {  //*******
            // equalsIgnoreCase for both searchTerm and each value in row

            for (Map.Entry<String, String> entry : row.entrySet()) {
                String value = entry.getValue();

                if (value.toUpperCase().contains(searchTerm.toUpperCase())) {
                    jobs.add(row);   //*******
                    //add a break to end your search (you already found an entry with the search term, you don't
                    //want to keep looking for the same term in the same HashMap otherwise you'll return the same
                    //HashMap twice.
                    break;

                }
            }

        }
        return jobs;   //*******
    }
    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

    }

